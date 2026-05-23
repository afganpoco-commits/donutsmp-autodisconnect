package dev.donutsmp.addon;

import com.mojang.logging.LogUtils;
import dev.donutsmp.addon.modules.AutoReconnectModule;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class DonutAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("DonutSMP");

    @Override
    public void onInitialize() {
        LOG.info("DonutSMP AutoReconnect Addon yuklendi!");
        Modules.get().add(new AutoReconnectModule());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "dev.donutsmp.addon";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("afganpoco-commits", "donutsmp-autodisconnect");
    }
}
