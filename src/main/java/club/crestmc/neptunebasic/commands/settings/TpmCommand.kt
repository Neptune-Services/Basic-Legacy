package club.crestmc.neptunebasic.commands.settings

import club.crestmc.neptunebasic.NeptuneBasic
import club.crestmc.neptunebasic.settings.SettingsManager
import club.crestmc.neptunebasic.utils.ChatUtil
import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Dependency
import co.aikar.commands.annotation.Description

@CommandAlias("tpm|toggleprivatemessages|togglemessages")
@Description("Toggle your private messages.")
class TpmCommand : BaseCommand() {
    @Dependency
    lateinit var plugin: NeptuneBasic

    @CommandAlias("tpm|toggleprivatemessages|togglemessages")
    @Description("Toggle your private messages.")
    fun onTpm(issuer: CommandIssuer) {
        val player = plugin.server.getPlayer(issuer.uniqueId)!!

        val settings = SettingsManager(plugin).getSettings(player)

        if(settings.allowMessages == true) {
            SettingsManager(plugin).updateSetting(player, "allowMessages", false)
            issuer.sendMessage(ChatUtil.translate("&cYou can no longer see private messages."))
        } else {
            SettingsManager(plugin).updateSetting(player, "allowMessages", true)
            issuer.sendMessage(ChatUtil.translate("&aYou can now see private messages."))
        }
    }
}