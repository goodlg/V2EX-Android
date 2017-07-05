package test.demo.gyniu.v2ex;

import android.content.Context;
import java.util.Collections;
import java.util.List;

import test.demo.gyniu.v2ex.dao.NodeDao;
import test.demo.gyniu.v2ex.model.Node;
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
        if (!isRefresh()) {
            //load nodes from cache
            if (DEBUG) LogUtil.d(TAG, "load nodes from cache");
            nodes = NodeDao.getAll();
            if (nodes != null && nodes.size() > 0) {
                Collections.sort(nodes);
                return nodes;
            }
        }
        List<Node> lists = HttpRequestHelper.getInstance().getALlNodes();
        LogUtil.d(TAG, "lists : " + lists.size());
        return lists;
    }
}
