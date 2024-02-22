import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class UpdateMoAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject(); // 获取当前项目
        try {
            sendPost(project);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void sendPost(Project project) throws IOException {
        Notifications.Bus.notify(new Notification("System Messages", "更新聊欢代码中", "请稍等", NotificationType.INFORMATION));
        new Thread(new Runnable(){
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL("http://10.75.2.225:8080");
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                HttpURLConnection con = null;
                try {
                    con = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    con.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    throw new RuntimeException(e);
                }
                int responseCode = 0;
                try {
                    responseCode = con.getResponseCode();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Response Code: " + responseCode);
                BufferedReader in = null;
                try {
                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String inputLine;
                StringBuilder response = new StringBuilder();
                while (true) {
                    try {
                        if (!((inputLine = in.readLine()) != null)) break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    response.append(inputLine);
                }
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Notifications.Bus.notify(new Notification("System Messages", "更新聊欢代码完成", response.toString(), NotificationType.INFORMATION));
            }
        }).start();


    }

}
