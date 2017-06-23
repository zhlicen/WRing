package live.a23333.wring;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Vibrator;

/**
 * Created by Samuel Zhou on 2017/6/23.
 */
public class SmsListener extends BroadcastReceiver {
    static MediaPlayer thePlayer;
    static Vibrator vibrator;
    static int orgVol;

    @Override
    public void onReceive(Context ctx, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
           if(CallReceiver.shouldRing(ctx)) {
               vibrator = (Vibrator)ctx.getSystemService(Context.VIBRATOR_SERVICE);
               thePlayer = MediaPlayer.create(ctx, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
               try {
                   final AudioManager am = (AudioManager)ctx.getSystemService(Context.AUDIO_SERVICE);
                   orgVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                   float vol = am.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
                   float max_vol = am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
                   float scale = vol/max_vol;
                   am.setStreamVolume(AudioManager.STREAM_MUSIC,
                           (int)(scale * am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)),
                           0);
                   thePlayer.setLooping(false);
                   thePlayer.start();
                   thePlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                       @Override
                       public void onCompletion(MediaPlayer mp) {
                           am.setStreamVolume(AudioManager.STREAM_MUSIC, orgVol, 0);
                       }
                   });
                   vibrator.vibrate(1000);
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
        }
    }
}