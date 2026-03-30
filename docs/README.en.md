# HyperTrust (Extended Unlock Fix)

<p align="right">Language: <a href="../README.md">简体中文</a></p>

HyperTrust is a system-level Android module built on the LSPosed framework designed to restore Google Extended Unlock(formerly Smart Lock) functionality that may be broken or hidden on Xiaomi HyperOS.
## Main Features

- Core state repair: Hook into V12 core logic to correct the derivedTrustState propagation, restoring the Trust Agent's unlock capability.

- Seamless configuration routing: Provides a transparent desktop entry. Tapping the app icon wakes the hidden GMS (Google Play services) Extended Unlock configuration page.

- High security, zero privilege escalation: Avoids root (su) and direct code injection into GMS. Uses a proxy wake approach by sending an IPC broadcast to the system UI (`com.android.systemui`), requiring no root and minimizing impact on Play Integrity.

- Lightweight: Pure Java (Xposed API 82), no extra UI or third-party libs.

## Requirements

- Android 12+ (API 31+), Hyper OS 1.0+
- LSPosed (or compatible Xposed-derived framework) installed and active
- Google Play services (GMS) installed and functioning on the device

## Installation & Configuration

1. Install the HyperTrust Release APK on the target device.
2. Open LSPosed manager and enable the HyperTrust module.
3. Scope configuration (important):

   - Enable `com.android.systemui` (to receive high-privilege wake broadcasts).
   - Enable the Android framework (`android`) or other lockscreen-related components (to intercept and repair Trust Agent state).
   - Do NOT enable Google Play services in LSPosed to avoid affecting device attestation.

4. Reboot the device (or restart SystemUI) to apply hooks.
5. Tap the HyperTrust icon on the launcher to open the Extended Unlock configuration page.

## About Extended Unlock(formerly Smart Lock)

Extended Unlock is a Google-provided feature that allows the device to stay unlocked in trusted environments or when connected to trusted devices. For official documentation, see:

- https://support.google.com/android/answer/9075927

> This English translation was provided by AI.

---

> If you spot any inaccuracies in this translation, please refer to the Chinese README (`../README.md`) as the authoritative source.
