# HyperTrust (Extend Unlock Fix)

<p align="right">Language: <a href="../README.md">简体中文</a></p>

HyperTrust is a system-level Android module built on the LSPosed framework, designed to fix the Extend Unlock (formerly Smart Lock) feature when it is hidden or disabled on Xiaomi HyperOS.

## Main Features

- Core state repair: Hook the system UI to intercept and correct the propagation of `derivedTrustState`, restoring the Trust Agent's unlock capability.

- Seamless configuration routing: Provide an LSPosed execution entry. Tapping the app icon wakes the hidden Extend Unlock configuration page.、

## About Extend Unlock(formerly Smart Lock)

Extend Unlock is a Google-provided feature that allows the device to stay unlocked in trusted environments or when connected to trusted devices. For official documentation, see:

- https://support.google.com/android/answer/9075927

## Requirements

- Android 12+ (API 31+), Hyper OS 1.0+
- LSPosed (or compatible Xposed-derived framework) installed and active
- Google Play services (GMS) installed and functioning on the device

## Installation & Configuration
1. Download and install the HyperTrust Release APK.

2. Open the LSPosed manager, find HyperTrust in the module list and enable it; in the scope configuration, check the System UI (`com.android.systemui`).

3. In the system settings, locate the Trust Agent and enable Extend Unlock.

4. Reboot the device (or force-restart SystemUI) to apply the hook logic.

5. In LSPosed, find and open the HyperTrust configuration page, then tap the execute icon at the bottom-right to open the Extend Unlock configuration page.

> This English translation was provided by AI.

---

> If you spot any inaccuracies in this translation, please refer to the Chinese README (`../README.md`) as the authoritative source.
