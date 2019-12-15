package msifeed.mc.aorta.economy.auction;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.economy.Economy;
import msifeed.mc.aorta.genesis.AortaCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class AuctionTerminalBlock extends Block {
    public static final String BLOCK_ID = "auction_terminal";

    public AuctionTerminalBlock() {
        super(Material.iron);

        setBlockName("auction_terminal");
        setBlockTextureName("aorta:auction_terminal");
        setCreativeTab(AortaCreativeTab.TOOLS);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
        if (!world.isRemote)
            Economy.getAuctionData().clearLots();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            Aorta.GUI_HANDLER.toggleAuctionTerminal();
        return true;
    }
}
