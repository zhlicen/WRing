package live.a23333.wring;

/**
 * Created by zhlic on 6/15/2017.
 */
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

public class RingService extends Service
{
    static boolean sIsRinging = false;
    static MediaPlayer thePlayer;
    static Vibrator vibrator;
    static int orgVol;

    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CallReceiver.mService = this;
        Thread ringTh = new Thread(new Runnable() {
            @Override
            public void run() {
                startRing();
            }
        });
        ringTh.run();
        return super.onStartCommand(intent, flags, startId);
    }

    public static void startRing() {
        sIsRinging = true;
        Context ctx = CallReceiver.mService;
        vibrator = (Vibrator)ctx.getSystemService(Context.VIBRATOR_SERVICE);
        thePlayer = MediaPlayer.create(ctx, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        try {
            AudioManager am = (AudioManager)ctx.getSystemService(Context.AUDIO_SERVICE);
            am.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
                                               @Override
                                               public void onAudioFocusChange(int focusChange) {
                                               }
                                           },
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            orgVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            if(orgVol != 0)
                am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                    0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        thePlayer.start();
        thePlayer.setLooping(true);
        long[] pattern = {0, 1000, 1500};
        vibrator.vibrate(pattern, 0);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRing(this);
    }

    public static void stopRing(Context ctx) {
        if(vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
        if(thePlayer != null) {
            thePlayer.stop();
            thePlayer.release();
            thePlayer = null;
        }
        AudioManager am = (AudioManager)ctx.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, orgVol,
                0);
    }
}


