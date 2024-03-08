package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildToolsConfig;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.diff.ApplyPatchesToZip;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class SetupWorkspace {
   public static boolean setupWorkspace() {
      return setupWorkspace0(false);
   }

   public static boolean pullRequestTest() {
      return setupWorkspace0(true);
   }

   private static boolean setupWorkspace0(boolean var0) {
      File var1 = EaglerBuildToolsConfig.getTemporaryDirectory();
      File var2 = EaglerBuildToolsConfig.getWorkspaceDirectory();

      try {
         return setupWorkspace1(var1, var2, var0);
      } catch (Throwable var4) {
         System.err.println();
         if (var0) {
            System.err.println("Exception encountered while running task 'pullrequest_test'!");
         } else {
            System.err.println("Exception encountered while running task 'workspace'!");
         }

         var4.printStackTrace();
         return false;
      }
   }

   private static boolean setupWorkspace1(File var0, File var1, boolean var2) throws Throwable {
      boolean var3 = var1.exists();
      if (var3 && (!var1.isDirectory() || var1.list().length != 0)) {
         System.err.println();
         System.err.println("WARNING: A workspace already exists in \"" + var1.getAbsolutePath() + "\"!");
         System.err.println();
         System.err.println("Any changes you've made to the code will be lost!");
         System.err.println();
         System.out.print("Do you want to reset the workspace? [Y/n]: ");
         String var4 = "n";

         try {
            var4 = (new BufferedReader(new InputStreamReader(System.in))).readLine();
         } catch (IOException var39) {
         }

         var4 = var4.toLowerCase();
         if (!var4.startsWith("y")) {
            System.out.println();
            System.out.println("Ok nice, the workspace folder will not be reset. (thank god)");
            System.out.println();
            System.out.println("Edit 'buildtools_config.json' to set up a different workspace folder");
            return true;
         }

         try {
            FileUtils.deleteDirectory(var1);
            var3 = false;
         } catch (IOException var38) {
            System.err.println("ERROR: Could not delete \"" + var1.getAbsolutePath() + "\"!");
            var38.printStackTrace();
            return false;
         }
      }

      File var42 = new File(var0, "MinecraftSrc");
      File var5 = new File(var42, "minecraft_res_patch.jar");
      File var6 = new File(var42, "minecraft_src_javadoc.jar");
      System.out.println();
      System.out.println("Setting up dev workspace in \"" + var1.getAbsolutePath() + "\"...");
      System.out.println();
      if (!var1.isDirectory() && !var1.mkdirs()) {
         System.err.println("ERROR: could not create \"" + var1.getAbsolutePath() + "\"!");
         throw new IOException("Could not create \"" + var1.getAbsolutePath() + "\"!");
      } else if (!var6.isFile()) {
         System.err.println("ERROR: could not find 'minecraft_src_javadoc.jar' in your current temporary directory!");
         System.err.println("Run the 'init' command again to generate it");
         return false;
      } else if (!var5.isFile()) {
         System.err.println("ERROR: could not find 'minecraft_res_patch.jar' in your current temporary directory!");
         System.err.println("Run the 'init' command again to generate it");
         return false;
      } else {
         File var7 = new File("./sources");
         File var8 = new File(var7, "setup/workspace_template");
         File var9 = new File(var7, "main/java");
         File var10 = new File(var7, "teavm/java");
         File var11 = new File(var7, "lwjgl/java");
         File var12 = new File(var7, "resources");
         File var13 = new File(var1, "src/main/java");
         File var14 = new File(var1, "src/lwjgl/java");
         File var15 = new File(var1, "src/teavm/java");
         File var16 = new File(var1, "desktopRuntime/resources");
         File var17 = new File(var42, "minecraft_languages.zip");
         File var18 = new File(var1, "javascript/lang");
         System.out.println("Copying files from \"/setup/workspace_template/\" to \"" + var1.getName() + "\"...");

         try {
            FileUtils.copyDirectory(var8, var1);
         } catch (IOException var37) {
            System.err.println("ERROR: could not copy \"/setup/workspace_template/\" to \"" + var1.getAbsolutePath() + "\"!");
            throw var37;
         }

         String var19 = System.getProperty("os.name").toLowerCase();
         File var20;
         if (var19.contains("linux") || var19.contains("macos") || var19.contains("osx")) {
            var20 = new File(var1, "gradlew");
            if (!var20.setExecutable(true)) {
               System.err.println("ERROR: could not set executable bit on 'gradlew'!");
               System.err.println("Enter the root directory of the repository and run 'chmod +x gradlew' if you need access to the gradlew command");
            }
         }

         var20 = new File(var1, ".gitignore");
         if (var20.exists() && !var20.delete() || !(new File(var1, ".gitignore.default")).renameTo(var20)) {
            System.err.println("ERROR: Could not rename \".gitignore.default\" to \".gitignore\" in the workspace directory!");
         }

         if (var10.isDirectory()) {
            System.out.println("Copying files from \"/sources/teavm/java/\" to workspace...");

            try {
               if (!var15.isDirectory() && !var15.mkdirs()) {
                  System.err.println("ERROR: Could not create destination directory!");
                  return false;
               }

               FileUtils.copyDirectory(var10, var15);
            } catch (IOException var36) {
               System.err.println("ERROR: could not copy \"/sources/teavm/java/\" to \"" + var15.getAbsolutePath() + "\"!");
               throw var36;
            }
         }

         System.out.println("Copying files from \"/sources/main/java/\" to workspace...");

         try {
            FileUtils.copyDirectory(var9, var13);
         } catch (IOException var35) {
            System.err.println("ERROR: could not copy \"/sources/main/java/\" to \"" + var13.getAbsolutePath() + "\"!");
            throw var35;
         }

         if (var11.isDirectory()) {
            System.out.println("Copying files from \"/sources/lwjgl/java/\" to workspace...");

            try {
               if (!var14.isDirectory() && !var14.mkdirs()) {
                  System.err.println("ERROR: Could not create destination directory!");
                  return false;
               }

               FileUtils.copyDirectory(var11, var14);
            } catch (IOException var34) {
               System.err.println("ERROR: could not copy \"/sources/lwjgl/java/\" to \"" + var14.getAbsolutePath() + "\"!");
               throw var34;
            }
         }

         System.out.println("Copying files from \"/sources/resources/\" to workspace...");

         try {
            if (!var16.isDirectory() && !var16.mkdirs()) {
               System.err.println("ERROR: Could not create destination directory!");
               return false;
            }

            FileUtils.copyDirectory(var12, var16);
         } catch (IOException var41) {
            System.err.println("ERROR: could not copy \"/sources/resources/\" to \"" + var16.getAbsolutePath() + "\"!");
            throw var41;
         }

         if (var2) {
            System.out.println();
            System.out.println("Applying \"pullrequest\" directory to \"minecraft_src_patch.jar\"...");
            File var21 = new File(var42, "minecraft_src.jar");
            File var22 = new File(var42, "minecraft_src_patch.jar");
            File var23 = new File(var42, "minecraft_res.jar");
            File var24 = new File(var42, "minecraft_res_patch.jar");
            File var25 = new File(var42, "minecraft_src_pullrequest_patch.jar");
            File var26 = new File(var42, "minecraft_src_pullrequest_javadoc.jar");
            File var27 = new File(var42, "minecraft_res_pullrequest_patch.jar");

            try {
               ApplyPatchesToZip.applyPatches(var22, var21, new File("./pullrequest/source"), var25, false, false);
            } catch (Throwable var33) {
               System.err.println();
               System.err.println("ERROR: Could not apply pullrequest directory patches to: " + var22.getName());
               System.err.println(var33.toString());
               var25.delete();
               return false;
            }

            CSVMappings var28 = new CSVMappings();
            if (!InsertJavaDoc.processSource(var25, var26, new File(var0, "ModCoderPack"), var28)) {
               System.err.println();
               System.err.println("ERROR: Could not create pullrequest javadoc!");
               return false;
            }

            var25.delete();

            try {
               ApplyPatchesToZip.applyPatches(var24, var23, new File("./pullrequest/resources"), var27, false, false);
            } catch (Throwable var32) {
               System.err.println();
               System.err.println("ERROR: Could not apply pullrequest directory patches to: " + var24.getName());
               System.err.println(var32.toString());
               var25.delete();
               var27.delete();
               return false;
            }

            var6 = var26;
            var5 = var27;
         } else {
            System.out.println("Extracting files from \"minecraft_src_javadoc.jar\" to \"/src/main/java/\"...");
         }

         try {
            if (!var13.isDirectory() && !var13.mkdirs()) {
               System.err.println("ERROR: Could not create destination directory!");
               return false;
            }

            extractJarTo(var6, var13);
         } catch (IOException var40) {
            System.err.println("ERROR: could not extract \"" + var6.getName() + ".jar\" to \"" + var13.getAbsolutePath() + "\"!");
            throw var40;
         }

         System.out.println("Extracting files from \"minecraft_res_patch.jar\" to \"/desktopRuntime/resources/\"...");

         try {
            extractJarTo(var5, var16);
         } catch (IOException var31) {
            System.err.println("ERROR: could not extract \"" + var5.getName() + "\" to \"" + var16.getAbsolutePath() + "\"!");
            throw var31;
         }

         if (var2) {
            var6.delete();
            var5.delete();
         }

         System.out.println("Extracting files from \"minecraft_languages.zip\" to \"/javascript/lang/\"...");

         try {
            extractJarTo(var17, var18);
         } catch (IOException var30) {
            System.err.println("ERROR: could not extract \"" + var17.getName() + "\" to \"" + var18.getAbsolutePath() + "\"!");
            throw var30;
         }

         System.out.println("Creating eclipse project for desktop runtime...");
         if (!createDesktopRuntimeProject(new File(var7, "setup/eclipseProjectFiles"), var1)) {
            System.err.println("ERROR: could not create eclipse project for desktop runtime!");
            return false;
         } else {
            return true;
         }
      }
   }

   public static int extractJarTo(File var0, File var1) throws IOException {
      int var2 = 0;
      Throwable var3 = null;
      Object var4 = null;

      try {
         ZipInputStream var5 = new ZipInputStream(new FileInputStream(var0));

         ZipEntry var6;
         try {
            while((var6 = var5.getNextEntry()) != null) {
               if (!var6.isDirectory()) {
                  String var7 = var6.getName();
                  if (var7.startsWith("/")) {
                     var7 = var7.substring(1);
                  }

                  if (!var7.startsWith("META-INF")) {
                     File var8 = new File(var1, var7);
                     if (!var8.exists()) {
                        File var9 = var8.getParentFile();
                        if (!var9.isDirectory() && !var9.mkdirs()) {
                           throw new IOException("Could not create directory: " + var9.getAbsolutePath());
                        }

                        Throwable var10 = null;
                        Object var11 = null;

                        try {
                           FileOutputStream var12 = new FileOutputStream(var8);

                           try {
                              IOUtils.copy(var5, var12, 4096);
                              ++var2;
                           } finally {
                              if (var12 != null) {
                                 var12.close();
                              }

                           }
                        } catch (Throwable var30) {
                           if (var10 == null) {
                              var10 = var30;
                           } else if (var10 != var30) {
                              var10.addSuppressed(var30);
                           }

                           throw var10;
                        }
                     }
                  }
               }
            }
         } finally {
            if (var5 != null) {
               var5.close();
            }

         }

         return var2;
      } catch (Throwable var32) {
         if (var3 == null) {
            var3 = var32;
         } else if (var3 != var32) {
            var3.addSuppressed(var32);
         }

         throw var3;
      }
   }

   private static boolean createDesktopRuntimeProject(File var0, File var1) throws Throwable {
      File var2 = new File(var1, "desktopRuntime");
      File var3 = new File(var2, "eclipseProject");
      if (!var3.isDirectory() && !var3.mkdirs()) {
         System.err.println("ERROR: failed to create directory: \"" + var3.getAbsolutePath() + "\"!");
         return false;
      } else {
         File var4 = new File(var3, "bin");
         if (!var4.isDirectory() && !var4.mkdir()) {
            System.err.println("ERROR: failed to create directory: \"" + var4.getAbsolutePath() + "\"!");
            return false;
         } else {
            String var5 = FileUtils.readFileToString(new File(var0, ".classpath"), "UTF-8");
            String var6 = FileUtils.readFileToString(new File(var0, "classpath_entry.txt"), "UTF-8");
            String var7 = FileUtils.readFileToString(new File(var0, ".project"), "UTF-8");
            String var8 = FileUtils.readFileToString(new File(var0, "eaglercraftDebugRuntime.launch"), "UTF-8");
            String var9 = FileUtils.readFileToString(new File(var0, "main_class.txt"), "UTF-8");
            ArrayList var10 = new ArrayList();
            File[] var11 = var2.listFiles();

            File var13;
            for(int var12 = 0; var12 < var11.length; ++var12) {
               var13 = var11[var12];
               if (var13.getName().endsWith(".jar")) {
                  var10.add(var6.replace("${JAR_PATH}", bsToS(var13.getAbsolutePath())));
               }
            }

            var5 = var5.replace("${LIBRARY_CLASSPATH}", String.join(System.lineSeparator(), var10));
            FileUtils.writeStringToFile(new File(var3, ".classpath"), var5, "UTF-8");
            var7 = var7.replace("${LWJGL_SRC_FOLDER}", bsToS((new File(var1, "src/lwjgl/java")).getAbsolutePath()));
            var7 = var7.replace("${MAIN_SRC_FOLDER}", bsToS((new File(var1, "src/main/java")).getAbsolutePath()));
            FileUtils.writeStringToFile(new File(var3, ".project"), var7, "UTF-8");
            var8 = var8.replace("${MAIN_CLASS_FILE}", var9);
            String var15 = var9.substring(var9.indexOf(47) + 1);
            if (var15.endsWith(".java")) {
               var15 = var15.substring(0, var15.length() - 5);
            }

            var15 = var15.replace('/', '.');
            var8 = var8.replace("${MAIN_CLASS_NAME}", var15);
            var8 = var8.replace("${WORKING_DIRECTORY}", bsToS(var2.getAbsolutePath()));
            FileUtils.writeStringToFile(new File(var3, "eaglercraftDebugRuntime.launch"), var8, "UTF-8");
            var13 = new File(var0, "org.eclipse.jdt.core.prefs");
            File var14 = new File(var3, ".settings");
            if (!var14.isDirectory() && !var14.mkdir()) {
               System.err.println("ERROR: failed to create directory: \"" + var14.getAbsolutePath() + "\"!");
               return false;
            } else {
               FileUtils.copyFile(var13, new File(var14, "org.eclipse.jdt.core.prefs"));
               return true;
            }
         }
      }
   }

   private static String bsToS(String var0) {
      return var0.replace('\\', '/');
   }
}
