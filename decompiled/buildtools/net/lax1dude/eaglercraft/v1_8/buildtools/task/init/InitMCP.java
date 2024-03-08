package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;

public class InitMCP {
   public static boolean initTask(File var0, File var1) throws Throwable {
      File var2 = new File(var1, "runtime.jar");
      String[] var3 = new String[]{"mcinjector.jar", "specialsource.jar"};
      boolean[] var4 = new boolean[var3.length];
      String[] var5 = new String[]{"exceptor.json", "fields.csv", "joined.exc", "joined.srg", "methods.csv", "params.csv", "fernflower.jar"};
      boolean[] var6 = new boolean[var5.length];
      HashSet var7 = new HashSet();
      System.out.println();
      System.out.println("Extracting \"" + var0.getAbsolutePath() + "\" to \"" + var1.getAbsolutePath() + "\"...");

      CSVMappings var9;
      try {
         Throwable var8 = null;
         var9 = null;

         try {
            ZipInputStream var10 = new ZipInputStream(new FileInputStream(var0));

            try {
               ZipOutputStream var11 = new ZipOutputStream(new FileOutputStream(var2));

               try {
                  var11.setLevel(0);
                  var11.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
                  var11.write("Manifest-Version: 1.0\nCreated-By: Eaglercraft BuildTools\n".getBytes(StandardCharsets.UTF_8));

                  ZipEntry var12;
                  label1109:
                  while((var12 = var10.getNextEntry()) != null) {
                     String var13 = var12.getName();
                     if (var13.startsWith("/")) {
                        var13 = var13.substring(1);
                     }

                     int var14;
                     ZipEntry var16;
                     for(var14 = 0; var14 < var3.length; ++var14) {
                        if (var13.endsWith(var3[var14])) {
                           System.out.println("   " + var3[var14] + " -> " + var2.getName());
                           ZipInputStream var15 = new ZipInputStream(var10);

                           while((var16 = var15.getNextEntry()) != null) {
                              if (!var16.isDirectory()) {
                                 String var17 = var16.getName();
                                 int var18 = var17.indexOf("META-INF");
                                 if (var18 != 0 && var18 != 1 && var7.add(var17)) {
                                    ZipEntry var19 = new ZipEntry(var16.getName());
                                    var11.putNextEntry(var19);
                                    IOUtils.copy(var15, var11, 4096);
                                 }
                              }
                           }

                           var4[var14] = true;
                           continue label1109;
                        }
                     }

                     for(var14 = 0; var14 < var5.length; ++var14) {
                        if (var13.endsWith(var5[var14])) {
                           System.out.println("   " + var5[var14] + " -> " + var5[var14]);
                           Throwable var60 = null;
                           var16 = null;

                           try {
                              FileOutputStream var61 = new FileOutputStream(new File(var1, var5[var14]));

                              try {
                                 IOUtils.copy(var10, var61, 32768);
                              } finally {
                                 if (var61 != null) {
                                    var61.close();
                                 }

                              }
                           } catch (Throwable var51) {
                              if (var60 == null) {
                                 var60 = var51;
                              } else if (var60 != var51) {
                                 var60.addSuppressed(var51);
                              }

                              throw var60;
                           }

                           var6[var14] = true;
                           break;
                        }
                     }
                  }
               } finally {
                  if (var11 != null) {
                     var11.close();
                  }

               }
            } catch (Throwable var53) {
               if (var8 == null) {
                  var8 = var53;
               } else if (var8 != var53) {
                  var8.addSuppressed(var53);
               }

               if (var10 != null) {
                  var10.close();
               }

               throw var8;
            }

            if (var10 != null) {
               var10.close();
            }
         } catch (Throwable var54) {
            if (var8 == null) {
               var8 = var54;
            } else if (var8 != var54) {
               var8.addSuppressed(var54);
            }

            throw var8;
         }
      } catch (IOException var55) {
         System.err.println("ERROR: failed to extract \"" + var0.getAbsolutePath() + "\" to \"" + var1.getAbsolutePath() + "\"!");
         var55.printStackTrace();
         return false;
      }

      boolean var57 = false;

      int var56;
      for(var56 = 0; var56 < var3.length; ++var56) {
         if (!var4[var56]) {
            var57 = true;
            System.err.println("JAR not found: \"" + var3[var56] + "\"!");
         }
      }

      for(var56 = 0; var56 < var5.length; ++var56) {
         if (!var6[var56]) {
            var57 = true;
            System.err.println("Config not found: \"" + var5[var56] + "\"!");
         }
      }

      if (var57) {
         System.err.println("ERROR: Could not extract all required MCP files from \"" + var0.getName() + "\"!");
         return false;
      } else {
         var9 = new CSVMappings();
         File var58 = new File(var1, "minecraft.srg");
         if (!GenerateSRGs.generate(var1, var58, var9)) {
            System.err.println("ERROR: could not generate joined \"minecraft.srg\" file from conf in \"" + var1.getAbsolutePath() + "\"!");
            return false;
         } else {
            File var59 = new File(var1, "minecraft.exc");
            if (!GenerateEXCs.generateEXCs(var1, var59, var9)) {
               System.err.println("ERROR: could not generate joined \"minecraft.exc\" file from conf in \"" + var1.getAbsolutePath() + "\"!");
               return false;
            } else {
               return true;
            }
         }
      }
   }
}
