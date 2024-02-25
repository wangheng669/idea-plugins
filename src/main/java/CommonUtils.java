import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonUtils {
    public static String getBaiduMD5(String p, String randomString) {
        String flexString = TransConfig.BD_APP_ID + p + randomString + TransConfig.BD_APP_SEC;
        return md5(flexString);
    }
    public static String getYouDaoMD5(String p, String randomString) {
        String flexString = TransConfig.YD_APP_ID + p + randomString + TransConfig.YD_APP_KEY;
        return md5(flexString);
    }

    //默认不需要samples
    public static String getBingUrl(String word) {
        return getBingUrl(word, false);
    }

    public static String getBingUrl(String word, boolean needSample) {
        if (needSample) {
            return TransConfig.BING_URL + "?Word=" + word;
        } else {
            return TransConfig.BING_URL + "?Word=" + word + "&Samples=false";
        }
    }

    public static String md5(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new BigInteger(1, digest).toString(16);
    }

}
