package com.tacz.guns.api.event.common;

import cn.sh1rocu.tacz.api.event.BaseEvent;

/** Stub: KubeJS integration is disabled in the 1.21.4 port. */
public interface KubeJSGunEventPoster<E extends BaseEvent> {
    default void postEventToKubeJS(E event) {}
    default void postClientEventToKubeJS(E event) {}
    default void postServerEventToKubeJS(E event) {}
}
