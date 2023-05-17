package club.crestmc.neptunebasic.commands.gamemode

import org.bukkit.GameMode

class GamemodeTools {
    fun getGamemode(gamemode: String): GameMode? {
        when(gamemode.lowercase()) {
            "creative", "1", "c" -> {
                return GameMode.CREATIVE
            }

            "survival", "0", "s" -> {
                return GameMode.SURVIVAL
            }

            "adventure", "2", "a" -> {
                return GameMode.ADVENTURE
            }

            "spectator", "3", "sp" -> {
                return GameMode.SPECTATOR
            }

            else -> {
                return null
            }

        }
    }
}