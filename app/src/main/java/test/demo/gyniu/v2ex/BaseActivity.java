package test.demo.gyniu.v2ex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by uiprj on 17-5-10.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppCtx.getInstance().isInited()) {
            return;
        }
    }
}
