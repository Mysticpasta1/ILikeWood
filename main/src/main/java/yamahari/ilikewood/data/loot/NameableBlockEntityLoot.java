package yamahari.ilikewood.data.loot;

import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import yamahari.ilikewood.ILikeWood;
import yamahari.ilikewood.registry.objecttype.WoodenBlockType;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public final class NameableBlockEntityLoot extends BlockLoot {
    private final WoodenBlockType blockType;

    public NameableBlockEntityLoot(final WoodenBlockType blockType) {
        this.blockType = blockType;
    }

    @Nonnull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ILikeWood.BLOCK_REGISTRY.getObjects(this.blockType).collect(Collectors.toList());
    }

    @Override
    protected void addTables() {
        ILikeWood.BLOCK_REGISTRY.getObjects(this.blockType)
            .forEach(block -> this.add(block, BlockLoot::createNameableBlockEntityTable));
    }
}