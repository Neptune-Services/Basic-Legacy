package club.crestmc.neptunebasic.commands.playermanagement

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

@CommandAlias("feed")
@Description("Feed a player, or yourself.")
@CommandPermission("basic.feed")
class FeedCommand : BaseCommand() {
    @Dependency
    lateinit var plugin: NeptuneBasic

    @Default
    @CommandCompletion("@allOnline")
    fun onHeal(issuer: CommandIssuer, @Name("target") @Optional targetArg: String?) {
        val player = plugin.server.getPlayer(issuer.uniqueId)!!

        if(targetArg == null) {
            player.saturation = 20.0F
            player.foodLevel = 20

            player.sendMessage(ChatUtil.translate("${Constants.primaryColor}You've fed yourself."))
        } else {
            val target = plugin.server.getPlayer(targetArg)
            if (target == null || !player.canSee(target)) {
                plugin.manager
                    .sendMessage(issuer, MessageType.ERROR, MessageKeys.COULD_NOT_FIND_PLAYER, "{search}", targetArg)
                return
            }

            target.saturation = 20.0F
            target.foodLevel = 20
            player.sendMessage(ChatUtil.translate("${Constants.primaryColor}You've fed ${Constants.secondaryColor}${target.name}${Constants.primaryColor}."))


            StaffMessage(plugin).sendStaffMessage("Fed ${target.name}", player.name)
        }
    }
}