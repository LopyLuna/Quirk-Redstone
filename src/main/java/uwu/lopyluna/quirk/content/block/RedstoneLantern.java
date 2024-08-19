package uwu.lopyluna.quirk.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings({"deprecation"})
public class RedstoneLantern extends LanternBlock {
    //CURRENTLY BROKEN
    public static final BooleanProperty LIT;
    
    public RedstoneLantern(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, true));
    }

    public boolean isHangingForSignal(BlockState pBlockState, Direction pSide) {
        return pBlockState.getValue(LIT) && pBlockState.getValue(HANGING) && Direction.DOWN != pSide;
    }
    public boolean isLitForSignal(BlockState pBlockState, Direction pSide) {
        return pBlockState.getValue(LIT) && Direction.UP != pSide;
    }

    public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        return isLitForSignal(pBlockState, pSide) || isHangingForSignal(pBlockState, pSide) ? 15 : 0;
    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT);
    }
}
