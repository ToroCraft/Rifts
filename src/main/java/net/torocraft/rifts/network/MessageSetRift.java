package net.torocraft.rifts.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.torocraft.rifts.Rifts;
import net.torocraft.rifts.gui.GuiRiftStatus;
import net.torocraft.rifts.save.data.RiftData;

public class MessageSetRift implements IMessage {

  private RiftData data;

  public static void init(int messageId) {
    Rifts.NETWORK.registerMessage(
        MessageSetRift.Handler.class,
        MessageSetRift.class,
        messageId,
        Side.CLIENT);
  }

  public MessageSetRift() {

  }

  public MessageSetRift(RiftData data) {
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

  public static class Handler implements IMessageHandler<MessageSetRift, IMessage> {

    @Override
    public IMessage onMessage(final MessageSetRift message, MessageContext ctx) {
      Minecraft.getMinecraft().addScheduledTask(() -> GuiRiftStatus.data = message.data);
      return null;
    }

  }

}
