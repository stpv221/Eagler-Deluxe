package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildToolsConfig;
import org.apache.commons.io.FileUtils;

public class TaskClean {
   public static boolean taskClean() {
      try {
         return taskClean0();
      } catch (Throwable var1) {
         System.err.println();
         System.err.println("Exception encountered while running task 'clean'!");
         var1.printStackTrace();
         return false;
      }
   }

   private static boolean taskClean0() throws Throwable {
      File var0 = EaglerBuildToolsConfig.getTemporaryDirectory();
      File var1 = new File("pullrequest");
      boolean var2 = var0.exists();
      boolean var3 = var1.exists();
      if (var2 && (!var0.isDirectory() || var0.list().length != 0) || var3 && (!var1.isDirectory() || var1.list().length != 0)) {
         System.out.println();
         System.out.println("Notice: Clean will delete the init directory and also");
         System.out.println("all of the files in the current pull request");
         System.out.println();
         System.out.println("you must revert all changes in the 'patches' directory of");
         System.out.println("this repo back to the main repository's current commits,");
         System.out.println("otherwise the 'pullrequest' command wll not work properly");
         System.out.println();
         System.out.print("Do you want to clean? [Y/n]: ");
         String var4 = "n";

         try {
            var4 = (new BufferedReader(new InputStreamReader(System.in))).readLine();
         } catch (IOException var7) {
         }

         var4 = var4.toLowerCase();
         if (!var4.startsWith("y")) {
            System.out.println();
            System.out.println("Ok nice, the clean will be cancelled. (thank god)");
            return true;
         }

         try {
            if (var3) {
               System.out.println();
               System.out.println("Deleting pull request...");
               FileUtils.deleteDirectory(var1);
               var3 = false;
            }
         } catch (IOException var8) {
            System.err.println("ERROR: Could not delete \"" + var1.getAbsolutePath() + "\"!");
            var8.printStackTrace();
            return false;
         }

         try {
            if (var2) {
               System.out.println();
               System.out.println("Deleting init directory...");
               FileUtils.deleteDirectory(var0);
               var2 = false;
            }
         } catch (IOException var6) {
            System.err.println("ERROR: Could not delete \"" + var0.getAbsolutePath() + "\"!");
            var6.printStackTrace();
            return false;
         }
      }

      return true;
   }
}
