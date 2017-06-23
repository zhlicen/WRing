package live.a23333.wring;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.wearable.view.WatchViewStub;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;


public class MainActivity extends Activity {

    private Button mBtnRing;
    private Button mBtnSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mBtnRing = (Button) stub.findViewById(R.id.id_btn_ring);
                Button btnRingClick = (Button) stub.findViewById(R.id.id_btn_ring_click);
                btnRingClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isRingWorking()) {
                            Toast.makeText(MainActivity.this, getString(R.string.disable_ntf),
                                    Toast.LENGTH_SHORT).show();
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

                mBtnSms = (Button) stub.findViewById(R.id.id_btn_sms);
                Button btnSmsClick = (Button) stub.findViewById(R.id.id_btn_sms_click);
                btnSmsClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isSmsWorking()) {
                            Toast.makeText(MainActivity.this, getString(R.string.disable_ntf),
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            startActivity(i);
                        }
                        else {
                            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},
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
        if (isRingWorking()) {
            mBtnRing.setBackground(getDrawable(R.drawable.check));
        }
        else {
            mBtnRing.setBackground(getDrawable(R.drawable.uncheck));
        }

        if (isSmsWorking()) {
            mBtnSms.setBackground(getDrawable(R.drawable.check));

        }
        else {
            mBtnSms.setBackground(getDrawable(R.drawable.uncheck));
        }
        updateService();
    }

    public boolean isRingWorking(){
        return checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public boolean  isSmsWorking(){
        return checkSelfPermission(Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }

    public void updateService() {
        if(isRingWorking() || isSmsWorking()) {
            if(WakeupService.mInstance == null) {
                Intent i = new Intent(this, WakeupService.class);
                startService(i);
            }
        }
        if(!isRingWorking() && !isSmsWorking()) {
            if(WakeupService.mInstance != null) {
                Intent i = new Intent(this, WakeupService.class);
                stopService(i);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mBtnRing != null) {
            updateBtnStatus();
        }
    }
}
