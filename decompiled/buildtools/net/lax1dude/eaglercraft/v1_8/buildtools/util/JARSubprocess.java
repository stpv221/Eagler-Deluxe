package net.lax1dude.eaglercraft.v1_8.buildtools.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JARSubprocess {
   public static final char classPathSeperator = System.getProperty("os.name").toLowerCase().contains("windows") ? 59 : 58;
   private static final List<Process> activeProcesses = new ArrayList();
   private static boolean shutdownThreadStarted = false;

   public static int runJava(File var0, String[] var1, String var2) throws IOException {
      if (var2.length() > 0 && !var2.endsWith(" ")) {
         var2 = var2 + " ";
      }

      String var3 = System.getProperty("java.home");
      File var4;
      if (classPathSeperator == ';') {
         var4 = new File(var3, "bin/java.exe");
         if (!var4.isFile()) {
            var4 = new File(var3, "java.exe");
            if (!var4.isFile()) {
               throw new IOException("Could not find /bin/java.exe equivelant on java.home! (java.home=" + var3 + ")");
            }
         }

         var3 = var4.getAbsolutePath();
      } else {
         var4 = new File(var3, "bin/java");
         if (!var4.isFile()) {
            var4 = new File(var3, "java");
            if (!var4.isFile()) {
               throw new IOException("Could not find /bin/java equivelant on java.home! (java.home=" + var3 + ")");
            }
         }

         var3 = var4.getAbsolutePath();
      }

      String[] var20 = new String[var1.length + 1];
      var20[0] = var3;
      System.arraycopy(var1, 0, var20, 1, var1.length);
      ProcessBuilder var5 = new ProcessBuilder(var20);
      var5.directory(var0);
      Process var6 = var5.start();
      synchronized(activeProcesses) {
         if (!shutdownThreadStarted) {
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
               public void run() {
                  synchronized(JARSubprocess.activeProcesses) {
                     Iterator var3 = JARSubprocess.activeProcesses.iterator();

                     while(var3.hasNext()) {
                        Process var2 = (Process)var3.next();

                        try {
                           if (var2.isAlive()) {
                              var2.destroy();
                           }
                        } catch (Throwable var4) {
                        }
                     }

                  }
               }
            }, "Subprocess Exit Thread"));
            shutdownThreadStarted = true;
         }

         activeProcesses.add(var6);
      }

      InputStream var7 = var6.getInputStream();
      InputStream var8 = var6.getErrorStream();
      BufferedReader var9 = new BufferedReader(new InputStreamReader(var7));
      BufferedReader var10 = new BufferedReader(new InputStreamReader(var8));
      String var11 = "";
      String var12 = "";
      short var13 = 128;
      boolean var14 = false;

      do {
         boolean var15 = false;

         char var16;
         int var21;
         for(var21 = 0; var9.ready(); var15 = true) {
            if (var10.ready()) {
               ++var21;
               if (var21 >= var13) {
                  break;
               }
            }

            var16 = (char)var9.read();
            if (var16 != '\r') {
               if (var16 == '\n') {
                  System.out.println(var2 + var11);
                  var11 = "";
               } else {
                  var11 = var11 + var16;
               }
            }
         }

         for(var21 = 0; var10.ready(); var15 = true) {
            if (var9.ready()) {
               ++var21;
               if (var21 >= var13) {
                  break;
               }
            }

            var16 = (char)var10.read();
            if (var16 != '\r') {
               if (var16 == '\n') {
                  System.err.println(var2 + var12);
                  var12 = "";
               } else {
                  var12 = var12 + var16;
               }
            }
         }

         if (!var15) {
            try {
               Thread.sleep(10L);
            } catch (InterruptedException var17) {
            }
         }
      } while(var6.isAlive());

      while(true) {
         try {
            return var6.waitFor();
         } catch (InterruptedException var18) {
         }
      }
   }
}
