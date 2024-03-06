package system;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

public class Common {

    public static void Skip(Project project, String filePath){
        if (project != null) {
            VirtualFile file = LocalFileSystem.getInstance().findFileByPath(filePath); // 获取文件对象
            if (file != null) {
                FileEditorManager.getInstance(project).openFile(file, true); // 打开文件
                Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor(); // 获取当前活动的编辑器
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
