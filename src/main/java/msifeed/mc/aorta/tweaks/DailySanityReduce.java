package msifeed.mc.aorta.tweaks;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.character.CharRpc;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.meta.MetaInfo;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.core.utils.MetaAttribute;
import msifeed.mc.aorta.defines.data.SanityDefines;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public enum DailySanityReduce {
    INSTANCE;

    public static void apply() {
        FMLCommonHandler.instance().bus().register(INSTANCE);
    }

    @SubscribeEvent
    public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) throws ParseException {
        EntityPlayer player = event.player;
        int sanityReduce = 0;
        SanityDefines sanity = Aorta.DEFINES.get().sanity;

        if (CharacterAttribute.has(player, Trait.sanity_light))
            sanityReduce = sanity.light;
        else if (CharacterAttribute.has(player, Trait.sanity_medium))
            sanityReduce = sanity.medium;
        else if (CharacterAttribute.has(player, Trait.sanity_hard))
            sanityReduce = sanity.hard;
        else if (CharacterAttribute.has(player, Trait.sanity_hardcore))
            sanityReduce = sanity.hardcore;
        else if (CharacterAttribute.has(player, Trait.sanity_extreme))
            sanityReduce = sanity.extreme;

        if (sanityReduce > 0) {
            MetaInfo meta = MetaAttribute.require(player);
            final long curTime = System.currentTimeMillis();
            final long msInDay = 86400000;
            final long loginTime = meta.lastLogin;

            if (curTime - loginTime > msInDay || meta.lastLogin == 0) {
                NBTTagCompound c = CharacterAttribute.require(player).toNBT();
                c.setByte("sanity", (byte)Math.max(1, c.getByte("sanity") - sanityReduce));
                CharRpc.updateChar(player, c, (EntityPlayerMP)player);

                MetaAttribute.INSTANCE.update(player, metaInfo -> metaInfo.lastLogin = curTime);
            }
        }
    }
}
