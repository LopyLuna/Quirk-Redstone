package uwu.lopyluna.quirk.foundation.util;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import uwu.lopyluna.quirk.registry.BlocksQuirk;

@SuppressWarnings({"unused"})
public class BlockUtil {

    public static <T extends Block> BlockEntry<T> regBlock(String name, NonNullFunction<Block.Properties, T> factory) {
        return BlocksQuirk.REGISTRATION.block(name, factory).simpleItem().register();
    }
    public static <T extends Block> BlockEntry<T> regBlockLang(String name, String lang, NonNullFunction<Block.Properties, T> factory) {
        return BlocksQuirk.REGISTRATION.block(name, factory).simpleItem().lang(lang).register();
    }
    public static BlockEntry<Block> simpleBlock(String name) {
        return BlocksQuirk.REGISTRATION.block(name, Block::new).simpleItem().register();
    }
    public static BlockEntry<Block> simpleBlock(String name, String lang) {
        return BlocksQuirk.REGISTRATION.block(name, Block::new).simpleItem().lang(lang).register();
    }


    @SafeVarargs
    public static <T extends Block> BlockEntry<T> regBlockTag(String name, NonNullFunction<Block.Properties, T> factory, TagKey<Block>... tags) {
        return BlocksQuirk.REGISTRATION.block(name, factory).tag(tags).simpleItem().register();
    }
    @SafeVarargs
    public static <T extends Block> BlockEntry<T> regBlockTag(String name, String lang, NonNullFunction<Block.Properties, T> factory, TagKey<Block>... tags) {
        return BlocksQuirk.REGISTRATION.block(name, factory).tag(tags).simpleItem().lang(lang).register();
    }
    @SafeVarargs
    public static BlockEntry<Block> simpleBlockTag(String name, TagKey<Block>... tags) {
        return BlocksQuirk.REGISTRATION.block(name, Block::new).tag(tags).simpleItem().register();
    }
    @SafeVarargs
    public static BlockEntry<Block> simpleBlockTag(String name, String lang, TagKey<Block>... tags) {
        return BlocksQuirk.REGISTRATION.block(name, Block::new).tag(tags).simpleItem().lang(lang).register();
    }
}
