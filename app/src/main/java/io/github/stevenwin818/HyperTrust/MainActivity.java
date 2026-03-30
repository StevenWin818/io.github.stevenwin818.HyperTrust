package io.github.stevenwin818.HyperTrust;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(
                    "com.google.android.gms",
                    "com.google.android.gms.trustagent.GoogleTrustAgentCheckConfiguredActivity"
            ));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.toast_gms_not_found), Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}