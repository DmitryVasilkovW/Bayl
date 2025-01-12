plugins {
    id("dev.welbyseely.gradle-cmake-plugin") version "0.1.0"
}

cmake {
    @Suppress("DEPRECATION")
    workingFolder = file("${buildDir.path}/cmake")
    sourceFolder = file(projectDir.path)
    installPrefix = System.getProperty("user.home").toString()
    buildSharedLibs = true
    buildClean = true
}
