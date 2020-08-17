package yamahari.ilikewood.client.tileentity.renderer;

import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.TileEntity;
import yamahari.ilikewood.client.Atlases;
import yamahari.ilikewood.client.tileentity.WoodenChestTileEntity;

import java.util.Map;

public final class WoodenChestTileEntityRenderer extends ChestTileEntityRenderer {
    private Map<ChestType, RenderMaterial> materials;

    public WoodenChestTileEntityRenderer(final TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
        this.materials = null;
    }

    @Override
    protected RenderMaterial getMaterial(final TileEntity tileEntity, final ChestType chestType) {
        if (this.materials == null) {
            this.materials = Atlases.getChestMaterials(((WoodenChestTileEntity) tileEntity).getWoodType());
        }
        assert this.materials != null;
        return this.materials.get(chestType);
    }
}
