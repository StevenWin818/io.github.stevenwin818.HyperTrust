package io.github.stevenwin818.HyperTrust;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 发送广播，由 SystemUI 进程中的 Receiver 代理启动 GMS 页面，绕过普通应用无法启动 GMS 内部 Activity 的限制
        Intent intent = new Intent("io.github.stevenwin818.HyperTrust.WAKE_TRUST_AGENT");
        intent.setPackage("com.android.systemui"); // 明确指向 SystemUI 提高安全性
        sendBroadcast(intent);
        
        // 立即关闭透明界面
        finish();
    }
}
