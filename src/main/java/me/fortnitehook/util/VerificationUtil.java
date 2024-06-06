package me.fortnitehook.util;

/*    */ import java.io.BufferedReader;
/*    */ import java.io.InputStreamReader;
/*    */ import java.net.URL;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */
/*    */
/*    */
/*    */
/*    */ public class VerificationUtil
        /*    */ {
    /*    */   public static List<String> readURL() {
        /* 15 */     List<String> s = new ArrayList<>();
        /*    */     try {
            /* 17 */       URL url = new URL("https://pastebin.com/raw/eiBZJYXU");
            /* 18 */       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            /*    */       String hwid;
            /* 20 */       while ((hwid = bufferedReader.readLine()) != null) {
                /* 21 */         s.add(hwid);
                /*    */       }
            /* 23 */     } catch (Exception exception) {}
        /*    */
        /*    */
        /* 26 */     return s;
        /*    */   }
    /*    */ }

