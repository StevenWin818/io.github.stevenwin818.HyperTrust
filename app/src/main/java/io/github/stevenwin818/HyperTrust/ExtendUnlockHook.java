package io.github.stevenwin818.HyperTrust;

import android.app.KeyguardManager;
import android.content.Context;
import android.util.SparseBooleanArray;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ExtendUnlockHook implements IXposedHookLoadPackage {

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

                    // 1. 保留原生安全：SIM 卡锁校验
                    boolean isSimSecure = (boolean) XposedHelpers.callMethod(updateMonitor, "isSimPinSecure");

                    // 2. 保留原生安全：强认证校验
                    boolean isBiometricAllowed = (boolean) XposedHelpers.callMethod(updateMonitor, "isUnlockingWithBiometricAllowed", true);

                    // 3. 核心侧信道推导：获取底层真实状态
                    Context context = (Context) XposedHelpers.getObjectField(updateMonitor, "mContext");
                    KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                    
                    // 使用 XposedHelpers 调用隐藏 API
                    boolean isSecure = (boolean) XposedHelpers.callMethod(keyguardManager, "isDeviceSecure", userId);
                    boolean isLocked = (boolean) XposedHelpers.callMethod(keyguardManager, "isDeviceLocked", userId);
                    
                    boolean derivedTrustState = isSecure && !isLocked;

                    // 4. 解决信号粘连：双向同步清理
                    SparseBooleanArray trustArray = (SparseBooleanArray) XposedHelpers.getObjectField(updateMonitor, "mUserHasTrust");
                    if (trustArray != null) {
                        trustArray.put(userId, derivedTrustState);
                    }

                    // 5. 对齐 AOSP Android 14 原生逻辑
                    return !isSimSecure && derivedTrustState && isBiometricAllowed;
                }
            }
        );
    }
}
