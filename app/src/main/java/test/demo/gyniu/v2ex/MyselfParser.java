package test.demo.gyniu.v2ex;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import test.demo.gyniu.v2ex.model.Avatar;
import test.demo.gyniu.v2ex.model.LoginResult;

/**
 * Created by gyniu on 17-6-17.
 */
public class MyselfParser extends ParserHelper {

    public static LoginResult parseLoginResult(Document doc) {
        Element tr = new JsoupObjects(doc).body().child("#Wrapper").child(".content")
                .child("#Rightbar").dfs("tr").getOne();

        final String url = new JsoupObjects(tr).dfs(".avatar").getOne().attr("src");
        final Avatar avatar = new Avatar.Builder().setUrl(url).createAvatar();

        final String username = new JsoupObjects(tr).child("td").child(".bigger").child("a")
                .getOne().text();
        return new LoginResult(username, avatar);
    }
}
