package club.crestmc.neptunebasic.commands

import club.crestmc.neptunebasic.Constants
import club.crestmc.neptunebasic.NeptuneBasic
import club.crestmc.neptunebasic.utils.ChatUtil
import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandHelp
import co.aikar.commands.CommandIssuer
import co.aikar.commands.annotation.*

@CommandAlias("basic")
@CommandPermission("basic.admin")
@Description("Reload the config or view plugin information.")
class BasicCommand : BaseCommand() {
    @Dependency
    lateinit var plugin: NeptuneBasic

    @Subcommand("info")
    @CommandPermission("basic.info")
    @Description("View info about the plugin.")
    fun onInfoCommand(issuer: CommandIssuer) {
        val licensedString = plugin.licensed
            .takeIf { it }
            ?.let { "&6Yes &7(${plugin.configManager.config?.getString("license-key")})" }
            ?: "&cNo, please check your license to continue using this plugin."
        issuer.sendMessage(ChatUtil.translate("&6Neptune Basic &eby &6HyperfireRS"))
        issuer.sendMessage(ChatUtil.translate("&7&m------------------------------------"))
        issuer.sendMessage(ChatUtil.translate("&eLicensed: $licensedString"))
        issuer.sendMessage(ChatUtil.translate("&7&m------------------------------------"))
    }

    @Subcommand("reload")
    @CommandPermission("basic.reload")
    @Description("Reload the config.")
    fun onReloadCommand(issuer: CommandIssuer) {
        if(!plugin.licensed) {
            issuer.sendMessage(Constants.notLicensed)
            return
        }

        plugin.configManager.reloadConfig()
        issuer.sendMessage(ChatUtil.translate("&aSuccessfully reloaded Basic."))
    }

    @CatchUnknown
    @HelpCommand
    fun onHelp(issuer: CommandIssuer, help: CommandHelp) {
        help.showHelp()
    }
}