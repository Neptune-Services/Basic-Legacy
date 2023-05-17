package club.crestmc.neptunebasic.commands.teleport

import club.crestmc.neptunebasic.Constants
import club.crestmc.neptunebasic.NeptuneBasic
import club.crestmc.neptunebasic.utils.ChatUtil
import club.crestmc.neptunebasic.utils.StaffMessage
import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.MessageKeys
import co.aikar.commands.MessageType
import co.aikar.commands.annotation.*
import org.bukkit.entity.Player

@CommandAlias("tphere|teleporthere|tph|btphere")
@Description("Teleport a player to you.")
@CommandPermission("basic.tphere")
class TeleportHereCommand : BaseCommand() {
    @Dependency
    lateinit var plugin: NeptuneBasic

    @Default
    @CommandCompletion("@allOnline")
    fun onTpHere(issuer: CommandIssuer, @Name("target") targetArg: String) {
        val player = plugin.server.getPlayer(issuer.uniqueId)

        val target = plugin.server.getPlayer(targetArg)
        if (target == null) {
            plugin.manager
                .sendMessage(issuer, MessageType.ERROR, MessageKeys.COULD_NOT_FIND_PLAYER, "{search}", targetArg)
            return
        }

        target.teleport(player!!)

        player.sendMessage(
            ChatUtil.translate(
            "${Constants.primaryColor}You've teleported ${Constants.secondaryColor}${target.name}${Constants.primaryColor} to you."
        ))
        StaffMessage(plugin).sendStaffMessage("Teleported ${target.name} to ${player.name}", player.name)
    }
}