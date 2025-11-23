plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm") version "2.0.0"
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "ir.syrent"
version = findProperty("version")
val slug = "enhancedvelocity"
description = "Customize your Velocity network experience"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")

    implementation("org.bstats:bstats-velocity:3.0.2")
    implementation("net.kyori:adventure-text-minimessage:4.17.0")
    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("commons-io:commons-io:2.16.1")
    implementation(kotlin("stdlib-jdk8"))
}

java {
    withSourcesJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    processResources {
        filesMatching("plugin.json") {
            expand(
                "version" to project.version,
                "slug" to slug,
                "name" to project.name,
                "description" to project.description
            )
        }
    }

    shadowJar {
        archiveFileName.set("${findProperty("plugin-name") as String} v${project.version}.jar")
        archiveClassifier.set(null as String?)

        relocate("org.bstats", "ir.syrent.enhancedvelocity.bstats")
        relocate("org.spongepowered", "ir.syrent.spongepowered")

        minimize()
    }

    jar {
        archiveFileName.set("${findProperty("plugin-name") as String} v${project.version}-unshaded.jar")
    }

    withType<Jar> {
        destinationDirectory.set(file("$rootDir/bin/"))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(tasks["sourcesJar"])

            pom {
                name.set("EnhancedVelocity")
                description.set(project.description)
                url.set("https://github.com/syrent/enhancedvelocity")
                developers {
                    developer {
                        id.set("syrent")
                        name.set("Abbas")
                        email.set("syrent2356@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:github.com/syrent/enhancedvelocity.git")
                    developerConnection.set("scm:git:ssh://github.com/syrent/enhancedvelocity.git")
                    url.set("https://github.com/syrent/enhancedvelocity/tree/master")
                }
            }
        }
    }

    repositories {
        maven {
            name = "SayanDevelopment"
            url = uri("https://repo.sayandev.org/snapshots/")
            credentials {
                username = System.getenv("REPO_SAYAN_USER") ?: findProperty("repo.sayan.user") as? String
                password = System.getenv("REPO_SAYAN_TOKEN") ?: findProperty("repo.sayan.token") as? String
            }
        }
    }
}
