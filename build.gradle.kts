plugins {
    id("java")
}

group = "rip.cdx"
version = "1.0.0"

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