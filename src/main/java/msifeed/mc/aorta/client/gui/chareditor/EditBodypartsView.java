package msifeed.mc.aorta.client.gui.chareditor;

import msifeed.mc.aorta.core.character.BodyPart;
import msifeed.mc.aorta.core.character.BodyPartHealth;
import msifeed.mc.aorta.core.character.CharStatus;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.button.FlatButtonLabel;

import java.util.stream.Stream;

class EditBodypartsView extends Widget {
    private final Character character;
    private final CharStatus charStatus;

    private final Widget bodypartList = new Widget();

    EditBodypartsView(Character character, CharStatus charStatus) {
        this.character = character;
        this.charStatus = charStatus;

        setLayout(ListLayout.VERTICAL);

        bodypartList.setLayout(ListLayout.VERTICAL);
        addChild(bodypartList);

        addChild(new Separator());
        addEditButtons();

        refillList();
    }

    private void addEditButtons() {
        final ButtonLabel addPartBtn = new ButtonLabel("Add part");
        addPartBtn.setClickCallback(() -> getTopParent().addChild(new EditBodypartDialog(bp -> {
            character.bodyParts.put(bp.name, bp);
            charStatus.health.put(bp.name, new BodyPartHealth(bp.max, (short) 0));
            refillList();
        })));
        addChild(addPartBtn);
    }

    private void refillList() {
        bodypartList.clearChildren();

        if (character.bodyParts.isEmpty()) {
            bodypartList.addChild(new Label("No bodyparts! Ha-Ha!"));
            final ButtonLabel addDefault = new ButtonLabel("Add default parts");
            addDefault.setClickCallback(() -> {
                addDefaultBodyparts();
                refillList();
            });
            bodypartList.addChild(addDefault);
            return;
        }

        character.bodyParts.values().stream().sorted().forEach(bp -> {
            final FlatButtonLabel b = new FlatButtonLabel();
            b.setLabel(bp.toLineString());
            b.setClickCallback(() -> getTopParent().addChild(new EditBodypartDialog(bp, nbp -> {
                if (nbp != null) {
                    final BodyPartHealth prevBph = charStatus.health.getOrDefault(bp.name, new BodyPartHealth());
                    final BodyPartHealth nbph = new BodyPartHealth((short) Math.min(prevBph.health, nbp.max), prevBph.armor);
                    character.bodyParts.put(nbp.name, nbp);
                    charStatus.health.put(nbp.name, nbph);
                }
                if (nbp == null || !bp.name.equals(nbp.name)) {
                    character.bodyParts.remove(bp.name);
                    charStatus.health.remove(bp.name);
                }
                refillList();
            })));
            bodypartList.addChild(b);
        });
    }

    private void addDefaultBodyparts() {
        Stream.of(
                new BodyPart("head", BodyPart.Type.HEAD, 8, true),
                new BodyPart("body", BodyPart.Type.BODY, 20, true),
                new BodyPart("lhand", BodyPart.Type.HAND, 10, false),
                new BodyPart("rhand", BodyPart.Type.HAND, 10, false),
                new BodyPart("lleg", BodyPart.Type.LEG, 12, false),
                new BodyPart("rleg", BodyPart.Type.LEG, 12, false)
        ).forEach(bp -> {
            character.bodyParts.put(bp.name, bp);
            charStatus.health.put(bp.name, new BodyPartHealth(bp.max, (short) 0));
        });
    }
}
