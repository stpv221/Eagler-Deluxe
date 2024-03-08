package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import net.lax1dude.eaglercraft.v1_8.buildtools.util.FileReaderUTF;
import net.lax1dude.eaglercraft.v1_8.buildtools.util.FileWriterUTF;

public class GenerateSRGs {
   public static boolean generate(File var0, File var1, CSVMappings var2) throws Throwable {
      System.out.println();
      System.out.println("Generating \"" + var1.getName() + "\" from \"" + var0.getName() + "\"...");
      File var3 = new File(var0, "methods.csv");

      Throwable var5;
      FileReaderUTF var6;
      try {
         Throwable var4 = null;
         var5 = null;

         try {
            var6 = new FileReaderUTF(var3);

            try {
               var2.loadMethodsFile(var6);
            } finally {
               if (var6 != null) {
                  var6.close();
               }

            }
         } catch (Throwable var92) {
            if (var4 == null) {
               var4 = var92;
            } else if (var4 != var92) {
               var4.addSuppressed(var92);
            }

            throw var4;
         }
      } catch (IOException var93) {
         System.err.println("ERROR: failed to read \"" + var3.getAbsolutePath() + "\"!");
         var93.printStackTrace();
         return false;
      }

      File var98 = new File(var0, "fields.csv");

      try {
         var5 = null;
         var6 = null;

         try {
            FileReaderUTF var7 = new FileReaderUTF(var98);

            try {
               var2.loadFieldsFile(var7);
            } finally {
               if (var7 != null) {
                  var7.close();
               }

            }
         } catch (Throwable var89) {
            if (var5 == null) {
               var5 = var89;
            } else if (var5 != var89) {
               var5.addSuppressed(var89);
            }

            throw var5;
         }
      } catch (IOException var90) {
         System.err.println("ERROR: failed to read \"" + var98.getAbsolutePath() + "\"!");
         var90.printStackTrace();
         return false;
      }

      int var99 = 0;
      int var100 = 0;
      int var101 = 0;

      try {
         Throwable var8 = null;
         Object var9 = null;

         try {
            BufferedReader var10 = new BufferedReader(new FileReaderUTF(new File(var0, "joined.srg")));

            try {
               PrintWriter var11 = new PrintWriter(new FileWriterUTF(var1));

               String var12;
               try {
                  while((var12 = var10.readLine()) != null) {
                     int var13;
                     String var14;
                     if (var12.startsWith("MD:")) {
                        var13 = var12.lastIndexOf(32);
                        var14 = var12.substring(var13 + 1);
                        var12 = var12.substring(0, var13);
                        int var15 = var12.lastIndexOf(47);
                        String var16 = var12.substring(var15 + 1);
                        var12 = var12.substring(0, var15);
                        CSVMappings.Symbol var17 = (CSVMappings.Symbol)var2.csvMethodsMappings.get(var16);
                        if (var17 != null) {
                           ++var100;
                           var16 = var17.name;
                        }

                        var11.println(var12 + "/" + var16 + " " + var14);
                     } else if (var12.startsWith("FD:")) {
                        var13 = var12.lastIndexOf(47);
                        var14 = var12.substring(var13 + 1);
                        var12 = var12.substring(0, var13);
                        CSVMappings.Symbol var102 = (CSVMappings.Symbol)var2.csvFieldsMappings.get(var14);
                        if (var102 != null) {
                           ++var101;
                           var14 = var102.name;
                        }

                        var11.println(var12 + "/" + var14);
                     } else if (var12.startsWith("CL:")) {
                        ++var99;
                        var11.println(var12);
                     } else {
                        var11.println(var12);
                     }
                  }
               } finally {
                  if (var11 != null) {
                     var11.close();
                  }

               }
            } catch (Throwable var95) {
               if (var8 == null) {
                  var8 = var95;
               } else if (var8 != var95) {
                  var8.addSuppressed(var95);
               }

               if (var10 != null) {
                  var10.close();
               }

               throw var8;
            }

            if (var10 != null) {
               var10.close();
            }
         } catch (Throwable var96) {
            if (var8 == null) {
               var8 = var96;
            } else if (var8 != var96) {
               var8.addSuppressed(var96);
            }

            throw var8;
         }
      } catch (IOException var97) {
         System.err.println("ERROR: failed to write \"" + var1.getName() + "\" from \"joined.srg\"!");
         var97.printStackTrace();
         return false;
      }

      System.out.println("   - Deobf " + var99 + " classes to \"" + var1.getName() + "\"");
      System.out.println("   - Deobf " + var100 + " methods to \"" + var1.getName() + "\"");
      System.out.println("   - Deobf " + var101 + " fields to \"" + var1.getName() + "\"");
      return true;
   }
}
