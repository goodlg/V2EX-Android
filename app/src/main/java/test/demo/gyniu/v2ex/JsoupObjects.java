package test.demo.gyniu.v2ex;

import org.jsoup.nodes.Element;

import java.util.Iterator;

/**
 * Created by uiprj on 17-5-4.
 */
public class JsoupObjects implements Iterable<Element>{

    private Sequence<Element> mResult;

    public JsoupObjects(Element elements) {
        mResult = elements.asSequence();
    }

    @Override
    public Iterator<Element> iterator() {
        return null;
    }
}
