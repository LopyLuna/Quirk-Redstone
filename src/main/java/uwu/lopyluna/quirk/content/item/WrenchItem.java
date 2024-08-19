package uwu.lopyluna.quirk.content.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class WrenchItem extends Item {
    public WrenchItem(Properties pProperties) {
        super(pProperties.stacksTo(1).durability(250));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        BlockPos clickedPos = pContext.getClickedPos();
        Direction clickedFace = pContext.getClickedFace();
        BlockState clickedState = level.getBlockState(clickedPos);
        ItemStack itemstack = pContext.getItemInHand();
        BlockState newState = getRotatedBlockState(clickedState, clickedFace, Objects.requireNonNull(player).isShiftKeyDown());
        if (level instanceof ServerLevel serverLevel && newState.canSurvive(level, clickedPos) && hasAllRotatedProperty(clickedState)) {
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, clickedPos, itemstack);
            }

            serverLevel.setBlock(clickedPos, newState, Block.UPDATE_ALL_IMMEDIATE);
            serverLevel.updateNeighborsAt(clickedPos, clickedState.getBlock());
            itemstack.hurtAndBreak(1, player, (p_150686_) -> p_150686_.broadcastBreakEvent(pContext.getHand()));
        }
        if (newState.canSurvive(level, clickedPos) && hasAllRotatedProperty(clickedState) && !(clickedState == newState)) {
            level.playSound(player, clickedPos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 0.25f, 2.0f);
            level.playSound(player, clickedPos, clickedState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 0.5f, 1.5f);

            return InteractionResult.SUCCESS;
        } else if (hasAllRotatedProperty(clickedState) && (!newState.canSurvive(level, clickedPos) || clickedState == newState)) {
            level.playSound(player, clickedPos, clickedState.getSoundType().getBreakSound(), SoundSource.BLOCKS, 0.2f, 0.5f);

            return InteractionResult.FAIL;
        }
        return super.useOn(pContext);
    }

    public boolean hasAllRotatedProperty(BlockState pState) {
        return hasProperty(pState, BlockStateProperties.HORIZONTAL_FACING) ||
                hasProperty(pState, BlockStateProperties.FACING) ||
                hasProperty(pState, BlockStateProperties.FACING_HOPPER) ||
                hasProperty(pState, BlockStateProperties.ROTATION_16) ||
                hasProperty(pState, BlockStateProperties.HORIZONTAL_AXIS) ||
                hasProperty(pState, BlockStateProperties.AXIS);
    }

    public <T extends Comparable<T>> boolean hasProperty(BlockState pState, Property<T> property) {
        return pState.hasProperty(property);
    }

    public BlockState getRotatedBlockState(BlockState clickedState, Direction clickedFace, boolean isShifting) {
        if (isShifting) {
            if (clickedState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                return clickedState.setValue(BlockStateProperties.HORIZONTAL_FACING,
                        clickedState.getValue(BlockStateProperties.HORIZONTAL_FACING).getCounterClockWise(clickedFace.getAxis()));

            } else if (clickedState.hasProperty(BlockStateProperties.FACING)) {
                return clickedState.setValue(BlockStateProperties.FACING,
                        clickedState.getValue(BlockStateProperties.FACING).getCounterClockWise(clickedFace.getAxis()));

            } else if (clickedState.hasProperty(BlockStateProperties.FACING_HOPPER)) {
                boolean up = clickedState.getValue(BlockStateProperties.FACING_HOPPER).getCounterClockWise(clickedFace.getAxis()) == Direction.UP;
                Direction newState = up ? Direction.DOWN : clickedState.getValue(BlockStateProperties.FACING_HOPPER).getCounterClockWise(clickedFace.getAxis());
                return clickedState.setValue(BlockStateProperties.FACING_HOPPER, newState);

            } else if (clickedState.hasProperty(BlockStateProperties.ROTATION_16) && clickedFace.getAxis().isVertical()) {
                int direction = clickedState.getValue(BlockStateProperties.ROTATION_16);
                int newDirection = direction == 0 ? 15 : direction - 1;

                return clickedState.setValue(BlockStateProperties.ROTATION_16, newDirection);
            } else {
                return getRotatedBlockStateAxis(clickedState, clickedFace);
            }
        } else {
            if (clickedState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                return clickedState.setValue(BlockStateProperties.HORIZONTAL_FACING,
                        clickedState.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise(clickedFace.getAxis()));

            } else if (clickedState.hasProperty(BlockStateProperties.FACING)) {
                return clickedState.setValue(BlockStateProperties.FACING,
                        clickedState.getValue(BlockStateProperties.FACING).getClockWise(clickedFace.getAxis()));

            } else if (clickedState.hasProperty(BlockStateProperties.FACING_HOPPER)) {
                boolean up = clickedState.getValue(BlockStateProperties.FACING_HOPPER).getCounterClockWise(clickedFace.getAxis()) == Direction.UP;
                Direction newState = up ? Direction.DOWN : clickedState.getValue(BlockStateProperties.FACING_HOPPER).getCounterClockWise(clickedFace.getAxis());
                return clickedState.setValue(BlockStateProperties.FACING_HOPPER, newState);

            } else if (clickedState.hasProperty(BlockStateProperties.ROTATION_16) && clickedFace.getAxis().isVertical()) {
                int direction = clickedState.getValue(BlockStateProperties.ROTATION_16);
                int newDirection = direction == 15 ? 0 : direction + 1;

                return clickedState.setValue(BlockStateProperties.ROTATION_16, newDirection);
            } else {
                return getRotatedBlockStateAxis(clickedState, clickedFace);
            }
        }
    }

    public BlockState getRotatedBlockStateAxis(BlockState clickedState, Direction clickedFace) {
        if (clickedState.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
            Direction.Axis axis = clickedState.getValue(BlockStateProperties.HORIZONTAL_AXIS);
            Direction.Axis clickedAxis = clickedFace.getAxis();
            Direction.Axis x = Direction.Axis.X;
            Direction.Axis z = Direction.Axis.Z;
            Direction.Axis newAxis = clickedAxis.isVertical() ? axis==x ? z:axis==z ? x:axis : axis;

            return clickedState.setValue(BlockStateProperties.HORIZONTAL_AXIS, newAxis);
        } else if (clickedState.hasProperty(BlockStateProperties.AXIS)) {
            Direction.Axis axis = clickedState.getValue(BlockStateProperties.AXIS);
            Direction.Axis clickedAxis = clickedFace.getAxis();
            Direction.Axis x = Direction.Axis.X;
            Direction.Axis y = Direction.Axis.Y;
            Direction.Axis z = Direction.Axis.Z;
            Direction.Axis yAxis = clickedAxis.isVertical() ? axis==x ? z:axis==z ? x:axis : axis;
            Direction.Axis zAxis = clickedAxis==z ? axis==x ? y:axis.isVertical() ? x:axis : yAxis;
            Direction.Axis newAxis = clickedAxis==x ? axis==z ? y:axis.isVertical() ? z:axis : zAxis;

            return clickedState.setValue(BlockStateProperties.AXIS, newAxis);
        } else {
            return clickedState;
        }
    }
}
