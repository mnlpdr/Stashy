// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.11" apply false
    id("com.android.library") version "8.1.4" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

buildscript {
    repositories {
        google()  // Certifique-se de que este repositório esteja presente
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.15") // Adicione esta linha
    }
}
