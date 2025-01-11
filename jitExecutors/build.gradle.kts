plugins {
    id("dev.welbyseely.gradle-cmake-plugin") version "0.1.0"
}

cmake {
    workingFolder = file("${layout.buildDirectory}/cmake")
    sourceFolder = file(projectDir)
    installPrefix = System.getProperty("user.home")
    buildStaticLibs = true
    buildSharedLibs = true
    buildClean = false
}
