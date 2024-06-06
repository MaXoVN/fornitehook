package me.fortnitehook;

import me.fortnitehook.manager.*;
import me.fortnitehook.tracker.Tracker;
import me.fortnitehook.util.umm.Thingy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid = "fortnitehook", name = "fortnitehook", version = "1.2-beta")
public class OyVey {
    public static final String MODID = "fortnitehook";
    public static final String MODNAME = "fortnitehook";
    public static Tracker tracker;
    public static Thingy hwidManager;

    public static final String MODVER = "1.2";
    public static final String MODBUILD = "-build-zoav8lspfr468ed";
    public static String getVersion() {
        return MODVER;
    }
    public static final Logger LOGGER = LogManager.getLogger("fortnitehook");
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static PacketManager packetManager;
    public static ColorManager colorManager;
    public static HoleManager holeManager;
    public static InventoryManager inventoryManager;
    public static PotionManager potionManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static SpeedManager speedManager;
    public static ReloadManager reloadManager;
    public static FileManager fileManager;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static EventManager eventManager;
    public static TextManager textManager;
    public static TotemPopManager totemPopManager;
    @Mod.Instance
    public static OyVey INSTANCE;
    private static boolean unloaded;

    static {
        unloaded = false;
    }

    public static void load() {
        LOGGER.info("\n\nLoading fortnitehook");
        unloaded = false;
        if (reloadManager != null) {
            reloadManager.unload();
            reloadManager = null;
        }
        textManager = new TextManager();
        commandManager = new CommandManager();
        friendManager = new FriendManager();
        moduleManager = new ModuleManager();
        rotationManager = new RotationManager();
        packetManager = new PacketManager();
        eventManager = new EventManager();
        speedManager = new SpeedManager();
        potionManager = new PotionManager();
        inventoryManager = new InventoryManager();
        serverManager = new ServerManager();
        fileManager = new FileManager();
        colorManager = new ColorManager();
        positionManager = new PositionManager();
        configManager = new ConfigManager();
        holeManager = new HoleManager();
        totemPopManager = new TotemPopManager();
        LOGGER.info("Managers loaded.");
        moduleManager.init();
        LOGGER.info("Modules loaded.");
        configManager.init();
        eventManager.init();
        LOGGER.info("EventManager loaded.");
        textManager.init(true);
        moduleManager.onLoad();
        LOGGER.info("OyVey successfully loaded!\n");
    }

    public static void unload(boolean unload) {
        LOGGER.info("\n\nUnloading OyVey by Alpha432");
        if (unload) {
            reloadManager = new ReloadManager();
            reloadManager.init(commandManager != null ? commandManager.getPrefix() : ".");
        }
        OyVey.onUnload();
        eventManager = null;
        friendManager = null;
        speedManager = null;
        holeManager = null;
        positionManager = null;
        rotationManager = null;
        configManager = null;
        commandManager = null;
        colorManager = null;
        serverManager = null;
        fileManager = null;
        potionManager = null;
        inventoryManager = null;
        moduleManager = null;
        textManager = null;
        LOGGER.info("OyVey unloaded!\n");
    }

    public static void reload() {
        OyVey.unload(false);
        OyVey.load();
    }

    public static void onUnload() {
        if (!unloaded) {
            eventManager.onUnload();
            moduleManager.onUnload();
            configManager.saveConfig(OyVey.configManager.config.replaceFirst("fortnitehook/", ""));
            moduleManager.onUnloadPost();
            unloaded = true;
        }
    }





    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        tracker = new Tracker();
        LOGGER.info("I am gona gas you kike - Alpha432");
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Display.setTitle("fortnitehook $$$");
        hwidManager = new Thingy();
        OyVey.load();
    }
}

