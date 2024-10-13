package net.mikitstrees.snugglebeasts.entity.custom;

import com.mojang.serialization.Codec;
import java.util.function.IntFunction;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.VariantHolder;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class LovebirdEntity extends TameableShoulderEntity implements VariantHolder<LovebirdEntity.Variant>, Flutterer {
    private static final TrackedData<Integer> VARIANT = DataTracker.registerData(LovebirdEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    private float flapSpeed = 1.0F;
    private float field_28640 = 1.0F;
    private boolean songPlaying;
    @Nullable
    private BlockPos songSource;

    public LovebirdEntity(EntityType<? extends LovebirdEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 10, false);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setVariant(Util.getRandom(LovebirdEntity.Variant.values(), world.getRandom()));
        if (entityData == null) {
            entityData = new PassiveEntity.PassiveData(false);
        }

        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new TameableEntity.TameableEscapeDangerGoal(1.25));
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(2, new FollowOwnerGoal(this, 1.0, 5.0F, 1.0F));
        this.goalSelector.add(2, new LovebirdEntity.FlyOntoTreeGoal(this, 1.0));
        // add duo group goal
        this.goalSelector.add(3, new SitOnOwnerShoulderGoal(this));
        this.goalSelector.add(3, new FollowMobGoal(this, 1.0, 3.0F, 7.0F));
    }

    public static DefaultAttributeContainer.Builder createLovebirdAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.4F)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2F)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    @Override
    public void tickMovement() {
        if (this.songSource == null || !this.songSource.isWithinDistance(this.getPos(), 3.46) || !this.getWorld().getBlockState(this.songSource).isOf(Blocks.JUKEBOX)
        )
        {
            this.songPlaying = false;
            this.songSource = null;
        }

        super.tickMovement();
        this.flapWings();
    }

    @Override
    public void setNearbySongPlaying(BlockPos songPosition, boolean playing) {
        this.songSource = songPosition;
        this.songPlaying = playing;
    }

    public boolean isSongPlaying() {
        return this.songPlaying;
    }

    private void flapWings() {
        this.prevFlapProgress = this.flapProgress;
        this.prevMaxWingDeviation = this.maxWingDeviation;
        this.maxWingDeviation = this.maxWingDeviation + (float)(!this.isOnGround() && !this.hasVehicle() ? 4 : -1) * 0.3F;
        this.maxWingDeviation = MathHelper.clamp(this.maxWingDeviation, 0.0F, 1.0F);
        if (!this.isOnGround() && this.flapSpeed < 1.0F) {
            this.flapSpeed = 1.0F;
        }

        this.flapSpeed *= 0.9F;
        Vec3d vec3d = this.getVelocity();
        if (!this.isOnGround() && vec3d.y < 0.0) {
            this.setVelocity(vec3d.multiply(1.0, 0.6, 1.0));
        }

        this.flapProgress = this.flapProgress + this.flapSpeed * 2.0F;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!this.isTamed() && itemStack.isIn(ItemTags.PARROT_FOOD)) {
            itemStack.decrementUnlessCreative(1, player);
            if (!this.isSilent()) {
                this.getWorld()
                        .playSound(
                                null,
                                this.getX(),
                                this.getY(),
                                this.getZ(),
                                SoundEvents.ENTITY_PARROT_EAT,
                                this.getSoundCategory(),
                                1.0F,
                                1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F
                        );
            }

            if (!this.getWorld().isClient) {
                if (this.random.nextInt(10) == 0) {
                    this.setOwner(player);
                    this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
                } else {
                    this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_NEGATIVE_PLAYER_REACTION_PARTICLES);
                }
            }

            return ActionResult.success(this.getWorld().isClient);
        } else if (!itemStack.isIn(ItemTags.PARROT_POISONOUS_FOOD)) {
            if (!this.isInAir() && this.isTamed() && this.isOwner(player)) {
                if (!this.getWorld().isClient) {
                    this.setSitting(!this.isSitting());
                }

                return ActionResult.success(this.getWorld().isClient);
            } else {
                return super.interactMob(player, hand);
            }
        } else {
            itemStack.decrementUnlessCreative(1, player);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 900));
            if (player.isCreative() || !this.isInvulnerable()) {
                this.damage(this.getDamageSources().playerAttack(player), Float.MAX_VALUE);
            }

            return ActionResult.success(this.getWorld().isClient);
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    public static boolean canSpawn(EntityType<LovebirdEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(BlockTags.PARROTS_SPAWNABLE_ON) && isLightLevelValidForNaturalSpawn(world, pos);
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        return false;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_PARROT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PARROT_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_PARROT_STEP, 0.15F, 1.0F);
    }

    @Override
    protected boolean isFlappingWings() {
        return this.speed > this.field_28640;
    }

    @Override
    protected void addFlapEffects() {
        this.playSound(SoundEvents.ENTITY_PARROT_FLY, 0.15F, 1.0F);
        this.field_28640 = this.speed + this.maxWingDeviation / 2.0F;
    }

    @Override
    public float getSoundPitch() {
        return getSoundPitch(this.random);
    }

    public static float getSoundPitch(Random random) {
        return (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void pushAway(Entity entity) {
        if (!(entity instanceof PlayerEntity)) {
            super.pushAway(entity);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (!this.getWorld().isClient) {
                this.setSitting(false);
            }

            return super.damage(source, amount);
        }
    }

    public LovebirdEntity.Variant getVariant() {
        return LovebirdEntity.Variant.byIndex(this.dataTracker.get(VARIANT));
    }

    public void setVariant(LovebirdEntity.Variant variant) {
        this.dataTracker.set(VARIANT, variant.id);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(VARIANT, 0);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Variant", this.getVariant().id);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setVariant(LovebirdEntity.Variant.byIndex(nbt.getInt("Variant")));
    }

    @Override
    public boolean isInAir() {
        return !this.isOnGround();
    }

    @Override
    protected boolean canTeleportOntoLeaves() {
        return true;
    }

    @Override
    public Vec3d getLeashOffset() {
        return new Vec3d(0.0, (double)(0.5F * this.getStandingEyeHeight()), (double)(this.getWidth() * 0.4F));
    }

    static class FlyOntoTreeGoal extends FlyGoal {
        public FlyOntoTreeGoal(PathAwareEntity pathAwareEntity, double d) {
            super(pathAwareEntity, d);
        }

        @Nullable
        @Override
        protected Vec3d getWanderTarget() {
            Vec3d vec3d = null;
            if (this.mob.isTouchingWater()) {
                vec3d = FuzzyTargeting.find(this.mob, 15, 15);
            }

            if (this.mob.getRandom().nextFloat() >= this.probability) {
                vec3d = this.locateTree();
            }

            return vec3d == null ? super.getWanderTarget() : vec3d;
        }

        @Nullable
        private Vec3d locateTree() {
            BlockPos blockPos = this.mob.getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            BlockPos.Mutable mutable2 = new BlockPos.Mutable();

            for (BlockPos blockPos2 : BlockPos.iterate(
                    MathHelper.floor(this.mob.getX() - 3.0),
                    MathHelper.floor(this.mob.getY() - 6.0),
                    MathHelper.floor(this.mob.getZ() - 3.0),
                    MathHelper.floor(this.mob.getX() + 3.0),
                    MathHelper.floor(this.mob.getY() + 6.0),
                    MathHelper.floor(this.mob.getZ() + 3.0)
            )) {
                if (!blockPos.equals(blockPos2)) {
                    BlockState blockState = this.mob.getWorld().getBlockState(mutable2.set(blockPos2, Direction.DOWN));
                    boolean bl = blockState.getBlock() instanceof LeavesBlock || blockState.isIn(BlockTags.LOGS);
                    if (bl && this.mob.getWorld().isAir(blockPos2) && this.mob.getWorld().isAir(mutable.set(blockPos2, Direction.UP))) {
                        return Vec3d.ofBottomCenter(blockPos2);
                    }
                }
            }

            return null;
        }
    }

    public static enum Variant implements StringIdentifiable {
        GREEN(0, "green"),
        WHITE(1, "white");

        public static final Codec<LovebirdEntity.Variant> CODEC = StringIdentifiable.createCodec(LovebirdEntity.Variant::values);
        private static final IntFunction<LovebirdEntity.Variant> BY_ID = ValueLists.createIdToValueFunction(
                LovebirdEntity.Variant::getId, values(), ValueLists.OutOfBoundsHandling.CLAMP
        );
        final int id;
        private final String name;

        Variant(final int id, final String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        public static LovebirdEntity.Variant byIndex(int index) {
            return (LovebirdEntity.Variant)BY_ID.apply(index);
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
}
