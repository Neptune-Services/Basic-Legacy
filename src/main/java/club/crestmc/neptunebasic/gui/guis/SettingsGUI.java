package club.crestmc.neptunebasic.gui.guis;

import club.crestmc.neptunebasic.NeptuneBasic;
import club.crestmc.neptunebasic.gui.Button;
import club.crestmc.neptunebasic.gui.CustomGUI;
import club.crestmc.neptunebasic.gui.GUI;
import club.crestmc.neptunebasic.settings.Settings;
import club.crestmc.neptunebasic.settings.SettingsManager;
import club.crestmc.neptunebasic.utils.ChatUtil;
import club.crestmc.neptunebasic.utils.ItemBuilder;
import com.cryptomorin.xseries.XMaterial;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Objects;

public class SettingsGUI extends CustomGUI {
    private final NeptuneBasic plugin;
    public SettingsGUI(Player player) {
        super(player, 9, ChatUtil.INSTANCE.translate("&e&lSettings"));
        plugin = NeptuneBasic.getPlugin(NeptuneBasic.class);
    }

    public void setup(Player player) {
        Settings settings = new SettingsManager(plugin).getSettings(player);
        ItemBuilder messages = new ItemBuilder(XMaterial.WRITABLE_BOOK.parseItem(), 1, "&e&lPrivate Messages", new ArrayList<>());
        ItemBuilder staffMessages = new ItemBuilder(XMaterial.DIAMOND_SWORD.parseItem(), 1, "&e&lStaff Messages", new ArrayList<>());

        Button tpmButton = new Button(messages.getItem(), messages.getAmount(), messages.getName(), messages.getLore());
        tpmButton.getLore().add(ChatUtil.INSTANCE.translate("&7Receive private messages from others."));
        tpmButton.getLore().add(ChatUtil.INSTANCE.translate("&7"));
        tpmButton.getLore().add(ChatUtil.INSTANCE.translate(settings.getAllowMessages() == true ? " &e&l" + '\u2503' + " &aEnabled" : " &e" + '\u2503' + " &7Enabled"));
        tpmButton.getLore().add(ChatUtil.INSTANCE.translate(settings.getAllowMessages() == false ? " &e&l" + '\u2503' + " &cDisabled" : " &e" + '\u2503' + " &7Disabled"));
        tpmButton.getLore().add(ChatUtil.INSTANCE.translate("&7"));
        tpmButton.getLore().add(ChatUtil.INSTANCE.translate("&eClick to toggle!"));
        tpmButton.setAction(() -> {

            //GUI.close(gui);
            new BukkitRunnable() {
                @Override
                public void run() {
                    settings.setAllowMessages(!settings.getAllowMessages());

                    tpmButton.getLore().clear();
                    tpmButton.getLore().add(ChatUtil.INSTANCE.translate("&7Receive private messages from others."));
                    tpmButton.getLore().add(ChatUtil.INSTANCE.translate("&7"));
                    tpmButton.getLore().add(ChatUtil.INSTANCE.translate(settings.getAllowMessages() == true ? " &e&l" + '\u2503' + " &aEnabled" : " &e" + '\u2503' + " &7Enabled"));
                    tpmButton.getLore().add(ChatUtil.INSTANCE.translate(settings.getAllowMessages() == false ? " &e&l" + '\u2503' + " &cDisabled" : " &e" + '\u2503' + " &7Disabled"));
                    tpmButton.getLore().add(ChatUtil.INSTANCE.translate("&7"));
                    tpmButton.getLore().add(ChatUtil.INSTANCE.translate("&eClick to toggle!"));
                    gui.setButton(0, tpmButton);

                    plugin.getServer().dispatchCommand(player, "tpm");

                }
            }.runTaskLater(plugin, 1);
        });

        gui.setButton(0, tpmButton);

        // STAFF MESSAGES
        Button tsmButton = new Button(staffMessages.getItem(), staffMessages.getAmount(), staffMessages.getName(), staffMessages.getLore());
        tsmButton.getLore().add(ChatUtil.INSTANCE.translate("&7Receive staff-only alerts."));
        tsmButton.getLore().add(ChatUtil.INSTANCE.translate("&7"));
        tsmButton.getLore().add(ChatUtil.INSTANCE.translate(settings.getStaffMessages() == true ? " &e&l" + '\u2503' + " &aEnabled" : " &e" + '\u2503' + " &7Enabled"));
        tsmButton.getLore().add(ChatUtil.INSTANCE.translate(settings.getStaffMessages() == false ? " &e&l" + '\u2503' + " &cDisabled" : " &e" + '\u2503' + " &7Disabled"));
        tsmButton.getLore().add(ChatUtil.INSTANCE.translate("&7"));
        tsmButton.getLore().add(ChatUtil.INSTANCE.translate(player.hasPermission("basic.staff") ? "&eClick to toggle!" : "&cYou cannot edit this setting."));
        tsmButton.setAction(() -> {

            //GUI.close(gui);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(player.hasPermission("basic.staff")) {
                        settings.setStaffMessages(!settings.getStaffMessages());

                        tsmButton.getLore().clear();
                        tsmButton.getLore().add(ChatUtil.INSTANCE.translate("&7Receive staff-only alerts."));
                        tsmButton.getLore().add(ChatUtil.INSTANCE.translate("&7"));
                        tsmButton.getLore().add(ChatUtil.INSTANCE.translate(settings.getStaffMessages() == true ? " &e&l" + '\u2503' + " &aEnabled" : " &e" + '\u2503' + " &7Enabled"));
                        tsmButton.getLore().add(ChatUtil.INSTANCE.translate(settings.getStaffMessages() == false ? " &e&l" + '\u2503' + " &cDisabled" : " &e" + '\u2503' + " &7Disabled"));
                        tsmButton.getLore().add(ChatUtil.INSTANCE.translate("&7"));
                        tsmButton.getLore().add(ChatUtil.INSTANCE.translate("&eClick to toggle!"));
                        gui.setButton(1, tsmButton);

                        plugin.getServer().dispatchCommand(player, "tsm");
                    } else {
                        player.sendMessage(ChatUtil.INSTANCE.translate("&cError: You cannot edit this setting."));
                    }

                }
            }.runTaskLater(plugin, 1);
        });

        gui.setButton(1, tsmButton);
    }
}
