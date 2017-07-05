package test.demo.gyniu.v2ex.loader;

import android.content.Context;
import java.util.Collections;
import java.util.List;

import test.demo.gyniu.v2ex.BuildConfig;
import test.demo.gyniu.v2ex.dao.NodeDao;
import test.demo.gyniu.v2ex.model.Node;
import test.demo.gyniu.v2ex.network.HttpRequestHelper;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-7-5.
 */
public class NodeListLoader extends AsyncTaskLoader<List<Node>> {
    private static final boolean DEBUG = LogUtil.LOGD;
    private static final String TAG = "NodeListLoader";

    public NodeListLoader(Context context) {
        super(context);
    }

    @Override
    public List<Node> loadInBackgroundWithException() throws Exception {
        List<Node> nodes;
        //for debug
        long startTime = 0;
        if (BuildConfig.DEBUG) startTime = System.nanoTime();
        //////////////////////////
        nodes = NodeDao.getAllNodes();
        //for debug
        if (BuildConfig.DEBUG) {
            long consumingTime = System.nanoTime() - startTime;
            LogUtil.d(TAG, "!!!consuming time : " + consumingTime/1000 + "μs");
        }
        ///////////////////////
        if (nodes != null && nodes.size() > 0) {
            Collections.sort(nodes);
        }
        return nodes;
    }
}
