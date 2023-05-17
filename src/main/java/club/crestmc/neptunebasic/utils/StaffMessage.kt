package club.crestmc.neptunebasic.utils

import club.crestmc.neptunebasic.NeptuneBasic
import club.crestmc.neptunebasic.settings.SettingsManager
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class StaffMessage(val plugin: NeptuneBasic) {
    fun sendStaffMessage(string: String, causer: String) {
        for(player: Player in plugin.server.onlinePlayers) {
            if(player.hasPermission("basic.staff") && SettingsManager(plugin).getSettings(player as OfflinePlayer).staffMessages == true) {
                player.sendMessage(ChatUtil.translate("&7&o[$causer: &e$string&7&o]"))
            }
        }
    }
}