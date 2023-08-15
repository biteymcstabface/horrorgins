package io.github.biteymcstabface.horrorgins.factories;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.ActiveCooldownPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.biteymcstabface.horrorgins.Horrorgins;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class WolfHowlPower extends ActiveCooldownPower{
    public List<WolfEntity> wolves = Lists.newArrayList(); //creates an arraylist called Wolves that stores Wolf Entities
    public WolfHowlPower(PowerType<?> type, LivingEntity entity, int cooldownDuration, HudRender hudRender) {
        super(type, entity, cooldownDuration, hudRender, null);
    }

    @Override
    public void onUse() {
        if(canUse()) {
            //plays the wolf howl sound
            entity.playSound(SoundEvents.ENTITY_WOLF_HOWL, 1f, 1f);
            //summon wolves
            //loops while the number of items in the  wolves arraylist is less than 3
            while(wolves.size() < 3){
                //creates a wolf entity called e
                WolfEntity e = EntityType.WOLF.create(entity.getWorld());
                //if e exists
                if(e != null) {
                    //sets the player who used the power as the owner (making the wolf tamed)
                    e.setOwner((PlayerEntity) entity);
                    //sets the wolf positions to that of the player
                    e.refreshPositionAndAngles(entity.getPos().x, entity.getPos().y, entity.getPos().z, entity.getYaw(), entity.getPitch());
                    //spawns the wolf
                    entity.getWorld().spawnEntity(e);
                    //adds the new wolf (e) to the arraylist
                    wolves.add(e);
                }
            }
            use(); //activates the power
        }
    }

    public static PowerFactory createFactory() {
        return new PowerFactory<>(Horrorgins.identifier("wolf_howl"),
                new SerializableData()
                        .add("cooldown", SerializableDataTypes.INT, 1)
                        .add("hud_render", ApoliDataTypes.HUD_RENDER, HudRender.DONT_RENDER)
                        .add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, new Active.Key()),
                data ->
                        (type, player) -> {
                            WolfHowlPower power = new WolfHowlPower(type, player, data.getInt("cooldown"),
                                    (HudRender)data.get("hud_render"));
                            power.setKey((Active.Key)data.get("key"));
                            return power;
                        })
                .allowCondition();
    }
}
