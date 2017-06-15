package test.demo.gyniu.v2ex;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by uiprj on 17-5-10.
 */
public class SigninActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mUserName;
    private EditText mUserPasswd;
    private Button mButton;
    private TextView mForget;
    private TextView mOrSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mUserName = (EditText) findViewById(R.id.username);
        mUserPasswd = (EditText) findViewById(R.id.passwd);
        mButton = (Button) findViewById(R.id.signin);
        mForget = (TextView) findViewById(R.id.forget);
        mForget.setOnClickListener(this);
        mOrSignup = (TextView) findViewById(R.id.orSignup);

        //bottom color text link
        String str1 = getResources().getString(R.string.str_no_accout);
        String str2 = getResources().getString(R.string.str_signup);
        SpannableString ss = new SpannableString(str2);

        CustomClickableSpan clickSpan = new CustomClickableSpan(this, str2);
        ss.setSpan(clickSpan, 0, str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mOrSignup.setText(str1);
        mOrSignup.append(" ");
        mOrSignup.append(ss);
        mOrSignup.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.forget) {
            Toast.makeText(this, "forget?", Toast.LENGTH_SHORT).show();
        }
    }

    class CustomClickableSpan extends ClickableSpan {
        private Context context;
        private String text;

        public CustomClickableSpan(Context context,String text)
        {
            this.context = context;
            this.text = text;
        }

        public void updateDrawState(TextPaint ds) {
            ds.setColor(context.getResources().getColor(R.color.colorOrange1));
        }

        @Override
        public void onClick(View widget) {
            widget.setBackground(null);
            Toast.makeText(context, "Sign up", Toast.LENGTH_SHORT).show();
        }
    }
}
