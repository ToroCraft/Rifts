package net.torocraft.rifts.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.rifts.Rifts;

public class GuiRiftStatus extends Gui {

  private static final ResourceLocation GUI_BARS_TEXTURES =
      new ResourceLocation(Rifts.MODID, "textures/gui/bars.png");

  private static final int BAR_WIDTH = 92;
  private final Minecraft mc = Minecraft.getMinecraft();

  private int offsetX;
  private int offsetY;

  public GuiRiftStatus() {

  }

  @SubscribeEvent
  public void drawHealthBar(RenderGameOverlayEvent.Pre event) {
    if (mc.player.dimension != Rifts.RIFT_DIM_ID || event.getType() != ElementType.CHAT) {
      return;
    }
    draw();
  }

  enum Color {
    PINK, BLUE, RED, GREEN, YELLOW, PURPLE, WHITE
  }

  private void draw() {

    if (Rifts.currentRift == null) {
      return;
    }

    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    mc.getTextureManager().bindTexture(GUI_BARS_TEXTURES);

    renderBar(0, Color.GREEN, Rifts.currentRift.progress());
    renderBar(5, Color.RED, Rifts.currentRift.time());
  }

  private void renderBar(int y, Color color, float progress) {
    if (progress < 0 || progress > 1) {
      return;
    }
    drawTexturedModalRect(offsetX, y + offsetY, 0, color.ordinal() * 5 * 2, BAR_WIDTH, 5);
    int progressWidth = (int) (progress * BAR_WIDTH);
    if (progressWidth > 0) {
      drawTexturedModalRect(offsetX, y + offsetY, 0, color.ordinal() * 5 * 2 + 5, progressWidth, 5);
    }
  }
}
