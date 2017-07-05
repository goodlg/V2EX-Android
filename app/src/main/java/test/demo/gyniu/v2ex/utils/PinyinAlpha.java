package test.demo.gyniu.v2ex.utils;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * Created by uiprj on 17-7-5.
 */
public class PinyinAlpha {
    static public String getFirstChar(String value) {
        char firstChar = value.charAt(0);
        String first = null;
        String[] print = PinyinHelper.toHanyuPinyinStringArray(firstChar);
        if (print == null) {
            if ((firstChar >= 97 && firstChar <= 122)) {
                firstChar -= 32;
            }
            if (firstChar >= 65 && firstChar <= 90) {
                first = String.valueOf(firstChar);
            } else {
                first = "#";
            }
        } else {
            first = String.valueOf((char) (print[0].charAt(0) - 32));
        }
        if (first == null) {
            first = "?";
        }
        return first;
    }
}
