package msifeed.mc.aorta.economy.data;

import msifeed.mc.aorta.core.traits.Trait;

public enum ScienceTrait {
    melee_weapons(Trait.sci_melee_weapons),
    ranged_weapons(Trait.sci_ranged_weapons),
    armor(Trait.sci_armor),
    chemistry(Trait.sci_chemistry),
    biology(Trait.sci_biology),
    physics(Trait.sci_physics),
    computers(Trait.sci_computers),
    implants(Trait.sci_implants),
    engineer(Trait.sci_engineer),
    materials(Trait.sci_materials),
    psionics(Trait.sci_psionics),
    optimisation(Trait.sci_optimisation),
    ;

    public final Trait trait;

    ScienceTrait(Trait t) {
        trait = t;
    }
}
