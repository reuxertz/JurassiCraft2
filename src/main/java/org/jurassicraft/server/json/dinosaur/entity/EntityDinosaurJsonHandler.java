package org.jurassicraft.server.json.dinosaur.entity;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.dinosaur.DinosaurEntity;
import org.jurassicraft.server.entity.SwimmingDinosaurEntity;
import org.jurassicraft.server.entity.ai.LeapingMeleeEntityAI;
import org.jurassicraft.server.entity.ai.RaptorLeapEntityAI;

import java.util.Map;
import java.util.function.Function;

public class EntityDinosaurJsonHandler {
    public static final Map<String, Function<World, DinosaurEntity>> TYPE_MAP = Maps.newHashMap();
    public static final Map<String, IAttribute> ATTRIBUTE_MAP = Maps.newHashMap();
    public static final Map<String, Function<JsonObject, Function<DinosaurEntity, EntityAIBase>>> TASK_MAP = Maps.newHashMap();

    static {
        TYPE_MAP.put("dinosaur", DinosaurEntity::new);
        TYPE_MAP.put("swimming_dinosaur", SwimmingDinosaurEntity::new);

        TASK_MAP.put("leap", json -> RaptorLeapEntityAI::new);
        TASK_MAP.put("leaping_melee", json -> dino -> new LeapingMeleeEntityAI(dino, dino.getDinosaur().attackSpeed));

        ATTRIBUTE_MAP.put("max_health", SharedMonsterAttributes.MAX_HEALTH);
        ATTRIBUTE_MAP.put("follow_range", SharedMonsterAttributes.FOLLOW_RANGE);
        ATTRIBUTE_MAP.put("knockback_resistance", SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
        ATTRIBUTE_MAP.put("movement_speed", SharedMonsterAttributes.MOVEMENT_SPEED);
        ATTRIBUTE_MAP.put("flying_speed", SharedMonsterAttributes.FLYING_SPEED);
        ATTRIBUTE_MAP.put("attack_damage", SharedMonsterAttributes.ATTACK_DAMAGE);
        ATTRIBUTE_MAP.put("attack_speed", SharedMonsterAttributes.ATTACK_SPEED);
        ATTRIBUTE_MAP.put("armor", SharedMonsterAttributes.ARMOR);
        ATTRIBUTE_MAP.put("armor_toughness", SharedMonsterAttributes.ARMOR_TOUGHNESS);
        ATTRIBUTE_MAP.put("luck", SharedMonsterAttributes.LUCK);
    }
}
