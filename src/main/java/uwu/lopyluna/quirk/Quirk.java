package uwu.lopyluna.quirk;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import uwu.lopyluna.quirk.foundation.QuirkRegistrate;
import uwu.lopyluna.quirk.registry.BlocksQuirk;
import uwu.lopyluna.quirk.registry.CreativeTabsQuirk;
import uwu.lopyluna.quirk.registry.ItemsQuirk;

@SuppressWarnings({"unused"})
@Mod(Quirk.MOD_ID)
public class Quirk {
    public static final String MOD_ID = "quirk";
    public static final String NAME = "Quirk";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final QuirkRegistrate REGISTRATION = QuirkRegistrate.create(MOD_ID);
    static IEventBus bus;

    public Quirk() {
        bus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemsQuirk.register();
        BlocksQuirk.register();

        CreativeTabsQuirk.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
        finalizeRegistrate();
    }

    public static void finalizeRegistrate() {
        registrate().registerEventListeners(bus);
    }

    public static ResourceLocation asResource(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static QuirkRegistrate registrate() {
        return REGISTRATION;
    }
}
