package me.fortnitehook.features.modules.combat;
/*     */ import java.util.Comparator;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
/*     */ import com.mojang.realmsclient.gui.ChatFormatting;
          import me.fortnitehook.features.command.Command;
/*     */ import me.fortnitehook.features.modules.Module;
/*     */ import me.fortnitehook.features.setting.Setting;
/*     */ import me.fortnitehook.manager.BreakManager;
/*     */ import me.fortnitehook.util.BlockUtil;
/*     */ import me.fortnitehook.util.EntityUtil;
/*     */ import me.fortnitehook.util.InventoryUtil;
/*     */ import me.fortnitehook.util.MathUtil;
/*     */ import me.fortnitehook.util.Timer;
/*     */ import me.fortnitehook.util.FortniteUtil;
/*     */
import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */
/*     */ public class HoleCampFix extends Module {
    /*  27 */   private final Setting<Float> range = register(new Setting("Range", Float.valueOf(5.0F), Float.valueOf(1.0F), Float.valueOf(6.0F)));
    /*  28 */   private final Setting<Integer> delay = register(new Setting("Delay", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500)));
    /*  29 */   private final Timer timer = new Timer();
    /*     */
    /*     */   public EntityPlayer target;
    /*     */
    /*     */   public HoleCampFix() {
        /*  34 */     super("HoleCampFix", "push cyrstal hf", Module.Category.COMBAT, true, false, false);
        /*     */   }
    /*     */
    /*     */   public static boolean EntityCheck(BlockPos pos) {
        /*  38 */     for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
            /*  39 */       if (entity != null)
                /*  40 */         return true;
            /*     */     }
        /*  42 */     return false;
        /*     */   }
    /*     */
    /*     */   public void onTick() {
        /*  46 */     if (fullNullCheck())
            /*     */       return;
        /*  48 */     this.target = getTarget(this.range.getValue().floatValue());
        /*  49 */     if (this.target == null)
            /*     */       return;
        /*  51 */     int redblock = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK);
        /*  52 */     int piston = InventoryUtil.findHotbarBlock(Blocks.PISTON);
        /*  53 */     int crystal = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
        /*  54 */     if (redblock == -1 || piston == -1 || crystal == -1) {
            /*  55 */       Command.sendMessage(ChatFormatting.RED + "No RedBlock or Piston or Crystal!");
            /*  56 */       disable();
            /*     */       return;
            /*     */     }
        /*  59 */     BlockPos enemy = EntityUtil.getPlayerPos(this.target);
        /*  60 */     if (BlockUtil.getBlock(enemy) != Blocks.AIR) {
            /*     */       return;
            /*     */     }
        /*     */
        /*  64 */     if (this.timer.passedMs(this.delay.getValue().intValue())) {
            /*  65 */       if (placecheck(enemy.add(1, 0, 0), 1)) {
                /*  66 */         if (isAir(enemy.add(1, 1, 0)) && isAir(enemy.add(1, 2, 0)))
                    /*  67 */           place(enemy.add(1, 0, 0), 1, 270);
                /*  68 */         PistonHead(Objects.requireNonNull(getPiston(enemy.add(1, 0, 0), 1)));
                /*     */
                /*     */       }
            /*  71 */       else if (placecheck(enemy.add(-1, 0, 0), 2)) {
                /*  72 */         if (isAir(enemy.add(-1, 1, 0)) && isAir(enemy.add(-1, 2, 0)))
                    /*  73 */           place(enemy.add(-1, 0, 0), 2, 90);
                /*  74 */         PistonHead(Objects.requireNonNull(getPiston(enemy.add(-1, 0, 0), 2)));
                /*     */
                /*     */       }
            /*  77 */       else if (placecheck(enemy.add(0, 0, 1), 3)) {
                /*  78 */         if (isAir(enemy.add(0, 1, 1)) && isAir(enemy.add(0, 2, 1)))
                    /*  79 */           place(enemy.add(0, 0, 1), 3, 0);
                /*  80 */         PistonHead(Objects.requireNonNull(getPiston(enemy.add(0, 0, 1), 3)));
                /*     */       }
            /*  82 */       else if (placecheck(enemy.add(0, 0, -1), 4)) {
                /*  83 */         if (isAir(enemy.add(0, 1, -1)) && isAir(enemy.add(0, 2, -1)))
                    /*  84 */           place(enemy.add(0, 0, -1), 4, 180);
                /*  85 */         PistonHead(Objects.requireNonNull(getPiston(enemy.add(0, 0, -1), 4)));
                /*     */
                /*     */       }
            /*  88 */       else if (placecheck(enemy.add(1, 1, 0), 1) && checkdamage(enemy.add(0, 1, 0))) {
                /*  89 */         if (isAir(enemy.add(1, 2, 0)) && isAir(enemy.add(1, 3, 0)))
                    /*  90 */           place(enemy.add(1, 1, 0), 1, 270);
                /*  91 */         PistonHead(Objects.requireNonNull(getPiston(enemy.add(1, 1, 0), 1)));
                /*     */       }
            /*  93 */       else if (placecheck(enemy.add(-1, 1, 0), 2) && checkdamage(enemy.add(0, 1, 0))) {
                /*  94 */         if (isAir(enemy.add(-1, 2, 0)) && isAir(enemy.add(-1, 3, 0)))
                    /*  95 */           place(enemy.add(-1, 1, 0), 2, 90);
                /*  96 */         PistonHead(Objects.requireNonNull(getPiston(enemy.add(-1, 1, 0), 2)));
                /*     */       }
            /*  98 */       else if (placecheck(enemy.add(0, 1, 1), 3) && checkdamage(enemy.add(0, 1, 0))) {
                /*  99 */         if (isAir(enemy.add(0, 2, 1)) && isAir(enemy.add(0, 3, 1)))
                    /* 100 */           place(enemy.add(0, 1, 1), 3, 0);
                /* 101 */         PistonHead(Objects.requireNonNull(getPiston(enemy.add(0, 1, 1), 3)));
                /*     */       }
            /* 103 */       else if (placecheck(enemy.add(0, 1, -1), 4) && checkdamage(enemy.add(0, 1, 0))) {
                /* 104 */         if (isAir(enemy.add(0, 2, -1)) && isAir(enemy.add(0, 3, -1)))
                    /* 105 */           place(enemy.add(0, 1, -1), 4, 180);
                /* 106 */         PistonHead(Objects.requireNonNull(getPiston(enemy.add(0, 1, -1), 4)));
                /*     */       }
            /*     */
            /* 109 */       breakcrystal();
            /* 110 */       this.timer.reset();
            /*     */     }
        /*     */   }
    /*     */
    /*     */
    /*     */   public void breakcrystal() {
        /* 116 */     for (Entity crystal : mc.world.loadedEntityList.stream().filter(e -> (e instanceof net.minecraft.entity.item.EntityEnderCrystal && !e.isDead)).sorted(Comparator.comparing(e -> Float.valueOf(mc.player.getDistance(e)))).collect(Collectors.toList())) {
            /* 117 */       if (crystal instanceof net.minecraft.entity.item.EntityEnderCrystal && crystal.getDistance(this.target) <= MathUtil.square(2.5D)) {
                /* 118 */         EntityUtil.attackEntity(crystal, true);
                /*     */       }
            /*     */     }
        /*     */   }
    /*     */
    /*     */   private boolean placecheck(BlockPos pos, int ab) {
        /* 124 */     return (canPlaceCrystal(pos) && getPiston(pos, ab) != null && getRedStone(Objects.requireNonNull(getPiston(pos, ab)), ab) != null && checkDistance(getPiston(pos, ab)));
        /*     */   }
    /*     */
    /*     */   private void place(BlockPos pos, int ab, int pitch) {
        /* 128 */     place(getRedStone(Objects.requireNonNull(getPiston(pos, ab)), ab), getPiston(pos, ab), pitch, pos);
        /*     */   }
    /*     */
    /*     */   private EntityPlayer getTarget(double range) {
        /* 132 */     EntityPlayer target = null;
        /* 133 */     double distance = range;
        /* 134 */     for (EntityPlayer player : mc.world.playerEntities) {
            /* 135 */       if (EntityUtil.isntValid(player, range))
                /*     */         continue;
            /* 137 */       if (target == null) {
                /* 138 */         target = player;
                /* 139 */         distance = EntityUtil.mc.player.getDistanceSq(player);
                /*     */         continue;
                /*     */       }
            /* 142 */       if (EntityUtil.mc.player.getDistanceSq(player) >= distance)
                /*     */         continue;
            /* 144 */       target = player;
            /* 145 */       distance = EntityUtil.mc.player.getDistanceSq(player);
            /*     */     }
        /* 147 */     return target;
        /*     */   }
    /*     */
    /*     */
    /*     */   private void place(BlockPos redstone, BlockPos piston, int pitch, BlockPos crystal) {
        /* 152 */     if (CanPlace(redstone) && CanPlace(piston) && CanPlace(crystal) && !EntityCheck(crystal.add(0, 1, 0))) {
            /* 153 */       float a = this.target.rotationPitch;
            /* 154 */       int oldslot = mc.player.inventory.currentItem;
            /* 155 */       int reblock = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK);
            /* 156 */       int pistonblock = InventoryUtil.findHotbarBlock(Blocks.PISTON);
            /* 157 */       int crystalblock = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
            /* 158 */       InventoryUtil.switchToSlot(reblock);
            /* 159 */       BlockUtil.placeBlock(redstone, EnumHand.MAIN_HAND, false, true, false);
            /* 160 */       InventoryUtil.switchToSlot(pistonblock);
            /* 161 */       mc.player.connection.sendPacket(new CPacketPlayer.Rotation(pitch, a, true));
            /* 162 */       BlockUtil.placeBlock(piston, EnumHand.MAIN_HAND, false, true, false);
            /* 163 */       InventoryUtil.switchToSlot(crystalblock);
            /* 164 */       BlockUtil.placeCrystalOnBlock(crystal, EnumHand.MAIN_HAND);
            /* 165 */       InventoryUtil.switchToSlot(oldslot);
            /*     */     }
        /*     */   }
    /*     */
    /*     */
    /*     */   private boolean CanPlace(BlockPos pos) {
        /* 171 */     return (!BlockUtil.PlayerCheck(pos) && mc.player.getDistanceSq(pos) <= MathUtil.square(this.range.getValue().floatValue()));
        /*     */   }
    /*     */
    /*     */   private boolean checkdamage(BlockPos pos) {
        /* 175 */     return FortniteUtil.CanPlaceCrystal(pos);
        /*     */   }
    /*     */
    /*     */   private boolean checkDistance(BlockPos pos) {
        /* 179 */     return (mc.player.getDistanceSq(pos) > MathUtil.square(3.0D));
        /*     */   }
    /*     */
    /*     */   private boolean isAir(BlockPos block) {
        /* 183 */     return (mc.world.getBlockState(block).getBlock() == Blocks.AIR && CanPlace(block));
        /*     */   }
    /*     */
    /*     */
    /*     */   public String getDisplayInfo() {
        /* 188 */     if (this.target != null) {
            /* 189 */       return this.target.getName();
            /*     */     }
        /* 191 */     return null;
        /*     */   }
    /*     */
    /*     */   private boolean canPlaceCrystal(BlockPos block) {
        /* 195 */     return ((getblock(block) == Blocks.OBSIDIAN || getblock(block) == Blocks.BEDROCK) && !HardBlock(block.add(0, 1, 0)) && isAir(block.add(0, 2, 0)));
        /*     */   }
    /*     */
    /*     */   private void PistonHead(BlockPos pos) {
        /* 199 */     for (EnumFacing face : EnumFacing.VALUES) {
            /* 200 */       if (!face.equals(EnumFacing.DOWN) && !face.equals(EnumFacing.UP) && !BreakManager.SelfMine(pos.offset(face)))
                /*     */       {
                /* 202 */         if (getblock(pos.offset(face)).equals(Blocks.PISTON_HEAD))
                    /* 203 */           mc.playerController.onPlayerDamageBlock(pos.offset(face), BlockUtil.getRayTraceFacing(pos.offset(face)));
                /*     */       }
            /*     */     }
        /*     */   }
    /*     */
    /*     */   private boolean HardBlock(BlockPos block) {
        /* 209 */     return (getblock(block) == Blocks.OBSIDIAN || getblock(block) == Blocks.BEDROCK || getblock(block) == Blocks.ENDER_CHEST);
        /*     */   }
    /*     */
    /*     */   private BlockPos getPiston(BlockPos pos, int piston) {
        /* 213 */     switch (piston) {
            /*     */       case 1:
                /* 215 */         if (!HardBlock(pos.add(1, 1, 0)))
                    /* 216 */           return pos.add(1, 1, 0);
                /* 217 */         if (!HardBlock(pos.add(1, 1, 1)) && !HardBlock(pos.add(0, 1, 1)))
                    /* 218 */           return pos.add(1, 1, 1);
                /* 219 */         if (!HardBlock(pos.add(1, 1, -1)) && !HardBlock(pos.add(0, 1, -1)))
                    /* 220 */           return pos.add(1, 1, -1);
                /*     */         break;
            /*     */       case 2:
                /* 223 */         if (!HardBlock(pos.add(-1, 1, 0)))
                    /* 224 */           return pos.add(-1, 1, 0);
                /* 225 */         if (!HardBlock(pos.add(-1, 1, 1)) && !HardBlock(pos.add(0, 1, 1)))
                    /* 226 */           return pos.add(-1, 1, 1);
                /* 227 */         if (!HardBlock(pos.add(-1, 1, -1)) && !HardBlock(pos.add(0, 1, -1)))
                    /* 228 */           return pos.add(-1, 1, -1);
                /*     */         break;
            /*     */       case 3:
                /* 231 */         if (!HardBlock(pos.add(0, 1, 1)))
                    /* 232 */           return pos.add(0, 1, 1);
                /* 233 */         if (!HardBlock(pos.add(1, 1, 1)) && !HardBlock(pos.add(1, 1, 0)))
                    /* 234 */           return pos.add(1, 1, 1);
                /* 235 */         if (!HardBlock(pos.add(-1, 1, 1)) && !HardBlock(pos.add(-1, 1, 0)))
                    /* 236 */           return pos.add(-1, 1, 1);
                /*     */         break;
            /*     */       case 4:
                /* 239 */         if (!HardBlock(pos.add(0, 1, -1)))
                    /* 240 */           return pos.add(0, 1, -1);
                /* 241 */         if (!HardBlock(pos.add(1, 1, -1)) && !HardBlock(pos.add(1, 1, 0)))
                    /* 242 */           return pos.add(1, 1, -1);
                /* 243 */         if (!HardBlock(pos.add(-1, 1, -1)) && !HardBlock(pos.add(-1, 1, 0)))
                    /* 244 */           return pos.add(-1, 1, -1);
                /*     */         break;
            /*     */     }
        /* 247 */     return null;
        /*     */   }
    /*     */
    /*     */
    /*     */   private BlockPos getRedStone(BlockPos pos, int redstone) {
        /* 252 */     if (!HardBlock(pos.add(0, -1, 0)) && check(pos.add(0, -1, 0)))
            /* 253 */       return pos.add(0, -1, 0);
        /* 254 */     if (!HardBlock(pos.add(0, 1, 0)) && check(pos.add(0, 1, 0))) {
            /* 255 */       return pos.add(0, 1, 0);
            /*     */     }
        /* 257 */     switch (redstone) {
            /*     */       case 1:
                /* 259 */         if (!HardBlock(pos.add(1, 0, 0)) && check(pos.add(1, 0, 0)))
                    /* 260 */           return pos.add(1, 0, 0);
                /* 261 */         if (!HardBlock(pos.add(0, 0, 1)) && check(pos.add(0, 0, 1)))
                    /* 262 */           return pos.add(0, 0, 1);
                /* 263 */         if (!HardBlock(pos.add(0, 0, -1)) && check(pos.add(0, 0, -1)))
                    /* 264 */           return pos.add(0, 0, -1);
                /*     */         break;
            /*     */       case 2:
                /* 267 */         if (!HardBlock(pos.add(-1, 0, 0)) && check(pos.add(-1, 0, 0)))
                    /* 268 */           return pos.add(-1, 0, 0);
                /* 269 */         if (!HardBlock(pos.add(0, 0, 1)) && check(pos.add(0, 0, 1)))
                    /* 270 */           return pos.add(0, 0, 1);
                /* 271 */         if (!HardBlock(pos.add(0, 0, -1)) && check(pos.add(0, 0, -1)))
                    /* 272 */           return pos.add(0, 0, -1);
                /*     */         break;
            /*     */       case 3:
                /* 275 */         if (!HardBlock(pos.add(1, 0, 0)) && check(pos.add(1, 0, 0)))
                    /* 276 */           return pos.add(1, 0, 0);
                /* 277 */         if (!HardBlock(pos.add(-1, 0, 0)) && check(pos.add(-1, 0, 0)))
                    /* 278 */           return pos.add(-1, 0, 0);
                /* 279 */         if (!HardBlock(pos.add(0, 0, 1)) && check(pos.add(0, 0, 1)))
                    /* 280 */           return pos.add(0, 0, 1);
                /*     */         break;
            /*     */       case 4:
                /* 283 */         if (!HardBlock(pos.add(1, 0, 0)) && check(pos.add(1, 0, 0)))
                    /* 284 */           return pos.add(1, 0, 0);
                /* 285 */         if (!HardBlock(pos.add(-1, 0, 0)) && check(pos.add(-1, 0, 0)))
                    /* 286 */           return pos.add(-1, 0, 0);
                /* 287 */         if (!HardBlock(pos.add(0, 0, -1)) && check(pos.add(0, 0, -1))) {
                    /* 288 */           return pos.add(0, 0, -1);
                    /*     */         }
                /*     */         break;
            /*     */     }
        /*     */
        /*     */
        /* 294 */     return null;
        /*     */   }
    /*     */
    /*     */   private Block getblock(BlockPos pos) {
        /* 298 */     return mc.world.getBlockState(pos).getBlock();
        /*     */   }
    /*     */
    /*     */   private boolean check(BlockPos pos) {
        /* 302 */     return ((HardBlock(pos.add(0, 1, 0)) || HardBlock(pos.add(0, -1, 0)) || HardBlock(pos.add(1, 0, 0)) || HardBlock(pos.add(-1, 0, 0)) || HardBlock(pos.add(0, 0, 1)) || HardBlock(pos.add(0, 0, -1))) && !BreakManager.isMine(pos, true));
        /*     */   }
    /*     */ }


/* Location:              C:\Users\Administrator\Videos\remapped.jar!\me\abHack\features\modules\combat\HoleCampFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */