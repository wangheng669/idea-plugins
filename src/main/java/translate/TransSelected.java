package translate;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import org.apache.http.util.TextUtils;
import java.util.regex.Pattern;
import translate.baidu.Translate;

public class TransSelected implements EditorMouseListener {


    public void mouseReleased(EditorMouseEvent e){
        Editor editor=e.getEditor();
        String selectedWord=editor.getSelectionModel().getSelectedText();
        if(TextUtils.isEmpty(selectedWord)) return;
        if(selectedWord.contains("\n")) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                translate(selectedWord,editor);
            }
        }).start();
    }

    public void translate(String selectedWord, Editor editor) {
        String to = containsChinese(selectedWord) ? "en" : "zh";
        if(!containsChinese(selectedWord)){ // 非中文处理
            selectedWord = selectedWord.replaceAll("[^a-z^A-Z^0-9^ ^_^(^)]", "");
            if (TextUtils.isBlank(selectedWord) || selectedWord.length() < 2) return;
            selectedWord = selectedWord.replaceAll("_", " ");
            selectedWord = parseWord(selectedWord); // 驼峰
        }
        Translate.TransByBaidu(selectedWord, editor,to);
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

    public static boolean containsChinese(String str) {
        String regex = "[\\u4e00-\\u9fa5]";
        return Pattern.compile(regex).matcher(str).find();
    }



}