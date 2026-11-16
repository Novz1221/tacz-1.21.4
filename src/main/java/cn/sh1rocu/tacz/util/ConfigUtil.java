package cn.sh1rocu.tacz.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.function.Predicate;

public class ConfigUtil {
    private ConfigUtil() {
    }

    private static final Splitter DOT_SPLITTER = Splitter.on(".");

    private static List<String> split(String path) {
        return Lists.newArrayList(DOT_SPLITTER.split(path));
    }

    public static <T> ModConfigSpec.ConfigValue<List<? extends T>> defineListAllowEmpty(ModConfigSpec.Builder builder, String path, List<? extends T> defaultValue, Predicate<Object> elementValidator) {
        return defineListAllowEmpty(builder, split(path), defaultValue, elementValidator);
    }

    public static <T> ModConfigSpec.ConfigValue<List<? extends T>> defineListAllowEmpty(ModConfigSpec.Builder builder, List<String> path, List<? extends T> defaultValue, Predicate<Object> elementValidator) {
        return builder.defineListAllowEmpty(path, () -> defaultValue, elementValidator);
    }
}
