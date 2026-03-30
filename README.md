# HyperTrust (智能解锁修复)

HyperTrust 是一个基于 LSPosed 框架的 Android 系统级模块，专为修复小米澎湃 OS (HyperOS) 及部分深度定制系统中失效或被隐藏的 Google Smart Lock (延长解锁) 功能而设计。

## 主要功能

- 底层状态修复：通过 Hook V12 核心逻辑，接管并修正 derivedTrustState 的状态传递，恢复 Trust Agent (信任代理) 的正常解锁能力。

- 无缝配置路由：提供一个透明的桌面入口。点击应用图标后，自动唤醒被系统隐藏的 GMS (Google Play 服务) 延长解锁配置页面。

- 高安全性与零提权：摒弃传统的 Root (su) 提权命令和对 GMS 进程的直接代码注入。采用向系统界面 (com.android.systemui) 发送 IPC 广播的代理唤醒方案，全程无需 Root 权限，且绝对不会触发 Play Integrity 认证降级。

- 极致轻量：采用纯 Java (API 82) 编写，无任何冗余 UI、第三方依赖或库文件。

## 环境要求

- 系统版本：Android 12+ (API 31+)

- 框架依赖：已安装并激活 LSPosed (或兼容的 Xposed 衍生框架)

- 前置条件：系统已安装且正常运行 Google Play 服务 (GMS)

## 安装与配置

1. 下载并安装 HyperTrust 的 Release 版本 APK。

2. 打开 LSPosed 管理器，在模块列表中找到并启用 HyperTrust。

3. 配置作用域 (极其重要)：

	- 必须勾选 系统界面 (com.android.systemui) （用于接收高权限唤醒广播）。

	- 必须勾选 Android 系统框架 (android) 或其他对应的锁屏组件（用于拦截并修复系统底层的 Trust Agent 状态）。

	- 注意：请勿勾选 Google Play 服务，以免影响设备安全认证。

4. 重启设备（或强制重启系统界面）以使 Hook 逻辑生效。

5. 在桌面点击 HyperTrust 图标，即可直接进入延长解锁配置页面。