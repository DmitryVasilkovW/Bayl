plugins {
    id("java")
    id("io.freefair.lombok") version "8.0.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":jitExecutors"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
        vendor.set(JvmVendorSpec.ADOPTIUM)
        implementation.set(JvmImplementation.VENDOR_SPECIFIC)
    }
}

tasks.withType<JavaCompile>().configureEach {
    dependsOn(":jitExecutors:build")
    javaCompiler = javaToolchains.compilerFor {
        languageVersion = JavaLanguageVersion.of(23)
    }
    options.compilerArgs.add("--enable-preview")
}

var nativeLibPath = "../jitExecutors/build/cmake/"

tasks.withType<JavaExec> {
    jvmArgs = listOf(
        "--enable-preview",
        "-Djava.library.path=${file(nativeLibPath).absolutePath}",
        "--enable-native-access=ALL-UNNAMED"
    )
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.bayl.Launcher"
    }
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.30")
    implementation("org.jetbrains:annotations:26.0.2")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testImplementation("org.mockito:mockito-core:5.15.2")
    testImplementation("org.projectlombok:lombok:1.18.30")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
}

tasks.test {
    useJUnitPlatform()

    jvmArgs = listOf(
        "-Djava.library.path=${file(nativeLibPath).absolutePath}",
        "--enable-native-access=ALL-UNNAMED"
    )
}
