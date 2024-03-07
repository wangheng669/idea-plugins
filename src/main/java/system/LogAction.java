package system;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
public class LogAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Common.Skip(e.getProject(),"/Users/wangheng/Desktop/笔记/日志.md");
    }
}
