package cn.sh1rocu.tacz.api.extension;

import net.minecraft.world.entity.vehicle.AbstractMinecart;

public interface IMinecart {
    default boolean tacz$canBeRidden() {
        return this instanceof net.minecraft.world.entity.vehicle.Minecart;
    }
}
