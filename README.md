![version: 0.11.2](https://img.shields.io/badge/kobweb-0.11.2-blue)
![version: 0.9.10](https://img.shields.io/badge/kobweb_cli-0.9.10-blue)
<br>
![kotlin: 1.7.20](https://img.shields.io/badge/kotlin-1.7.20-blue?logo=kotlin)
![compose: 1.2.1](https://img.shields.io/badge/compose-1.2.1-blue?logo=jetpackcompose)
<br>
<a href="https://discord.gg/5NZ2GKV5Cs">
<img alt="Varabyte Discord" src="https://img.shields.io/discord/886036660767305799.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2" />
</a>
[![Mastodon Follow](https://img.shields.io/mastodon/follow/109382855401210782?domain=https%3A%2F%2Ffosstodon.org&style=social)](https://fosstodon.org/@bitspittle)

# K🕸️bweb

Kobweb is an opinionated Kotlin framework for creating websites and web apps, built on top of
[Compose for Web](https://compose-web.ui.pages.jetbrains.team) and inspired by [Next.js](https://nextjs.org)
and [Chakra UI](https://chakra-ui.com).

```kotlin
@Page
@Composable
fun HomePage() {
  Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
    Row(Modifier.align(Alignment.End)) {
      var colorMode by rememberColorMode()
      Button(
        onClick = { colorMode = colorMode.opposite() },
        Modifier.clip(Circle())
      ) {
        Box(Modifier.margin(4.px)) {
          // Includes support for Font Awesome icons
          if (colorMode.isLight()) FaSun() else FaMoon()
        }
      }
    }
    H1 {
      Text("Welcome to Kobweb!")
    }
    Row(Modifier.whiteSpace(WhiteSpace.PreWrap)) { // Preserve trailing whitespace
      SpanText("Create rich, dynamic web apps with ease, leveraging ")
      Link("https://kotlinlang.org/", "Kotlin")
      SpanText(" and ")
      Link("https://compose-web.ui.pages.jetbrains.team/", "Compose for Web")
    }
  }
}
```

<p align="center">
<img src="https://github.com/varabyte/media/raw/main/kobweb/screencasts/kobweb-welcome.gif" />
</p>

---

**Kobweb is still a technology preview, but it's usable, and it is getting close to stabilization**. Please consider
starring the project to indicate interest, so we know we're creating something the community wants.
[How ready is it? ▼](https://github.com/varabyte/kobweb#can-we-kobweb-yet)

Our goal is to provide:

* an intuitive structure for organizing your Kotlin website or web app
* automatic handling of routing between pages
* a collection of useful _batteries included_ widgets built on top of Compose for Web
* an environment built from the ground up around live reloading
* static site exports for improved SEO and potentially cheaper server setups
* shared, rich types between client and server
* out-of-the-box Markdown support
* an open source foundation that the community can extend
* and much, much more!

Here's a demo where we create a Compose for Web project from scratch with Markdown support and live reloading, in under
10 seconds:

https://user-images.githubusercontent.com/43705986/135570277-2d67033a-f647-4b04-aac0-88f8992145ef.mp4

# Trying it out yourself

The first step is to get the Kobweb binary. You can install it, download it, and/or build it, so we'll include
instructions for all these approaches.

## Install the Kobweb binary

*Major thanks to [aalmiray](https://github.com/aalmiray) and [helpermethod](https://github.com/helpermethod) to helping
me get these installation options working. Check out [JReleaser](https://github.com/jreleaser/jreleaser) if you ever
need to do this in your own project!*

### [Homebrew](https://brew.sh/)

*OS: Mac and Linux*

```bash
$ brew install varabyte/tap/kobweb
```

### [Scoop](https://scoop.sh/)

*OS: Windows*

```shell
# Note: Adding buckets only has to be done once.

# Feel free to skip java if you already have it
> scoop bucket add java
> scoop install java/openjdk

# Install kobweb
> scoop bucket add varabyte https://github.com/varabyte/scoop-varabyte.git
> scoop install varabyte/kobweb
```

### [SDKMAN!](https://sdkman.io/)

*OS: Windows, Mac, and \*nix*

```shell
$ sdk install kobweb
```

### Arch Linux

*Thanks a ton to [aksh1618](https://github.com/aksh1618) for adding support for this target!*

With an AUR helper:

```shell
$ trizen -S kobweb
```

Without an AUR helper:

```shell
$ git clone https://aur.archlinux.org/kobweb.git
$ cd kobweb
$ makepkg -si
```

### Don't see your favorite package manager?

Please see: https://github.com/varabyte/kobweb/issues/117 and consider leaving a comment!

## Download the Kobweb binary

Our binary artifact is hosted on GitHub. To download latest:

```bash
$ cd /path/to/applications

# You can either pull down the zip file

$ wget https://github.com/varabyte/kobweb/releases/download/cli-v0.9.10/kobweb-0.9.10.zip
$ unzip kobweb-0.9.10.zip

# ... or the tar file

$ wget https://github.com/varabyte/kobweb/releases/download/cli-v0.9.10/kobweb-0.9.10.tar
$ tar -xvf kobweb-0.9.10.tar
```

and I recommend adding it to your path, either directly:

```bash
$ PATH=$PATH:/path/to/applications/kobweb-0.9.10/bin
$ kobweb version # to check it's working
```

or via symbolic link:

```bash
$ cd /path/to/bin # some folder you've created that's in your PATH
$ ln -s /path/to/applications/kobweb-0.9.10/bin/kobweb kobweb
```

## Build the Kobweb binary

Although we host Kobweb artifacts on GitHub, it's easy enough to build your own.

**Note:** Building Kobweb requires JDK11 or newer. If you don't already have this set up, the easiest way is to
[download a JDK](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html), unzip it somewhere,
and update your `JAVA_HOME` variable to point at it.

```bash
JAVA_HOME=/path/to/jdks/corretto-11.0.12
# ... or whatever version or path you chose
```

With `JAVA_HOME` set up, building is just a single Gradle command:

```bash
$ cd /path/to/src/root
$ git clone --recurse-submodules https://github.com/varabyte/kobweb
$ cd kobweb
$ ./gradlew :cli:kobweb:installDist
```

Finally, update your PATH:

```bash
$ PATH=$PATH:/path/to/src/root/kobweb/cli/kobweb/build/install/kobweb/bin
$ kobweb version # to check it's working
```

## Create your Kobweb site

```bash
$ cd /path/to/projects/
$ kobweb create site
```

You'll be asked a few questions required for setting up your project.

You don't need to create a root folder for your project ahead of time - the setup process will prompt you for one to
create.

When finished, you'll have a basic project with three pages - a home page, an about page, and a markdown page - and some
components (which are collections of reusable, composable pieces). Your own directory structure should look something
like:

```
my-project
└── src/jsMain
    ├── kotlin.org.example.myproject
    │   ├── components
    │   │   ├── layouts
    │   │   │   └── PageLayout.kt
    │   │   ├── sections
    │   │   │   └── NavHeader.kt
    │   │   └── widgets
    │   │       └── GoHomeLink.kt
    │   ├── pages
    │   │   ├── About.kt
    │   │   └── Index.kt
    │   └── MyApp.kt
    └── resources/markdown
        └── Markdown.md
```

Note that there's no index.html or routing logic anywhere! We generate that for you automatically when you run Kobweb.
Which brings us to the next section...

## Run your Kobweb site

```bash
$ cd /path/to/projects/root/your-project
$ kobweb run
```

This command spins up a webserver at http://localhost:8080. If you want to configure the port, you can do so by editing
your project's `.kobweb/conf.yaml` file.

You can open your project in IntelliJ and start editing it. While Kobweb is running, it will detect changes, recompile,
and deploy updates to your site automatically.

### Using IntelliJ

If you don't want to keep a separate terminal window open beside your IDE window, you may prefer alternate solutions.

#### Terminal tool window

Use the [IntelliJ terminal tool window](https://www.jetbrains.com/help/idea/terminal-emulator.html).

You can run `kobweb` within it, and if you run into a compile error, the stack trace lines will get decorated with
links, making it easy to navigate to the relevant source.

#### Gradle commands

Run gradle commands directly. `kobweb` itself delegates to Gradle, but nothing is stopping you from calling the commands
yourself.

To start a Kobweb server, execute the `kobwebStart -t` command, and to stop it later, use the `kobwebStop` command. The
`-t` argument (or, `--continuous`) tells Gradle to watch for file changes, which gives you live loading behavior.

You can read all about [IntelliJ's Gradle integration here](https://www.jetbrains.com/help/idea/gradle.html), but in
general, you should create two run configurations: one for `kobwebStart -t` and one for `kobwebStop`. To do this, start
from [these instructions](https://www.jetbrains.com/help/idea/run-debug-gradle.html).

## Running examples

Kobweb will provide a growing collection of samples for you to learn from. To see what's available, run:

```bash
$ kobweb list

You can create the following Kobweb projects by typing `kobweb create ...`

• examples/jb/counter: A very minimal site with just a counter (based on the Jetbrains tutorial)
• examples/todo: An example TODO app, showcasing client / server interactions
• site: A template for a minimal site that demonstrates the basic features of Kobweb
```

For example, `kobweb create examples/todo` will instantiate a TODO app locally.

# Beginner topics

Kobweb, at its core, is a handful of classes responsible for trimming away much of the boilerplate around building a
Compose for Web app, such as routing and configuring basic CSS styles.

Kobweb is also a CLI binary of the same name which provides commands to handle the tedious parts of building and / or
running a Compose for Web app. We want to get that stuff out of the way, so you can enjoy focusing on the more
interesting work!

## Create a page

Creating a page is easy! It's just a normal `@Composable` method. To upgrade your composable to a page, all you need to
do is:

1. Define your composable in a file somewhere under the `pages` package in your `jsMain` source directory.
1. Annotate it with `@Page`

Just from that, Kobweb will create a site entry for you automatically.

For example, if I create the following file:

```kotlin
// jsMain/kotlin/com/example/mysite/pages/admin/Settings.kt

@Page
@Composable
fun SettingsPage() {
    /* ... */
}
```

this will create a page that I can then visit by going to `mysite.com/admin/settings`.

**Note:** The last part of a URL, here `settings`, is called a *slug*.

By default, the slug comes from the file name, but this behavior can be overridden (more on that shortly).

The file name `Index.kt` is special. If a page is defined inside such a file, it will be treated as the default page
under that URL. For example, a page defined in `.../pages/admin/Index.kt` will be visited if the user visits
`mysite.com/admin/`.

### Route Override

If you ever need to change the route generated for a page, you can set the `Page` annotation's `routeOverride` field:

```kotlin
// jsMain/kotlin/com/example/mysite/pages/admin/Settings.kt

@Page(routeOverride = "config")
@Composable
fun SettingsPage() {
    /* ... */
}
```

The above would create a page you could visit by going to `mysite.com/admin/config`.

`routeOverride` can additionally contain slashes, and if the value begins and/or ends with a slash, that has a special
meaning.

* Begins with a slash - represent the whole route from the root
* Ends with a slash - a slug will still be generated from the filename and appended to the route.

And if you set the override to "index", that behaves the same as setting the file to `Index.kt` as described above.

Some examples can clarify these rules (and how they behave when combined). Assuming we're defining a page for our site
`example.com` within the file `a/b/c/Slug.kt`:

| Annotation              | Resulting URL                   |
|-------------------------|---------------------------------|
| `@Page`                 | `example.com/a/b/c/slug`        |
| `@Page("other")`        | `example.com/a/b/c/other`       |
 | `@Page("index")`        | `example.com/a/b/c/`            |
 | `@Page("d/e/f/")`       | `example.com/a/b/c/d/e/f/slug`  |
 | `@Page("d/e/f/other")`  | `example.com/a/b/c/d/e/f/other` |
 | `@Page("/d/e/f/")`      | `example.com/d/e/f/slug`        |
 | `@Page("/d/e/f/other")` | `example.com/d/e/f/other`       |
 | `@Page("/")`            | `example.com/slug`              |
 | `@Page("/other")`       | `example.com/other`             |

⚠️ We close this section with a warning - despite the flexibility allowed here, you should not be using this feature
frequently, if at all. A Kobweb project benefits from the fact that a user can easily associate a URL on your site with
a file in your codebase, but this feature allows you to break those assumptions. It is mainly provided to enable
dynamic routing (see the section below) or enabling a URL name that uses characters which aren't allowed in Kotlin
filenames.

### PackageMapping

If you don't want to change your slug but you *do* want to change a part of the route, you don't have to use a `Page`
annotation for this. You can instead register a package mapping with a `PackageMapping` file annotation. Doing so looks
like this:

```kotlin
// site/pages/blog/_2022/PackageMapping.kt
@file:PackageMapping("2022")

package site.pages.blog._2022

import com.varabyte.kobweb.core.PackageMapping
```

As with the `Page` route overrides, the main reason you'd want to do this is that Java / Kotlin package naming
requirements are much stricter than what you might want to allow in a URL part. `site.com/blog/2022/mypost` reads way
better than `site.com/blog/_2022/mypost`.

### Page context

Every page method provides access to its `PageContent` via the `rememberPageContext()` method.

A page's context provides it access to a router, allowing you to navigate to other pages, as well as other dynamic
information about the current page's URL (discussed in the following sections).

```kotlin
@Page
@Composable
fun ExamplePage() {
    val ctx = rememberPageContext()
    Button(onClick = { ctx.router.navigateTo("/other/page") }) {
        Text("Click me")
    }
}
```

### Query parameters

You can use the page context to check the values of any query parameters passed into the current page's URL.

So if you visit `site.com/posts?id=12345&mode=edit`, you can query those values like so:

```kotlin
@Page
@Composable
fun Posts() {
    val ctx = rememberPageContext()
    // Here, I'm assuming these params are always present, but you can
    // use `get` instead of `getValue` to handle the nullable case.
    val postId = ctx.params.getValue("id").toInt()
    val mode = EditMode.from(ctx.params.getValue("mode"))
    /* ... */
}
```

### Dynamic routes

In addition to query parameters, Kobweb supports embedding arguments directly in the URL itself. For example, you might
want to register the path `users/{user}/posts/{post}` which would be visited if the site visitor typed in a URL like
`users/bitspittle/posts/20211231103156`.

How do we set it up? Thankfully, it's fairly easy.

But first, notice that in the example dynamic route `users/{user}/posts/{post}` there are actually two different dynamic
parts, one in the middle and one at the tail end. These can be handled by the `PackageMapping` and `Page` annotations,
respectively.

#### PackageMapping

Pay attention to the use of the curly braces in the mapping name! That lets Kobweb know that this is a dynamic package.

```kotlin
// pages/users/user/PackageMapping.kt
@file:PackageMapping("{user}") // or @file:PackageMapping("{}")

package site.pages.users.user

import com.varabyte.kobweb.core.PackageMapping
```

If you pass an empty `"{}"` into the `PackageMapping` annotation, it directs Kobweb to use the name of the package
itself (i.e. `user` in this specific case).

#### Page

Like `PackageMapping`, the `Page` annotation can also take curly braces to indicate a dynamic value.

```kotlin
// pages/users/user/posts/Post.kt

@Page("{post}") // Or @Page("{}")
@Composable
fun PostPage() {
   /* ... */
}
```

An empty `"{}"` tells Kobweb to use the name of the current file.

Remember that the `Page` annotation allows you to rewrite the entire route. That value also accepts dynamic parts, so
you could even do something like:

```kotlin
// pages/users/user/posts/Post.kt

@Page("/users/{user}/posts/{post}") // Or @Page("/users/{user}/posts/{}")
@Composable
fun PostPage() {
    /* ... */
}
```

but with great power comes great responsibility. Tricks like this may be hard to find and/or update later, especially as
your project gets larger. While it works, you should only use this format in cases where you absolutely need to (perhaps
after a code refactor where you have to support legacy URL paths).

#### Querying dynamic route values

You query dynamic route values exactly the same as if you were requesting query parameters. That is, use `ctx.params`:

```kotlin
@Page("{}")
@Composable
fun PostPage() {
    val ctx = rememberPageContext()
    val postId = ctx.params.getValue("post")
    /* ... */
}
```

**Note:** You should avoid creating URL paths where the dynamic path and the query parameters have the same name, as in
`mysite.com/posts/{post}?post=...`, as there are no guarantees about which value will win out. Either way, one will be
overwritten by the other.

## Silk

Silk is a UI layer included with Kobweb and built upon Compose for Web. (To learn more about Compose for Web, please
visit [the official tutorials](https://github.com/JetBrains/compose-jb/tree/master/tutorials/Web/Getting_Started)).

While Compose for Web requires you to understand underlying html / css concepts, Silk attempts to abstract some of that
away, providing an API more akin to what you might experience developing a Compose app on Android or Desktop. Less
"div, span, flexbox, attrs, styles, classes" and more "Rows, Columns, Boxes, and Modifiers".

We consider Silk a pretty important part of the Kobweb experience, but it's worth pointing out that it's designed as an
optional component. You can absolutely use Kobweb without Silk. (You can also use Silk without Kobweb!).

You can also interleave Silk and Compose for Web components easily (as Silk is just composing them itself).

### Inline vs StyleSheet

For those new to web dev, it's worth understanding that there are two ways to set styles on your HTML elements: inline
and stylesheet.

Inline styles are defined on the element tag itself. In raw HTML, this might look like:

```html
<div style="background-color:black">
```

Meanwhile, any given html page can reference a list of stylesheets which can define a bunch of styles, where each style
is tied to a selector (a rule which _selects_ what elements those styles apply to).

A concrete example of a very short stylesheet can help here:

```css
body {
  background-color: black;
  color: magenta
}
#title {
  color: yellow
}
```

And you could use that stylesheet to style the following document:

```html
<body>
  <!-- Div gets background-color from "body" and foreground color from "#title" -->
  <div id="title">
      Yellow on green
  </div>
</body>
```

There's no hard and fast rule, but in general, when writing html / css by hand, stylesheets are often preferred over
inline styles as it better maintains a separation of concerns. That is, the html should represent the content of your
site, while the css controls the look and feel.

However! We're not writing html / css by hand. We're using Compose for Web! So the distinctions discussed up until now
are less important here.

That said, there are times when you have to use stylesheets, because without them you can't define styles for advanced
behaviors (particularly [pseudo classes](https://developer.mozilla.org/en-US/docs/Web/CSS/Pseudo-classes),
[pseudo elements](https://developer.mozilla.org/en-US/docs/Web/CSS/Pseudo-elements), and
[media queries](https://developer.mozilla.org/en-US/docs/Web/CSS/Media_Queries/Using_media_queries), the discussion of
which are outside the scope of this README). For example, you can't override the color of visited links without using a
stylesheet approach. So it's worth realizing there are occasional differences.

---

In general, when you pass styles defined on the fly into a composable widget in Silk, those will result in inline
styles, whereas if you use a `ComponentStyle` to define the styles, those will get embedded into the site's stylesheet:

```kotlin
// Uses inline styles
Box(Modifier.color(Colors.Red)) { /* ... */ }

// Uses a stylesheet
val MyBoxStyle = ComponentStyle("my-box") {
    base { Modifier.Color(Colors.Red) }
}
Box(MyBoxStyle.toModifier()) { /* ... */ }
```

We'll talk more about these approaches in the following sections.

One last note: debugging your page with browser tools may be easier if you lean on stylesheets over inline styles,
because it makes your DOM tree easier to look through without all that extra noise. As a result, I tend to use
`ComponentStyle` quite often.

### Modifier

Silk introduces the `Modifier` class, in order to provide an experience similar to what you find in Jetpack Compose.
(You can read [more about them here](https://developer.android.com/jetpack/compose/modifiers) if you're unfamiliar with
the concept).

In the world of Compose for Web, you can think of a `Modifier` as a wrapper on top of CSS styles and attributes. So
this:

```kotlin
Modifier.backgroundColor(Colors.Red).color(Colors.Green).padding(200.px)
```

when passed into a widget provided by Kobweb, like `Box`:

```kotlin
Box(Modifier.backgroundColor(Colors.Red).color(Colors.Green).padding(200.px)) {
    /* ... */
}
```

would generate an HTML tag with a style property like: `<div style="background:red;color:green;padding:200px">`

#### attrsModifier and styleModifier

There are a bunch of modifier extensions (and they're growing) provided by Kobweb, like `background`, `color`, and
`padding` above. But there are also two escape hatches anytime you run into a modifier that's missing:
`attrsModifier` and `styleModifier`.

Using them looks like this:

```kotlin
// Modify attributes of an element tag
// e.g. the "a", "b", and "c" in <tag a="..." b="..." c="..." />
Modifier.attrsModifier {
    onMouseDown { /* ... */ }
}

// Modify styles of an element tag
// e.g. the "a", "b", and "c" in `<tag style="a:...;b:...;c:..." />
Modifier.styleModifier {
    width(100.percent)
    height(50.percent)
}

// Note: Because "style" itself is an attribute, you can define styles in an attrsModifier:
Modifier.attrsModifier {
    style {
        width(100.percent)
        height(50.percent)
    }
}
// ... but in the above case, you should use a styleModifier for simplicity
```

### ComponentStyle

With Silk, you can define a style like so, using the `base` block:

```kotlin
val CustomStyle = ComponentStyle("custom") {
    base {
        Modifier.backgroud(Colors.Red)
    }
}
```

and convert it to a modifier by using `CustomStyle.toModifier()`. At this point, you can pass it into any composable
which takes a `Modifier` parameter:

```kotlin
// Approach #1 (uses inline styles)
Box(Modifier.backgroundColor(Colors.Red)) { /* ... */ }

// Appraoch #2 (uses stylesheets)
Box(CustomStyle.toModifier()) { /* ... */}
```

#### Additional states

So, what's up with the `base` block?

True, it looks a bit verbose on its own. However, you can define additional styles that take effect conditionally. The
base style will always apply first, but then additional styles can be applied based on what state the element is in. (If
multiple states are applicable at the same time, they will be applied in the order specified.)

Here, we create a style which is red by default, but green when the mouse hovers over it:

```kotlin
val CustomStyle = ComponentStyle("custom") {
    base {
        Modifier.color(Colors.Red)
    }

    hover {
        Modifier.color(Colors.Green)
    }
}
```

Kobweb provides a bunch of these state blocks for you for convenience, but for those who are CSS-savvy, you can always
define the CSS rule directly to enable more complex combinations or reference states that Kobweb hasn't added yet.

For example, this is identical to the above style definition:

```kotlin
val CustomStyle = ComponentStyle("custom") {
    base {
        Modifier.color(Colors.Red)
    }

    cssRule(":hover") {
        Modifier.color(Colors.Green)
    }
}
```

#### Breakpoints

There's a feature in the world of responsive html / css design called breakpoints, which confusingly have nothing to do
with debugging breakpoints. Rather, they specify size boundaries for your site when styles change. This is how sites
present content differently on mobile vs. tablet vs. desktop.

Kobweb provides four breakpoint sizes you can use for your project, which, including using no breakpoint size at all,
gives you five buckets you can work with when designing your site:

* no breakpoint - mobile (and larger)
* sm - tablets (and larger)
* md - desktops (and larger)
* lg - widescreen (and larger)
* xl - ultra widescreen (and larger)

You can change the default values of breakpoints for your site by adding an "@InitSilk" block to your code:

```kotlin
@InitSilk
fun initializeBreakpoints(ctx: InitSilkContext) {
    ctx.theme.breakpoints = BreakpointSizes(
        sm = 30.cssRem,
        md = 48.cssRem,
        lg = 62.cssRem,
        xl = 80.cssRem,
    )
}
```

To reference a breakpoint in a `ComponentStyle`, just invoke it:

```kotlin
val CustomStyle = ComponentStyle("custom") {
    base {
        Modifier.fontSize(24.px)
    }

    Breakpoint.MD {
        Modifier.fontSize(32.px)
    }
}
```

#### Color-mode aware

When you define a `ComponentStyle`, an optional field is available for you to use called `colorMode`:

```kotlin
val CustomStyle = ComponentStyle("custom") {
    base {
        Modifier.color(if (colorMode.isLight()) Colors.Red else Colors.Pink)
    }
}
```

Note that Silk provides a `SilkTheme` object you can reference in styles. For example, if you want to set your element's
color to match the color that we use for links, you can reference the `SilkTheme.palettes[colorMode]` object to do so:

```kotlin
val CustomStyle = ComponentStyle("custom") {
    base {
        Modifier.color(SilkTheme.palettes[colorMode].link.default)
    }
}
```

`SilkTheme` contains very simple (e.g. black and white) defaults, but you can override them in an `@InitSilk` method,
perhaps to something that is more brand aware:

```kotlin
@InitSilk
fun overrideSilkTheme(ctx: InitSilkContext) {
    ctx.theme.palettes = SilkPalettes(/* ... */)
}
```

#### ComponentVariant

With a style, you can also create a variant of that style (that is, additional modifications that are always applied
_on top of_ the style).

You define one using the `ComponentStyle.addVariant` method, but otherwise the declaration looks the same as defining a
`ComponentStyle`:

```kotlin
val CustomVariant = CustomStyle.addVariant("example-variant") {
    base {
        Modifier.backgroundColor(Colors.Green)
    }
}
```

Variants can be particularly useful if you're defining a custom widget that has default styles, but you want to give
callers an easy way to deviate from it in special cases.

For example, maybe you define a button widget (perhaps you're not happy with the one provided by Silk):

```kotlin
val MyButtonStyle = ComponentStyle("my-button") { /* ... */ }

// Note: Creates a style called "my-button-outline"
val OutlineButtonVariant = MyButtonStyle.addVariant("outline") { /* ... */ }

// Note: Creates a style called "my-button-invert"
val InvertButtonVariant = MyButtonStyle.addVariant("invert") { /* ... */ }
```

The `ComponentStyle.toModifier(...)` method, mentioned earlier, optionally takes a variant parameter. When passed in,
both styles will be applied -- the base style followed by the variant style.

For example, `MyButtonStyle.toModifier(OutlineButtonVariant)` applies the main button style first followed by additional
outline styling.

***Note:** Using a variant that was created from a different style will have no effect. In other words,
`LinkStyle.toModifier(OutlineButtonVariant)` will ignore the button variant in that case. We tried to use generics as a
fancy way to enforce this at compile time but ran into limitations with the Compose compiler (see
[Compose for Web bug #1333](https://github.com/JetBrains/compose-jb/issues/1333)). We may revisit this API design later
if resolved, but until then, don't do that!*

#### Writing custom widgets

While Silk methods are all written to support component styles and variants, if you ever want to write your own custom
widget that mimics Silk, your code should look something like:

```kotlin
val CustomWidgetStyle = ComponentStyle("custom-widget") { /* ... */ }

@Composable
fun CustomWidget(
    modifier: Modifier = Modifier,
    variant: ComponentVariant? = null,
    @Composable content: () -> Unit
) {
    val finalModifier = CustomWidgetStyle.toModifier(variant).then(modifier)
    Box(finalModifier, content)
}
```

In other words, you should take in an optional `ComponentVariant` parameter, and then you should apply the modifiers in
order of: base style, then variant, then finally user overrides.

A caller might call your widget one of several ways:

```kotlin
// Approach #1: Use default styling
CustomWidget { /* ... */ }

// Approach #2: Tweak default styling with a variant
CustomWidget(variant = TransparentWidgetVariant) { /* ... */ }

// Approach #3: Tweak default styling with user overrides
CustomWidget(Modifier.backgroundColor(Colors.Blue)) { /* ... */ }

// Approach #4: Tweak default styling with a variant and then user overrides
CustomWidget(Modifier.backgroundColor(Colors.Blue), variant = TransparentWidgetVariant) { /* ... */ }
```

### ElementRefScope and raw HTML elements

Occasionally, you may need access to the raw element backing the Silk widget you've just created. All Silk widgets
provide an optional `ref` parameter which take a listener that provide this information.

```kotlin
Box(
    ref = /* ... */
) {
    /* ... */
}
```

All callbacks (discussed shortly) will receive an `org.w3c.dom.Element` subclass. You can check out the
[Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/) class (and its often more
relevant [HTMLElement](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-element/) inheritor) to see the
methods and properties that are available on it.

For example, you could use this to set initial focus on one element in your DOM.

#### `ref`

Additionally, Silk provides a `ref` method which takes a callback and whose output can be passed into the `ref`
parameter:

```kotlin
Box(
    ref = ref { element ->
        // Triggered when this Box is first added into the DOM
        element.focus()
    }
)
```

The `ref { ... }` method can actually take one or more optional keys of any value. If any of these keys change on a
subsequent recomposition, the callback will be rerun:

```kotlin
val colorMode by rememberColorMode()
Box(
    // Callback will get triggered each time the color mode changes
    ref = ref(colorMode) { element -> /* ... */}
)
```

#### `disposableRef`

If you need to know both when the element enters AND exits the DOM, you can use `disposableRef` instead. With
`disposableRef`, the very last line in your block must be a call to `onDispose`:

```kotlin
val activeElements: MutableSet<HTMLElement> = /* ... */

/* ... later ... */

Box(
    ref = disposableRef { element ->
        activeElements.put(element)
        onDispose { activeElements.remove(element) }
    }
)
```

The `disposableRef` method can also take keys which rerun the listener if any of them change. The `onDispose` callback
will also be triggered in that case, as the old effect gets discarded.

#### `refScope`

And, finally, you may want to have multiple listeners that are recreated independently of one another based on different
keys. You can use `refScope` as a way to combine two or more `ref` and/or `disposableRef` calls in any combination:

```kotlin
val isFeature1Enabled: Boolean = /* ... */
val isFeature2Enabled: Boolean = /* ... */

Box(
    ref = refScope {
        ref(isFeature1Enabled) { element -> /* ... */ }
        dispoasbleRef(isFeature2Enabled) { element -> /* ... */; onDispose { /* ... */ } }
    }
)
```

### Font Awesome

Kobweb provides the `kobweb-silk-icons-fa` artifact which you can use in your project if you want access to all the free
Font Awesome (v6) icons.

Using it is easy! Search the [Font Awesome gallery](https://fontawesome.com/search?o=r&m=free), choose an
icon, and then call it using the associated Font Awesome icon composable.

For example, if I wanted to add the Kobweb-themed
[spider icon](https://fontawesome.com/icons/spider?s=solid&f=classic), I could call this in my Kobweb code:

```kotlin
FaSpider()
```

That's it!

Some icons have a choice between solid and outline versions, such as "Square"
([outline](https://fontawesome.com/icons/square?s=solid&f=classic) and
[filled](https://fontawesome.com/icons/square?s=regular&f=classic)). In that case, the default choice will be outline
mode, but you can pass in a style enum to control this:

```kotlin
FaSquare(style = IconStyle.FILLED)
```

All Font Awesome composables accept a modifier parameter, so you can tweak it further:

```kotlin
FaSpider(Modifier.color(Colors.Red))
```

***Note**: When you create a project using our `site` template, Font Awesome icons are included.*

### Material Design Icons

Kobweb provides the `kobweb-silk-icons-mdi` artifact which you can use in your project if you want access to all the
free Material Design icons.

Using it is easy! Search the [Material Icons gallery](https://fonts.google.com/icons?icon.set=Material+Icons), choose an
icon, and then call it using the associated Material Design Icon composable.

For example, let's say after a search I found and wanted to use their
[bug report icon](https://fonts.google.com/icons?icon.set=Material+Icons&icon.query=bug+report), I could call this in my
Kobweb code by converting the name to camel case:

```kotlin
MdiBugReport()
```

That's it!

Most material design icons support multiple styles: outlined, filled, rounded, sharp, and two-tone. Check the gallery
search link above to verify what styles are supported by your icon. You can identify the one you want to use by passing
it into the method's `style` parameter:

```kotlin
MdiLightMode(style = IconStyle.TWO_TONE)
```

All Material Design Icon composables accept a modifier parameter, so you can tweak it further:

```kotlin
MdiError(Modifier.color(Colors.Red))
```

## Intermediate topics

### Components: Layouts, Sections, and Widgets

Outside of pages, it is common to create reusable, composable parts. While Kobweb doesn't enforce any particular rule
here, we recommend a convention which, if followed, may make it easier to allow new readers of your codebase to get
around.

First, as a sibling to pages, create a folder called **components**. Within it, add:

* **layouts** - High-level composables that provide entire page layouts. Most (all?) of your `@Page` pages will start by
  calling a page layout function first. You may only have a single layout for your entire site.
* **sections** - Medium-level composables that represent compound areas inside your pages, organizing a collection of
  many children composables. If you have multiple layouts, it's likely sections would be shared across them. For
  example, nav headers and footers are great candidates for this subfolder.
* **widgets** - Low-level composables. Focused UI pieces that you may want to re-use all around your site. For example,
  a stylized visitor counter would be a good candidate for this subfolder.

### Specifying your application root

By default, Kobweb will automatically root every page to the [`KobwebApp` composable](https://github.com/varabyte/kobweb/blob/main/frontend/kobweb-core/src/jsMain/kotlin/com/varabyte/kobweb/core/App.kt)
(or, if using Silk, to a [`SilkApp` composable](https://github.com/varabyte/kobweb/blob/main/frontend/kobweb-silk/src/jsMain/kotlin/com/varabyte/kobweb/silk/SilkApp.kt)).
These perform some minimal common work (e.g. applying CSS styles) that should be present across your whole site.

This means if you register a page:

```kotlin
// jsMain/kotlin/com/example/mysite/pages/Index.kt

@Page
@Composable
fun HomePage() {
    /* ... */
}
```

then the final result that actually runs on your site will be:

```kotlin
// In a generated main.kt somewhere...

KobwebApp {
  HomePage()
}
```

It is likely you'll want to configure this further for your own application. Perhaps you have additional styles you'd
like to globally define (e.g. the default font used by your site). Perhaps you have some initialization logic that you'd
like to run before any page gets run (like logic for updating saved settings into local storage).

In this case, you can create your own root composable and annotate it with `@App`. If present, Kobweb will use that
instead of its own default. You should, of course, delegate to `KobwebApp` (or `SilkApp` if using Silk), as the
initialization logic from those methods should still be run.

Here's an example application composable override that I use in one of my own projects:

```kotlin
@App
@Composable
fun MyApp(content: @Composable () -> Unit) {
    SilkApp {
        val colorMode = getColorMode()
        LaunchedEffect(colorMode) { // Relaunched every time the color mode changes
            localStorage.setItem(COLOR_MODE_KEY, colorMode.name)
        }

        // A full screen Silk surface. Sets the background based on Silk's palette and animates color changes.
        Surface(Modifier.minHeight(100.vh)) {
            content()
        }
    }
}
```

You can only define *at most* a single `@App` on your site, or else the Kobweb Application plugin will complain at build
time.

### Define API routes

You can define and annotate methods which will generate server endpoints you can interact with. To add one:

1. Define your method (optionally `suspend`able) in a file somewhere under the `api` package your `jvmMain` source
   directory.
1. The method should take exactly one argument, an `ApiContext`.
1. Annotate it with `@Api`

For example, here's a simple method that echoes back an argument passed into it:

```kotlin
// jvmMain/kotlin/com/example/mysite/api/Echo.kt

@Api
fun echo(ctx: ApiContext) {
    // ctx.req is for the incoming request, ctx.res for responding back to the client

    // Params are parsed from the URL, e.g. here "/api/echo?message=..."
    val msg = ctx.req.params["message"] ?: ""
    ctx.res.setBodyText(msg)
}
```

After running your project, you can test the endpoint by visiting `mysite.com/api/echo?message=hello`

You can also trigger the endpoint in your frontend code by using the extension `api` property added to the
`kotlinx.browser.window` class:

```kotlin
@Page
@Composable
fun ApiDemoPage() {
  val coroutineScope = rememberCoroutineScope()

  Button(onClick = {
    coroutineScope.launch {
      println("Echoed: " + window.api.get("echo?message=hello")!!.decodeToString())
    }
  })
}
```

All the HTTP methods are supported (`post`, `put`, etc.).

If you know what you're doing, you can also use `window.fetch(...)` directly.

### Markdown

If you create a markdown file under the `jsMain/resources/markdown` folder, a corresponding page will be created for you
at build time, using the filename as its path.

For example, if I create the following file:

```markdown
// jsMain/resources/markdown/docs/tutorial/Kobweb.kt

# Kobweb Tutorial

...
```

this will create a page that I can then visit by going to `mysite.com/docs/tutorial/kobweb`

#### Front Matter

Front Matter is metadata that you can specify at the beginning of your document, like so:

```markdown
---
title: Tutorial
author: bitspittle
---

...
```

In a following section, we'll discuss how to embed code in your markdown, but for now, know that these key / value pairs
can be queried in code using the page's context:

```kotlin
@Composable
fun AuthorWidget() {
    val ctx = rememberPageContext()
    // Note: You can use `markdown!!` only if you're sure that
    // this composable is called while inside a page generated
    // from Markdown.
    val author = ctx.markdown!!.frontMatter.getValue("author").single()
    Text("Article by $author")
}
```

***Note:** If you're not seeing `ctx.markdown` autocomplete, you need to make sure you depend on the
`com.varabyte.kobwebx:kobwebx-markdown` artifact in your project's `build.gradle`*.

##### Root

Within your front matter, there's a special value which, if set, will be used to render a root `@Composable` that wraps
the code your markdown file would otherwise create. This is useful for specifying a layout for example:

```markdown
---
root: .components.layout.DocsLayout
---

# Kobweb Tutorial
```

The above will generate code like the following:

```kotlin
import com.mysite.components.layout.DocsLayout

@Composable
@Page
fun KobwebPage() {
    DocsLayout {
        H1 {
            Text("Kobweb Tutorial")
        }
    }
}
```

#### Kobweb Call

The power of Kotlin + Compose for Web is interactive components, not static text! Therefore, Kobweb Markdown support
enables special syntax that can be used to insert Kotlin code.

##### Block syntax

Usually, you will define widgets that belong in their own section. Just use three triple-curly braces to insert a
function that lives in its own block:

```markdown
# Kobweb Tutorial

...

{{{ .components.widgets.VisitorCounter }}}
```

which will generate code for you like the following:

```kotlin
@Composable
@Page
fun KobwebPage() {
    /* ... */
    com.mysite.components.widgets.VisitorCounter()
}
```

You may have noticed that the code path in the markdown file is prefixed with a `.`. When you do that, the final path
will automatically be prepended with your site's full package.

##### Inline syntax

Occasionally, you may want to insert a smaller widget into the flow of a single sentence. For this case, use the
`${...}` inline syntax:

```markdown
Press ${.components.widgets.ColorButton} to toggle the site's current color.
```

**Warning:** Spaces are not allowed within the curly braces! If you have them there, Markdown skips over the whole
thing and leaves it as text.

### Version Catalogs

The project templates created by Kobweb all embrace Gradle version catalogs, which are (at the time of writing this
README) a relatively new feature, so users may not be aware of it.

There is a file called `libs.versions.toml` that exists inside your project's root `gradle` folder. If you find yourself
wanting to tweak or add new versions to projects you originally created via `kobweb create`, that's where you'll find
them.

For example, here's the
[libs.versions.toml](https://github.com/varabyte/kobweb-site/blob/main/gradle/libs.versions.toml) we use for our own
landing site.

To read more about the feature, please check out the
[official docs](https://docs.gradle.org/current/userguide/platforms.html#sub:conventional-dependencies-toml).

# Advanced topics

## Multimodule

For simplicity, the default site Kobweb creates for you is a monolithic project. All your code is dumped into a single
module at the project root.

However, Kobweb is capable of splitting code up across modules. To do this, you move your components, pages, and/or
widgets to separate modules and apply the `com.varabyte.kobweb.library` plugin on them (while the main module applies
the `com.varabyte.kobweb.application` plugin.)

In other words, you can change a project layout from this:

```
my-project
├── build.gradle.kts
└── src/jsMain
    └── kotlin.org.example.myproject
        ├── components
        └── pages
```

To something like this:

```
my-project
├── sitelib
│   ├── build.gradle.kts # apply "com.varabyte.kobweb.library"
│   └── src/jsMain
│       └── kotlin.org.example.myproject.sitelib
│           ├── components
│           └── pages
└── site
    ├── build.gradle.kts # apply "com.varabyte.kobweb.application"
    └── src/jsMain
        └── kotlin.org.example.myproject.site
            ├── components
            └── pages
```

If you'd like to explore a multimodule project example, you can do so by running:

```bash
$ kobweb create examples/chat
```

which demonstrates a chat application with its auth and chat functionality managed in their own, separate modules.

## Templates

Kobweb provides its templates in a separate git repository, which is referenced within this project as a submodule for
convenience. To pull down everything, run:

```bash
/path/to/src/root
$ git clone --recurse-submodules https://github.com/varabyte/kobweb

# or, if you've already previously cloned kobweb...
/path/to/src/root/kobweb
$ git submodule update --init
```

## What about Multiplatform Widgets?

Jetbrains is working on an experimental project called "multiplatform widgets" which is supposed to bring the Desktop /
Android API to the web. And it may seem like the Kobweb + Silk approach will be obsolete when it is finished.

However, I've found there is a fundamental distance between Desktop / Android flavors of Compose and Compose for Web.
Specifically, Desktop / Android targets render to their own surface, while Web modifies an html / css DOM tree and
leaves it up to the browser to do the final rendering.

This has major implications on how similar the two APIs can get. For example, in Desktop / Android, the order you apply
modifiers matters, while in Web, this action simply sets html style properties under the hood, where order does not
matter.

One approach would be to own the entire rendering pipeline, ditching html / css entirely and targeting a full page
canvas. However, this has several limits:

* robots would lose the ability to crawl and index your site
* your UI will be opaque to the rich suite of dev tools that come bundled with browsers
* you won't have the ability to style unvisited vs visited links differently (this information is hidden from you by
  the browser and can only be set through html / css)
* you won't have the ability to turn elements on / off when printing the page
* accessibility tools for browsers might not work

It would also prevent a developer from making use of the rich ecosystem of Javascript libraries out there that modify
the DOM tree themselves.

For now, I am making a bet that the best way forward is to embrace the web, sticking to html / css, but providing a rich
UI library of widgets that hopefully makes it relatively rare for the developer to worry about it. For example, flexbox
is a very powerful component, but you'll find it's much easier to compose `Row`s and `Column`s together than trying to
remember if you should be justifying your items or aligning your content, even if `Row`s and `Column`s are just creating
the correct html / css for you behind the scenes.

I think there is value in supporting both approaches.

# Can We Kobweb Yet

Current state: **Foundations are in place! You may encounter API gaps.**

Kobweb is becoming quite functional. We are already using it to build https://kobweb.varabyte.com and
https://bitspittle.dev (depending on when you're reading this, they may still be fairly barebones, but these sites will
get more of our full attention as Kobweb is finally stabilizing).

At this point:

* It is easy to set up a new project and get things running quickly.
* The live reloading flow is pretty nice, and you'll miss it when you switch to projects that don't have it.
* It supports generating pages from Markdown that can reference your Composable code.
* While it's not quite server-side rendering, you can export static pages which will get hydrated on load.
* You can use the `Modifier` builder for a growing number of css properties.
* Silk components are color mode aware and support responsive behavior.

However, there's always more to do.

* The API surface is a bit lean in some areas right now, especially around Silk UI components
* The APIs that interact with Compose for Web may have some holes in them.
* A lot of detailed documentation is planned to go into the Kobweb site (linked just above) but it isn't done yet.

I think there's enough there now to let you do almost anything you'd want to do, as either Kobweb supports it or you can
escape hatch to underlying Compose for Web / Kotlin/JS approaches, but there might be some areas where it's still a bit
DIY. It would be great to get real world experience to hear what issues users are actually running into.

So, should you use Kobweb at this point? If you are...

* playing around with Compose for Web for the first time and want to get up and running quickly on a toy project:
    * **YES!!!** Please see the [connecting with us ▼](https://github.com/varabyte/kobweb#connecting-with-us) section
      below, we'd definitely love to hear from you. Now's a great time if you'd want to have a voice in the direction of
      this project.
* a Kotlin developer who wants to write a small web app or create a new blog from scratch:
    * **Worth a shot!** I think if you evaluate Kobweb at this point, you'll find a lot to like. You can get in touch
      with us at our Discord if you try it and have questions or run into missing features.
* someone who already has an existing project in progress and wants to integrate Kobweb into it:
    * **No** - this may never be a tenable path.
* a company:
    * **Probably not** (someday, we hope, but not yet)

# Connecting with us

* [Join my Discord!](https://discord.gg/5NZ2GKV5Cs)
* Follow me on Mastodon: [@bitspittle@fosstodon.org](https://fosstodon.org/@bitspittle)
* You can send direct queries to [my email](mailto:bitspittle@gmail.com)

# Filing issues and leaving feedback

It is still early days, and while we believe we've proven the feasibility of this approach at this point, there's still
plenty of work to do to get to a 1.0 launch! We are hungry for the community's feedback, so please don't hesitate to:

* [Open an issue](https://github.com/varabyte/kobweb/issues/new/choose)
* Contact us (using any of the ways mentioned above) telling us what features you want
* Ask us for guidance, especially as there are no tutorials yet (your questions can help us know what to write first!)

Thank you for your support and interest in Kobweb!