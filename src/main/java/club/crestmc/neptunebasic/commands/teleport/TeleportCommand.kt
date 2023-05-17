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

@CommandAlias("teleport|tp|btp")
@Description("Teleport to a player, or teleport a player to you.")
@CommandPermission("basic.teleport")
class TeleportCommand : BaseCommand() {

    @Dependency
    lateinit var plugin: NeptuneBasic

    @Default
    @CommandCompletion("@allOnline")
    fun onTeleportRun(issuer: CommandIssuer, @Name("target") targetArg: String, @Name("target2") @Optional target2Arg: String?) {

        val player = plugin.server.getPlayer(issuer.uniqueId)!!

        if(target2Arg == null) {

            if(!issuer.isPlayer) {
                plugin.manager.sendMessage(issuer, MessageType.ERROR, MessageKeys.NOT_ALLOWED_ON_CONSOLE)
                return
            }

            val target1 = plugin.server.getPlayer(targetArg)
            if (target1 == null) {
                plugin.manager
                    .sendMessage(issuer, MessageType.ERROR, MessageKeys.COULD_NOT_FIND_PLAYER, "{search}", targetArg)
                return
            }

            player.teleport(target1.location)

            player.sendMessage(ChatUtil.translate(
                "${Constants.primaryColor}You've teleported to ${Constants.secondaryColor}${target1.name}${Constants.primaryColor}."
            ))

            StaffMessage(plugin).sendStaffMessage("Teleported to ${target1.name}", player.name)

        } else {
            val target1 = plugin.server.getPlayer(targetArg)
            val target2 = plugin.server.getPlayer(target2Arg)

            if (target1 == null) {
                plugin.manager
                    .sendMessage(issuer, MessageType.ERROR, MessageKeys.COULD_NOT_FIND_PLAYER, "{search}", targetArg)
                return
            }

            if (target2 == null) {
                plugin.manager
                    .sendMessage(issuer, MessageType.ERROR, MessageKeys.COULD_NOT_FIND_PLAYER, "{search}", target2Arg)
                return
            }

            target1.teleport(target2)

            player.sendMessage(ChatUtil.translate(
                "${Constants.primaryColor}You have teleported ${Constants.secondaryColor}${target1.name}${Constants.primaryColor} to ${Constants.secondaryColor}${target2.name}${Constants.primaryColor}."
            ))

            StaffMessage(plugin).sendStaffMessage("Teleported ${target1.name} to ${target2.name}", player.name)
        }
    }
}