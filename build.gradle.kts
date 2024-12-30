plugins {
    id("java")
}

group = "org.bayl"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(22))
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "net.zeminvaders.lang.Launcher"
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}
