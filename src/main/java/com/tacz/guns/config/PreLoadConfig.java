package com.tacz.guns.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.tacz.guns.GunMod;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Tiny stand-alone config that has to be readable before the regular config system is loaded
 * (the gunpack extraction runs very early). ModConfig is final in current Forge Config API Port,
 * so the old ModConfig subclass hack was replaced with a direct NightConfig read.
 */
public class PreLoadConfig {
    private static final String KEY = "gunpack.DefaultPackDebug";
    private static Path file;

    public static final Flag override = new Flag();

    public static void init() {
        // nothing to register; the file is read by load(Path)
    }

    public static void load(Path configBasePath) {
        file = configBasePath.resolve("tacz-pre.toml");
        try (CommentedFileConfig config = CommentedFileConfig.builder(file).sync().build()) {
            if (Files.exists(file)) {
                config.load();
            }
            Object value = config.get(KEY);
            if (value == null) {
                config.setComment("gunpack", "Enable to preserve local default gun pack edits.\nWhen disabled, changes to the TACZ version or bundled resources trigger replacement at startup.\nTo regenerate, disable this option, delete <game directory>/tacz/tacz_default_gun, then restart.");
                config.set(KEY, false);
                config.save();
                override.value = false;
            } else {
                override.value = Boolean.parseBoolean(String.valueOf(value));
            }
        } catch (Exception e) {
            GunMod.LOGGER.warn("Failed to read tacz-pre.toml, using defaults", e);
        }
    }

    public static void save() {
        if (file == null) {
            return;
        }
        try (CommentedFileConfig config = CommentedFileConfig.builder(file).sync().build()) {
            if (Files.exists(file)) {
                config.load();
            }
            config.set(KEY, override.value);
            config.save();
        } catch (Exception e) {
            GunMod.LOGGER.warn("Failed to save tacz-pre.toml", e);
        }
    }

    public static final class Flag {
        private volatile boolean value;

        public boolean get() {
            return value;
        }

        public void set(boolean value) {
            this.value = value;
        }
    }
}
