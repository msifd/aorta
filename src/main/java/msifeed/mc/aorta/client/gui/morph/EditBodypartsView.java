package msifeed.mc.aorta.client.gui.morph;

import msifeed.mc.aorta.core.character.BodyPart;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.button.FlatButtonLabel;
import msifeed.mc.mellow.widgets.text.Label;

import java.util.stream.Stream;

class EditBodypartsView extends Widget {
    private final Character character;

    private final Widget bodypartList = new Widget();

    EditBodypartsView(Character character) {
        this.character = character;

        setLayout(ListLayout.VERTICAL);

        bodypartList.setLayout(ListLayout.VERTICAL);
        addChild(bodypartList);

        addChild(new Separator());
        addEditButtons();

        refill();
    }

    private void addEditButtons() {
        final ButtonLabel addPartBtn = new ButtonLabel("Add part");
        addPartBtn.setClickCallback(() -> getTopParent().addChild(new EditBodypartDialog(this, bp -> {
            bp.health = bp.maxHealth;
            character.bodyParts.put(bp.name, bp);
            refill();
        })));
        addChild(addPartBtn);
    }

    @Override
    public void refill() {
        bodypartList.clearChildren();

        if (character.bodyParts.isEmpty()) {
            bodypartList.addChild(new Label("No bodyparts! Ha-Ha!"));
            final ButtonLabel addDefault = new ButtonLabel("Add default parts");
            addDefault.setClickCallback(() -> {
                addDefaultBodyparts();
                refill();
            });
            bodypartList.addChild(addDefault);
            return;
        }

        for (BodyPart bp : character.bodyParts.values()) {
            final FlatButtonLabel btn = new FlatButtonLabel();
            btn.setLabel(bp.toString());
            btn.setClickCallback(() -> getTopParent().addChild(new EditBodypartDialog(this, bp, newBp -> {
                if (newBp != null)
                    character.bodyParts.put(newBp.name, newBp);
                if (newBp == null || !bp.name.equals(newBp.name))
                    character.bodyParts.remove(bp.name);
                refill();
            })));
            bodypartList.addChild(btn);
        }
    }

    private void addDefaultBodyparts() {
        Stream.of(
                new BodyPart("голова", 12, true),
                new BodyPart("тело", 30, true),
                new BodyPart("левая рука", 15, false),
                new BodyPart("правая рука", 15, false),
                new BodyPart("левая нога", 15, false),
                new BodyPart("правая нога", 15, false)
        ).forEach(bp -> {
            character.bodyParts.put(bp.name, bp);
        });
    }
}
