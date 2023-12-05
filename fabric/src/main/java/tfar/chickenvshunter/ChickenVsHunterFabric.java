package tfar.chickenvshunter;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import tfar.chickenvshunter.entity.GhickenEntity;
import tfar.chickenvshunter.entity.GhickenFireballEntity;
import tfar.chickenvshunter.network.PacketHandler;

public class ChickenVsHunterFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        ChickenVsHunter.init();
        initializeItems();
        register();
        CommandRegistrationCallback.EVENT.register(this::commands);
    //    UseEntityCallback.EVENT.register(this::rightClickChicken);
        AttackEntityCallback.EVENT.register(this::attackEntity);
        PlayerBlockBreakEvents.BEFORE.register(this::blockBreak);
        ServerTickEvents.START_WORLD_TICK.register(this::tickLevel);
        UseBlockCallback.EVENT.register(this::useBlock);

        FabricDefaultAttributeRegistry.register(Init.GHICKEN,GhickenEntity.createAttributes());


        PacketHandler.registerMessages();
    }


    private InteractionResult useBlock(Player player, Level level, InteractionHand hand, BlockHitResult blockHitResult) {
        Entity passenger = player.getFirstPassenger();
        if (passenger instanceof Chicken && !player.getItemBySlot(EquipmentSlot.HEAD).is(Init.CHICKEN_HELMET)) {
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }

    private void tickLevel(ServerLevel serverLevel) {
        ChickenVsHunter.worldTick(serverLevel);
    }

    private boolean blockBreak(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        return !ChickenVsHunter.holdingChicken(player);
    }


    private InteractionResult attackEntity(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {
        return ChickenVsHunter.onPlayerAttack(player,level,hand,entity,entityHitResult);
    }

    private InteractionResult rightClickChicken(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {
        if (entity instanceof Chicken chicken && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            chicken.startRiding(player);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }


    private void commands(CommandDispatcher<CommandSourceStack> commandSourceStackCommandDispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
        ModCommands.register(commandSourceStackCommandDispatcher);
    }

    public void register() {
        registerItem("chicken_helmet", Init.CHICKEN_HELMET);
        registerItem("chicken_chestplate", Init.CHICKEN_CHESTPLATE);
        registerItem("chicken_leggings", Init.CHICKEN_LEGGINGS);
        registerItem("chicken_boots", Init.CHICKEN_BOOTS);

        Registry.register(BuiltInRegistries.BLOCK,new ResourceLocation(ChickenVsHunter.MOD_ID,"golden_egg"),Init.GOLDEN_EGG);
        registerItem("golden_egg",Init.GOLDEN_EGG_I);

        registerItem("chicken_axe",Init.CHICKEN_AXE);
        registerItem("chicken_bow",Init.CHICKEN_BOW);
        registerItem("chicken_pickaxe",Init.CHICKEN_PICKAXE);

        registerItem("iron_seeds",Init.IRON_SEEDS);
        registerItem("gold_seeds",Init.GOLD_SEEDS);
        registerItem("diamond_seeds",Init.DIAMOND_SEEDS);
        registerItem("netherite_seeds",Init.NETHERITE_SEEDS);
        registerItem("chicken_compass",Init.CHICKEN_COMPASS);
        registerItem("ender_egg",Init.ENDER_EGG);

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,new ResourceLocation(ChickenVsHunter.MOD_ID,"creative_tab"),Init.creativeModeTab);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,new ResourceLocation(ChickenVsHunter.MOD_ID,"golden_egg"),Init.GOLDEN_EGG_E);
        Registry.register(BuiltInRegistries.ENTITY_TYPE,new ResourceLocation(ChickenVsHunter.MOD_ID,"ghicken"),Init.GHICKEN);
        Registry.register(BuiltInRegistries.ENTITY_TYPE,new ResourceLocation(ChickenVsHunter.MOD_ID,"ghicken_fireball"),Init.GHICKEN_FIREBALL);
        Registry.register(BuiltInRegistries.ENTITY_TYPE,new ResourceLocation(ChickenVsHunter.MOD_ID,"chicken_arrow"),Init.CHICKEN_ARROW);
        Registry.register(BuiltInRegistries.ENTITY_TYPE,new ResourceLocation(ChickenVsHunter.MOD_ID,"block_breaker"),Init.BLOCK_BREAKER);
        Registry.register(BuiltInRegistries.MOB_EFFECT,new ResourceLocation(ChickenVsHunter.MOD_ID,"chicken_curse"),Init.CHICKEN_CURSE);

    }

    public static void registerItem(String name, Item item) {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(ChickenVsHunter.MOD_ID, name), item);
    }

    public void initializeItems() {
        Init.CHICKEN_HELMET = new ChickenArmorItem(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, new Item.Properties());
        Init.CHICKEN_CHESTPLATE = new ChickenArmorItem(ArmorMaterials.NETHERITE, ArmorItem.Type.CHESTPLATE, new Item.Properties());
        Init.CHICKEN_LEGGINGS = new ChickenArmorItem(ArmorMaterials.NETHERITE, ArmorItem.Type.LEGGINGS, new Item.Properties());
        Init.CHICKEN_BOOTS = new ChickenArmorItem(ArmorMaterials.NETHERITE, ArmorItem.Type.BOOTS, new Item.Properties());
        Init.GHICKEN = EntityType.Builder.of(GhickenEntity::new, MobCategory.CREATURE).sized(2,1).build("ghicken");
    }
}
