package com.vcredit.linechartview;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具类，适用于所有工程
 * Created by zhuofeng on 2015/4/8.
 */
public class CommonUtils {

    // 自定义log参数
    private static final String LOG_TAG = "Vdebug";
    private static final int LOG_SIZE_LIMIT = 3500;
    // 定时器参数
    private static Button mBtn;
    private static int mTime;
    private static Handler handler = new Handler();
    private static boolean isSending = false;
    private static boolean mallowgetCode;
    private static boolean mispress;

    /**
     * 隐藏软件输入法
     *
     * @param activity
     */
    public static void hideSoftInput(Activity activity) {
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * 关闭已经显示的输入法窗口。
     *
     * @param context      上下文对象，一般为Activity
     * @param focusingView 输入法所在焦点的View
     */
    public static void closeSoftInput(Context context, View focusingView) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(focusingView.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }

    }

    /**
     * 将输入流转换为byte数组
     *
     * @param in
     * @return
     */
    public static byte[] getByte(Bitmap in) {
        if (in == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            in.compress(Bitmap.CompressFormat.JPEG, 95, out);
            out.flush();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 判断指定路径下的文件是否存在
     */
    public static boolean detectFileIsExist(String path) {
        if (null != path) {
            File file = new File(path);
            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 通用创建dialog
     *
     * @param context
     * @param title            弹框的标题
     * @param positiveListener 确定按钮的实例化监听
     * @param negativeListener 取消按钮的实例化监听
     * @param positiveText     确定按钮的文字显示
     * @param negativeText     取消按钮的文字显示
     */
    public static void showDialog(Context context, String title,
                                  DialogInterface.OnClickListener positiveListener,
                                  DialogInterface.OnClickListener negativeListener,
                                  String positiveText, String negativeText) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setPositiveButton(positiveText, positiveListener);
        alertDialog.setNegativeButton(negativeText, negativeListener);
        if (title != null && !"".equals(title)) {
            alertDialog.setTitle(title);
            alertDialog.create().show();
        }
    }

    public static void showDialog(Context context, String title, String message,
                                  DialogInterface.OnClickListener positiveListener,
                                  DialogInterface.OnClickListener negativeListener,
                                  String positiveText, String negativeText) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setPositiveButton(positiveText, positiveListener);
        alertDialog.setNegativeButton(negativeText, negativeListener);
        if (title != null && !"".equals(title)) {
            alertDialog.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            alertDialog.setMessage(message);
            alertDialog.create().show();
        }
    }

    public static void showDialogClickOutSideNoDimiss(Context context, String title, String message,
                                                      DialogInterface.OnClickListener positiveListener,
                                                      DialogInterface.OnClickListener negativeListener,
                                                      String positiveText, String negativeText) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(positiveText, positiveListener);
        alertDialog.setNegativeButton(negativeText, negativeListener);

        if (title != null && !"".equals(title)) {
            alertDialog.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            alertDialog.setMessage(message);
            alertDialog.create().show();

        }
    }

    /**
     * 按type定制自己需要的类型
     *
     * @param num
     * @param type 添加分隔符（例如"#,###"、"#,###.00"）其他类型（例如"#.0"、"#%"等）
     * @return
     */
    public static String decimalFormat(double num, String type) {
        DecimalFormat df = new DecimalFormat(type);
        return df.format(num);
    }

    /**
     * 判断Sdcard是否存在
     *
     * @return
     */
    public static boolean detectSdcardIsExist() {
        String extStorageState = Environment.getExternalStorageState();
        File file = Environment.getExternalStorageDirectory();
        if (!Environment.MEDIA_MOUNTED.equals(extStorageState)
                || !file.exists() || !file.canWrite()
                || file.getFreeSpace() <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断存储空间大小是否满足条件
     *
     * @param sizeByte
     * @return
     */
    public static boolean isAvaiableSpace(float sizeByte) {
        boolean ishasSpace = false;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            StatFs statFs = new StatFs(sdcard);
            long blockSize = statFs.getBlockSize();
            long blocks = statFs.getAvailableBlocks();
            float availableSpare = blocks * blockSize;
            if (availableSpare > (sizeByte + 1024 * 1024)) {
                ishasSpace = true;
            }
        }
        return ishasSpace;
    }

    /**
     * 开始安装apk文件
     *
     * @param context
     * @param localFilePath
     */
    public static void installApkByGuide(Context context, String localFilePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + localFilePath),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String versionName = "0.00";
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取versioncode
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        int versionCode = 0;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public interface MessageFilter {
        String filter(String msg);
    }

    public static MessageFilter msgFilter;

    /**
     * 短时间显示Toast消息，并保证运行在UI线程中
     *
     * @param activity Activity
     * @param message  消息内容
     */
    public static void showToastS(final Activity activity, final String message) {
        showToast(activity, message, false, Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示Toast消息，并保证运行在UI线程中
     *
     * @param activity Activity
     * @param message  消息内容
     */
    public static void showToastL(final Activity activity, final String message) {
        showToast(activity, message, false, Toast.LENGTH_LONG);
    }


    /**
     * Toast消息
     *
     * @param activity
     * @param message  消息内容
     * @param center   是否居中
     * @param time     显示时间
     */
    public static void showToast(final Activity activity, final String message, final boolean center, final int time) {
        final String msg = msgFilter != null ? msgFilter.filter(message) : message;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast toast = Toast.makeText(activity, msg, time);
                if (center) toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public static int Dp2Px(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * px转dp
     *
     * @param px
     * @return
     */
    public static int Px2Dp(Context context, float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 数字金额大写转换
     */
    public static String digitUppercase(double n) {
        String fraction[] = {"角", "分"};
        String digit[] = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        String unit[][] = {{"元", "万", "亿"}, {"", "拾", "佰", "仟"}};

        String head = n < 0 ? "负" : "";
        n = Math.abs(n);

        String s = "";
        int len = fraction.length;
        for (int i = 0; i < len; i++) {
            s += (digit[(int) (Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
        }
        if (s.length() < 1) {
            s = "整";
        }

        int integerPart = (int) Math.floor(n);
        len = unit[0].length;
        for (int i = 0; i < len && integerPart > 0; i++) {
            String p = "";
            int length = unit[1].length;
            for (int j = 0; j < length && n > 0; j++) {
                p = digit[integerPart % 10] + unit[1][j] + p;
                integerPart = integerPart / 10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }


    /**
     * 判断输入的密码是否合法
     * 输入密码的白名单
     * @param Pwd 输入的密码
     * @return true 合法；false 不合法
     */
    public static boolean isValidPwd(String Pwd) {
        //6到16位密码，只能包含字母数字下划线
        Pattern pat = Pattern.compile("^[0-9a-zA-Z_!@#$%^&*()+-=/]/[,.<>/]{6,16}$");
        Matcher mat = pat.matcher(Pwd);
        return mat.matches();
    }




    /**
     * 将String转化为Double类型
     *
     * @param input string
     * @return
     */
    public static Double changeStringToDouble(String input) {
        Double temp;
        try {
            if (TextUtils.isEmpty(input)) {
                return 0.0;
            }
            temp = Double.parseDouble(input);
        } catch (Exception e) {
            return 0.0;
        }
        return temp;
    }

    /**
     * Flost
     *
     * @param input string
     * @return
     */
    public static float changeStringToFlost(String input) {
        float temp;
        try {
            if (TextUtils.isEmpty(input)) {
                return 0f;
            }
            temp = Float.parseFloat(input);
        } catch (Exception e) {
            return 0f;
        }
        return temp;
    }

    /**
     * 将String转化为Double类型
     *
     * @param input string
     * @return
     */
    public static Double changeStringToDouble2(String input) {
        Double temp;
        try {
            if (TextUtils.isEmpty(input)) {
                return 0.00;
            }
            Double Senate = Double.parseDouble(input);
            DecimalFormat df = new DecimalFormat("0.00");
            temp = new Double(df.format(Senate).toString());
        } catch (Exception e) {
            return 0.00;
        }
        return temp;
    }


    /**
     * 将String转化为int类型
     *
     * @param input string
     * @return
     */
    public static int changeStringToInt(String input) {
        int temp;
        try {
            if (TextUtils.isEmpty(input)) {
                return -1;
            }
            temp = Integer.parseInt(input);
        } catch (Exception e) {
            return -1;
        }
        return temp;
    }

    public static boolean isSending() {
        return isSending;
    }

    /**
     * 简单手机正则校验
     *
     * @param MobileNo 手机号
     * @return
     */
    public static boolean IsValidMobileNo(String MobileNo) {
        String regPattern = "^1[3-9]\\d{9}$";
        return Pattern.matches(regPattern, MobileNo);

    }

    /**
     * 验证密码：
     */
    public static boolean PassWordValidation(String str) {
        if (str.length() < 6 || str.length() > 16) {
            return true;
        }
        String regular = ".*?[\u4E00-\u9FFF]+.*";
        return str.matches(regular);
    }

    /**
     * 时间差格式化 HH:mm:ss
     *
     * @param l
     * @return
     */
    public static String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        long totalSecond;

        totalSecond = l / 1000;

        if (totalSecond >= 60) {
            minute = (int) (totalSecond / 60);
            second = (int) (totalSecond % 60);
        }
        if (minute >= 60) {
            hour = minute / 60;
            minute = minute % 60;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(getTwoLength(hour));
        sb.append(":");
        sb.append(getTwoLength(minute));
        sb.append(":");
        sb.append(getTwoLength(second));

        return sb.toString();
    }

    /**
     * 时间差格式化 HH:mm:ss
     *
     * @param l long类型 的时间
     * @return HH:mm:ss 的时间
     */
    public static List<Integer> formatLongToTimelist(Long l) {
        List<Integer> list = new ArrayList<Integer>();
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second;
        second = l.intValue() / 1000;

        if (second >= 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute >= 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        if (hour >= 24) {
            day = hour / 24;
            hour = hour % 24;
        }
        list.add((day / 10) % 10);
        list.add(day % 10);
        list.add((hour / 10) % 10);
        list.add(hour % 10);
        list.add((minute / 10) % 10);
        list.add(minute % 10);
        return list;
    }


    /**
     * 为个位数前面添加0补齐
     *
     * @param data
     * @return
     */
    public static String getTwoLength(int data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    }

    /**
     * InputStream to String
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    /**
     * 时间戳转字符串格式
     */
    public static String getDateToString(long time, String format) {
        Date date = new Date(time);
        SimpleDateFormat sDF = new SimpleDateFormat(format);
        return sDF.format(date);
    }

    /**
     * 获得当日日期
     */
    public static String getCurrentDate() {
        String currentData = "";
        Calendar c = Calendar.getInstance();
        currentData = " (" + c.get(Calendar.YEAR) + "." +
                formatTime(c.get(Calendar.MONTH) + 1) + "." +
                formatTime(c.get(Calendar.DAY_OF_MONTH)) + ")";
        return currentData;
    }

    /**
     * 获得当日星期
     */
    public static String getCurrentWeek(){
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){
            mWay ="天";
        }else if("2".equals(mWay)){
            mWay ="一";
        }else if("3".equals(mWay)){
            mWay ="二";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }
        return  "星期" + mWay ;
    }


    public static String formatTime(int t) {
        return t >= 10 ? "" + t : "0" + t;
    }

    /**
     * 屏蔽手机号中间段
     *
     * @param replace
     * @return
     */
    public static String replaceWithAsteriskForPhone(String replace) {
        if (replace == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(replace.substring(0, 3));
        for (int i = 3; i < replace.length() - 4; i++) {
            sb.append("*");
        }
        sb.append(replace.substring(replace.length() - 4));
        return sb.toString();
    }

    /**
     * 判断前台activity是否是目标activity
     *
     * @param context
     * @param className
     * @return
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;
    }

    //传入double数值，根据是否为整数，返回整数形式或带小数点的形式
    public static String doubleTrans(double num) {
        BigDecimal bigDecimal = new BigDecimal(Double.toString(num));
        num = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (num % 1.0 == 0) {
            return String.valueOf((long) num);
        }
        return String.valueOf(num);
    }

    /**
     * 根据进程id得到包名
     */

    public static String getAppNameByPid(int pID, Context context) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {

                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
            }
        }
        return processName;
    }


    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }
    /**
     *
     * u盟集成测试 获取设备信息
     *
     */
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
