package cn.sh1rocu.simplebedrockmodel.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/** Local replacement: simplebedrockmodel for 1.21.x no longer ships RenderTickEvent. Fired from TaCZFabricClient. */
public class RenderTickEvent {
    public enum Phase { START, END }

    public final Phase phase;
    public final float renderTickTime;

    public RenderTickEvent(Phase phase, float renderTickTime) {
        this.phase = phase;
        this.renderTickTime = renderTickTime;
    }

    public interface Callback {
        void onRenderTick(RenderTickEvent event);
    }

    public static final Event<Callback> EVENT = EventFactory.createArrayBacked(Callback.class, callbacks -> event -> {
        for (Callback callback : callbacks) {
            callback.onRenderTick(event);
        }
    });
}
