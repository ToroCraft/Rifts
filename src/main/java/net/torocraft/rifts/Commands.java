package net.torocraft.rifts;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.torocraft.rifts.blocks.BlockRiftPortal;
import net.torocraft.rifts.dim.DimensionUtil;
import net.torocraft.rifts.dim.GuardianSpawner;
import net.torocraft.rifts.world.RiftUtil;

public class Commands extends CommandBase {

  private static final UUID TEST_ID = UUID.fromString("2027e16a-6edd-11e7-907b-a6006ad3dba0");

  @Override
  @Nonnull
  public String getName() {
    return "rifts";
  }

  @Override
  @Nonnull
  public String getUsage(@Nullable ICommandSender sender) {
    return "commands.nemesis_system.usage";
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 2;
  }

  @Override
  public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender,
      @Nonnull String[] args) throws CommandException {

    if (args.length < 1) {
      throw new WrongUsageException("commands.rifts.usage");
    }

    String command = args[0];

    switch (command) {
      case "enter":
        enter(server, sender, args);
        return;
      case "leave":
        leave(server, sender, args);
        return;
      case "guardian":
        guardian(server, sender, args);
        return;
      case "create_portal":
        createPortal(server, sender, args);
        return;
      default:
        throw new WrongUsageException("commands.rifts.command_not_found");
    }
  }

  private void guardian(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (!(sender instanceof EntityPlayer)) {
      return;
    }

    EntityPlayerMP player = getCommandSenderAsPlayer(sender);
    GuardianSpawner.spawnRiftGuardianAroundPlayer(player);
  }


  private void enter(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (!(sender instanceof EntityPlayer)) {
      return;
    }

    if (args.length != 2) {
      throw new WrongUsageException("commands.rifts.rift_id_required");
    }

    EntityPlayerMP player = getCommandSenderAsPlayer(sender);
    int riftId = i(args[1]);
    DimensionUtil.travelToRift(player, riftId);
  }

  private void leave(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (!(sender instanceof EntityPlayer)) {
      return;
    }
    EntityPlayerMP player = getCommandSenderAsPlayer(sender);
    int riftId = RiftUtil.getRiftIdForChunk(player.chunkCoordX, player.chunkCoordZ);
    DimensionUtil.travelToOverworld(player, riftId);
  }

  private void createPortal(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (!(sender instanceof EntityPlayer)) {
      return;
    }
    EntityPlayerMP player = getCommandSenderAsPlayer(sender);

    BlockPos pos = player.getPosition().north().north().up();

    server.getWorld(player.dimension)
        .setBlockState(pos, BlockRiftPortal.INSTANCE.getDefaultState());

  }

  private int senderDimId(ICommandSender sender) {
    try {
      return getCommandSenderAsPlayer(sender).dimension;
    } catch (Exception e) {
      return 0;
    }
  }

  private int i(String s) {
    try {
      return Integer.parseInt(s);
    } catch (Exception e) {
      return 1;
    }
  }

  @Override
  @Nonnull
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender,
      String[] args, @Nullable BlockPos targetPos) {
    if (args.length == 1) {
      return getListOfStringsMatchingLastWord(args, "enter", "leave", "create_portal", "guardian");
    }
    String command = args[0];
    switch (command) {
      case "enter":
        return tabCompletionsForEnter(args);
      default:
        return Collections.emptyList();
    }
  }

  private List<String> tabCompletionsForEnter(String[] args) {
    if (args.length == 2) {
      return getListOfStringsMatchingLastWord(args, numbers());
    }
    return Collections.emptyList();
  }

  private String[] numbers() {
    String[] numbers = new String[10];
    for (int i = 0; i < 10; i++) {
      numbers[i] = Integer.toString(i, 10);
    }
    return numbers;
  }
}
