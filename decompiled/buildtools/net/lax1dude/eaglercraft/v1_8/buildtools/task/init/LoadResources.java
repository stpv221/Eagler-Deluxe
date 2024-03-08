package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildTools;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class LoadResources {
   public static boolean loadResources(File var0, File var1, File var2, File var3, File var4) {
      System.out.println("Copying resources from '" + var0.getName() + "' into '" + var2.getName() + "'");

      try {
         Throwable var5 = null;
         Object var6 = null;

         try {
            ZipOutputStream var7 = new ZipOutputStream(new FileOutputStream(var2));

            try {
               var7.setLevel(5);
               var7.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
               var7.write("Manifest-Version: 1.0\nCreated-By: Eaglercraft BuildTools\n".getBytes(StandardCharsets.UTF_8));
               Throwable var8 = null;
               Throwable var9 = null;

               ZipInputStream var10;
               try {
                  var10 = new ZipInputStream(new FileInputStream(var0));

                  ZipEntry var11;
                  try {
                     while((var11 = var10.getNextEntry()) != null) {
                        if (!var11.isDirectory()) {
                           String var12 = var11.getName();
                           if (var12.startsWith("/")) {
                              var12 = var12.substring(1);
                           }

                           if (!var12.startsWith("META-INF") && !var12.endsWith(".class")) {
                              var7.putNextEntry(var11);
                              IOUtils.copy(var10, var7, 4096);
                           }
                        }
                     }
                  } finally {
                     if (var10 != null) {
                        var10.close();
                     }

                  }
               } catch (Throwable var140) {
                  if (var8 == null) {
                     var8 = var140;
                  } else if (var8 != var140) {
                     var8.addSuppressed(var140);
                  }

                  throw var8;
               }

               System.out.println();
               System.out.println("Reading 'assetsIndexTransformer.json'...");

               ResourceRulesList var153;
               try {
                  var153 = ResourceRulesList.loadResourceRules(new File(EaglerBuildTools.repositoryRoot, "mcp918/assetsIndexTransformer.json"));
               } catch (IOException var146) {
                  System.err.println();
                  System.err.println("ERROR: failed to read 'mcp918/assetsIndexTransformer.json'!");
                  var146.printStackTrace();
                  return false;
               }

               System.out.println();
               System.out.println("Reading asset index '" + var1.getAbsolutePath() + "'...");
               var9 = null;
               var10 = null;

               try {
                  ZipOutputStream var154 = new ZipOutputStream(new FileOutputStream(var4));

                  try {
                     var154.setLevel(5);

                     try {
                        JSONObject var155 = (new JSONObject(FileUtils.readFileToString(var1, StandardCharsets.UTF_8))).getJSONObject("objects");
                        Iterator var13 = var155.keys();
                        System.out.println("Downloading assets from 'https://resources.download.minecraft.net/'...");

                        while(var13.hasNext()) {
                           String var14 = (String)var13.next();
                           JSONObject var15 = var155.getJSONObject(var14);
                           ResourceRulesList.ResourceRule var16 = var153.get(var14);
                           if (var16.action == ResourceRulesList.Action.EXCLUDE) {
                              System.out.println("Skipping file '" + var14 + "'");
                           } else {
                              String var17 = var15.getString("hash");
                              int var18 = var15.getInt("size");
                              System.out.println("Downloading '" + var14 + "' (" + formatByteLength(var18) + ") ...");

                              URL var19;
                              try {
                                 var19 = new URL("https://resources.download.minecraft.net/" + var17.substring(0, 2) + "/" + var17);
                              } catch (MalformedURLException var142) {
                                 System.err.println("Resource file '" + var14 + "' had an invalid URL!");
                                 var142.printStackTrace();
                                 continue;
                              }

                              byte[] var20 = new byte[var18];

                              try {
                                 Throwable var21 = null;
                                 Object var22 = null;

                                 try {
                                    InputStream var23 = var19.openStream();

                                    try {
                                       int var24 = 0;
                                       int var25 = 0;

                                       while(true) {
                                          if (var24 == var18 || (var25 = var23.read(var20, var24, var18 - var24)) <= 0) {
                                             int var26 = var23.available();
                                             if (var24 == var18 && var26 <= 0) {
                                                break;
                                             }

                                             throw new IOException("File '" + var19.toString() + "' was the wrong length! " + (var26 > 0 ? var26 + " bytes remaining" : var18 - var24 + " bytes missing"));
                                          }

                                          var24 += var25;
                                       }
                                    } finally {
                                       if (var23 != null) {
                                          var23.close();
                                       }

                                    }
                                 } catch (Throwable var144) {
                                    if (var21 == null) {
                                       var21 = var144;
                                    } else if (var21 != var144) {
                                       var21.addSuppressed(var144);
                                    }

                                    throw var21;
                                 }
                              } catch (IOException var145) {
                                 System.err.println("Resource file '" + var19.toString() + "' could not be downloaded!");
                                 var145.printStackTrace();
                                 continue;
                              }

                              if (var16.action == ResourceRulesList.Action.ENCODE) {
                                 try {
                                    System.out.println(" - encoding ogg: " + var16.ffmpegSamples / 1000 + "kHz, " + var16.ffmpegBitrate + "kbps, " + (var16.ffmpegStereo ? "stereo" : "mono"));
                                    var20 = FFMPEG.encodeOgg(var3, var20, var16.ffmpegSamples, var16.ffmpegBitrate, var16.ffmpegStereo);
                                 } catch (IOException var141) {
                                    System.err.println("Resource file '" + var14 + "' could not be encoded!");
                                    var141.printStackTrace();
                                    continue;
                                 }
                              } else if (var16.action == ResourceRulesList.Action.LANGUAGES_ZIP) {
                                 int var156 = var14.lastIndexOf(47);
                                 if (var156 != -1) {
                                    var14 = var14.substring(var156 + 1);
                                 }

                                 System.out.println(" - writing language '" + var14 + "' to '" + var4.getName() + "'");
                                 var154.putNextEntry(new ZipEntry(var14));
                                 var154.write(var20);
                                 continue;
                              }

                              var7.putNextEntry(new ZipEntry("assets/" + var14));
                              var7.write(var20);
                           }
                        }

                        return true;
                     } catch (JSONException | IOException var147) {
                        System.err.println("ERROR: failed to download additional assets from '" + var1.getName() + "'!");
                        var147.printStackTrace();
                     }
                  } finally {
                     if (var154 != null) {
                        var154.close();
                     }

                  }
               } catch (Throwable var149) {
                  if (var9 == null) {
                     var9 = var149;
                  } else if (var9 != var149) {
                     var9.addSuppressed(var149);
                  }

                  throw var9;
               }
            } finally {
               if (var7 != null) {
                  var7.close();
               }

            }

            return false;
         } catch (Throwable var151) {
            if (var5 == null) {
               var5 = var151;
            } else if (var5 != var151) {
               var5.addSuppressed(var151);
            }

            throw var5;
         }
      } catch (IOException var152) {
         System.err.println("ERROR: failed to copy from '" + var0.getName() + "' -> '" + var2.getName() + "'!");
         var152.printStackTrace();
         return false;
      }
   }

   private static String formatByteLength(int var0) {
      if (var0 < 4096) {
         return "" + var0;
      } else {
         return var0 < 4194304 ? var0 / 1024 + "k" : var0 / 1024 / 1024 + "M";
      }
   }
}
