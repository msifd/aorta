package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.SizePolicy;

public class Icon extends Widget {
    protected Part iconPart;

    public Icon(String partName) {
        iconPart = Mellow.THEME.parts.get(partName);
        setSizeHint(iconPart.size);
        setSizePolicy(SizePolicy.Policy.MAXIMUM, SizePolicy.Policy.MAXIMUM);
    }
}
