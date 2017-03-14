package test.demo.gyniu.v2ex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by uiprj on 17-3-14.
 */
public class TopicListFragment extends Fragment{
    private static final String PAGE = "page";
    private String mTitle;
    public TopicListFragment(){}

    public static Fragment newInstance(String str){
        Fragment fragment = new TopicListFragment();
        Bundle args = new Bundle();
        args.putString(PAGE,str);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        if (b != null){
            mTitle = b.getString(PAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.topic_list, null);
        TextView tv = (TextView)view.findViewById(R.id.tv);
        tv.setText(mTitle);
        return view;
    }
}
