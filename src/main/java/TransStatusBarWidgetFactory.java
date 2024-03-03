import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;

public class TransStatusBarWidgetFactory implements StatusBarWidgetFactory {
    @Override
    public String getId() {
        return "TransStatusBarWidget";
    }

    @Override
    public @NotNull @NlsContexts.ConfigurableName String getDisplayName() {
        return "翻译";
    }

    @Override
    public boolean isAvailable(@NotNull Project project) {
        // 返回true表示小部件在给定项目中可用
        return true;
    }

    @Override
    public StatusBarWidget createWidget(@NotNull Project project) {
        return new TransStatusBarWidget();
    }

    @Override
    public void disposeWidget(@NotNull StatusBarWidget widget) {
        Disposer.dispose(widget);
    }

    @Override
    public boolean canBeEnabledOn(@NotNull StatusBar statusBar) {
        // 返回true表示小部件可以在给定的状态栏上启用
        return true;
    }
}

