package net.lax1dude.eaglercraft.v1_8.buildtools.gui.headless;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildTools;
import net.lax1dude.eaglercraft.v1_8.buildtools.LicensePrompt;
import net.lax1dude.eaglercraft.v1_8.buildtools.gui.CompileLatestClientGUI;
import net.lax1dude.eaglercraft.v1_8.buildtools.gui.EPKCompiler;
import net.lax1dude.eaglercraft.v1_8.buildtools.gui.JavaC;
import net.lax1dude.eaglercraft.v1_8.buildtools.gui.MakeOfflineDownload;
import net.lax1dude.eaglercraft.v1_8.buildtools.gui.TeaVMBinaries;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.DecompileMinecraft;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.FFMPEG;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.InitMCP;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.teavm.TeaVMBridge;
import net.lax1dude.eaglercraft.v1_8.buildtools.util.FileReaderUTF;
import net.lax1dude.eaglercraft.v1_8.buildtools.util.FileWriterUTF;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CompileLatestClientHeadless {
   public static void main(String[] var0) throws Throwable {
      System.out.println();
      System.out.println("Launching client compiler...");
      System.out.println("Copyright (c) 2022-2023 lax1dude");
      System.out.println();
      boolean var1 = false;
      String var2 = null;
      if (var0.length == 1) {
         var2 = var0[0];
      } else {
         if (var0.length != 2 || !(var1 = var0[0].equalsIgnoreCase("-y"))) {
            System.err.println("Usage: java -jar BuildTools.jar [-y] <config file>");
            System.err.println();
            System.exit(-1);
            return;
         }

         var2 = var0[1];
      }

      System.out.println("Loading config file: " + var2);
      System.out.println();
      File var3 = new File(var2);

      String var4;
      try {
         var4 = FileUtils.readFileToString(var3, StandardCharsets.UTF_8);
      } catch (FileNotFoundException var413) {
         var413.printStackTrace();
         System.err.println();
         System.err.println("ERROR: File '" + var3.getAbsolutePath() + "' does not exist!");
         System.err.println();
         System.exit(-1);
         return;
      }

      JSONObject var5;
      try {
         var5 = new JSONObject(var4);
      } catch (JSONException var412) {
         System.err.println("ERROR: Could not parse '" + var3.getName() + "' as JSON!");
         System.err.println();
         System.err.println(var412.toString());
         System.err.println();
         System.exit(-1);
         return;
      }

      String var12 = "ffmpeg";
      String var13 = null;
      File var14 = null;
      File var15 = null;
      File var16 = null;
      ArrayList var17 = null;
      ArrayList var18 = null;
      ArrayList var19 = null;
      File var21 = null;
      boolean var23 = false;
      boolean var24 = true;

      File var6;
      File var7;
      File var8;
      File var9;
      File var10;
      File var11;
      boolean var20;
      boolean var22;
      String var26;
      try {
         var6 = new File(var5.optString("repositoryFolder", "."));
         var7 = new File(var5.getString("modCoderPack"));
         var8 = new File(var5.getString("minecraftJar"));
         var9 = new File(var5.getString("assetsIndex"));
         var10 = new File(var5.getString("outputDirectory"));
         String var25 = var5.optString("temporaryDirectory");
         var11 = var25 == null ? new File(var10, "build") : new File(var25);
         var12 = var5.optString("ffmpeg", var12);
         if (var12.length() == 0) {
            var12 = "ffmpeg";
         }

         var26 = var5.optString("productionIndex");
         if (var26 != null) {
            var15 = new File(var26);
            String var27 = var5.optString("productionFavicon");
            if (var27 != null) {
               var16 = new File(var27);
            }

            JSONArray var28 = var5.optJSONArray("addScripts");
            int var29;
            int var30;
            if (var28 != null) {
               var29 = var28.length();
               if (var29 > 0) {
                  var17 = new ArrayList(var29);

                  for(var30 = 0; var30 < var29; ++var30) {
                     var17.add(var28.getString(var30));
                  }
               }
            }

            var28 = var5.optJSONArray("removeScripts");
            if (var28 != null) {
               var29 = var28.length();
               if (var29 > 0) {
                  var18 = new ArrayList(var29);

                  for(var30 = 0; var30 < var29; ++var30) {
                     var18.add(var28.getString(var30));
                  }
               }
            }

            var28 = var5.optJSONArray("injectInOffline");
            if (var28 != null) {
               var29 = var28.length();
               if (var29 > 0) {
                  var19 = new ArrayList(var29);

                  for(var30 = 0; var30 < var29; ++var30) {
                     var19.add(var28.getString(var30));
                  }
               }
            }
         }

         var13 = var5.optString("mavenURL");
         var14 = new File(var5.getString("mavenLocal"));
         var20 = var5.optBoolean("generateOfflineDownload", false);
         if (var20) {
            var21 = new File(var5.getString("offlineDownloadTemplate"));
         }

         var22 = var5.optBoolean("keepTemporaryFiles", false);
         var23 = var5.optBoolean("writeSourceMap", false);
         var24 = var5.optBoolean("minifying", true);
      } catch (JSONException var429) {
         System.err.println("CONFIG ERROR: " + var429.toString());
         System.err.println();
         System.exit(-1);
         return;
      }

      System.out.println("Loaded config successfully:");
      System.out.println();
      System.out.println(" - Repository Folder: " + var6.getAbsolutePath().replace('\\', '/'));
      System.out.println(" - Mod Coder Pack: " + var7.getAbsolutePath().replace('\\', '/'));
      System.out.println(" - Minecraft 1.8.8: " + var8.getAbsolutePath().replace('\\', '/'));
      System.out.println(" - Assets Index 1.8: " + var9.getAbsolutePath().replace('\\', '/'));
      System.out.println(" - Temporary Directory: " + var11.getAbsolutePath().replace('\\', '/'));
      System.out.println(" - Output Directory: " + var10.getAbsolutePath().replace('\\', '/'));
      System.out.println(" - FFmpeg Executable: " + var12.replace('\\', '/'));
      System.out.println(" - Maven Repo URL: " + var13);
      System.out.println(" - Maven Local Dir: " + var14.getAbsolutePath().replace('\\', '/'));
      System.out.println(" - Production Index: " + (var15 == null ? "null" : var15.getAbsolutePath().replace('\\', '/')));
      System.out.println(" - Production Favicon: " + (var16 == null ? "null" : var16.getAbsolutePath().replace('\\', '/')));
      System.out.println(" - Generate Offline: " + var20);
      System.out.println(" - Offline Template: " + (var21 == null ? "null" : var21.getAbsolutePath().replace('\\', '/')));
      System.out.println(" - Inject in Offline: " + (var19 == null ? "[ ]" : "[ " + String.join(", ", var19).replace('\\', '/') + " ]"));
      System.out.println(" - Minifying: " + var24);
      System.out.println(" - Write Source Map: " + var23);
      System.out.println(" - Keep Temp Files: " + var22);
      System.out.println(" - Add Scripts: " + (var17 == null ? "[ ]" : "[ " + String.join(", ", var17).replace('\\', '/') + " ]"));
      System.out.println(" - Remove Scripts: " + (var18 == null ? "[ ]" : "[ " + String.join(", ", var18).replace('\\', '/') + " ]"));
      System.out.println();
      if (!var1) {
         System.out.println();
         LicensePrompt.display();
         System.out.println();
      }

      EaglerBuildTools.repositoryRoot = var6;

      try {
         if (!var10.isDirectory() && !var10.mkdirs()) {
            throw new CompileLatestClientGUI.CompileFailureException("Could not create output directory!");
         }

         File[] var430 = var10.listFiles();
         File var433;
         if (var430.length > 0) {
            if (!var1) {
               System.out.print("Output directory has existing files, would you like to delete them? [y/n] ");
               var26 = (new BufferedReader(new InputStreamReader(System.in))).readLine();
               System.out.println();
               if (!var26.equalsIgnoreCase("y") && !var26.equalsIgnoreCase("yes")) {
                  System.out.println("Build cancelled.");
                  System.out.println();
                  System.exit(-1);
                  return;
               }
            }

            System.out.println("Deleting existing files from the output directory...");

            try {
               for(int var431 = 0; var431 < var430.length; ++var431) {
                  var433 = var430[var431];
                  if (var433.isDirectory()) {
                     FileUtils.deleteDirectory(var433);
                  } else if (!var433.delete()) {
                     throw new IOException("Could not delete: " + var433.getAbsolutePath());
                  }
               }
            } catch (IOException var426) {
               throw new CompileLatestClientGUI.CompileFailureException("Could not delete old output directory: " + var426.getMessage());
            }
         }

         File var432 = new File(var11, "ModCoderPack");
         var433 = new File(var11, "MinecraftSrc");
         if (var12.length() == 0) {
            FFMPEG.foundFFMPEG = "ffmpeg";
         } else {
            FFMPEG.foundFFMPEG = var12;
         }

         if (!var432.isDirectory() && !var432.mkdirs()) {
            throw new CompileLatestClientGUI.CompileFailureException("Error: failed to create \"" + var432.getAbsolutePath() + "\"!");
         }

         if (!InitMCP.initTask(var7, var432)) {
            throw new CompileLatestClientGUI.CompileFailureException("Error: could not initialize MCP from \"" + var7.getAbsolutePath() + "\"!");
         }

         if (!var433.isDirectory() && !var433.mkdirs()) {
            throw new CompileLatestClientGUI.CompileFailureException("Error: failed to create \"" + var433.getAbsolutePath() + "\"!");
         }

         if (!DecompileMinecraft.decompileMinecraft(var432, var8, var433, var9, false)) {
            throw new CompileLatestClientGUI.CompileFailureException("Error: could not decompile and patch 1.8.8.jar from \"" + var8.getAbsolutePath() + "\"!");
         }

         try {
            FileUtils.copyFile(new File(var6, "patches/minecraft/output_license.txt"), new File(var11, "MinecraftSrc/LICENSE"));
         } catch (IOException var411) {
            System.err.println("Error: failed to write LICENSE in temporary directory!");
            var411.printStackTrace();
         }

         System.out.println();
         if (var13 == null) {
            System.out.println("TeaVM JARs will be loaded from: " + var14.getAbsolutePath());
            System.out.println();

            try {
               TeaVMBinaries.loadFromDirectory(var14);
            } catch (TeaVMBinaries.MissingJARsException var410) {
               throw new CompileLatestClientGUI.CompileFailureException(var410.getMessage());
            }
         } else {
            System.out.println("TeaVM JARs will be downloaded from repository: " + var13);
            System.out.println();

            try {
               TeaVMBinaries.downloadFromMaven(var13, var14);
            } catch (TeaVMBinaries.MissingJARsException var409) {
               throw new CompileLatestClientGUI.CompileFailureException(var409.getMessage());
            }

            System.out.println();
            System.out.println("Notice: make sure to delete \"" + var14.getAbsolutePath() + "\" when the compiler is finished, it will not be deleted automatically");
            System.out.println();
         }

         File var435 = new File(var11, "classes");

         try {
            int var434;
            try {
               var434 = JavaC.runJavaC(new File(var433, "minecraft_src_javadoc.jar"), var435, var11, TeaVMBinaries.getTeaVMRuntimeClasspath(), new File(var6, "sources/main/java"), new File(var6, "sources/teavm/java"));
            } catch (IOException var408) {
               throw new CompileLatestClientGUI.CompileFailureException("failed to run javac compiler! " + var408.toString(), var408);
            }

            System.out.println();
            if (var434 != 0) {
               throw new CompileLatestClientGUI.CompileFailureException("failed to run javac compiler! exit code " + var434 + ", check log");
            }

            System.out.println("Java compiler completed successfully");
         } finally {
            File var32 = new File(var11, "MinecraftSrc/src_javadoc_tmp");
            if (var32.exists()) {
               System.out.println();
               System.out.println("Deleting temporary directory: " + var32.getAbsolutePath());

               try {
                  FileUtils.deleteDirectory(var32);
               } catch (IOException var404) {
                  System.err.println("Failed to delete temporary directory!");
                  var404.printStackTrace();
               }
            }

         }

         System.out.println();
         System.out.println("Preparing arguments for TeaVM...");
         if (!TeaVMBinaries.tryLoadTeaVMBridge()) {
            System.err.println("Failed to locate TeaVMBridge.jar, you can specify it's path manually by adding the JVM argument \"-Deaglercraft.TeaVMBridge=<path>\"");
            throw new CompileLatestClientGUI.CompileFailureException("Failed to locate TeaVMBridge.jar!");
         }

         HashMap var436 = new HashMap();
         ArrayList var31 = new ArrayList();
         var31.add(var435.getAbsolutePath());
         var31.addAll(Arrays.asList(TeaVMBinaries.getTeaVMRuntimeClasspath()));
         var436.put("classPathEntries", var31);
         var436.put("entryPointName", "main");
         var436.put("mainClass", "net.lax1dude.eaglercraft.v1_8.internal.teavm.MainClass");
         var436.put("minifying", var24);
         var436.put("optimizationLevel", "ADVANCED");
         var436.put("targetDirectory", var10.getAbsolutePath());
         var436.put("generateSourceMaps", var23);
         var436.put("targetFileName", "classes.js");
         System.out.println();

         boolean var437;
         try {
            var437 = TeaVMBridge.compileTeaVM(var436);
         } catch (TeaVMBridge.TeaVMClassLoadException var406) {
            throw new CompileLatestClientGUI.CompileFailureException("Failed to link TeaVM jar files! Did you select the wrong jar?", var406);
         } catch (TeaVMBridge.TeaVMRuntimeException var407) {
            throw new CompileLatestClientGUI.CompileFailureException("Failed to run TeaVM! Check log", var407);
         }

         if (!var437) {
            System.out.println("TeaVM reported problems, check the log");
            System.out.println();
            System.exit(-1);
            return;
         }

         File var33 = new File(var6, "sources/setup/workspace_template/desktopRuntime/CompileEPK.jar");
         if (!var33.exists()) {
            throw new CompileLatestClientGUI.CompileFailureException("EPKCompiler JAR file is missing: " + var33.getAbsolutePath());
         }

         System.out.println();
         System.out.println("Writing index.html...");
         System.out.println();
         String var34 = null;
         if (var16 != null) {
            var34 = var16.getName();
            int var35 = var34.lastIndexOf(46);
            if (var35 != -1) {
               var34 = var34.substring(var35 + 1);
            }
         }

         Throwable var438 = null;
         Object var36 = null;

         String var39;
         String var40;
         String var43;
         int var44;
         int var45;
         try {
            BufferedReader var37 = new BufferedReader(new FileReaderUTF(var15));

            try {
               PrintWriter var38 = new PrintWriter(new FileWriterUTF(new File(var10, "index.html")));

               try {
                  label8785:
                  while(true) {
                     while(true) {
                        while(true) {
                           if ((var39 = var37.readLine()) == null) {
                              break label8785;
                           }

                           var40 = var39.trim();
                           if (var40.startsWith("<link") && var40.contains("rel=\"shortcut icon\"")) {
                              if (var34 != null) {
                                 String var446;
                                 label8782: {
                                    label8781: {
                                       var446 = "image/png";
                                       switch(var34.hashCode()) {
                                       case 97669:
                                          if (var34.equals("bmp")) {
                                             var446 = "image/bmp";
                                             break label8782;
                                          }
                                          break;
                                       case 102340:
                                          if (var34.equals("gif")) {
                                             var446 = "image/gif";
                                             break label8782;
                                          }
                                          break;
                                       case 104085:
                                          if (var34.equals("ico")) {
                                             var446 = "image/x-icon";
                                             break label8782;
                                          }
                                          break;
                                       case 105441:
                                          if (var34.equals("jpg")) {
                                             break label8781;
                                          }
                                          break;
                                       case 111145:
                                          if (var34.equals("png")) {
                                             break label8782;
                                          }
                                          break;
                                       case 3268712:
                                          if (var34.equals("jpeg")) {
                                             break label8781;
                                          }
                                          break;
                                       case 3645340:
                                          if (var34.equals("webp")) {
                                             var446 = "image/webp";
                                             break label8782;
                                          }
                                       }

                                       System.err.println();
                                       System.err.println("WARNING: favicon extension '" + var34 + "' is unknown, defaulting to image/png MIME type");
                                       System.err.println();
                                       break label8782;
                                    }

                                    var446 = "image/jpeg";
                                 }

                                 var38.println(var39.replace("favicon.png", "favicon." + var34).replace("image/png", var446));
                                 System.out.println("Setting favicon <link> href to \"favicon." + var34 + "\", MIME type \"" + var446 + "\" in index.html");
                              } else {
                                 System.out.println("Removed favicon <link> from index.html, no favicon configured");
                              }
                           } else if (var40.startsWith("<meta") && var40.contains("property=\"og:image\"")) {
                              if (var34 != null) {
                                 var38.println(var39.replace("favicon.png", "favicon." + var34));
                                 System.out.println("Setting og:image <link> href to \"favicon." + var34 + "\"");
                              } else {
                                 System.out.println("Removed og:image <meta> tag in index.html, no favicon configured");
                              }
                           } else {
                              if (var40.startsWith("<script")) {
                                 int var41 = var39.indexOf("src=\"");
                                 int var42 = var39.indexOf(34, var41 + 5);
                                 var43 = var39.substring(var41 + 5, var42);
                                 if (var17 != null && var43.equals("classes.js")) {
                                    var44 = 0;

                                    for(var45 = var17.size(); var44 < var45; ++var44) {
                                       String var46 = (String)var17.get(var44);
                                       var38.println(var39.replace("classes.js", var46));
                                       System.out.println("Added <script> tag with src \"" + var46 + "\" to index.html");
                                    }
                                 }

                                 if (var18 != null && var18.contains(var43)) {
                                    System.out.println("Removed <script> tag with src \"" + var43 + "\" from index.html");
                                    continue;
                                 }
                              }

                              var38.println(var39);
                           }
                        }
                     }
                  }
               } finally {
                  if (var38 != null) {
                     var38.close();
                  }

               }
            } catch (Throwable var424) {
               if (var438 == null) {
                  var438 = var424;
               } else if (var438 != var424) {
                  var438.addSuppressed(var424);
               }

               if (var37 != null) {
                  var37.close();
               }

               throw var438;
            }

            if (var37 != null) {
               var37.close();
            }
         } catch (Throwable var425) {
            if (var438 == null) {
               var438 = var425;
            } else if (var438 != var425) {
               var438.addSuppressed(var425);
            }

            throw var438;
         }

         System.out.println();
         if (var16 != null) {
            FileUtils.copyFile(var16, new File(var10, "favicon." + var34));
         }

         System.out.println();
         System.out.println("Running EPKCompiler on assets...");
         EPKCompiler.compilerMain(var33, new String[]{(new File(var433, "minecraft_res_patch.jar")).getAbsolutePath() + System.getProperty("path.separator") + (new File(var6, "sources/resources")).getAbsolutePath(), (new File(var10, "assets.epk")).getAbsolutePath()});
         System.out.println();
         System.out.println("Running EPKCompiler on languages.zip...");
         EPKCompiler.compilerMain(var33, new String[]{(new File(var433, "minecraft_languages.zip")).getAbsolutePath(), (new File(var11, "languages.epk")).getAbsolutePath()});
         System.out.println();
         System.out.println("Creating languages directory...");
         File var440 = new File(var10, "lang");
         byte[] var439 = new byte[16384];
         Throwable var442 = null;
         var39 = null;

         try {
            ZipInputStream var445 = new ZipInputStream(new FileInputStream(new File(var433, "minecraft_languages.zip")));

            ZipEntry var451;
            try {
               while((var451 = var445.getNextEntry()) != null) {
                  if (!var451.isDirectory()) {
                     File var448 = new File(var440, var451.getName());
                     File var450 = var448.getParentFile();
                     if (!var450.exists() && !var450.mkdirs()) {
                        throw new IOException("Could not create directory: " + var450.getAbsolutePath());
                     }

                     Throwable var453 = null;
                     Object var454 = null;

                     try {
                        FileOutputStream var455 = new FileOutputStream(var448);

                        int var441;
                        try {
                           while((var441 = var445.read(var439)) != -1) {
                              var455.write(var439, 0, var441);
                           }
                        } finally {
                           if (var455 != null) {
                              var455.close();
                           }

                        }
                     } catch (Throwable var420) {
                        if (var453 == null) {
                           var453 = var420;
                        } else if (var453 != var420) {
                           var453.addSuppressed(var420);
                        }

                        throw var453;
                     }
                  }
               }
            } finally {
               if (var445 != null) {
                  var445.close();
               }

            }
         } catch (Throwable var422) {
            if (var442 == null) {
               var442 = var422;
            } else if (var442 != var422) {
               var442.addSuppressed(var422);
            }

            throw var442;
         }

         System.out.println();
         if (var20) {
            System.out.println("Running offline download generator...");
            System.out.println();
            File var443 = var21;
            if (var19 != null) {
               var443 = new File(var11, "offline_download_template.txt");
               Throwable var444 = null;
               var40 = null;

               try {
                  BufferedReader var452 = new BufferedReader(new FileReaderUTF(var21));

                  try {
                     PrintWriter var449 = new PrintWriter(new FileWriterUTF(var443));

                     try {
                        for(; (var43 = var452.readLine()) != null; var449.println(var43)) {
                           if (var43.contains("${classes_js}")) {
                              var44 = 0;

                              for(var45 = var19.size(); var44 < var45; ++var44) {
                                 File var456 = new File((String)var19.get(var44));
                                 String var47 = var456.getAbsolutePath();
                                 String var48 = var456.getName();
                                 System.out.println("Adding file to offline download template: " + var47);
                                 var449.println("// %%%%%%%%% " + var48 + " %%%%%%%%%");
                                 var449.println();
                                 Throwable var49 = null;
                                 Object var50 = null;

                                 try {
                                    BufferedReader var51 = new BufferedReader(new FileReaderUTF(var456));

                                    String var52;
                                    try {
                                       while((var52 = var51.readLine()) != null) {
                                          var449.println(var52);
                                       }
                                    } finally {
                                       if (var51 != null) {
                                          var51.close();
                                       }

                                    }
                                 } catch (Throwable var415) {
                                    if (var49 == null) {
                                       var49 = var415;
                                    } else if (var49 != var415) {
                                       var49.addSuppressed(var415);
                                    }

                                    throw var49;
                                 }

                                 var449.println();
                                 char[] var457 = new char[20 + var48.length()];

                                 for(int var458 = 0; var458 < var457.length; ++var458) {
                                    var457[var458] = '%';
                                 }

                                 var449.print("// ");
                                 var449.println(var457);
                                 var449.println();
                                 var449.println();
                              }

                              System.out.println();
                           }
                        }
                     } finally {
                        if (var449 != null) {
                           var449.close();
                        }

                     }
                  } catch (Throwable var417) {
                     if (var444 == null) {
                        var444 = var417;
                     } else if (var444 != var417) {
                        var444.addSuppressed(var417);
                     }

                     if (var452 != null) {
                        var452.close();
                     }

                     throw var444;
                  }

                  if (var452 != null) {
                     var452.close();
                  }
               } catch (Throwable var418) {
                  if (var444 == null) {
                     var444 = var418;
                  } else if (var444 != var418) {
                     var444.addSuppressed(var418);
                  }

                  throw var444;
               }
            }

            File var447 = new File(var6, "sources/setup/workspace_template/desktopRuntime/MakeOfflineDownload.jar");
            MakeOfflineDownload.compilerMain(var447, new String[]{var443.getAbsolutePath(), (new File(var10, "classes.js")).getAbsolutePath(), (new File(var10, "assets.epk")).getAbsolutePath(), (new File(var10, "EaglercraftX_1.8_Offline_en_US.html")).getAbsolutePath(), (new File(var10, "EaglercraftX_1.8_Offline_International.html")).getAbsolutePath(), (new File(var11, "languages.epk")).getAbsolutePath()});
         }

         System.out.println("Releasing external ClassLoader(s)...");
         System.out.println();
         TeaVMBridge.free();
         EPKCompiler.free();
         if (var20) {
            MakeOfflineDownload.free();
         }

         if (!var22) {
            System.out.println("Cleaning up temporary files...");

            try {
               FileUtils.deleteDirectory(var11);
            } catch (IOException var405) {
               System.err.println("Failed to delete temporary directory: " + var11.getAbsolutePath());
               var405.printStackTrace();
            }
         }

         System.out.println();
         System.out.println("Client build successful! Check the output directory for your files");
      } catch (CompileLatestClientGUI.CompileFailureException var428) {
         System.out.println();
         System.err.println("COMPILATION FAILED: " + var428.getMessage());
         System.out.println();
         System.exit(-1);
      }

   }
}
