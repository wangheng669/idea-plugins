import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import java.net.*;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;


public class UpdateCodeAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            sendPost(e.getPresentation().getText());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void sendPost(String project_name) throws IOException {
        Notifications.Bus.notify(new Notification("System Messages", project_name+"代码中", "请稍等", NotificationType.INFORMATION));
        new Thread(new Runnable(){
            @Override
            public void run() {
                String json = String.format("{\"message\": \"%s\"}", Objects.equals(project_name, "更新聊缘") ? 2 : 1);
                String message = CommonUtils.request(json,"http://127.0.0.1:8080/updatecode");
                Notifications.Bus.notify(new Notification("System Messages", project_name+"代码完成", message, NotificationType.INFORMATION));
            }
        }).start();
    }

}
