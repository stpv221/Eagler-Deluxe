package net.lax1dude.eaglercraft.v1_8.buildtools.gui;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildTools;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.DecompileMinecraft;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.FFMPEG;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.InitMCP;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.teavm.TeaVMBridge;
import org.apache.commons.io.FileUtils;

public class CompileLatestClientGUI {
   public static CompileLatestClientFrame frame = null;

   public static void main(String[] var0) {
      System.out.println();
      System.out.println("Launching client compiler wizard...");
      System.out.println("Copyright (c) 2022-2024 lax1dude");
      System.out.println();
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | ClassNotFoundException var4) {
               System.err.println("Could not set system look and feel: " + var4.toString());
            }

            if (!System.getProperty("eaglercraft.isJava11", "false").equalsIgnoreCase("true")) {
               try {
                  if (!(Boolean)Class.forName("net.lax1dude.eaglercraft.v1_8.buildtools.Java11Check", true, new URLClassLoader(new URL[]{(new File("buildtools/Java11Check.jar")).toURI().toURL()})).getMethod("classLoadCheck").invoke((Object)null)) {
                     throw new RuntimeException("wtf?");
                  }
               } catch (Throwable var3) {
                  JOptionPane.showMessageDialog((Component)null, "Error: Java 11 is required to run this program", "Unsupported JRE", 0);
                  System.exit(-1);
                  return;
               }
            }

            label62: {
               CompileLatestClientGUI.frame = new CompileLatestClientFrame();
               CompileLatestClientGUI.frame.frmCompileLatestClient.setLocationRelativeTo((Component)null);
               CompileLatestClientGUI.frame.frmCompileLatestClient.setVisible(true);
               System.out.println("you eagler");
               System.out.println();
               CompileLatestClientGUI.frame.launchLogUpdateThread();
               System.setOut(new PrintStream(new ConsoleRedirector(false)));
               System.setErr(new PrintStream(new ConsoleRedirector(true)));
               if (JavaC.jdkHome == null) {
                  if (JOptionPane.showConfirmDialog(CompileLatestClientGUI.frame.frmCompileLatestClient, "Error: A JDK is required to run this program!\nYou are currently running on a JRE\nDo you have a JDK installed that you would like to use instead?", "Unsupported JRE", 0) != 0) {
                     JOptionPane.showMessageDialog(CompileLatestClientGUI.frame.frmCompileLatestClient, "Please install a JDK and re-launch this program", "Unsupported JRE", 0);
                     System.exit(-1);
                  } else {
                     JOptionPane.showMessageDialog(CompileLatestClientGUI.frame.frmCompileLatestClient, "You need at least JDK 8 to compile EaglercraftX 1.8!\nSelect the path to the installation you want to use", "Unsupported JRE", 1);
                     JFileChooser var1 = new JFileChooser((new File(System.getProperty("java.home"))).getParentFile());
                     var1.setMultiSelectionEnabled(false);
                     var1.setFileHidingEnabled(false);
                     var1.setFileSelectionMode(1);

                     while(true) {
                        if (var1.showOpenDialog(CompileLatestClientGUI.frame.frmCompileLatestClient) != 0) {
                           break label62;
                        }

                        label49: {
                           File var2 = var1.getSelectedFile();
                           if (JavaC.windows) {
                              if ((new File(var2, "bin/javac.exe")).exists()) {
                                 break label49;
                              }
                           } else if ((new File(var2, "bin/javac")).canExecute()) {
                              break label49;
                           }

                           if (JOptionPane.showConfirmDialog(CompileLatestClientGUI.frame.frmCompileLatestClient, "Could not find a java compiler in this directory!\nWould you like to try again?", "Unsupported JRE", 0) != 0) {
                              break label62;
                           }
                           continue;
                        }

                        JavaC.jdkHome = var1.getSelectedFile();
                        JOptionPane.showMessageDialog(CompileLatestClientGUI.frame.frmCompileLatestClient, "The JDK \"" + JavaC.jdkHome.getAbsolutePath() + "\" will be used to compile EaglercraftX", "Unsupported JRE", 1);
                        break;
                     }
                  }
               }

               EventQueue.invokeLater(new Runnable() {
                  public void run() {
                     CompileLatestClientGUI.frame.scrollPane_LicenseText.getVerticalScrollBar().setValue(0);
                  }
               });
               return;
            }

            JOptionPane.showMessageDialog(CompileLatestClientGUI.frame.frmCompileLatestClient, "Please install JDK 8 or newer to continue", "Unsupported JRE", 0);
            System.exit(-1);
         }
      });
   }

   public static void runCompiler() {
      try {
         runCompiler0();
      } catch (CompileLatestClientGUI.CompileFailureException var1) {
         System.out.println();
         System.err.println("Error: " + var1.getMessage());
         var1.printStackTrace();
         frame.finishCompiling(true, var1.getMessage());
         return;
      } catch (Throwable var2) {
         System.out.println();
         System.err.println("Error: unhandled exception caught while compiling!");
         var2.printStackTrace();
         frame.finishCompiling(true, var2.toString());
         return;
      }

      if (!frame.finished) {
         System.out.println();
         System.err.println("Error: compilation finished with unknown status!");
         frame.finishCompiling(true, "Compilation finished with unknown status");
      }

   }

   private static void runCompiler0() throws Throwable {
      File var0 = new File(frame.textField_RepositoryPath.getText().trim());
      EaglerBuildTools.repositoryRoot = var0;
      File var1 = new File(frame.textField_ModCoderPack.getText().trim());
      File var2 = new File(frame.textField_JarFilePath.getText().trim());
      File var3 = new File(frame.textField_AssetsIndexJSON.getText().trim());
      File var4 = new File(frame.textField_OutputDirectory.getText().trim());
      File var5 = new File(var4, "build");
      File[] var6 = var4.listFiles();
      File var8;
      if (var6.length > 0) {
         System.out.println("Deleting existing files from the output directory...");

         try {
            for(int var7 = 0; var7 < var6.length; ++var7) {
               var8 = var6[var7];
               if (var8.isDirectory()) {
                  FileUtils.deleteDirectory(var8);
               } else if (!var8.delete()) {
                  throw new IOException("Could not delete: " + var8.getAbsolutePath());
               }
            }
         } catch (IOException var119) {
            throw new CompileLatestClientGUI.CompileFailureException("Could not delete old output directory: " + var119.getMessage());
         }
      }

      File var120 = new File(var5, "ModCoderPack");
      var8 = new File(var5, "MinecraftSrc");
      String var9 = frame.chckbxUsePathFFmpeg.isSelected() ? "" : frame.textField_pathToFFmpeg.getText().trim();
      if (var9.length() == 0) {
         FFMPEG.foundFFMPEG = "ffmpeg";
      } else {
         FFMPEG.foundFFMPEG = var9;
      }

      String var10 = frame.getRepositoryURL();
      if (var10 == null) {
         new File(frame.textField_MavenRepoLocal.getText().trim());
      }

      boolean var11 = frame.chckbxOutputOfflineDownload.isSelected();
      boolean var12 = frame.chckbxKeepTemporaryFiles.isSelected();
      if (!var120.isDirectory() && !var120.mkdirs()) {
         throw new CompileLatestClientGUI.CompileFailureException("Error: failed to create \"" + var120.getAbsolutePath() + "\"!");
      } else if (!InitMCP.initTask(var1, var120)) {
         throw new CompileLatestClientGUI.CompileFailureException("Error: could not initialize MCP from \"" + var1.getAbsolutePath() + "\"!");
      } else if (!var8.isDirectory() && !var8.mkdirs()) {
         throw new CompileLatestClientGUI.CompileFailureException("Error: failed to create \"" + var8.getAbsolutePath() + "\"!");
      } else if (!DecompileMinecraft.decompileMinecraft(var120, var2, var8, var3, false)) {
         throw new CompileLatestClientGUI.CompileFailureException("Error: could not decompile and patch 1.8.8.jar from \"" + var2.getAbsolutePath() + "\"!");
      } else {
         try {
            FileUtils.copyFile(new File(var0, "patches/minecraft/output_license.txt"), new File(var5, "MinecraftSrc/LICENSE"));
         } catch (IOException var113) {
            System.err.println("Error: failed to write LICENSE in temporary directory!");
            var113.printStackTrace();
         }

         System.out.println();
         if (frame.rdbtnMavenRepoLocal.isSelected()) {
            System.out.println("TeaVM JARs will be loaded from: " + frame.textField_MavenRepoLocal.getText());
         } else {
            String var13 = frame.getRepositoryURL();
            System.out.println("TeaVM JARs will be downloaded from repository: " + var13);
            System.out.println();

            try {
               TeaVMBinaries.downloadFromMaven(var13, new File("##TEAVM.TMP##"));
            } catch (TeaVMBinaries.MissingJARsException var112) {
               throw new CompileLatestClientGUI.CompileFailureException(var112.getMessage());
            }
         }

         System.out.println();
         File var14 = new File(var5, "classes");

         try {
            int var121;
            try {
               var121 = JavaC.runJavaC(new File(var8, "minecraft_src_javadoc.jar"), var14, var5, TeaVMBinaries.getTeaVMRuntimeClasspath(), new File(var0, "sources/main/java"), new File(var0, "sources/teavm/java"));
            } catch (IOException var111) {
               throw new CompileLatestClientGUI.CompileFailureException("failed to run javac compiler! " + var111.toString(), var111);
            }

            System.out.println();
            if (var121 != 0) {
               throw new CompileLatestClientGUI.CompileFailureException("failed to run javac compiler! exit code " + var121 + ", check log");
            }

            System.out.println("Java compiler completed successfully");
         } finally {
            File var17 = new File(var5, "MinecraftSrc/src_javadoc_tmp");
            if (var17.exists()) {
               System.out.println();
               System.out.println("Deleting temporary directory: " + var17.getAbsolutePath());

               try {
                  FileUtils.deleteDirectory(var17);
               } catch (IOException var106) {
                  System.err.println("Failed to delete temporary directory!");
                  var106.printStackTrace();
               }
            }

         }

         System.out.println();
         System.out.println("Preparing arguments for TeaVM...");
         if (!TeaVMBinaries.tryLoadTeaVMBridge()) {
            System.err.println("Failed to locate TeaVMBridge.jar, you can specify it's path manually by adding the JVM argument \"-Deaglercraft.TeaVMBridge=<path>\"");
            throw new CompileLatestClientGUI.CompileFailureException("Failed to locate TeaVMBridge.jar!");
         } else {
            HashMap var15 = new HashMap();
            ArrayList var16 = new ArrayList();
            var16.add(var14.getAbsolutePath());
            var16.addAll(Arrays.asList(TeaVMBinaries.getTeaVMRuntimeClasspath()));
            var15.put("classPathEntries", var16);
            var15.put("entryPointName", "main");
            var15.put("mainClass", "net.lax1dude.eaglercraft.v1_8.internal.teavm.MainClass");
            var15.put("minifying", true);
            var15.put("optimizationLevel", "ADVANCED");
            var15.put("targetDirectory", var4.getAbsolutePath());
            var15.put("generateSourceMaps", true);
            var15.put("targetFileName", "classes.js");
            System.out.println();

            boolean var122;
            try {
               var122 = TeaVMBridge.compileTeaVM(var15);
            } catch (TeaVMBridge.TeaVMClassLoadException var109) {
               throw new CompileLatestClientGUI.CompileFailureException("Failed to link TeaVM jar files! Did you select the wrong jar?", var109);
            } catch (TeaVMBridge.TeaVMRuntimeException var110) {
               throw new CompileLatestClientGUI.CompileFailureException("Failed to run TeaVM! Check log", var110);
            }

            if (!var122) {
               frame.finishCompiling(true, "TeaVM reported problems, check the log");
            } else {
               File var18 = new File(var0, "sources/setup/workspace_template/desktopRuntime/CompileEPK.jar");
               if (!var18.exists()) {
                  throw new CompileLatestClientGUI.CompileFailureException("EPKCompiler JAR file is missing: " + var18.getAbsolutePath());
               } else {
                  System.out.println();
                  System.out.println("Writing default index.html...");
                  FileUtils.copyFile(new File(var0, "buildtools/production-index.html"), new File(var4, "index.html"));
                  FileUtils.copyFile(new File(var0, "buildtools/production-favicon.png"), new File(var4, "favicon.png"));
                  System.out.println();
                  System.out.println("Running EPKCompiler on assets...");
                  EPKCompiler.compilerMain(var18, new String[]{(new File(var8, "minecraft_res_patch.jar")).getAbsolutePath() + System.getProperty("path.separator") + (new File(var0, "sources/resources")).getAbsolutePath(), (new File(var4, "assets.epk")).getAbsolutePath()});
                  System.out.println();
                  System.out.println("Running EPKCompiler on languages.zip...");
                  EPKCompiler.compilerMain(var18, new String[]{(new File(var8, "minecraft_languages.zip")).getAbsolutePath(), (new File(var5, "languages.epk")).getAbsolutePath()});
                  System.out.println();
                  System.out.println("Creating languages directory...");
                  File var19 = new File(var4, "lang");
                  byte[] var20 = new byte[16384];
                  Throwable var22 = null;
                  Object var23 = null;

                  try {
                     ZipInputStream var24 = new ZipInputStream(new FileInputStream(new File(var8, "minecraft_languages.zip")));

                     ZipEntry var25;
                     try {
                        while((var25 = var24.getNextEntry()) != null) {
                           if (!var25.isDirectory()) {
                              File var26 = new File(var19, var25.getName());
                              File var27 = var26.getParentFile();
                              if (!var27.exists() && !var27.mkdirs()) {
                                 throw new IOException("Could not create directory: " + var27.getAbsolutePath());
                              }

                              Throwable var28 = null;
                              Object var29 = null;

                              try {
                                 FileOutputStream var30 = new FileOutputStream(var26);

                                 int var21;
                                 try {
                                    while((var21 = var24.read(var20)) != -1) {
                                       var30.write(var20, 0, var21);
                                    }
                                 } finally {
                                    if (var30 != null) {
                                       var30.close();
                                    }

                                 }
                              } catch (Throwable var115) {
                                 if (var28 == null) {
                                    var28 = var115;
                                 } else if (var28 != var115) {
                                    var28.addSuppressed(var115);
                                 }

                                 throw var28;
                              }
                           }
                        }
                     } finally {
                        if (var24 != null) {
                           var24.close();
                        }

                     }
                  } catch (Throwable var117) {
                     if (var22 == null) {
                        var22 = var117;
                     } else if (var22 != var117) {
                        var22.addSuppressed(var117);
                     }

                     throw var22;
                  }

                  System.out.println();
                  if (var11) {
                     System.out.println("Running offline download generator...");
                     System.out.println();
                     File var123 = new File(var0, "sources/setup/workspace_template/desktopRuntime/MakeOfflineDownload.jar");
                     MakeOfflineDownload.compilerMain(var123, new String[]{(new File(var0, "sources/setup/workspace_template/javascript/OfflineDownloadTemplate.txt")).getAbsolutePath(), (new File(var4, "classes.js")).getAbsolutePath(), (new File(var4, "assets.epk")).getAbsolutePath(), (new File(var4, "EaglercraftX_1.8_Offline_en_US.html")).getAbsolutePath(), (new File(var4, "EaglercraftX_1.8_Offline_International.html")).getAbsolutePath(), (new File(var4, "build/languages.epk")).getAbsolutePath()});
                  }

                  System.out.println("Releasing external ClassLoader(s)...");
                  System.out.println();
                  TeaVMBridge.free();
                  EPKCompiler.free();
                  if (var11) {
                     MakeOfflineDownload.free();
                  }

                  if (!var12) {
                     System.out.println("Cleaning up temporary files...");

                     try {
                        FileUtils.deleteDirectory(var5);
                     } catch (IOException var108) {
                        System.err.println("Failed to delete temporary directory: " + var5.getAbsolutePath());
                        var108.printStackTrace();
                     }
                  }

                  System.out.println();
                  System.out.println("Client build successful! Check the output directory for your files");

                  try {
                     Desktop.getDesktop().open(var4);
                  } catch (Throwable var107) {
                  }

                  frame.finishCompiling(false, "");
               }
            }
         }
      }
   }

   public static class CompileFailureException extends RuntimeException {
      public CompileFailureException(String var1) {
         super(var1);
      }

      public CompileFailureException(String var1, Throwable var2) {
         super(var1, var2);
      }
   }
}
