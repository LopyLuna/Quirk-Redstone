package uwu.lopyluna.quirk.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;

@ParametersAreNonnullByDefault
public class GateBlock extends DiodeBlock {

    RedstoneGateType type;

    public static GateBlock gateAND(Properties properties) {
        return new GateBlock(properties, RedstoneGateType.AND);
    }
    public static GateBlock gateOR(Properties properties) {
        return new GateBlock(properties, RedstoneGateType.OR);
    }
    public static GateBlock gateXOR(Properties properties) {
        return new GateBlock(properties, RedstoneGateType.XOR);
    }

    protected GateBlock(Properties pProperties, RedstoneGateType gateType) {
        super(pProperties.instabreak().sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY));
        type = gateType;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
    }

    @Override
    public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        if (!pBlockState.getValue(POWERED)) {
            return 0;
        } else {
            return (getRightDirection(pBlockState) == pSide) || (getLeftDirection(pBlockState) == pSide) ? this.getOutputSignal(pBlockAccess, pPos, pBlockState) : 0;
        }
    }

    @Override
    protected int getInputSignal(Level pLevel, BlockPos pPos, BlockState pState) {
        if (hasSideInput(pLevel, pPos, pState)) {
            return getSideSignal(pLevel, pPos, pState);
        } else {
            return 0;
        }
    }

    protected int getSideSignal(Level pLevel, BlockPos pPos, BlockState pState) {
        boolean greaterThanLeft = getRightStateSignal(pLevel, pPos, pState) > getLeftStateSignal(pLevel, pPos, pState);
        return greaterThanLeft ? getRightStateSignal(pLevel, pPos, pState) : getLeftStateSignal(pLevel, pPos, pState);
    }

    @Override
    protected int getOutputSignal(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel instanceof Level level) {
            return getSideSignal(level, pPos, pState);
        } else {
            return 0;
        }
    }

    @Override
    protected int getDelay(BlockState blockState) {
        return 2;
    }

    protected Direction getRightDirection(BlockState pState) {
        return pState.getValue(FACING).getClockWise();
    }
    protected Direction getLeftDirection(BlockState pState) {
        return pState.getValue(FACING).getCounterClockWise();
    }

    protected BlockPos getRightBlockPos(BlockPos pPos, BlockState pState) {
        return pPos.relative(getRightDirection(pState));
    }
    protected BlockPos getLeftBlockPos(BlockPos pPos, BlockState pState) {
        return pPos.relative(getLeftDirection(pState));
    }

    protected int getRightStateSignal(Level pLevel, BlockPos pPos, BlockState pState) {
        return pLevel.getSignal(getRightBlockPos(pPos, pState), getRightDirection(pState));
    }

    protected int getLeftStateSignal(Level pLevel, BlockPos pPos, BlockState pState) {
        return pLevel.getSignal(getLeftBlockPos(pPos, pState), getLeftDirection(pState));
    }
    protected boolean hasSignalAtRightState(Level pLevel, BlockPos pPos, BlockState pState) {
        return getRightStateSignal(pLevel, pPos, pState) > 0;
    }
    protected boolean hasSignalAtLeftState(Level pLevel, BlockPos pPos, BlockState pState) {
        return getLeftStateSignal(pLevel, pPos, pState) > 0;
    }

    protected boolean gateAND(Level pLevel, BlockPos pPos, BlockState pState) {
        return hasSignalAtLeftState(pLevel, pPos, pState) && hasSignalAtRightState(pLevel, pPos, pState);
    }
    protected boolean gateOR(Level pLevel, BlockPos pPos, BlockState pState) {
        return hasSignalAtLeftState(pLevel, pPos, pState) || hasSignalAtRightState(pLevel, pPos, pState);
    }
    protected boolean gateXOR(Level pLevel, BlockPos pPos, BlockState pState) {
        boolean and = gateAND(pLevel, pPos, pState);
        boolean or = gateOR(pLevel, pPos, pState);
        return !and && or;
    }

    @Override
    protected boolean sideInputDiodesOnly() {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, POWERED);
    }

    protected boolean hasSideInput(Level pLevel, BlockPos pPos, BlockState pState) {
        return switch (type) {
            case OR -> gateOR(pLevel, pPos, pState);
            case AND -> gateAND(pLevel, pPos, pState);
            case XOR -> gateXOR(pLevel, pPos, pState);
        };
    }

    public enum RedstoneGateType implements StringRepresentable {
        AND, OR, XOR;

        @Override
        public @NotNull String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
