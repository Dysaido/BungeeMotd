package xyz.dysaido.bungeemotd;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommand extends Command {
    public ReloadCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("Reload Successful");
        BungeeMotd.getInstance().loadFile();
        BungeeMotd.getInstance().load();
    }
}
