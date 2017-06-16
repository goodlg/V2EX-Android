package test.demo.gyniu.v2ex;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

    public static String parseOnceCode(String html) {
        final Document doc = toDoc(html);
        Element ele = new JsoupObjects(doc).body().child("#Wrapper").child(".content")
                .child("#Main").child(".box").child(".cell").dfs("form").dfs("[name=once]").getOne();

        return ele.val();
    }
}
