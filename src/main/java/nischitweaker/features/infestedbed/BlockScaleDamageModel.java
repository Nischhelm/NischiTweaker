package nischitweaker.features.infestedbed;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Recomputes quad UVs from vertex positions, like a full block would have.
 * The block breaking overlay keeps each quad's relative UVs, so models using small regions of a large texture would get a zoomed in overlay.
 */
@SideOnly(Side.CLIENT)
public class BlockScaleDamageModel implements IBakedModel {
    private final IBakedModel parent;

    public BlockScaleDamageModel(IBakedModel parent) {
        this.parent = parent;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> quads = parent.getQuads(state, side, rand);
        List<BakedQuad> result = new ArrayList<>(quads.size());
        for (BakedQuad quad : quads) result.add(remap(quad));
        return result;
    }

    private static BakedQuad remap(BakedQuad quad) {
        int[] data = quad.getVertexData().clone();
        int stride = quad.getFormat().getIntegerSize();
        int uvOffset = quad.getFormat().getUvOffsetById(0) / 4;
        TextureAtlasSprite sprite = quad.getSprite();
        for (int i = 0; i < 4; i++) {
            int start = i * stride;
            float x = Float.intBitsToFloat(data[start]);
            float y = Float.intBitsToFloat(data[start + 1]);
            float z = Float.intBitsToFloat(data[start + 2]);
            float u, v;
            switch (quad.getFace()) {
                case DOWN: u = x; v = 1 - z; break;
                case UP: u = x; v = z; break;
                case NORTH: u = 1 - x; v = 1 - y; break;
                case SOUTH: u = x; v = 1 - y; break;
                case WEST: u = z; v = 1 - y; break;
                default: u = 1 - z; v = 1 - y; break;
            }
            data[start + uvOffset] = Float.floatToRawIntBits(sprite.getInterpolatedU(u * 16));
            data[start + uvOffset + 1] = Float.floatToRawIntBits(sprite.getInterpolatedV(v * 16));
        }
        return new BakedQuad(data, quad.getTintIndex(), quad.getFace(), sprite, quad.shouldApplyDiffuseLighting(), quad.getFormat());
    }

    @Override
    public boolean isAmbientOcclusion() {
        return parent.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return parent.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return parent.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return parent.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return parent.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return parent.getOverrides();
    }
}
