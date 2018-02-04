package net.torocraft.rifts.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.torocraft.rifts.Rifts;
import net.torocraft.rifts.gui.GuiRiftStatus;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.rifts.world.RiftUtil;

public class MessageRiftUpdate implements IMessage {

  private RiftData data;

  public static void init(int messageId) {
    Rifts.NETWORK.registerMessage(
        MessageRiftUpdate.Handler.class,
        MessageRiftUpdate.class,
        messageId,
        Side.CLIENT);
  }

  public MessageRiftUpdate() {

  }

  public MessageRiftUpdate(RiftData data) {
    this.data = data;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    boolean notNull = buf.readBoolean();
    if (notNull) {
      data = RiftData.fromNBT(ByteBufUtils.readTag(buf));
    } else {
      data = null;
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeBoolean(data != null);
    if (data != null) {
      ByteBufUtils.writeTag(buf, RiftData.toNBT(data));
    }
  }

  public static class Handler implements IMessageHandler<MessageRiftUpdate, IMessage> {

    @Override
    public IMessage onMessage(final MessageRiftUpdate message, MessageContext ctx) {
      Minecraft.getMinecraft().addScheduledTask(() -> work(message));
      return null;
    }

    private static void work(MessageRiftUpdate message) {
      if (message.data == null) {
        GuiRiftStatus.data = null;
        return;
      }
      EntityPlayer player = Minecraft.getMinecraft().player;
      int playerRiftId = RiftUtil.getRiftIdForChunk(player.chunkCoordX, player.chunkCoordZ);
      if (playerRiftId != message.data.riftId) {
        return;
      }
      GuiRiftStatus.data = message.data;
    }
  }

}
