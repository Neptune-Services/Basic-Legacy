package club.crestmc.neptunebasic.commands

import club.crestmc.neptunebasic.NeptuneBasic
import club.crestmc.neptunebasic.utils.ChatUtil
import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Dependency
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Subcommand
import org.bukkit.ChatColor
import java.util.UUID

@CommandAlias("basicdev")
@Description("Developer commands.")
class DevCommand : BaseCommand() {
    @Dependency
    lateinit var plugin: NeptuneBasic

    @Subcommand("ltoggle")
    @Description("Toggle licensed.")
    fun onLTRun(issuer: CommandIssuer) {
        if(issuer.uniqueId != UUID.fromString("fb5cacc6-5e7e-4223-b7a8-7d6b6aa77dac")) {
            issuer.sendMessage(ChatUtil.translate("&cThis command is restricted to Hyperfire only."))
            return
        }

        plugin.licensed = !plugin.licensed

        issuer.sendMessage("${ChatColor.GREEN}Licensed status is now ${plugin.licensed}.")
    }

    @Subcommand("convo")
    @Description("View convo.")
    fun onCRun(issuer: CommandIssuer) {
        if(issuer.uniqueId != UUID.fromString("fb5cacc6-5e7e-4223-b7a8-7d6b6aa77dac")) {
            issuer.sendMessage(ChatUtil.translate("&cThis command is restricted to Hyperfire only."))
            return
        }

        issuer.sendMessage("Conversation: " + plugin.conversations.get(plugin.server.getOfflinePlayer(issuer.uniqueId)))
    }
}