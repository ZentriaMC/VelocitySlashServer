plugins {
    java
    id("net.minecrell.licenser") version "0.4.1"
    id("net.kyori.blossom") version "1.2.0"
}

group = "eu.mikroskeem"
version = "0.0.1-SNAPSHOT"

val velocityApiVersion = "1.1.5"

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://repo.velocitypowered.com/snapshots/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:$velocityApiVersion")
    annotationProcessor("com.velocitypowered:velocity-api:$velocityApiVersion")
}

license {
    header = rootProject.file("etc/HEADER")
    filter.include("**/*.java")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

blossom {
    val id = "slashserver"
    val name = "SlashServer"
    val description = "Simply server name to command binding plugin"
    val authors = listOf("mikroskeem")

    val constantsFile = "src/main/java/eu/mikroskeem/velocityslashserver/VelocitySlashServer.java"
    mapOf(
            "%ID%" to id,
            "%NAME%" to name,
            "%VERSION%" to "${rootProject.version}",
            "%DESCRIPTION%" to description,
            "%AUTHORS%" to authors.joinToString(separator = "\", \"")
    ).forEach { (token, value) ->
        replaceToken(token, value, constantsFile)
    }
}
