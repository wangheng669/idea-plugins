import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class MyToolWindow {

    private JPanel myToolWindowContent;

    public MyToolWindow(ToolWindow toolWindow) {
        toolWindow.setStripeTitle("当日开发文件");
    }

    public static String goCreateTree(Editor editor){
        String fileName = editor.getVirtualFile().getPath();
        String baseDir = editor.getProject().getBasePath();
        int lineNumber = editor.getCaretModel().getLogicalPosition().line +1;
        boolean isFileInProject = fileName.startsWith(baseDir);
        if (isFileInProject) {
            fileName = fileName.replace(baseDir,"");
            String jsonString = String.format("{\"file_name\": \"%s\", \"line\": %d, \"base_dir\": \"%s\"}", fileName, lineNumber, baseDir);
            CommonFile.Save("test3",fileName+":"+String.valueOf(lineNumber));
            List<NodeTree> nodeTrees = DomTree.createTree(CommonFile.readFileToArray("test3.txt"));
            for (NodeTree nodeTree : nodeTrees) {
                System.out.println(nodeTree.getName());
            }
            return CommonUtils.request(jsonString,"http://127.0.0.1:8080/codetree");
        }
        System.out.println("当前文件不在项目下.");
        return "";
    }

    public static void UpdateToolWindowContent(Project project, String treeResult) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow toolWindow = toolWindowManager.getToolWindow("MyToolWindowFactory");
        if (toolWindow != null) {
            // 在这里，你可以更新工具窗口的内容，例如设置文本标签的新文本
            SwingUtilities.invokeLater(() -> {
                MyToolWindow myToolWindow = new MyToolWindow(toolWindow);
                toolWindow.getComponent().removeAll();
                toolWindow.getComponent().add(myToolWindow.createTree(treeResult,project));
                toolWindow.getComponent().updateUI();
            });
        }
    }

    public JPanel createTree(String treeResult, Project project){
        DefaultMutableTreeNode rootTree = null;
        ObjectMapper objectMapper = new ObjectMapper();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("...");
        try {
            JsonNode rootNode = objectMapper.readTree(treeResult);
            rootTree = traverse(rootNode,root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        Tree tree = new Tree(new DefaultTreeModel(rootTree));
        expandAll(tree,new TreePath(rootTree),true);
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 如果在这棵树上点击了2次,即双击
                if (e.getSource() == tree) {
                    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                    if (selPath != null)// 谨防空指针异常!双击空白处是会这样
                    {
                        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                        if (selectedNode != null) {
                            NodeData userObject = (NodeData) selectedNode.getUserObject();
                            String replacedPath = selPath.toString().replace(", ", "/");
                            replacedPath = replacedPath.replace("...",project.getBasePath());
                            replacedPath = replacedPath.substring(1, replacedPath.length() - 1); // 去掉开头和结尾的方括号
                            replacedPath = "/" + replacedPath; // 添加最开始的斜杠
                            CommonUtils.SkipFile(project,replacedPath,userObject.getLine());
                        }
                    }
                }

            }
        });
        tree.setCellRenderer(new CustomTreeCellRenderer());
        JBScrollPane scrollPane = new JBScrollPane(tree);
        contentPanel.add(scrollPane);
        return contentPanel;
    }

    private static DefaultMutableTreeNode traverse(JsonNode node, DefaultMutableTreeNode root) {
        if (node.isArray()) {
            for (JsonNode childNode : node) {
                traverse(childNode,root);
            }
        } else {
            JsonNode nameNode = node.get("name");
            JsonNode childrenNode = node.get("children");
            JsonNode line = node.get("line");
            if (nameNode != null) { // 是目录
                DefaultMutableTreeNode node_children_tree = new DefaultMutableTreeNode(nameNode.asText());
                root.add(node_children_tree);
                node_children_tree.setUserObject(new NodeData(nameNode.asText(), childrenNode != null ? AllIcons.Nodes.Folder : AllIcons.Nodes.Function,line.asInt())); // 使用IntelliJ IDEA自带的文件夹图标
                if (childrenNode != null && childrenNode.isArray()) {
                    for (JsonNode child : childrenNode) {
                        traverse(child,node_children_tree);
                    }
                }
            }
        }
        return root;
    }

    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() > 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    public JPanel getContent(Project project) {
        String baseDir = project.getBasePath();
        String jsonString = String.format("{\"base_dir\": \"%s\"}", baseDir);
        String treeResult = CommonUtils.request(jsonString,"http://127.0.0.1:8080/codetree");
        return createTree(treeResult,project);
    }

}

// 自定义树节点渲染器
class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        // 获取节点对象
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        if (node.getUserObject() instanceof NodeData) {
            NodeData nodeData = (NodeData) node.getUserObject();
            setIcon(nodeData.getIcon()); // 设置节点图标
        }

        return this;
    }
}

// 自定义节点数据类，用于存储节点名称和图标
class NodeData {
    private String name;
    private Icon icon;
    private Integer line;

    public NodeData(String name, Icon icon,Integer line) {
        this.name = name;
        this.icon = icon;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public Icon getIcon() {
        return icon;
    }

    public Integer getLine() {
        return line;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}