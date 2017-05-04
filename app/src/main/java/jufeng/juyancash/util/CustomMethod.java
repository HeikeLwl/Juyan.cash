package jufeng.juyancash.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.R;
import jufeng.juyancash.dao.DiscountEntity;
import jufeng.juyancash.ui.customview.ClearEditText;

/**
 * Created by Administrator102 on 2016/7/18.
 */
public class CustomMethod {
    final static int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999,
            99999999, 999999999, Integer.MAX_VALUE};

    //时间戳转字符串时间，格式为自定义
    public static String parseTime(long timeMills, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(timeMills));
    }

    //将时间字符串转化为时间戳
    public static long parseTime(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String addZeroBefore(int number) {
        DecimalFormat df = new DecimalFormat("000");
        String str2 = df.format(number);
        return str2;
    }

    //获取本地id
    public static String getLocalHostIp() {
        String ipaddress = "";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements()) {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(ip
                            .getHostAddress())) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("feige", "获取本地ip地址失败");
            e.printStackTrace();
        }
        return ipaddress;
    }

    public static void TtSpeech(TextToSpeech tts, String str) {
        if (tts != null) {
            tts.speak(str, TextToSpeech.QUEUE_ADD, null);
        }
    }

    /**
     * 控制浮点数两位小数
     *
     * @param param
     * @return
     */
    public static Float decimalFloat(float param) {
        int paramToInt = (int) (param * 100);
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        return Float.valueOf(decimalFormat.format(((float) paramToInt) / 100));
    }

    public static double decimalFloat(int param, int param1) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        return Double.valueOf(decimalFormat.format(((double) param) / param1));
    }

    //dp转px
    public static int dp2px(Context context,int dpValue) {
        return (int) (dpValue * context.getResources().getDisplayMetrics().density);
    }

    public static boolean isInPeriod(DiscountEntity discountEntity) {
        boolean result = true;
        long curTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(curTime);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String weekStr = "";
        switch (dayOfWeek) {
            case 0:
                weekStr = "周日";
                break;
            case 1:
                weekStr = "周一";
                break;
            case 2:
                weekStr = "周二";
                break;
            case 3:
                weekStr = "周三";
                break;
            case 4:
                weekStr = "周四";
                break;
            case 5:
                weekStr = "周五";
                break;
            case 6:
                weekStr = "周六";
                break;
        }
        if (discountEntity.getIsDateAlidity() == 1) {//格式2016-09-01
            String dateBegin = discountEntity.getDateBegin() + " 00:00";
            String dateEnd = discountEntity.getDateEnd() + " 23:59";
            long begin = parseTime(dateBegin, "yyyy-MM-dd HH:mm");
            long end = parseTime(dateEnd, "yyyy-MM-dd HH:mm");
            if (curTime < begin || curTime > end) {
                result = false;
            }
        }

        if (discountEntity.getIsWeekAlidity() == 1) {
            String weekDate = discountEntity.getWeekdate();
            String[] weeks = weekDate.split(",");
            result = false;
            for (String week :
                    weeks) {
                if (weekStr.equals(week)) {
                    result = true;
                    break;
                }
            }
        }

        if (discountEntity.getIsTimeAlidity() == 1) {
            result = false;
            String timeBegin = parseTime(curTime, "yyyy-MM-dd") + " " + discountEntity.getTimeStart();
            String timeEnd = parseTime(curTime, "yyyy-MM-dd") + " " + discountEntity.getTimeEnd();
            long begin = parseTime(timeBegin, "yyyy-MM-dd HH:mm");
            long end = parseTime(timeEnd, "yyyy-MM-dd HH:mm");
            if (curTime > begin && curTime < end) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 判断是否是明天
     *
     * @param time
     * @return
     */
    public static boolean isTomorrow(long time) {
        long curTime = System.currentTimeMillis();
        long tomorrow = parseTime(parseTime(curTime, "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        return time > tomorrow;
    }

    //获取整型数据的长度
    public static int sizeOfInt(int x) {
        for (int i = 0; ; i++)
            if (x <= sizeTable[i])
                return i + 1;
    }

    /**
     * 判断是否是三天前
     *
     * @param time
     * @return
     */
    public static boolean isThreeDayAgo(long time) {
        long curTime = System.currentTimeMillis();
        //今天凌晨
        long zeroTime = parseTime(parseTime(curTime, "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        //三天前的凌晨
        long threeAgoZeroTime = zeroTime - 24 * 60 * 60 * 1000;
        return time < threeAgoZeroTime;
    }

    /**
     * 生成二维码Bitmap
     *
     * @param content   内容
     * @param widthPix  图片宽度
     * @param heightPix 图片高度
     * @param logoBm    二维码中心的Logo图标（可以为null）
     * @param filePath  用于存储二维码图片的文件路径
     * @return 生成二维码及保存文件是否成功
     */
    public static boolean createQRImage(Context context, String content, int widthPix, int heightPix, Bitmap logoBm, String filePath) {
        try {
            if (content == null || "".equals(content)) {
                return false;
            }

            //配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
//            hints.put(EncodeHintType.M, 2); //default is 4

            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }

            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    //文件存储根目录
    public static String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_toast);
        tv.setText(message);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.show();
    }

    /**
     * 返回当前程序版本名
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = 2;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            versionCode = 2;
        }
        return versionCode;
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    //毛玻璃
    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {


        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);


        if (radius < 1) {

            return (null);

        }


        int w = bitmap.getWidth();

        int h = bitmap.getHeight();


        int[] pix = new int[w * h];

        bitmap.getPixels(pix, 0, w, 0, 0, w, h);


        int wm = w - 1;

        int hm = h - 1;

        int wh = w * h;

        int div = radius + radius + 1;


        int r[] = new int[wh];

        int g[] = new int[wh];

        int b[] = new int[wh];

        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;

        int vmin[] = new int[Math.max(w, h)];


        int divsum = (div + 1) >> 1;

        divsum *= divsum;

        int temp = 256 * divsum;

        int dv[] = new int[temp];

        for (i = 0; i < temp; i++) {

            dv[i] = (i / divsum);

        }


        yw = yi = 0;


        int[][] stack = new int[div][3];

        int stackpointer;

        int stackstart;

        int[] sir;

        int rbs;

        int r1 = radius + 1;

        int routsum, goutsum, boutsum;

        int rinsum, ginsum, binsum;


        for (y = 0; y < h; y++) {

            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;

            for (i = -radius; i <= radius; i++) {

                p = pix[yi + Math.min(wm, Math.max(i, 0))];

                sir = stack[i + radius];

                sir[0] = (p & 0xff0000) >> 16;

                sir[1] = (p & 0x00ff00) >> 8;

                sir[2] = (p & 0x0000ff);

                rbs = r1 - Math.abs(i);

                rsum += sir[0] * rbs;

                gsum += sir[1] * rbs;

                bsum += sir[2] * rbs;

                if (i > 0) {

                    rinsum += sir[0];

                    ginsum += sir[1];

                    binsum += sir[2];

                } else {

                    routsum += sir[0];

                    goutsum += sir[1];

                    boutsum += sir[2];

                }

            }

            stackpointer = radius;


            for (x = 0; x < w; x++) {


                r[yi] = dv[rsum];

                g[yi] = dv[gsum];

                b[yi] = dv[bsum];


                rsum -= routsum;

                gsum -= goutsum;

                bsum -= boutsum;


                stackstart = stackpointer - radius + div;

                sir = stack[stackstart % div];


                routsum -= sir[0];

                goutsum -= sir[1];

                boutsum -= sir[2];


                if (y == 0) {

                    vmin[x] = Math.min(x + radius + 1, wm);

                }

                p = pix[yw + vmin[x]];


                sir[0] = (p & 0xff0000) >> 16;

                sir[1] = (p & 0x00ff00) >> 8;

                sir[2] = (p & 0x0000ff);


                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;
                sir = stack[i + radius];
                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];
                rbs = r1 - Math.abs(i);
                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];
                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi += w;
            }
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

    /**
     * 根据打印字符数填充空格
     *
     * @param start
     * @param count
     * @param content
     * @return
     */
    public static String getPrintStr(int start, int count, String content) {
        StringBuilder sb = new StringBuilder();
        if (content.getBytes(Charset.forName("GB2312")).length > count) {
            //文本内容超过总长度
            sb.append(content.substring(0, count)).append("\n");
            String subStr = content.substring(count, content.length());
            for (int i = 0; i < start + count - subStr.getBytes(Charset.forName("GB2312")).length; i++) {
                sb.append(" ");
            }
            sb.append(subStr);
        } else {
            for (int i = 0; i < count - content.getBytes(Charset.forName("GB2312")).length; i++) {
                sb.append(" ");
            }
            sb.append(content);
        }
        return sb.toString();
    }

    /**
     * 使得content0居左，content1居右
     *
     * @param content0
     * @param content1
     * @return
     */
    public static String getPrintStr(String content0, String content1, int rate, int totalChar) {
        StringBuilder sb = new StringBuilder();
        sb.append(content0);
        int length0 = content0.getBytes(Charset.forName("GB2312")).length * rate;
        int length1 = content1.getBytes(Charset.forName("GB2312")).length * rate;
        if (length0 + length1 > totalChar) {
            sb.append("\n").append(content1).append("\n");
            return sb.toString();
        } else {
            for (int i = 0; i < (totalChar - length0 - length1) / rate; i++) {
                sb.append(" ");
            }
            return sb.append(content1).append("\n").toString();
        }
    }

    public static String getPrintStr(String content, int rate, int totalChar) {
        StringBuilder sb = new StringBuilder();
        if (content.getBytes(Charset.forName("GB2312")).length * rate < totalChar) {
            int count = totalChar - content.getBytes(Charset.forName("GB2312")).length * rate;
            for (int i = 0; i < count / 2 / rate; i++) {
                sb.append(" ");
            }
            sb.append(content);
            for (int i = 0; i < count / 2 / rate; i++) {
                sb.append(" ");
            }
        } else {
            sb.append(content);
        }
        return sb.toString();
    }

    //设置隐藏键盘显示光标
    public static void setMyInputType1(final EditText editText, Activity activity) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        } else {
            if (activity == null) {
                return;
            }
            activity.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus = cls.getMethod(
                        "setSoftInputShownOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod(
                        "setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //设置隐藏键盘显示光标
    public static void setMyInputType(EditText editText, Activity activity) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            if (activity == null) {
                return;
            }
            activity.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus = cls.getMethod(
                        "setSoftInputShownOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod(
                        "setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //设置隐藏键盘显示光标
    public static void setMyInputType2(EditText editText, Activity activity) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL | InputType.TYPE_CLASS_NUMBER);
        } else {
            if (activity == null) {
                return;
            }
            activity.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus = cls.getMethod(
                        "setSoftInputShownOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod(
                        "setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //提示对话框
    public static void showMessage(Context context, String message) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("提示信息");
        dialog.setMessage(message);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //提示对话框
    public static void showMessage(Context context, String title, String message) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 添加备注
     *
     * @param context
     * @param etNote
     */
    public static void addNote(Context context, final TextView etNote) {
        final ClearEditText et = new ClearEditText(context);
        et.setBackgroundResource(R.color.activityBackground);
        et.setMinLines(4);
        et.setPadding(6, 6, 6, 6);
        et.setText(etNote.getText());
        et.setTextSize(16);
        et.setTextColor(0xff333333);

        new AlertDialog.Builder(context).setTitle("修改备注")
                .setIcon(R.drawable.note)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        etNote.setText(input);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    public static Bitmap getStoreLogoBitmap(Context context) {
        Bitmap bitmap = null;
        try {
            //得到图片地址
            File file1 = new File(context.getResources().getString(R.string.JUYANCASH_DIR_URL));
            if (!file1.exists()) {
                file1.mkdir();
            }
            File file = new File(context.getResources().getString(R.string.JUYANCASH_IMG_URL));
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(context.getResources().getString(R.string.JUYANCASH_IMG_URL));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //声明称为静态变量有助于调用
    public static byte[] readImage(String path) throws Exception {
        URL url = new URL(path);
        // 记住使用的是HttpURLConnection类
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        //如果运行超过5秒会自动失效 这是android规定
        conn.setConnectTimeout(5 * 1000);
        InputStream inStream = conn.getInputStream();

        if (conn != null) {
            conn.disconnect();
        }
        //调用readStream方法
        return readStream(inStream);
    }


    public static byte[] readStream(InputStream inStream) throws Exception {

        //把数据读取存放到内存中去
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        buffer = null;
        return outSteam.toByteArray();
    }

    public static void hideSoftKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
