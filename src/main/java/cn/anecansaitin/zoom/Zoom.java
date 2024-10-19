package cn.anecansaitin.zoom;

import cn.anecansaitin.zoom.client.ZoomClientConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Zoom.MODID)
public class Zoom {
    public static final String MODID = "zoom";

    public Zoom(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ZoomClientConfig.SPEC);
    }
}
