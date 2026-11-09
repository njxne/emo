# AGENTS.md

工作区只有一个项目：`app/`（Emo 笔记 App，底部液态玻璃导航栏）。

**液态玻璃渲染库不再 vendored**：直接依赖发布在 Maven Central 的官方库
`io.github.kyant0:backdrop:2.0.1`（`libs.kyant.backdrop`）。它自带 Compose 1.12.0 /
shapes 1.2.1 依赖。本地不再有 `backdrop/` 模块，禁止再拷贝渲染库源码进来（会用官方构件）。

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
- **液态玻璃效果库用官方 Maven 构件** `io.github.kyant0:backdrop:2.0.1`（KMP android
  target），app 直接 `implementation(libs.kyant.backdrop)`。API：`rememberLayerBackdrop` /
  `layerBackdrop` / `drawBackdrop` + effects(blur/lens/vibrancy) + highlight/shadow。

## 代码结构

- `app/src/main/kotlin/com/njxne/emo/notes/`
  - `MainActivity.kt` — 入口，`enableEdgeToEdge`
  - `NotesApp.kt` — 顶层：壁纸渐变 + `rememberLayerBackdrop` + 三个 tab + 编辑器
  - `data/NoteStore.kt` — 笔记仓库，JSON 文件持久化（`filesDir/notes.json`），StateFlow
  - `ui/components/liquid/` — 官方 AndroidLiquidGlass catalog 的底栏 UI 组件：
    `LiquidBottomTabs.kt`（容器，**官方仓库没把这个 UI 组件发布成 Maven 构件**，只能
    从源头源码保留）+ `LiquidBottomTab.kt` + 配套工具 `DampedDragAnimation` /
    `InteractiveHighlight` / `DragGestureInspector` / `awaitFrame`。这些是官方 demo 代码，
    不是仿写；修改只在纯 Android 适配（`kotlin.time.Clock`→`System.currentTimeMillis()`、
    KMP `expect awaitFrame()`→`withFrameNanos`）。
  - `ui/screens/` — 笔记列表 / 收藏 / 设置 / 编辑器
  - `ui/theme/Icons.kt` — 手写的 Material 风格矢量图标（`ImageVector.Builder`）
- 底栏是核心：`NotesApp` 用 `LiquidBottomTabs` 声明容器，向 `content`（RowScope）里放
  `LiquidBottomTab(onClick) { 图标 + 文字 }`。渲染全部走官方 backdrop Maven 库。

## Git

- 分支 `main`。提交前确认 `.gitignore` 排除了 `build/`、`.gradle/`、`*.apk`、`local.properties`。
- 未经主人确认不要代跑 `git add` / `commit` / `push` / `reset`。
