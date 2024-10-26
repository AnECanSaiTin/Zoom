package cn.anecansaitin.zoom.client.listener;

import cn.anecansaitin.zoom.Zoom;
import cn.anecansaitin.zoom.client.ZoomClientConfig;
import cn.anecansaitin.zoom.client.ZoomKeyMapping;
import cn.anecansaitin.zoom.client.gui.screen.ZoomSettingScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.joml.Vector3f;

import static cn.anecansaitin.zoom.client.ZoomKeyMapping.*;

@EventBusSubscriber(modid = Zoom.MODID, value = Dist.CLIENT)
public class ZoomKeyClicked {
    private static Mode mode = Mode.NONE;

    @SubscribeEvent
    public static void keyClick(ClientTickEvent.Post event) {
        while (FREE_MODE.get().consumeClick()) {
            switchMode(Mode.FREE);
        }

        if (FreeMode.ENABLED) {
            while (MOVE_MODE.get().consumeClick()) {
                FreeMode.MOVE_MODE = !FreeMode.MOVE_MODE;
            }
        } else {
            while (MOVE_MODE.get().consumeClick()) {
            }
        }

        while (SETTING.get().consumeClick()) {
            Minecraft.getInstance().setScreen(new ZoomSettingScreen());
        }

        while (FPS_PLUS_MODE.get().consumeClick()) {
            switchMode(Mode.FPS_PLUS);
        }

        if (Z_ROT_ANTICLOCKWISE.get().isDown()) {
            if (FirstPersonPlus.ENABLED) {
                FirstPersonPlus.zRot(true);
            } else if (FreeMode.ENABLED) {
                FreeMode.adjustZRot(true);
            }
        }

        if (Z_ROT_CLOCKWISE.get().isDown()) {
            if (FirstPersonPlus.ENABLED) {
                FirstPersonPlus.zRot(false);
            } else if (FreeMode.ENABLED) {
                FreeMode.adjustZRot(false);
            }
        }

        if (FOV_UP.get().isDown()) {
            if (FirstPersonPlus.ENABLED) {
                FirstPersonPlus.adjustFOV(true);
            } else if (FreeMode.ENABLED) {
                FreeMode.adjustFOV(true);
            }
        }

        if (FOV_DOWN.get().isDown()) {
            if (FirstPersonPlus.ENABLED) {
                FirstPersonPlus.adjustFOV(false);
            } else if (FreeMode.ENABLED) {
                FreeMode.adjustFOV(false);
            }
        }

        if (MEMORY.get().isDown()) {
            if (mode == Mode.FPS_PLUS) {
                if (SHORTCUT_1.get().isDown()) {
                    Vector3f rot = FirstPersonPlus.getRot();
                    ZoomClientConfig.setFpsPlusShortcut(1, rot.x, rot.y, rot.z);
                    ZoomClientConfig.save();
                } else if (SHORTCUT_2.get().isDown()) {
                    Vector3f rot = FirstPersonPlus.getRot();
                    ZoomClientConfig.setFpsPlusShortcut(2, rot.x, rot.y, rot.z);
                    ZoomClientConfig.save();
                } else if (SHORTCUT_3.get().isDown()) {
                    Vector3f rot = FirstPersonPlus.getRot();
                    ZoomClientConfig.setFpsPlusShortcut(3, rot.x, rot.y, rot.z);
                    ZoomClientConfig.save();
                }
            }
        } else {
            if (mode == Mode.FPS_PLUS) {
                if (ZoomKeyMapping.SHORTCUT_1.get().isDown()) {
                    FirstPersonPlus.setShortcutIndex((byte) 1);
                } else if (ZoomKeyMapping.SHORTCUT_2.get().isDown()) {
                    FirstPersonPlus.setShortcutIndex((byte) 2);
                } else if (ZoomKeyMapping.SHORTCUT_3.get().isDown()) {
                    FirstPersonPlus.setShortcutIndex((byte) 3);
                } else {
                    FirstPersonPlus.setShortcutIndex((byte) 0);
                }
            }
        }
    }

    private static void switchMode(Mode mode) {
        if (mode == ZoomKeyClicked.mode) {
            mode.launch.run();
            ZoomKeyClicked.mode = Mode.NONE;
        } else {
            ZoomKeyClicked.mode.launch.run();
            ZoomKeyClicked.mode = mode;
            mode.into.run();
        }
    }

    private enum Mode {
        NONE(() -> {
        }, () -> {
        }),
        FREE(() -> FreeMode.ENABLED = true, () -> FreeMode.ENABLED = false),
        FPS_PLUS(() -> FirstPersonPlus.ENABLED = true, () -> FirstPersonPlus.ENABLED = false);

        private final Runnable into;
        private final Runnable launch;

        Mode(Runnable into, Runnable launch) {
            this.into = into;
            this.launch = launch;
        }
    }
}
