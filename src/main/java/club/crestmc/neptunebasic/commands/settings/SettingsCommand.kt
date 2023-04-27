package club.crestmc.neptunebasic.commands.settings

import club.crestmc.neptunebasic.NeptuneBasic
import club.crestmc.neptunebasic.gui.GUI
import club.crestmc.neptunebasic.gui.guis.SettingsGUI
import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Dependency
import co.aikar.commands.annotation.Description

@CommandAlias("settings|options|preferences")
@Description("Edit your settings on the server.")
class SettingsCommand : BaseCommand() {
    @Dependency
    lateinit var plugin: NeptuneBasic

    @CommandAlias("settings|options|preferences")
    @Description("Edit your settings on the server.")
    fun onSettings(issuer: CommandIssuer) {
        val player = plugin.server.getPlayer(issuer.uniqueId)!!

        val settingsGui = SettingsGUI(player).setup(player)
        GUI.open(SettingsGUI.getGui())
    }

}