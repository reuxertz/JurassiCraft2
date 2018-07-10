package org.jurassicraft.server.json.dinosaur.entity;

import com.google.common.collect.Maps;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.world.World;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.DinosaurEntity;

import java.util.Map;
import java.util.function.BiFunction;

public class EntityDinosaurJsonHandler {
    public static final Map<String, BiFunction<World, Dinosaur, DinosaurEntity>> TYPE_MAP = Maps.newHashMap();
    public static final Map<String, IAttribute> ATTRIBUTE_MAP = Maps.newHashMap();

    static {
        TYPE_MAP.put("Dinosaur", DinosaurEntity::new);

        ATTRIBUTE_MAP.put("max_health", SharedMonsterAttributes.MAX_HEALTH);
        ATTRIBUTE_MAP.put("follow_range", SharedMonsterAttributes.FOLLOW_RANGE);
        ATTRIBUTE_MAP.put("knockback_resistance", SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
        ATTRIBUTE_MAP.put("movement_speed", SharedMonsterAttributes.MOVEMENT_SPEED);
        ATTRIBUTE_MAP.put("flying_speed", SharedMonsterAttributes.FLYING_SPEED);
        ATTRIBUTE_MAP.put("attack_damage", SharedMonsterAttributes.ATTACK_DAMAGE);
        ATTRIBUTE_MAP.put("attack_speed", SharedMonsterAttributes.ATTACK_SPEED);
        ATTRIBUTE_MAP.put("armor", SharedMonsterAttributes.ARMOR);
        ATTRIBUTE_MAP.put("armor_toughness", SharedMonsterAttributes.ARMOR_TOUGHNESS);
        ATTRIBUTE_MAP.put("luck", SharedMonsterAttributes.LUCK);    }
}
