plugins {
    kotlin("jvm") version "1.9.21"
    `maven-publish`
}

group = "ru.DmN.phtx"
version = "1.2.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    api("ru.DmN.siberia:Project-Siberia:1.25.1")
    api("ru.DmN.pht:Project-Pihta:1.29.3")
    api(files("Lazurite-2.7.5.jar"))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    java {
        withSourcesJar()
    }

    test {
        useJUnitPlatform()
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = "Project-PLS"
            version = project.version as String
            from(components["java"])
        }
    }
}