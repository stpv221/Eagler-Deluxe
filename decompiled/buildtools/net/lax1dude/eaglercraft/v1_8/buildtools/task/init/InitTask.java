package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildTools;
import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildToolsConfig;
import org.apache.commons.io.FileUtils;

public class InitTask {
   private static File locatedMCPZip = null;
   private static File locatedMinecraftJar = null;
   private static File locatedAssetsJson = null;

   public static boolean initTask() {
      try {
         return initTask0();
      } catch (Throwable var1) {
         System.err.println();
         System.err.println("Exception encountered while running task 'init'!");
         var1.printStackTrace();
         return false;
      }
   }

   private static boolean initTask0() throws Throwable {
      System.out.println("Scanning 'mcp918' folder...");
      File var0 = new File(EaglerBuildTools.repositoryRoot, "mcp918");
      if (!var0.isDirectory()) {
         System.err.println("ERROR: \"" + var0.getAbsolutePath() + "\" is not a directory!");
         return false;
      } else {
         File[] var4;
         int var3 = (var4 = var0.listFiles()).length;

         File var1;
         for(int var2 = 0; var2 < var3; ++var2) {
            var1 = var4[var2];
            if (var1.getName().equalsIgnoreCase("mcp918.zip")) {
               locatedMCPZip = var1;
            }

            if (locatedMCPZip == null && var1.getName().endsWith(".zip")) {
               locatedMCPZip = var1;
            }

            if (var1.getName().equalsIgnoreCase("1.8.8.jar")) {
               locatedMinecraftJar = var1;
            }

            if (locatedMinecraftJar == null && var1.getName().endsWith(".jar")) {
               locatedMinecraftJar = var1;
            }

            if (var1.getName().equalsIgnoreCase("1.8.json")) {
               locatedAssetsJson = var1;
            }

            if (locatedAssetsJson == null && var1.getName().endsWith(".json")) {
               locatedAssetsJson = var1;
            }
         }

         if (locatedMCPZip == null) {
            System.err.println("ERROR: could not find ./mcp918/mcp918.zip! Please locate it and copy it into the 'mcp918' folder.");
            return false;
         } else {
            if (locatedMinecraftJar == null) {
               locatedMinecraftJar = MinecraftLocator.locateMinecraftVersionJar("1.8.8");
               if (locatedMinecraftJar == null) {
                  System.err.println("ERROR: could not find ./mcp918/1.8.8.jar! Please locate it and copy it into the 'mcp918' folder.");
                  return false;
               }
            }

            if (locatedAssetsJson == null) {
               locatedAssetsJson = MinecraftLocator.locateMinecraftVersionAssets("1.8");
               if (locatedAssetsJson == null) {
                  System.err.println("ERROR: could not find ./mcp918/1.8.json! Please locate it and copy it into the 'mcp918' folder.");
                  return false;
               }
            }

            FFMPEG.confirmFFMPEG();
            var1 = EaglerBuildToolsConfig.getTemporaryDirectory();
            boolean var8 = var1.exists();
            if (var8 && (!var1.isDirectory() || var1.list().length != 0)) {
               System.out.println();
               System.out.println("Notice: BuildTools is already initialized.");
               System.out.println();
               System.out.println("you must revert all changes in the 'patches' directory of");
               System.out.println("this repo back to the main repository's current commits,");
               System.out.println("otherwise the 'pullrequest' command wll not work properly");
               System.out.println();
               System.out.print("Do you want to re-initialize? [Y/n]: ");
               String var9 = "n";

               try {
                  var9 = (new BufferedReader(new InputStreamReader(System.in))).readLine();
               } catch (IOException var7) {
               }

               var9 = var9.toLowerCase();
               if (!var9.startsWith("y")) {
                  System.out.println();
                  System.out.println("Ok nice, the re-init will be cancelled. (thank god)");
                  return true;
               }

               try {
                  FileUtils.deleteDirectory(var1);
                  var8 = false;
               } catch (IOException var6) {
                  System.err.println("ERROR: Could not delete \"" + var1.getAbsolutePath() + "\"!");
                  var6.printStackTrace();
                  return false;
               }
            }

            File var11 = new File(var1, "ModCoderPack");
            if (!var11.isDirectory() && !var11.mkdirs()) {
               System.err.println("ERROR: failed to create \"" + var11.getAbsolutePath() + "\"!");
               return false;
            } else {
               boolean var10 = false;
               if (!var10 && !InitMCP.initTask(locatedMCPZip, var11)) {
                  System.err.println("ERROR: could not initialize MCP from \"" + locatedMCPZip.getAbsolutePath() + "\"!");
                  return false;
               } else {
                  File var5 = new File(var1, "MinecraftSrc");
                  if (!var5.isDirectory() && !var5.mkdirs()) {
                     System.err.println("ERROR: failed to create \"" + var5.getAbsolutePath() + "\"!");
                     return false;
                  } else if (!DecompileMinecraft.decompileMinecraft(var11, locatedMinecraftJar, var5, locatedAssetsJson, true)) {
                     System.err.println("ERROR: could not decompile and patch 1.8.8.jar from \"" + locatedMinecraftJar.getAbsolutePath() + "\"!");
                     return false;
                  } else {
                     return true;
                  }
               }
            }
         }
      }
   }
}
