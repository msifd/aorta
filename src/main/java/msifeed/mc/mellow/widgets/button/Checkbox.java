package msifeed.mc.mellow.widgets.button;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.SizePolicy;

import java.util.function.Consumer;

public class Checkbox extends Button {
    private final Part offPart = Mellow.getPart("checkbox_off");
    private final Part onPart = Mellow.getPart("checkbox_on");

    private boolean checked = false;
    private Consumer<Boolean> onChange = b -> {};

    public Checkbox() {
        setSizeHint(offPart.size.x, offPart.size.y);
        setVerSizePolicy(SizePolicy.Policy.FIXED);
        setZLevel(1);
    }

    public Checkbox(boolean checked) {
        this();
        setChecked(checked);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            this.checked = checked;
            this.onChange.accept(checked);
        }
    }

    public void setCallback(Consumer<Boolean> onChange) {
        this.onChange = onChange;
    }

    @Override
    protected void renderSelf() {
        RenderParts.slice(isChecked() ? onPart : offPart, getGeometry());
    }

    @Override
    public void onClick(int xMouse, int yMouse, int button) {
        if (!isDisabled())
            setChecked(!isChecked());
    }
}
