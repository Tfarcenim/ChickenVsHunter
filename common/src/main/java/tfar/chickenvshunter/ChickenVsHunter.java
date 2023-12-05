package tfar.chickenvshunter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.chickenvshunter.ducks.ChickenDuck;
import tfar.chickenvshunter.world.ChickVHunterSavedData;
import tfar.chickenvshunter.world.deferredevent.LevelDeferredEvent;
import tfar.chickenvshunter.world.deferredevent.LevelDeferredEventSystem;

import java.util.List;
import java.util.function.Predicate;

import static tfar.chickenvshunter.world.ChickVHunterSavedData.chicken;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class ChickenVsHunter {

    public static final String MOD_ID = "chickenvshunter";
    public static final String MOD_NAME = "ChickenVsHunter";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {

        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.

    }

    public static LevelDeferredEventSystem getDeferredEventSystem(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .computeIfAbsent(LevelDeferredEventSystem::loadStatic, LevelDeferredEventSystem::new, "deferredevents");
    }

    public static void addDeferredEvent(ServerLevel level, LevelDeferredEvent event) {
        getDeferredEventSystem(level).addDeferredEvent(event);
    }

    //return true to cancel fall damage
    public static boolean onFallDamage(LivingEntity livingEntity) {
        return livingEntity.getItemBySlot(EquipmentSlot.FEET).is(Init.CHICKEN_BOOTS);
    }

    public static InteractionResult onPlayerAttack(Player player, Level level, InteractionHand hand, Entity target, @Nullable EntityHitResult entityHitResult) {
        if (player.getFirstPassenger() instanceof Chicken chicken) {
            if  (chicken == target) {
                return InteractionResult.FAIL;
            }
            if (!player.getItemBySlot(EquipmentSlot.HEAD).is(Init.CHICKEN_HELMET)) {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.PASS;
    }

    public static void playerClone(ServerPlayer newPlayer, ServerPlayer oldPlayer, boolean keepEverything) {
        if (oldPlayer.hasEffect(Init.CHICKEN_CURSE)) {
            newPlayer.addEffect(new MobEffectInstance(Init.CHICKEN_CURSE,-1,0));
        }
    }



    public static void chickenTick(Chicken chicken) {
        if (!chicken.level().isClientSide) {
            ChickenDuck chickenDuck = (ChickenDuck) chicken;
            if (chicken.getUUID().equals(ChickVHunterSavedData.chicken)) {
                chicken.setCustomName(Component.literal("Health: " + (int)chicken.getHealth() +"/" + (int)chicken.getMaxHealth()));
            }

            if (chicken.getVehicle() instanceof Player player) {
                boolean chickenHelmet = player.getItemBySlot(EquipmentSlot.HEAD).is(Init.CHICKEN_HELMET);
                if (!chickenHelmet) {
                    //slowly heal chicken if time has passed
                    int timeSinceHit = chicken.tickCount - chicken.getLastHurtMobTimestamp();
                    if (timeSinceHit > 20 * 3) {
                        chicken.heal(.05f);//this runs 20 times a second
                    }
                }
                int reinfecementTime  = chickenDuck.getReinforcementTime();
                chickenDuck.setReinforcementTime(reinfecementTime - 1);
                if (reinfecementTime <=0) {
                    summonChicken((ServerPlayer) player);
                    chickenDuck.setReinforcementTime(ChickVHunterSavedData.REINFORCEMENT_DELAY);
                }
            }

            if (chickenDuck.getBlocksLeft() > 0) {
                for (int y = -1; y < 2;y++ ) {
                    for (int z = -1; z < 2;z++ ) {
                        for (int x = -1; x < 2;x++ ) {
                            Level level = chicken.level();
                            BlockPos pos = chicken.blockPosition().offset(x,y,z);
                            BlockState blockState = chicken.level().getBlockState(pos);
                            if (!blockState.isAir()) {
                                chickenDuck.setBlocksLeft(chickenDuck.getBlocksLeft() - 1);

                                LivingEntity owner = (LivingEntity) ((ServerLevel)level).getEntity(ChickVHunterSavedData.speedrunner);
                                if (owner instanceof ServerPlayer) {
                                    List<ItemStack> loot = Block.getDrops(blockState, (ServerLevel) level, pos, level.getBlockEntity(pos), owner,
                                            owner.getItemInHand(InteractionHand.MAIN_HAND));
                                    loot.forEach(stack -> ((ServerPlayer) owner).getInventory().add(stack));
                                    level.removeBlock(pos,false);
                                }
                                if (chickenDuck.getBlocksLeft() <=0) {
                                    chicken.setNoGravity(false);
                                    chicken.setDiscardFriction(false);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (chicken.getUUID().equals(ChickVHunterSavedData.chicken)) {
            Init.CHICKEN_COMPASS.pos = GlobalPos.of(chicken.level().dimension(),chicken.blockPosition());
        }
    }

    public static void summonChicken(ServerPlayer player) {
        ServerLevel serverLevel = player.serverLevel();
        Chicken chicken = EntityType.CHICKEN.spawn(serverLevel,player.blockPosition(), MobSpawnType.COMMAND);
        ((ChickenDuck)chicken).setOwnerUUID(player.getUUID());
        chicken.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(ModCommands.SPEEDRUNNER_BUFF);
        chicken.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier("Agressive",.25, AttributeModifier.Operation.ADDITION));
        chicken.setHealth(20);
    }

    public static boolean rageMode(ServerLevel serverLevel) {
        Entity entity = serverLevel.getEntity(ChickVHunterSavedData.chicken);
        if (entity instanceof Chicken chicken) {
        return chicken.getHealth() < 2;
        }
        return false;
    }

    public static void worldTick(Level level) {
        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;
            getDeferredEventSystem(serverLevel).tickDeferredEvents(serverLevel);
        }
    }

    public static boolean holdingChicken(Player player) {
        return player.getFirstPassenger() instanceof Chicken && !player.getItemBySlot(EquipmentSlot.HEAD).is(Init.CHICKEN_HELMET);
    }

    public static void dropChicken(ServerPlayer player,int id) {
        if (player.getFirstPassenger() instanceof Chicken chicken) {
            chicken.stopRiding();
        } else {
            Entity entity = player.level().getEntity(id);
            if (entity instanceof Chicken chicken) {
                chicken.startRiding(player);
            }
        }
    }

    static final Predicate<Entity> PREDICATE = entity -> !entity.isSpectator() && entity.isPickable();

    public static void playerTick(Player player) {
        if (!player.level().isClientSide) {
            if (player.getFirstPassenger() instanceof Chicken) {
                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 2, 0,false,false));
            }
        }
    }

    public static boolean onDamaged(LivingEntity livingEntity, float damageAmount, DamageSource damageSource) {
        //attempting to attack chicken
        if (livingEntity instanceof Chicken chicken) {
            Entity vehicle = chicken.getVehicle();
            if (vehicle instanceof Player player) {

                boolean chestplate = player.getItemBySlot(EquipmentSlot.CHEST).is(Init.CHICKEN_CHESTPLATE);
                //chicken is not in critical condition
                if (chicken.getHealth() > 2 && damageAmount < chicken.getHealth()) {

                } else {
                    //hurt the player instead, chestplate makes chicken take no damage
                    if (chestplate) {
                        player.hurt(damageSource, damageAmount);
                        return true;
                    }
                }
            }
        } else if (livingEntity instanceof Player player) {
            Entity passenger = player.getFirstPassenger();
            if (passenger instanceof Chicken chicken) {
                boolean helmet = player.getItemBySlot(EquipmentSlot.HEAD).is(Init.CHICKEN_HELMET);
                if (helmet) {
                    //chicken takes damage first unless it's critical
                    if (chicken.getHealth() > 2 && damageAmount < chicken.getHealth()) {
                        chicken.hurt(damageSource,damageAmount);
                        return true;
                    }
                }
                summonChicken((ServerPlayer) player);
            }
        }

        if (damageSource.getEntity() instanceof  Chicken chicken && rageMode((ServerLevel) livingEntity.level())) {
            livingEntity.setSecondsOnFire(2);
            livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION,200,0));
            livingEntity.addEffect(new MobEffectInstance(Init.CHICKEN_CURSE,-1,0));
        }

        return false;
    }

    public static boolean onHeal(LivingEntity entity, float healAmount) {
        if (entity instanceof Player player && player.getFirstPassenger() instanceof Chicken chicken) {
            //chicken heals first
            boolean helmet = player.getItemBySlot(EquipmentSlot.HEAD).is(Init.CHICKEN_HELMET);
            if (helmet) {
                float healRemaining = chicken.getMaxHealth() - chicken.getHealth();
                if (healRemaining > 0) {
                    float chickenHeal = Math.min(healRemaining,healAmount);
                    chicken.heal(chickenHeal);
                    float leftover = healAmount - chickenHeal;
                    if (leftover > 0) {
                        player.heal(leftover);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    //return true to cancel
    public static boolean attackEvent(LivingEntity livingEntity, DamageSource source) {
        if (livingEntity instanceof Chicken chicken ) {
            return ((ChickenDuck) chicken).getBlocksLeft() > 0 && source.is(DamageTypes.IN_WALL);
        }
        return false;
    }
}

//- i can hold chicken above my head to move chicken around with me
//	- cannot do anything with my hands at this point, like attack or mine etc...
//
//- chicken shows its HP out of 20 over its head for everyone to see
//
//- chicken starts to slowly regain health after 3 seconds of not being hit
//
//- hunters and I both have a compass to track chicken
//
//- can shoot eggs that when thrown it tps me to egg location even when in mid air if i click to "throw" the egg again
//	- if it lands on block before i click again it just acts like an ender pearl but rather then taking damage we appear in a poof of feathers and dont take damage
//	- keep an egg item in my inv for this ability that i throw, can only be used when chicken is held above me
//	- dont need egg to show in my hand egg should shoot from the chicken above my head
//	- egg should have ender pearl physics when thrown/ shot
//
// random chance every 2 minutes that it will poop 1 chicken when im holding it that will fight hunters for us, and follow us when we are moving around (chickens that will act like wolves basically and have same HP as main chicken so 20hp)
//
//- todo chicken can talk to other nearby chickens and make other chickens attack the hunters (just normal chickens with normal health that become part of our chicken army when we get within 10 blocks of em)
//
//- when holding chicken I can glide
//
// todo - feed the chicken OP seeds:
//
//  - OP seeds would be seeds surrounding a type of ore (gold, iron, diamond, netherite)
//
//		- gold seed: gives chicken axe (durability like diamond, damage as iron)
//			- turns the hunters into chicken sized versions of them selves temporarily
//			- reducing their reach and speed in scale to their size
//
//		- iron seed: gives chicken pick (durability like diamond, mining speed of iron)
//		- sends a swarm of chicken to mine out in what ever direction i right click for 10 blocks
//			- sends me all ores and materials mined
//
//		- diamond seed: gives chicken bow (bow with unbreaking 3 durability)
//	 - launches chicken eggs that when landed 3 chickens appear that murder everything in site then dissapear after 5 seconds
//
//	- very small chance any one of the chickens explodes when it disapears
//
//		- netherite seed: gives full set of chicken armour (netherite tiered armour thats chicken themed)
//			- boots give:
//				- light as a feather effect, no fall damage
//
//			- helmet gives:
//				- chicken coop effect, chicken now live in nest on my head and i no longer lose ability to do things with my hands
//				- but chicken will take all damage first before me (if hunter hits me damage goes directly to chicken first)
//				- chicken now only heals when i eat and restore sturation, aka its health is essentially linked to my hunter/ health levels even if i drop the chicken after this point.
//
//			- chest piece gives:
//				- chicken heart effect, in order to kill the chicken you must be slain as well
//				- so if the chicken is down to 1 heart it is actually immune unless I am dead and no longer wearing the chicken chest piece
//				- to clarify how this works in conjunction with helmet, chicken takes all damage first it gets onto one heart
// then all the damage goes to me since chicken is on 1 heart and immune now once i am dead and not wearing chest piece chicken is no longer immune and can be killed
//
//			- leggings give:
//				- chicken legs effect, 25% chance to evade all melee and arrow damage
//				- speed effect (no particles or speed icon in top right corner)
//
//- When the chicken reaches one heart it goes rage mode.
//	todo - rage mode:
//		- if i drop the chicken at this point it runs around attacking hunters itself
//			- when attacking everytime it hits the hunters they have chance of being set on fire
//			- it has random chance to give nausea when attacking the hunters
//			- if hit by the chicken the hunter who was hit will get curse of the raging chicken given to them and have them dropping the items they are holding randomly and hear a chicken bawk sound and also get blindness for the rest of the game even after death. (maybe once every 3 - 5 mins)
//			- chicken has speed 2
//
//- command that give me a golden egg in my inv (looks like a dragon egg, that says it will be ready for summoning in 10 mins and will summon a chicken god to help in our conquest for victory)
// todo	- when the egg is used a giant chicken ghast thing is summoned called the ghacken (can use ghast as base ill have a similar model remade or a retexture made to make it a combination of ghast and chicken)
//
//	todo	- it shoots giant chicken eggs (ghast fireballs) at hunters that also have a chance of landing and summoning another ghacken
//
//	todo	- it strikes lightning down randomly all around it
//
//	todo	- it summons chicken warriors that rain down from the sky that seek out hunters to kill (just like the chicken wolf things from earlier)