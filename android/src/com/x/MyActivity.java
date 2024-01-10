package com.x;

//Qt5

import org.qtproject.qt5.android.bindings.QtActivity;

import com.x.MyService;

import android.os.Process;
import android.os.HandlerThread;
import android.content.ClipboardManager;
import android.content.ClipData;

import java.util.Stack;

import android.view.inputmethod.InputMethodSession.EventCallback;
import android.app.Application;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.ContentResolver;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.database.Cursor;
import android.content.ContentUris;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.Handler;
import android.os.Looper;
import android.app.PendingIntent;
import android.text.TextUtils;
import android.util.Log;
import android.os.Build;
import android.graphics.Color;
import android.view.WindowManager;
import android.view.View;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.hardware.SensorManager;
import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.os.PersistableBundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import android.content.ContentUris;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.zip.ZipInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.util.zip.ZipEntry;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.Objects;
import java.util.zip.ZipOutputStream;
import java.util.Random;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.widget.Toast;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.support.v4.content.FileProvider;
//import androidx.core.content.FileProvider;

import android.graphics.Rect;
import android.view.ViewTreeObserver;

import java.lang.reflect.Field;

import android.widget.LinearLayout;

//import javax.swing.text.View;
import android.webkit.WebView;
import android.webkit.JavascriptInterface;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.ContextMenu;
import android.util.AttributeSet;

import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageInfo;

import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;

import android.os.FileObserver;


public class MyActivity extends QtActivity implements Application.ActivityLifecycleCallbacks {

    private static MyActivity m_instance = null;
    private static SensorManager mSensorManager;
    public static int isStepCounter = -1;
    public static float stepCounts;
    private static WakeLock mWakeLock = null;
    private static PersistService mySerivece;
    private static final int DELAY = SensorManager.SENSOR_DELAY_NORMAL;

    private static AlarmManager alarmManager;
    private static PendingIntent pi;
    private static Intent intent;
    public static String strAlarmInfo;
    public static int alarmCount;
    public static boolean isScreenOff = false;
    public static int keyBoardHeight;

    private final static String TAG = "QtKnot";
    private static Context context;
    public static boolean ReOpen = false;
    private FileWatcher mFileWatcher;

    public native static void CallJavaNotify_0();

    public native static void CallJavaNotify_1();

    public native static void CallJavaNotify_2();

    public native static void CallJavaNotify_3();

    public native static void CallJavaNotify_4();

    public MyActivity() {

    }

    //------------------------------------------------------------------------
    public static int startAlarm(String str) {
        // 特殊转义字符，必须加"\\"（“.”和“|”都是转义字符）
        String[] array = str.split("\\|");
        for (int i = 0; i < array.length; i++)
            System.out.println(array[i]);

        String strTime = array[0];
        String strText = array[1];
        String strTotalS = array[2];

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        int ts = Integer.parseInt(strTotalS);
        c.add(Calendar.SECOND, ts);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);

        //定时精度不够
        //alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);

        Log.e("Alarm Manager", c.getTimeInMillis() + "");
        Log.e("Alarm Manager", str);

        System.out.println(ts);
        System.out.println("startAlarm+++++++++++++++++++++++");
        return 1;
    }

    public static int stopAlarm() {
        if (alarmManager != null) {
            if (pi != null) {
                alarmManager.cancel(pi);
            }

            System.out.println("stopAlarm+++++++++++++++++++++++" + alarmCount);
        }
        alarmCount = 0;

        return 1;
    }

    //------------------------------------------------------------------------
    public static int mini() {
        System.out.println("Mini+++++++++++++++++++++++");
        m_instance.moveTaskToBack(true);

        return 1;
    }

    public static int setReOpen() {
        ReOpen = true;
        return 1;
    }

    //------------------------------------------------------------------------


    //全局获取Context
    public static Context getContext() {
        return context;
    }

    //全透状态栏
    private void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 状态栏字体设置为深色，SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 为SDK23增加
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // 部分机型的statusbar会有半透明的黑色背景
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);// SDK21
        }
    }

    // 非全透,带颜色的状态栏,需要指定颜色（目前采用）
    private void setStatusBarColor(String color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 需要安卓版本大于5.0以上
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor(color));
        }
    }

    //----------------------------------------------------------------------

    private Sensor countSensor;

    public void initStepSensor() {
        if (countSensor != null) {
            if (mSensorManager != null) {
                mSensorManager.unregisterListener(mySerivece);
                mSensorManager
                        .registerListener(
                                mySerivece,
                                mSensorManager
                                        .getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                                SensorManager.SENSOR_DELAY_NORMAL);
            }
            isStepCounter = 1;
        } else
            isStepCounter = 0;

    }

    public static int getHardStepCounter() {
        return isStepCounter;
    }

    public static float getSteps() {
        return stepCounts;
    }

    // 屏幕唤醒相关
    private ScreenStatusReceiver mScreenStatusReceiver = null;

    private void registSreenStatusReceiver() {
        mScreenStatusReceiver = new ScreenStatusReceiver();
        IntentFilter screenStatusIF = new IntentFilter();
        screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
        screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenStatusReceiver, screenStatusIF);
    }

    class ScreenStatusReceiver extends BroadcastReceiver {
        String SCREEN_ON = "android.intent.action.SCREEN_ON";
        String SCREEN_OFF = "android.intent.action.SCREEN_OFF";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (SCREEN_ON.equals(intent.getAction())) {
                if (isStepCounter == 1) {


                }
                isScreenOff = false;
                CallJavaNotify_2();
                Log.w("Knot", "屏幕亮了");

            } else if (SCREEN_OFF.equals(intent.getAction())) {
                if (isStepCounter == 1) {
                    if (mySerivece != null) {
                        mSensorManager.unregisterListener(mySerivece);
                    }


                }
                isScreenOff = true;
                Log.w("Knot", "屏幕熄了");
            }
        }
    }

    //----------------------------------------------------------------------
    public void acquireWakeLock() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mySerivece = new PersistService();
        PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);// CPU保存运行
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);// 屏幕熄掉后依然运行
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mySerivece.mReceiver, filter);

        mWakeLock.acquire();// 屏幕熄后，CPU继续运行
        mSensorManager.registerListener(
                mySerivece,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                DELAY);

        Log.i(TAG, "call acquireWakeLock");
    }

    public static void releaseWakeLock() {

        if (mWakeLock != null && mWakeLock.isHeld()) {
            mSensorManager.unregisterListener(mySerivece);
            mWakeLock.release();
            mWakeLock = null;
            Log.i(TAG, "call releaseWakeLock");
        }

    }

    //--------------------------------------------------------------------------------------------------
    // 目前正在使用，经过改良，目前能解压所有的epub文件
    public static void Unzip(String zipFile, String targetDir) {
        Log.i(TAG, zipFile);
        Log.i(TAG, targetDir);

        int BUFFER = 1024 * 1024; //这里缓冲区我们使用4KB，
        String strEntry; //保存每个zip的条目名称
        try {
            BufferedOutputStream dest = null; //缓冲输出流
            InputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry; //每个zip条目的实例
            while ((entry = zis.getNextEntry()) != null) {
                try {
                    // 先创建目录，否则有些文件没法解压，比如根目录里面的文件
                    if (entry.isDirectory()) {
                        File fmd = new File(targetDir + entry.getName());
                        fmd.mkdirs();
                        continue;
                    }
                    int count;
                    byte data[] = new byte[BUFFER];
                    strEntry = entry.getName();
                    File entryFile = new File(targetDir + strEntry);
                    Log.i(TAG, targetDir + strEntry);
                    File entryDir = new File(entryFile.getParent());
                    Log.i(TAG, " entryDir: " + entryFile.getParent());
                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(entryFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {

                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            zis.close();
            Log.i(TAG, "unzip end...");
        } catch (Exception cwj) {
            cwj.printStackTrace();
        }
    }

    /**
     * 读取文件内容并压缩，既支持文件也支持文件夹
     *
     * @param filePath 文件路径
     */
    private static void compressFileToZip(String filePath,
                                          String zipFilePath) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            //递归的压缩文件夹和文件
            doCompress("", filePath, zos);
            //必须
            zos.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doCompress(String parentFilePath, String filePath, ZipOutputStream zos) {
        File sourceFile = new File(filePath);
        if (!sourceFile.exists()) {
            return;
        }
        String zipEntryName = parentFilePath + "/" + sourceFile.getName();
        if (parentFilePath.isEmpty()) {
            zipEntryName = sourceFile.getName();
        }
        if (sourceFile.isDirectory()) {
            File[] childFiles = sourceFile.listFiles();
            if (Objects.isNull(childFiles)) {
                return;
            }
            for (File childFile : childFiles) {
                doCompress(zipEntryName, childFile.getAbsolutePath(), zos);
            }
        } else {
            int len = -1;
            byte[] buf = new byte[1024];
            try (InputStream input = new BufferedInputStream(new FileInputStream(sourceFile))) {
                zos.putNextEntry(new ZipEntry(zipEntryName));
                while ((len = input.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String processViewIntent(Intent intent) {
        return intent.getDataString();
    }

    //-----------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //在onCreate方法这里调用来动态获取权限
        verifyStoragePermissions(this);

        //File Watch FileWatcher
        if (null == mFileWatcher) {
            mFileWatcher = new FileWatcher("/storage/emulated/0/KnotData/");
            mFileWatcher.startWatching(); //开始监听
        }

        if (m_instance != null) {
            Log.d(TAG, "App is already running... this won't work");
            Intent intent = getIntent();
            if (intent != null) {
                Log.d(TAG, "There's an intent waiting...");
                String sharedData = processViewIntent(intent);
                if (sharedData != null) {
                    Log.d(TAG, "It's a view intent");
                    Intent viewIntent = new Intent(getApplicationContext(), MyActivity.class);
                    viewIntent.setAction(Intent.ACTION_VIEW);
                    viewIntent.putExtra("sharedData", sharedData);
                    startActivity(viewIntent);
                }
            }
            finish();
            return;
        }
        m_instance = this;
        Log.d(TAG, "Android activity created");

        //唤醒锁（手机上不推荐使用，其它插电安卓系统可考虑，比如广告机等）
        //acquireWakeLock();
        mySerivece = new PersistService();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        initStepSensor();

        registSreenStatusReceiver();

        //状态栏
        context = getApplicationContext();  // 获取程序句柄
        // 设置状态栏颜色,需要安卓版本大于5.0
         this.setStatusBarColor("#F3F3F3");  //灰
        // this.setStatusBarColor("#19232D");  //深色
        // 设置状态栏全透明
        // this.setStatusBarFullTransparent();
        //状态栏文字自适应
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Application application = this.getApplication();
        application.registerActivityLifecycleCallbacks(this);

        // 获取系统语言
        MyService.isZh(this);

        // 服务
        Intent bindIntent = new Intent(MyActivity.this, MyService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(bindIntent);

        } else {
            bindService(bindIntent, mCon, Context.BIND_AUTO_CREATE);
            startService(new Intent(bindIntent));
        }

        //Test
        //MyService.notify(getApplicationContext(), "Hello!");

        // 定时闹钟
        alarmCount = 0;
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        intent = new Intent(MyActivity.this, ClockActivity.class);
        pi = PendingIntent.getActivity(MyActivity.this, 0, intent, 0);

        // HomeKey
        registerReceiver(mHomeKeyEvent, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

        getShare("Knot");

    }

    public String getShare(String uripath) {
        //获取分享的数据
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        //设置接收类型为文本
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handlerText(intent);
                return "1";
            }
        }
        return "0";
    }

    //该方法用于获取intent所包含的文本信息，并显示到APP的Activity界面上
    private void handlerText(Intent intent) {
        String data = intent.getStringExtra(Intent.EXTRA_TEXT);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Knot", data);
        clipboard.setPrimaryClip(clip);
    }


    private static ServiceConnection mCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            // Auto-generated method stub
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // Auto-generated method stub
        }
    };

    @Override
    public void onPause() {
        System.out.println("onPause...");
        super.onPause();

    }

    @Override
    public void onStop() {
        System.out.println("onStop...");
        super.onStop();

        //目前暂不需要，已采用新方法
        //QtApplication.invokeDelegate();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy...");
        releaseWakeLock();
        if (null != mFileWatcher) mFileWatcher.stopWatching(); //停止监听

        //让系统自行处理，否则退出时有可能出现崩溃
        //if(mHomeKeyEvent!=null)
        //unregisterReceiver(mHomeKeyEvent);

        //if(mScreenStatusReceiver!=null)
        //unregisterReceiver(mScreenStatusReceiver);


        if (ReOpen) {
            doStartApplicationWithPackageName("com.x");
        }

        android.os.Process.killProcess(android.os.Process.myPid());

        super.onDestroy();

        //目前暂不需要，已采用新方法
        //QtApplication.invokeDelegate();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        //转至后台
        System.out.println("MyActivity onActivityStopped...");

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    //---------------------------------------------------------------------------
    class PersistService extends Service implements SensorEventListener {
        public BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mSensorManager != null) {//取消监听后重写监听，以保持后台运行
                    mSensorManager.unregisterListener(PersistService.this);
                    mSensorManager
                            .registerListener(
                                    PersistService.this,
                                    mSensorManager
                                            .getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                                    SensorManager.SENSOR_DELAY_NORMAL);
                }
            }
        };

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.i(TAG, "PersistService.onAccuracyChanged().");
        }

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // 做个判断传感器类型很重要，这可以过滤掉杂音（比如可能来自其它传感器的值）
            if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                //float[] values = sensorEvent.values;
                stepCounts = (long) sensorEvent.values[0];
            }
        }

        @Override
        public IBinder onBind(Intent intent) {
            // Auto-generated method stub
            return null;
        }

    }

    //--------------------------------------------------------------------------------

    public class PDFView extends WebView {

        public boolean isTop = true, isBottom = false;//用于判断滑动位置

        private final static String PDFJS = "file:///android_asset/pdfjs/web/viewer.html?file=";

        public PDFView(Context context) {
            super(context);
            init();
        }

        public PDFView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public PDFView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return false;
        }

        private void init() {
            WebSettings settings = getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
            settings.setSupportZoom(true);
            settings.setUseWideViewPort(true);
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);

            setWebViewClient(new WebViewClient() {// 注册滑动监听方法viewerContainer为加载pdf的viewid
                @Override
                public void onPageFinished(WebView webView, String s) {
                    super.onPageFinished(webView, s);
                    //滑动监听
                    String startSave = "\n" +
                            "document.getElementById(\"viewerContainer\").addEventListener('scroll',function () {\n" +
                            "if(this.scrollHeight-this.scrollTop - this.clientHeight < 50){\n" +
                            "window.java.bottom(); \n" +
                            "}\n" +
                            "else if(this.scrollTop==0){\n" +
                            "window.java.top(); \n" +
                            "}\n" +
                            "else {\n" +
                            "window.java.scrolling(); \n" +
                            "}\n" +
                            "});";
                    webView.loadUrl("javascript:" + startSave);
                }
            });
            addJavascriptInterface(new Object() {
                @JavascriptInterface
                public void bottom() {
                    Log.e("msg+++++++", "到了低端");
                    isBottom = true;
                    isTop = false;
                }

                @JavascriptInterface
                public void scrolling() {
                    Log.e("msg+++++++", "滑动中");
                    isBottom = false;
                    isTop = false;
                }

                @JavascriptInterface
                public void top() {
                    Log.e("msg+++++++", "到了顶端");
                    isBottom = false;
                    isTop = true;
                }
            }, "java");
        }

        @Override
        protected void onCreateContextMenu(ContextMenu menu) {
            super.onCreateContextMenu(menu);
        }

        @Override
        public ActionMode startActionMode(ActionMode.Callback callback) {
            return super.startActionMode(callback);
        }

        //加载本地的pdf
        public void loadLocalPDF(String path) {
            loadUrl(PDFJS + "file://" + path);
        }

        //加载url的pdf
        public void loadOnlinePDF(String url) {
            loadUrl(PDFJS + url);
        }
    }


    //----------------------------------------------------------------------------------------------
    /*
This method can parse out the real local file path from a file URI.
*/
    public String getUriPath(String uripath) {
        String URL = uripath;
        String str = "None";
        try {
            //if (Build.VERSION.SDK_INT >= 26) {
            str = URLDecoder.decode(URL, "UTF-8");
            //}
        } catch (Exception e) {
            System.err.println("Error : URLDecoder.decode");
            e.printStackTrace();
        }

        Log.i(TAG, "UriString  " + uripath);
        Log.i(TAG, "RealPath  " + str);
        return str;

    }

    private String getUriRealPath(Context ctx, Uri uri) {
        String ret = "";

        if (isAboveKitKat()) {
            // Android sdk version number bigger than 19.
            ret = getUriRealPathAboveKitkat(ctx, uri);
        } else {
            // Android sdk version number smaller than 19.
            ret = getImageRealPath(getContentResolver(), uri, null);
        }

        return ret;
    }

    /*
    This method will parse out the real local file path from the file content URI.
    The method is only applied to the android SDK version number that is bigger than 19.
    */
    private String getUriRealPathAboveKitkat(Context ctx, Uri uri) {
        String ret = "";

        if (ctx != null && uri != null) {

            if (isContentUri(uri)) {
                if (isGooglePhotoDoc(uri.getAuthority())) {
                    ret = uri.getLastPathSegment();
                } else {
                    ret = getImageRealPath(getContentResolver(), uri, null);
                }
            } else if (isFileUri(uri)) {
                ret = uri.getPath();
            } else if (isDocumentUri(ctx, uri)) {

                // Get uri related document id.
                String documentId = DocumentsContract.getDocumentId(uri);

                // Get uri authority.
                String uriAuthority = uri.getAuthority();

                if (isMediaDoc(uriAuthority)) {
                    String idArr[] = documentId.split(":");
                    if (idArr.length == 2) {
                        // First item is document type.
                        String docType = idArr[0];

                        // Second item is document real id.
                        String realDocId = idArr[1];

                        // Get content uri by document type.
                        Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        if ("image".equals(docType)) {
                            mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if ("video".equals(docType)) {
                            mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if ("audio".equals(docType)) {
                            mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        // Get where clause with real document id.
                        String whereClause = MediaStore.Images.Media._ID + " = " + realDocId;

                        ret = getImageRealPath(getContentResolver(), mediaContentUri, whereClause);
                    }

                } else if (isDownloadDoc(uriAuthority)) {
                    // Build download URI.
                    Uri downloadUri = Uri.parse("content://downloads/public_downloads");

                    // Append download document id at URI end.
                    Uri downloadUriAppendId = ContentUris.withAppendedId(downloadUri, Long.valueOf(documentId));

                    ret = getImageRealPath(getContentResolver(), downloadUriAppendId, null);

                } else if (isExternalStoreDoc(uriAuthority)) {
                    String idArr[] = documentId.split(":");
                    if (idArr.length == 2) {
                        String type = idArr[0];
                        String realDocId = idArr[1];

                        if ("primary".equalsIgnoreCase(type)) {
                            ret = Environment.getExternalStorageDirectory() + "/" + realDocId;
                        }
                    }
                }
            }
        }

        return ret;
    }

    /* Check whether the current android os version is bigger than KitKat or not. */
    private boolean isAboveKitKat() {
        boolean ret = false;
        ret = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        return ret;
    }

    /* Check whether this uri represent a document or not. */
    private boolean isDocumentUri(Context ctx, Uri uri) {
        boolean ret = false;
        if (ctx != null && uri != null) {
            ret = DocumentsContract.isDocumentUri(ctx, uri);
        }
        return ret;
    }

    /* Check whether this URI is a content URI or not.
     *  content uri like content://media/external/images/media/1302716
     *  */
    private boolean isContentUri(Uri uri) {
        boolean ret = false;
        if (uri != null) {
            String uriSchema = uri.getScheme();
            if ("content".equalsIgnoreCase(uriSchema)) {
                ret = true;
            }
        }
        return ret;
    }

    /* Check whether this URI is a file URI or not.
     *  file URI like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
     * */
    private boolean isFileUri(Uri uri) {
        boolean ret = false;
        if (uri != null) {
            String uriSchema = uri.getScheme();
            if ("file".equalsIgnoreCase(uriSchema)) {
                ret = true;
            }
        }
        return ret;
    }


    /* Check whether this document is provided by ExternalStorageProvider. Return true means the file is saved in external storage. */
    private boolean isExternalStoreDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.android.externalstorage.documents".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by DownloadsProvider. return true means this file is a downloaded file. */
    private boolean isDownloadDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.android.providers.downloads.documents".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /*
    Check if MediaProvider provides this document, if true means this image is created in the android media app.
    */
    private boolean isMediaDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.android.providers.media.documents".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /*
    Check whether google photos provide this document, if true means this image is created in the google photos app.
    */
    private boolean isGooglePhotoDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.google.android.apps.photos.content".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /* Return uri represented document file real local path.*/
    private String getImageRealPath(ContentResolver contentResolver, Uri uri, String whereClause) {
        String ret = "";

        // Query the URI with the condition.
        Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);

        if (cursor != null) {
            boolean moveToFirst = cursor.moveToFirst();
            if (moveToFirst) {

                // Get columns name by URI type.
                String columnName = MediaStore.Images.Media.DATA;

                if (uri == MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Images.Media.DATA;
                } else if (uri == MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Audio.Media.DATA;
                } else if (uri == MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Video.Media.DATA;
                }

                // Get column index.
                int imageColumnIndex = cursor.getColumnIndex(columnName);

                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex);
            }
        }

        return ret;
    }

    private BroadcastReceiver mHomeKeyEvent = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    // 表示按了home键,程序直接进入到后台

                    System.out.println("MyActivity HOME键被按下...");
                } else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
                    // 表示长按home键,显示最近使用的程序
                    System.out.println("MyActivity 长按HOME键...");
                }
            }
        }
    };

    //---------------------------------------------------------------------------------------------
    static public int copyFile(String srcPath, String FileName) {

        Log.i(TAG, "src  " + srcPath);
        Log.i(TAG, "dest  " + FileName);

        int result = 0;
        if ((srcPath == null) || (FileName == null)) {
            return result;
        }
        File src = new File(srcPath);
        //File dest = new File("/storage/emulated/0/Download/ " + FileName);
        File dest = new File(FileName);
        if (dest != null && dest.exists()) {
            dest.delete(); // delete file
        }
        try {
            dest.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileChannel srcChannel = null;
        FileChannel dstChannel = null;

        try {
            srcChannel = new FileInputStream(src).getChannel();
            dstChannel = new FileOutputStream(dest).getChannel();
            srcChannel.transferTo(0, srcChannel.size(), dstChannel);
            result = 1;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
        try {
            srcChannel.close();
            dstChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //----------------------------------------------------------------------------------------------
    public void openKnotBakDir() {
        Uri dir = Uri.parse("/storage/emulated/0/KnotBak/");
        int PICKFILE_RESULT_CODE = 1;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, dir);
        intent.setType("*/*");
        Intent i = Intent.createChooser(intent, "View Default File Manager");
        startActivityForResult(i, PICKFILE_RESULT_CODE);
    }

    //----------------------------------------------------------------------------------------------
    public static int getAndroidVer() {
        int a = Build.VERSION.SDK_INT;

        Log.i(TAG, "os ver=" + a);
        return a;
    }

    private static String souAPK;

    public static void setAPKFile(String apkfile) {
        souAPK = apkfile;
    }

    public void installApk(String sou) {
        File newApkFile = new File(sou);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type = "application/vnd.android.package-archive";
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName(), newApkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(newApkFile);
        }
        intent.setDataAndType(uri, type);
        context.startActivity(intent);
    }

    private void doStartApplicationWithPackageName(String packagename) {
        Log.i(TAG, "自启动开始...");
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = getPackageManager().getPackageInfo(packagename, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            startActivity(intent);

            Log.i(TAG, "启动自己已完成...");
        }

        Log.i(TAG, "过程完成...");
    }

    //==============================================================================================
    //动态获取权限需要添加的常量
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    //被调用的方法
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //==============================================================================================

    public class FileWatcher extends FileObserver {
        static final String TAG = "FileWatcher";
        ArrayList<FileObserver> mObservers;
        String mPath;
        int mMask;

        String mThreadName = FileWatcher.class.getSimpleName();
        HandlerThread mThread;
        Handler mThreadHandler;

        private Context mContext;

        public FileWatcher(Context context, String path) {
            this(path, ALL_EVENTS);
            mContext = context;
        }

        public FileWatcher(String path) {
            this(path, ALL_EVENTS);
        }

        public FileWatcher(String path, int mask) {
            super(path, mask);
            mPath = path;
            mMask = mask;
        }

        @Override
        public void startWatching() {
            mThreadName = FileWatcher.class.getSimpleName();
            if (mThread == null || !mThread.isAlive()) {
                mThread = new HandlerThread(mThreadName, Process.THREAD_PRIORITY_BACKGROUND);
                mThread.start();

                mThreadHandler = new Handler(mThread.getLooper());
                mThreadHandler.post(new startRunnable());
            }
        }

        @Override
        public void stopWatching() {
            if (null != mThreadHandler && null != mThread && mThread.isAlive()) {
                mThreadHandler.post(new stopRunnable());
            }
            mThreadHandler = null;
            mThread.quit();
            mThread = null;
        }

        @Override
        public void onEvent(int event, String path) {
            event = event & FileObserver.ALL_EVENTS;
            final String tmpPath = path;
            switch (event) {
                case FileObserver.ACCESS:
                    // Log.i("FileWatcher", "ACCESS: " + path);
                    if (path.contains("/storage/emulated/0/KnotData//todo.ini") || path.contains("/storage/emulated/0/KnotData//mainnotes.ini"))
                        CallJavaNotify_0();
                    break;
                case FileObserver.ATTRIB:
                    // Log.i("FileWatcher", "ATTRIB: " + path);
                    break;
                case FileObserver.CLOSE_NOWRITE:
                    // Log.i("FileWatcher", "CLOSE_NOWRITE: " + path);
                    break;
                case FileObserver.CLOSE_WRITE:
                    //Log.i("FileWatcher", "CLOSE_WRITE: " + path);
                    // 文件写入完毕后会回调，可以在这对新写入的文件做操作

                    mThreadHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            //
                        }
                    });
                    break;
                case FileObserver.CREATE:
                    //Log.i(TAG, "CREATE: " + path);

                    mThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            doCreate(tmpPath);
                        }
                    });
                    break;
                case FileObserver.DELETE:
                    // Log.i(TAG, "DELETE: " + path);
                    mThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            doDelete(tmpPath);
                        }
                    });
                    break;
                case FileObserver.DELETE_SELF:
                    // Log.i("FileWatcher", "DELETE_SELF: " + path);
                    break;
                case FileObserver.MODIFY:
                    // Log.i("FileWatcher", "MODIFY: " + path);

                    break;
                case FileObserver.MOVE_SELF:
                    // Log.i("FileWatcher", "MOVE_SELF: " + path);
                    break;
                case FileObserver.MOVED_FROM:
                    // Log.i("FileWatcher", "MOVED_FROM: " + path);
                    break;
                case FileObserver.MOVED_TO:
                    // Log.i("FileWatcher", "MOVED_TO: " + path);
                    break;
                case FileObserver.OPEN:
                    // Log.i("FileWatcher", "OPEN: " + path);
                    break;
                default:
                    // Log.i(TAG, "DEFAULT(" + event + ";) : " + path);

                    break;
            }
        }

        private void doCreate(String path) {
            synchronized (FileWatcher.this) {
                File file = new File(path);
                if (!file.exists()) {
                    return;
                }

                if (file.isDirectory()) {
                    // 新建文件夹，对该文件夹及子目录添加监听
                    Stack<String> stack = new Stack<String>();
                    stack.push(path);

                    while (!stack.isEmpty()) {
                        String parent = stack.pop();
                        SingleFileObserver observer = new SingleFileObserver(parent, mMask);
                        observer.startWatching();

                        mObservers.add(observer);
                        Log.d(TAG, "add observer " + parent);
                        File parentFile = new File(parent);
                        File[] files = parentFile.listFiles();
                        if (null == files) {
                            continue;
                        }

                        for (File f : files) {
                            if (f.isDirectory() && !f.getName().equals(".") && !f.getName().equals("..")) {
                                stack.push(f.getPath());
                            }
                        }
                    }

                    stack.clear();
                    stack = null;
                } else {
                    // 新建文件
                }
            }
        }

        private void doDelete(String path) {
            synchronized (FileWatcher.this) {
                Iterator<FileObserver> it = mObservers.iterator();
                while (it.hasNext()) {
                    SingleFileObserver sfo = (SingleFileObserver) it.next();
                    // 如果删除的是文件夹移除对该文件夹及子目录的监听
                    if (sfo.mPath != null && (sfo.mPath.equals(path) || sfo.mPath.startsWith(path + "/"))) {
                        Log.d(TAG, "stop observer " + sfo.mPath);
                        sfo.stopWatching();
                        it.remove();
                        sfo = null;
                    }
                }
            }
        }

        /**
         * Monitor single directory and dispatch all events to its parent, with full
         * path.
         */
        class SingleFileObserver extends FileObserver {
            String mPath;

            public SingleFileObserver(String path) {
                this(path, ALL_EVENTS);
                mPath = path;
            }

            public SingleFileObserver(String path, int mask) {
                super(path, mask);
                mPath = path;
            }

            @Override
            public void onEvent(int event, String path) {
                if (path == null) {
                    return;
                }
                String newPath = mPath + "/" + path;
                FileWatcher.this.onEvent(event, newPath);
            }
        }

        class startRunnable implements Runnable {
            @Override
            public void run() {
                synchronized (FileWatcher.this) {
                    if (mObservers != null) {
                        return;
                    }

                    mObservers = new ArrayList<FileObserver>();
                    Stack<String> stack = new Stack<String>();
                    stack.push(mPath);

                    while (!stack.isEmpty()) {
                        String parent = String.valueOf(stack.pop());
                        mObservers.add(new SingleFileObserver(parent, mMask));
                        File path = new File(parent);
                        File[] files = path.listFiles();
                        if (null == files) {
                            continue;
                        }

                        for (File f : files) {
                            if (f.isDirectory() && !f.getName().equals(".") && !f.getName().equals("..")) {
                                stack.push(f.getPath());
                            }
                        }
                    }

                    Iterator<FileObserver> it = mObservers.iterator();
                    while (it.hasNext()) {
                        SingleFileObserver sfo = (SingleFileObserver) it.next();
                        sfo.startWatching();
                    }
                }
            }
        }

        class stopRunnable implements Runnable {
            @Override
            public void run() {
                synchronized (FileWatcher.this) {
                    if (mObservers == null) {
                        return;
                    }

                    Iterator<FileObserver> it = mObservers.iterator();
                    while (it.hasNext()) {
                        FileObserver sfo = it.next();
                        sfo.stopWatching();
                    }
                    mObservers.clear();
                    mObservers = null;
                }
            }
        }
    }

}


