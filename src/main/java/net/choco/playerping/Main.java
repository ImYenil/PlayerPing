package net.choco.playerping;

import me.mattstudios.mf.base.CommandManager;
import net.choco.playerping.command.PingCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private CommandManager commandManager;

    public void onEnable() {
        commandManager = new CommandManager(this);
        commandManager.register(new PingCommand());
    }
}
