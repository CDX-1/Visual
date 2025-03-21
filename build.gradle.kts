plugins {
    id("java")
    `maven-publish`
}

group = "rip.cdx"
version = "0.1.0-SNAPSHOT"

description = "A component-based GUI library for Minestom, inspired by React.js"

java {
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io/") }
}

dependencies {
    compileOnly("net.minestom:minestom-snapshots:39d445482f")
    implementation("org.projectlombok:lombok:1.18.36")
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testImplementation("net.minestom:minestom-snapshots:39d445482f")
}