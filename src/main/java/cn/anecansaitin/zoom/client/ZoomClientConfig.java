package cn.anecansaitin.zoom.client;

import cn.anecansaitin.zoom.Zoom;
import cn.anecansaitin.zoom.client.gui.overlay.ZoomOverlay;
import cn.anecansaitin.zoom.client.listener.FirstPersonPlus;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

@EventBusSubscriber(modid = Zoom.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ZoomClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FREE_MODE_OVERLAY_OFFSETS = BUILDER.defineList("free_mode_overlay_offsets", IntArrayList.of(10, 10), null, (i) -> i instanceof Integer);
    private static final ModConfigSpec.BooleanValue FREE_MODE_OVERLAY_ENABLED = BUILDER.define("free_mode_overlay_enabled", true);
    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> FPS_PLUS_MODE_OVERLAY_OFFSETS = BUILDER.defineList("fps_plus_mode_overlay_offsets", IntArrayList.of(10, 60), null, (i) -> i instanceof Integer);
    private static final ModConfigSpec.BooleanValue FPS_PLUS_MODE_OVERLAY_ENABLED = BUILDER.define("fps_plus_mode_overlay_enabled", true);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FPS_PLUS_SHORTCUT_1 = BUILDER.defineList("fps_plus_shortcut_1", DoubleArrayList.of(0, 0, 0), null, (d) -> d instanceof Double);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FPS_PLUS_SHORTCUT_2 = BUILDER.defineList("fps_plus_shortcut_2", DoubleArrayList.of(0, 0, 0), null, (d) -> d instanceof Double);
    private static final ModConfigSpec.ConfigValue<List<? extends Double>> FPS_PLUS_SHORTCUT_3 = BUILDER.defineList("fps_plus_shortcut_3", DoubleArrayList.of(0, 0, 0), null, (d) -> d instanceof Double);
    public static final ModConfigSpec SPEC = BUILDER.build();

    public static void setFreeModeOverlayEnabled(boolean enabled) {
        FREE_MODE_OVERLAY_ENABLED.set(enabled);
    }

    public static void setFreeModeOverlayOffsets(int x, int y) {
        List<Integer> list = (List<Integer>) FREE_MODE_OVERLAY_OFFSETS.get();
        list.set(0, x);
        list.set(1, y);
        FREE_MODE_OVERLAY_OFFSETS.set(list);
    }

    public static void setFpsPlusModeOverlayEnabled(boolean enabled) {
        FPS_PLUS_MODE_OVERLAY_ENABLED.set(enabled);
    }

    public static void setFpsPlusModeOverlayOffsets(int x, int y) {
        List<Integer> list = (List<Integer>) FPS_PLUS_MODE_OVERLAY_OFFSETS.get();
        list.set(0, x);
        list.set(1, y);
        FPS_PLUS_MODE_OVERLAY_OFFSETS.set(list);
    }

    public static void setFpsPlusShortcut(int index, double x, double y, double z) {
        var shortcut = switch (index) {
            case 1 -> FPS_PLUS_SHORTCUT_1;
            case 2 -> FPS_PLUS_SHORTCUT_2;
            case 3 -> FPS_PLUS_SHORTCUT_3;
            default -> throw new IllegalStateException("Unexpected value: " + index);
        };
        DoubleArrayList doubles = DoubleArrayList.of();
        List<Double> list = (List<Double>) shortcut.get();
        list.set(0, x);
        list.set(1, y);
        list.set(2, z);
        shortcut.set(list);
    }

    public static void save() {
        SPEC.save();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        List<? extends Integer> freeModeOffset = FREE_MODE_OVERLAY_OFFSETS.get();
        ZoomOverlay.FREE_MODE_OFFSET_X = freeModeOffset.get(0);
        ZoomOverlay.FREE_MODE_OFFSET_Y = freeModeOffset.get(1);
        ZoomOverlay.FREE_MODE_ENABLED = FREE_MODE_OVERLAY_ENABLED.get();
        List<? extends Integer> fpsOffset = FPS_PLUS_MODE_OVERLAY_OFFSETS.get();
        ZoomOverlay.FPS_PLUS_OFFSET_X = fpsOffset.get(0);
        ZoomOverlay.FPS_PLUS_OFFSET_Y = fpsOffset.get(1);
        ZoomOverlay.FPS_PLUS_ENABLED = FPS_PLUS_MODE_OVERLAY_ENABLED.get();
        List<? extends Double> shortcut =  FPS_PLUS_SHORTCUT_1.get();
        FirstPersonPlus.setShortcut(1, shortcut.get(0).floatValue(), shortcut.get(1).floatValue(), shortcut.get(2).floatValue());
        shortcut = FPS_PLUS_SHORTCUT_2.get();
        FirstPersonPlus.setShortcut(2, shortcut.get(0).floatValue(), shortcut.get(1).floatValue(), shortcut.get(2).floatValue());
        shortcut = FPS_PLUS_SHORTCUT_3.get();
        FirstPersonPlus.setShortcut(3, shortcut.get(0).floatValue(), shortcut.get(1).floatValue(), shortcut.get(2).floatValue());
    }
}
