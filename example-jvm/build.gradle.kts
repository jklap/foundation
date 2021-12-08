val artifactGroup: String by project
val foundationVersion: String by project
val kotlinxCoroutinesVersion: String by project
val kotlinxSerializationRuntimeVersion: String by project
val javaFakerVersion: String by project

plugins {
    idea
}

group = "$artifactGroup.example-jvm"
version = foundationVersion

repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
}

dependencies {
    // ------- Dependencies for development
    implementation(kotlin("stdlib"))
    implementation(project(":foundation"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationRuntimeVersion")
    implementation("com.github.javafaker:javafaker:$javaFakerVersion")
    implementation("com.github.jhg023:BitBuffer:1.0.1")

    kapt(project(":foundation-processor"))
    kaptTest(project(":foundation-processor"))

    // ------- Dependencies for testing with published artifact
    // implementation(kotlin("stdlib"))
    // implementation("com.github.nhat-phan.foundation:foundation-jvm:$artifactVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationRuntimeVersion")

    // kapt("com.github.nhat-phan.foundation:foundation-processor:$artifactVersion")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

idea {
    module {
        sourceDirs = sourceDirs + files(
            "build/generated/source/kapt/main",
            "build/generated/source/kaptKotlin/main"
        )

        testSourceDirs = testSourceDirs + files(
            "build/generated/source/kapt/test",
            "build/generated/source/kaptKotlin/test"
        )

        generatedSourceDirs = generatedSourceDirs + files(
            "build/generated/source/kapt/main",
            "build/generated/source/kaptKotlin/main",
            "build/generated/source/kapt/test",
            "build/generated/source/kaptKotlin/test"
        )
    }
}
