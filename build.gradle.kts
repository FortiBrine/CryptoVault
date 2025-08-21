plugins {
    java

    alias(libs.plugins.shadow)
}

group = "cc.fortibrine"
version = "1.0"

repositories {
    mavenCentral()

    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.panda-lang.org/releases")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly(libs.paper) {
        exclude("org.apache.commons", "commons-lang3")
    }
    compileOnly(libs.sqlite)
    compileOnly(libs.lombok)
    compileOnly(libs.vault.api) {
        exclude("org.bukkit", "bukkit")
    }
    compileOnly(libs.placeholderapi)
    annotationProcessor(libs.lombok)

    compileOnly(libs.ormlite)
    implementation(libs.litecommands)
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()

        sourceCompatibility = "21"
        targetCompatibility = "21"
    }

    shadowJar {
        archiveClassifier.set("")

        relocate("dev.rollczi.litecommands", "cc.fortibrine.cryptovault.shade.litecommands")
//        relocate("com.j256.ormlite", "me.fortibrine.cryptovault.shade.ormlite")
    }

    processResources {
        inputs.property("version", project.version)
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
        filesMatching("config.yml") {
            expand("version" to project.version)
        }
        filesMatching("messages.yml") {
            expand("version" to project.version)
        }
    }
}
