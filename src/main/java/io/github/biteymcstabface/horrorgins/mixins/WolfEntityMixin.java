package io.github.biteymcstabface.horrorgins.mixins;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.biteymcstabface.horrorgins.factories.WolfFormPower;
import io.github.biteymcstabface.horrorgins.factories.WolfHowlPower;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.WolfEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(WolfEntity.class)
public class WolfEntityMixin {

    @Inject (method = "onDeath", at = @At("HEAD"))
    private void removeWolf(DamageSource damageSource, CallbackInfo ci){
        WolfEntity wolfEntity = (WolfEntity) (Object) this;
        if(PowerHolderComponent.hasPower(wolfEntity.getOwner(), WolfHowlPower.class)){
            List<WolfHowlPower> powers = PowerHolderComponent.getPowers(wolfEntity.getOwner(), WolfHowlPower.class);
            for (WolfHowlPower power: powers) {
                if(power.wolves.contains(wolfEntity)){
                    power.wolves.remove(wolfEntity);
                }
            }
        }
    }

    @Inject(method = "getTailAngle", at = @At("RETURN"), cancellable = true)
    private void getTailAngleFromPlayer (CallbackInfoReturnable < Float > cir) {
        WolfEntity wolfEntity = (WolfEntity) (Object) this;
        if (PowerHolderComponent.hasPower(wolfEntity.getOwner(), WolfFormPower.class)) {
            WolfFormPower power = PowerHolderComponent.getPowers(wolfEntity.getOwner(), WolfFormPower.class).get(0);
            if (wolfEntity == power.wolfEntity) {
                if (power.isActive()) {
                    cir.setReturnValue((0.55F - (wolfEntity.getOwner().getMaxHealth() - wolfEntity.getOwner().getHealth()) * 0.02F) * 3.1415927F);
                }
            }
        }
    }
}