package me.fortnitehook.util;
/*    */ import org.apache.commons.codec.digest.DigestUtils;
/*    */
/*    */
/*    */
/*    */ public class SystemUtil
        /*    */ {
    /*    */   public static String getSystemInfo() {
        /* 10 */     return DigestUtils.sha256Hex(DigestUtils.sha256Hex(System.getenv("os") +
                /* 11 */           System.getProperty("os.name") +
                /* 12 */           System.getProperty("os.arch") +
                /* 13 */           System.getProperty("user.name") +
                /* 14 */           System.getenv("SystemRoot") +
                /* 15 */           System.getenv("HOMEDRIVE") +
                /* 16 */           System.getenv("PROCESSOR_LEVEL") +
                /* 17 */           System.getenv("PROCESSOR_REVISION") +
                /* 18 */           System.getenv("PROCESSOR_IDENTIFIER") +
                /* 19 */           System.getenv("PROCESSOR_ARCHITECTURE") +
                /* 20 */           System.getenv("PROCESSOR_ARCHITEW6432") +
                /* 21 */           System.getenv("NUMBER_OF_PROCESSORS")));
        /*    */   }
    /*    */
    /*    */
    /*    */   public static String getAll() {
        /* 26 */     return "true";
        /*    */   }
    /*    */ }
