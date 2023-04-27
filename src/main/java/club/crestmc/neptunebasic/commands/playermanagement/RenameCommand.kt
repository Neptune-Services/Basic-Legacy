package club.crestmc.neptunebasic.commands.playermanagement

import club.crestmc.neptunebasic.Constants
import club.crestmc.neptunebasic.NeptuneBasic
import club.crestmc.neptunebasic.utils.ChatUtil
import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.annotation.*
import org.bukkit.Material
import org.bukkit.entity.Player


@CommandAlias("rename")
@Description("Rename the item in your hand.")
@CommandPermission("basic.rename")
class RenameCommand : BaseCommand() {
    @Dependency
    lateinit var plugin: NeptuneBasic

    @CommandAlias("rename")
    @Description("Rename the item in your hand.")
    fun onRenameRun(issuer: CommandIssuer, @Name("name") nameArg: String) {
        val name = nameArg.trim()

        val player = plugin.server.getPlayer(issuer.uniqueId)!!

        if(player.itemInHand.type == Material.AIR) {
            player.sendMessage(ChatUtil.translate("&cError: You must be holding an item."))
            return
        }

        val heldItem = player.itemInHand // get the item currently held by the player

        val itemMeta = heldItem.itemMeta // get the metadata for the held item


        itemMeta.setDisplayName(ChatUtil.translate(name)) // set the display name for the held item


        heldItem.itemMeta = itemMeta // update the metadata for the held item

        player.setItemInHand(heldItem) // update the player's held item

        player.sendMessage(ChatUtil.translate(
            "${Constants.primaryColor}You've renamed your currently held item to ${Constants.secondaryColor}${name}${Constants.primaryColor}."
        ))

        for(lp: Player in plugin.server.onlinePlayers) {
            if(lp.hasPermission("basic.staff")) {
                lp.sendMessage(ChatUtil.translate(
                    "&7&o[${player.name}: &eRenamed their item to ${name}&7&o]"
                ))
            }
        }

    }
}