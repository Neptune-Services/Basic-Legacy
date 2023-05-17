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

@CommandAlias("tpall|teleportall|btpall")
@Description("Teleport all online players to yourself.")
@CommandPermission("basic.tpall")
class TpAllCommand : BaseCommand() {
    @Dependency
    lateinit var plugin: NeptuneBasic

    @Default
    @CommandCompletion("@allOnline")
    fun onTpallCommand(issuer: CommandIssuer) {
        val player = plugin.server.getPlayer(issuer.uniqueId)

        for (p: Player in plugin.server.onlinePlayers) {
            if(p != player) p.teleport(player!!)
        }

        player!!.sendMessage(ChatUtil.translate(
                "${Constants.primaryColor}You've teleported all online players to you."
        ))
        StaffMessage(plugin).sendStaffMessage("Teleported all online players to ${player.name}", player.name)
    }
}