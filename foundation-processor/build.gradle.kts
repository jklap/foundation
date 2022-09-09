val artifactGroup: String by project
val releasingProcessorVersion: String by project
val kotlinxMetadataJvmVersion: String by project
val kotlinPoetVersion: String by project
val guavaVersion: String by project
val truthVersion: String by project
val compileTestingVersion: String by project

plugins {
    `maven-publish`
}

group = artifactGroup
version = releasingProcessorVersion

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":foundation"))
    implementation(project(":foundation-generator"))
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:$kotlinxMetadataJvmVersion")
    implementation("com.github.jhg023:BitBuffer:1.0.1")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("com.google.guava:guava:$guavaVersion")
    testImplementation("com.google.truth:truth:$truthVersion")
    testImplementation("com.google.testing.compile:compile-testing:$compileTestingVersion")
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("source")
    from(sourceSets.main.get().allSource)
}

publishing {
    repositories {
        maven {
            url = uri("$buildDir/repo")
        }
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}
