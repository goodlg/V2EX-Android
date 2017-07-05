package test.demo.gyniu.v2ex.utils;

import java.util.Comparator;

import test.demo.gyniu.v2ex.model.Node;
import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * Created by uiprj on 17-7-5.
 */
public class PinyinComparator implements Comparator<Node> {
    public int compare(Node obj1, Node obj2) {
        String ostr1 = obj1.getTitle();
        String ostr2 = obj2.getTitle();

        for (int i = 0; i < ostr1.length() && i < ostr2.length(); i++) {
            int codePoint1 = ostr1.charAt(i);
            int codePoint2 = ostr2.charAt(i);
            if (Character.isSupplementaryCodePoint(codePoint1)
                    || Character.isSupplementaryCodePoint(codePoint2)) {
                i++;
            }
            if (codePoint1 != codePoint2) {
                if (Character.isSupplementaryCodePoint(codePoint1)
                        || Character.isSupplementaryCodePoint(codePoint2)) {
                    return codePoint1 - codePoint2;
                }
                String pinyin1 = pinyin((char) codePoint1);
                String pinyin2 = pinyin((char) codePoint2);

                if (pinyin1 != null && pinyin2 != null) {
                    if (!pinyin1.equals(pinyin2)) {
                        return pinyin1.compareTo(pinyin2);
                    }
                } else {
                    return codePoint1 - codePoint2;
                }
            }
        }
        return ostr1.length() - ostr2.length();
    }

    private String pinyin(char c) {
        String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c);
        if (pinyins == null) {
            return null;
        }
        return pinyins[0];
    }
}

