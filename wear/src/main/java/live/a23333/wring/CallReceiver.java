package live.a23333.wring;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by zhlic on 6/15/2017.
 */

public class CallReceiver extends PhonecallReceiver {


    public static RingService mService;

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {
        boolean shouldRing = shouldRing(ctx);
        if(!shouldRing) {
            // Toast.makeText(ctx, "", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(ctx, ctx.getString(R.string.ringing), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ctx , RingService.class);
        ctx.startService(intent);
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        stopService(ctx);
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        stopService(ctx);
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        //
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        //
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        stopService(ctx);
    }

    public static boolean isLocked(final Context ctx) {
        KeyguardManager km = (KeyguardManager) ctx.getSystemService(Context.KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }

    public static boolean shouldRing(Context ctx) {
        boolean isLocked = isLocked(ctx);
        AudioManager am = (AudioManager)ctx.getSystemService(Context.AUDIO_SERVICE);
        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                Log.i("MyApp","Silent mode");
                return false;
        }
        return isLocked;
    }

    public void stopService(Context ctx) {
        if(mService != null) {
            mService.stopRing(ctx);
        }
        Intent intent = new Intent(ctx , RingService.class);
        ctx.stopService(intent);
    }

}