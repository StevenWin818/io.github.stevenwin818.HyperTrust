package io.github.stevenwin818.HyperTrust;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.SparseBooleanArray;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ExtendUnlockHook implements IXposedHookLoadPackage {

    private static final String ACTION_WAKE_TRUST_AGENT = "io.github.stevenwin818.HyperTrust.WAKE_TRUST_AGENT";
    private static final String PERMISSION_WAKE = "io.github.stevenwin818.HyperTrust.PERMISSION_WAKE";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.android.systemui")) {
            return;
        }

        XposedHelpers.findAndHookMethod(
                "com.android.keyguard.KeyguardUpdateMonitor",
                lpparam.classLoader,
                "getUserHasTrust",
                int.class,
                new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        int userId = (int) param.args[0];
                        Object updateMonitor = param.thisObject;

                        boolean isSimSecure = (boolean) XposedHelpers.callMethod(updateMonitor, "isSimPinSecure");
                        boolean isBiometricAllowed = (boolean) XposedHelpers.callMethod(updateMonitor, "isUnlockingWithBiometricAllowed", true);

                        Context context = (Context) XposedHelpers.getObjectField(updateMonitor, "mContext");
                        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

                        // 使用反射调用隐藏 API (isDeviceSecure/isDeviceLocked)
                        boolean isSecure = (boolean) XposedHelpers.callMethod(keyguardManager, "isDeviceSecure", userId);
                        boolean isLocked = (boolean) XposedHelpers.callMethod(keyguardManager, "isDeviceLocked", userId);

                        boolean derivedTrustState = isSecure && !isLocked;

                        // 同步清理内部缓存，防止 UI 状态不刷新
                        SparseBooleanArray trustArray = (SparseBooleanArray) XposedHelpers.getObjectField(updateMonitor, "mUserHasTrust");
                        if (trustArray != null) {
                            trustArray.put(userId, derivedTrustState);
                        }

                        return !isSimSecure && derivedTrustState && isBiometricAllowed;
                    }
                }
        );

        // 权限绕过逻辑：在 SystemUI 进程注册代理广播，实现免 Root 唤醒 GMS 设置页
        if (lpparam.processName.equals("com.android.systemui")) {
            XposedHelpers.findAndHookMethod(
                    Application.class.getName(),
                    lpparam.classLoader,
                    "onCreate",
                    new XC_MethodHook() {
                        @SuppressLint("UnspecifiedRegisterReceiverFlag")
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Application app = (Application) param.thisObject;

                            BroadcastReceiver receiver = new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {
                                    try {
                                        Intent uiIntent = new Intent();
                                        uiIntent.setComponent(new ComponentName(
                                                "com.google.android.gms",
                                                "com.google.android.gms.trustagent.TrustAgentSearchEntryPointActivity"
                                        ));
                                        uiIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(uiIntent);
                                    } catch (Exception ignored) {
                                    }
                                }
                            };

                            IntentFilter filter = new IntentFilter(ACTION_WAKE_TRUST_AGENT);
                            
                            // 修复广播安全漏洞：要求发送者持有签名级权限
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                app.registerReceiver(receiver, filter, PERMISSION_WAKE, null, Context.RECEIVER_EXPORTED);
                            } else {
                                app.registerReceiver(receiver, filter, PERMISSION_WAKE, null);
                            }
                        }
                    }
            );
        }
    }
}
