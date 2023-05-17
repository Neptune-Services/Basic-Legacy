package club.crestmc.neptunebasic.listeners

import club.crestmc.neptunebasic.NeptuneBasic
import club.crestmc.neptunebasic.utils.ChatUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class MOTDListener(val plugin: NeptuneBasic) : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if(plugin.configManager.config!!.getBoolean("motd.enabled")) {
            plugin.server.scheduler.scheduleSyncDelayedTask(plugin, {
                e.player.sendMessage(ChatUtil.translate(
                    plugin.configManager.config!!.getString("motd.content")
                ))
            }, 1)
        }
    }
}