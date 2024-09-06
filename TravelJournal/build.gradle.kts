buildscript {
    val kotlin_version by extra("1.8.10")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

// Elimină secțiunea 'allprojects' pentru a evita conflictul
