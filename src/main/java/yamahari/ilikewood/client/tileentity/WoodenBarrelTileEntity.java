package yamahari.ilikewood.client.tileentity;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.BarrelTileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.StringUtils;
import yamahari.ilikewood.IWoodType;
import yamahari.ilikewood.block.WoodenBarrelBlock;
import yamahari.ilikewood.util.Constants;
import yamahari.ilikewood.util.IWooden;
import yamahari.ilikewood.util.WoodenObjectType;

public final class WoodenBarrelTileEntity extends BarrelTileEntity implements IWooden {
    private final IWoodType woodType;
    private final TranslationTextComponent defaultName;

    public WoodenBarrelTileEntity(final IWoodType woodType, final TileEntityType<?> type) {
        super(type);
        this.woodType = woodType;
        this.defaultName = new TranslationTextComponent(
                StringUtils.joinWith(".", "container", Constants.MOD_ID,
                        this.getWoodType().toString() + "_" + WoodenObjectType.BARREL.toString()));
    }

    @Override
    public IWoodType getWoodType() {
        return this.woodType;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected ITextComponent getDefaultName() {
        return this.defaultName;
    }

    @Override
    public void barrelTick() {
        final int x = this.pos.getX();
        final int y = this.pos.getY();
        final int z = this.pos.getZ();
        this.numPlayersUsing = ChestTileEntity.calculatePlayersUsing(this.world, this, x, y, z);
        if (this.numPlayersUsing > 0) {
            this.scheduleTick();
        } else {
            final BlockState blockState = this.getBlockState();
            if (!(blockState.getBlock() instanceof WoodenBarrelBlock)) {
                this.remove();
                return;
            }

            final boolean open = blockState.get(BarrelBlock.PROPERTY_OPEN);
            if (open) {
                this.playSound(blockState, SoundEvents.BLOCK_BARREL_CLOSE);
                this.setOpenProperty(blockState, false);
            }
        }
    }
}
