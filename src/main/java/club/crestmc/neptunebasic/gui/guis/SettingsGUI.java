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

        Button tpmButton = new Button(messages.getItem(), messages.getAmount(), messages.getName(), messages.getLore());
        tpmButton.getLore().add(ChatUtil.INSTANCE.translate("&7Recieve private messages from others."));
        tpmButton.getLore().add(ChatUtil.INSTANCE.translate("&7"));
        tpmButton.getLore().add(ChatUtil.INSTANCE.translate(settings.getAllowMessages() == true ? " &e&l" + '\u2503' + " &aEnabled" : " &e" + '\u2503' + " &7Enabled"));
        tpmButton.getLore().add(ChatUtil.INSTANCE.translate(settings.getAllowMessages() == false ? " &e&l" + '\u2503' + " &cDisabled" : " &e" + '\u2503' + " &7Disabled"));
        tpmButton.getLore().add(ChatUtil.INSTANCE.translate("&7"));
        tpmButton.getLore().add(ChatUtil.INSTANCE.translate("&eClick to toggle!"));
        tpmButton.setAction(() -> {

            GUI.close(gui);
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getServer().dispatchCommand(player, "tpm");
                }
            }.runTaskLater(plugin, 1);
        });

        gui.setButton(0, tpmButton);
    }
}
