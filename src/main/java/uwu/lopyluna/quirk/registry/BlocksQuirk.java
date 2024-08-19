package uwu.lopyluna.quirk.registry;

import com.tterrag.registrate.util.entry.BlockEntry;
import uwu.lopyluna.quirk.Quirk;
import uwu.lopyluna.quirk.content.block.GateBlock;
import uwu.lopyluna.quirk.foundation.QuirkRegistrate;

import static uwu.lopyluna.quirk.foundation.util.BlockUtil.regBlock;

@SuppressWarnings({"unused"})
public class BlocksQuirk {
    public static final QuirkRegistrate REGISTRATION = Quirk.registrate();

    public static final BlockEntry<GateBlock> REDSTONE_AND_GATE = regBlock("redstone_and_gate", GateBlock::gateAND);
    public static final BlockEntry<GateBlock> REDSTONE_OR_GATE = regBlock("redstone_or_gate", GateBlock::gateOR);
    public static final BlockEntry<GateBlock> REDSTONE_XOR_GATE = regBlock("redstone_xor_gate", GateBlock::gateXOR);




    public static void register() {}
}
