package club.crestmc.neptunebasic.config

import club.crestmc.neptunebasic.NeptuneBasic
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.util.logging.Level

class ConfigManager(private val plugin: NeptuneBasic) {
    var config: FileConfiguration? = null
    private val configFile: File

    init {
        configFile = File(plugin.dataFolder, "config.yml")
        load()
    }

    fun load() {
        if (!plugin.dataFolder.exists()) {
            plugin.dataFolder.mkdir()
        }
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false)
        }
        plugin.logger.info("Loading config.yml...")
        config = YamlConfiguration.loadConfiguration(configFile)
    }

    fun saveConfig() {
        try {
            config!!.save(configFile)
        } catch (e: IOException) {
            plugin.logger.log(Level.SEVERE, "An error occurred whilst saving the configuration file", e)
        }
    }

    fun reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile)
    }
}