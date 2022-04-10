package dinglydell.tfice.block;

import com.dunk.tfc.Blocks.Vanilla.BlockCustomIce;

import dinglydell.tfice.TFIce;
import net.minecraft.world.World;

/** Dummy block that replaces TFC's ice with BlockUnstableIce when water freezes.
 *  This gives full control over thawing of ice to this addon. */
public class BlockIceReplacer extends BlockCustomIce {
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		world.setBlock(x, y, z, TFIce.unstableIce, world.getBlockMetadata(x, y, z), 4);
	}
}
