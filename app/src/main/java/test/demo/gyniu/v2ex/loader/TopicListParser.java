package test.demo.gyniu.v2ex.loader;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import test.demo.gyniu.v2ex.model.Tab;
import test.demo.gyniu.v2ex.utils.JsoupObjects;
import test.demo.gyniu.v2ex.model.Avatar;
import test.demo.gyniu.v2ex.model.Entity;
import test.demo.gyniu.v2ex.model.Member;
import test.demo.gyniu.v2ex.model.Node;
import test.demo.gyniu.v2ex.model.Topic;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-5-4.
 * parser of topic list
 */
public class TopicListParser extends ParserHelper{
    private static final String TAG = "TopicListParser";
    private static final boolean DEBUG = LogUtil.LOGD;
    private static final Pattern PATTERN_REPLY_TIME = Pattern.compile("•\\s*(.+?)(?:\\s+•|$)");

    public static TopicListLoader.TopicList parseDoc(Document doc, Entity entity){
        final Element contentBox = new JsoupObjects(doc).bfs("body").child("#Wrapper")
                .child(".content").child("#Main").child(".box").getOne();
        if (entity instanceof Node) {
            return parseDocForNode(contentBox, entity);
        } else if (entity instanceof Tab) {
            return parseDocForTab(contentBox, entity);
        } else {
            throw new IllegalArgumentException("unknown entity type: " + entity);
        }
    }

    private static TopicListLoader.TopicList parseDocForNode(Element contentBox, Entity entity) {
        final JsoupObjects elements = new JsoupObjects(contentBox).child("#TopicsNode")
                .child(".cell").child("table").child("tbody").child("tr");
        List lists = Lists.newArrayList(Iterables.transform(elements,
                e -> parseItemForEntity(e, entity)));
        int[] pageNum = getMaxPage(contentBox);
        return new TopicListLoader.TopicList(lists, pageNum[0], pageNum[1], false, null);
    }

    private static TopicListLoader.TopicList parseDocForTab(Element contentBox, Entity entity) {
        final JsoupObjects elements = new JsoupObjects(contentBox).child(".item")
                .child("table").child("tbody").child("tr");
        List lists = Lists.newArrayList(Iterables.transform(elements,
                e -> parseItemForEntity(e, entity)));
        int[] pageNum = getMaxPage(contentBox);
        return new TopicListLoader.TopicList(lists, pageNum[0], pageNum[1], false, null);
    }

    private static int[] getMaxPage(Element main) {
        final Optional<Element> optional = new JsoupObjects(main)
                .child(".cell").dfs(".page_input").getOptional();
        if (optional.isPresent()) {
            final Element element = optional.get();
            final int maxPage = Integer.parseInt(element.attr("max"));
            final int curPage = Integer.parseInt(element.attr("value"));

            LogUtil.d(TAG, " @@@@@ getMaxPage maxPage:" + maxPage + ", curPage:" + curPage);

            return new int[]{curPage, maxPage};
        } else {
            LogUtil.d(TAG, " @@@@@ getMaxPage 2...");
            return new int[]{1, 1};
        }
    }

    private static Topic parseItemForEntity(Element item, Entity entity) {
        final Elements list = item.children();

        final Topic.Builder topicBuilder = new Topic.Builder();
        parseMember(topicBuilder, list.get(0));

        final Element ele = list.get(2);
        parseTitle(topicBuilder, ele);
        if (entity instanceof Node) {
            parseInfo(topicBuilder, ele, (Node)entity);
        } else if (entity instanceof Tab) {
            parseInfo(topicBuilder, ele, null);
        } else {
            parseInfo(topicBuilder, ele, null);
        }

        parseReplyCount(topicBuilder, list.get(3));

        return topicBuilder.createTopic();
    }

    static void parseMember(Topic.Builder builder, Element ele) {
        final Member.Builder memberBuilder = new Member.Builder();

        // get member url
        ele = ele.child(0);
        Preconditions.checkState(ele.tagName().equals("a"));
        final String url = ele.attr("href");
        memberBuilder.setUserName(Member.getNameFromUrl(url));

        // get member avatar
        final Avatar.Builder avatarBuilder = new Avatar.Builder();
        ele = ele.child(0);
        Preconditions.checkState(ele.tagName().equals("img"));
        avatarBuilder.setUrl(ele.attr("src"));
        memberBuilder.setAvatar(avatarBuilder.createAvatar());

        builder.setMember(memberBuilder.createMember());
    }

    private static void parseTitle(Topic.Builder topicBuilder, Element ele) {
        ele = new JsoupObjects(ele).child(".item_title").child("a").getOne();
        String url = ele.attr("href");
        topicBuilder.setId(Topic.getIdFromUrl(url));
//        List<Element> imgs = new JsoupObjects(ele).child("img").getList();
//        for (Element e : imgs) {
//            String imgUrl = e.attr("src");
//            if (imgUrl != null) {
//                if (imgUrl.trim().startsWith("/")) {
//                    imgUrl = Constant.BASE_URL + imgUrl;
//                    e.attr("src", imgUrl);
//                }
//            }
//        }
        topicBuilder.setTitle(ele.html());
    }

    private static void parseInfo(Topic.Builder builder, Element ele, Node node) {
        ele = JsoupObjects.child(ele, ".fade");

        boolean hasNode = false;
        if (node == null) {
            node = parseNode(JsoupObjects.child(ele, ".node"));
        } else {
            hasNode = true;
        }

        builder.setNode(node);

        final int index = hasNode ? 0 : 1;
        if (ele.textNodes().size() > index) {
            parseReplyTime(builder, ele.textNodes().get(index));
        } else {
            // reply time may not exists
            builder.setTime("");
        }
    }

    private static Node parseNode(Element nodeEle) {
        final String title = nodeEle.text();
        final String url = nodeEle.attr("href");
        final String name = Node.getNameFromUrl(url);
        return new Node.Builder().setTitle(title).setName(name).createNode();
    }

    private static void parseReplyCount(Topic.Builder topicBuilder, Element ele) {
        final Elements children = ele.children();
        final int count;
        if (children.size() > 0) {
            final String numStr = ele.child(0).text();
            count = Integer.parseInt(numStr);
        } else {
            // do not have reply yet
            count = 0;
        }
        topicBuilder.setReplyCount(count);
    }

    private static void parseReplyTime(Topic.Builder topicBuilder, TextNode textNode) {
        final String text = textNode.text();
        final Matcher matcher = PATTERN_REPLY_TIME.matcher(text);
        if (!matcher.find()) {
            throw new RuntimeException("match reply time for topic failed: " + text);
        }
        final String time = matcher.group(1);
        topicBuilder.setTime(time);
    }
}
