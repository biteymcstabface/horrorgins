package io.github.biteymcstabface.horrorgins.mixins;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.biteymcstabface.horrorgins.factories.HideNametagPower;
import io.github.biteymcstabface.horrorgins.utils.DimensionsRefresher;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow private EntityDimensions dimensions;

    @Shadow public abstract EntityPose getPose();

    @Shadow public abstract EntityDimensions getDimensions(EntityPose pose);

    @Shadow private float standingEyeHeight;

    @Shadow protected abstract float getEyeHeight(EntityPose pose, EntityDimensions dimensions);

    @Shadow public abstract Box getBoundingBox();

    @Shadow public abstract void setBoundingBox(Box boundingBox);

    @Shadow protected boolean firstUpdate;

    @Shadow public abstract void move(MovementType movementType, Vec3d movement);

    @Shadow protected abstract void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition);

    @Inject(method = "isSneaky", at = @At("HEAD"), cancellable = true)
    private void hideNametag(CallbackInfoReturnable<Boolean> cir){
        Entity entity = (Entity) (Object) this;
        if(PowerHolderComponent.hasPower(entity, HideNametagPower.class)){
            List<HideNametagPower> powers = PowerHolderComponent.getPowers(entity, HideNametagPower.class);
            boolean active = powers.stream().anyMatch(Power::isActive);
            cir.setReturnValue(active);
        }
    }


    public void refreshDimensions() {
        EntityDimensions currentDimensions = this.dimensions;
        EntityPose entityPose = this.getPose();
        EntityDimensions newDimensions = this.getDimensions(entityPose);

        this.dimensions = newDimensions;
        this.standingEyeHeight = this.getEyeHeight(entityPose, newDimensions);

        Box box = this.getBoundingBox();
        this.setBoundingBox(new Box(box.minX, box.minY, box.minZ, box.minX + (double) newDimensions.width,
                box.minY + (double) newDimensions.height, box.minZ + (double) newDimensions.width));

        if(!this.firstUpdate) {
            float f = currentDimensions.width- newDimensions.width;
            this.move(MovementType.SELF, new Vec3d(f, 0.0d, f));
        }
    }
}
