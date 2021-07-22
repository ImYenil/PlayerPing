package net.choco.playerping.command;

import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import net.choco.playerping.utility.PingUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("ping")
public class PingCommand extends CommandBase {

    @Default
    public void defaultCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.sendMessage(String.valueOf(PingUtils.getPing(p)));
        }
    }
}
