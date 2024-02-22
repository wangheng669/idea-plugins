import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

public class LogAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject(); // 获取当前项目
        if (project != null) {
            String filePath = "/Users/wangheng/Desktop/笔记/日志.md"; // 使用绝对路径
            VirtualFile file = LocalFileSystem.getInstance().findFileByPath(filePath); // 获取文件对象
            if (file != null) {
                FileEditorManager.getInstance(project).openFile(file, true); // 打开文件

                // 获取当前活动的编辑器
                Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
                if (editor == null) {
                    return;
                }

                PsiFile currentFile = PsiUtilBase.getPsiFileInEditor(editor, project);
                if (currentFile == null) {
                    return;
                }

                editor.getCaretModel().moveToOffset(currentFile.getTextLength()); // 将光标移动到文档末尾
                editor.getScrollingModel().scrollVertically(editor.getContentComponent().getHeight() - editor.getScrollingModel().getVisibleArea().height);
            }
        }
    }
}
