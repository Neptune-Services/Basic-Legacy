package club.crestmc.neptunebasic.commands.teleport

import club.crestmc.neptunebasic.Constants
import club.crestmc.neptunebasic.NeptuneBasic
import club.crestmc.neptunebasic.utils.ChatUtil
import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.MessageKeys
import co.aikar.commands.MessageType
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Dependency
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Name
import org.bukkit.entity.Player

@CommandAlias("tpall|teleportall|btpall")
@Description("Teleport all online players to yourself.")
@CommandPermission("basic.tpall")
class TpAllCommand : BaseCommand() {
    @Dependency
    lateinit var plugin: NeptuneBasic

    @CommandAlias("tpall|teleportall|btpall")
    @Description("Teleport all online players to yourself.")
    @CommandPermission("basic.tpall")
    @CommandCompletion("@allOnline")
    fun onTpallCommand(issuer: CommandIssuer) {
        val player = plugin.server.getPlayer(issuer.uniqueId)

        for (p: Player in plugin.server.onlinePlayers) {
            if(p != player) p.teleport(player!!)
        }

        player!!.sendMessage(ChatUtil.translate(
                "${Constants.primaryColor}You've teleported all online players to you."
        ))

        for (p: Player in plugin.server.onlinePlayers) {
            if(p.hasPermission("basic.staff")) {
                p.sendMessage(
                    ChatUtil.translate(
                        "&7&o[${player.name}: &eTeleported all online players to ${player.name}&7&o]"
                    ))
            }
        }
    }
}