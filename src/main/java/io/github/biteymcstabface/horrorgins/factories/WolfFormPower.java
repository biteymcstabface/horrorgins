package io.github.biteymcstabface.horrorgins.factories;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.biteymcstabface.horrorgins.Horrorgins;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;

public class WolfFormPower extends Power {
    private boolean isActive = false;
    private final ActionFactory<Entity>.Instance action;
    public WolfEntity wolfEntity;

    public WolfFormPower(PowerType<?> type, LivingEntity entity, ActionFactory<Entity>.Instance action) {
        super(type, entity);
        this.action = action;
        this.setTicking();
        this.wolfEntity =  EntityType.WOLF.create(entity.world);
    }

    @Override
    public void onAdded() {

    }

    @Override
    public boolean isActive() {
        boolean wasActive = isActive;
        isActive = conditions.stream().allMatch(condition -> condition.test(entity));
        if(wasActive != isActive){
            //create smoke
            this.action.accept(entity);
        }
        return isActive;

    }
    public static PowerFactory<?> createFactory() {
        return new PowerFactory<>(Horrorgins.identifier("wolf_form"),
                new SerializableData()
                        .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null),
                data ->
                (powerType, livingEntity) -> {
                    ActionFactory<Entity>.Instance action = data.get("entity_action");
                    return new WolfFormPower(powerType, livingEntity, action);
                }).allowCondition();
    }
}
