package tfar.chickenvshunter.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import tfar.chickenvshunter.ChickenVsHunter;

public class PacketHandler {
    public static final ResourceLocation keybind = new ResourceLocation(ChickenVsHunter.MOD_ID, "keybind");

    public static void registerMessages() {
        ServerPlayNetworking.registerGlobalReceiver(keybind,PacketHandler::receiveKeybind);
    }

    static void receiveKeybind(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int id  = buf.readInt();
        server.execute(() -> ChickenVsHunter.dropChicken(player,id));
    }
}
