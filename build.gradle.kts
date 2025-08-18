plugins {
    java

    alias(libs.plugins.shadow)
}

group = "me.fortibrine"
version = "1.0"

repositories {
    mavenCentral()

    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.panda-lang.org/releases")
}

dependencies {
    compileOnly(libs.paper) {
        exclude("org.apache.commons", "commons-lang3")
    }
    compileOnly(libs.sqlite)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    implementation(libs.ormlite)
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

        relocate("com.j256.ormlite", "me.fortibrine.cryptovault.shade.ormlite")
    }

    processResources {
        inputs.property("version", project.version)
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }
}
