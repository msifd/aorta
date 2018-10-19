package msifeed.mc.aorta.client.gui.chareditor;

import msifeed.mc.aorta.core.character.BodyPart;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.button.FlatButtonLabel;

import java.util.stream.Stream;

public class BodypartManageView extends Widget {
    private final Character character;

    private final Widget bodypartList = new Widget();

    BodypartManageView(Character character) {
        this.character = character;

        setLayout(ListLayout.VERTICAL);

        bodypartList.setLayout(ListLayout.VERTICAL);
        addChild(bodypartList);

        addChild(new Separator());
        addEditButtons();

        refillList();
    }

    private void addEditButtons() {
        final ButtonLabel addPartBtn = new ButtonLabel("Add part");
        addPartBtn.setClickCallback(() -> {
            getTopParent().addChild(new EditBodypartDialog(bp -> {
                character.bodyParts.put(bp.name, bp);
                refillList();
            }));
        });
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

        for (BodyPart bp : character.bodyParts.values()) {
            final FlatButtonLabel b = new FlatButtonLabel();
            b.setLabel(bp.toLineString());
            b.setClickCallback(() -> {
                getTopParent().addChild(new EditBodypartDialog(bp, nbp -> {
                    character.bodyParts.remove(bp.name);
                    if (nbp != null)
                        character.bodyParts.put(nbp.name, nbp);
                    refillList();
                }));
            });
            bodypartList.addChild(b);
        }
    }

    private void addDefaultBodyparts() {
        Stream.of(
                new BodyPart("head", BodyPart.Type.HEAD, 8, 4, true),
                new BodyPart("body", BodyPart.Type.BODY, 20, 5, true),
                new BodyPart("lhand", BodyPart.Type.HAND, 10, 3, false),
                new BodyPart("rhand", BodyPart.Type.HAND, 10, 3, false),
                new BodyPart("lleg", BodyPart.Type.LEG, 12, 4, false),
                new BodyPart("rleg", BodyPart.Type.LEG, 12, 4, false)
        ).forEach(bp -> character.bodyParts.put(bp.name, bp));
    }
}
