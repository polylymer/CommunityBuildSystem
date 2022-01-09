@file:Suppress("PropertyName")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.1"
    kotlin("plugin.serialization") version "1.6.10"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

val mcVersion = "1.18.1"

group = "de.hglabor"
version = "${mcVersion}_v1"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://nexus-repo.jordanosterberg.com/repository/maven-releases/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("org.spigotmc", "spigot", "${mcVersion}-R0.1-SNAPSHOT")
    implementation("net.axay", "kspigot", "1.16.29")
    implementation("net.kyori", "adventure-api", "4.9.3")
    implementation("dev.jcsoftware", "JScoreboards", "2.1.2-RELEASE")
}

java.sourceCompatibility = JavaVersion.VERSION_16
java.targetCompatibility = JavaVersion.VERSION_16

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

tasks {
    processResources {
        expand("version" to version)
    }
    shadowJar {
        simpleRelocate("net.axay.kspigot")
    }
}

fun ShadowJar.simpleRelocate(pattern: String) {
    relocate(pattern, "${project.group}.${project.name.toLowerCase()}.shadow.$pattern")
}

bukkit {
    name = "BuildSystem"
    main = "de.hglabor.BuildSystem"
    version = mcVersion
    apiVersion = "1.18"
    authors = listOf("polylymer")
    commands {
        register("skull") {}
        register("settings") {}
    }
}
