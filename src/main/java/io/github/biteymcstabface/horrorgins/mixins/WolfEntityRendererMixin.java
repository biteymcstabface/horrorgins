package io.github.biteymcstabface.horrorgins.mixins;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.biteymcstabface.horrorgins.factories.WolfFormPower;
import net.minecraft.client.render.entity.WolfEntityRenderer;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WolfEntityRenderer.class)
public class WolfEntityRendererMixin {

    @Shadow @Final private static Identifier ANGRY_TEXTURE;

    @Inject(method = "getTexture*", at = @At(value = "RETURN",
            target = "Lnet/minecraft/client/render/entity/WolfEntityRenderer;getTexture(Lnet/minecraft/entity/passive/WolfEntity;)Lnet/minecraft/util/Identifier;"
            ),
            cancellable = true)
    private void setPlayerWolfTexture (WolfEntity wolfEntity, CallbackInfoReturnable<Identifier> cir) {
        if (PowerHolderComponent.hasPower(wolfEntity.getOwner(), WolfFormPower.class)) {
            WolfFormPower power = PowerHolderComponent.getPowers(wolfEntity.getOwner(), WolfFormPower.class).get(0);
            if (wolfEntity == power.wolfEntity) {
                if (power.isActive()) {
                    cir.setReturnValue(ANGRY_TEXTURE);
                }
            }
        }
    }
}
