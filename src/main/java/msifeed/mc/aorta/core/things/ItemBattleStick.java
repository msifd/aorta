package msifeed.mc.aorta.core.things;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.core.character.CharacterFactory;
import msifeed.mc.aorta.core.character.CharacterProperty;
import msifeed.mc.aorta.props.SyncProp;
import msifeed.mc.aorta.things.AortaCreativeTab;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class ItemBattleStick extends Item {
    public static String ITEM_NAME = "battle_stick";

    public ItemBattleStick() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName("blaze_rod");
        setCreativeTab(AortaCreativeTab.ITEMS);
        setMaxStackSize(1);
        setFull3D();

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
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity target) {
        if (!player.worldObj.isRemote || !(target instanceof EntityLivingBase))
            return true;

        final EntityLivingBase entity = (EntityLivingBase) target;

        System.out.println("entity in");
        final CharacterProperty charProp = CharacterProperty.get(entity);
        charProp.setCharacter(CharacterFactory.forEntity(entity));
        SyncProp.sync(entity.worldObj, target, charProp);

        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            System.out.println("self out");
//            charProp.character =
        }
        return itemStack;
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        if (!FMLCommonHandler.instance().getMinecraftServerInstance().isServerRunning())
            return;
        final ItemStack heldItem = event.entityPlayer.getHeldItem();
        if (heldItem == null || !(heldItem.getItem() instanceof ItemBattleStick))
            return;

        System.out.println("entity out");
    }
}
