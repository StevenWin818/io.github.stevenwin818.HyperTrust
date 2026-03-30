# HyperTrust (延长解锁修复)

<p align="right">切换语言: <a href="docs/README.en.md">English</a></p>

HyperTrust 是一个基于 LSPosed 框架的 Android 系统级模块，专为修复小米澎湃 OS (HyperOS) 系统中被隐藏且失效的 延长解锁 (Extend Unlock，原称 Smart Lock) 功能而设计。

## 主要功能

- 底层状态修复：通过 Hook 系统界面，接管并修正 derivedTrustState 的状态传递，恢复 Trust Agent (可信代理) 的正常解锁能力。

- 无缝配置路由：提供一个 LSPosed 执行入口。可以打开被系统隐藏的延长解锁配置页面。

## 环境要求

- 系统版本：Android 12+ (API 31+), Hyper OS 1.0+

- 框架依赖：已安装并激活 LSPosed (或兼容的 Xposed 衍生框架)

- 前置条件：系统已安装且正常运行 Google Play 服务 (GMS)

## 关于延长解锁（Extend Unlock）

延长解锁（原称 Smart Lock）是 Google 提供的功能，允许在受信任的环境或设备上自动解除锁屏。有关官方说明与配置详情，请参阅：

- 官方文档：https://support.google.com/android/answer/9075927

## 安装与配置

1. 下载并安装 HyperTrust 的 Release 版本 APK。

2. 打开 LSPosed 管理器，在模块列表中找到并启用 HyperTrust，配置作用域勾选系统界面 (com.android.systemui)。

3. 在系统中找到可信代理，打开延长解锁。

4. 重启设备（或强制重启系统界面）以使 Hook 逻辑生效。

5. 在 LSPosed 中找到并进入 HyperTrust 配置页面，点击右下角执行图标，即可进入延长解锁配置页面。
