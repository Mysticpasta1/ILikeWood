package yamahari.ilikewood.provider;

import biomesoplenty.api.block.BOPBlocks;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import yamahari.ilikewood.ILikeWood;
import yamahari.ilikewood.data.tag.BlockTags;
import yamahari.ilikewood.data.tag.ItemTags;
import yamahari.ilikewood.item.tiered.IWoodenTieredItem;
import yamahari.ilikewood.registry.WoodenBlocks;
import yamahari.ilikewood.registry.WoodenItems;
import yamahari.ilikewood.util.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Consumer;

public final class RecipeProvider extends net.minecraft.data.RecipeProvider {
    public RecipeProvider(DataGenerator generator) {
        super(generator);
    }

    private static IItemProvider getIngredient(final String name, final Class<?> objectHolder) {
        try {
            final Field block = objectHolder.getDeclaredField(name);
            return (IItemProvider) block.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            ILikeWood.LOGGER.error(e.getMessage());
        }
        return null;
    }

    private static IItemProvider getIngredient(final WoodType woodType, final String name) {
        final IItemProvider ingredient;
        switch (woodType.getModId()) {
            case Constants.MOD_ID:
                ingredient = getIngredient(Util.toRegistryName(woodType.toString().toUpperCase(), name.toUpperCase()), Blocks.class);
                break;
            case Constants.BOP_MOD_ID:
                ingredient = getIngredient(Util.toRegistryName(woodType.toString(), name), BOPBlocks.class);
                break;
            default:
                ingredient = null;
                break;
        }
        assert ingredient != null;
        return ingredient;
    }

    private static IItemProvider getLog(final WoodType woodType) {
        return getIngredient(woodType, "log");
    }

    private static IItemProvider getSlab(final WoodType woodType) {
        return getIngredient(woodType, "slab");
    }

    private static IItemProvider getPlanks(final WoodType woodType) {
        return getIngredient(woodType, "planks");
    }

    private static IItemProvider getFence(final WoodType woodType) {
        return getIngredient(woodType, "fence");
    }

    private InventoryChangeTrigger.Instance hasItem(Ingredient ingredientIn) {
        return InventoryChangeTrigger.Instance.forItems(Arrays.stream(ingredientIn.getMatchingStacks()).map(ItemStack::getItem).toArray(Item[]::new));
    }

    @Override
    protected void registerRecipes(final Consumer<IFinishedRecipe> consumer) {
        WoodenBlocks.getBlocks(WoodenObjectType.PANELS).forEach(block -> {
            final IItemProvider slab = getSlab(((IWooden) block).getWoodType());

            ShapedRecipeBuilder.shapedRecipe(block)
                    .key('#', slab)
                    .patternLine("#")
                    .patternLine("#")
                    .addCriterion("has_slab", hasItem(slab))
                    .setGroup(BlockTags.PANELS.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenBlocks.getBlocks(WoodenObjectType.STAIRS).forEach(block -> {
            final IItemProvider panels = WoodenBlocks.getBlock(WoodenObjectType.PANELS, ((IWooden) block).getWoodType());

            ShapedRecipeBuilder.shapedRecipe(block, 4)
                    .key('#', panels)
                    .patternLine("#  ")
                    .patternLine("## ")
                    .patternLine("###")
                    .addCriterion("has_panels", hasItem(panels))
                    .setGroup(BlockTags.PANELS.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenBlocks.getBlocks(WoodenObjectType.SLAB).forEach(block -> {
            final WoodType woodType = ((IWooden) block).getWoodType();
            final IItemProvider panels = WoodenBlocks.getBlock(WoodenObjectType.PANELS, woodType);
            final IItemProvider planks = getPlanks(woodType);

            ShapedRecipeBuilder.shapedRecipe(block, 6)
                    .key('#', panels)
                    .patternLine("###")
                    .addCriterion("has_panels", hasItem(panels))
                    .setGroup(BlockTags.PANELS_SLABS.func_230234_a_().getPath())
                    .build(consumer);

            ShapedRecipeBuilder.shapedRecipe(planks)
                    .key('S', block)
                    .patternLine("S")
                    .patternLine("S")
                    .addCriterion("has_panels_slab", hasItem(block))
                    .setGroup("ilikewood:planks")
                    .build(consumer, Constants.MOD_ID + ":" + planks.asItem().getRegistryName().getPath() + "_from_" + block.getRegistryName().getPath());
        });

        WoodenBlocks.getBlocks(WoodenObjectType.BARREL).forEach(block -> {
            final WoodType woodType = ((IWooden) block).getWoodType();
            final IItemProvider panels = WoodenBlocks.getBlock(WoodenObjectType.PANELS, woodType);
            final IItemProvider slab = WoodenBlocks.getBlock(WoodenObjectType.SLAB, woodType);

            ShapedRecipeBuilder.shapedRecipe(block)
                    .key('P', panels)
                    .key('S', slab)
                    .patternLine("PSP")
                    .patternLine("P P")
                    .patternLine("PSP")
                    .addCriterion("has_panels", hasItem(panels))
                    .setGroup(BlockTags.BARRELS.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenBlocks.getBlocks(WoodenObjectType.BOOKSHELF).forEach(block -> {
            final IItemProvider panels = WoodenBlocks.getBlock(WoodenObjectType.PANELS, ((IWooden) block).getWoodType());

            ShapedRecipeBuilder.shapedRecipe(block)
                    .key('#', panels)
                    .key('X', Items.BOOK)
                    .patternLine("###")
                    .patternLine("XXX")
                    .patternLine("###")
                    .addCriterion("has_book", hasItem(Items.BOOK))
                    .setGroup(BlockTags.BOOKSHELFS.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenBlocks.getBlocks(WoodenObjectType.CHEST).forEach(block -> {
            final IItemProvider panels = WoodenBlocks.getBlock(WoodenObjectType.PANELS, ((IWooden) block).getWoodType());

            ShapedRecipeBuilder.shapedRecipe(block)
                    .key('#', panels)
                    .patternLine("###")
                    .patternLine("# #")
                    .patternLine("###")
                    .addCriterion("has_panels", hasItem(panels))
                    .setGroup(BlockTags.CHESTS.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenBlocks.getBlocks(WoodenObjectType.COMPOSTER).forEach(block -> {
            final WoodType woodType = ((IWooden) block).getWoodType();
            final IItemProvider panels = WoodenBlocks.getBlock(WoodenObjectType.PANELS, woodType);
            final IItemProvider fence = getFence(woodType);

            ShapedRecipeBuilder.shapedRecipe(block)
                    .key('#', panels)
                    .key('F', fence)
                    .patternLine("F F")
                    .patternLine("F F")
                    .patternLine("###")
                    .addCriterion("has_panels", hasItem(panels))
                    .setGroup(BlockTags.COMPOSTER.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenBlocks.getBlocks(WoodenObjectType.WALL).forEach(block -> {
            final IItemProvider log = getLog(((IWooden) block).getWoodType());

            ShapedRecipeBuilder.shapedRecipe(block, 6)
                    .key('#', log)
                    .patternLine("###")
                    .patternLine("###")
                    .addCriterion("has_log", hasItem(log))
                    .setGroup(BlockTags.WALLS.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenBlocks.getBlocks(WoodenObjectType.LADDER).forEach(block -> {
            final WoodType woodType = ((IWooden) block).getWoodType();
            final IItemProvider stick = WoodenItems.getItem(WoodenObjectType.STICK, woodType);

            ShapedRecipeBuilder.shapedRecipe(block, 3)
                    .key('I', stick)
                    .patternLine("I I")
                    .patternLine("III")
                    .patternLine("I I")
                    .addCriterion("has_stick", hasItem(stick))
                    .setGroup(BlockTags.LADDERS.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenBlocks.getBlocks(WoodenObjectType.TORCH).forEach(block -> {
            final WoodType woodType = ((IWooden) block).getWoodType();
            final IItemProvider stick = WoodenItems.getItem(WoodenObjectType.STICK, woodType);
            final Ingredient coals = Ingredient.fromTag(net.minecraft.tags.ItemTags.COALS);

            ShapedRecipeBuilder.shapedRecipe(block, 4)
                    .key('I', stick)
                    .key('#', coals)
                    .patternLine("#")
                    .patternLine("I")
                    .addCriterion("has_coal", hasItem(coals))
                    .setGroup(BlockTags.TORCHES.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenBlocks.getBlocks(WoodenObjectType.CRAFTING_TABLE).forEach(block -> {
            final WoodType woodType = ((IWooden) block).getWoodType();
            final IItemProvider panels = WoodenBlocks.getBlock(WoodenObjectType.PANELS, woodType);

            ShapedRecipeBuilder.shapedRecipe(block)
                    .key('#', panels)
                    .patternLine("##")
                    .patternLine("##")
                    .addCriterion("has_panels", hasItem(panels))
                    .setGroup(BlockTags.CRAFTING_TABLES.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenBlocks.getBlocks(WoodenObjectType.SCAFFOLDING).forEach(block -> {
            final WoodType woodType = ((IWooden) block).getWoodType();
            final IItemProvider stick = WoodenItems.getItem(WoodenObjectType.STICK, woodType);

            ShapedRecipeBuilder.shapedRecipe(block)
                    .key('I', stick)
                    .key('~', Items.STRING)
                    .patternLine("I~I")
                    .patternLine("I I")
                    .patternLine("I I")
                    .addCriterion("has_stick", hasItem(stick))
                    .setGroup(BlockTags.SCAFFOLDINGS.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenBlocks.getBlocks(WoodenObjectType.LECTERN).forEach(block -> {
            final WoodType woodType = ((IWooden) block).getWoodType();
            final IItemProvider slab = WoodenBlocks.getBlock(WoodenObjectType.SLAB, woodType);
            final IItemProvider bookshelf = WoodenBlocks.getBlock(WoodenObjectType.BOOKSHELF, woodType);

            ShapedRecipeBuilder.shapedRecipe(block)
                    .key('S', slab)
                    .key('B', bookshelf)
                    .patternLine("SSS")
                    .patternLine(" B ")
                    .patternLine(" S ")
                    .addCriterion("has_book", hasItem(Items.BOOK))
                    .setGroup(BlockTags.LECTERNS.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenBlocks.getBlocks(WoodenObjectType.POST).forEach(block -> {
            final IItemProvider log = getLog(((IWooden) block).getWoodType());

            ShapedRecipeBuilder.shapedRecipe(block, 6)
                    .key('#', log)
                    .patternLine("#")
                    .patternLine("#")
                    .patternLine("#")
                    .addCriterion("has_log", hasItem(log))
                    .setGroup(BlockTags.POSTS.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenItems.getItems(WoodenObjectType.STICK).forEach(item -> {
            final WoodType woodType = ((IWooden) item).getWoodType();
            final IItemProvider panels = WoodenBlocks.getBlock(WoodenObjectType.PANELS, woodType);

            ShapedRecipeBuilder.shapedRecipe(item, 4)
                    .key('#', panels)
                    .patternLine("#")
                    .patternLine("#")
                    .addCriterion("has_panels", hasItem(panels))
                    .setGroup(ItemTags.STICKS.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenItems.getTieredItems(WoodenTieredObjectType.AXE).forEach(item -> {
            final WoodType woodType = ((IWooden) item).getWoodType();
            final Ingredient repair = ((IWoodenTieredItem) item).getWoodenItemTier().getRepairMaterial();
            final IItemProvider stick = WoodenItems.getItem(WoodenObjectType.STICK, woodType);

            ShapedRecipeBuilder.shapedRecipe(item)
                    .key('I', stick)
                    .key('#', repair)
                    .patternLine("##")
                    .patternLine("#I")
                    .patternLine(" I")
                    .addCriterion("has_material", hasItem(repair))
                    .setGroup(ItemTags.AXES.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenItems.getTieredItems(WoodenTieredObjectType.HOE).forEach(item -> {
            final WoodType woodType = ((IWooden) item).getWoodType();
            final Ingredient repair = ((IWoodenTieredItem) item).getWoodenItemTier().getRepairMaterial();
            final IItemProvider stick = WoodenItems.getItem(WoodenObjectType.STICK, woodType);

            ShapedRecipeBuilder.shapedRecipe(item)
                    .key('I', stick)
                    .key('#', repair)
                    .patternLine("##")
                    .patternLine(" I")
                    .patternLine(" I")
                    .addCriterion("has_material", hasItem(repair))
                    .setGroup(ItemTags.HOES.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenItems.getTieredItems(WoodenTieredObjectType.PICKAXE).forEach(item -> {
            final WoodType woodType = ((IWooden) item).getWoodType();
            final Ingredient repair = ((IWoodenTieredItem) item).getWoodenItemTier().getRepairMaterial();
            final IItemProvider stick = WoodenItems.getItem(WoodenObjectType.STICK, woodType);

            ShapedRecipeBuilder.shapedRecipe(item)
                    .key('I', stick)
                    .key('#', repair)
                    .patternLine("###")
                    .patternLine(" I ")
                    .patternLine(" I ")
                    .addCriterion("has_material", hasItem(repair))
                    .setGroup(ItemTags.PICKAXES.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenItems.getTieredItems(WoodenTieredObjectType.SHOVEL).forEach(item -> {
            final WoodType woodType = ((IWooden) item).getWoodType();
            final Ingredient repair = ((IWoodenTieredItem) item).getWoodenItemTier().getRepairMaterial();
            final IItemProvider stick = WoodenItems.getItem(WoodenObjectType.STICK, woodType);

            ShapedRecipeBuilder.shapedRecipe(item)
                    .key('I', stick)
                    .key('#', repair)
                    .patternLine("#")
                    .patternLine("I")
                    .patternLine("I")
                    .addCriterion("has_material", hasItem(repair))
                    .setGroup(ItemTags.SHOVELS.func_230234_a_().getPath())
                    .build(consumer);
        });

        WoodenItems.getTieredItems(WoodenTieredObjectType.SWORD).forEach(item -> {
            final WoodType woodType = ((IWooden) item).getWoodType();
            final Ingredient repair = ((IWoodenTieredItem) item).getWoodenItemTier().getRepairMaterial();
            final IItemProvider stick = WoodenItems.getItem(WoodenObjectType.STICK, woodType);

            ShapedRecipeBuilder.shapedRecipe(item)
                    .key('I', stick)
                    .key('#', repair)
                    .patternLine("#")
                    .patternLine("#")
                    .patternLine("I")
                    .addCriterion("has_material", hasItem(repair))
                    .setGroup(ItemTags.SWORDS.func_230234_a_().getPath())
                    .build(consumer);
        });
    }

    @Override
    public String getName() {
        return "I Like Wood - Recipe Provider";
    }
}
