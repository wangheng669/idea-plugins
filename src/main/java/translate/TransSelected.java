package translate;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import org.apache.http.util.TextUtils;
import java.util.regex.Pattern;
import translate.youdao.Translate;

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
    public static String camelToUnderscore(String camelCase) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char currentChar = camelCase.charAt(i);
            if (Character.isUpperCase(currentChar) && i > 0) {
                result.append('_');
            }
            result.append(Character.toLowerCase(currentChar));
        }
        return result.toString();
    }

    public void translate(String selectedWord, Editor editor) {
        if(selectedWord.matches(".*[a-z].*")){
            selectedWord = camelToUnderscore(selectedWord); // 大写处理
        }else{
            selectedWord = selectedWord.toLowerCase();
        }
        System.out.println(selectedWord);
        String to = containsChinese(selectedWord) ? "en" : "zh";
        if(!containsChinese(selectedWord)){ // 非中文处理
            selectedWord = selectedWord.replaceAll("[^a-z^A-Z^0-9^ ^_^(^)]", "");
            if (TextUtils.isBlank(selectedWord) || selectedWord.length() < 2) return;
            selectedWord = selectedWord.replaceAll("_", " ");
            selectedWord = parseWord(selectedWord); // 驼峰
        }
        Translate.TransByYoudao(selectedWord, editor,to);
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