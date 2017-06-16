package test.demo.gyniu.v2ex;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import test.demo.gyniu.v2ex.common.UserState;
import test.demo.gyniu.v2ex.model.LoginResult;
import test.demo.gyniu.v2ex.utils.LogUtil;

/**
 * Created by uiprj on 17-5-10.
 */
public class SigninActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener{
    private static final String TAG = "SigninActivity";
    private static final boolean DEBUG = LogUtil.LOGD;
    private EditText mAccount;
    private EditText mPasswd;
    private Button mButton;
    private TextView mForget;
    private TextView mOrSignup;

    private View mProgressView;
    private View mLoginFormView;

    private UserLoginTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mAccount = (EditText) findViewById(R.id.account);
        mPasswd = (EditText) findViewById(R.id.passwd);
        mPasswd.setOnEditorActionListener(this);
        mButton = (Button) findViewById(R.id.signin);
        mButton.setOnClickListener(this);
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

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget:
                break;
            case R.id.signin:
                doLogin();
                break;
        }
        if (view.getId() == R.id.forget) {
            Toast.makeText(this, "forget?", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        int mActionIdSignIn = getResources().getInteger(R.integer.id_action_signin);
        if (id == mActionIdSignIn || id == EditorInfo.IME_ACTION_DONE) {
            doLogin();
            return true;
        }
        return false;
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
            Toast.makeText(context, "Sign up", Toast.LENGTH_SHORT).show();
        }
    }

    public void doLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mAccount.setError(null);
        mPasswd.setError(null);

        String nameOremail = mAccount.getText().toString();
        String password = mPasswd.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPasswd.setError(getString(R.string.error_field_required));
            focusView = mPasswd;
            cancel = true;
        }

        if (TextUtils.isEmpty(nameOremail)) {
            mAccount.setError(getString(R.string.error_field_required));
            focusView = mAccount;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserLoginTask(nameOremail, password);
            mAuthTask.execute((Void) null);
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String TAG = UserLoginTask.class.getSimpleName();

        private final String mAccount;
        private final String mPasswd;
        private Exception mException;

        UserLoginTask(String username, String password) {
            mAccount = username;
            mPasswd = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                final LoginResult result = HttpRequestHelper.getInstance().login(mAccount, mPasswd);
                if (result != null) {
                    UserState.getInstance().login(result.mAccount, result.mAvatar);
                    return true;
                }
            } catch (Exception e) {
                mException = e;
                LogUtil.e(TAG, "HAS Exception: " + e);
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                onLoginSuccess(mAccount);
                return;
            }

            if (mException == null) {
                SigninActivity.this.mPasswd.setError(getString(R.string.error_incorrect_password));
                SigninActivity.this.mPasswd.requestFocus();
                return;
            }

            if (DEBUG) LogUtil.w(TAG, "login failed: " + mException);

            int resId = R.string.error_incorrect_password;
            Toast.makeText(SigninActivity.this, resId, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void onLoginSuccess(String username) {
        Toast.makeText(this, getString(R.string.toast_login_success, username),
                Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }
}
