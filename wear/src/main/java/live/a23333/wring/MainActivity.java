package live.a23333.wring;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button mBtnMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mBtnMain = (Button) stub.findViewById(R.id.id_btn_main);
                mBtnMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isWorking()) {
                            Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            startActivity(i);
                        }
                        else {
                            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                                    5);
                        }
                    }
                });
                updateBtnStatus();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 5) {
            updateBtnStatus();
        }
    }


    public void updateBtnStatus() {
        if (isWorking()) {
            mBtnMain.setText("服务开启，点击关闭权限，停止服务");
            Intent i = new Intent(this, WakeupService.class);
            startService(i);

        }
        else {
            mBtnMain.setText("服务停止，点击授权，开启服务");
            Intent i = new Intent(this, WakeupService.class);
            stopService(i);
        }
    }

    public boolean isWorking(){
        return checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mBtnMain != null) {
            updateBtnStatus();
        }
    }
}
