package uwu.lopyluna.quirk.registry;


import com.tterrag.registrate.util.entry.ItemEntry;
import uwu.lopyluna.quirk.Quirk;
import uwu.lopyluna.quirk.content.item.WrenchItem;
import uwu.lopyluna.quirk.foundation.QuirkRegistrate;

import static uwu.lopyluna.quirk.foundation.util.ItemUtil.regItem;

@SuppressWarnings({"unused"})
public class ItemsQuirk {
    public static final QuirkRegistrate REGISTRATION = Quirk.registrate();

    public static final ItemEntry<WrenchItem> WRENCH = regItem("wrench", WrenchItem::new);



    public static void register() {}
}