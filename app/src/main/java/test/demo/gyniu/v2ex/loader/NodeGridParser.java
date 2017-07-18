package test.demo.gyniu.v2ex.loader;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

import test.demo.gyniu.v2ex.BuildConfig;
import test.demo.gyniu.v2ex.model.Avatar;
import test.demo.gyniu.v2ex.model.Node;
import test.demo.gyniu.v2ex.utils.JsoupObjects;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by gyniu on 17-7-13.
 */
public class NodeGridParser extends ParserHelper {
    public static List<Node> parseFavNodes(Document doc) {
        final JsoupObjects elements = new JsoupObjects(doc).body().child("#Wrapper")
                .child(".content").child("#Main").child(".box").child("#MyNodes").child("a");

        return Lists.newArrayList(Iterables.transform(elements, ele -> {
            LogUtil.d("___ngy", "____aaaa");
            final Element ele1 = ele.children().get(0).children().get(0);//img
            //Element ele1 = new JsoupObjects(ele).child("img");
            LogUtil.d("___ngy", "____bbbb");

            final String text = ele.text();
            String[] str = text.split(" ");//Steam 396
            Preconditions.checkNotNull(str);
            final String title = str[0];
            final String url = ele.attr("href");
            final String name = Node.getNameFromUrl(url);
            final String imgUrl = ele1.attr("src");
            final Avatar avatar = new Avatar.Builder().setUrl(imgUrl).createAvatar();
            final int topics = Integer.parseInt(str[1]);
            if (BuildConfig.DEBUG) LogUtil.d("___ngy", "____ title:" + title
                    + ", name:" + name
                    + ", imgUrl:" + imgUrl
                    + ", topics:" + topics);
            return new Node.Builder()
                    .setTitle(title)
                    .setName(name)
                    .setAvatar(avatar)
                    .setTopics(topics)
                    .createNode();
        }));
    }

}
