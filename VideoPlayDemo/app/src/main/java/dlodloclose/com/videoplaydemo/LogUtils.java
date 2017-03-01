package dlodloclose.com.videoplaydemo;

import android.util.Log;

public class LogUtils {

	public static String TAG = "LogUtils";
	public static int logLevel = Log.VERBOSE;


	/**
	 * 获取打印信息所在方法名，行号等信息
	 * @return
	 */
	private static String[] getAutoJumpLogInfos() {
		String[] infos = new String[] { "", "", "" };
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if (elements.length < 5) {
			Log.e("MyLogger", "Stack is too shallow!!!");
			return infos;
		} else {
			//类名
			infos[0] = elements[4].getClassName().substring(
					elements[4].getClassName().lastIndexOf(".") + 1);
			//方法名
			infos[1] = elements[4].getMethodName() + "()";
			//行号
			infos[2] = " at (" + elements[4].getClassName() + ".java:"
					+ elements[4].getLineNumber() + ")";
			return infos;
		}
	}

	public static void pwh (String msg){
		String[] infos = getAutoJumpLogInfos();
		e("pwh",msg + "   ------Other info-----"+ infos[0] + ":" + infos[1] + ":"+infos[2]);
	}

	public static void e(String tag, String msg) {
		if (logLevel <= Log.ERROR) {
			Log.e(tag, msg);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (logLevel <= Log.ERROR) {
			Log.e(tag, msg, tr);
		}
	}

	public static void d(String tag, String msg) {
		if (logLevel <= Log.DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (logLevel <= Log.DEBUG) {
			Log.d(tag, msg, tr);
		}
	}

	public static void v(String tag, String msg) {
		if (logLevel <= Log.VERBOSE) {
			Log.v(tag, msg);
		}
	}

	public static void v(String tag, String msg, Throwable tr) {
		if (logLevel <= Log.VERBOSE) {
			Log.v(tag, msg, tr);
		}
	}

	public static void i(String tag, String msg) {
		if (logLevel <= Log.INFO) {
			Log.i(tag, msg);
		}
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (logLevel <= Log.INFO) {
			Log.i(tag, msg, tr);
		}
	}

	public static void w(String tag, String msg) {
		if (logLevel <= Log.WARN) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (logLevel <= Log.WARN) {
			Log.i(tag, msg, tr);
		}
	}

    public static void a(String tag, String... msg){
        String str = "";
        for (String m : msg){
            str += m+",";
        }
        Log.d(tag,str);
    }

	public static boolean isLoggable(String tag, int level) {
		return logLevel <= level;
	}
}
