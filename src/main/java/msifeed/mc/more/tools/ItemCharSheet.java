package msifeed.mc.more.tools;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.genesis.GenesisCreativeTab;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

import java.util.List;

public class ItemCharSheet extends Item {
    public static final String ITEM_NAME = "char_sheet";

    public ItemCharSheet() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName("paper");
        setCreativeTab(GenesisCreativeTab.TOOLS);
        setMaxStackSize(1);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean hasEffect(ItemStack par1ItemStack, int pass) {
        return true;
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return 0;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advanced) {
        lines.add(L10n.tr("item.char_sheet.desc"));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onEntityInteract(EntityInteractEvent event) {
        // ПКМ листком по существу для редактирования его стат
        if (!(event.target instanceof EntityLivingBase))
            return;

//        final ItemStack itemStack = event.entityPlayer.getHeldItem();
//        if (itemStack == null) {
//            final Context playerCtx = ContextManager.INSTANCE.getContext(event.entityPlayer);
//            if (playerCtx == null || !playerCtx.status.isFighting()) return;
//            final Context targetCtx = ContextManager.INSTANCE.getContext((EntityLivingBase) event.target);
//            if (targetCtx == null || !targetCtx.status.isFighting()) return;
//
//            CharacterHud.INSTANCE.setEditable(false);
//        } else if (!(itemStack.getItem() instanceof ItemCharSheet)) {
//            return;
//        }
//
//        CharacterHud.INSTANCE.setEntity((EntityLivingBase) event.target);
//        HudManager.INSTANCE.openHud(CharacterHud.INSTANCE);
    }
}
