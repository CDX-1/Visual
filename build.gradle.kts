plugins {
    id("java")
    `java-library`
    `maven-publish`
    signing
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

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/CDX-1/Visual")

                licenses {
                    license {
                        name.set("GNU General Public License v2.0")
                        url.set("https://opensource.org/license/gpl-2-0")
                    }
                }

                developers {
                    developer {
                        id.set("cdx")
                        name.set("CDX")
                        email.set("contact@cdx.rip")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/CDX-1/Visual.git")
                    developerConnection.set("scm:git:https://github.com/CDX-1/Visual.git")
                    url.set("https://github.com/CDX-1/Visual")
                }
            }
        }
    }

    repositories {
        maven {
            url = uri(
                if (version.toString().endsWith("SNAPSHOT"))
                    "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                else
                    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            )

            credentials {
                username = findProperty("ossrhUsername") as String? ?: ""
                password = findProperty("ossrhPassword") as String? ?: ""
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        findProperty("signingKey") as String? ?: "",
        findProperty("signingPassword") as String? ?: ""
    )

    sign(publishing.publications)
}