package test.demo.gyniu.v2ex.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.util.Log;

import test.demo.gyniu.v2ex.BuildConfig;

public class LogUtil {
	public static final boolean LOGD = true;
	private static final boolean DEFAULT_LOG = true;
	private static final String DEFAULT_TAG = "ngy" + "/" + BuildConfig.VERSION_NAME;

	private static final String LOG_PREFIX = "@@@@@ ";

	public static void d(String TAG, String className, String method, String msg) {
        if (DEFAULT_LOG) {
            Log.d(DEFAULT_TAG, LOG_PREFIX + "< " + className + " >" + "[ " + method + " ]" + msg);
		} else {
            Log.d(TAG, LOG_PREFIX + "< " + className + " >" + "[ " + method + " ]" + msg);
		}
	}

	public static void d(String TAG, String method, String msg) {
		if (DEFAULT_LOG) {
			Log.d(DEFAULT_TAG, LOG_PREFIX + "[ " + method + " ]" + msg);
		} else {
			Log.d(TAG, LOG_PREFIX + "[ " + method + " ]" + msg);
		}
	}

	public static void d(String TAG, String msg) {
		if (DEFAULT_LOG) {
			Log.d(DEFAULT_TAG, LOG_PREFIX + getFileLineMethod() + " " + msg);
		} else {
			Log.d(TAG, LOG_PREFIX + getFileLineMethod() + " " + msg);
		}
	}

	public static void d(String msg) {
		Log.d(DEFAULT_TAG, LOG_PREFIX + _FILE_() + " " + getLineMethod() + " " + msg);
	}

	public static void d(Object[] obj) {
		StringBuffer toStringBuffer = new StringBuffer("[");
		for (Object o : obj) {
			if (o != null) {
				toStringBuffer.append(o.toString() + ",");
			}
		}
		toStringBuffer.append("]");

		Log.d(DEFAULT_TAG, LOG_PREFIX + "[ " + getLineMethod() + " ]"
				+ toStringBuffer.toString());
	}

	public static void i(String TAG, String method, String msg) {
		if (DEFAULT_LOG) {
			Log.i(DEFAULT_TAG, LOG_PREFIX + "[ " + method + " ]" + msg);
		} else {
			Log.i(TAG, LOG_PREFIX + "[ " + method + " ]" + msg);
		}
	}

	public static void i(String TAG, String msg) {
		if (DEFAULT_LOG) {
			Log.i(DEFAULT_TAG, LOG_PREFIX + getFileLineMethod() + " " + msg);
		} else {
			Log.i(TAG, LOG_PREFIX + getFileLineMethod() + " " + msg);
		}
	}

	public static void i(String msg) {
		Log.i(DEFAULT_TAG, LOG_PREFIX + _FILE_() + " " + getLineMethod() + " " + msg);
	}

	public static void w(String TAG, String method, String msg) {
		if (DEFAULT_LOG) {
			Log.w(DEFAULT_TAG, LOG_PREFIX + "[ " + method + " ]" + msg);
		} else {
			Log.w(TAG, LOG_PREFIX + "[ " + method + " ]" + msg);
		}
	}

	public static void w(String TAG, String msg) {
		if (DEFAULT_LOG) {
			Log.w(DEFAULT_TAG, LOG_PREFIX + getFileLineMethod() + " " + msg);
		} else {
			Log.w(TAG, LOG_PREFIX + getFileLineMethod() + " " + msg);
		}
	}

	public static void w(String msg) {
		Log.w(DEFAULT_TAG, LOG_PREFIX + _FILE_() + " " + getLineMethod() + " " + msg);
	}

	public static void e(String TAG, String method, String msg) {
		if (DEFAULT_LOG) {
			Log.i(DEFAULT_TAG, LOG_PREFIX + "[ " + method + " ]" + msg);
		} else {
			Log.i(TAG, LOG_PREFIX + "[ " + method + " ]" + msg);
		}
	}

	public static void e(String TAG, String msg) {
		// Log.e(TAG, getLineMethod() + msg);
		if (DEFAULT_LOG) {
			Log.e(DEFAULT_TAG, LOG_PREFIX + getFileLineMethod() + " " + msg);
		} else {
			Log.e(TAG, LOG_PREFIX + getFileLineMethod() + " " + msg);
		}
	}

	public static void e(String msg) {
		Log.e(DEFAULT_TAG, LOG_PREFIX + _FILE_() + " " + getLineMethod() + " " + msg);
	}

	public static String makeLogTag(Class<?> cls) {
		if (cls != null) {
			return cls.getSimpleName();
		}
		return null;
	}

	public static String getFileLineMethod() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
		StringBuffer toStringBuffer = new StringBuffer("[ ")
				.append(traceElement.getFileName()).append(" | ")
				.append(traceElement.getLineNumber()).append(" | ")
				.append(traceElement.getMethodName()).append("()").append(" ]");
		return toStringBuffer.toString();
	}

	public static String getLineMethod() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
		StringBuffer toStringBuffer = new StringBuffer("[ ")
				.append(traceElement.getLineNumber()).append(" | ")
				.append(traceElement.getMethodName()).append("()").append(" ]");
		return toStringBuffer.toString();
	}

	public static String _FILE_() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
		return traceElement.getFileName();
	}

	public static String _FUNC_() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
		return traceElement.getMethodName();
	}

	public static int _LINE_() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
		return traceElement.getLineNumber();
	}

	@SuppressLint("SimpleDateFormat")
	public static String _TIME_() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sdf.format(now);
	}
}