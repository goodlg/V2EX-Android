package test.demo.gyniu.v2ex;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import test.demo.gyniu.v2ex.model.Entity;

/**
 * Created by uiprj on 17-5-4.
 */
public class TopicListParser extends ParserHelper{
    public static TopicListLoader.TopicList parseDoc(Document doc, Entity entity){
        final Element contentBox = new JsoupObjects(doc).bfs("body").child("#Wrapper")
                .child(".content").child("#Main").child(".box").getOne();

        return parseDocForTab(contentBox);
    }
}
