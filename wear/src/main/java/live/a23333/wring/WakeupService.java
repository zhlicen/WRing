package live.a23333.wring;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;

public class WakeupService extends Service {
    public static WakeupService mInstance;
    public WakeupService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        runAsForeground();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mInstance = null;
    }

    private void runAsForeground(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        Notification notification=new NotificationCompat.Builder(this)
                .setContentText("Runing...")
                .setContentTitle("WRing")
                .setContentIntent(pendingIntent).build();

        startForeground(123, notification);
    }
}
