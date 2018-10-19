package msifeed.mc.aorta.client.gui.fighter;

import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.character.BodyPart;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.button.FlatButtonLabel;
import net.minecraft.entity.EntityLivingBase;

import java.util.Optional;

class BodypartHealthView extends Widget {
    private final ScreenFightHelper parent;

    private final EntityLivingBase entity;
    private Widget bodypartList = new Widget();

    BodypartHealthView(ScreenFightHelper parent) {
        this.parent = parent;
        this.entity = parent.entity;
        setLayout(ListLayout.VERTICAL);

        final Optional<Character> characterOpt = CharacterAttribute.get(entity);
        if (!characterOpt.isPresent()) {
            addChild(new Label("Missing character data!"));
            return;
        }
        final Character character = characterOpt.get();

        bodypartList.setLayout(ListLayout.VERTICAL);
        addChild(bodypartList);
        refillBodyparts(character);
    }

    private void refillBodyparts(Character character) {
        bodypartList.clearChildren();

        if (character.bodyParts.isEmpty()) {
            bodypartList.addChild(new Label("No bodyparts! Ha-Ha!"));
            return;
        }

        for (BodyPart bp : character.bodyParts.values()) {
            final FlatButtonLabel b = new FlatButtonLabel();
            b.setLabel(bp.toLineString());
//            b.setClickCallback(() -> {
//                getTopParent(this).addChild(new EditBodypartDialog(bp, nbp -> {
//                    character.bodyParts.remove(bp.name);
//                    if (nbp != null)
//                        character.bodyParts.put(nbp.name, nbp);
//                    updateCharacter(character);
//                }));
//            });
            bodypartList.addChild(b);
        }
    }

    private void updateCharacter(Character character) {
        CharacterAttribute.INSTANCE.set(entity, character);
        refillBodyparts(character);
    }
}
