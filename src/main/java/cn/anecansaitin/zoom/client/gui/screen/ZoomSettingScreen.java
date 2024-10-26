package cn.anecansaitin.zoom.client.gui.screen;

import cn.anecansaitin.zoom.client.ZoomClientConfig;
import cn.anecansaitin.zoom.client.ZoomKeyMapping;
import cn.anecansaitin.zoom.client.gui.overlay.ZoomOverlay;
import cn.anecansaitin.zoom.client.listener.FirstPersonPlus;
import cn.anecansaitin.zoom.client.listener.FreeMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ZoomSettingScreen extends Screen {
    private final List<OffsetSettingWidget> widgets = new ArrayList<>();

    public ZoomSettingScreen() {
        super(Component.translatable("gui.setting_screen.title"));
    }

    @Override
    protected void init() {
        widgets.add(new FreeModeOverlaySetting(ZoomOverlay.FREE_MODE_OFFSET_X, ZoomOverlay.FREE_MODE_OFFSET_Y));
        widgets.add(new FPSPlusOverlaySetting(ZoomOverlay.FPS_PLUS_OFFSET_X, ZoomOverlay.FPS_PLUS_OFFSET_Y));
        addRenderableWidget(widgets.get(0));
        addRenderableWidget(widgets.get(1));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        super.onClose();

        for (OffsetSettingWidget widget : widgets) {
            widget.onClose();
        }

        ZoomClientConfig.save();
    }

    private static abstract class OffsetSettingWidget extends AbstractWidget {
        protected double xOffset;
        protected double yOffset;
        protected boolean enable;

        public OffsetSettingWidget(int x, int y, int width, int height, Component message) {
            super(x, y, width, height, message);
            xOffset = x;
            yOffset = y;
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            int x = getX();
            int y = getY();
            int width = getWidth();
            int height = getHeight();
            guiGraphics.vLine(x, y, y + height, 0xffff8c94);
            guiGraphics.vLine(x + width, y, y + height, 0xffff8c94);
            guiGraphics.hLine(x, x + width, y, 0xffff8c94);
            guiGraphics.hLine(x, x + width, y + height, 0xffff8c94);
        }

        @Override
        protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
            xOffset += dragX;
            yOffset += dragY;
            setX((int) xOffset);
            setY((int) yOffset);
        }

        protected void onClose() {
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        }
    }

    private static class FreeModeOverlaySetting extends OffsetSettingWidget {
        private long time;

        public FreeModeOverlaySetting(int x, int y) {
            super(x, y, 20, 20, Component.translatable("gui.setting_screen.free_mode_overlay_setting.title"));
            enable = ZoomOverlay.FREE_MODE_ENABLED;
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

            if (!enable) {
                return;
            }

            int x = getX();
            int y = getY();
            ZoomOverlay.renderFreeMode(guiGraphics, x, y);
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
            long current = System.currentTimeMillis();

            if (current - time < 500) {
                enable = !enable;
                time = 0;
            } else {
                time = current;
            }
        }

        @Override
        protected void onClose() {
            ZoomClientConfig.setFreeModeOverlayEnabled(enable);
            ZoomClientConfig.setFreeModeOverlayOffsets((int) xOffset, (int) yOffset);
        }
    }

    private static class FPSPlusOverlaySetting extends OffsetSettingWidget {
        private long time;

        public FPSPlusOverlaySetting(int x, int y) {
            super(x, y, 20, 20, Component.translatable("gui.setting_screen.fps_plus_mode_overlay_setting.title"));
            enable = ZoomOverlay.FREE_MODE_ENABLED;
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

            if (!enable) {
                return;
            }

            int x = getX();
            int y = getY();
            ZoomOverlay.renderFPSPlus(guiGraphics, x, y);
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
            long current = System.currentTimeMillis();

            if (current - time < 500) {
                enable = !enable;
                time = 0;
            } else {
                time = current;
            }
        }

        @Override
        protected void onClose() {
            ZoomClientConfig.setFpsPlusModeOverlayEnabled(enable);
            ZoomClientConfig.setFpsPlusModeOverlayOffsets((int) xOffset, (int) yOffset);
        }
    }
}
