<!-- Logo -->
<p align="center">
  <a href="https://jitpack.io/#iGoodie/RuntimeGoodies">
    <img src="https://raw.githubusercontent.com/iGoodie/RuntimeGoodies/master/.github/assets/logo.png" height="150" alt="RuntimeGoodies Logo" aria-label="RuntimeGoodies Logo" />
  </a>
</p>

<!-- Slogan -->
<p align="center">
  A <a href="www.java.com">Java</a> library for representing, reading, validating and fixing externally stored files/configs. 
</p>

<!-- Badges -->
<p align="center">
  <!-- Main Badges -->
  <img src="https://raw.githubusercontent.com/iGoodie/RuntimeGoodies/master/.github/assets/main-badge.png" height="20px"/>
  <a href="https://jitpack.io/#iGoodie/RuntimeGoodies">
    <img src="https://jitpack.io/v/iGoodie/RuntimeGoodies.svg"/>
  </a>
  <a href="https://github.com/iGoodie/RuntimeGoodies/releases">
    <img src="https://img.shields.io/github/v/release/iGoodie/RuntimeGoodies"/>
  </a>
  <a href="https://github.com/iGoodie/RuntimeGoodies/releases">
    <img src="https://img.shields.io/github/v/release/iGoodie/RuntimeGoodies?include_prereleases&label=release-snapshot"/>
  </a>
  <a href="https://github.com/iGoodie/RuntimeGoodies">
    <img src="https://img.shields.io/github/languages/top/iGoodie/RuntimeGoodies"/>
  </a>

  <br/>

  <!-- Github Badges -->
  <img src="https://raw.githubusercontent.com/iGoodie/RuntimeGoodies/master/.github/assets/github-badge.png" height="20px"/>
  <a href="https://github.com/iGoodie/RuntimeGoodies/commits/master">
    <img src="https://img.shields.io/github/last-commit/iGoodie/RuntimeGoodies"/>
  </a>
  <a href="https://github.com/iGoodie/RuntimeGoodies/issues">
    <img src="https://img.shields.io/github/issues/iGoodie/RuntimeGoodies"/>
  </a>
  <a href="https://github.com/iGoodie/RuntimeGoodies/tree/master/src">
    <img src="https://img.shields.io/github/languages/code-size/iGoodie/RuntimeGoodies"/>
  </a>

  <br/>

  <!-- Support Badges -->
  <img src="https://raw.githubusercontent.com/iGoodie/RuntimeGoodies/master/.github/assets/support-badge.png" height="20px"/>
  <a href="https://discord.gg/KNxxdvN">
    <img src="https://img.shields.io/discord/610497509437210624?label=discord"/>
  </a>
  <a href="https://www.patreon.com/iGoodie">
    <img src="https://img.shields.io/endpoint.svg?url=https%3A%2F%2Fshieldsio-patreon.vercel.app%2Fapi%3Fusername%3DiGoodie%26type%3Dpatrons"/>
  </a>
</p>

# Description

RuntimeGoodies is a Java 8+ library that aims to solve a fundamental problem: reading & maintaining program
configurations. It attemps to load external config files into
declared <a href="https://en.wikipedia.org/wiki/Plain_old_Java_object">
POJO<a/>s with its custom annotations and validators. It even fixes the config when necessary *(e.g an empty field is
replaced by the default value and saved)*

It comes with a few modules to support following formats:

- <a href="https://www.json.org/json-en.html">JSON</a> (module: `com.github.iGoodie.RuntimeGoodies:json:<version-here>`)
- <a href="https://toml.io/en/v0.4.0">TOML 0.4.0</a> (module: `com.github.iGoodie.RuntimeGoodies:toml:<version-here>`)

Out of the box, it comes with a built-in way to universally represent entities during runtime called Goodies. Goodies
are serializable in any format desired. They have a very similar structure
to <a href="https://www.json.org/json-en.html">JSON<a/> elements. Goodies do not exist outside the runtime, they are
in-memory only.

# How to Use?

This library uses <a href="https://jitpack.io/#iGoodie/RuntimeGoodies">JitPack<a/> to serve artifacts that can be
depended on. If your project is a **Gradle** project, use following steps:

1. Add JitPack repository to your `build.gradle` file:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. Depend on desired release. (Replace `<version-here>` with the version of choice)

```groovy
dependencies {
    implementation 'com.github.iGoodie.RuntimeGoodies:core:<version-here>'
    implementation 'com.github.iGoodie.RuntimeGoodies:json:<version-here>' // Format of choice, see supported formats above
}
```

List of all available the versions can be found under <a href="https://github.com/iGoodie/RuntimeGoodies/tags">GitHub
Releases<a/>
section. Alternatively, `master-SNAPSHOT` can be used to depend on the nightly version (at your own risk).

# Getting Started

1. Create a POJO (ConfiGoodie) to represent the external config file. (In this example, external file is in JSON format)

```java
public class ServerConfig extends JsonConfiGoodie {

    @Goodie // <-- Annotate the fields you want to be exposed with @Goodie
    String username, password;

    @Goodie
    Date launchDate; // Some values are converted from string!

    @Goodie
    int port = 3000; // You can declare default values!

    @Goodie
    DBConnection mongodbConnection; // Other ConfiGoodies/POJOs can be nested too!

    @Goodie
    DBConnection mysqlConnection = defaultValue(() -> {
        DBConnection connection = new DBConnection();
        connection.ports = Arrays.asList(3000, 3001, 3002);
        return connection;
    }); // You can declare default values via your own supplier!

    public static class DBConnection {

        @Goodie
        String uri = "localhost:3000";

        @Goodie
        List<Integer> ports; // You can have Lists as well!

        @Goodie
        Map<String, String> aliases; // Guess what, Maps too!

    }

}
```

2. Instance that POJO and let RuntimeGoodie read (and fix if necessary) for you!

```java
public class Test {

    public static void main(String[] args) {
        File configFile = new File("path/to/config.json");

        // Tadaaa! Your config object is now read, validated and fixed accordingly.
        ServerConfig config = new ServerConfig().readConfig(configFile);

        // Note that, the call above will override the file if changes are made during fixing phase.
        // Check out the Wiki, if you want to customize that behavior.
    }

}
```

In order to learn more about what else you can do, check the <a href="https://github.com/iGoodie/RuntimeGoodies/wiki">
Wiki<a/> out!

## Facing an Issue?

- Join our Discord Server - https://discordapp.com/invite/KNxxdvN
- Create an issue on Github: https://github.com/iGoodie/RuntimeGoodies/issues
- Contact iGoodie via Discord: iGoodie#1945

## License

&copy; 2021 Taha Anılcan Metinyurt (iGoodie)

For any part of this work for which the license is applicable, this work is licensed under
the [Attribution-NonCommercial-NoDerivatives 4.0 International](http://creativecommons.org/licenses/by-nc-nd/4.0/)
license. (See LICENSE).

<a rel="license" href="http://creativecommons.org/licenses/by-nc-nd/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-nd/4.0/88x31.png" /></a>
