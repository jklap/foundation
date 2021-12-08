val artifactGroup: String by project
val kotlinxCoroutinesVersion: String by project
val kotlinxSerializationRuntimeVersion: String by project
val javaFakerVersion: String by project

val testVersion: String = "0.3.4"
val testKaptVersion: String = "0.3.4.1"

plugins {
    idea
}

group = "$artifactGroup.integration-test-service"
version = testVersion

repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":foundation"))
    implementation(project(":integration-test-contract"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationRuntimeVersion")
    implementation("com.github.javafaker:javafaker:$javaFakerVersion")

    implementation(project(":example-jvm"))
    implementation("com.github.jhg023:BitBuffer:1.0.1")
    kapt("com.github.nhat-phan.foundation:foundation-processor:$testKaptVersion")
    kaptTest("com.github.nhat-phan.foundation:foundation-processor:$testKaptVersion")

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
