package tfar.chickenvshunter;

import tfar.chickenvshunter.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}

//- todo i can hold chicken above my head to move chicken around with me
//	todo - cannot do anything with my hands at this point, like attack or mine etc...
//
//- todo chicken shows its HP out of 20 over its head for everyone to see
//
//- todo chicken starts to slowly regain health after 3 seconds of not being hit
//
//- hunters and I both have a compass to track chicken
//
//- can shoot eggs that when thrown it tps me to egg location even when in mid air if i click to "throw" the egg again
//	- if it lands on block before i click again it just acts like an ender pearl but rather then taking damage we appear in a poof of feathers and dont take damage
//	- keep an egg item in my inv for this ability that i throw, can only be used when chicken is held above me
//	- dont need egg to show in my hand egg should shoot from the chicken above my head
//	- egg should have ender pearl physics when thrown/ shot
//
//- todo random chance every 2 minutes that it will poop 1 chicken when im holding it that will fight hunters for us, and follow us when we are moving around (chickens that will act like wolves basically and have same HP as main chicken so 20hp)
//
//- todo chicken can talk to other nearby chickens and make other chickens attack the hunters (just normal chickens with normal health that become part of our chicken army when we get within 10 blocks of em)
//
//- todo when holding chicken I can glide
//
// todo - feed the chicken OP seeds:
//	- OP seeds would be seeds surrounding a type of ore (gold, iron, diamond, netherite)
//
//		- gold seed: gives chicken axe (durability like diamond, damage as iron)
//			- turns the hunters into chicken sized versions of them selves temporarily
//			- reducing their reach and speed in scale to their size
//
//		- iron seed: gives chicken pick (durability like diamond, mining speed of iron)
//			- sends a swarm of chicken to mine out in what ever direction i right click for 10 blocks
//			- sends me all ores and materials mined
//
//		- diamond seed: gives chicken bow (bow with unbreaking 3 durability)
//			- launches chicken eggs that when landed 3 chickens appear that murder everything in site then dissapear after 5 seconds
//			- very small chance any one of the chickens explodes when it disapears
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
// todo then all the damage goes to me since chicken is on 1 heart and immune now once i am dead and not wearing chest piece chicken is no longer immune and can be killed
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
//	- when the egg is used a giant chicken ghast thing is summoned called the ghacken (can use ghast as base ill have a similar model remade or a retexture made to make it a combination of ghast and chicken)
//		- it shoots giant chicken eggs (ghast fireballs) at hunters that also have a chance of landing and summoning another ghacken
//		- it strikes lightning down randomly all around it
//		- it summons chicken warriors that rain down from the sky that seek out hunters to kill (just like the chicken wolf things from earlier)