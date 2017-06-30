package test.demo.gyniu.v2ex.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import java.util.NoSuchElementException;

import test.demo.gyniu.v2ex.R;
import test.demo.gyniu.v2ex.network.HttpStatus;

/**
 * Created by uiprj on 17-6-30.
 */
public class ExceptionHelper {

    public static boolean handleExceptionNoCatch(Fragment fragment, Exception ex) {
        try {
            return handleException(fragment, ex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean handleException(Fragment fragment, Exception e) throws Exception {
        final FragmentActivity activity = fragment.getActivity();
        int stringId;
        boolean needFinishActivity = false;
        if (e instanceof RequestException) {
            final RequestException ex = (RequestException) e;
            switch (ex.getCode()) {
                case HttpStatus.SC_FORBIDDEN:
                    stringId = R.string.toast_access_denied;
                    break;
                default:
                    throw e;
            }
        } else {
            throw e;
        }

        if (fragment.getUserVisibleHint()) {
            Toast.makeText(activity, stringId, Toast.LENGTH_LONG).show();
        }
        return needFinishActivity;
    }
}
