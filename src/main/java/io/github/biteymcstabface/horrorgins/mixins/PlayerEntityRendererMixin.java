package io.github.biteymcstabface.horrorgins.mixins;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.biteymcstabface.horrorgins.Horrorgins;
import io.github.biteymcstabface.horrorgins.factories.WolfFormPower;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity,
        PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx,
                                     PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }
    @Redirect( //Starts the render redirect - I assume it makes the game execute the following method instead of the target
            method="render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    )
    private void renderWolf(LivingEntityRenderer renderer,
                            LivingEntity entity, float f, float g, MatrixStack matrixStack,
                            VertexConsumerProvider vertexConsumerProvider, int i){
        if(PowerHolderComponent.hasPower(entity, WolfFormPower.class)) {
            WolfFormPower power = PowerHolderComponent.getPowers(entity, WolfFormPower.class).get(0);
            if(power.isActive()) {
                WolfEntity wolfEntity = power.wolfEntity;
                //I believe this chunk sets the physical aspects of the wolfEntity to those of the player
                wolfEntity.lastLimbDistance = entity.lastLimbDistance;
                wolfEntity.limbDistance = entity.limbDistance;
                wolfEntity.limbAngle = entity.limbAngle;
                wolfEntity.handSwinging = entity.handSwinging;
                wolfEntity.handSwingTicks = entity.handSwingTicks;
                wolfEntity.lastHandSwingProgress = entity.lastHandSwingProgress;
                wolfEntity.handSwingProgress = entity.handSwingProgress;
                wolfEntity.bodyYaw = entity.bodyYaw;
                wolfEntity.prevBodyYaw = entity.prevBodyYaw;
                wolfEntity.headYaw = entity.headYaw;
                wolfEntity.prevHeadYaw = entity.prevHeadYaw;
                wolfEntity.age = entity.age;
                wolfEntity.preferredHand = entity.preferredHand;
                wolfEntity.setOnGround(entity.isOnGround());
                wolfEntity.setVelocity(entity.getVelocity());
                //next two make the wolfEntity tamed (with the player themselves as the owner),
                //and their collar colour to light gray (which is harder to see on the wolf model)
                wolfEntity.setOwner((PlayerEntity) entity);
                wolfEntity.setCollarColor(DyeColor.LIGHT_GRAY);
                //These next two make the wolf use the angry wolf texture
                wolfEntity.setAngerTime(100);
                wolfEntity.setAngryAt(entity.getUuid());

                WolfEntityRenderer entityRenderer = (WolfEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(wolfEntity);
                entityRenderer.render(wolfEntity, f, g, matrixStack, vertexConsumerProvider, i);
            } else{
                super.render((AbstractClientPlayerEntity) entity, f, g, matrixStack, vertexConsumerProvider, i);
            }
        } else{
            super.render((AbstractClientPlayerEntity) entity, f, g, matrixStack, vertexConsumerProvider, i);
        }

    }
    //error (singular) out the ass
    @Inject(method = "renderArm", at = @At("HEAD"), cancellable = true)
    private void renderWolfArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci) {
        if(PowerHolderComponent.hasPower(player, WolfFormPower.class)) {
            WolfFormPower power = PowerHolderComponent.getPowers(player, WolfFormPower.class).get(0);
            if(power.isActive()) {
                PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel = (PlayerEntityModel) this.getModel();
                ((PlayerEntityRenderer) (Object) this).setModelPose(player);
                playerEntityModel.handSwingProgress = 0.0F;
                playerEntityModel.sneaking = false;
                playerEntityModel.leaningPitch = 0.0F;
                playerEntityModel.setAngles(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
                arm.pitch = 0.0F;
                arm.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(Horrorgins.identifier("textures/entity/wolf.png"))), light, OverlayTexture.DEFAULT_UV);
                sleeve.pitch = 0.0F;
                sleeve.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(Horrorgins.identifier("textures/entity/wolf.png"))), light, OverlayTexture.DEFAULT_UV);
                ci.cancel();
            }
        }
    }
}
