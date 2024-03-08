package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class MinecraftLocator {
   private static boolean hasTriedToFind = false;
   private static File directory = null;

   private static File locateOrCopyFile(String var0, String var1) {
      File var2 = new File("./mcp918/" + var0);
      if (var2.isFile()) {
         return var2;
      } else {
         if (!hasTriedToFind) {
            hasTriedToFind = true;
            String var3 = System.getProperty("os.name").toLowerCase();
            if (var3.contains("win")) {
               String var4 = System.getenv("APPDATA");
               if (var4 != null) {
                  directory = new File(var4, ".minecraft");
               } else {
                  directory = new File(System.getProperty("user.home"), ".minecraft");
               }
            } else if (var3.contains("mac")) {
               directory = new File(System.getProperty("user.home"), "Library/Application Support/minecraft");
            } else {
               directory = new File(System.getProperty("user.home"), ".minecraft");
            }

            if (!directory.isDirectory()) {
               directory = new File(System.getProperty("user.home"), "minecraft");
               if (!directory.isDirectory()) {
                  directory = null;
               }
            }
         }

         if (directory == null) {
            return null;
         } else {
            File var6 = new File(directory, var1);
            if (var6.isFile()) {
               try {
                  System.out.println("Copying '" + var1 + "' from your .minecraft directory into './mcp918'...");
                  FileUtils.copyFile(var6, var2, true);
                  return var2;
               } catch (IOException var5) {
                  System.err.println("ERROR: failed to copy '" + var1 + "' from your .minecraft directory into './mcp918'!");
                  var5.printStackTrace();
                  return null;
               }
            } else {
               return null;
            }
         }
      }
   }

   public static File locateMinecraftVersionJar(String var0) {
      return locateOrCopyFile(var0 + ".jar", "versions/" + var0 + "/" + var0 + ".jar");
   }

   public static File locateMinecraftVersionAssets(String var0) {
      return locateOrCopyFile(var0 + ".json", "assets/indexes/" + var0 + ".json");
   }
}
