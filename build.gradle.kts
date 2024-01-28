import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
}

group = "com.gistgarden"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

if (hasProperty("buildScan")) {
    extensions.findByName("buildScan")?.withGroovyBuilder {
        setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
        setProperty("termsOfServiceAgree", "yes")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.mysql:mysql-connector-j")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()

//    testLogging.showStandardStreams = true
    testLogging.exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL

    maxParallelForks = Runtime.getRuntime().availableProcessors().div(2).let { if (it == 0) 1 else it }
    logger.lifecycle("Test runner threads (maxParallelForks): $maxParallelForks")
}


tasks {
    "bootJar" {
        archivesName.set("gistgarden-webservice")
    }
}

task("resolveDependencies") {
    doLast {
        logger.lifecycle("Resolving dependencies")
        project.rootProject.allprojects.forEach { subProject ->
            subProject.buildscript.configurations.forEach { configuration ->
                tryToResolveConfiguration(subProject, configuration)
            }
            subProject.configurations.forEach { configuration ->
                tryToResolveConfiguration(subProject, configuration)
            }
        }
    }
}

fun tryToResolveConfiguration(subProject: Project, configuration: Configuration) {
    logger.lifecycle("Resolving configuration: $subProject -> $configuration ... ")
    if (configuration.isCanBeResolved) {
        configuration.resolve()
        logger.lifecycle(" - DONE")
    } else {
        logger.lifecycle(" - CAN NOT BE RESOLVED")
    }
}


