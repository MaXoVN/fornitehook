package me.fortnitehook.util;
/*    */
/*    */ public class NoStackTraceThrowable
        /*    */   extends RuntimeException {
    /*    */   public NoStackTraceThrowable(String msg) {
        /*  6 */     super(msg);
        /*  7 */     setStackTrace(new StackTraceElement[0]);
        /*    */   }
    /*    */
    /*    */
    /*    */   public synchronized Throwable fillInStackTrace() {
        /* 12 */     return this;
        /*    */   }
    /*    */ }
