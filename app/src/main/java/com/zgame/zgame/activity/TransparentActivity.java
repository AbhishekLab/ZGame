package com.zgame.zgame.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.zgame.zgame.R;
import static android.util.Log.d;

public class TransparentActivity extends Activity {

    private boolean isActivityAlive = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);

        startActivityService();






        //findViewById(R.id.startDummyService).setOnClickListener(view -> startActivityService());
        findViewById(R.id.checkInstance).setOnClickListener(view -> stopActivityService());

        //finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        d("OnDestroyCalled", "OnDestroyCalled");
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActivityAlive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityAlive = false;
    }

    private void startActivityService() {
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, TransparentActivity.class);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        new Thread(() -> {

            for (int j = 0; j < 100; j++) {
                d("Helloworld", "HelloWorld" + j);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    d("Helloworld", "HelloWorld" + e.getMessage());
                }
            }

        }).start();
        finish();
    }

    private void stopActivityService() {
        if(isActivityAlive){
            Toast.makeText(this, "hello world Activity is ALIVE", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "hello world Activity is DEAD", Toast.LENGTH_SHORT).show();
        }
    }

    /*private void  permission() {
        requestAppPermissions(
                arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                R.string.permission_text,
                Constant.STORAGE_PERMISSION
        )
    }*/
}
