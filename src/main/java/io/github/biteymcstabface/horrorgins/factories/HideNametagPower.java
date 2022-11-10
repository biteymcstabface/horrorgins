package io.github.biteymcstabface.horrorgins.factories;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.biteymcstabface.horrorgins.Horrorgins;
import net.minecraft.entity.LivingEntity;

public class HideNametagPower extends Power {
    public HideNametagPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }
    public static PowerFactory<?> createFactory() {
        return new PowerFactory<>(Horrorgins.identifier("hide_nametag"),
                new SerializableData(),
                data -> HideNametagPower::new).allowCondition();
    }
}
