package club.crestmc.neptunebasic

import org.bukkit.plugin.java.JavaPlugin


class InternalErrors {
    val plugin = JavaPlugin.getPlugin(NeptuneBasic::class.java)

    fun throwFatalStartupError(msg: String) {
        plugin.server.logger.info(" ")
        plugin.server.logger.warning("==============================================================")
        plugin.server.logger.warning("                 A fatal error has occurred.")
        plugin.server.logger.warning(" ")
        plugin.server.logger.warning(msg)
        plugin.server.logger.warning("Please contact a developer if you cannot solve this issue.")
        plugin.server.logger.warning("==============================================================")
        plugin.server.logger.info(" ")

        plugin.server.shutdown()
        plugin.server.pluginManager.disablePlugin(plugin)
    }

    fun throwLicenseError(msg: String) {
        plugin.server.logger.info(" ")
        plugin.server.logger.warning("==============================================================")
        plugin.server.logger.warning("          An error occured while verifying your license.")
        plugin.server.logger.warning(" ")
        plugin.server.logger.warning(msg)
        plugin.server.logger.warning("Please contact support. Most features will be disabled until this is fixed.")
        plugin.server.logger.warning("==============================================================")
        plugin.server.logger.info(" ")

        plugin.licensed = false
    }
}