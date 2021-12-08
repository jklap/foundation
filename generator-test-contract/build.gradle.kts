val artifactGroup: String by project
val foundationVersion: String by project
val releasingProcessorVersion: String by project

plugins {
    idea
}

group = "$artifactGroup.generator-test-contract"
version = releasingProcessorVersion

repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":foundation"))
    implementation("com.github.jhg023:BitBuffer:1.0.1")

    kapt(project(":foundation-processor"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

kapt {
    arguments {
        arg("foundation.processor.dev", "true")
        arg("foundation.processor.mode", "contractOnly")
        arg("foundation.processor.settingsClass", "com.generator.ContractData")
    }
}

idea {
    module {
        sourceDirs = sourceDirs + files(
            "build/generated/source/kapt/main",
            "build/generated/source/kaptKotlin/main"
        )

        generatedSourceDirs = generatedSourceDirs + files(
            "build/generated/source/kapt/main",
            "build/generated/source/kaptKotlin/main",
            "build/generated/source/kapt/test",
            "build/generated/source/kaptKotlin/test"
        )
    }
}