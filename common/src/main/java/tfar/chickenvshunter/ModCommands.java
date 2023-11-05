package tfar.chickenvshunter;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(ChickenVsHunter.MOD_ID).then(Commands.literal("golden_egg").executes(ModCommands::summonGoldenEgg)));
    }

    private static int summonGoldenEgg(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null) {

            return 1;
        }
        return 0;
    }
}
