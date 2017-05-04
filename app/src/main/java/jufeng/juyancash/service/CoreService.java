package jufeng.juyancash.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.website.AssetsWebsite;


/**
 * Created by Administrator102 on 2017/3/30.
 */

public class CoreService extends Service {

    /**
     * AndServer.
     */
    private Server mServer;

    private AssetManager mAssetManager;

    @Override
    public void onCreate() {
        mAssetManager = getAssets();

        AndServer andServer = new AndServer.Build()
                .port(8080)
                .timeout(50 * 1000)
                .registerHandler("login", new RequestLoginHandler(this))
                .registerHandler("tablelist", new RequestGetTablesHandler(this))
                .website(new AssetsWebsite(mAssetManager, ""))
                .listener(mListener)
                .build();

        // Create server.
        mServer = andServer.createServer();
    }

    /**
     * Server listener.
     */
    private Server.Listener mListener = new Server.Listener() {
        @Override
        public void onStarted() {
            ServerStatusReceiver.serverStart(CoreService.this);
        }

        @Override
        public void onStopped() {
            ServerStatusReceiver.serverStop(CoreService.this);
        }

        @Override
        public void onError(Exception e) {
            // Ports may be occupied.
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startServer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServer(); // Stop server.
    }

    /**
     * Start server.
     */
    private void startServer() {
        if (mServer != null) {
            if (mServer.isRunning()) {
                ServerStatusReceiver.serverHasStarted(CoreService.this);
            } else {
                mServer.start();
            }
        }
    }

    /**
     * Stop server.
     */
    private void stopServer() {
        if (mServer != null) {
            mServer.stop();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}