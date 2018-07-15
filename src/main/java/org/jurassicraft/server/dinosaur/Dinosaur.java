package org.jurassicraft.server.dinosaur;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaCubeContainer;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.animation.PoseHandler;
import org.jurassicraft.server.api.GrowthStageGenderContainer;
import org.jurassicraft.server.api.Hybrid;
import org.jurassicraft.server.entity.Diet;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.GrowthStage;
import org.jurassicraft.server.entity.SleepTime;
import org.jurassicraft.server.entity.ai.util.MovementType;
import org.jurassicraft.server.json.dinosaur.DinosaurJsonHandler;
import org.jurassicraft.server.json.dinosaur.entity.EntityDinosaurJsonHandler;
import org.jurassicraft.server.json.dinosaur.entity.objects.EntityProperties;
import org.jurassicraft.server.period.TimePeriod;
import org.jurassicraft.server.tabula.TabulaModelHelper;

import javax.vecmathimpl.Matrix4d;
import javax.vecmathimpl.Vector3d;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Data
@Setter(value = AccessLevel.PROTECTED)
public class Dinosaur extends IForgeRegistryEntry.Impl<Dinosaur> implements Comparable<Dinosaur> {

    @GameRegistry.ObjectHolder(JurassiCraft.MODID + ":velociraptor")
    public static final Dinosaur MISSING = null;

    private final Map<GrowthStage, List<ResourceLocation>> overlays = new HashMap<>();
    private final Map<GrowthStage, ResourceLocation> maleTextures = new HashMap<>();
    private final Map<GrowthStage, ResourceLocation> femaleTextures = new HashMap<>();
    private final Map<GrowthStageGenderContainer, ResourceLocation> eyelidTextures = new HashMap<>();

    private String name;
    private Class<? extends DinosaurEntity> entityClass;
    @Deprecated
    private String animatorClassName;
    private DinosaurBehaviourType dinosaurBehaviourType;
    private DinosaurHomeType homeType;
    private int primaryEggColorMale, primaryEggColorFemale;
    private int secondaryEggColorMale, secondaryEggColorFemale;
    private TimePeriod timePeriod;
    private double babyHealth, adultHealth;
    private double babyStrength, adultStrength;
    private double babySpeed, adultSpeed;
    private float babySizeX, adultSizeX;
    private float babySizeY, adultSizeY;
    private float babyEyeHeight, adultEyeHeight;
    private double attackSpeed = 1.0;
    private int storage;
    private int overlayCount;
    private Diet diet;
    private SleepTime sleepTime = SleepTime.DIURNAL;
    private String[] bones;
    private int maximumAge;
    private String headCubeName;
    private float shadowSize;
    private MovementType movementType = MovementType.NEAR_SURFACE;
    private BirthType birthType = BirthType.EGG_LAYING;
    private boolean isImprintable;

    private int lipids = 1500;
    private int vitamins = 1500;
    private int minerals = 1500;
    private int proximates = 1500;

    private boolean randomFlock = true;

    private float scaleInfant;
    private float scaleAdult;

    private float offsetX;
    private float offsetY;
    private float offsetZ;

    private TabulaModelContainer modelAdult;
    private TabulaModelContainer modelInfant;
    private TabulaModelContainer modelJuvenile;
    private TabulaModelContainer modelAdolescent;
    private TabulaModelContainer modelSkeleton;

    private PoseHandler<?> poseHandler;

    private boolean defendOwner;

    private boolean flee;
    private double flockSpeed = 0.8;

    private double attackBias = 200.0;
    private int maxHerdSize = 32;

    private int spawnChance;
    private Biome[] spawnBiomes;
    private BiomeDictionary.Type[] biomeTypes;
    private boolean canClimb;

    private int breedCooldown;
    private boolean breedAroundOffspring;
    private int minClutch = 2;
    private int maxClutch = 6;
    private boolean defendOffspring;
    private int jumpHeight;

    private String[][] recipe;

    private String jawCubeName = "Lower Teeth Front"; //TODO: make json based

    @Setter(AccessLevel.NONE)
    private EntityProperties properties; //@

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private boolean init;

    public static Matrix4d getParentRotationMatrix(TabulaModelContainer model, TabulaCubeContainer cube, boolean includeParents, boolean ignoreSelf, float rot) {
        List<TabulaCubeContainer> parentCubes = new ArrayList<>();

        do {
            if (ignoreSelf) {
                ignoreSelf = false;
            } else {
                parentCubes.add(cube);
            }
        }
        while (includeParents && cube.getParentIdentifier() != null && (cube = TabulaModelHelper.getCubeByIdentifier(cube.getParentIdentifier(), model)) != null);

        Matrix4d mat = new Matrix4d();
        mat.setIdentity();
        Matrix4d transform = new Matrix4d();

        transform.rotY(rot / 180 * Math.PI);
        mat.mul(transform);

        for (int i = parentCubes.size() - 1; i >= 0; i--) {
            cube = parentCubes.get(i);
            transform.setIdentity();
            transform.setTranslation(new Vector3d(cube.getPosition()));
            mat.mul(transform);

            double rotX = cube.getRotation()[0];
            double rotY = cube.getRotation()[1];
            double rotZ = cube.getRotation()[2];

            transform.rotZ(rotZ / 180 * Math.PI);
            mat.mul(transform);
            transform.rotY(rotY / 180 * Math.PI);
            mat.mul(transform);
            transform.rotX(rotX / 180 * Math.PI);
            mat.mul(transform);
        }

        return mat;
    }

    private static double[][] getTransformation(Matrix4d matrix) {
        double sinRotationAngleY, cosRotationAngleY, sinRotationAngleX, cosRotationAngleX, sinRotationAngleZ, cosRotationAngleZ;

        sinRotationAngleY = -matrix.m20;
        cosRotationAngleY = Math.sqrt(1 - sinRotationAngleY * sinRotationAngleY);

        if (Math.abs(cosRotationAngleY) > 0.0001) {
            sinRotationAngleX = matrix.m21 / cosRotationAngleY;
            cosRotationAngleX = matrix.m22 / cosRotationAngleY;
            sinRotationAngleZ = matrix.m10 / cosRotationAngleY;
            cosRotationAngleZ = matrix.m00 / cosRotationAngleY;
        } else {
            sinRotationAngleX = -matrix.m12;
            cosRotationAngleX = matrix.m11;
            sinRotationAngleZ = 0;
            cosRotationAngleZ = 1;
        }

        double rotationAngleX = epsilon(Math.atan2(sinRotationAngleX, cosRotationAngleX)) / Math.PI * 180;
        double rotationAngleY = epsilon(Math.atan2(sinRotationAngleY, cosRotationAngleY)) / Math.PI * 180;
        double rotationAngleZ = epsilon(Math.atan2(sinRotationAngleZ, cosRotationAngleZ)) / Math.PI * 180;
        return new double[][] { { epsilon(matrix.m03), epsilon(matrix.m13), epsilon(matrix.m23) }, { rotationAngleX, rotationAngleY, rotationAngleZ } };
    }

    private static double epsilon(double x) {
        return x < 0 ? x > -0.0001 ? 0 : x : x < 0.0001 ? 0 : x;
    }

    public final void init() {
        if(init) {
           return;
        }
        this.init = true;

        try {
            this.properties = DinosaurJsonHandler.GSON.fromJson(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(this.getRegistryName().getResourceDomain(), "jurassicraft/entities/" + this.getRegistryName().getResourcePath() + ".json")).getInputStream()), EntityProperties.class);;
        } catch (IOException e) {
            JurassiCraft.getLogger().error("Unable to load dinosaur behaviours for " + this.getRegistryName(), e);
            this.properties = new EntityProperties("dinosaur", null, null, Lists.newArrayList(), null);
        }

        String formattedName = this.getRegistryName().getResourcePath();
        String domain = this.getRegistryName().getResourceDomain();

        for (GrowthStage stage : GrowthStage.VALUES) {
            if (this.doesSupportGrowthStage(stage)) {
                this.setModelContainer(stage, this.parseModel(stage.name().toLowerCase(Locale.ENGLISH)));
            } else {
                this.setModelContainer(stage, this.modelAdult);
            }
        }

        String baseTextures = "textures/entities/" + formattedName + "/";

        for (GrowthStage growthStage : GrowthStage.values()) {
            String growthStageName = growthStage.name().toLowerCase(Locale.ENGLISH);

            if (!this.doesSupportGrowthStage(growthStage)) {
                growthStageName = GrowthStage.ADULT.name().toLowerCase(Locale.ENGLISH);
            }

            if (this instanceof Hybrid) {
                String baseName = baseTextures + formattedName + "_" + growthStageName;

                ResourceLocation hybridTexture = new ResourceLocation(domain, baseName + ".png");

                this.maleTextures.put(growthStage, hybridTexture);
                this.femaleTextures.put(growthStage, hybridTexture);

                ResourceLocation eyelidTexture = new ResourceLocation(domain, baseName + "_eyelid.png");
                this.eyelidTextures.put(new GrowthStageGenderContainer(growthStage, false), eyelidTexture);
                this.eyelidTextures.put(new GrowthStageGenderContainer(growthStage, true), eyelidTexture);
            } else {
                this.maleTextures.put(growthStage, new ResourceLocation(domain, baseTextures + formattedName + "_male_" + growthStageName + ".png"));
                this.femaleTextures.put(growthStage, new ResourceLocation(domain, baseTextures + formattedName + "_female_" + growthStageName + ".png"));
                this.eyelidTextures.put(new GrowthStageGenderContainer(growthStage, true), new ResourceLocation(domain, baseTextures + formattedName + "_male_" + growthStageName + "_eyelid.png"));
                this.eyelidTextures.put(new GrowthStageGenderContainer(growthStage, false), new ResourceLocation(domain, baseTextures + formattedName + "_female_" + growthStageName + "_eyelid.png"));
            }

            List<ResourceLocation> overlaysForGrowthStage = new ArrayList<>();

            for (int i = 1; i <= this.getOverlayCount(); i++) {
                overlaysForGrowthStage.add(new ResourceLocation(domain, baseTextures + formattedName + "_overlay_" + growthStageName + "_" + i + ".png"));
            }

            this.overlays.put(growthStage, overlaysForGrowthStage);
        }

        this.poseHandler = new PoseHandler(this);
    }

    public DinosaurEntity createEntity(World world) {
        return EntityDinosaurJsonHandler.TYPE_MAP.getOrDefault(this.properties.getType(), DinosaurEntity::new).apply(world).setDinosaur(this);
    }

    protected void setDinosaurBehaviourType(DinosaurBehaviourType type) {
        this.dinosaurBehaviourType = type;
    }

    protected TabulaModelContainer parseModel(String growthStage) {
        String formattedName = this.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        String modelPath = "/assets/jurassicraft/models/entities/" + formattedName + "/" + growthStage + "/" + formattedName + "_" + growthStage + "_idle";

        try {
            return TabulaModelHelper.loadTabulaModel(modelPath);
        } catch (Exception e) {
            JurassiCraft.getLogger().fatal("Couldn't load model " + modelPath, e);
        }

        return null;
    }

    public void setBreeding(BirthType birthType, int minClutch, int maxClutch, int breedCooldown, boolean breedAroundOffspring, boolean defendOffspring) {
        this.birthType = birthType;
        this.minClutch = minClutch;
        this.maxClutch = maxClutch;
        this.breedCooldown = breedCooldown * 20 * 60;
        this.breedAroundOffspring = breedAroundOffspring;
        this.defendOffspring = defendOffspring;
    }

    public ResourceLocation getMaleTexture(GrowthStage stage) {
        return this.maleTextures.get(stage);
    }

    public ResourceLocation getFemaleTexture(GrowthStage stage) {
        return this.femaleTextures.get(stage);
    }

    protected String getDinosaurTexture(String subtype) {
        String dinosaurName = this.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

        String texture = "jurassicraft:textures/entities/" + dinosaurName + "/" + dinosaurName;

        if (subtype.length() > 0) {
            texture += "_" + subtype;
        }

        return texture + ".png";
    }

    protected int fromDays(int days) {
        return (days * 24000) / 8;
    }

    @Override
    public int compareTo(Dinosaur dinosaur) {
        return this.getName().compareTo(dinosaur.getName());
    }


    public ResourceLocation getOverlayTexture(GrowthStage stage, int overlay) {
        return this.overlays.containsKey(stage) ? this.overlays.get(stage).get(overlay) : null;
    }

    public ResourceLocation getEyelidTexture(DinosaurEntity entity) {
        return this.eyelidTextures.get(new GrowthStageGenderContainer(entity.getGrowthStage(), entity.isMale()));
    }

    @Override
    public int hashCode() {
        return this.getRegistryName().hashCode();
    }

    public double[] getCubePosition(String cubeName, GrowthStage stage) {
        TabulaModelContainer model = this.getModelContainer(stage);

        TabulaCubeContainer cube = TabulaModelHelper.getCubeByName(cubeName, model);

        if (cube != null) {
            return cube.getPosition();
        }

        return new double[] { 0.0, 0.0, 0.0 };
    }

    public double[] getParentedCubePosition(String cubeName, GrowthStage stage, float rot) {
        TabulaModelContainer model = this.getModelContainer(stage);

        TabulaCubeContainer cube = TabulaModelHelper.getCubeByName(cubeName, model);

        if (cube != null) {
            return getTransformation(getParentRotationMatrix(model, cube, true, false, rot))[0];
        }

        return new double[] { 0.0, 0.0, 0.0 };
    }

    public double[] getHeadPosition(GrowthStage stage, float rot) {
        return this.getParentedCubePosition(this.getHeadCubeName(), stage, rot);
    }

    public TabulaModelContainer getModelContainer(GrowthStage stage) {
        switch (stage) {
            case INFANT:
                return this.modelInfant;
            case JUVENILE:
                return this.modelJuvenile;
            case ADOLESCENT:
                return this.modelAdolescent;
            case SKELETON:
                return this.modelSkeleton;
            default:
                return this.modelAdult;
        }
    }

    private void setModelContainer(GrowthStage stage, TabulaModelContainer model) {
        switch (stage) {
            case INFANT:
                this.modelInfant = model;
                break;
            case JUVENILE:
                this.modelJuvenile = model;
                break;
            case ADOLESCENT:
                this.modelAdolescent = model;
                break;
            case SKELETON:
                this.modelSkeleton = model;
                break;
            default:
                this.modelAdult = model;
                break;
        }
    }

    public boolean doesSupportGrowthStage(GrowthStage stage) {
        return stage == GrowthStage.ADULT || stage == GrowthStage.SKELETON;
    }

    public void setSpawn(int chance, BiomeDictionary.Type... allBiomes) {
        this.spawnChance = chance;
        List<Biome> spawnBiomes = new LinkedList<>();
        for (BiomeDictionary.Type type : allBiomes) {
            for (Biome biome : BiomeDictionary.getBiomes(type)) {
                if (!spawnBiomes.contains(biome)) {
                    spawnBiomes.add(biome);
                }
            }
        }
        this.spawnBiomes = spawnBiomes.toArray(new Biome[0]);
        this.biomeTypes = allBiomes;
    }

    public String getTranslationKey() {
        return "entity.jurassicraft." + this.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_") + ".name";
    }

    public List<GrowthStage> getSupportedStages() {
        List<GrowthStage> supportedStages = new ArrayList<>(4);
        for (GrowthStage stage : GrowthStage.VALUES) {
            if (this.doesSupportGrowthStage(stage)) {
                supportedStages.add(stage);
            }
        }
        return supportedStages;
    }

    public enum DinosaurHomeType {
        LAND,
        MARINE;
    }

    public enum DinosaurBehaviourType {
        AGGRESSIVE,
        NEUTRAL,
        PASSIVE,
        SCARED
    }

    public enum BirthType {
        LIVE_BIRTH,
        EGG_LAYING
    }
}
