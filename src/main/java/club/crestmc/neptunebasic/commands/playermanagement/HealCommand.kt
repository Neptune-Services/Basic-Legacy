package club.crestmc.neptunebasic.commands.playermanagement

import club.crestmc.neptunebasic.Constants
import club.crestmc.neptunebasic.NeptuneBasic
import club.crestmc.neptunebasic.utils.ChatUtil
import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.MessageKeys
import co.aikar.commands.MessageType
import co.aikar.commands.annotation.*
import org.bukkit.entity.Player

@CommandAlias("heal")
@Description("Heal a player, or yourself.")
@CommandPermission("basic.heal")
class HealCommand : BaseCommand() {
    @Dependency
    lateinit var plugin: NeptuneBasic

    @CommandAlias("heal")
    @Description("Heal a player, or yourself.")
    @CommandCompletion("@allOnline")
    fun onHeal(issuer: CommandIssuer, @Name("target") @Optional targetArg: String?) {
        val player = plugin.server.getPlayer(issuer.uniqueId)!!

        if(targetArg == null) {
            player.health = 20.0

            player.sendMessage(ChatUtil.translate("${Constants.primaryColor}You've healed yourself."))
        } else {
            val target = plugin.server.getPlayer(targetArg)
            if (target == null || !player.canSee(target)) {
                plugin.manager
                    .sendMessage(issuer, MessageType.ERROR, MessageKeys.COULD_NOT_FIND_PLAYER, "{search}", targetArg)
                return
            }

            target.health = 20.0
            player.sendMessage(ChatUtil.translate("${Constants.primaryColor}You've healed ${Constants.secondaryColor}${target.name}${Constants.primaryColor}."))

            for(lp: Player in plugin.server.onlinePlayers) {
                if(lp.hasPermission("basic.staff")) {
                    lp.sendMessage(ChatUtil.translate(
                        "&7&o[${player.name}: &eHealed ${target.name}&7&o]"
                    ))
                }
            }
        }
    }
}