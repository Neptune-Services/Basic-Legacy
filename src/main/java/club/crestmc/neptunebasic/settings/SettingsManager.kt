package club.crestmc.neptunebasic.settings

import club.crestmc.neptunebasic.NeptuneBasic
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import org.bson.Document
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class SettingsManager(val plugin: NeptuneBasic) {
    fun getSettings(player: OfflinePlayer): Settings {
        var doc = plugin.databaseManager.usersCollection.find(eq("uuid", player.uniqueId.toString())).first()

        if(doc == null) {
            plugin.databaseManager.usersCollection.insertOne(
                Document("uuid", player.uniqueId.toString())
                    .append("allowMessages", true)
            )

            doc = plugin.databaseManager.usersCollection.find(eq("uuid", player.uniqueId.toString())).first()
        }

        val settings: Settings = Settings()
        settings.allowMessages = doc.getBoolean("allowMessages")
        return settings
    }

    fun updateSetting(player: OfflinePlayer, setting: String, value: Any): Settings {
        var doc = plugin.databaseManager.usersCollection.find(eq("uuid", player.uniqueId.toString())).first()!!
        plugin.databaseManager.usersCollection.updateOne(eq("uuid", player.uniqueId.toString()), Updates.combine(
            Updates.set(setting, value)
        ))

        return getSettings(player)
    }
}