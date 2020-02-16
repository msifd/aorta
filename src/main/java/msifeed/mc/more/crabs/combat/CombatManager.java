package msifeed.mc.more.crabs.combat;

import com.google.common.collect.Multimap;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.sys.rpc.Rpc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.Collection;

public class CombatManager {
    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
        Rpc.register(CombatRpc.INSTANCE);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHurtDamage(LivingHurtEvent event) {
        final Entity damager = event.source.getEntity();
        if (damager instanceof EntityLivingBase) {
            final ItemStack item = ((EntityLivingBase) damager).getHeldItem();
            if (item != null) {
                final String uname = item.getItem().getUnlocalizedName(item);
                final Multimap map = item.getAttributeModifiers();
                final Collection<AttributeModifier> attrs = map.get(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
                for (AttributeModifier a : attrs) {
                    System.out.println(a.toString());
                }
//                System.out.println(uname);
//                if (uname.equals("item.bow"))
//                    event.ammount = 20;
            }
        }
        System.out.println(event.source.getSourceOfDamage().toString());
        System.out.println("dmg: " + event.ammount);
    }
}
