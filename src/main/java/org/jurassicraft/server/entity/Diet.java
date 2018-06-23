package org.jurassicraft.server.entity;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import org.jurassicraft.server.food.FoodHelper;
import org.jurassicraft.server.food.FoodType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Diet {
    public static final Supplier<Diet> CARNIVORE = () -> new Diet()
            .withModule(new DietModule(FoodType.MEAT))
            .withModule(new DietModule(FoodType.INSECT)
                    .withCondition(DietConditionType.INFANT));
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
        private List<Predicate<DinosaurEntity>> conditions = Lists.newArrayList();
        private FoodType type;

        public DietModule(FoodType type) {
            this.type = type;
        }

        public DietModule withCondition(Predicate<DinosaurEntity> condition) {
            this.conditions.add(condition);
            return this;
        }

        public boolean canEat(DinosaurEntity entity, Item item) {
            return this.runConditions(entity) && FoodHelper.getFoodType(item) == type;
        }

        public boolean runConditions(DinosaurEntity entity) {
            for (Predicate<DinosaurEntity> condition : conditions) {
                if(!condition.test(entity)) {
                    return false;
                }
            }
            return true;
        }

        public List<DietConditionType> getTypes() {
            List<DietConditionType> types = Lists.newArrayList();
            for (Predicate<DinosaurEntity> condition : conditions) {
                for (DietConditionType dietConditionType : DietConditionType.values()) {
                    if(condition == dietConditionType) {
                        types.add(dietConditionType);
                    }
                }
            }
            return types;
        }

        public FoodType getFoodType() {
            return this.type;
        }

        public boolean applies(DinosaurEntity entity) {
            return this.runConditions(entity);
        }
    }
}