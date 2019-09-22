package msifeed.mc.aorta.client.gui.roller;

import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.Checkbox;
import msifeed.mc.mellow.widgets.button.FlatButtonLabel;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;

class TargetView extends Widget {
    private final Checkbox.Group targetGroup = new Checkbox.Group();
    private TargetChoice targetChoice;

    TargetView() {
        getSizeHint().x = 120;
        setLayout(ListLayout.VERTICAL);

        addChild(new Label("Атаковать в ..."));
        addChild(new Separator());

        final Widget targetBtns = new Widget();
        targetBtns.setLayout(new GridLayout(1));
        addChild(targetBtns);

        targetBtns.addChild(new TargetChoiceButton("голову"));
        targetBtns.addChild(new TargetChoiceButton("тело"));
        targetBtns.addChild(new TargetChoiceButton("левую руку"));
        targetBtns.addChild(new TargetChoiceButton("правую руку"));
        targetBtns.addChild(new TargetChoiceButton("левую ногу"));
        targetBtns.addChild(new TargetChoiceButton("правую ногу"));

        addChild(targetChoice = new TargetChoiceInput());

        targetChoice.cb.setChecked(true);
    }

    public String getTarget() {
        return targetChoice.toString();
    }

    abstract class TargetChoice extends Widget {
        final Checkbox cb = new Checkbox(targetGroup);

        TargetChoice() {
            setLayout(ListLayout.HORIZONTAL);
            cb.getPos().y = 3;
            cb.setCallback(checked -> {
                if (checked)
                    targetChoice = this;
            });
            addChild(cb);
        }
    }

    private class TargetChoiceButton extends TargetChoice {
        final FlatButtonLabel btn;

        TargetChoiceButton(String label) {
            btn = new FlatButtonLabel();
            btn.setLabel(label);
            btn.getSizeHint().x = 30;
            btn.setClickCallback(() -> cb.setChecked(true));
            addChild(btn);
        }

        @Override
        public String toString() {
            return btn.getLabel();
        }
    }

    private class TargetChoiceInput extends TargetChoice {
        final TextInput input = new TextInput();
        TargetChoiceInput() {
            input.getSizeHint().x = 60;
            input.setPlaceholderText("другое");
            addChild(input);
        }

        @Override
        public String toString() {
            return input.getText();
        }
    }
}
