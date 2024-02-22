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
                OkHttpClient client = new OkHttpClient();
                String json = String.format("{\"project_name\": \"%s\"}", project_name);
                RequestBody requestBody = RequestBody.create(Objects.requireNonNull(MediaType.parse("application/json; charset=utf-8")), json);
                Request request = new Request.Builder()
                        .url("http://127.0.0.1:8080")
                        .post(requestBody)
                        .build();
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    String responseData = response.body().string();
                    Notifications.Bus.notify(new Notification("System Messages", project_name+"代码完成", responseData, NotificationType.INFORMATION));
                    System.out.println(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
