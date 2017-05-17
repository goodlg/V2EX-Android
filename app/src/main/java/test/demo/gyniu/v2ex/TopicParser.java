package test.demo.gyniu.v2ex;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import test.demo.gyniu.v2ex.model.Avatar;
import test.demo.gyniu.v2ex.model.Comment;
import test.demo.gyniu.v2ex.model.Member;
import test.demo.gyniu.v2ex.model.Postscript;
import test.demo.gyniu.v2ex.model.Topic;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-5-16.
 */
public class TopicParser extends ParserHelper {
    private static final String TAG = "TopicParser";
    private static final boolean DEBUG = LogUtil.LOGD;
    private static final Pattern PATTERN_TOPIC_POST_TIME = Pattern.compile("·\\s*(.+?)(?:\\s+·|$)");
    private static final Pattern PATTERN_POSTSCRIPT = Pattern.compile("·\\s+(.+)");
    private static final Pattern PATTERN_NUMBERS = Pattern.compile("\\d+");

    public static TopicWithComments parseDoc(Document doc, Topic topic) {
        if (DEBUG) LogUtil.w(TAG, "TopicParser parseDoc");
        final Topic.Builder topicBuilder = topic.toBuilder();
        final Element mainElement = new JsoupObjects(doc).bfs("body").child("#Wrapper")
                .child(".content").child("#Main").getOne();

        parseTopicInfo(topicBuilder, mainElement);

        List<Comment> comments = parseComments(mainElement);
        if (DEBUG) LogUtil.e(TAG, "comments: " + comments);
        int[] pageNum = getMaxPage(mainElement);

        return new TopicWithComments(topicBuilder.createTopic(), comments, pageNum[0],
                pageNum[1], null, null);
    }

    private static int[] getMaxPage(Element main) {
        final Optional<Element> optional = new JsoupObjects(main)
                .child(".box:nth-child(4):not(.transparent)")
                .child(".cell:last-child:not([id])").dfs(".page_input").getOptional();
        if (optional.isPresent()) {
            final Element element = optional.get();
            final int maxPage = Integer.parseInt(element.attr("max"));
            final int curPage = Integer.parseInt(element.attr("value"));

            return new int[]{curPage, maxPage};
        } else {
            return new int[]{1, 1};
        }
    }

    private static void parseTopicInfo(Topic.Builder builder, Element main) {
        final Element topicBox = JsoupObjects.child(main, ".box");
        final Element header = JsoupObjects.child(topicBox, ".header");

        if (DEBUG) LogUtil.w(TAG, "builder: " + builder);

        parseTopicPostTime(builder, JsoupObjects.child(header, ".gray").textNodes().get(0));
        parseTopicClickRate(builder, JsoupObjects.child(header, ".gray").textNodes().get(0));
        parseTopicTitle(builder, header);
        parseTopicContent(builder, topicBox);
        parsePostscript(builder, topicBox);

        if (!builder.hasInfo()) {
            TopicListParser.parseMember(builder, JsoupObjects.child(header, ".fr"));
        }
    }

    private static void parseTopicPostTime(Topic.Builder builder, TextNode textNode) {
        final String text = textNode.text();
        final Matcher matcher = PATTERN_TOPIC_POST_TIME.matcher(text);
        if (!matcher.find()) {
            throw new RuntimeException("match post time for topic failed: " + text);
        }
        final String time = matcher.group(1);
        builder.setTime(time);
    }

    private static void parseTopicTitle(Topic.Builder builder, Element header) {
        final String title = JsoupObjects.child(header, "h1").html();
        builder.setTitle(title);
    }

    private static void parseTopicContent(Topic.Builder builder, Element topicBox) {
        final Optional<Element> optional = new JsoupObjects(topicBox)
                .child(".cell")
                .child(".topic_content")
                .getOptional();
        if (optional.isPresent()) {
            final Element element = optional.get();
            builder.setContent(element.html());
        }
    }

    private static void parsePostscript(Topic.Builder builder, Element topicBox) {
        final Iterable<Element> elements = new JsoupObjects(topicBox).child(".subtle");
        final Iterable<Postscript> subtles = Iterables.transform(elements, ele -> {
            final Element fade = JsoupObjects.child(ele, ".fade");
            final Matcher matcher = PATTERN_POSTSCRIPT.matcher(fade.text());
            Preconditions.checkArgument(matcher.find());
            final String time = matcher.group(1);

            final String content = JsoupObjects.child(ele, ".topic_content").html();

            return new Postscript(content, time);
        });

        builder.setPostscripts(Lists.newArrayList(subtles));
    }

    private static void parseTopicClickRate(Topic.Builder builder, TextNode textNode) {
        final String text = textNode.text();
        final Matcher matcher = PATTERN_NUMBERS.matcher(text);
        if (!matcher.find()) {
            throw new RuntimeException("match click rate for topic failed: " + text);
        }
        final int clickRate = Integer.parseInt(matcher.group(2));
        builder.setCount(clickRate);
    }

    private static List<Comment> parseComments(Element main) {
        final JsoupObjects elements = new JsoupObjects(main).child(":nth-child(4)").child(".cell[id]").child("table").child("tbody").child("tr");
        return Lists.newArrayList(Iterables.transform(elements, (ele) -> {
            final Avatar.Builder avatarBuilder = new Avatar.Builder();
            parseAvatar(avatarBuilder, ele);

            final Element td = JsoupObjects.child(ele, "td:nth-child(3)");

            final Member.Builder memberBuilder = new Member.Builder();
            memberBuilder.setAvatar(avatarBuilder.createAvatar());
            parseMember(memberBuilder, td);

            final Comment.Builder commentBuilder = new Comment.Builder();
            commentBuilder.setMember(memberBuilder.createMember());

            parseCommentInfo(commentBuilder, td);
            parseCommentContent(commentBuilder, td);

            return commentBuilder.createComment();
        }));
    }

    private static void parseAvatar(Avatar.Builder builder, Element ele) {
        builder.setUrl(new JsoupObjects(ele).dfs(".avatar").getOne().attr("src"));
    }

    private static void parseMember(Member.Builder builder, Element ele) {
        builder.setUserName(new JsoupObjects(ele).bfs(".dark").getOne().text());
    }

    private static void parseCommentInfo(Comment.Builder builder, Element ele) {
        final Element tableEle = JsoupObjects.parents(ele, "div");
        // example data: r_123456
        final int id = Integer.parseInt(tableEle.id().substring(2));
        builder.setId(id);

        final Element fr = JsoupObjects.child(ele, ".fr");
        builder.setFloor(Integer.parseInt(JsoupObjects.child(fr, ".no").text()));

        final List<Element> elements = new JsoupObjects(ele).child(".small").getList();

        final Element timeEle = elements.get(0);
        builder.setReplyTime(timeEle.text());
    }

    private static void parseCommentContent(Comment.Builder builder, Element ele) {
        builder.setContent(JsoupObjects.child(ele, ".reply_content").html());
    }
}
