package com.gmail.sneakdevs.coloredraildetector.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DetectorRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.List;

@Mixin(DetectorRailBlock.class)
public class DetectorRailBlockMixin {

    @ModifyConstant(method = "checkPressed", constant = @Constant(intValue = 1, ordinal = 0), slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z")))
    private int coloredraildetector_checkPressedMixin(int constant, Level level, BlockPos blockPos, BlockState blockState) {
        if (hasColored(level.getBlockState(blockPos.below()).getBlock())) {
            List<AbstractMinecart> list = level.getEntitiesOfClass(AbstractMinecart.class, new AABB((double)blockPos.getX() + 0.2D, blockPos.getY(), (double)blockPos.getZ() + 0.2D, (double)(blockPos.getX() + 1) - 0.2D, (double)(blockPos.getY() + 1) - 0.2D, (double)(blockPos.getZ() + 1) - 0.2D));
            if (!(list.get(0).getFirstPassenger() instanceof Player player)) {
                return 1;
            } else {
                if (player.getMainHandItem().getItem().equals(level.getBlockState(blockPos.below()).getBlock().asItem())) {
                    return 1;
                }
            }
            return 0;
        }
        return 1;
    }

    private boolean hasColored(Block block) {
        String desc = block.getDescriptionId();
        return (desc.contains("minecraft.") && (desc.contains("_terracotta") || desc.contains("wool") || desc.contains("concrete")));
    }
}