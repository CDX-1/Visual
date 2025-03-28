import org.jreleaser.model.Active

plugins {
    id("java")
    id("org.jreleaser") version "1.17.0"
    `maven-publish`
}

group = "rip.cdx"
version = "0.1.1"

description = "A component-based GUI library for Minestom, inspired by React.js"

java {
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
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
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name

            from(components["java"])

            pom {
                name = project.name
                description = project.description
                url = "https://github.com/CDX-1/Visual"
                inceptionYear = "2025"
                licenses {
                    license {
                        name = "GPL-2.0"
                        url = "https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html"
                    }
                    developers {
                        developer {
                            id = "cdx"
                            name = "CDX"
                            email = "contact@cdx.rip"
                        }
                    }
                    scm {
                        connection = "scm:git:https://github.com/CDX-1/Visual.git"
                        developerConnection = "scm:git:ssh://github.com/CDX-1/Visual.git"
                        url = "https://github.com/CDX-1/Visual"
                    }
                }
            }
        }

        repositories {
            maven {
                url = layout.buildDirectory.dir("stage-deploy").get().asFile.toURI()
            }
        }
    }
}

jreleaser {
    signing {
        active.set(Active.ALWAYS)
        armored = true
    }
    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    active.set(Active.ALWAYS)
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/stage-deploy")
                }
            }
        }
    }
}