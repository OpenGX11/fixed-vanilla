package net.greenjab.fixedminecraft.mixin.villager;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import net.greenjab.fixedminecraft.CustomData;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.MapColorComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.potion.Potions;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TradedItem;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityMixin {


    @ModifyExpressionValue(method = "fillRecipes", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/village/TradeOffers;WANDERING_TRADER_TRADES:Ljava/util/List;"
    ))
    private List<Pair<TradeOffers.Factory[], Integer>> newTrades(List<Pair<TradeOffers.Factory[], Integer>> original){
        return List.of(
                Pair.of(new TradeOffers.Factory[]{
                        new TradeOffers.BuyItemFactory(createPotion(), 1, 1, 1),
                        new TradeOffers.BuyItemFactory(Items.WATER_BUCKET, 1, 1, 1, 2),
                        new TradeOffers.BuyItemFactory(Items.MILK_BUCKET, 1, 1, 1, 2),
                        new TradeOffers.BuyItemFactory(Items.FERMENTED_SPIDER_EYE, 1, 1, 1, 3),
                        new TradeOffers.BuyItemFactory(Items.BAKED_POTATO, 4, 1, 1),
                        new TradeOffers.BuyItemFactory(Items.HAY_BLOCK, 2, 1, 1),

                        new TradeOffers.BuyItemFactory(Items.GOLDEN_CARROT, 2, 1, 1),
                        new TradeOffers.BuyItemFactory(Items.PUMPKIN_PIE, 2, 1, 1),
                        new TradeOffers.BuyItemFactory(Items.BEETROOT_SOUP, 1, 1, 1),
                        new TradeOffers.BuyItemFactory(Items.COMPASS, 1, 1, 1),
                        new TradeOffers.BuyItemFactory(Items.LEAD, 2, 1, 1),
                        new TradeOffers.BuyItemFactory(Items.COOKIE, 2, 1, 1),
                }, 3+(int)(Math.random()*2)),
                Pair.of(new TradeOffers.Factory[]{
                        new TradeOffers.SellItemFactory(Items.PACKED_ICE, 1, 1, 6, 1),
                        new TradeOffers.SellItemFactory(Items.BLUE_ICE, 6, 1, 6, 1),
                        new TradeOffers.SellItemFactory(Items.GUNPOWDER, 1, 4, 2, 1),
                        new TradeOffers.SellItemFactory(Items.PODZOL, 3, 3, 6, 1),
                        new TradeOffers.SellItemFactory(Blocks.ACACIA_LOG, 1, 8, 4, 1),
                        new TradeOffers.SellItemFactory(Blocks.BIRCH_LOG, 1, 8, 4, 1),
                        new TradeOffers.SellItemFactory(Blocks.DARK_OAK_LOG, 1, 8, 4, 1),
                        new TradeOffers.SellItemFactory(Blocks.JUNGLE_LOG, 1, 8, 4, 1),
                        new TradeOffers.SellItemFactory(Blocks.OAK_LOG, 1, 8, 4, 1),
                        new TradeOffers.SellItemFactory(Blocks.SPRUCE_LOG, 1, 8, 4, 1),
                        new TradeOffers.SellItemFactory(Blocks.CHERRY_LOG, 1, 8, 4, 1),
                        new TradeOffers.SellEnchantedToolFactory(Items.IRON_PICKAXE, 1, 1, 1, 0.2F),
                        new TradeOffers.SellItemFactory(createPotionStack(), 5, 1, 1, 1),
                        new TradeOffers.SellItemFactory(Items.NAUTILUS_SHELL, 5, 1, 5, 1)
                }, 2+(int)(Math.random()*2)),
                Pair.of(new TradeOffers.Factory[]{
                        new TradeOffers.SellItemFactory(Items.TROPICAL_FISH_BUCKET, 3, 1, 4, 1),
                        new TradeOffers.SellItemFactory(Items.PUFFERFISH_BUCKET, 3, 1, 4, 1),
                        new TradeOffers.SellItemFactory(Items.SEA_PICKLE, 2, 1, 5, 1),
                        new TradeOffers.SellItemFactory(Items.SLIME_BALL, 4, 1, 5, 1),
                        new TradeOffers.SellItemFactory(Items.GLOWSTONE, 2, 1, 5, 1),
                        new TradeOffers.SellItemFactory(Items.FERN, 1, 1, 12, 1),
                        new TradeOffers.SellItemFactory(Items.SUGAR_CANE, 1, 1, 8, 1),
                        new TradeOffers.SellItemFactory(Items.PUMPKIN, 1, 1, 4, 1),
                        new TradeOffers.SellItemFactory(Items.KELP, 3, 1, 12, 1),
                        new TradeOffers.SellItemFactory(Items.CACTUS, 3, 1, 8, 1),
                        new TradeOffers.SellItemFactory(Items.DANDELION, 1, 1, 12, 1),
                        new TradeOffers.SellItemFactory(Items.POPPY, 1, 1, 12, 1),
                        new TradeOffers.SellItemFactory(Items.BLUE_ORCHID, 1, 1, 8, 1),
                        new TradeOffers.SellItemFactory(Items.ALLIUM, 1, 1, 12, 1),
                        new TradeOffers.SellItemFactory(Items.AZURE_BLUET, 1, 1, 12, 1),
                        new TradeOffers.SellItemFactory(Items.RED_TULIP, 1, 1, 12, 1),
                        new TradeOffers.SellItemFactory(Items.ORANGE_TULIP, 1, 1, 12, 1),
                        new TradeOffers.SellItemFactory(Items.WHITE_TULIP, 1, 1, 12, 1),
                        new TradeOffers.SellItemFactory(Items.PINK_TULIP, 1, 1, 12, 1),
                        new TradeOffers.SellItemFactory(Items.OXEYE_DAISY, 1, 1, 12, 1),
                        new TradeOffers.SellItemFactory(Items.CORNFLOWER, 1, 1, 12, 1),
                        new TradeOffers.SellItemFactory(Items.LILY_OF_THE_VALLEY, 1, 1, 7, 1),
                        new TradeOffers.SellItemFactory(Items.WHEAT_SEEDS, 1, 1, 12, 1),
                        new TradeOffers.SellItemFactory(Items.BEETROOT_SEEDS, 1, 1, 12, 1),
                        new TradeOffers.SellItemFactory(Items.PUMPKIN_SEEDS, 1, 1, 12, 1),
                        new TradeOffers.SellItemFactory(Items.MELON_SEEDS, 1, 1, 12, 1),
                        new TradeOffers.SellItemFactory(Items.ACACIA_SAPLING, 5, 1, 8, 1),
                        new TradeOffers.SellItemFactory(Items.BIRCH_SAPLING, 5, 1, 8, 1),
                        new TradeOffers.SellItemFactory(Items.DARK_OAK_SAPLING, 5, 1, 8, 1),
                        new TradeOffers.SellItemFactory(Items.JUNGLE_SAPLING, 5, 1, 8, 1),
                        new TradeOffers.SellItemFactory(Items.OAK_SAPLING, 5, 1, 8, 1),
                        new TradeOffers.SellItemFactory(Items.SPRUCE_SAPLING, 5, 1, 8, 1),
                        new TradeOffers.SellItemFactory(Items.CHERRY_SAPLING, 5, 1, 8, 1),
                        new TradeOffers.SellItemFactory(Items.MANGROVE_PROPAGULE, 5, 1, 8, 1),
                        new TradeOffers.SellItemFactory(Items.RED_DYE, 1, 3, 12, 1),
                        new TradeOffers.SellItemFactory(Items.WHITE_DYE, 1, 3, 12, 1),
                        new TradeOffers.SellItemFactory(Items.BLUE_DYE, 1, 3, 12, 1),
                        new TradeOffers.SellItemFactory(Items.PINK_DYE, 1, 3, 12, 1),
                        new TradeOffers.SellItemFactory(Items.BLACK_DYE, 1, 3, 12, 1),
                        new TradeOffers.SellItemFactory(Items.GREEN_DYE, 1, 3, 12, 1),
                        new TradeOffers.SellItemFactory(Items.LIGHT_GRAY_DYE, 1, 3, 12, 1),
                        new TradeOffers.SellItemFactory(Items.MAGENTA_DYE, 1, 3, 12, 1),
                        new TradeOffers.SellItemFactory(Items.YELLOW_DYE, 1, 3, 12, 1),
                        new TradeOffers.SellItemFactory(Items.GRAY_DYE, 1, 3, 12, 1),
                        new TradeOffers.SellItemFactory(Items.PURPLE_DYE, 1, 3, 12, 1),
                        new TradeOffers.SellItemFactory(Items.LIGHT_BLUE_DYE, 1, 3, 12, 1),
                        new TradeOffers.SellItemFactory(Items.LIME_DYE, 1, 3, 12, 1),
                        new TradeOffers.SellItemFactory(Items.ORANGE_DYE, 1, 3, 12, 1),
                        new TradeOffers.SellItemFactory(Items.BROWN_DYE, 1, 3, 12, 1),
                        new TradeOffers.SellItemFactory(Items.CYAN_DYE, 1, 3, 12, 1),
                        new TradeOffers.SellItemFactory(Items.BRAIN_CORAL_BLOCK, 3, 1, 8, 1),
                        new TradeOffers.SellItemFactory(Items.BUBBLE_CORAL_BLOCK, 3, 1, 8, 1),
                        new TradeOffers.SellItemFactory(Items.FIRE_CORAL_BLOCK, 3, 1, 8, 1),
                        new TradeOffers.SellItemFactory(Items.HORN_CORAL_BLOCK, 3, 1, 8, 1),
                        new TradeOffers.SellItemFactory(Items.TUBE_CORAL_BLOCK, 3, 1, 8, 1),
                        new TradeOffers.SellItemFactory(Items.VINE, 1, 3, 4, 1),
                        new TradeOffers.SellItemFactory(Items.BROWN_MUSHROOM, 1, 3, 4, 1),
                        new TradeOffers.SellItemFactory(Items.RED_MUSHROOM, 1, 3, 4, 1),
                        new TradeOffers.SellItemFactory(Items.LILY_PAD, 1, 5, 2, 1),
                        new TradeOffers.SellItemFactory(Items.SMALL_DRIPLEAF, 1, 2, 5, 1),
                        new TradeOffers.SellItemFactory(Items.SAND, 1, 8, 8, 1),
                        new TradeOffers.SellItemFactory(Items.RED_SAND, 1, 4, 6, 1),
                        new TradeOffers.SellItemFactory(Items.POINTED_DRIPSTONE, 1, 2, 5, 1),
                        new TradeOffers.SellItemFactory(Items.ROOTED_DIRT, 1, 2, 5, 1),
                        new TradeOffers.SellItemFactory(Items.MOSS_BLOCK, 1, 2, 5, 1)
                }, 2+(int)(Math.random()*2)),
                Pair.of(new TradeOffers.Factory[]{
                        new TradeOffers.SellItemFactory(createSpecialItem(), 10, 1, 1, 1)
                }, 1)

        );
    }


    @Unique
    private ItemStack createSpecialItem() {
        int i = (int)(Math.random()*5);
        return switch (i) {
            case 0 -> createMusicDiscStack();
            case 3 -> createMobHeadStack();
            default -> createBiomeMapStack();
        };
    }

    @Unique
    private TradedItem createPotion() {
        return new TradedItem(Items.POTION)
                .withComponents( builder -> builder.add(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Potions.WATER)));
    }

    @Unique
    private ItemStack createPotionStack() {
        return PotionContentsComponent.createStack(Items.POTION, Potions.LONG_INVISIBILITY);
    }

    @Unique
    private ItemStack createMusicDiscStack() {
        Item[] discs = {Items.MUSIC_DISC_13, Items.MUSIC_DISC_CAT};
        return discs[(int)(Math.random()*discs.length)].getDefaultStack();
    }

    @Unique
    private ItemStack createMobHeadStack() {
        Item[] heads = {Items.PLAYER_HEAD};
        ItemStack head = heads[(int)(Math.random()*heads.length)].getDefaultStack();
        if (head.isOf(Items.PLAYER_HEAD)) {

            int who = (int)(Math.random()*2);
            switch (who) {
                case 0:
                    //mod maker
                    head.set(DataComponentTypes.PROFILE, new ProfileComponent(Optional.of("green_jab"), Optional.empty(), new PropertyMap()));
                    break;
                case 1:
                    //patreon
                    String[] names = {"Rellati"};
                    head.set(DataComponentTypes.PROFILE, new ProfileComponent(Optional.of(names[(int)(Math.random()*names.length)]), Optional.empty(), new PropertyMap()));
                    break;
                default:
                    //discontinued
                    WanderingTraderEntity WTE = (WanderingTraderEntity)(Object)this;
                    PlayerEntity playerEntity = WTE.getWorld().getClosestPlayer(WTE, 100);
                    if (playerEntity != null) {
                        head.set(DataComponentTypes.PROFILE, new ProfileComponent(playerEntity.getGameProfile()));
                    }
            }

        }
        return head;
    }

    @Unique
    private ItemStack createBiomeMapStack() {
        WanderingTraderEntity WTE = (WanderingTraderEntity)(Object)this;
            List<RegistryKey<Biome>> biomes = List.of();
            Set<RegistryKey<Biome>> var10001 = Set.copyOf(biomes);
            Objects.requireNonNull(var10001);
            Predicate<RegistryEntry<Biome>> predicate = var10001::contains;

            World world = WTE.getWorld();
            if (world instanceof ServerWorld serverWorld) {

                int map = serverWorld.random.nextInt(6);
                switch (map) {
                    case 0:
                        CustomData.biomeSearch = BiomeKeys.MUSHROOM_FIELDS;
                        break;
                    case 1:
                        CustomData.biomeSearch = BiomeKeys.CHERRY_GROVE;
                        break;
                    case 2:
                        CustomData.biomeSearch = BiomeKeys.ICE_SPIKES;
                        break;
                    case 3:
                        CustomData.biomeSearch = BiomeKeys.BADLANDS;
                        break;
                    case 4:
                        CustomData.biomeSearch = BiomeKeys.WARM_OCEAN;
                        break;
                    case 5:
                        CustomData.biomeSearch = BiomeKeys.PALE_GARDEN;
                        break;
                    default:
                        CustomData.biomeSearch = BiomeKeys.FOREST;
                        break;
                }

                com.mojang.datafixers.util.Pair<BlockPos, RegistryEntry<Biome>> pair = serverWorld.locateBiome(predicate.negate(), WTE.getBlockPos(), 6400, 32, 64);
                if (pair != null) {
                    BlockPos blockPos = pair.getFirst();
                    ItemStack itemStack = FilledMapItem.createMap(serverWorld, blockPos.getX(), blockPos.getZ(), (byte) 2, true, true);
                    FilledMapItem.fillExplorationMap(serverWorld, itemStack);

                    String[] names = {"mushroom_fields", "cherry_grove", "ice_spikes", "badlands", "warm_ocean", "pale_garden"};
                    Text name = Text.translatable("filled_map.explorer", Text.translatable("biome.minecraft." + names[map]));
                    int[] colour = {7412448, 16751570, 4639231, 16725801, 1938431, 10856879};

                    itemStack.set(DataComponentTypes.ITEM_NAME, name);
                    MapState m = FilledMapItem.getMapState(itemStack, serverWorld);
                    assert m != null;
                    m.addBanner(serverWorld, new BlockPos(blockPos.getX(), -1000 - map, blockPos.getZ()));
                    itemStack.set(DataComponentTypes.MAP_COLOR, new MapColorComponent(colour[map]));

                    return itemStack;
                }
            }
        return Items.MAP.getDefaultStack();
    }

}
