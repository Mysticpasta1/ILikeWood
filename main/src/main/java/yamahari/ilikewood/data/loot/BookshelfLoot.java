package yamahari.ilikewood.data.loot;

import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import yamahari.ilikewood.ILikeWood;
import yamahari.ilikewood.registry.objecttype.WoodenBlockType;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public final class BookshelfLoot extends BlockLoot {
    @Nonnull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ILikeWood.BLOCK_REGISTRY.getObjects(WoodenBlockType.BOOKSHELF).collect(Collectors.toList());
    }

    @Override
    protected void addTables() {
        ILikeWood.BLOCK_REGISTRY.getObjects(WoodenBlockType.BOOKSHELF)
            .forEach(block -> this.add(
                block,
                b -> createSingleItemTableWithSilkTouch(b, Items.BOOK, ConstantValue.exactly(3))
            ));
    }
}
