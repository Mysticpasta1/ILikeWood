package yamahari.ilikewood.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import yamahari.ilikewood.client.tileentity.renderer.WoodenChestItemStackTileEntityRenderer;
import yamahari.ilikewood.item.WoodenBlockItem;
import yamahari.ilikewood.item.WoodenItem;
import yamahari.ilikewood.item.WoodenScaffoldingItem;
import yamahari.ilikewood.item.WoodenWallOrFloorItem;
import yamahari.ilikewood.item.tiered.WoodenHoeItem;
import yamahari.ilikewood.item.tiered.WoodenSwordItem;
import yamahari.ilikewood.item.tiered.tool.WoodenAxeItem;
import yamahari.ilikewood.item.tiered.tool.WoodenPickAxeItem;
import yamahari.ilikewood.item.tiered.tool.WoodenShovelItem;
import yamahari.ilikewood.util.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public final class WoodenItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
    private static final Map<WoodenObjectType, Map<WoodType, RegistryObject<Item>>> REGISTRY_OBJECTS;
    private static final Map<WoodenTieredObjectType, Map<WoodType, Map<WoodenItemTier, RegistryObject<Item>>>> TIERED_REGISTRY_OBJECTS;

    static {
        final Map<WoodenObjectType, Map<WoodType, RegistryObject<Item>>> registryObjects = new EnumMap<>(WoodenObjectType.class);

        final BiFunction<WoodenObjectType, RegistryObject<Block>, Item> simpleBuildingBlockItem = registerSimpleBlockItem((new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS));
        final BiFunction<WoodenObjectType, RegistryObject<Block>, Item> simpleDecorationBlockItem = registerSimpleBlockItem((new Item.Properties()).group(ItemGroup.DECORATIONS));
        final BiFunction<WoodenObjectType, RegistryObject<Block>, Item> simpleMiscBlockItem = registerSimpleBlockItem((new Item.Properties()).group(ItemGroup.MISC));
        final BiFunction<WoodenObjectType, RegistryObject<Block>, Item> simpleRedstoneBlockItem = registerSimpleBlockItem((new Item.Properties()).group(ItemGroup.REDSTONE));

        registryObjects.put(WoodenObjectType.PANELS, registerBlockItems(WoodenObjectType.PANELS, simpleBuildingBlockItem));
        registryObjects.put(WoodenObjectType.STAIRS, registerBlockItems(WoodenObjectType.STAIRS, simpleBuildingBlockItem));
        registryObjects.put(WoodenObjectType.SLAB, registerBlockItems(WoodenObjectType.SLAB, simpleBuildingBlockItem));
        registryObjects.put(WoodenObjectType.BARREL, registerBlockItems(WoodenObjectType.BARREL, simpleDecorationBlockItem));
        registryObjects.put(WoodenObjectType.BOOKSHELF, registerBlockItems(WoodenObjectType.BOOKSHELF, simpleBuildingBlockItem));
        registryObjects.put(WoodenObjectType.COMPOSTER, registerBlockItems(WoodenObjectType.COMPOSTER, simpleMiscBlockItem));
        registryObjects.put(WoodenObjectType.WALL, registerBlockItems(WoodenObjectType.WALL, simpleDecorationBlockItem));
        registryObjects.put(WoodenObjectType.CHEST, registerBlockItems(WoodenObjectType.CHEST, registerSimpleBlockItem((new Item.Properties()).group(ItemGroup.DECORATIONS).setISTER(() -> WoodenChestItemStackTileEntityRenderer::new))));
        registryObjects.put(WoodenObjectType.LADDER, registerBlockItems(WoodenObjectType.LADDER, simpleDecorationBlockItem));
        registryObjects.put(WoodenObjectType.STICK, registerSimpleItems(WoodenItems::registerStickItem));
        registryObjects.put(WoodenObjectType.TORCH, registerSimpleItems(WoodenItems::registerTorchItem));
        registryObjects.put(WoodenObjectType.CRAFTING_TABLE, registerBlockItems(WoodenObjectType.CRAFTING_TABLE, simpleDecorationBlockItem));
        registryObjects.put(WoodenObjectType.SCAFFOLDING, registerBlockItems(WoodenObjectType.SCAFFOLDING, (objectType, block) -> new WoodenScaffoldingItem(objectType, block.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS))));
        registryObjects.put(WoodenObjectType.LECTERN, registerBlockItems(WoodenObjectType.LECTERN, simpleRedstoneBlockItem));
        registryObjects.put(WoodenObjectType.POST, registerBlockItems(WoodenObjectType.POST, simpleDecorationBlockItem));
        registryObjects.put(WoodenObjectType.STRIPPED_POST, registerBlockItems(WoodenObjectType.STRIPPED_POST, simpleDecorationBlockItem));

        REGISTRY_OBJECTS = Collections.unmodifiableMap(registryObjects);

        final Map<WoodenTieredObjectType, Map<WoodType, Map<WoodenItemTier, RegistryObject<Item>>>> tieredRegistryObjects = new EnumMap<>(WoodenTieredObjectType.class);

        tieredRegistryObjects.put(WoodenTieredObjectType.AXE, registerTieredItems(WoodenItems::registerAxeItem));
        tieredRegistryObjects.put(WoodenTieredObjectType.HOE, registerTieredItems(WoodenItems::registerHoeItem));
        tieredRegistryObjects.put(WoodenTieredObjectType.PICKAXE, registerTieredItems(WoodenItems::registerPickaxeItem));
        tieredRegistryObjects.put(WoodenTieredObjectType.SHOVEL, registerTieredItems(WoodenItems::registerShovelItem));
        tieredRegistryObjects.put(WoodenTieredObjectType.SWORD, registerTieredItems(WoodenItems::registerSwordItem));

        TIERED_REGISTRY_OBJECTS = Collections.unmodifiableMap(tieredRegistryObjects);
    }

    private WoodenItems() {
    }

    public static Item getItem(final WoodenObjectType objectType, final WoodType woodType) {
        return REGISTRY_OBJECTS.get(objectType).get(woodType).get();
    }

    public static Stream<Item> getItems(final WoodenObjectType objectType) {
        return REGISTRY_OBJECTS.get(objectType).values().stream().map(RegistryObject::get);
    }

    public static Item getTieredItem(final WoodenTieredObjectType tieredObjectType, final WoodType woodType, final WoodenItemTier itemTier) {
        return TIERED_REGISTRY_OBJECTS.get(tieredObjectType).get(woodType).get(itemTier).get();
    }

    public static Stream<Item> getTieredItems(final WoodenTieredObjectType tieredObjectType) {
        return TIERED_REGISTRY_OBJECTS.get(tieredObjectType).values().stream().flatMap(m -> m.values().stream()).map(RegistryObject::get);
    }

    public static Stream<Item> getTieredItems(final WoodenTieredObjectType... tieredObjectTypes) {
        return Arrays.stream(tieredObjectTypes).flatMap(WoodenItems::getTieredItems);
    }

    private static Map<WoodType, RegistryObject<Item>> registerBlockItems(final WoodenObjectType objectType, final BiFunction<WoodenObjectType, RegistryObject<Block>, Item> function) {
        final Map<WoodType, RegistryObject<Item>> items = new EnumMap<>(WoodType.class);
        WoodType.getLoadedValues().forEach(woodType -> {
            final RegistryObject<Block> block = WoodenBlocks.getRegistryObject(objectType, woodType);
            items.put(woodType, REGISTRY.register(block.getId().getPath(), () -> function.apply(objectType, block)));
        });
        return Collections.unmodifiableMap(items);
    }

    private static Map<WoodType, RegistryObject<Item>> registerSimpleItems(final Function<WoodType, RegistryObject<Item>> function) {
        final Map<WoodType, RegistryObject<Item>> items = new EnumMap<>(WoodType.class);
        WoodType.getLoadedValues().forEach(woodType -> items.put(woodType, function.apply(woodType)));
        return Collections.unmodifiableMap(items);
    }

    private static Map<WoodType, Map<WoodenItemTier, RegistryObject<Item>>> registerTieredItems(final BiFunction<WoodType, WoodenItemTier, RegistryObject<Item>> function) {
        final Map<WoodType, Map<WoodenItemTier, RegistryObject<Item>>> tieredItems = new EnumMap<>(WoodType.class);
        WoodType.getLoadedValues().forEach(woodType -> {
            final Map<WoodenItemTier, RegistryObject<Item>> items = new EnumMap<>(WoodenItemTier.class);
            for (final WoodenItemTier itemTier : WoodenItemTier.values()) {
                if (itemTier.isWood() && !itemTier.toString().equals(woodType.toString())) continue;
                items.put(itemTier, function.apply(woodType, itemTier));
            }
            tieredItems.put(woodType, Collections.unmodifiableMap(items));
        });
        return Collections.unmodifiableMap(tieredItems);
    }

    private static BiFunction<WoodenObjectType, RegistryObject<Block>, Item> registerSimpleBlockItem(final Item.Properties properties) {
        return (objectType, block) -> new WoodenBlockItem(objectType, block.get(), properties);
    }

    private static RegistryObject<Item> registerStickItem(final WoodType woodType) {
        return REGISTRY.register(Util.toRegistryName(woodType.toString(), WoodenObjectType.STICK.toString()), () -> new WoodenItem(woodType, WoodenObjectType.STICK, (new Item.Properties()).group(ItemGroup.MATERIALS)));
    }

    private static RegistryObject<Item> registerTorchItem(final WoodType woodType) {
        final RegistryObject<Block> torch = WoodenBlocks.getRegistryObject(WoodenObjectType.TORCH, woodType);
        final RegistryObject<Block> wallTorch = WoodenBlocks.getRegistryObject(WoodenObjectType.WALL_TORCH, woodType);
        return REGISTRY.register(torch.getId().getPath(), () -> new WoodenWallOrFloorItem(WoodenObjectType.TORCH, torch.get(), wallTorch.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
    }

    private static RegistryObject<Item> registerHoeItem(final WoodType woodType, final WoodenItemTier itemTier) {
        return REGISTRY.register(Util.toRegistryName((itemTier.isWood() ? "" : itemTier.toString() + "_") + woodType.toString(), WoodenTieredObjectType.HOE.toString()), () -> new WoodenHoeItem(woodType, itemTier));
    }

    private static RegistryObject<Item> registerSwordItem(final WoodType woodType, final WoodenItemTier itemTier) {
        return REGISTRY.register(Util.toRegistryName((itemTier.isWood() ? "" : itemTier.toString() + "_") + woodType.toString(), WoodenTieredObjectType.SWORD.toString()), () -> new WoodenSwordItem(woodType, itemTier));
    }

    private static RegistryObject<Item> registerAxeItem(final WoodType woodType, final WoodenItemTier itemTier) {
        return REGISTRY.register(Util.toRegistryName((itemTier.isWood() ? "" : itemTier.toString() + "_") + woodType.toString(), WoodenTieredObjectType.AXE.toString()), () -> new WoodenAxeItem(woodType, itemTier));
    }

    private static RegistryObject<Item> registerPickaxeItem(final WoodType woodType, final WoodenItemTier itemTier) {
        return REGISTRY.register(Util.toRegistryName((itemTier.isWood() ? "" : itemTier.toString() + "_") + woodType.toString(), WoodenTieredObjectType.PICKAXE.toString()), () -> new WoodenPickAxeItem(woodType, itemTier));
    }

    private static RegistryObject<Item> registerShovelItem(final WoodType woodType, final WoodenItemTier itemTier) {
        return REGISTRY.register(Util.toRegistryName((itemTier.isWood() ? "" : itemTier.toString() + "_") + woodType.toString(), WoodenTieredObjectType.SHOVEL.toString()), () -> new WoodenShovelItem(woodType, itemTier));
    }
}
