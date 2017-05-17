package test.demo.gyniu.v2ex;

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

import test.demo.gyniu.v2ex.model.Avatar;
import test.demo.gyniu.v2ex.model.Entity;
import test.demo.gyniu.v2ex.model.Member;
import test.demo.gyniu.v2ex.model.Topic;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-5-4.
 */
public class TopicListParser extends ParserHelper{
    private static final String TAG = "TopicListParser";
    private static final boolean DEBUG = LogUtil.LOGD;
    private static final Pattern PATTERN_REPLY_TIME = Pattern.compile("•\\s*(.+?)(?:\\s+•|$)");

    public static TopicListLoader.TopicList parseDoc(Document doc, Entity entity){
        final Element contentBox = new JsoupObjects(doc).bfs("body").child("#Wrapper")
                .child(".content").child("#Main").child(".box").getOne();

        return parseDocForTab(contentBox);
    }

    private static TopicListLoader.TopicList parseDocForTab(Element contentBox) {
        final JsoupObjects elements = new JsoupObjects(contentBox).child(".item")
                .child("table").child("tbody").child("tr");
        List lists = Lists.newArrayList(Iterables.transform(elements,
                TopicListParser::parseItemForTab));

        if (DEBUG) {
            for(int i=0; lists!=null && i<lists.size(); i++){
                Topic topic = (Topic)lists.get(i);
                LogUtil.d(TAG, "Topic : " + topic);
            }
        }

        return new TopicListLoader.TopicList(lists, false, null);
    }

    private static Topic parseItemForTab(Element item) {
        final Elements list = item.children();

        final Topic.Builder topicBuilder = new Topic.Builder();
        parseMember(topicBuilder, list.get(0));

        final Element ele = list.get(2);
        parseTitle(topicBuilder, ele);
        parseInfo(topicBuilder, ele);

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
        topicBuilder.setTitle(ele.html());
    }

    private static void parseInfo(Topic.Builder topicBuilder, Element ele) {
        ele = JsoupObjects.child(ele, ".fade");

        boolean hasNode = false;

        final int index = hasNode ? 0 : 1;
        if (ele.textNodes().size() > index) {
            parseReplyTime(topicBuilder, ele.textNodes().get(index));
        } else {
            // reply time may not exists
            topicBuilder.setTime("");
        }
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
        topicBuilder.setCount(count);
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
