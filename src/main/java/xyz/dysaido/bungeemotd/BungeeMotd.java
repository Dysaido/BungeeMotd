package xyz.dysaido.bungeemotd;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public final class BungeeMotd extends Plugin implements Listener {

    public static String motd;
    public static ServerPing.PlayerInfo playerinfo;
    private File file;
    private Configuration config;
    private static BungeeMotd instance;
    public static BungeeMotd getInstance() {
        return instance;
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        loadFile();
        load();
        getProxy().getPluginManager().registerCommand(this, new ReloadCommand("motdreload", "bungee.motd", "This is a bungee MOTD reload command"));
        getProxy().getPluginManager().registerListener(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadFile() {
        try {
            if (!getDataFolder().exists())
                getDataFolder().mkdir();
            file = new File(getDataFolder().getPath(), "config.yml");
            if (!file.exists()) {
                file.createNewFile();
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                getLines();
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @EventHandler
    public void onProxyPing(ProxyPingEvent event) {
        ServerPing ping = event.getResponse();
        ServerPing.Players players = ping.getPlayers();
        ServerPing.PlayerInfo[] pi = {playerinfo};
        players.setSample(pi);
        ping.setDescription(motd);
    }

    public void load() {
        motd = getString("MOTD.FirstLine") + "\n" + getString("MOTD.SecondLine");
        playerinfo = new ServerPing.PlayerInfo(getString("MOTD.PlayerInfo"), UUID.randomUUID());
    }

    private void getLines() {
        config.set("MOTD.FirstLine", "    &7&m--------&f &6&lVortex &fSzerverhálózat &7&m-------- ");
        config.set("MOTD.SecondLine", "&f                 &fTámogatott &c[1.7.x-1.16.x]");
        config.set("MOTD.PlayerInfo", "&e&lVortex&6Network &c[1.7.x-1.16.x]");
        saveConfig();
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String s) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(s));
    }
}
