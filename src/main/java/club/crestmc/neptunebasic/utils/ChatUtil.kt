package club.crestmc.neptunebasic.utils

import club.crestmc.neptunebasic.NeptuneBasic
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

object ChatUtil {
    private var plugin: NeptuneBasic? = null

    init {
        plugin = JavaPlugin.getPlugin(
            NeptuneBasic::class.java
        )
    }

    fun translate(text: String?): String {
        return ChatColor.translateAlternateColorCodes('&', text!!)
    }
}