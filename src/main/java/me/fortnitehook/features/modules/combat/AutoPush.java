package me.fortnitehook.features.modules.combat;
import com.mojang.realmsclient.gui.ChatFormatting;
/*     */ import me.fortnitehook.OyVey;
/*     */ import me.fortnitehook.features.command.Command;
/*     */ import me.fortnitehook.features.modules.Module;
/*     */ import me.fortnitehook.features.setting.Setting;
/*     */ import me.fortnitehook.manager.BreakManager;
/*     */ import me.fortnitehook.util.BlockUtil;
/*     */ import me.fortnitehook.util.EntityUtil;
/*     */ import me.fortnitehook.util.InventoryUtil;
/*     */ import me.fortnitehook.util.MathUtil;
/*     */
import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockDirectional;
/*     */ import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */
/*     */ public class AutoPush extends Module {
    /*  24 */   public static final BlockPos[] position = new BlockPos[] { new BlockPos(1, 1, 0), new BlockPos(0, 1, -1), new BlockPos(-1, 1, 0), new BlockPos(0, 1, 1) };
    /*     */
    /*     */   public static AutoPush INSTANCE;
    /*  27 */   private final Setting<Float> range = register(new Setting("Range", Float.valueOf(5.2F), Float.valueOf(1.0F), Float.valueOf(6.0F)));
    /*  28 */   private final Setting<Boolean> burrow = register(new Setting("PushBurrow", Boolean.valueOf(true)));
    /*  29 */   private final Setting<Boolean> pullback = register(new Setting("Pullback", Boolean.valueOf(true)));
    /*  30 */   private final Setting<Boolean> toggle = register(new Setting("AutoToggle", Boolean.valueOf(true)));
    /*     */   int redBlock;
    /*     */   int redTorch;
    /*     */   int pistonBlock;
    /*  34 */   int place = 0;
    /*     */   BlockPos Mine;
    /*     */   EntityPlayer targer;
    /*     */
    /*     */   public AutoPush() {
        /*  39 */     super("AutoPush", "push hf", Module.Category.COMBAT, true, false, false);
        /*  40 */     INSTANCE = this;
        /*     */   }
    /*     */
    /*     */
    /*     */   public void onTick() {
        /*  45 */     if (fullNullCheck())
            /*     */       return;
        /*  47 */     this.redBlock = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK);
        /*  48 */     this.redTorch = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_TORCH);
        /*  49 */     this.pistonBlock = InventoryUtil.findHotbarBlock(Blocks.PISTON);
        /*  50 */     if ((this.redBlock == -1 && this.redTorch == -1) || this.pistonBlock == -1) {
            /*  51 */       Command.sendMessage(ChatFormatting.RED + "No RedBlock or Piston!");
            /*  52 */       disable();
            /*     */       return;
            /*     */     }
        /*  55 */     if (mc.player.onGround)
            /*     */     {
            /*  57 */       for (EntityPlayer player : mc.world.playerEntities) {
                /*     */
                /*  59 */         if (EntityUtil.isntValid(player, this.range.getValue().floatValue()))
                    /*     */           continue;
                /*  61 */         if (skip(player) || OyVey.speedManager.getPlayerSpeed(player) > 20.0D)
                    /*     */           continue;
                /*  63 */         if (!this.toggle.getValue().booleanValue() && !EntityUtil.isInHole(player) && isAir(EntityUtil.getPlayerPos(player)))
                    /*     */           continue;
                /*  65 */         if (!EntityUtil.isInHole(mc.player) && isAir(EntityUtil.getPlayerPos(mc.player)) && !this.toggle.getValue().booleanValue()) {
                    /*     */           continue;
                    /*     */         }
                /*  68 */         BlockPos enemy = EntityUtil.getPlayerPos(player);
                /*  69 */         if (!isAir(enemy) && !this.burrow.getValue().booleanValue())
                    /*     */           continue;
                /*  71 */         if (BreakManager.isMine(enemy.add(0, 1, 0), true) && !this.toggle.getValue().booleanValue())
                    /*     */           continue;
                /*  73 */         this.targer = player;
                /*     */
                /*  75 */         int i = 0;
                /*     */
                /*  77 */         for (EnumFacing position : EnumFacing.VALUES) {
                    /*  78 */           BlockPos push = EntityUtil.getPlayerPos(this.targer).up();
                    /*  79 */           if (!position.equals(EnumFacing.DOWN) && !position.equals(EnumFacing.UP)) {
                        /*     */
                        /*  81 */             i++;
                        /*  82 */             if (isPiston(push.offset(position), position.getOpposite()) && ((isAir(push.offset(position.getOpposite())) && isAir(push.offset(position.getOpposite()).up())) || (!isAir(enemy) && mineCheck(push.offset(position).up()) && HardBlock(push.offset(position).down()) && this.redBlock != -1 && this.pullback.getValue().booleanValue() && this.toggle.getValue().booleanValue())) && getRedStone(push.offset(position)) != null) {
                            /*  83 */               place(getRedStone(push.offset(position)), push.offset(position), getPitch(i));
                            /*     */               break;
                            /*     */             }
                        /*     */           }
                    /*     */         }
                /*     */       }  }
        /*  89 */     if (this.toggle.getValue().booleanValue() && this.place >= 2) {
            /*  90 */       Command.sendMessage(ChatFormatting.RED + "No position!");
            /*  91 */       disable();
            /*     */     }
        /*     */   }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */   private int getPitch(int i) {
        /* 102 */     if (i == 1)
            /* 103 */       return 180;
        /* 104 */     if (i == 2)
            /* 105 */       return 0;
        /* 106 */     if (i == 3) {
            /* 107 */       return 90;
            /*     */     }
        /* 109 */     return 270;
        /*     */   }
    /*     */
    /*     */
    /*     */
    /*     */   private void place(BlockPos redstone, BlockPos piston, int pitch) {
        /* 115 */     this.Mine = piston;
        /*     */
        /* 117 */     if (BlockUtil.CrystalCheck(redstone) || BlockUtil.CrystalCheck(piston)) {
            /* 118 */
            /*     */
            /*     */       return;
            /*     */     }
        /* 122 */     float a = this.targer.rotationPitch;
        /* 123 */     int oldSlot = mc.player.inventory.currentItem;
        /* 124 */     int tempSlot = (this.redBlock != -1) ? this.redBlock : this.redTorch;
        /*     */
        /* 126 */     if (isAir(piston) && redstone.equals(piston.up(1)) && !HelpBlock(redstone) && this.toggle.getValue().booleanValue() && this.redBlock != -1) {
            /* 127 */       InventoryUtil.switchToSlot(this.pistonBlock);
            /* 128 */       mc.player.connection.sendPacket(new CPacketPlayer.Rotation(pitch, a, true));
            /* 129 */       BlockUtil.placeBlock(piston, EnumHand.MAIN_HAND, false, false, false);
            /* 130 */       this.place++;
            /*     */     }
        /*     */
        /* 133 */     if (RedStoneCheck(piston) == null) {
            /* 134 */       InventoryUtil.switchToSlot(tempSlot);
            /* 135 */       BlockUtil.placeBlock(redstone, EnumHand.MAIN_HAND, false, HelpBlock(piston), false);
            /* 136 */       this.place++;
            /*     */     }
        /* 138 */     if (getblock(piston) == Blocks.AIR) {
            /* 139 */       InventoryUtil.switchToSlot(this.pistonBlock);
            /* 140 */       mc.player.connection.sendPacket(new CPacketPlayer.Rotation(pitch, a, true));
            /* 141 */       BlockUtil.placeBlock(piston, EnumHand.MAIN_HAND, false, true, false);
            /* 142 */       this.place++;
            /*     */     }
        /* 144 */     InventoryUtil.switchToSlot(oldSlot);
        /*     */
        /* 146 */     if (this.redBlock != -1 && this.pullback.getValue().booleanValue()) {
            /* 147 */       BlockPos block = (RedStoneCheck(piston) == null) ? redstone : RedStoneCheck(piston);
            /* 148 */       mc.playerController.onPlayerDamageBlock(block, BlockUtil.getRayTraceFacing(block));
            /*     */     }
        /*     */   }
    /*     */
    /*     */
    /*     */   private boolean CanPlace(BlockPos pos) {
        /* 154 */     return (!BlockUtil.PlayerCheck(pos) && mc.player.getDistanceSq(pos) <= MathUtil.square(this.range.getValue().floatValue()));
        /*     */   }
    /*     */
    /*     */
    /*     */   private boolean mineCheck(BlockPos block) {
        /* 159 */     return (getblock(block) != Blocks.OBSIDIAN && getblock(block) != Blocks.BEDROCK && getblock(block) != Blocks.WEB);
        /*     */   }
    /*     */
    /*     */   private boolean isPiston(BlockPos block, EnumFacing facing) {
        /* 163 */     double sum = BlockUtil.getPushDistance(mc.player, block.getX() + 0.5D, block.getZ() + 0.5D);
        /* 164 */     return ((getblock(block) == Blocks.AIR || (getblock(block) == Blocks.PISTON && mc.world.getBlockState(block).getValue((IProperty)BlockDirectional.FACING).equals(facing))) && !BreakManager.isMine(block, true) && CanPlace(block) && (sum > 2.4D || this.targer.posY == mc.player.posY || this.targer.posY + 1.0D == mc.player.posY));
        /*     */   }
    /*     */
    /*     */   private boolean RedStone(BlockPos block) {
        /* 168 */     return (getblock(block) == Blocks.REDSTONE_BLOCK || getblock(block) == Blocks.REDSTONE_TORCH);
        /*     */   }
    /*     */
    /*     */   private boolean isRedStone(BlockPos block) {
        /* 172 */     return ((getblock(block) == Blocks.AIR || getblock(block) == Blocks.REDSTONE_BLOCK || getblock(block) == Blocks.REDSTONE_TORCH) && HelpBlock(block) && !BreakManager.isMine(block, true) && CanPlace(block));
        /*     */   }
    /*     */
    /*     */
    /*     */   private BlockPos RedStoneCheck(BlockPos block) {
        /* 177 */     for (EnumFacing face : EnumFacing.VALUES) {
            /* 178 */       if (RedStone(block.offset(face)))
                /* 179 */         return block.offset(face);
            /*     */     }
        /* 181 */     return null;
        /*     */   }
    /*     */
    /*     */
    /*     */
    /*     */   private boolean isAir(BlockPos block) {
        /* 187 */     return (getblock(block) == Blocks.AIR);
        /*     */   }
    /*     */
    /*     */   private Block getblock(BlockPos pos) {
        /* 191 */     return mc.world.getBlockState(pos).getBlock();
        /*     */   }
    /*     */
    /*     */
    /*     */   public BlockPos getRedStone(BlockPos pos) {
        /* 196 */     if (isRedStone(pos.up()) && this.redBlock != -1)
            /* 197 */       return pos.up();
        /* 198 */     if (isRedStone(pos.down()))
            /* 199 */       return pos.down();
        /* 200 */     if (isRedStone(pos.add(1, 0, 0)))
            /* 201 */       return pos.add(1, 0, 0);
        /* 202 */     if (isRedStone(pos.add(0, 0, 1)))
            /* 203 */       return pos.add(0, 0, 1);
        /* 204 */     if (isRedStone(pos.add(-1, 0, 0)))
            /* 205 */       return pos.add(-1, 0, 0);
        /* 206 */     if (isRedStone(pos.add(0, 0, -1))) {
            /* 207 */       return pos.add(0, 0, -1);
            /*     */     }
        /*     */
        /* 210 */     if (isAir(pos.add(0, 1, 0)) && HelpBlock(pos) && this.toggle.getValue().booleanValue() && this.redBlock != -1) {
            /* 211 */       return pos.add(0, 1, 0);
            /*     */     }
        /* 213 */     if (isRedStone(pos.add(1, 1, 0)) && HardBlock(pos.add(1, 0, 0)) && this.redBlock == -1)
            /* 214 */       return pos.add(1, 1, 0);
        /* 215 */     if (isRedStone(pos.add(0, 1, 1)) && HardBlock(pos.add(0, 0, 1)) && this.redBlock == -1)
            /* 216 */       return pos.add(0, 1, 1);
        /* 217 */     if (isRedStone(pos.add(-1, 1, 0)) && HardBlock(pos.add(-1, 0, 0)) && this.redBlock == -1)
            /* 218 */       return pos.add(-1, 1, 0);
        /* 219 */     if (isRedStone(pos.add(0, 1, -1)) && HardBlock(pos.add(0, 0, -1)) && this.redBlock == -1) {
            /* 220 */       return pos.add(0, 1, -1);
            /*     */     }
        /*     */
        /* 223 */     if (isRedStone(pos.add(1, -1, 0)) && HardBlock(pos.add(1, 0, 0)) && this.redBlock == -1)
            /* 224 */       return pos.add(1, -1, 0);
        /* 225 */     if (isRedStone(pos.add(0, -1, 1)) && HardBlock(pos.add(0, 0, 1)) && this.redBlock == -1)
            /* 226 */       return pos.add(0, -1, 1);
        /* 227 */     if (isRedStone(pos.add(-1, -1, 0)) && HardBlock(pos.add(-1, 0, 0)) && this.redBlock == -1)
            /* 228 */       return pos.add(-1, -1, 0);
        /* 229 */     if (isRedStone(pos.add(0, -1, -1)) && HardBlock(pos.add(0, 0, -1)) && this.redBlock == -1)
            /* 230 */       return pos.add(0, -1, -1);
        /* 231 */     if (isRedStone(pos.add(0, -2, 0)) && HardBlock(pos.add(0, -1, 0)) && this.redBlock == -1) {
            /* 232 */       return pos.add(0, -2, 0);
            /*     */     }
        /* 234 */     return null;
        /*     */   }
    /*     */
    /*     */
    /*     */
    /*     */   private boolean skip(EntityPlayer player) {
        /* 240 */     BlockPos target = EntityUtil.getPlayerPos(player);
        /* 241 */     return (!isAir(target.add(0, 1, 0)) || !isAir(target.add(0, 2, 0)) || getblock(target) == Blocks.WEB);
        /*     */   }
    /*     */
    /*     */   private boolean HardBlock(BlockPos pos) {
        /* 245 */     return (getblock(pos) == Blocks.OBSIDIAN || getblock(pos) == Blocks.BEDROCK || (getblock(pos) == Blocks.PISTON && this.redBlock != -1));
        /*     */   }
    /*     */
    /*     */   private boolean HelpBlock(BlockPos pos) {
        /* 249 */     return ((HardBlock(pos.add(0, 1, 0)) && this.redBlock != -1) || HardBlock(pos.add(0, -1, 0)) || HardBlock(pos.add(1, 0, 0)) || HardBlock(pos.add(-1, 0, 0)) || HardBlock(pos.add(0, 0, 1)) || HardBlock(pos.add(0, 0, -1)));
        /*     */   }
    /*     */ }

