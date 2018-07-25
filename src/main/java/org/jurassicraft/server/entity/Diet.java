package org.jurassicraft.server.entity;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import org.jurassicraft.server.food.FoodHelper;
import org.jurassicraft.server.food.FoodType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class Diet {
    public static final Supplier<Diet> CARNIVORE = () -> new Diet()
            .withModule(new DietModule(FoodType.MEAT))
            .withModule(new DietModule(FoodType.INSECT)
                    .withCondition(DietCondition.INFANT));
    public static final Supplier<Diet> HERBIVORE = () -> new Diet()
            .withModule(new DietModule(FoodType.PLANT));
    public static final Supplier<Diet> INSECTIVORE = () -> new Diet()
            .withModule(new DietModule(FoodType.INSECT));
    public static final Supplier<Diet> PISCIVORE = () -> new Diet()
            .withModule(new DietModule(FoodType.FISH));

    private List<DietModule> modules = new ArrayList<>();

    public Diet withModule(DietModule module) {
        this.modules.add(module);
        return this;
    }

    public List<DietModule> getModules() {
        return this.modules;
    }

    public boolean canEat(DinosaurEntity entity, FoodType foodType) {
        for (DietModule module : this.modules) {
            if (module.applies(entity) && module.getFoodType() == foodType) {
                return true;
            }
        }
        return false;
    }

    public static class DietModule {
        private List<DietCondition> conditions = Lists.newArrayList();
        private FoodType type;

        public DietModule(FoodType type) {
            this.type = type;
        }

        public DietModule withCondition(DietCondition condition) {
            this.conditions.add(condition);
            return this;
        }

        public boolean canEat(DinosaurEntity entity, Item item) {
            return this.runConditions(entity) && FoodHelper.getFoodType(item) == type;
        }

        public boolean runConditions(DinosaurEntity entity) {
            for (DietCondition condition : conditions) {
                if(!condition.apply(entity)) {
                    return false;
                }
            }
            return true;
        }

        public Collection<DietCondition> getTypes() {
            return Collections.unmodifiableCollection(this.conditions);
        }

        public FoodType getFoodType() {
            return this.type;
        }

        public boolean applies(DinosaurEntity entity) {
            return this.runConditions(entity);
        }
    }
}