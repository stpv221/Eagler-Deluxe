package net.lax1dude.eaglercraft.v1_8.buildtools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class EaglerBuildToolsConfig {
   public static File temporary_directory = new File(System.getProperty("user.home"), ".eaglercraft_1.12_buildtools");
   private static boolean temporary_directory_isInit = false;
   private static boolean temporary_directory_mentioned = false;
   public static File workspace_directory = new File("../eaglercraft_1.12_workspace");
   private static boolean workspace_directory_isInit = false;
   private static boolean workspace_directory_mentioned = false;
   private static boolean config_file_loaded = false;
   public static final File configFile = new File("./buildtools_config.json");

   public static void load() {
      if (configFile.exists()) {
         try {
            Throwable var0 = null;
            Object var1 = null;

            try {
               FileInputStream var2 = new FileInputStream(configFile);

               try {
                  byte[] var3 = new byte[(int)configFile.length()];
                  var2.read(var3);
                  var2.close();
                  String var4 = new String(var3, StandardCharsets.UTF_8);
                  JSONObject var5 = new JSONObject(var4);
                  String var6 = var5.optString("temporary_directory", (String)null);
                  if (var6 != null) {
                     temporary_directory = new File(var6);
                     temporary_directory_isInit = true;
                  }

                  var6 = var5.optString("workspace_directory", (String)null);
                  if (var6 != null) {
                     workspace_directory = new File(var6);
                     workspace_directory_isInit = true;
                  }
               } finally {
                  if (var2 != null) {
                     var2.close();
                  }

               }
            } catch (Throwable var14) {
               if (var0 == null) {
                  var0 = var14;
               } else if (var0 != var14) {
                  var0.addSuppressed(var14);
               }

               throw var0;
            }
         } catch (Throwable var15) {
            System.err.println("Failed to read config!");
            var15.printStackTrace();
         }
      }

   }

   public static void save() {
      JSONObject var0 = new JSONObject();
      if (temporary_directory_isInit) {
         var0.put("temporary_directory", temporary_directory.getAbsolutePath());
      }

      if (workspace_directory_isInit) {
         var0.put("workspace_directory", workspace_directory.getAbsoluteFile());
      }

      try {
         Throwable var1 = null;
         Object var2 = null;

         try {
            FileOutputStream var3 = new FileOutputStream(configFile);

            try {
               var3.write(var0.toString(4).getBytes(StandardCharsets.UTF_8));
               var3.close();
            } finally {
               if (var3 != null) {
                  var3.close();
               }

            }
         } catch (Throwable var11) {
            if (var1 == null) {
               var1 = var11;
            } else if (var1 != var11) {
               var1.addSuppressed(var11);
            }

            throw var1;
         }
      } catch (IOException var12) {
         System.err.println("Failed to write config!");
         var12.printStackTrace();
      }

   }

   private static void mentionConfigPath() {
      System.out.println("Edit '" + configFile.getName() + "' to change");
   }

   public static File getTemporaryDirectory() {
      if (!config_file_loaded) {
         load();
         config_file_loaded = true;
      }

      if (!temporary_directory_isInit) {
         File var0 = temporary_directory;
         System.out.println();
         System.out.println("Using temporary directory: " + var0.getAbsolutePath());
         temporary_directory_mentioned = true;
         var0 = askIfChangeIsWanted(var0);
         temporary_directory = var0;

         for(temporary_directory_isInit = true; !temporary_directory.isDirectory() && !temporary_directory.mkdirs(); temporary_directory = askIfChangeIsWanted(var0)) {
            System.err.println("Failed to create: " + var0.getAbsolutePath());
         }

         save();
         System.out.println();
         return temporary_directory;
      } else {
         if (!temporary_directory_mentioned) {
            System.out.println("Using temporary directory: " + temporary_directory.getAbsolutePath());

            for(temporary_directory_mentioned = true; !temporary_directory.isDirectory() && !temporary_directory.mkdirs(); temporary_directory = askIfChangeIsWanted(temporary_directory)) {
               System.err.println("Failed to create: " + temporary_directory.getAbsolutePath());
            }

            mentionConfigPath();
         }

         return temporary_directory;
      }
   }

   public static File getWorkspaceDirectory() {
      if (!config_file_loaded) {
         load();
         config_file_loaded = true;
      }

      if (!workspace_directory_isInit) {
         File var0 = workspace_directory;
         System.out.println();
         System.out.println("Using workspace directory: " + var0.getAbsolutePath());
         workspace_directory_mentioned = true;
         var0 = askIfChangeIsWanted(var0);
         workspace_directory = var0;

         for(workspace_directory_isInit = true; !workspace_directory.isDirectory() && !workspace_directory.mkdirs(); workspace_directory = askIfChangeIsWanted(var0)) {
            System.err.println("Failed to create: " + var0.getAbsolutePath());
         }

         save();
         System.out.println();
         return workspace_directory;
      } else {
         if (!workspace_directory_mentioned) {
            System.out.println("Using workspace directory: " + workspace_directory.getAbsolutePath());

            for(workspace_directory_mentioned = true; !workspace_directory.isDirectory() && !workspace_directory.mkdirs(); workspace_directory = askIfChangeIsWanted(workspace_directory)) {
               System.err.println("Failed to create: " + workspace_directory.getAbsolutePath());
            }

            mentionConfigPath();
         }

         return workspace_directory;
      }
   }

   public static File askIfChangeIsWanted(File var0) {
      System.out.println("Would you like to change this directory?");
      System.out.println("Enter 'Y' for yes or 'N' for no: ");
      String var1 = "N";

      try {
         var1 = (new BufferedReader(new InputStreamReader(System.in))).readLine();
      } catch (IOException var4) {
      }

      if (var1 != null && ((var1 = var1.trim()).equalsIgnoreCase("y") || var1.equalsIgnoreCase("yes"))) {
         System.out.println();
         System.out.println("Type a new filename or hit 'Enter' to browse: ");

         try {
            var1 = (new BufferedReader(new InputStreamReader(System.in))).readLine();
         } catch (IOException var3) {
         }

         if (var1 != null && (var1 = var1.trim()).length() > 0) {
            var0 = new File(var1);
         } else {
            File var2 = FileChooserTool.load(true);
            if (var2 == null) {
               System.out.println("You hit cancel on the file chooser, the directory '" + var0.getAbsolutePath() + "' will be used.");
               var0 = askIfChangeIsWanted(var0);
            } else {
               var0 = var2;
            }
         }
      }

      return var0;
   }
}
