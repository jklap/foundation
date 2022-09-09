val artifactGroup: String by project

val testVersion: String = "0.3.4"
val testKaptVersion: String = "0.3.4.1"

group = "$artifactGroup.integration-test-contract"
version = testVersion

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.github.nhat-phan.foundation:foundation-jvm:$testVersion")
    implementation("com.github.jhg023:BitBuffer:1.0.1")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
