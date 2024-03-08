package net.lax1dude.eaglercraft.v1_8.buildtools.task.diff;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.Patch;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildToolsConfig;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.CSVMappings;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.InsertJavaDoc;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.SetupWorkspace;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class MergePullRequest {
   private static final CharsetDecoder utf8Decoder;

   static {
      utf8Decoder = StandardCharsets.UTF_8.newDecoder();
   }

   public static boolean mergeTask() {
      try {
         return mergeTask0();
      } catch (Throwable var1) {
         System.err.println();
         System.err.println("Exception encountered while running task 'merge'!");
         var1.printStackTrace();
         return false;
      }
   }

   private static boolean mergeTask0() throws Throwable {
      File var0 = new File("pullrequest");
      if (var0.isDirectory() && !FileUtils.isEmptyDirectory(var0)) {
         if ((new File(var0, "merged.txt")).exists()) {
            System.err.println("ERROR: the 'pullrequest' directory has already been merged, aborting merge because there's nothing to merge");
            System.err.println("To override, delete 'merged.txt' from the folder.");
            return false;
         } else {
            System.out.println();
            System.out.println("Warning: running 'merge' is a command only intended to be used");
            System.out.println("by the repository's owner, it will perminantly incorporate all");
            System.out.println("changes in the 'pullrequest' directory into this repository's");
            System.out.println("patch file directory!");
            System.out.println();
            System.out.println("Doing so will make it impossible to reliably create any future");
            System.out.println("pull requests back to this project's main repository, unless the");
            System.out.println("main repository has merged the same pull request into it's patch");
            System.out.println("file directory too.");
            System.out.println();
            System.out.println("Back up the current state of the patch file directory in a local");
            System.out.println("commit or branch to allow you to undo any unintentional changes");
            System.out.println("made to the directory as a result of running this command.");
            System.out.println();
            System.out.print("Do you really want to do this? [Y/n]: ");
            String var1 = "n";

            try {
               var1 = (new BufferedReader(new InputStreamReader(System.in))).readLine();
            } catch (IOException var25) {
            }

            var1 = var1.toLowerCase();
            if (!var1.startsWith("y")) {
               System.out.println();
               System.out.println("OKAY THANK GOD, crisis averted!");
               System.out.println();
               System.out.println("Thank the author of this tool kindly for providing this check.");
               return true;
            } else {
               System.out.println();
               System.out.println("Warning: close all programs that may have files or folders open");
               System.out.println("in the repository or the merge could fail catastrophically");
               System.out.println();
               System.out.println("This folder: " + (new File(".")).getAbsolutePath());
               System.out.println();
               System.out.println("Check for any file explorer windows displaying the contents of a");
               System.out.println("file or folder in this directory.");
               System.out.println();
               System.out.println("Close any programs with files open someplace in this folder.");
               System.out.println();
               System.out.println("If merging fails, revert all changes in this directory with git");
               System.out.println("or a backup, re-run 'init', then run 'pullrequest' and 'merge'");
               System.out.println();
               System.out.print("Did you close everything? [Y/n]: ");
               var1 = "n";

               try {
                  var1 = (new BufferedReader(new InputStreamReader(System.in))).readLine();
               } catch (IOException var24) {
               }

               var1 = var1.toLowerCase();
               if (!var1.startsWith("y")) {
                  System.out.println();
                  System.out.println("OKAY THANK GOD, crisis averted!");
                  System.out.println();
                  System.out.println("Thank the author of this tool kindly for providing this check.");
                  return true;
               } else {
                  System.out.println();
                  File var2 = EaglerBuildToolsConfig.getTemporaryDirectory();
                  System.out.println();
                  File var3 = new File(var0, "source");
                  File var4 = new File(var0, "resources");
                  boolean var5 = var3.isDirectory() && !FileUtils.isEmptyDirectory(var3);
                  boolean var6 = var4.isDirectory() && !FileUtils.isEmptyDirectory(var4);
                  if (!var5 && !var6) {
                     System.err.println("ERROR: the 'pullrequest' directory does not exist or is empty, aborting merge because there's nothing to merge");
                     return false;
                  } else {
                     File var7;
                     File var8;
                     File var9;
                     File var10;
                     File var11;
                     File var12;
                     int var13;
                     if (var5) {
                        var7 = new File(var2, "MinecraftSrc/minecraft_src.jar");
                        if (!var7.isFile()) {
                           System.err.println("ERROR: file '" + var7.getName() + "' was not found!");
                           System.err.println("Run the 'init' task again to re-generate it");
                           return false;
                        }

                        var8 = new File(var2, "MinecraftSrc/minecraft_src_patch.jar");
                        if (!var8.isFile()) {
                           System.err.println("ERROR: file '" + var8.getName() + "' was not found!");
                           System.err.println("Run the 'init' task again to re-generate it");
                           return false;
                        }

                        var9 = new File(var2, "MinecraftSrc/minecraft_src_merge.jar");
                        var10 = new File(var2, "MinecraftSrc/minecraft_src_merge_diffs.zip");
                        System.out.println("Applying pull request to '" + var8.getName() + "'...");
                        System.out.println();
                        ApplyPatchesToZip.applyPatches(var8, var7, var3, var9, true, false);

                        try {
                           createMergeDiffs(var9, var7, var10);
                        } catch (Throwable var23) {
                           var9.delete();
                           throw var23;
                        }

                        System.out.println();
                        var11 = new File("./patches/minecraft");
                        var12 = new File("./patches.bak/minecraft");
                        if (var11.exists()) {
                           System.out.println("Backing up '" + var11.getAbsolutePath() + "'...");

                           try {
                              FileUtils.deleteDirectory(var12);
                              FileUtils.moveDirectory(var11, var12);
                           } catch (Throwable var22) {
                              var9.delete();
                              throw var22;
                           }
                        }

                        FileUtils.copyFile(new File(var12, "output_license.txt"), new File(var11, "output_license.txt"));
                        System.out.println("Extracting '" + var10 + "' to 'patches/minecraft'...");
                        var13 = SetupWorkspace.extractJarTo(var10, var11);
                        if (!var10.delete()) {
                           System.err.println("ERROR: could not delete '" + var10.getName() + "'!");
                        }

                        System.out.println("Wrote " + var13 + " files.");
                        System.out.println("Copying '" + var9.getName() + "' to '" + var8.getName() + "'...");
                        if ((!var8.exists() || var8.delete()) && var9.renameTo(var8)) {
                           File var14 = new File(var2, "MinecraftSrc/minecraft_src_javadoc.jar");
                           CSVMappings var15 = new CSVMappings();
                           if (!InsertJavaDoc.processSource(var8, var14, new File(var2, "ModCoderPack"), var15)) {
                              System.err.println();
                              System.err.println("ERROR: Could not create javadoc!");
                              return false;
                           }
                        } else {
                           System.err.println("ERROR: could not copy '" + var9.getName() + "' to '" + var8.getName() + "'!");
                           System.err.println("Run the 'init' task again before proceeding");
                           var8.delete();
                        }

                        if (var9.exists()) {
                           var9.delete();
                        }

                        System.out.println("Deleting backup folder...");

                        try {
                           FileUtils.deleteDirectory(var12);
                        } catch (Throwable var21) {
                           System.err.println("ERROR: could not delete 'patches.bak/minecraft'!");
                           System.err.println(var21.toString());
                        }

                        System.out.println();
                     }

                     if (var6) {
                        var7 = new File(var2, "MinecraftSrc/minecraft_res.jar");
                        if (!var7.isFile()) {
                           System.err.println("ERROR: file '" + var7.getName() + "' was not found!");
                           System.err.println("Run the 'init' task again to re-generate it");
                           return false;
                        }

                        var8 = new File(var2, "MinecraftSrc/minecraft_res_patch.jar");
                        if (!var8.isFile()) {
                           System.err.println("ERROR: file '" + var8.getName() + "' was not found!");
                           System.err.println("Run the 'init' task again to re-generate it");
                           return false;
                        }

                        var9 = new File(var2, "MinecraftSrc/minecraft_res_merge.jar");
                        var10 = new File(var2, "MinecraftSrc/minecraft_res_merge_diffs.zip");
                        System.out.println("Applying pull request to '" + var8.getName() + "'...");
                        System.out.println();
                        ApplyPatchesToZip.applyPatches(var8, var7, var4, var9, true, false);

                        try {
                           createMergeDiffs(var9, var7, var10);
                        } catch (Throwable var20) {
                           var9.delete();
                           throw var20;
                        }

                        System.out.println();
                        var11 = new File("./patches/resources");
                        var12 = new File("./patches.bak/resources");
                        if (var11.exists()) {
                           System.out.println("Backing up '" + var11.getAbsolutePath() + "'...");

                           try {
                              FileUtils.deleteDirectory(var12);
                              FileUtils.moveDirectory(var11, var12);
                           } catch (Throwable var19) {
                              var9.delete();
                              throw var19;
                           }
                        }

                        System.out.println("Extracting '" + var10 + "' to 'patches/resources'...");
                        var13 = SetupWorkspace.extractJarTo(var10, var11);
                        if (!var10.delete()) {
                           System.err.println("ERROR: could not delete '" + var10.getName() + "'!");
                        }

                        System.out.println("Wrote " + var13 + " files.");
                        System.out.println("Copying '" + var9.getName() + "' to '" + var8.getName() + "'...");
                        if (var8.exists() && !var8.delete() || !var9.renameTo(var8)) {
                           System.err.println("ERROR: could not copy '" + var9.getName() + "' to '" + var8.getName() + "'!");
                           System.err.println("Run the 'init' task again before proceeding");
                           var8.delete();
                        }

                        if (var9.exists()) {
                           var9.delete();
                        }

                        System.out.println("Deleting backup folder...");

                        try {
                           FileUtils.deleteDirectory(var12);
                        } catch (Throwable var18) {
                           System.err.println("ERROR: could not delete 'patches.bak/resources'!");
                           System.err.println(var18.getMessage());
                        }

                        System.out.println();
                     }

                     (new File("./patches.bak")).delete();
                     System.out.println("Successfully merged pullrequest directory!");

                     try {
                        SimpleDateFormat var26 = new SimpleDateFormat("MM-dd-yy");
                        SimpleDateFormat var28 = new SimpleDateFormat("kk:mm:ss");
                        Date var29 = new Date();
                        FileUtils.writeStringToFile(new File(var0, "merged.txt"), "This pullrequest was merged on " + var26.format(new Date()) + " at " + var28.format(var29) + ".", "UTF-8");
                     } catch (IOException var17) {
                        System.err.println("ERROR: could not write 'merged.txt' in pullrequest directory!");
                        System.err.println("Creating a file called 'merged.txt' is important to tell buildtools that the");
                        System.err.println("existing pullrequest has already been merged! Do not try to merge it again!");
                     }

                     System.out.println("Backing up to 'pullrequest_merged_backup'...");
                     String var27 = var0.getAbsolutePath();
                     if (var27.endsWith("/") || var27.endsWith("\\")) {
                        var27 = var27.substring(0, var27.length() - 1);
                     }

                     var8 = new File(var27 + "_merged_backup");
                     if (var8.exists() && !FileUtils.deleteQuietly(var8)) {
                        System.err.println("Could not delete old backup!");
                        var8 = new File(var27 + "_merged_backup1");
                        if (var8.exists() && !FileUtils.deleteQuietly(var8)) {
                           System.err.println("Could not delete 2nd old backup!");
                           return true;
                        }
                     }

                     try {
                        FileUtils.moveDirectory(var0, var8);
                     } catch (IOException var16) {
                        System.err.println("Could not create backup!");
                     }

                     return true;
                  }
               }
            }
         }
      } else {
         System.err.println("ERROR: the 'pullrequest' directory does not exist or is empty, aborting merge because there's nothing to merge");
         return false;
      }
   }

   private static void createMergeDiffs(File var0, File var1, File var2) throws Throwable {
      System.out.println("Creating patches from '" + var0.getName() + "'...");
      System.out.println("Loading files from '" + var1.getName() + "'...");
      Throwable var4 = null;
      Throwable var5 = null;

      Map var3;
      FileInputStream var6;
      try {
         var6 = new FileInputStream(var1);

         try {
            var3 = JARMemoryCache.loadJAR(var6);
         } finally {
            if (var6 != null) {
               var6.close();
            }

         }
      } catch (Throwable var53) {
         if (var4 == null) {
            var4 = var53;
         } else if (var4 != var53) {
            var4.addSuppressed(var53);
         }

         throw var4;
      }

      if (var3 == null) {
         throw new IOException("Failed to load JAR into memory: '" + var1.getName());
      } else {
         System.out.println("Loading files from '" + var0.getName() + "'...");
         var5 = null;
         var6 = null;

         Map var56;
         try {
            FileInputStream var7 = new FileInputStream(var0);

            try {
               var56 = JARMemoryCache.loadJAR(var7);
            } finally {
               if (var7 != null) {
                  var7.close();
               }

            }
         } catch (Throwable var51) {
            if (var5 == null) {
               var5 = var51;
            } else if (var5 != var51) {
               var5.addSuppressed(var51);
            }

            throw var5;
         }

         if (var56 == null) {
            throw new IOException("Failed to load JAR into memory: '" + var0.getName());
         } else {
            HashSet var57 = new HashSet();
            var57.addAll(var3.keySet());
            System.out.println("Generating patch files..");
            System.out.println("(Writing to: " + var2.getName() + ")");
            System.out.println("(this may take a while)");
            System.out.print("   ");
            int var58 = 0;
            Throwable var59 = null;
            Object var8 = null;

            try {
               ZipOutputStream var9 = new ZipOutputStream(new FileOutputStream(var2));

               try {
                  var9.setLevel(5);
                  Iterator var11 = var56.entrySet().iterator();

                  while(var11.hasNext()) {
                     Entry var10 = (Entry)var11.next();
                     String var12 = (String)var10.getKey();
                     byte[] var13 = (byte[])var3.get(var12);
                     if (var13 == null) {
                        System.err.println("Error: tried to patch file '" + var12 + "' that doesn't exist in the minecraft source");
                     } else {
                        var57.remove(var12);
                        if (writeDiff(var13, (byte[])var10.getValue(), var12, var9)) {
                           ++var58;
                           if (var58 % 75 == 74) {
                              System.out.print(".");
                           }
                        }
                     }
                  }

                  System.out.println();
                  System.out.println("Wrote " + var58 + " patch files.");
                  var9.putNextEntry(new ZipEntry("delete.txt"));
                  PrintWriter var60 = new PrintWriter(var9);
                  var60.println("# " + var57.size() + " files to delete:");
                  Iterator var62 = var57.iterator();

                  while(var62.hasNext()) {
                     String var61 = (String)var62.next();
                     var60.println(var61);
                  }

                  var60.flush();
                  System.out.println("Wrote " + var57.size() + " deletes.");
               } finally {
                  if (var9 != null) {
                     var9.close();
                  }

               }
            } catch (Throwable var55) {
               if (var59 == null) {
                  var59 = var55;
               } else if (var59 != var55) {
                  var59.addSuppressed(var55);
               }

               throw var59;
            }
         }
      }
   }

   private static boolean writeDiff(byte[] var0, byte[] var1, String var2, ZipOutputStream var3) throws IOException {
      if (Arrays.equals(var0, var1)) {
         return false;
      } else {
         String var4 = toStringIfValid(var0);
         String var5 = var4 == null ? null : toStringIfValid(var1);
         if (var4 != null && var5 != null) {
            List var6 = Lines.linesList(var4);
            List var7 = Lines.linesList(var5);
            Patch var8 = DiffUtils.diff(var6, var7);
            var3.putNextEntry(new ZipEntry(makeName(var2, "edit")));
            PrintWriter var9 = new PrintWriter(new OutputStreamWriter(var3, "UTF-8"));
            EaglerContextRedacted.writeContextRedacted(var8, var9);
            var9.flush();
            return true;
         } else {
            var3.putNextEntry(new ZipEntry(makeName(var2, "replace")));
            IOUtils.write(var1, var3);
            return true;
         }
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

   public static boolean mergeDirect() {
      try {
         return mergeDirect0();
      } catch (Throwable var1) {
         System.err.println();
         System.err.println("Exception encountered while running task 'merge_direct'!");
         var1.printStackTrace();
         return false;
      }
   }

   private static boolean mergeDirect0() throws Throwable {
      if (!PullRequestTask.pullRequest()) {
         System.err.println();
         System.err.println("Error: could not create merge_direct pull request!");
         return false;
      } else {
         try {
            if (!mergeTask0()) {
               System.err.println();
               System.err.println("Exception encountered while running task 'merge_direct'!");
               return false;
            } else {
               return true;
            }
         } catch (Throwable var1) {
            System.err.println();
            System.err.println("Exception encountered while running task 'merge_direct'!");
            var1.printStackTrace();
            return false;
         }
      }
   }
}
