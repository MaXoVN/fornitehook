package me.fortnitehook.manager;

/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import me.fortnitehook.features.Feature;
/*    */ import me.fortnitehook.features.modules.player.FortniteMine;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.play.server.SPacketBlockBreakAnim;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */
/*    */ public class BreakManager
        /*    */   extends Feature
        /*    */ {
    /* 14 */   public static Map<String, BlockPos> map = new HashMap<>();
    /*    */
    /*    */   public static boolean isMine(BlockPos pos, boolean self) {
        /* 17 */     for (Map.Entry<String, BlockPos> block : map.entrySet()) {
            /* 18 */       if (block.getValue().equals(pos)) {
                /* 19 */         return true;
                /*    */       }
            /*    */     }
        /* 22 */     return (self && FortniteMine.breakPos != null && FortniteMine.breakPos.equals(pos));
        /*    */   }
    /*    */
    /*    */   public static boolean SelfMine(BlockPos pos) {
        /* 26 */     return (FortniteMine.breakPos != null && FortniteMine.breakPos.equals(pos));
        /*    */   }
    /*    */
    /*    */   public void onPacketReceive(SPacketBlockBreakAnim packet) {
        /* 30 */     BlockPos pos = packet.getPosition();
        /* 31 */     Entity breaker = mc.world.getEntityByID(packet.getBreakerId());
        /* 32 */     if (breaker == null)
            /* 33 */       return;  map.put(breaker.getName(), pos);
        /*    */   }
    /*    */ }
