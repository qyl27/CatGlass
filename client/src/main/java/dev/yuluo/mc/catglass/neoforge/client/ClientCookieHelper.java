package dev.yuluo.mc.catglass.neoforge.client;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

public class ClientCookieHelper {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static List<Identifier> listCookies() {
        return getCookies().keySet().stream().toList();
    }

    private static Map<Identifier, byte[]> getCookies() {
        var mc = Minecraft.getInstance();
        var connection = mc.getConnection();
        if (connection == null) {
            var ex = new IllegalStateException("No connection available!");
            LOGGER.error("How did you get here?", ex);
            throw ex;
        }
        return connection.serverCookies;
    }

    public static byte @Nullable [] getCookie(Identifier key) {
        return getCookies().get(key);
    }

    public static void setCookie(Identifier key, byte[] payload) {
        getCookies().put(key, payload);
    }

    public static void unsetCookie(Identifier key) {
        getCookies().remove(key);
    }
}
