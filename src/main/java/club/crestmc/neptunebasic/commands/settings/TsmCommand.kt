package club.crestmc.neptunebasic.commands.settings

import club.crestmc.neptunebasic.NeptuneBasic
import club.crestmc.neptunebasic.settings.SettingsManager
import club.crestmc.neptunebasic.utils.ChatUtil
import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Dependency
import co.aikar.commands.annotation.Description

@CommandAlias("tsm|togglestaffmessages|togglestaffalerts")
@Description("Toggle recieving staff messages.")
@CommandPermission("basic.staff")
class TsmCommand : BaseCommand() {
    @Dependency
    lateinit var plugin: NeptuneBasic

    @Default
    fun onTpm(issuer: CommandIssuer) {
        val player = plugin.server.getPlayer(issuer.uniqueId)!!

        val settings = SettingsManager(plugin).getSettings(player)

        if(settings.staffMessages == true) {
            SettingsManager(plugin).updateSetting(player, "staffMessages", false)
            issuer.sendMessage(ChatUtil.translate("&cYou can no longer see staff messages."))
        } else {
            SettingsManager(plugin).updateSetting(player, "staffMessages", true)
            issuer.sendMessage(ChatUtil.translate("&aYou can now see staff messages."))
        }
    }
}