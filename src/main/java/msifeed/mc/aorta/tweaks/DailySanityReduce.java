package msifeed.mc.aorta.tweaks;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.core.character.CharRpc;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.meta.MetaInfo;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.core.utils.MetaAttribute;
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
        MetaInfo meta = MetaAttribute.require(player);
        final long curTime = System.currentTimeMillis();
        final long msInDay = 86400000;
        final long loginTime = meta.lastLogin;

        if (curTime - loginTime > msInDay || meta.lastLogin == 0) {
            NBTTagCompound c = CharacterAttribute.require(player).toNBT();
            c.setByte("sanity", (byte)Math.max(1, c.getByte("sanity") - 3));
            CharRpc.updateChar(player, c, (EntityPlayerMP)player);

            meta.lastLogin = curTime;
        }
    }
}
