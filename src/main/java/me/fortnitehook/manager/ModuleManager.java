package me.fortnitehook.manager;

import me.fortnitehook.event.events.Render2DEvent;
import me.fortnitehook.event.events.Render3DEvent;
import me.fortnitehook.features.Feature;
import me.fortnitehook.features.gui.OyVeyGui;
import me.fortnitehook.features.modules.Module;
import me.fortnitehook.features.modules.client.*;
import me.fortnitehook.features.modules.client.TargetHud.TargetHud;
import me.fortnitehook.features.modules.combat.*;
import me.fortnitehook.features.modules.exploit.*;
import me.fortnitehook.features.modules.misc.*;
import me.fortnitehook.features.modules.player.Speedmine;
import me.fortnitehook.features.modules.render.*;
import me.fortnitehook.features.modules.movement.HoleSnap;
import me.fortnitehook.features.modules.movement.NoVoid;
import me.fortnitehook.features.modules.movement.ReverseStep;
import me.fortnitehook.features.modules.movement.Step;
import me.fortnitehook.features.modules.player.AntiHoleKick;
import me.fortnitehook.features.modules.player.FakePlayer;
import me.fortnitehook.features.modules.player.TpsSync;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ModuleManager
        extends Feature {
    public static ArrayList<Module> modules = new ArrayList();
    public List<Module> sortedModules = new ArrayList<Module>();
    public List<String> sortedModulesABC = new ArrayList<String>();
    public Animation animationThread;

    public void init() {
      // Client
        modules.add(new ClickGui());
        modules.add(new FontMod());
        modules.add(new Csgo());
        modules.add(new HUD());
        modules.add(new RPC());
        modules.add(new TargetHud());
     // Combat
        modules.add(new DurabilityAlert());
        modules.add(new ArmorMessage());
        modules.add(new AutoSand());
        modules.add(new Aura());
        modules.add(new CrystalAura());
        modules.add(new AutoCrystalOld());
        modules.add(new BedBomb());
        modules.add(new HoleCampFix());
        modules.add(new AutoPush());
        modules.add(new AutoCityPlus());
        modules.add(new Offhand());
        modules.add(new FastBow());

     // Exploit
        modules.add(new Clip());
        modules.add(new FakePearl());
        modules.add(new PearlPhase());
        modules.add(new Phase());
        modules.add(new PopLag());



        // Misc
        modules.add(new BetterChat());
        modules.add(new AutoFrameDupe());
        modules.add(new PotionDetect());
        modules.add(new PearlNotify());
        modules.add(new KillEffect());
        modules.add(new PopCounter());
        //modules.add(new Notifications());
        modules.add(new VisualRange());
     // Movement
        modules.add(new NoVoid());
        modules.add(new ReverseStep());
        modules.add(new Step());
        modules.add(new HoleSnap());
     // Player
        modules.add(new FakePlayer());
        modules.add(new AntiHoleKick());
        modules.add(new Speedmine());
        modules.add(new TpsSync());
        //this.modules.add(new FortniteMine());
     //Render
        modules.add(new ViewModel());
        modules.add(new NoRender());
        modules.add(new NoLag());
        modules.add(new Swing());
        modules.add(new LegacyGlow());
        modules.add(new AmongUsESP());
        modules.add(new EntityCircle());
        modules.add(new CrystalSpawns());
        modules.add(new ChinaHat());
        modules.add(new CrystalChams());
        modules.add(new BlockHighlight());
        modules.add(new BreakESP());
        modules.add(new ESP());
        modules.add(new HoleESP());
        modules.add(new Trajectories());
        modules.add(new SomeRenders());
        updateLookup();
    }


    static HashMap<String, Module> lookup = new HashMap<>();

    public static void updateLookup() {
        lookup.clear();
        for (Module m : modules)
            lookup.put(m.getName().toLowerCase(), m);
    }

    public static Module getModuleByName(String name) {
        return lookup.get(name.toLowerCase());
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : modules) {
            if (!clazz.isInstance(module)) continue;
            return (T) module;
        }
        return null;
    }

    public void enableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }

    public void enableModule(String name) {
        Module module = getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(String name) {
        Module module = getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }




    public Module getModuleByDisplayName(String displayName) {
        for (Module module : modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }



    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (Module module : modules) {
            if (!module.isEnabled()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public ArrayList<String> getEnabledModulesName() {
        ArrayList<String> enabledModules = new ArrayList<String>();
        for (Module module : modules) {
            if (!module.isEnabled() || !module.isDrawn()) continue;
            enabledModules.add(module.getFullArrayString());
        }
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<Module>();
        modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add(module);
            }
        });
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        modules.stream().filter(Module::listening).forEach(((EventBus) MinecraftForge.EVENT_BUS)::register);
        modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public static boolean isModuleEnabled(String name) {
        Module module = getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class clazz) {
        Object module = this.getModuleByClass(clazz);
        return module != null && ((Module) module).isOn();
    }


    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }




    public void sortModulesABC() {
        this.sortedModulesABC = new ArrayList<String>(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }

    public void onLogout() {
        modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        modules.forEach(Module::onLogin);
    }

    public void onUnload() {
        modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof OyVeyGui) {
            return;
        }
        modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }

    private class Animation
            extends Thread {
        public Module module;
        public float offset;
        public float vOffset;
        ScheduledExecutorService service;

        public Animation() {
            super("Animation");
            this.service = Executors.newSingleThreadScheduledExecutor();
        }



        @Override
        public void start() {
            System.out.println("Starting animation thread.");
            this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }
}

