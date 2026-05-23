package dev.donutsmp.addon.modules;

import dev.donutsmp.addon.DonutAddon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

/**
 * DonutSMP AutoReconnect Module
 *
 * Oyuncu DonutSMP sunucusunda Y 0 ile -5 arasina indiginde
 * otomatik olarak oyundan cikar ve 3 saniye icinde geri baglanir.
 */
public class AutoReconnectModule extends Module {

    // Durum makinesi: 0=izleniyor, 1=cikiliyor, 2=bekleniyor, 3=yeniden baglaniliyor
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
            // --- Durum 0: Sunucu ve Y konumunu izle ---
            case 0 -> {
                if (mc.player == null || mc.getNetworkHandler() == null) return;

                var server = mc.getCurrentServerEntry();
                if (server == null) return;

                // Sadece DonutSMP sunucusunda calis
                if (!server.address.toLowerCase().contains("donutsmp")) return;

                double y = mc.player.getY();
                if (y >= -5.0 && y <= 0.0) {
                    DonutAddon.LOG.info("[DonutSMP] Y={} algilandi, cikiliyor...", (int) y);
                    targetServer = server.address;
                    state = 1;
                    mc.execute(mc::disconnect);
                }
            }

            // --- Durum 1: Dünyanın kapanmasını bekle ---
            case 1 -> {
                if (mc.world == null) {
                    countdown = 60; // 3 saniye (20 tick/sn)
                    state = 2;
                }
            }

            // --- Durum 2: Yeniden baglama sayaci ---
            case 2 -> {
                if (--countdown <= 0) state = 3;
            }

            // --- Durum 3: Yeniden baglan ---
            case 3 -> {
                state = 0;
                if (targetServer != null && mc.world == null) {
                    String addr = targetServer;
                    targetServer = null;
                    DonutAddon.LOG.info("[DonutSMP] Yeniden baglaniliyor: {}", addr);
                    ServerInfo info = new ServerInfo("DonutSMP", addr, ServerInfo.ServerType.OTHER);
                    ConnectScreen.connect(new TitleScreen(), mc, ServerAddress.parse(addr), info, false, null);
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
