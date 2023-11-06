package tfar.chickenvshunter;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import tfar.chickenvshunter.world.ChickVHunterSavedData;

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

    public static void startGame(ServerPlayer speedrunner) {
        ChickVHunterSavedData.speedrunner = speedrunner.getUUID();
    }

}
