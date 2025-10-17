plugins {
    java
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.scraping"
version = "0.0.1-SNAPSHOT"
description = "ScrapingDemo"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // https://mvnrepository.com/artifact/com.microsoft.playwright/playwright
    implementation("com.microsoft.playwright:playwright:1.55.0")
    
    // Jackson for JSON processing
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<JavaExec>("playwrightInstallDeps") {
    group = "playwright"
    description = "Install system dependencies required by Playwright"
    mainClass.set("com.microsoft.playwright.CLI")
    classpath = sourceSets.main.get().runtimeClasspath
    args = listOf("install-deps")
}

tasks.register<JavaExec>("playwrightInstall") {
    group = "playwright"
    description = "Install Playwright browsers"
    mainClass.set("com.microsoft.playwright.CLI")
    classpath = sourceSets.main.get().runtimeClasspath
    args = listOf("install")
}

// Hook into Gradle build lifecycle so it always runs once before build/tests
tasks.named("build") {
    dependsOn("playwrightInstallDeps", "playwrightInstall")
}
