package translate;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class TransStatusBarWidget implements StatusBarWidget, StatusBarWidget.TextPresentation {
    private String myText;

    public TransStatusBarWidget() {
        myText = "翻译";
    }

    public void updateText(@NotNull String newText, Project project) {

    }

    @Override
    public String ID() {
        return "translate.TransStatusBarWidget";
    }

    @Override
    public WidgetPresentation getPresentation(@NotNull PlatformType type) {
        return this;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        // 在这里可以添加一些初始化代码
    }

    @Override
    public void dispose() {
        // 在这里可以添加一些清理代码
    }

    @NotNull
    @Override
    public String getText() {
        return myText;
    }
    @Override
    public float getAlignment() {
        return Component.CENTER_ALIGNMENT;
    }

    @Override
    public String getTooltipText() {
        return "翻译";
    }

}
