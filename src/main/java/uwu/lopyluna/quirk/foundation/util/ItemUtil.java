package uwu.lopyluna.quirk.foundation.util;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import uwu.lopyluna.quirk.registry.ItemsQuirk;

@SuppressWarnings({"unused"})
public class ItemUtil {


    public static <T extends Item> ItemEntry<T> regItem(String name, NonNullFunction<Item.Properties, T> factory) {
        return ItemsQuirk.REGISTRATION.item(name, factory).register();
    }
    public static <T extends Item> ItemEntry<T> regItemLang(String name, String lang, NonNullFunction<Item.Properties, T> factory) {
        return ItemsQuirk.REGISTRATION.item(name, factory).lang(lang).register();
    }
    public static ItemEntry<Item> simpleItem(String name) {
        return ItemsQuirk.REGISTRATION.item(name, Item::new).register();
    }
    public static ItemEntry<Item> simpleItem(String name, String lang) {
        return ItemsQuirk.REGISTRATION.item(name, Item::new).lang(lang).register();
    }
    @SafeVarargs
    public static ItemEntry<Item> simpleItemTag(String name, TagKey<Item>... tags) {
        return ItemsQuirk.REGISTRATION.item(name, Item::new).tag(tags).register();
    }
    @SafeVarargs
    public static ItemEntry<Item> simpleItemTagLang(String name, String lang, TagKey<Item>... tags) {
        return ItemsQuirk.REGISTRATION.item(name, Item::new).tag(tags).lang(lang).register();
    }
}
