package cn.anecansaitin.zoom.client.listener;

import cn.anecansaitin.zoom.Zoom;
import cn.anecansaitin.zoom.client.ZoomKeyMapping;
import cn.anecansaitin.zoom.client.gui.screen.ZoomSettingScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = Zoom.MODID, value = Dist.CLIENT)
public class ZoomKeyClicked {
    @SubscribeEvent
    public static void keyClick(ClientTickEvent.Post event) {
        while (ZoomKeyMapping.FREE_MODE.get().consumeClick()) {
            FreeMode.ENABLED = !FreeMode.ENABLED;
        }

        if (FreeMode.ENABLED) {
            while (ZoomKeyMapping.MOVE_MODE.get().consumeClick()) {
                FreeMode.MOVE_MODE = !FreeMode.MOVE_MODE;
            }
        } else {
            while (ZoomKeyMapping.MOVE_MODE.get().consumeClick()) {}
        }

        while (ZoomKeyMapping.SETTING.get().consumeClick()) {
            Minecraft.getInstance().setScreen(new ZoomSettingScreen());
        }

        while (ZoomKeyMapping.FPS_PLUS_MODE.get().consumeClick()) {
            FirstPersonPlus.ENABLED = !FirstPersonPlus.ENABLED;
        }
    }
}
