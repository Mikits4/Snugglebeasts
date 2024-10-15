package net.mikitstrees.snugglebeasts.entity.passive;

import net.mikitstrees.snugglebeasts.entity.ai.goal.FollowLovebirdLeaderGoal;
import net.mikitstrees.snugglebeasts.entity.custom.LovebirdEntity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.FollowGroupLeaderGoal;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

//public abstract class LovebirdDuoEntity extends LovebirdEntity {
//    @Nullable
//    private LovebirdDuoEntity  leader;
//    private int groupSize = 2;
//
//    public LovebirdDuoEntity(EntityType<? extends LovebirdDuoEntity > entityType, World world) {
//        super(entityType, world);
//    }
//
//    @Override
//    protected void initGoals() {
//        super.initGoals();
//        this.goalSelector.add(5, new FollowLovebirdLeaderGoal(this));
//    }
//
//    @Override
//    public int getLimitPerChunk() {
//        return this.getMaxGroupSize();
//    }
//
//    public int getMaxGroupSize() {
//        return super.getLimitPerChunk();
//    }
//
//    @Override
//    protected boolean hasSelfControl() {
//        return !this.hasLeader();
//    }
//
//    public boolean hasLeader() {
//        return this.leader != null && this.leader.isAlive();
//    }
//
//    public net.minecraft.entity.passive.SchoolingFishEntity joinGroupOf(net.minecraft.entity.passive.SchoolingFishEntity groupLeader) {
//        this.leader = groupLeader;
//        groupLeader.increaseGroupSize();
//        return groupLeader;
//    }
//
//    public void leaveGroup() {
//        this.leader.decreaseGroupSize();
//        this.leader = null;
//    }
//
//    private void increaseGroupSize() {
//        this.groupSize++;
//    }
//
//    private void decreaseGroupSize() {
//        this.groupSize--;
//    }
//
//    public boolean canHaveMoreFishInGroup() {
//        return this.hasOtherFishInGroup() && this.groupSize < this.getMaxGroupSize();
//    }
//
//    @Override
//    public void tick() {
//        super.tick();
//        if (this.hasOtherFishInGroup() && this.getWorld().random.nextInt(200) == 1) {
//            List<? extends FishEntity> list = this.getWorld().getNonSpectatingEntities(this.getClass(), this.getBoundingBox().expand(8.0, 8.0, 8.0));
//            if (list.size() <= 1) {
//                this.groupSize = 1;
//            }
//        }
//    }
//
//    public boolean hasOtherFishInGroup() {
//        return this.groupSize > 1;
//    }
//
//    public boolean isCloseEnoughToLeader() {
//        return this.squaredDistanceTo(this.leader) <= 121.0;
//    }
//
//    public void moveTowardLeader() {
//        if (this.hasLeader()) {
//            this.getNavigation().startMovingTo(this.leader, 1.0);
//        }
//    }
//
//    public void pullInOtherFish(Stream<? extends net.minecraft.entity.passive.SchoolingFishEntity> fish) {
//        fish.limit((long)(this.getMaxGroupSize() - this.groupSize)).filter(fishx -> fishx != this).forEach(fishx -> fishx.joinGroupOf(this));
//    }
//
//    @Nullable
//    @Override
//    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
//        super.initialize(world, difficulty, spawnReason, entityData);
//        if (entityData == null) {
//            entityData = new net.minecraft.entity.passive.SchoolingFishEntity.FishData(this);
//        } else {
//            this.joinGroupOf(((net.minecraft.entity.passive.SchoolingFishEntity.FishData)entityData).leader);
//        }
//
//        return entityData;
//    }
//
//    public static class FishData implements EntityData {
//        public final net.minecraft.entity.passive.SchoolingFishEntity leader;
//
//        public FishData(net.minecraft.entity.passive.SchoolingFishEntity leader) {
//            this.leader = leader;
//        }
//    }
//}
