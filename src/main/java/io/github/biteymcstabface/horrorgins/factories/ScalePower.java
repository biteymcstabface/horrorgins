package io.github.biteymcstabface.horrorgins.factories;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.biteymcstabface.horrorgins.Horrorgins;
import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

public class ScalePower extends Power {

    ScaleData playerWidth;
    ScaleData playerModelHeight;
    ScaleData playerStepHeight;

    public ScalePower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
        this.setTicking(true);
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void tick() {
        playerModelHeight = ScaleTypes.MODEL_HEIGHT.getScaleData(entity);
        playerStepHeight = ScaleTypes.STEP_HEIGHT.getScaleData(entity);
        playerWidth = ScaleTypes.WIDTH.getScaleData(entity);
        if(isActive()) {
            playerModelHeight.setTargetScale(2f);
            playerWidth.setTargetScale(2f);
            playerStepHeight.setTargetScale(2f);
        } else {
            playerModelHeight.setTargetScale(1f);
            playerStepHeight.setTargetScale(1f);
            playerWidth.setTargetScale(1f);
        }
    }

    @Override
    public void onRemoved() {
        playerWidth = ScaleTypes.WIDTH.getScaleData(entity);
        playerModelHeight = ScaleTypes.MODEL_HEIGHT.getScaleData(entity);
        if(playerWidth.getScale() != 1) {
            playerModelHeight.setTargetScale(1);
            playerStepHeight.setTargetScale(1f);
            playerWidth.setTargetScale(1);
        }
    }

    public static PowerFactory<?> createFactory() {
        return new PowerFactory<>(Horrorgins.identifier("scale_power"),
                new SerializableData(),
                data ->
                        (type, entity) -> new ScalePower(type, entity)).allowCondition();
    }
}
