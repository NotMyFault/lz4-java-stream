plugins {
    java
    `java-library`
    `maven-publish`
}

the<JavaPluginExtension>().toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
}

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    api("org.lz4:lz4-java:1.7.1")
    api("junit:junit:4.13.2")
}

version = "1.0.1-SNAPSHOT"
group = "net.jpountz"

publishing {
    publications {
        create<MavenPublication>("maven") {
            // This includes not only the original jar (i.e. not shadowJar),
            // but also sources & javadocs due to the above java block.
            from(components["java"])

            pom {
                scm {
                    url.set("https://github.com/NotMyFault/lz4-java-stream")
                    connection.set("scm:https://NotMyFault@github.com/IntellectualSites/lz4-java-stream.git")
                    developerConnection.set("scm:git://github.com/NotMyFault/lz4-java-stream.git")
                }
            }
        }
    }

    repositories {
        mavenLocal() // Install to own local repository

        // Accept String? to not err if they're not present.
        // Check that they both exist before adding the repo, such that
        // `credentials` doesn't err if one is null.
        // It's not pretty, but this way it can compile.
        val nexusUsername: String? by project
        val nexusPassword: String? by project
        if (nexusUsername != null && nexusPassword != null) {
            maven {
                val repositoryUrl = "https://mvn.intellectualsites.com/content/repositories/releases/"
                val snapshotRepositoryUrl = "https://mvn.intellectualsites.com/content/repositories/snapshots/"
                url = uri(
                        if (version.toString().endsWith("-SNAPSHOT")) snapshotRepositoryUrl
                        else repositoryUrl
                )

                credentials {
                    username = nexusUsername
                    password = nexusPassword
                }
            }
        } else {
            logger.warn("No nexus repository is added; nexusUsername or nexusPassword is null.")
        }
    }
}

val javadocDir = rootDir.resolve("docs").resolve("javadoc")
tasks {
    named<Delete>("clean") {
        doFirst {
            javadocDir.deleteRecursively()
        }
    }

    compileJava {
        options.compilerArgs.addAll(arrayOf("-Xmaxerrs", "1000"))
        options.compilerArgs.add("-Xlint:all")
        for (disabledLint in arrayOf("processing", "path", "fallthrough", "serial"))
            options.compilerArgs.add("-Xlint:$disabledLint")
        options.isDeprecation = true
        options.encoding = "UTF-8"
    }

    javadoc {
        val opt = options as StandardJavadocDocletOptions
        opt.addStringOption("Xdoclint:none", "-quiet")
        opt.tags(
                "apiNote:a:API Note:",
                "implSpec:a:Implementation Requirements:",
                "implNote:a:Implementation Note:"
        )
        opt.destinationDirectory = javadocDir
    }

    jar {
        this.archiveClassifier.set("jar")
    }
}
