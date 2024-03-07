package codetree;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import net.minidev.json.JSONObject;
import org.apache.http.util.TextUtils;

import javax.swing.*;

public class GoCodeTree implements EditorMouseListener {

    public void mouseReleased(EditorMouseEvent e){
        Editor editor=e.getEditor();
        new Thread(new Runnable() {
            @Override
            public void run() {
                codeTree(editor);
            }
        }).start();
    }

    public void codeTree(Editor editor) {
        String treeResult = FileWindow.goCreateTree(editor);
        FileWindow.UpdateToolWindowContent(editor.getProject(),treeResult);
    }

}