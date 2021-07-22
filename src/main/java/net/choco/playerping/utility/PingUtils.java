package net.choco.playerping.utility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingUtils {

    private static final Function<Player, Integer> PLAYER_GET_PING;

    public static int getPing(Player player) {
        return PLAYER_GET_PING.apply(player);
    }

    public static int minecraftVersion() {
        try {
            Matcher matcher = Pattern.compile("\\(MC: (\\d)\\.(\\d+)\\.?(\\d+?)?\\)").matcher(Bukkit.getVersion());
            if (matcher.find()) {
                return Integer.parseInt(matcher.toMatchResult().group(2), 10);
            }
            throw new IllegalArgumentException(String.format("No match found in '%s'", Bukkit.getVersion()));
        }
        catch (IllegalArgumentException ex) {
            throw new RuntimeException("Failed to determine Minecraft version", ex);
        }
    }

    static {
        PLAYER_GET_PING = new Function<Player, Integer>() {
            private Field ping;
            private Method getHandle;

            @Override
            public Integer apply(Player player) {
                if (this.ping == null) {
                    try {
                        this.cacheReflection(player);
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException ex) {
                        ex.printStackTrace();
                    }
                }
                try {
                    return this.ping.getInt(this.getHandle.invoke(player));
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();
                    return -1;
                }
            }

            private void cacheReflection(Player player) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
                (this.getHandle = player.getClass().getDeclaredMethod("getHandle")).setAccessible(true);
                Object entityPlayer = this.getHandle.invoke(player);
                (this.ping = entityPlayer.getClass().getDeclaredField((minecraftVersion() >= 17) ? "e" : "ping")).setAccessible(true);
            }
        };
    }
}