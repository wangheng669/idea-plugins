import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Objects;


public class UpdateCodeAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject(); // 获取当前项目
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
                String message = CommonUtils.request(json,"http://10.75.2.255:8080");
                Notifications.Bus.notify(new Notification("System Messages", project_name+"代码完成", message, NotificationType.INFORMATION));
            }
        }).start();
    }

}
