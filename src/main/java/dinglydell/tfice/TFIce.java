package dinglydell.tfice;

import net.minecraft.block.Block;
import com.dunk.tfc.api.TFCBlocks;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import dinglydell.tfice.block.BlockUnstableIce;

@Mod(modid = TFIce.MODID, version = TFIce.VERSION, dependencies="required-after:terrafirmacraftplus")
public class TFIce
{
    public static final String MODID = "tfice";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	TFCBlocks.ice = new BlockUnstableIce().setBlockName("ice").setHardness(0.5F).setStepSound(Block.soundTypeGlass);
		
    	
    	GameRegistry.registerBlock(TFCBlocks.ice, "ice");
    }
}
