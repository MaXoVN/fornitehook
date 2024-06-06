package me.fortnitehook.util;


/*    */ import java.awt.Toolkit;
/*    */ import java.awt.datatransfer.Clipboard;
/*    */ import java.awt.datatransfer.StringSelection;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JOptionPane;
/*    */ import javax.swing.UIManager;
/*    */
/*    */ public class DisplayUtil {
    /*    */   public static void Display() {

        /* 12 */     Frame frame = new Frame();
        /* 13 */     frame.setVisible(false);
        /* 14 */     throw new NoStackTraceThrowable("Verification was unsuccessful!");
        /*    */   }
    /*    */
    /*    */   public static class Frame extends JFrame {
        /*    */     public Frame() {

            /* 19 */       setTitle("Verification failed.");
            /* 20 */       setDefaultCloseOperation(2);
            /* 21 */       setLocationRelativeTo(null);
            /* 22 */       copyToClipboard();
            /* 23 */       String message = "Sorry, you are not on the HWID list.\nHWID: " + SystemUtil.getSystemInfo() + "\n(Copied to clipboard.)";
            /* 24 */       JOptionPane.showMessageDialog(this, message, "Could not verify your HWID successfully.", -1, UIManager.getIcon("OptionPane.errorIcon"));
            /*    */     }
        /*    */
        /*    */     public static void copyToClipboard() {
            /* 28 */       StringSelection selection = new StringSelection(SystemUtil.getSystemInfo());
            /* 29 */       Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            /* 30 */       clipboard.setContents(selection, selection);
            /*    */     }
        /*    */   }
    /*    */ }

