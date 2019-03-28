package msifeed.mc.aorta.economy.data;

import java.util.*;

public class Blueprint {
    public int quality = 0;
    public Set<ScienceTrait> traits = EnumSet.noneOf(ScienceTrait.class);
    public Set<Facility> facilities = EnumSet.noneOf(Facility.class);
    public Map<Resource, Integer> resources = new EnumMap<>(Resource.class);
    public int time;
}
