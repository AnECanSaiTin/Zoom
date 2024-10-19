package cn.anecansaitin.zoom.client.listener;

import cn.anecansaitin.freecameraapi.CameraModifierManager;
import cn.anecansaitin.freecameraapi.ICameraModifier;
import cn.anecansaitin.zoom.Zoom;
import cn.anecansaitin.zoom.client.ZoomKeyMapping;
import cn.anecansaitin.zoom.client.event.MouseMoveEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@EventBusSubscriber(modid = Zoom.MODID, value = Dist.CLIENT)
public class FirstPersonPlus {
    public static boolean ENABLED;
    private static final Vector3f pos = new Vector3f();
    private static final Vector3f rot = new Vector3f();
    private static boolean check;
    private static final ICameraModifier modifier = CameraModifierManager.createModifier(Zoom.MODID, false);

    @SubscribeEvent
    public static void computeFov(ViewportEvent.ComputeFov event) {
        if (!ENABLED || !check || !ZoomKeyMapping.TURN_HEAD.get().isDown() || Minecraft.getInstance().options.getCameraType() != CameraType.FIRST_PERSON) {
            return;
        }

        rot.x = Mth.clamp(rot.x, -90, 90);
        rot.y = Mth.clamp(rot.y, -180, 180);
        modifier.setRotationYXZ(rot);
        modifier.setPos(pos.x, pos.y, pos.z);
    }

    @SubscribeEvent
    public static void mouseMove(MouseMoveEvent event) {
        Minecraft mc = Minecraft.getInstance();

        if (ENABLED && ZoomKeyMapping.TURN_HEAD.get().isDown() && mc.options.getCameraType() == CameraType.FIRST_PERSON) {
            LocalPlayer player = mc.player;
            float partialTick = mc.gameRenderer.getMainCamera().getPartialTickTime();
            Vec3 eyePosition = player.getEyePosition(partialTick);
            Vec3 position = player.getPosition(partialTick);
            pos.set(eyePosition.x - position.x, eyePosition.y - position.y, eyePosition.z - position.z);
            rot.add((float) event.getX() * 0.15F, (float) event.getY() * 0.15F, 0);

            if (!check) {
                modifier.enable()
                        .enablePos()
                        .enableRotation()
                        .enableFirstPersonArmFixed();
                check = true;
                rot.add(player.getXRot(), 0, 0);
                //这里是为了解决进入模式时会闪烁
                modifier.setRotationYXZ(rot);
                modifier.setPos(pos.x, pos.y, pos.z);
            }

            event.setCanceled(true);
        } else if (check) {
            modifier.reset();
            rot.zero();
            pos.zero();
            check = false;
        }
    }
}
