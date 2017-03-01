package dlodloclose.com.videoplaydemo;
/**
 * Created by admin on 2017/2/27.
 */
public class TimeUtil {

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "0''";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = minute + "'" + second + "''";
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99h59'59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = hour + "h" + minute + "'" + second + "''";
            }
        }
        return timeStr;
    }

    public static String getTimeStr(long time) {

        int second = (int)time;

        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }

//        String hs =h+ DloAppcation.getInstance().getString(R.string.hour),
//                ds=d+ DloAppcation.getInstance().getString(R.string.minute),
//                ss=s+ DloAppcation.getInstance().getString(R.string.second);
//
//
//        if(h < 1){
//
//            if(d < 1){
//                return ss;
//            }
//
//            return ds + ss ;
//        }
//        return hs + ds +  ss ;

        String hs =""+h, ds=""+d,ss=""+s;
        if(h<10){
            hs = "0"+h;
        }
        if(d<10){
            ds = "0"+d;
        }
        if(s<10){
            ss = "0"+s;
        }
        return hs + ":" + ds + ":" + ss ;
    }



}
