package club.crestmc.neptunebasic

import club.crestmc.neptunebasic.commands.BasicCommand
import club.crestmc.neptunebasic.commands.DevCommand
import club.crestmc.neptunebasic.commands.gamemode.GamemodeCommand
import club.crestmc.neptunebasic.commands.playermanagement.FeedCommand
import club.crestmc.neptunebasic.commands.playermanagement.HealCommand
import club.crestmc.neptunebasic.commands.playermanagement.RenameCommand
import club.crestmc.neptunebasic.commands.servermanagement.SetMaxPlayersCommand
import club.crestmc.neptunebasic.commands.settings.SettingsCommand
import club.crestmc.neptunebasic.commands.settings.TpmCommand
import club.crestmc.neptunebasic.commands.settings.TsmCommand
import club.crestmc.neptunebasic.commands.social.MessageCommand
import club.crestmc.neptunebasic.commands.social.ReplyCommand
import club.crestmc.neptunebasic.commands.teleport.TeleportCommand
import club.crestmc.neptunebasic.commands.teleport.TeleportHereCommand
import club.crestmc.neptunebasic.commands.teleport.TpAllCommand
import club.crestmc.neptunebasic.config.ConfigManager
import club.crestmc.neptunebasic.listeners.GUIClickListener
import club.crestmc.neptunebasic.database.DatabaseManager
import club.crestmc.neptunebasic.listeners.MOTDListener
import co.aikar.commands.BukkitCommandCompletionContext
import co.aikar.commands.MessageType
import co.aikar.commands.PaperCommandManager
import dev.demeng.sentinel.wrapper.SentinelClient
import dev.demeng.sentinel.wrapper.exception.*
import dev.demeng.sentinel.wrapper.exception.unchecked.UnauthorizedException
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException

class NeptuneBasic : JavaPlugin() {

    lateinit var configManager: ConfigManager
    lateinit var manager: PaperCommandManager
    lateinit var databaseManager: DatabaseManager
    var licensed: Boolean = false

    var conversations: MutableMap<OfflinePlayer, OfflinePlayer> = HashMap<OfflinePlayer, OfflinePlayer>()

    override fun onEnable() {
        configManager = ConfigManager(this)
        manager = PaperCommandManager(this)
        databaseManager = DatabaseManager(this)

        databaseManager.mongoConnect()

        lVerify()

        initAcf()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    private fun initAcf() {

        when (configManager.config?.getString("preset")) {
            "crest" -> {
                manager.setFormat(MessageType.HELP, ChatColor.DARK_AQUA, ChatColor.WHITE, ChatColor.AQUA)
                manager.setFormat(MessageType.SYNTAX, ChatColor.DARK_AQUA, ChatColor.AQUA, ChatColor.WHITE)
            }

            "minemen" -> {
                manager.setFormat(MessageType.HELP, ChatColor.LIGHT_PURPLE, ChatColor.YELLOW, ChatColor.YELLOW)
                manager.setFormat(MessageType.SYNTAX, ChatColor.YELLOW, ChatColor.LIGHT_PURPLE, ChatColor.WHITE)
            }

            "invaded" -> {
                manager.setFormat(MessageType.HELP, ChatColor.GOLD, ChatColor.GOLD, ChatColor.YELLOW)
                manager.setFormat(MessageType.SYNTAX, ChatColor.YELLOW, ChatColor.GOLD, ChatColor.WHITE)
            }

            "outerworlds" -> {
                manager.setFormat(MessageType.HELP, ChatColor.DARK_GRAY, ChatColor.LIGHT_PURPLE, ChatColor.GRAY)
                manager.setFormat(
                    MessageType.SYNTAX,
                    ChatColor.GRAY,
                    ChatColor.LIGHT_PURPLE,
                    ChatColor.WHITE,
                    ChatColor.BOLD,
                    ChatColor.DARK_PURPLE,
                    ChatColor.WHITE,
                    ChatColor.DARK_GRAY
                )
            }

            else -> {}
        }

        manager.commandCompletions.registerCompletion(
            "allOnline"
        ) { c: BukkitCommandCompletionContext? ->
            val toReturn: MutableCollection<String> = HashSet()
            for (p in server.onlinePlayers) {
                if(c?.issuer?.isPlayer == true) {
                    if(c.player?.canSee(p.player!!) == true) {
                        toReturn.add(p.name)
                    }
                } else {
                    toReturn.add(p.name)
                }
            }
            toReturn
        }

        manager.commandCompletions.registerCompletion(
            "gamemodes"
        ) { c: BukkitCommandCompletionContext? ->
            val toReturn: MutableCollection<String> = HashSet()
            toReturn.add("CREATIVE")
            toReturn.add("SURVIVAL")
            toReturn.add("ADVENTURE")
            toReturn.add("SPECTATOR")
            toReturn
        }

        manager.enableUnstableAPI("help")

        manager.registerCommand(BasicCommand())
        manager.registerCommand(DevCommand())

        if(licensed) {
            manager.registerCommand(TeleportCommand())
            manager.registerCommand(TeleportHereCommand())
            manager.registerCommand(TpAllCommand())

            manager.registerCommand(MessageCommand())
            manager.registerCommand(ReplyCommand())

            manager.registerCommand(HealCommand())
            manager.registerCommand(FeedCommand())
            manager.registerCommand(RenameCommand())

            manager.registerCommand(SettingsCommand())
            manager.registerCommand(TpmCommand())
            manager.registerCommand(TsmCommand())

            manager.registerCommand(SetMaxPlayersCommand())

            manager.registerCommand(GamemodeCommand())
            server.pluginManager.registerEvents(GUIClickListener(), this)
            server.pluginManager.registerEvents(MOTDListener(this), this)
        }

        return;
    }

    private fun lVerify() {
        val sentinel = SentinelClient("http://onepickbemonkey.boredsmp.club:2007/api/v1", "naa9a8hpo7i2ld17r941u6g7nt")

        var success = false
        // Get the platform controller.
        try {
            sentinel.licenseController.auth(configManager.config?.getString("license-key"), "basic", null, null, configManager.config?.getString("serverName")!!, SentinelClient.getCurrentIp())
            success = true
            licensed = true
        } catch (e: InvalidLicenseException) {
            InternalErrors().throwLicenseError("The provided license key is invalid.")
        } catch (e: ExpiredLicenseException) {
            InternalErrors().throwLicenseError("The provided license has expired.")
        } catch (e: BlacklistedLicenseException) {
            logger.info(" ")
            logger.info(" ")
            logger.info("=== LICENSE BLACKLISTED ===")
            logger.info("Your license has been blacklisted. More information can be found below.")
            logger.info("Reason: " + e.blacklist.reason)
            logger.info("Timestamp: " + e.blacklist.timestamp)
            logger.info("The plugin will now shut down.")
            logger.info("Please contact support if you believe this is false.")
            logger.info("===========================")
            logger.info(" ")
            logger.info(" ")
            InternalErrors().throwLicenseError("The provided license key is blacklisted.")
        } catch (e: ExcessiveServersException) {
            InternalErrors().throwLicenseError("The plugin cannot load because the server limit has been exceeded for the license.")
        } catch (e: ExcessiveIpsException) {
            InternalErrors().throwLicenseError("The plugin cannot load because the IP limit has been exceeded for the license.")
        } catch (e: InvalidProductException) {
            InternalErrors().throwLicenseError("The provided license is for a different product. Ensure you are using the correct key.")
        } catch (e: InvalidPlatformException) {
            InternalErrors().throwLicenseError("INVALID_PLATFORM_LICENSE")
        } catch (e: IOException) {
            InternalErrors().throwLicenseError("Could not connect to the license servers, please try again later.")
        } catch (e: NoSuchMethodError) {
            e.printStackTrace()
            InternalErrors().throwLicenseError("Could not parse string [Contact Developer]")
        } catch (e: UnauthorizedException) {
            InternalErrors().throwLicenseError("The license server rejected the api key. You most likely cannot fix this issue. Ensure the plugin is completely up to date, as this is most likely caused by an API key rotation.")
        }

        if(success) {
            logger.info("Your license has been verified.")
        }
    }
}