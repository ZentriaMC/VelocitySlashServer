plugins {
    java
    id("net.minecrell.licenser") version "0.4.1"
    id("net.kyori.blossom") version "1.1.0"
}

group = "eu.mikroskeem"
version = "0.0.1-SNAPSHOT"

val velocityApiVersion = "1.0.0-SNAPSHOT"

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