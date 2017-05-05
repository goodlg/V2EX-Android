package test.demo.gyniu.v2ex;

import android.support.annotation.NonNull;
import android.util.LruCache;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.TreeTraverser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Evaluator;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-5-5.
 */
public class JsoupObjects implements Iterable<Element> {
    private static final String TAG = "JsoupObjects";
    private static final boolean DEBUG = LogUtil.LOGD;
    private static final Method PARSE_METHOD;
    private static final LruCache<String, Evaluator> EVALUATOR_LRU_CACHE;

    private FluentIterable<Element> mResult;

    static {
        try {
            // this class isn't public.
            final Class<?> queryParserCls = Class.forName("org.jsoup.select.QueryParser");
            final Method parseMethod = queryParserCls.getDeclaredMethod("parse", String.class);
            parseMethod.setAccessible(true);
            PARSE_METHOD = parseMethod;
        } catch (Exception e) {
            LogUtil.e(TAG, "Exception: load QueryParser falied");
            throw new RuntimeException("get QueryParser#parse failed", e);
        }
        EVALUATOR_LRU_CACHE = new LruCache<>(64);
    }

    public JsoupObjects(Element... elements) {
        mResult = FluentIterable.of(elements);
    }

    public JsoupObjects(Iterable<Element> elements) {
        mResult = FluentIterable.from(elements);
    }

    public Element getOne() {
        final Optional<Element> first = mResult.first();
        if (!first.isPresent()) {
            throw new NoSuchElementException();
        }

        return first.get();
    }

    public Optional<Element> getOptional() {
        return mResult.first();
    }

    public List<Element> getList() {
        return mResult.toList();
    }

    private Iterable<Element> filterByEvaluator(Iterable<Element> iterator, final Evaluator evaluator) {
        return Iterables.filter(iterator, new Predicate<Element>() {
            @Override
            public boolean apply(Element input) {
                return evaluator.matches(input, input);
            }
        });
    }

    private void addQuery(Function<Element, Iterable<Element>> getElements, Evaluator evaluator) {
        //noinspection ConstantConditions
        mResult = mResult.transformAndConcat(filterByEvaluator(getElements.));
    }

    public static Element child(Element ele, String query) {
        return new JsoupObjects(ele).child(query).getOne();
    }

    public JsoupObjects child(String query) {
        final Evaluator evaluator = parseQuery(query);
        Function<Element, Iterable<Element>> getElements = new Function<Element, Iterable<Element>>() {
            @Override
            public Iterable<Element> apply(Element input) {
                return null;
            }

            @Override
            public boolean equals(Object object) {
                return false;
            }
        };
        addQuery(getElements, evaluator);
        return this;
    }

    public JsoupObjects dfs(String query) {
        final Evaluator evaluator = parseQuery(query);
        addQuery(TREE_TRAVERSER::preOrderTraversal, evaluator);
        addQuery(TREE_TRAVERSER.)
        return this;
    }

    public JsoupObjects body() {
        return bfs("body");
    }

    @Override
    public Iterator<Element> iterator() {
        return mResult.iterator();
    }

    private static final TreeTraverser<Element> TREE_TRAVERSER = new TreeTraverser<Element>() {
        @Override
        public Iterable<Element> children(@NonNull Element root) {
            return root.children();
        }
    };

    private static final TreeTraverser<Element> PARENT_TRAVERSER = new TreeTraverser<Element>() {
        @Override
        public Iterable<Element> children(@NonNull Element root) {
            return Lists.newArrayList(root.parent());
        }
    };
}

