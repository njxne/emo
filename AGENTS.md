# AGENTS.md

工作区现在只有一个项目：`app/`（Emo 笔记 App，底部液态玻璃导航栏）+ `backdrop/`（从
Kyant0/AndroidLiquidGlass vendored 进来的液态玻璃效果库）。旧的 `photo-answer-android/`
已删除，相关文档作废。

## 构建

```bash
./gradlew :app:assembleDebug        # debug APK
./gradlew :app:assembleRelease      # release APK（未签名）
```

产物在 `app/build/outputs/apk/`。**本机是 aarch64 Termux，AGP 自带的 aapt2 是 x86_64，
跑不了**——所以本地通常编译不通过，构建走 GitHub Actions（`.github/workflows/android.yml`，
push 到 main 自动触发，JDK 21 + ubuntu x86_64）。改完代码本地只能静态检查，靠 CI 验证。

## 版本契约（别随便改）

- AGP 9.3.2 / Kotlin 2.4.10 / Compose 1.12.0 / shapes 1.2.1，见 `gradle/libs.versions.toml`。
  Compose 1.12 是 KMP 发布，Android 类在 `-android` 变体里，Gradle 会自动解析，别手写坐标。
- **AGP 9.0 起内置 Kotlin 支持，不要再加 `org.jetbrains.kotlin.android` 插件**（Kotlin 插件会直接报错
  并提示移除）。两个模块只保留 `com.android.*` + `org.jetbrains.kotlin.plugin.compose`。
  `kotlin { compilerOptions { jvmTarget } }` 仍可用于指定 jvmTarget（app=17，backdrop=11）。
- `minSdk = 28`（Android 9+）。backdrop 库 minSdk 21，但它的 RuntimeShader/RenderEffect
  在 API 31/33 以下自动降级，别在库外面假设这些能力一定可用。
- **backdrop 库已从 KMP 转成纯 Android library**：commonMain+androidMain 合并进
  `src/main/kotlin`，expect/actual 全部去掉（Kotlin 2.4 纯模块不支持 expect/actual，
  会报 `'expect' and 'actual' declarations can be used only in multiplatform projects`）。
  android 实现文件加了 `.actual.kt`/`.android.kt` 后缀避免和 expect 版同名冲突。

## 代码结构

- `app/src/main/kotlin/com/njxne/emo/notes/`
  - `MainActivity.kt` — 入口，`enableEdgeToEdge`
  - `NotesApp.kt` — 顶层：壁纸渐变 + `rememberLayerBackdrop` + 三个 tab + 编辑器
  - `data/NoteStore.kt` — 笔记仓库，JSON 文件持久化（`filesDir/notes.json`），StateFlow
  - `ui/components/liquid/` — **从 AndroidLiquidGlass catalog 原样搬来的官方液玻璃底栏**：
    `LiquidBottomTabs.kt`（容器）+ `LiquidBottomTab.kt`（单 tab）+ 其配套工具
    `DampedDragAnimation` / `InteractiveHighlight` / `DragGestureInspector` / `awaitFrame`
  - `ui/screens/` — 笔记列表 / 收藏 / 设置 / 编辑器
  - `ui/theme/Icons.kt` — 手写的 Material 风格矢量图标（`ImageVector.Builder`）
- 底栏是核心，直接使用官方组件（别再手写仿品）：`NotesApp` 用 `LiquidBottomTabs`
  声明容器，向 `content`（RowScope）里放 `LiquidBottomTab(onClick) { 图标 + 文字 }`。
  官方实现含按压挤扁、横向拖动切 tab、镜头畸变 lens、随动光斑高光、内外阴影。
  工具文件里把 `kotlin.time.Clock` 换成了 `System.currentTimeMillis()`，把 KMP 的
  `expect fun awaitFrame()` 换成了 `withFrameNanos` 实现——因为本模块是纯 Android。

## Git

- 分支 `main`。提交前确认 `.gitignore` 排除了 `build/`、`.gradle/`、`*.apk`、`local.properties`。
- 未经主人确认不要代跑 `git add` / `commit` / `push` / `reset`。
