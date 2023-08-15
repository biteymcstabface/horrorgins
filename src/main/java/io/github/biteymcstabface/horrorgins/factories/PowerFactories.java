package io.github.biteymcstabface.horrorgins.factories;

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.registry.Registry;

public class PowerFactories {

    public static void register(){
        register(HideNametagPower.createFactory());
        register(WolfFormPower.createFactory());
        register(WolfHowlPower.createFactory());
        register(ScalePower.createFactory());
    }

    private static void register(PowerFactory<?> powerFactory){
        Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
    }
}
