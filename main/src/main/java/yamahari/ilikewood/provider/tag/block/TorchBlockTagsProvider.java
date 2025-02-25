package yamahari.ilikewood.provider.tag.block;

import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import yamahari.ilikewood.data.tag.ILikeWoodBlockTags;
import yamahari.ilikewood.registry.objecttype.WoodenBlockType;
import yamahari.ilikewood.util.Constants;

public final class TorchBlockTagsProvider extends AbstractBlockTagsProvider {
    public TorchBlockTagsProvider(final DataGenerator generator, final ExistingFileHelper existingFileHelper) {
        super(generator, existingFileHelper, Constants.TORCH_PLURAL);
    }

    @Override
    protected void addTags() {
        this.registerTag(ILikeWoodBlockTags.TORCHES, WoodenBlockType.TORCH);
        this.registerTag(ILikeWoodBlockTags.SOUL_TORCHES, WoodenBlockType.SOUL_TORCH);
        this.registerTag(ILikeWoodBlockTags.WALL_TORCHES, WoodenBlockType.WALL_TORCH);
        this.registerTag(ILikeWoodBlockTags.WALL_SOUL_TORCHES, WoodenBlockType.WALL_SOUL_TORCH);
        this.tag(BlockTags.PIGLIN_REPELLENTS).addTag(ILikeWoodBlockTags.SOUL_TORCHES);
        this.tag(BlockTags.PIGLIN_REPELLENTS).addTag(ILikeWoodBlockTags.WALL_SOUL_TORCHES);
        this.tag(BlockTags.MINEABLE_WITH_AXE).addTag(ILikeWoodBlockTags.TORCHES);
        this.tag(BlockTags.MINEABLE_WITH_AXE).addTag(ILikeWoodBlockTags.SOUL_TORCHES);
        this.tag(BlockTags.MINEABLE_WITH_AXE).addTag(ILikeWoodBlockTags.WALL_TORCHES);
        this.tag(BlockTags.MINEABLE_WITH_AXE).addTag(ILikeWoodBlockTags.WALL_SOUL_TORCHES);
    }
}
