package me.fortnitehook.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import java.util.function.BiPredicate;

import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.*;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class DamageUtil
        implements Util {
    public static int getRoundedDamage(ItemStack stack) {
        return (int) DamageUtil.getDamageInPercent(stack);
    }

    public static float getDamageInPercent(ItemStack stack) {
        return (float) (DamageUtil.getItemDamage(stack) / stack.getMaxDamage()) * 100.0f;
    }

    public static boolean isArmorLow(EntityPlayer player, int durability) {
        for (ItemStack piece : player.inventory.armorInventory) {
            if (piece == null) {
                return true;
            }
            if (DamageUtil.getItemDamage(piece) >= durability) continue;
            return true;
        }
        return false;
    }

    public static int getItemDamage(ItemStack stack) {
        return stack.getMaxDamage() - stack.getItemDamage();
    }

    public static boolean canTakeDamage(boolean suicide) {
        return !mc.player.capabilities.isCreativeMode && !suicide;
    }

    public static RayTraceResult rayTraceBlocks(World world, Vec3d start, Vec3d end, int attempts, BlockPos.MutableBlockPos mutablePos) {
        /* 175 */     return rayTraceBlocks(world, start, end, attempts, mutablePos, (pos, state) -> (state.getCollisionBoundingBox(world, pos) != null));
        /*     */   }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */   private static RayTraceResult rayTraceBlocks(World world, Vec3d start, Vec3d end, int attempts, BlockPos.MutableBlockPos mutablePos, BiPredicate<BlockPos, IBlockState> predicate) {
        /* 186 */     if (!Double.isNaN(start.x) && !Double.isNaN(start.y) && !Double.isNaN(start.z) &&
                /* 187 */       !Double.isNaN(end.x) && !Double.isNaN(end.y) && !Double.isNaN(end.z)) {
            /* 188 */       int currentX = MathHelper.floor(start.x);
            /* 189 */       int currentY = MathHelper.floor(start.y);
            /* 190 */       int currentZ = MathHelper.floor(start.z);
            /*     */
            /* 192 */       int endXFloor = MathHelper.floor(end.x);
            /* 193 */       int endYFloor = MathHelper.floor(end.y);
            /* 194 */       int endZFloor = MathHelper.floor(end.z);
            /*     */
            /* 196 */       IBlockState startBlockState = world.getBlockState(mutablePos.setPos(currentX, currentY, currentZ));
            /* 197 */       Block startBlock = startBlockState.getBlock();
            /*     */
            /* 199 */       if (startBlock.canCollideCheck(startBlockState, false) && predicate.test(mutablePos, startBlockState)) {
                /* 200 */         RayTraceResult result = startBlockState.collisionRayTrace(world, mutablePos, start, end);
                /*     */
                /* 202 */         if (result != null) return result;
                /*     */
                /*     */       }
            /* 205 */       int counter = attempts;
            /* 206 */       while (counter-- >= 0) {
                /* 207 */         EnumFacing side; if (Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z)) {
                    /* 208 */           return null;
                    /*     */         }
                /*     */
                /* 211 */         if (currentX == endXFloor && currentY == endYFloor && currentZ == endZFloor) {
                    /* 212 */           return null;
                    /*     */         }
                /*     */
                /* 215 */         double totalDiffX = end.x - start.x;
                /* 216 */         double totalDiffY = end.y - start.y;
                /* 217 */         double totalDiffZ = end.z - start.z;
                /*     */
                /* 219 */         double nextX = 999.0D;
                /* 220 */         double nextY = 999.0D;
                /* 221 */         double nextZ = 999.0D;
                /*     */
                /* 223 */         double diffX = 999.0D;
                /* 224 */         double diffY = 999.0D;
                /* 225 */         double diffZ = 999.0D;
                /*     */
                /* 227 */         if (endXFloor > currentX) {
                    /* 228 */           nextX = currentX + 1.0D;
                    /* 229 */           diffX = (nextX - start.x) / totalDiffX;
                    /* 230 */         } else if (endXFloor < currentX) {
                    /* 231 */           nextX = currentX;
                    /* 232 */           diffX = (nextX - start.x) / totalDiffX;
                    /*     */         }
                /*     */
                /* 235 */         if (endYFloor > currentY) {
                    /* 236 */           nextY = currentY + 1.0D;
                    /* 237 */           diffY = (nextY - start.y) / totalDiffY;
                    /* 238 */         } else if (endYFloor < currentY) {
                    /* 239 */           nextY = currentY;
                    /* 240 */           diffY = (nextY - start.y) / totalDiffY;
                    /*     */         }
                /*     */
                /* 243 */         if (endZFloor > currentZ) {
                    /* 244 */           nextZ = currentZ + 1.0D;
                    /* 245 */           diffZ = (nextZ - start.z) / totalDiffZ;
                    /* 246 */         } else if (endZFloor < currentZ) {
                    /* 247 */           nextZ = currentZ;
                    /* 248 */           diffZ = (nextZ - start.z) / totalDiffZ;
                    /*     */         }
                /*     */
                /* 251 */         if (diffX == -0.0D) diffX = -1.0E-4D;
                /* 252 */         if (diffY == -0.0D) diffY = -1.0E-4D;
                /* 253 */         if (diffZ == -0.0D) diffZ = -1.0E-4D;
                /*     */
                /*     */
                /* 256 */         if (diffX < diffY && diffX < diffZ) {
                    /* 257 */           side = (endXFloor > currentX) ? EnumFacing.WEST : EnumFacing.EAST;
                    /* 258 */           start = new Vec3d(nextX, start.y + totalDiffY * diffX, start.z + totalDiffZ * diffX);
                    /* 259 */         } else if (diffY < diffZ) {
                    /* 260 */           side = (endYFloor > currentY) ? EnumFacing.DOWN : EnumFacing.UP;
                    /* 261 */           start = new Vec3d(start.x + totalDiffX * diffY, nextY, start.z + totalDiffZ * diffY);
                    /*     */         } else {
                    /* 263 */           side = (endZFloor > currentZ) ? EnumFacing.NORTH : EnumFacing.SOUTH;
                    /* 264 */           start = new Vec3d(start.x + totalDiffX * diffZ, start.y + totalDiffY * diffZ, nextZ);
                    /*     */         }
                /*     */
                /* 267 */         currentX = MathHelper.floor(start.x) - ((side == EnumFacing.EAST) ? 1 : 0);
                /* 268 */         currentY = MathHelper.floor(start.y) - ((side == EnumFacing.UP) ? 1 : 0);
                /* 269 */         currentZ = MathHelper.floor(start.z) - ((side == EnumFacing.SOUTH) ? 1 : 0);
                /* 270 */         mutablePos.setPos(currentX, currentY, currentZ);
                /*     */
                /* 272 */         IBlockState state = world.getBlockState(mutablePos);
                /* 273 */         Block block = state.getBlock();
                /*     */
                /* 275 */         if (block.canCollideCheck(state, false) && predicate.test(mutablePos, state)) {
                    /* 276 */           RayTraceResult result = state.collisionRayTrace(world, mutablePos, start, end);
                    /*     */
                    /* 278 */           if (result != null) return result;
                    /*     */
                    /*     */         }
                /*     */       }
            /*     */     }
        /*     */
        /*     */
        /* 285 */     return null;
        /*     */   }



    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0f;
        double distancedsize = entity.getDistance(posX, posY, posZ) / (double) doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        } catch (Exception exception) {
            // empty catch block
        }
        double v = (1.0 - distancedsize) * blockDensity;
        float damage = (int) ((v * v + v) / 2.0 * 7.0 * (double) doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = DamageUtil.getBlastReduction((EntityLivingBase) entity, DamageUtil.getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float) finald;
    }
    public static float calculateDamage(double posX, double posY, double posZ, Entity entity, boolean ignoreTerrain) {
        float finalDamage = 1.0f;
        try {
            float doubleExplosionSize = 12.0F;
            double distancedSize = entity.getDistance(posX, posY, posZ) / (double) doubleExplosionSize;
            double blockDensity = ignoreTerrain ?
                    ignoreTerrainDecntiy(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox(), mc.world)
                    : entity.world.getBlockDensity(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());
            double v = (1.0D - distancedSize) * blockDensity;
            float damage = (float) ((int) ((v * v + v) / 2.0D * 7.0D * (double) doubleExplosionSize + 1.0D));

            if (entity instanceof EntityLivingBase) {
                finalDamage = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6F, false, true));
            }
        } catch (NullPointerException ignored) {
        }

        return finalDamage;
    }

    public static float calculateDamage(Entity crystal, Entity entity) {
        return DamageUtil.calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }



    private static float getBlockDensity(Vec3d vec, AxisAlignedBB bb, BlockPos.MutableBlockPos mutablePos) {
        /* 128 */     double x = 1.0D / ((bb.maxX - bb.minX) * 2.0D + 1.0D);
        /* 129 */     double y = 1.0D / ((bb.maxY - bb.minY) * 2.0D + 1.0D);
        /* 130 */     double z = 1.0D / ((bb.maxZ - bb.minZ) * 2.0D + 1.0D);
        /* 131 */     double xFloor = (1.0D - Math.floor(1.0D / x) * x) / 2.0D;
        /* 132 */     double zFloor = (1.0D - Math.floor(1.0D / z) * z) / 2.0D;
        /*     */
        /* 134 */     if (x >= 0.0D && y >= 0.0D && z >= 0.0D) {
            /* 135 */       int air = 0;
            /* 136 */       int traced = 0;
            /*     */       float a;
            /* 138 */       for (a = 0.0F; a <= 1.0F; a = (float)(a + x)) {
                /* 139 */         float b; for (b = 0.0F; b <= 1.0F; b = (float)(b + y)) {
                    /* 140 */           float c; for (c = 0.0F; c <= 1.0F; c = (float)(c + z)) {
                        /* 141 */             double xOff = bb.minX + (bb.maxX - bb.minX) * a;
                        /* 142 */             double yOff = bb.minY + (bb.maxY - bb.minY) * b;
                        /* 143 */             double zOff = bb.minZ + (bb.maxZ - bb.minZ) * c;
                        /*     */
                        /* 145 */             RayTraceResult result = rayTraceBlocks(mc.world, new Vec3d(xOff + xFloor, yOff, zOff + zFloor), vec, 20, mutablePos, DamageUtil::isResistant);
                        /*     */
                        /*     */
                        /*     */
                        /*     */
                        /*     */
                        /*     */
                        /*     */
                        /*     */
                        /* 154 */             if (result == null) {
                            /* 155 */               air++;
                            /*     */             }
                        /*     */
                        /* 158 */             traced++;
                        /*     */           }
                    /*     */         }
                /*     */       }
            /* 162 */       return air / traced;
            /*     */     }
        /* 164 */     return 0.0F;
        /*     */   }
    private static boolean isResistant(BlockPos pos, IBlockState state) {
        /* 290 */     return (!state.getMaterial().isLiquid() && state
/* 291 */       .getBlock().getExplosionResistance(mc.world, pos, null, null) >= 19.7D);
        /*     */   }
    /*     */
    /*     */

    public static float ignoreTerrainDecntiy(Vec3d vec, AxisAlignedBB bb, World world) {
        double d0 = 1.0D / ((bb.maxX - bb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((bb.maxY - bb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((bb.maxZ - bb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;

        if (d0 >= 0.0D && d1 >= 0.0D && d2 >= 0.0D)
        {
            int j2 = 0;
            int k2 = 0;

            for (float f = 0.0F; f <= 1.0F; f = (float)((double)f + d0))
            {
                for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float)((double)f1 + d1))
                {
                    for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float)((double)f2 + d2))
                    {
                        double d5 = bb.minX + (bb.maxX - bb.minX) * (double)f;
                        double d6 = bb.minY + (bb.maxY - bb.minY) * (double)f1;
                        double d7 = bb.minZ + (bb.maxZ - bb.minZ) * (double)f2;
                        RayTraceResult result;

                        if ( (result = world.rayTraceBlocks(new Vec3d(d5 + d3, d6, d7 + d4), vec)) == null)
                        {
                            ++j2;
                        } else {
                            Block blockHit = BlockUtil.getBlock(result.getBlockPos());
                            if (blockHit.blockResistance < 600)
                                ++j2;
                        }

                        ++k2;
                    }
                }
            }

            return (float)j2 / (float)k2;
        }
        else
        {
            return 0.0F;
        }

    }






    public static float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) entity;
            DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            int k = 0;
            try {
                k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            } catch (Exception exception) {
                // empty catch block
            }
            float f = MathHelper.clamp((float) k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    public static boolean isNaked(EntityPlayer player) {
        for (ItemStack piece : player.inventory.armorInventory) {
            if (piece == null || piece.isEmpty()) continue;
            return false;
        }
        return true;
    }



    public static float getDamageMultiplied(float damage) {
        int diff = mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static float calculateDamage(BlockPos pos, Entity entity) {
        return DamageUtil.calculateDamage((double) pos.getX() + 0.5, pos.getY() + 1, (double) pos.getZ() + 0.5, entity);
    }


    public static boolean canBreakWeakness(EntityPlayer player) {
        int strengthAmp = 0;
        PotionEffect effect = DamageUtil.mc.player.getActivePotionEffect(MobEffects.STRENGTH);
        if (effect != null) {
            strengthAmp = effect.getAmplifier();
        }
        return !DamageUtil.mc.player.isPotionActive(MobEffects.WEAKNESS) || strengthAmp >= 1 || DamageUtil.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || DamageUtil.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe || DamageUtil.mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe || DamageUtil.mc.player.getHeldItemMainhand().getItem() instanceof ItemSpade;
    }



    public static int getCooldownByWeapon(EntityPlayer player) {
        Item item = player.getHeldItemMainhand().getItem();
        if (item instanceof ItemSword) {
            return 600;
        }
        if (item instanceof ItemPickaxe) {
            return 850;
        }
        if (item == Items.IRON_AXE) {
            return 1100;
        }
        if (item == Items.STONE_HOE) {
            return 500;
        }
        if (item == Items.IRON_HOE) {
            return 350;
        }
        if (item == Items.WOODEN_AXE || item == Items.STONE_AXE) {
            return 1250;
        }
        if (item instanceof ItemSpade || item == Items.GOLDEN_AXE || item == Items.DIAMOND_AXE || item == Items.WOODEN_HOE || item == Items.GOLDEN_HOE) {
            return 1000;
        }
        return 250;
    }
}

