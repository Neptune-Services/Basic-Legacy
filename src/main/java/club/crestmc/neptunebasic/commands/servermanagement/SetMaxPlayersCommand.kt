package club.crestmc.neptunebasic.commands.servermanagement

import club.crestmc.neptunebasic.Constants
import club.crestmc.neptunebasic.NeptuneBasic
import club.crestmc.neptunebasic.utils.ChatUtil
import club.crestmc.neptunebasic.utils.ReflectionUtil
import club.crestmc.neptunebasic.utils.StaffMessage
import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.annotation.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.logging.Level

@CommandAlias("setmaxplayers")
@Description("Set the maximum amount of players that can join the server.")
@CommandPermission("basic.setmaxplayers")
class SetMaxPlayersCommand : BaseCommand() {
    @Dependency
    lateinit var plugin: NeptuneBasic

    @Default
    fun onSetmaxCommand(issuer: CommandIssuer, @Name("max") maxArg: String) {

        val max: Int = try {
            maxArg.toInt()
        } catch (e: IllegalArgumentException) {
            issuer.sendMessage(ChatUtil.translate("&cError: &e${maxArg} &cis not a valid number."))
            return
        }


        // Reflectively set the player limit field in the DedicatedPlayerList class
        try {
            val playerList: Any =
                ReflectionUtil.getOBCClass("CraftServer").getDeclaredMethod("getHandle").invoke(plugin.server)
            val maxPlayers = playerList.javaClass.superclass.getDeclaredField("maxPlayers")
            maxPlayers.isAccessible = true
            maxPlayers[playerList] = max
        } catch (e: ReflectiveOperationException) {
            plugin.logger.log(Level.SEVERE, "An error occurred whilst attempting to set the player limit", e)
        }

        // Save the new player limit to the server.properties file so it persists after restart

        // Save the new player limit to the server.properties file so it persists after restart
        try {
            val input = FileInputStream("./server.properties")
            val properties = Properties()
            properties.load(input)
            input.close()
            val output = FileOutputStream("./server.properties")
            properties.setProperty("max-players", max.toString() + "")
            properties.store(output, null)
            output.close()
        } catch (e: IOException) {
            plugin.logger.log(Level.SEVERE, "An error occurred whilst attempting to set the player limit", e)
        }

        issuer.sendMessage(ChatUtil.translate("${Constants.primaryColor}You've set the maximum players to ${Constants.secondaryColor}${max}${Constants.primaryColor}."))

        StaffMessage(plugin).sendStaffMessage("Set max players to ${max}", if(plugin.server.getPlayer(issuer.uniqueId) == null) "Console" else plugin.server.getPlayer(issuer.uniqueId)!!.name)
    }
}