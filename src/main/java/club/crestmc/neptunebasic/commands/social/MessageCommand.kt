package club.crestmc.neptunebasic.commands.social

import club.crestmc.neptunebasic.NeptuneBasic
import club.crestmc.neptunebasic.settings.SettingsManager
import club.crestmc.neptunebasic.utils.ChatUtil
import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.MessageKeys
import co.aikar.commands.MessageType
import co.aikar.commands.annotation.*
import me.clip.placeholderapi.PlaceholderAPI

@CommandAlias("message|pm|msg")
@Description("Send a player a private message.")
@CommandPermission("basic.message")
class MessageCommand : BaseCommand() {
    @Dependency
    lateinit var plugin: NeptuneBasic

    @CommandAlias("message|pm|msg")
    @Description("Send a player a private message.")
    @CommandCompletion("@allOnline")
    fun messageCommand(issuer: CommandIssuer, @Name("target") targetArg: String, @Name("message") messageArg: String) {
        var message = messageArg.trim()

        val player = plugin.server.getPlayer(issuer.uniqueId)!!

        val target = plugin.server.getPlayer(targetArg)
        if (target == null || !player.canSee(target)) {
            plugin.manager
                .sendMessage(issuer, MessageType.ERROR, MessageKeys.COULD_NOT_FIND_PLAYER, "{search}", targetArg)
            return
        }

        if(SettingsManager(plugin).getSettings(player).allowMessages == false) {
            player.sendMessage(ChatUtil.translate("&cError: You have your private messages turned off."))
            return
        }

        if(SettingsManager(plugin).getSettings(target).allowMessages == false) {
            player.sendMessage(ChatUtil.translate("&cError: &e${target.name}&c has their private messages turned off."))
            return
        }

        plugin.conversations.remove(player)
        plugin.conversations.remove(target)
        plugin.conversations[player] = target
        plugin.conversations[target] = player

        player.sendMessage(ChatUtil.translate(
            "&7(To ${PlaceholderAPI.setPlaceholders(target, "%luckperms_meta_color%")}${target.name}&7) ${message}"
        ))

        target.sendMessage(ChatUtil.translate(
            "&7(From ${PlaceholderAPI.setPlaceholders(player, "%luckperms_meta_color%")}${player.name}&7) ${message}"
        ))
    }
}