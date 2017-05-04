package test.demo.gyniu.v2ex;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by uiprj on 17-5-4.
 */
public abstract class ParserHelper {
    enum type {
        Tab,
        Node,
        Topic
    }

    public static Document toDoc(String html){
        return Jsoup.parse(html);
    }
}
