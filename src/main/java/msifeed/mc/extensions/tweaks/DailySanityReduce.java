package msifeed.mc.extensions.tweaks;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.meta.MetaInfo;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.core.utils.Differ;
import msifeed.mc.aorta.core.utils.MetaAttribute;
import msifeed.mc.aorta.defines.DrugDefines;
import msifeed.mc.aorta.defines.SanityDefines;
import msifeed.mc.commons.traits.Trait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;

import java.util.Map;

public enum DailySanityReduce {
    INSTANCE;

    public static void apply() {
        FMLCommonHandler.instance().bus().register(INSTANCE);
    }

    @SubscribeEvent
    public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        final EntityPlayer player = event.player;
        final SanityDefines sanity = Aorta.DEFINES.get().sanity;
        int sanityReduce = 0;

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

        MetaInfo meta = MetaAttribute.require(player);
        final long curTime = System.currentTimeMillis();
        final long msInDay = 1000; //86400000
        final long loginTime = meta.lastLogin;

        if (curTime - loginTime > msInDay) {
            final Character after = CharacterAttribute.require(player);
            final Character before = new Character(after);

            for (Map.Entry<String, Integer> e : before.addictions.entrySet()) {
                final DrugDefines drug = Aorta.DEFINES.get().drug;
                final int value = e.getValue();
                after.addictions.put(e.getKey(), value > 1 ? value - 2 : value);
                sanityReduce += value % 2 == 0 ? drug.baseSanityReduce : drug.strongSanityReduce;
            }

            if (sanityReduce != 0) {
                after.sanity = (byte)MathHelper.clamp_int(
                        after.sanity - sanityReduce, 1, 125);
                CharacterAttribute.INSTANCE.set(player, after);

                Differ.printDiffs((EntityPlayerMP)player, player, before, after);
                MetaAttribute.INSTANCE.update(player, metaInfo -> metaInfo.lastLogin = curTime);
            }
        }
    }
}
