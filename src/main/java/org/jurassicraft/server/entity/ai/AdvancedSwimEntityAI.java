package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.ai.util.AIUtils;

public class AdvancedSwimEntityAI extends EntityAIBase {
    private final DinosaurEntity entity;
    private BlockPos shore;
    private Path path;

    public AdvancedSwimEntityAI(DinosaurEntity entity) {
        this.entity = entity;
        ((PathNavigateGround) entity.getNavigator()).setCanSwim(true);
        this.setMutexBits(Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute() {
        return this.entity.isSwimming() && this.entity.getNavigator().noPath() && (this.entity.getAttackTarget() == null || this.entity.getAttackTarget().isDead);
    }

    @Override
    public void startExecuting() {
        BlockPos surface = AIUtils.findSurface(this.entity);

        if (surface != null) {
            this.shore = AIUtils.findShore(this.entity.getEntityWorld(), surface.down());

            if (this.shore != null) {
                this.path = this.entity.getNavigator().getPathToPos(this.shore.up());
                if (!this.entity.getNavigator().setPath(this.path, 1.5)) {
                    this.shore = null;
                }
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        Path currentPath = this.entity.getNavigator().getPath();
        return this.shore != null && this.path != null && (currentPath == null || !currentPath.isFinished());
    }

    @Override
    public void updateTask() {
        Path currentPath = this.entity.getNavigator().getPath();
        if (this.shore != null && !this.path.isSamePath(currentPath)) {
            Path path = this.entity.getNavigator().getPathToPos(this.shore);
            if (path != null && !path.isSamePath(currentPath)) {
                this.path = path;
                this.entity.getNavigator().setPath(path, 1.5);
            }
        }
    }
}
