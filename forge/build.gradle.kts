plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

architectury {
    platformSetupLoomIde()
    forge()
}

configurations {
    create("common")
    create("shadowCommon")
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    getByName("developmentForge").extendsFrom(configurations["common"])
}

loom {
    enableTransitiveAccessWideners.set(true)
    silentMojangMappingsLicense()
    accessWidenerPath.set(project(":common").loom.accessWidenerPath)
//    mixin {
//        defaultRefmapName.set("mixins.${project.name}.refmap.json")
//    }
    forge{
        convertAccessWideners.set(true)
        mixinConfig("fightorflight.mixins_common.json")
    }


}

repositories {
    mavenCentral()
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    maven("https://maven.impactdev.net/repository/development/")
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://thedarkcolour.github.io/KotlinForForge/")
}

dependencies {
    forge("net.minecraftforge:forge:1.20.1-${project.properties["forge_version"]}")
    modApi("dev.architectury:architectury-forge:${project.properties["architectury_version"]}")

    "common"(project(":common", "namedElements")) { isTransitive = false }
    "shadowCommon"(project(":common", "transformProductionForge")) { isTransitive = false }

    modImplementation("com.cobblemon:forge:1.4.0+1.20.1-SNAPSHOT")

    runtimeOnly("maven.modrinth:ordsPcFz:CZYJI3gh") //kotlinforforge

    include(modApi("me.shedaniel.cloth:cloth-config-forge:11.1.106")!!)

}

tasks {
    base.archivesName.set(base.archivesName.get() + "-forge")
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand(mapOf("version" to project.version))
        }
    }

    shadowJar {
        exclude("fabric.mod.json")
        exclude("generations/gg/generations/core/generationscore/forge/datagen/**")
        exclude("architectury-common.accessWidener")
        exclude("architectury.common.json")

        configurations = listOf(project.configurations.getByName("shadowCommon"))
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
    }

    jar.get().archiveClassifier.set("dev")

    sourcesJar {
        val commonSources = project(":common").tasks.sourcesJar
        duplicatesStrategy=DuplicatesStrategy.EXCLUDE
        dependsOn(commonSources)
        from(commonSources.get().archiveFile.map { zipTree(it) })
    }
}

components {
    java.run {
        if (this is AdhocComponentWithVariants)
            withVariantsFromConfiguration(project.configurations.shadowRuntimeElements.get()) { skip() }
    }
}
