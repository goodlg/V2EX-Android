package test.demo.gyniu.v2ex.loader;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import test.demo.gyniu.v2ex.utils.JsoupObjects;
import test.demo.gyniu.v2ex.model.Avatar;
import test.demo.gyniu.v2ex.model.UserProfile;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by gyniu on 17-6-17.
 */
public class MyselfParser extends ParserHelper {
    private static final String TAG = "MyselfParser";
    private static final boolean DEBUG = LogUtil.LOGD;

    public static UserProfile parseLoginResult(Document doc) {
        Element box = new JsoupObjects(doc).body().child("#Wrapper").child(".content")
                .child("#Rightbar").dfs(".box").getOne();
        Element tr = new JsoupObjects(box).dfs("tr").getOne();
        //find account
        final String account = new JsoupObjects(tr).child("td").child(".bigger").child("a")
                .getOne().text();
        //get avatar
        final String url = new JsoupObjects(tr).dfs(".avatar").getOne().attr("src");
        final Avatar avatar = new Avatar.Builder().setUrl(url).createAvatar();

        //get all links
        JsoupObjects alinks = new JsoupObjects(box).dfs("a");
        int found = 0;
        int nodes = 0, topics = 0, followings = 0, silver = 0, bronze= 0, remind = 0;
        for (Element e : alinks) {
            if (found == 5) break;
            //nodes
            String str = e.toString();
            if (str.contains("/my/nodes")) {
                nodes = Integer.parseInt(new JsoupObjects(e).child(".bigger").getOne().text());
                found++;
            } else if (str.contains("/my/topics")) {//topics
                topics = Integer.parseInt(new JsoupObjects(e).child(".bigger").getOne().text());
                found++;
            } else if (str.contains("/my/following")) {//following
                followings = Integer.parseInt(new JsoupObjects(e).child(".bigger").getOne().text());
                found++;
            } else if (str.contains("/balance")) {//following
                String[] text = e.text().split(" ");
                silver = Integer.parseInt(text[0]);
                bronze = Integer.parseInt(text[1]);
                found++;
            } else if (str.contains("/notifications")) {//notifications
                String[] text = e.text().split(" ");
                remind = Integer.parseInt(text[0]);
                found++;
            }
        }

        return new UserProfile(account, avatar, nodes , topics,
                followings, remind, silver, bronze);
    }
}
