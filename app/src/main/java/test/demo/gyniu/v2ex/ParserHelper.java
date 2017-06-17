package test.demo.gyniu.v2ex;

import com.google.common.base.Preconditions;

import junit.framework.Assert;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import test.demo.gyniu.v2ex.model.SignInForm;

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

    public static SignInForm parseSignInForm(String html) {
        final Document doc = toDoc(html);
        Element form = new JsoupObjects(doc).body().child("#Wrapper").child(".content")
                .child(".box").child(".cell").dfs("form").getOne();
        Element account = new JsoupObjects(form).dfs("input[type=text]").getOne();
        Element once = new JsoupObjects(form).dfs("input[name=once]").getOne();
        Element passwd = once.nextElementSibling();

        Assert.assertEquals("input", passwd.tagName());

        return new SignInForm(account.attr("name"), passwd.attr("name"), once.val());
    }


}
