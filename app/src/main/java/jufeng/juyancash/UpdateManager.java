package jufeng.juyancash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator102 on 2016/11/23.
 */
public class UpdateManager {
    private Context mContext;
    // 提示语
    private String updateMsg = "您有新的版本需要更新，是否需要下载更新？";
    // 返回你需要安装的安装包url
    private AlertDialog noticeDialog;
    private AlertDialog downloadDialog;
    /* 下载包安装路径 */
    private static final String savePath = "/sdcard/juyan_update/";

    private static final String saveFileName = savePath
            + "聚炎收银.apk";

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private int progress;

    private Thread downLoadThread;

    private OnUpdateManagerListener mOnUpdateManagerListener;

    private boolean interceptFlag = false;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                default:
                    break;
            }
        }
    };

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    // 外部接口让主Activity调用
    public void checkUpdateInfo(String message,OnUpdateManagerListener onUpdateManagerListener) {
        this.mOnUpdateManagerListener = onUpdateManagerListener;
        this.updateMsg = message;
        showNoticeDialog();
    }

    // 外部接口让主Activity调用
    public void checkUpdateInfo(String message) {
        this.updateMsg = message;
        showNoticeDialog();
    }

    private void showNoticeDialog() {
        noticeDialog = new AlertDialog.Builder(mContext).create();
        noticeDialog.setIcon(mContext.getResources().getDrawable(R.mipmap.ic_launcher));
        noticeDialog.setTitle("软件版本更新");
        if(updateMsg == null){
            updateMsg = "发现新的版本，请及时更新！";
        }
        noticeDialog.setMessage(Html.fromHtml("<html>"+updateMsg+"</html>"));
        noticeDialog.setButton(AlertDialog.BUTTON_POSITIVE,"下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        noticeDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(mOnUpdateManagerListener != null){
                    mOnUpdateManagerListener.onCancle();
                }
            }
        });
        noticeDialog.show();
    }

    private void showDownloadDialog() {
        downloadDialog = new AlertDialog.Builder(mContext).create();
        downloadDialog.setTitle("聚炎版本更新");
        downloadDialog.setIcon(mContext.getResources().getDrawable(R.mipmap.ic_launcher));
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.download_progress_layout, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        downloadDialog.setView(v);
        downloadDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        downloadDialog.show();

        downloadApk();
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(mContext.getResources().getString(R.string.VERSION_ADDRESS));

                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * 下载apk
     *
     */

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     *
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }

    public interface OnUpdateManagerListener{
        void onCancle();
    }
}
