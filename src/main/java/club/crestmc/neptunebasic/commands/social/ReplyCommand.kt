package club.crestmc.neptunebasic.commands.social

import club.crestmc.neptunebasic.Constants
import club.crestmc.neptunebasic.NeptuneBasic
import club.crestmc.neptunebasic.settings.SettingsManager
import club.crestmc.neptunebasic.utils.ChatUtil
import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.MessageKeys
import co.aikar.commands.MessageType
import co.aikar.commands.annotation.*
import com.mongodb.client.model.Filters
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.OfflinePlayer

@CommandAlias("reply|r")
@Description("Reply to your most recent message.")
@CommandPermission("basic.reply")
class ReplyCommand : BaseCommand() {
    @Dependency
    lateinit var plugin: NeptuneBasic

    @Default
    fun onReply(issuer: CommandIssuer, @Optional @Name("message") messageArg: String?) {

        val doc = plugin.databaseManager.mongoClient.getDatabase("NeptuneCobalt").getCollection("punishments")
            .find(Filters.and(Filters.eq("uuid", issuer.uniqueId.toString()), Filters.eq("active", true)))

        if(doc.first() != null) {
            issuer.sendMessage(ChatUtil.translate("&cError: You cannot message people while you are muted."))
            return
        }

        val player = plugin.server.getPlayer(issuer.uniqueId)
        val conversation = plugin.conversations[player as OfflinePlayer]
        if(conversation == null) {
            player.sendMessage(ChatUtil.translate("&cYou are currently not in a conversation."))
            return
        }
        if (messageArg == null) {
            player.sendMessage(ChatUtil.translate("${Constants.primaryColor}You're currently messaging ${Constants.secondaryColor}${conversation.name}${Constants.primaryColor}."))
            return // Exit the function early if messageArg is null
        }
        if(messageArg == null) {
            return
        } else {
            val message = messageArg.trim()
            val target = plugin.server.getPlayer(conversation.name!!)
            if (target == null || !player.canSee(target) || !target.isOnline) {
                plugin.manager
                    .sendMessage(issuer, MessageType.ERROR, MessageKeys.COULD_NOT_FIND_PLAYER, "{search}", conversation.name)
                return
            }

            if(SettingsManager(plugin).getSettings(player).allowMessages == false && !player.hasPermission("basic.tpmbypass")) {
                player.sendMessage(ChatUtil.translate("&cError: You have your private messages turned off."))
                return
            }

            if(SettingsManager(plugin).getSettings(target).allowMessages == false && !player.hasPermission("basic.tpmbypass")) {
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
}