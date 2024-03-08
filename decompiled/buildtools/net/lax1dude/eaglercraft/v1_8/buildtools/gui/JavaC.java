package net.lax1dude.eaglercraft.v1_8.buildtools.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JavaC {
   public static final boolean windows = System.getProperty("os.name").toLowerCase().contains("windows");
   public static File jdkHome;
   public static final List<String> compilerFlags = Arrays.asList("-Xlint:-unchecked", "-Xlint:-options", "-Xlint:-deprecation", "-source", "1.8", "-target", "1.8");
   private static int debugSourceFileCount = 0;

   static {
      String var0 = windows ? "javac.exe" : "javac";
      File var1 = new File(System.getProperty("java.home"));
      if ((new File(var1, "bin/" + var0)).isFile()) {
         jdkHome = var1;
      } else if ((new File(var1, "../bin/" + var0)).isFile()) {
         jdkHome = var1.getParentFile();
      } else {
         jdkHome = null;
      }

   }

   public static int runJavaC(File var0, File var1, File var2, String[] var3, File... var4) throws IOException {
      if (!var1.exists() && !var1.mkdirs()) {
         throw new IOException("Could not create output directory: " + var1.getAbsolutePath());
      } else if (!var2.exists() && !var2.mkdirs()) {
         throw new IOException("Could not create temporary directory: " + var1.getAbsolutePath());
      } else {
         File var5 = new File(var2, "MinecraftSrc/src_javadoc_tmp");
         if (!var5.exists() && !var5.mkdirs()) {
            throw new IOException("Could not create temporary directory: " + var5.getAbsolutePath());
         } else {
            debugSourceFileCount = 0;
            File var6 = new File(var2, "sourceFiles.txt");
            Throwable var7 = null;
            String var8 = null;

            int var11;
            InputStream var13;
            try {
               PrintWriter var9 = new PrintWriter(new FileWriter(var6));

               try {
                  System.out.println("Extracting decompiled source...");
                  byte[] var10 = new byte[16384];
                  Throwable var12 = null;
                  var13 = null;

                  try {
                     ZipInputStream var14 = new ZipInputStream(new FileInputStream(var0));

                     ZipEntry var15;
                     try {
                        while((var15 = var14.getNextEntry()) != null && !var15.isDirectory()) {
                           String var16 = var15.getName();
                           if (var16.endsWith(".java")) {
                              File var17 = new File(var5, var16);
                              File var18 = var17.getParentFile();
                              if (!var18.exists() && !var18.mkdirs()) {
                                 throw new IOException("Could not create temporary directory: " + var18.getAbsolutePath());
                              }

                              Throwable var19 = null;
                              Object var20 = null;

                              try {
                                 FileOutputStream var21 = new FileOutputStream(var17);

                                 try {
                                    while((var11 = var14.read(var10)) != -1) {
                                       var21.write(var10, 0, var11);
                                    }
                                 } finally {
                                    if (var21 != null) {
                                       var21.close();
                                    }

                                 }
                              } catch (Throwable var73) {
                                 if (var19 == null) {
                                    var19 = var73;
                                 } else if (var19 != var73) {
                                    var19.addSuppressed(var73);
                                 }

                                 throw var19;
                              }

                              var9.println("\"" + var17.getAbsolutePath().replace('\\', '/') + "\"");
                              ++debugSourceFileCount;
                           }
                        }
                     } finally {
                        if (var14 != null) {
                           var14.close();
                        }

                     }
                  } catch (Throwable var75) {
                     if (var12 == null) {
                        var12 = var75;
                     } else if (var12 != var75) {
                        var12.addSuppressed(var75);
                     }

                     throw var12;
                  }

                  System.out.println("Scanning source folder paths...");

                  for(int var83 = 0; var83 < var4.length; ++var83) {
                     discoverSourceFiles(var4[var83], var9);
                  }
               } finally {
                  if (var9 != null) {
                     var9.close();
                  }

               }
            } catch (Throwable var77) {
               if (var7 == null) {
                  var7 = var77;
               } else if (var7 != var77) {
                  var7.addSuppressed(var77);
               }

               throw var7;
            }

            ArrayList var78 = new ArrayList();
            if (windows) {
               var78.add((new File(jdkHome, "bin/javac.exe")).getAbsolutePath());
            } else {
               var78.add((new File(jdkHome, "bin/javac")).getAbsolutePath());
            }

            var78.addAll(compilerFlags);
            var8 = System.getProperty("path.separator");
            var78.add("-classpath");
            var78.add(String.join(var8, var3));
            var78.add("-sourcepath");
            StringBuilder var79 = new StringBuilder();
            var79.append(var0.getAbsolutePath());

            int var80;
            for(var80 = 0; var80 < var4.length; ++var80) {
               var79.append(var8).append(var4[var80].getAbsolutePath());
            }

            var78.add(var79.toString());
            var78.add("-d");
            var78.add(var1.getAbsolutePath());
            var78.add("@" + var6.getAbsolutePath());
            System.out.println();
            var80 = 0;

            for(var11 = var78.size(); var80 < var11; ++var80) {
               String var84 = (String)var78.get(var80);
               if (var84.indexOf(32) != -1) {
                  System.out.print("\"" + var84 + "\"");
               } else {
                  System.out.print(var84);
               }

               System.out.print(' ');
            }

            System.out.println();
            System.out.println();
            System.out.println("Compiling " + debugSourceFileCount + " source files...");
            ProcessBuilder var81 = new ProcessBuilder(var78);
            var81.directory(var2);
            Process var82 = var81.start();
            InputStream var85 = var82.getInputStream();
            var13 = var82.getErrorStream();
            byte[] var86 = new byte[128];

            do {
               boolean var88 = false;
               int var87 = var85.available();
               if (var87 > 0) {
                  if (var87 > 128) {
                     var87 = 128;
                  }

                  var85.read(var86, 0, var87);
                  System.out.write(var86, 0, var87);
                  var88 = true;
               }

               var87 = var13.available();
               if (var87 > 0) {
                  if (var87 > 128) {
                     var87 = 128;
                  }

                  var13.read(var86, 0, var87);
                  System.err.write(var86, 0, var87);
                  var88 = true;
               }

               if (!var88) {
                  try {
                     Thread.sleep(10L);
                  } catch (InterruptedException var70) {
                  }
               }
            } while(var82.isAlive());

            while(true) {
               try {
                  return var82.waitFor();
               } catch (InterruptedException var71) {
               }
            }
         }
      }
   }

   private static void discoverSourceFiles(File var0, PrintWriter var1) throws IOException {
      File[] var2 = var0.listFiles();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         File var4 = var2[var3];
         String var5 = var4.getAbsolutePath();
         if (var4.isDirectory()) {
            discoverSourceFiles(var4, var1);
         } else if (var5.endsWith(".java")) {
            var1.println("\"" + var5.replace('\\', '/') + "\"");
            ++debugSourceFileCount;
         }
      }

   }
}
