import java.util.Arrays;
import java.util.List;

public class DomTree {
    public static List<NodeTree> createTree(String[] paths) {
        NodeTree root = new NodeTree("");
        for (String path : paths) {
            buildTree(root, path.split("/"), 10);
        }
        return root.getChildren();
    }

    private static void buildTree(NodeTree node, String[] segments, double line) {
        if (segments.length == 0) {
            node.setLine(line);
            return;
        }
        String name = segments[0];
        NodeTree childNode = null;
        for (NodeTree child : node.getChildren()) {
            if (child.getName().equals(name)) {
                childNode = child;
                break;
            }
        }
        if (childNode == null) {
            childNode = new NodeTree(name);
            node.getChildren().add(childNode);
        }
        buildTree(childNode, Arrays.copyOfRange(segments, 1, segments.length), line);
    }
}
