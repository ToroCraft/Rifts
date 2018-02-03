package net.torocraft.rifts.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.torocraft.rifts.Rifts;
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
    data = RiftData.fromNBT(ByteBufUtils.readTag(buf));
  }

  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeTag(buf, RiftData.toNBT(data));
  }

  public static class Handler implements IMessageHandler<MessageRiftUpdate, IMessage> {

    @Override
    public IMessage onMessage(final MessageRiftUpdate message, MessageContext ctx) {
      Minecraft.getMinecraft().addScheduledTask(() -> work(message));
      return null;
    }

    private static void work(MessageRiftUpdate message) {
      EntityPlayer player = Minecraft.getMinecraft().player;
      int playerRiftId = RiftUtil.getRiftIdForChunk(player.chunkCoordX, player.chunkCoordZ);

      if (playerRiftId != message.data.riftId) {
        return;
      }

      System.out.println("rift update to client " + RiftData.toNBT(message.data));
      Rifts.currentRift = message.data;
    }
  }

}
