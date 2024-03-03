import baidu.HttpGet;
import baidu.MD5;
import com.google.gson.Gson;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import org.apache.http.util.TextUtils;
import entity.BaiduResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TransSelected implements EditorMouseListener {

    private Balloon waitBalloon;

    public void mouseReleased(EditorMouseEvent e){
        Editor editor=e.getEditor();
        String selectedWord=editor.getSelectionModel().getSelectedText();
        System.out.println(selectedWord);
        if(TextUtils.isEmpty(selectedWord)) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                translate(selectedWord,editor);
            }
        }).start();
    }

    public void translate(String text, Editor editor) {
        if (TextUtils.isEmpty(text)) return;
        String to;
        to = containsChinese(text) ? "en" : "zh";
        if(!containsChinese(text)){
            text = text.replaceAll("[^a-z^A-Z^0-9^ ^_^(^)]", "");
            if (TextUtils.isBlank(text) || text.length() < 2) return;
            text = text.replaceAll("_", " ");
            text = parseWord(text); //驼峰
        }
        System.out.println("parse后： " + text);
        transByBaidu(text, editor,to);
    }

    /**
     * 在大写字母前自动加入空格
     */
    private String parseWord(String text) {
        StringBuilder builder = new StringBuilder(text);
        for (int i = text.length() - 1; i > 0; i--) {
            if (Character.isUpperCase(text.charAt(i))) {
                builder.insert(i, " ");
            }
        }
        return builder.toString();
    }

    /**
     * 百度翻译，主要翻译句子
     */
    private void transByBaidu(String words, Editor editor,String to) {
        Map<String, String> params = buildParams(words, "auto", to);
        Gson gson = new Gson();
        BaiduResponseEntity baiduResponse = gson.fromJson(HttpGet.get(TransConfig.BD_URL, params), BaiduResponseEntity.class);
        showMessage(baiduResponse.trans_result.get(0).dst,editor);
    }

    public static boolean containsChinese(String str) {
        String regex = "[\\u4e00-\\u9fa5]";
        return Pattern.compile(regex).matcher(str).find();
    }

    private Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);
        params.put("appid", TransConfig.BD_APP_ID);
        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);
        // 签名
        String src = TransConfig.BD_APP_ID + query + salt + TransConfig.BD_APP_SEC; // 加密前的原文
        params.put("sign", MD5.md5(src));
        return params;
    }

    private Balloon balloon;
    private Balloon lastBalloon;


    private void showMessage(String message, Editor editor) {
        ApplicationManager.getApplication().invokeLater(() -> {
            if(waitBalloon!=null)
            {
                waitBalloon.hide();
            }
            JBPopupFactory jbPopupFactory = JBPopupFactory.getInstance();
            BalloonBuilder balloonBuilder = jbPopupFactory.createHtmlTextBalloonBuilder(message, null, new JBColor(
                    Gray._222
                    , Gray._77), null);
            balloonBuilder.setFadeoutTime(20000);
            balloon = balloonBuilder.createBalloon();

            if (lastBalloon != null) {
                lastBalloon.hide();
            }
            lastBalloon = balloon;
            balloon.setAnimationEnabled(false);
            balloon.show(jbPopupFactory.guessBestPopupLocation(editor), Balloon.Position.below);
        });
    }
}