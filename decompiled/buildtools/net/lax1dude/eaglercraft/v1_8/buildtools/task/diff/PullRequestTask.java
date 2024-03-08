package net.lax1dude.eaglercraft.v1_8.buildtools.task.diff;

import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.zip.CRC32;
import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildTools;
import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildToolsConfig;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.formatter.EclipseFormatter;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.InsertJavaDoc;
import net.lax1dude.eaglercraft.v1_8.buildtools.util.FileWriterUTF;
import org.apache.commons.io.FileUtils;

public class PullRequestTask {
   private static final CharsetDecoder utf8Decoder;
   private static final String hex = "0123456789ABCDEF";

   static {
      utf8Decoder = StandardCharsets.UTF_8.newDecoder();
   }

   public static boolean pullRequest() {
      try {
         return pullRequest0();
      } catch (Throwable var1) {
         System.err.println();
         System.err.println("Exception encountered while running task 'pullrequest'!");
         var1.printStackTrace();
         return false;
      }
   }

   private static boolean pullRequest0() throws Throwable {
      File var0 = new File(EaglerBuildToolsConfig.getTemporaryDirectory(), "MinecraftSrc/minecraft_src.jar");
      File var1 = new File(EaglerBuildToolsConfig.getTemporaryDirectory(), "MinecraftSrc/minecraft_src_patch.jar");
      File var2 = new File(EaglerBuildToolsConfig.getTemporaryDirectory(), "MinecraftSrc/minecraft_src_javadoc.jar");
      File var3 = new File(EaglerBuildTools.repositoryRoot, "sources/main/java");
      File var4 = new File(EaglerBuildTools.repositoryRoot, "sources/teavm/java");
      File var5 = new File(EaglerBuildTools.repositoryRoot, "sources/lwjgl/java");
      File var6 = new File(EaglerBuildToolsConfig.getTemporaryDirectory(), "MinecraftSrc/minecraft_res.jar");
      File var7 = new File(EaglerBuildToolsConfig.getTemporaryDirectory(), "MinecraftSrc/minecraft_res_patch.jar");
      File var8 = new File(EaglerBuildTools.repositoryRoot, "sources/resources");
      File var9 = new File(EaglerBuildToolsConfig.getWorkspaceDirectory(), "src/main/java");
      File var10 = new File(EaglerBuildToolsConfig.getWorkspaceDirectory(), "src/teavm/java");
      File var11 = new File(EaglerBuildToolsConfig.getWorkspaceDirectory(), "src/lwjgl/java");
      File var12 = new File(EaglerBuildToolsConfig.getWorkspaceDirectory(), "desktopRuntime/resources");
      File var13 = new File(EaglerBuildTools.repositoryRoot, "pullrequest");
      boolean var14 = var13.exists();
      if (var14 && (!var13.isDirectory() || var13.list().length != 0)) {
         System.out.println();
         System.out.print("Warning: The 'pullrequest' folder already exists in your repository. Overwrite? [Y/n]: ");
         String var15 = "n";

         try {
            var15 = (new BufferedReader(new InputStreamReader(System.in))).readLine();
         } catch (IOException var20) {
         }

         var15 = var15.toLowerCase();
         if (!var15.startsWith("y")) {
            System.out.println();
            System.out.println("The pull request was cancelled.");
            return true;
         }

         try {
            FileUtils.deleteDirectory(var13);
            var14 = false;
         } catch (IOException var19) {
            System.err.println("ERROR: Could not delete \"" + var13.getAbsolutePath() + "\"!");
            var19.printStackTrace();
            return false;
         }
      }

      if (!var14 && !var13.mkdirs()) {
         System.err.println("ERROR: Could not create folder \"" + var13.getAbsolutePath() + "\"!");
      }

      File var21 = new File(var13, "source");
      File var16 = new File(var13, "resources");
      boolean var17 = false;
      int var18 = copyAllModified(var10, var4);
      if (var18 > 0) {
         var17 = true;
      }

      System.out.println("Found " + var18 + " changed files in /src/teavm/java/");
      var18 = copyAllModified(var11, var5);
      if (var18 > 0) {
         var17 = true;
      }

      System.out.println("Found " + var18 + " changed files in /src/lwjgl/java/");
      var18 = createDiffFiles(var3, var2, var0, var1, var9, var21, true);
      if (var18 > 0) {
         var17 = true;
      }

      System.out.println("Found " + var18 + " changed files in /src/main/java/");
      var18 = createDiffFiles(var8, var7, var6, (File)null, var12, var16, false);
      if (var18 > 0) {
         var17 = true;
      }

      System.out.println("Found " + var18 + " changed files in /desktopRuntime/resources/");
      if (!var17) {
         System.out.println("ERROR: No modified files were found!");
         if (var13.exists()) {
            var13.delete();
         }
      }

      return true;
   }

   private static int createDiffFiles(File var0, File var1, File var2, File var3, File var4, File var5, boolean var6) throws Throwable {
      if (!var4.isDirectory()) {
         return 0;
      } else {
         boolean var7 = var5.isDirectory();
         int var8 = 0;
         Collection var9 = FileUtils.listFiles(var4, (String[])null, true);
         Object var10;
         Throwable var12;
         FileInputStream var13;
         if (var2 != null) {
            System.out.println("Loading files from '" + var2.getName() + "'...");
            Throwable var11 = null;
            var12 = null;

            try {
               var13 = new FileInputStream(var2);

               try {
                  var10 = JARMemoryCache.loadJAR(var13);
               } finally {
                  if (var13 != null) {
                     var13.close();
                  }

               }
            } catch (Throwable var65) {
               if (var11 == null) {
                  var11 = var65;
               } else if (var11 != var65) {
                  var11.addSuppressed(var65);
               }

               throw var11;
            }
         } else {
            var10 = new WeakHashMap();
         }

         Object var70;
         if (var1 != null) {
            System.out.println("Loading files from '" + var1.getName() + "'...");
            var12 = null;
            var13 = null;

            try {
               FileInputStream var14 = new FileInputStream(var1);

               try {
                  var70 = JARMemoryCache.loadJAR(var14);
               } finally {
                  if (var14 != null) {
                     var14.close();
                  }

               }
            } catch (Throwable var67) {
               if (var12 == null) {
                  var12 = var67;
               } else if (var12 != var67) {
                  var12.addSuppressed(var67);
               }

               throw var12;
            }
         } else {
            var70 = new WeakHashMap();
         }

         HashMap var71 = new HashMap();
         var71.putAll((Map)var10);
         var71.putAll((Map)var70);
         Object var72;
         HashSet var15;
         if (var3 != null) {
            System.out.println("Loading files from '" + var3.getName() + "'...");
            Throwable var73 = null;
            var15 = null;

            try {
               FileInputStream var16 = new FileInputStream(var3);

               try {
                  var72 = JARMemoryCache.loadJAR(var16);
               } finally {
                  if (var16 != null) {
                     var16.close();
                  }

               }
            } catch (Throwable var69) {
               if (var73 == null) {
                  var73 = var69;
               } else if (var73 != var69) {
                  var73.addSuppressed(var69);
               }

               throw var73;
            }
         } else {
            var72 = new WeakHashMap();
         }

         System.out.println("Comparing...");
         System.out.println("(this may take a while)");
         String var74 = var4.getAbsolutePath();
         var15 = new HashSet();
         Iterator var17 = var9.iterator();

         while(true) {
            while(var17.hasNext()) {
               File var75 = (File)var17.next();
               String var18 = var75.getAbsolutePath().replace(var74, "");
               if (var18.indexOf(92) != -1) {
                  var18 = var18.replace('\\', '/');
               }

               if (var18.startsWith("/")) {
                  var18 = var18.substring(1);
               }

               File var19 = new File(var0, var18);
               Object var20 = null;
               boolean var21 = var19.exists();
               if (var21) {
                  var15.add(var18);
                  if (copyFileIfChanged(var75, var19)) {
                     ++var8;
                  }
               } else {
                  byte[] var77;
                  if ((var77 = (byte[])var71.get(var18)) == null) {
                     var15.add(var18);
                     FileUtils.copyFile(var75, var19);
                     ++var8;
                  } else {
                     var15.add(var18);
                     byte[] var22 = var77;
                     byte[] var23 = FileUtils.readFileToByteArray(var75);
                     boolean var24 = false;
                     if (var77.length != var23.length) {
                        if (!var7) {
                           if (!var5.mkdirs()) {
                              throw new IOException("Could not create folder: \"" + var5.getAbsolutePath() + "\"!");
                           }

                           var7 = true;
                        }

                        String var25 = null;
                        byte[] var26 = (byte[])((Map)var72).get(var18);
                        if (var26 != null) {
                           var25 = new String(var26, StandardCharsets.UTF_8);
                        }

                        if (writeDiff(var77, var23, var5, var18, var6, var25)) {
                           var24 = true;
                           ++var8;
                        }
                     } else {
                        for(int var78 = 0; var78 < var22.length; ++var78) {
                           if (var22[var78] != var23[var78]) {
                              if (!var7) {
                                 if (!var5.mkdirs()) {
                                    throw new IOException("Could not create folder: \"" + var5.getAbsolutePath() + "\"!");
                                 }

                                 var7 = true;
                              }

                              String var79 = null;
                              byte[] var27 = (byte[])((Map)var72).get(var18);
                              if (var27 != null) {
                                 var79 = new String(var27, StandardCharsets.UTF_8);
                              }

                              if (writeDiff(var22, var23, var5, var18, var6, var79)) {
                                 var24 = true;
                                 ++var8;
                              }
                              break;
                           }
                        }
                     }

                     if (!var24 && !((Map)var70).containsKey(var18)) {
                        FileUtils.writeByteArrayToFile(new File(var5, makeName(var18, "recreate")), var77);
                        ++var8;
                     }
                  }
               }
            }

            if (((Map)var70).size() > 0) {
               var17 = ((Map)var70).entrySet().iterator();

               while(var17.hasNext()) {
                  Entry var76 = (Entry)var17.next();
                  if (!var15.contains(var76.getKey()) && !(new File(var4, (String)var76.getKey())).exists()) {
                     if (!var7) {
                        if (!var5.mkdirs()) {
                           throw new IOException("Could not create folder: \"" + var5.getAbsolutePath() + "\"!");
                        }

                        var7 = true;
                     }

                     FileUtils.writeStringToFile(new File(var5, makeName((String)var76.getKey(), "delete")), "#hash: " + getCRC32((byte[])var76.getValue()), "UTF-8");
                     ++var8;
                  }
               }
            }

            return var8;
         }
      }
   }

   private static boolean writeDiff(byte[] var0, byte[] var1, File var2, String var3, boolean var4, String var5) throws IOException {
      String var6 = toStringIfValid(var0);
      String var7 = var6 == null ? null : toStringIfValid(var1);
      if (var6 != null && var7 != null) {
         if (var5 != null) {
            var6 = var5;
         }

         var7 = stripJavadocAndFormat(var7);
         List var8 = Lines.linesList(var6);
         List var9 = Lines.linesList(var7);
         Patch var10 = DiffUtils.diff(var8, var9);
         List var11 = UnifiedDiffUtils.generateUnifiedDiff(var3, var3, var8, var10, 3);
         if (var11.size() == 0) {
            return false;
         }

         File var12 = new File(var2, makeName(var3, "edit"));
         File var13 = var12.getParentFile();
         if (!var13.isDirectory() && !var13.mkdirs()) {
            throw new IOException("Failed to create directory \"" + var13.getAbsolutePath() + "\"!");
         }

         Throwable var14 = null;
         Object var15 = null;

         try {
            PrintWriter var16 = new PrintWriter(new FileWriterUTF(var12));

            try {
               int var17 = 0;

               for(int var18 = var11.size(); var17 < var18; ++var17) {
                  var16.println((String)var11.get(var17));
               }
            } finally {
               if (var16 != null) {
                  var16.close();
               }

            }
         } catch (Throwable var24) {
            if (var14 == null) {
               var14 = var24;
            } else if (var14 != var24) {
               var14.addSuppressed(var24);
            }

            throw var14;
         }
      } else {
         FileUtils.writeByteArrayToFile(new File(var2, makeName(var3, "replace")), var1);
      }

      return true;
   }

   private static String stripJavadocAndFormat(String var0) {
      var0 = InsertJavaDoc.stripDocForDiff(var0);
      var0 = EclipseFormatter.processSource(var0, System.lineSeparator());
      return var0;
   }

   private static int copyAllModified(File var0, File var1) throws IOException {
      if (!var0.isDirectory()) {
         return 0;
      } else {
         int var2 = 0;
         Collection var3 = FileUtils.listFiles(var0, (String[])null, true);
         String var4 = var0.getAbsolutePath();
         Iterator var6 = var3.iterator();

         while(var6.hasNext()) {
            File var5 = (File)var6.next();
            String var7 = var5.getAbsolutePath().replace(var4, "");
            if (var7.indexOf(92) != -1) {
               var7 = var7.replace('\\', '/');
            }

            if (var7.startsWith("/")) {
               var7 = var7.substring(1);
            }

            File var8 = new File(var1, var7);
            if (copyFileIfChanged(var5, var8)) {
               ++var2;
            }
         }

         return var2;
      }
   }

   private static String makeName(String var0, String var1) {
      int var2 = var0.lastIndexOf(47);
      int var3 = var0.lastIndexOf(46);
      return var3 > var2 + 1 ? var0.substring(0, var3) + "." + var1 + var0.substring(var3) : var0 + "." + var1;
   }

   private static String toStringIfValid(byte[] var0) {
      ByteBuffer var1 = ByteBuffer.wrap(var0);

      CharBuffer var2;
      try {
         var2 = utf8Decoder.decode(var1);
      } catch (Throwable var3) {
         return null;
      }

      return var2.toString();
   }

   private static String hex32(long var0) {
      char[] var2 = new char[8];

      for(int var3 = 7; var3 >= 0; --var3) {
         var2[var3] = "0123456789ABCDEF".charAt((int)(var0 >> (var3 << 2) & 15L));
      }

      return new String(var2);
   }

   private static String getCRC32(File var0) throws IOException {
      CRC32 var1 = new CRC32();
      var1.update(FileUtils.readFileToByteArray(var0));
      return hex32(var1.getValue());
   }

   private static String getCRC32(byte[] var0) {
      CRC32 var1 = new CRC32();
      var1.update(var0);
      return hex32(var1.getValue());
   }

   private static boolean checkCRC32(File var0, File var1) throws IOException {
      CRC32 var2 = new CRC32();
      var2.update(FileUtils.readFileToByteArray(var0));
      long var3 = var2.getValue();
      var2.reset();
      var2.update(FileUtils.readFileToByteArray(var1));
      return var3 != var2.getValue();
   }

   private static boolean copyFileIfChanged(File var0, File var1) throws IOException {
      if (!var1.exists()) {
         FileUtils.copyFile(var0, var1);
         return true;
      } else if (var0.lastModified() == var1.lastModified()) {
         return false;
      } else {
         CRC32 var2 = new CRC32();
         byte[] var3 = FileUtils.readFileToByteArray(var0);
         var2.update(var3);
         long var4 = var2.getValue();
         var2.reset();
         byte[] var6 = FileUtils.readFileToByteArray(var1);
         var2.update(var6);
         if (var4 != var2.getValue()) {
            FileUtils.writeByteArrayToFile(var1, var3);
            return true;
         } else {
            return false;
         }
      }
   }
}
