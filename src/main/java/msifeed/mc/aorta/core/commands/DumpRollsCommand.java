package msifeed.mc.aorta.core.commands;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.meta.MetaInfo;
import msifeed.mc.aorta.core.rolls.Dices;
import msifeed.mc.aorta.core.rolls.FightAction;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.core.utils.MetaAttribute;
import msifeed.mc.aorta.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.util.List;

public class DumpRollsCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "dump_rolls";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "dump_rolls";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityLivingBase)) {
            error(sender, "You should be at least entity!");
            return;
        }

        final Character character = CharacterAttribute.require((Entity) sender);
        final MetaInfo meta = MetaAttribute.require((Entity) sender);

        if (!character.traits().contains(Trait.gm)) {
            error(sender, "Get lost.");
            return;
        }

        final StringBuilder sb = new StringBuilder();

        sb.append("\nFeatures\n");
        for (Feature f : Feature.values()) {
            sb.append(roll(character, meta, f));
            sb.append('\n');
        }

        sb.append("Actions\n");
        for (FightAction a : FightAction.values()) {
            sb.append(roll(character, meta, a));
            sb.append('\n');
        }

        System.out.println(sb.toString());
    }

    private static String roll(Character character, MetaInfo meta, Feature feature) {
        int froll = 0;

        final StringBuilder sb = new StringBuilder();
        for (Feature f : feature.feats) {
            final int fr = Dices.feature(character.features.get(f) + meta.modifiers.feat(f));
            froll += fr;
            sb.append(" + df(")
                    .append(character.features.get(f))
                    .append('+')
                    .append(meta.modifiers.feat(f))
                    .append(")=")
                    .append(fr);
        }
        sb.delete(0, 3); // remove " + "

        froll /= feature.feats.length;
        final int roll = Dices.randRound(froll);
        final int result = roll + meta.modifiers.rollMod;

        return String.format(
                "%s# randRound((%s/%d)=%d)=%d + mod:%d = %d",
                feature.tr(), sb.toString(), feature.feats.length, froll, roll, meta.modifiers.rollMod, result
        );
    }

    private static String roll(Character character, MetaInfo meta, FightAction action) {
        final List<Double> factors = Aorta.DEFINES.rules().modifiers.get(action);
        double featSum = 0;

        final StringBuilder sb = new StringBuilder();
        for (Feature f : Feature.mainFeatures()) {
            final double df = Dices.feature(character.features.get(f) + meta.modifiers.feat(f));
            final double fr = df * factors.get(f.ordinal());
            featSum += fr;
            sb.append(" + (df(")
                    .append(character.features.get(f))
                    .append('+')
                    .append(meta.modifiers.feat(f))
                    .append(")=")
                    .append(df)
                    .append("*")
                    .append(factors.get(f.ordinal()))
                    .append(")=")
                    .append(fr);
        }
        sb.delete(0, 3); // remove " + "

        final int roll = (int) Math.floor(featSum);
        final int result = roll + meta.modifiers.rollMod;

        return String.format(
                "%s# floor(%s)=%d + mod:%d = %d",
                action.tr(), sb.toString(), roll, meta.modifiers.rollMod, result
        );
    }
}
