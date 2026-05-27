plugins {
    kotlin("jvm") version "2.3.20"
    id("org.jlleitschuh.gradle.ktlint") version "14.2.0"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.13"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")
    implementation(files("libs/opencv-490.jar"))
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
    testImplementation(kotlin("reflect"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.16")
    implementation("org.boofcv:boofcv-core:1.1.1")
    implementation("org.boofcv:boofcv-io:1.1.1")
}

sourceSets {
    create("bench") {
        java.srcDir("src/bench/kotlin")
        compileClasspath += sourceSets["main"].output
        runtimeClasspath += output + compileClasspath
    }
}

configurations.named("benchImplementation") {
    extendsFrom(configurations["implementation"])
}

dependencies {
    add("benchImplementation", "org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.16")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

application {
    mainClass.set("org.example.MainKt")
}

kotlin {
    jvmToolchain(25)
}

ktlint {
    version = "1.4.0"
}

benchmark {
    targets {
        register("bench")
    }
}
