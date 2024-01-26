plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
    id("org.jetbrains.intellij") version "1.16.1"
}

group = "com.rudolf.tr"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

intellij {
    version.set("2023.1.5")
    type.set("IC")

    plugins.set(listOf("LivePlugin:0.8.6 beta"))
    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
    }
}

sourceSets {
    main {
        kotlin {
            val file = File(project.rootDir, ".live-plugins/bigDecimal")
            srcDir(file.absolutePath)
        }
    }
    test {
        kotlin {
            val file = File(project.rootDir, ".live-plugins/bigDecimal/test")
            srcDir(file.absolutePath)
        }
    }
}
tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("231")
        untilBuild.set("241.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
