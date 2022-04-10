package dinglydell.tfice.block;

import java.util.Random;

import com.dunk.tfc.Blocks.Vanilla.BlockCustomIce;
import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.WorldGen.TFCProvider;
import com.dunk.tfc.api.TFCBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockUnstableIce extends BlockCustomIce {
	private static final int ICE_TYPES = 3;
	
	@Override
	public int getLightOpacity(IBlockAccess world, int x, int y, int z) {

		int meta = world.getBlockMetadata(x,y,z);
		switch(meta) {
			case 0: // just about sea ice
				return 7;
			case 1: // just about fresh ice
				return 1;
			case 2: // just about tannin ice
				return 1;
			case 3: // solidish sea ice
				return 9;
			case 4: // solidish fresh ice
				return 3;
			case 5: // solidish tannin ice
				return 3;
			case 6: // very solid sea ice
				return 11;
			case 7: // very solid fresh ice
				return 5;
			case 8: // very solid tannin ice
				return 5;
		}
		return this.getLightOpacity();
	}
	
	@Override
	protected Block getBlockMelt(World world, int i, int j, int k, boolean moving) {
		Block block = world.getBlock(i,j,k);

		if(block != this)
			return block;

		int meta = world.getBlockMetadata(i, j, k);
		switch(meta % ICE_TYPES){
		case 0: return TFCBlocks.saltWater;
		case 1: return TFCBlocks.freshWater;
		case 2: return TFCBlocks.tanninWater;
		default: return TFCBlocks.saltWater;
		}
	}
	
	@Override
	public float getBlockHardness(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		// The thicker the ice, the harder to break
		return 0.2f * (1+meta/ICE_TYPES);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z,
			Entity entity) {
		if(world.isRemote) {
			return;
		}
		int meta = world.getBlockMetadata(x, y, z);
		if(meta >= ICE_TYPES * 2) {
			return;
		}
		//TerraFirmaCraft.LOG.info(meta + " entity " + entity.getClass());
		int breakChanceMultiplier = 4;
		if(entity.isSneaking()) {
			breakChanceMultiplier = 8;
		} else if(entity.isSprinting()) {
			breakChanceMultiplier = 2;
		}
		breakChanceMultiplier = Math.max(0, (int)(breakChanceMultiplier / (1+entity.fallDistance)));
		//if(entity instanceof EntityPlayer) {
		//	TerraFirmaCraft.LOG.info("meta" + meta + ", break chance:" + breakChanceMultiplier);
		//}
		if(meta < ICE_TYPES && world.rand.nextInt(1+2*breakChanceMultiplier) == 0) {
			world.setBlock(x, y, z, getBlockMelt(world, x, y, z, false));
			world.playSoundEffect(x, y, z, Block.soundTypeGlass.getBreakSound(), 1f, 1f);
			//TerraFirmaCraft.LOG.info("sound "+ x + ", " + y + "," + z);
		} else if(world.rand.nextInt(1+100 * breakChanceMultiplier) == 0) {
			world.setBlockMetadataWithNotify(x, y, z, meta - ICE_TYPES, 0);
		}
	
	}
	
	//stolen from soul sand
	 public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
	 {
	 	float f = 0.125F;
	 	return AxisAlignedBB.getBoundingBox((double)p_149668_2_, (double)p_149668_3_, (double)p_149668_4_, (double)(p_149668_2_ + 1), (double)((float)(p_149668_3_ + 1) - f), (double)(p_149668_4_ + 1));
	 }

	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int par1, int par2)
	{
		return super.getIcon(par1, par2 % ICE_TYPES);
	}
	
	
	
	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		if((world.provider) instanceof TFCProvider && !world.isRemote && world.getBlock(x, y, z) == this)
		{
			float temp = TFC_Climate.getHeightAdjustedTemp(world, x, y, z);
			int meta = world.getBlockMetadata(x, y, z);
			float freezeTemp = getFreezeTemp(meta);
			//float partialFreezeTemp = getPartialFreezeTemp(meta);
			int freezeProgressionChance =  (int) Math.max(1, (10 + 2*(temp - freezeTemp) + 8*(meta/ICE_TYPES)));
			int meltProgressionChance = (int)Math.max(1, (10 + 2*(freezeTemp - temp)));
			if(meta < 4 && temp < freezeTemp && rand.nextInt(freezeProgressionChance) == 0) {
				// freeze further
				world.setBlockMetadataWithNotify(x, y, z, meta + ICE_TYPES, 0);
				//TerraFirmaCraft.LOG.info("Frozen " + meta + "->" + (meta+2));
			} else if(temp > freezeTemp && rand.nextInt(meltProgressionChance) == 0 ) {
				//melt slightly
				if(meta > 2) {
				world.setBlockMetadataWithNotify(x, y, z, meta - ICE_TYPES, 0);
				} else {
					world.setBlock(x, y, z, getBlockMelt(world, x, y, z, false), 0, 2);
				}
				//TerraFirmaCraft.LOG.info("Melted " + meta + "->" + (meta-2));
			}
		}
	}
	private float getFreezeTemp(int meta) {
		switch(meta % ICE_TYPES) {
			case 0: //sea ice
				return -2;
			case 1: //fresh ice
				return 0;
			case 2: //tannin ice
				return 0;
		}
		return 0;
	}

}
