package system;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

public class DayTaskAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Common.Skip(e.getProject(),"/Users/wangheng/Desktop/学习/每日任务.md");
    }
}
