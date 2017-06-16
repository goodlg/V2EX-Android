package test.demo.gyniu.v2ex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by uiprj on 17-6-16.
 */
public class LoadingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, R.string.toast_please_wait, Toast.LENGTH_SHORT).show();
    }
}

