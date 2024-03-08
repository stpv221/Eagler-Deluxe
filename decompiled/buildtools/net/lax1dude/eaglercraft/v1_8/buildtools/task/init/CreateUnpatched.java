package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildToolsConfig;
import org.apache.commons.io.IOUtils;

public class CreateUnpatched {
   public static boolean createUnpatched() {
      try {
         return createUnpatched0();
      } catch (Throwable var1) {
         System.err.println();
         System.err.println("Exception encountered while running task 'unpatched'!");
         var1.printStackTrace();
         return false;
      }
   }

   private static boolean createUnpatched0() throws Throwable {
      File var0 = EaglerBuildToolsConfig.getTemporaryDirectory();
      File var1 = new File(var0, "ModCoderPack");
      File var2 = new File(var0, "MinecraftSrc/minecraft_src.jar");
      File var3 = new File(var0, "MinecraftSrc/minecraft_res.jar");
      File var4 = new File("./MinecraftSrc.zip");
      if (var4.exists()) {
         System.err.println("ERROR: The file 'MinecraftSrc.zip' already exists in this directory!");
         System.err.println("Delete it and re-run 'unpatched' to try again");
         return false;
      } else if (!var1.isDirectory()) {
         System.err.println("The '" + var1.getName() + "' directory was not found in the temporary directory!");
         System.err.println("Please run the 'init' command to create it");
         return false;
      } else if (!var2.isFile()) {
         System.err.println("The '" + var2.getName() + "' file was not found in the temporary directory!");
         System.err.println("Please run the 'init' command to create it");
         return false;
      } else if (!var3.isFile()) {
         System.err.println("The '" + var3.getName() + "' file was not found in the temporary directory!");
         System.err.println("Please run the 'init' command to create it");
         return false;
      } else {
         File var5 = new File(var0, "MinecraftSrc/minecraft_unpatched_javadoc.jar");
         System.out.println();
         System.out.println("Preparing source in '" + var2.getName() + "'...");
         System.out.println();
         CSVMappings var6 = new CSVMappings();
         InsertJavaDoc.processSource(var2, var5, var1, var6, false);
         Throwable var7 = null;
         Object var8 = null;

         try {
            ZipOutputStream var9 = new ZipOutputStream(new FileOutputStream(var4));

            try {
               var9.setLevel(0);
               System.out.println("Extracting '" + var5.getName() + "' into '" + var4.getName() + "'...");
               Throwable var11 = null;
               Object var12 = null;

               int var10;
               FileInputStream var13;
               try {
                  var13 = new FileInputStream(var5);

                  try {
                     var10 = extractZipTo(new ZipInputStream(var13), var9, "src");
                  } finally {
                     if (var13 != null) {
                        var13.close();
                     }

                  }
               } catch (Throwable var53) {
                  if (var11 == null) {
                     var11 = var53;
                  } else if (var11 != var53) {
                     var11.addSuppressed(var53);
                  }

                  throw var11;
               }

               System.out.println("Extracted " + var10 + " files.");
               System.out.println();
               System.out.println("Extracting '" + var3.getName() + "' into '" + var4.getName() + "'...");
               var11 = null;
               var12 = null;

               try {
                  var13 = new FileInputStream(var3);

                  try {
                     var10 = extractZipTo(new ZipInputStream(var13), var9, "res");
                  } finally {
                     if (var13 != null) {
                        var13.close();
                     }

                  }
               } catch (Throwable var51) {
                  if (var11 == null) {
                     var11 = var51;
                  } else if (var11 != var51) {
                     var11.addSuppressed(var51);
                  }

                  throw var11;
               }

               System.out.println("Extracted " + var10 + " files.");
            } finally {
               if (var9 != null) {
                  var9.close();
               }

            }
         } catch (Throwable var55) {
            if (var7 == null) {
               var7 = var55;
            } else if (var7 != var55) {
               var7.addSuppressed(var55);
            }

            throw var7;
         }

         if (!var5.delete()) {
            System.err.println();
            System.err.println("ERROR: failed to delete '" + var5.getName() + "' from temporary directory!");
         }

         return true;
      }
   }

   private static int extractZipTo(ZipInputStream var0, ZipOutputStream var1, String var2) throws IOException {
      int var3 = 0;

      ZipEntry var4;
      while((var4 = var0.getNextEntry()) != null) {
         if (!var4.isDirectory()) {
            String var5 = var4.getName();
            if (var5.startsWith("/")) {
               var5 = var5.substring(1);
            }

            if (!var5.startsWith("META-INF")) {
               ZipEntry var6 = new ZipEntry(var2 + "/" + var5);
               var1.putNextEntry(var6);
               IOUtils.copy(var0, var1, 8192);
               ++var3;
            }
         }
      }

      return var3;
   }
}
