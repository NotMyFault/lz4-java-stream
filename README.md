lz4-java-stream
===============

Build on jpountz's great lz4-java

## Gradle
```kotlin
repositories {
    maven { url = uri("https://mvn.intellectualsites.com/content/repositories/releases/") }
}

dependencies {
    implementation("net.jpountz:lz4-java-stream:1.0.0")
}
```

## Maven
```xml
<repository>
  <id>IntellectualSites' Releases</id>
  <url>https://mvn.intellectualsites.com/content/repositories/releases/</url>
</repository>

<dependency>
  <groupId>net.jpountz</groupId>
  <artifactId>lz4-java-stream</artifactId>
  <version>1.0.0</version>
</dependency>
```

Note: If you are shading in lz4-java-stream, you should use [gradle shadow](https://github.com/johnrengelman/shadow) or [maven shade](https://maven.apache.org/plugins/maven-shade-plugin/) to relocate this dependency under your classpath.