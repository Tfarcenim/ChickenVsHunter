package tfar.chickenvshunter;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import tfar.chickenvshunter.world.ChickVHunterSavedData;

import java.util.UUID;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(ChickenVsHunter.MOD_ID)
                .then(Commands.literal("golden_egg").executes(ModCommands::summonGoldenEgg))
                .then(Commands.literal("speedrunner")
                        .executes(ModCommands::assignSpeedrunner)
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(ModCommands::alsoAssignSpeedrunner))
                )
        );
    }

    private static int summonGoldenEgg(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null) {
            ItemStack goldEgg = new ItemStack(Init.GOLDEN_EGG);
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putInt("timer",28 *20);
            goldEgg.getOrCreateTag().put(BlockItem.BLOCK_ENTITY_TAG,compoundTag);
            player.drop(goldEgg,false);
            return 1;
        }
        return 0;
    }

    private static int assignSpeedrunner(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack commandSourceStack = context.getSource();
        ServerPlayer speedrunner = commandSourceStack.getPlayerOrException();
        startGame(speedrunner);
        return 1;
    }
    private static int alsoAssignSpeedrunner(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack commandSourceStack = context.getSource();
        ServerPlayer speedrunner = EntityArgument.getPlayer(context,"player");
        startGame(speedrunner);
        return 1;
    }

    static final UUID modifier_uuid = UUID.fromString("91559870-2417-4bc1-aafc-7abed56178c3");

    public static final AttributeModifier SPEEDRUNNER_BUFF = new AttributeModifier(modifier_uuid,"Speedrunner Buff",16, AttributeModifier.Operation.ADDITION);

    public static void startGame(ServerPlayer speedrunner) {
        ChickVHunterSavedData.speedrunner = speedrunner.getUUID();
        ServerLevel serverLevel = speedrunner.serverLevel();
        Chicken chicken = EntityType.CHICKEN.spawn(serverLevel,speedrunner.blockPosition(), MobSpawnType.COMMAND);
        chicken.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(SPEEDRUNNER_BUFF);
        chicken.setHealth(20);
        chicken.setCustomName(Component.literal("Health: " + (int)chicken.getHealth() +"/" + (int)chicken.getMaxHealth()));
        chicken.setCustomNameVisible(true);
    }
}
