package dev.donutsmp.addon.modules;

import dev.donutsmp.addon.DonutAddon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

/**
 * DonutSMP AutoReconnect Module — MC 1.21.11 (Mojang mappings)
 *
 * Oyuncu DonutSMP sunucusunda Y 0 ile -5 arasina indiginde
 * otomatik olarak oyundan cikar ve 3 saniye icinde geri baglanir.
 */
public class AutoReconnectModule extends Module {

    private int state = 0;
    private int countdown = 0;
    private String targetServer = null;

    public AutoReconnectModule() {
        super(DonutAddon.CATEGORY, "donut-auto-reconnect",
            "DonutSMP'de Y 0 ile -5 arasina inince otomatik olarak cikip yeniden baglanir.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        switch (state) {
            case 0 -> {
                if (mc.player == null || mc.getConnection() == null) return;

                var server = mc.getCurrentServer();
                if (server == null) return;
                if (!server.ip.toLowerCase().contains("donutsmp")) return;

                double y = mc.player.getY();
                if (y >= -5.0 && y <= 0.0) {
                    DonutAddon.LOG.info("[DonutSMP] Y={} algilandi, cikiliyor...", (int) y);
                    targetServer = server.ip;
                    state = 1;
                    mc.execute(() -> mc.disconnect(new TitleScreen(), false));
                }
            }
            case 1 -> {
                if (mc.level == null) {
                    countdown = 60;
                    state = 2;
                }
            }
            case 2 -> {
                if (--countdown <= 0) state = 3;
            }
            case 3 -> {
                state = 0;
                if (targetServer != null && mc.level == null) {
                    String addr = targetServer;
                    targetServer = null;
                    DonutAddon.LOG.info("[DonutSMP] Yeniden baglaniliyor: {}", addr);
                    ServerData serverData = new ServerData("DonutSMP", addr, ServerData.Type.OTHER);
                    ConnectScreen.startConnecting(new TitleScreen(), mc, ServerAddress.parseString(addr), serverData, false, null);
                }
            }
        }
    }

    @Override
    public void onDeactivate() {
        state = 0;
        countdown = 0;
        targetServer = null;
    }
}
