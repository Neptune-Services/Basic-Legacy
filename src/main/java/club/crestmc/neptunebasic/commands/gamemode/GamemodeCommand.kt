package club.crestmc.neptunebasic.commands.gamemode

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

@CommandAlias("gamemode|gm")
@Description("Change your or another player's gamemode.")
@CommandPermission("basic.gamemode")
class GamemodeCommand : BaseCommand() {

    @Dependency
    lateinit var plugin: NeptuneBasic

    @Default
    @CommandCompletion("@gamemodes @allOnline")
    fun onGamemodeCommand(issuer: CommandIssuer, @Name("gamemode") gamemodeArg: String, @Name("target") @Optional targetArg: String?) {
        val player = plugin.server.getPlayer(issuer.uniqueId)
        var target: Player?
        if(targetArg != null) {
            target = plugin.server.getPlayer(targetArg)
            if (target == null) {
                plugin.manager
                    .sendMessage(issuer, MessageType.ERROR, MessageKeys.COULD_NOT_FIND_PLAYER, "{search}", targetArg)
                return
            }
        } else {
            target = player
        }

        val gamemode = GamemodeTools().getGamemode(gamemodeArg)
        if(gamemode == null) {
            issuer.sendMessage(ChatUtil.translate("&cError: &e$gamemodeArg&c is not a valid gamemode."))
            return
        }

        target?.gameMode = gamemode
        player?.sendMessage(ChatUtil.translate("${Constants.primaryColor}You've set ${Constants.secondaryColor}${target!!.name}${Constants.primaryColor}'s gamemode to ${Constants.secondaryColor}${gamemode.toString().lowercase()}${Constants.primaryColor}."))
        StaffMessage(plugin).sendStaffMessage("Set ${target?.name}'s gamemode to ${gamemode.toString().lowercase()}", player!!.name)
    }
}