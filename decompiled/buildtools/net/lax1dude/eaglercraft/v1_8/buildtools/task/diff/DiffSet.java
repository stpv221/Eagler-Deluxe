package net.lax1dude.eaglercraft.v1_8.buildtools.task.diff;

import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

public class DiffSet {
   public static final DiffSet.DeleteFunction deleteFunction = new DiffSet.DeleteFunction((DiffSet.DeleteFunction)null);
   public final Map<String, Object> diffs = new HashMap();
   public final Map<String, byte[]> recreate = new HashMap();
   private static final Pattern editPattern = Pattern.compile(".*\\.edit(\\.[^\\.\\/\\\\]+)?$");
   private static final Pattern replacePattern = Pattern.compile(".*\\.replace(\\.[^\\.\\/\\\\]+)?$");
   private static final Pattern deletePattern = Pattern.compile(".*\\.delete(\\.[^\\.\\/\\\\]+)?$");
   private static final Pattern recreatePattern = Pattern.compile(".*\\.recreate(\\.[^\\.\\/\\\\]+)?$");

   public int loadFolder(File var1, boolean var2, DiffSet.SourceProvider var3) throws IOException {
      String var4 = var1.getAbsolutePath();
      int var5 = 0;
      File var6 = new File(var1, "delete.txt");
      if (var6.isFile()) {
         List var7 = FileUtils.readLines(var6, "UTF-8");
         Iterator var9 = var7.iterator();

         while(var9.hasNext()) {
            String var8 = (String)var9.next();
            var8 = var8.trim();
            var8 = var8.replace('\\', '/');
            if (!var8.startsWith("#")) {
               if (var8.startsWith("/")) {
                  var8 = var8.substring(1);
               }

               this.diffs.put(var8, deleteFunction);
            }
         }
      }

      Collection var31 = FileUtils.listFiles(var1, (String[])null, true);
      Iterator var33 = var31.iterator();

      while(true) {
         while(var33.hasNext()) {
            File var32 = (File)var33.next();
            String var10 = var32.getAbsolutePath().replace(var4, "").replace('\\', '/');
            if (var10.startsWith("/")) {
               var10 = var10.substring(1);
            }

            String var11;
            if (editPattern.matcher(var10).matches()) {
               try {
                  var11 = removeExt(var10, "edit");
                  Patch var12;
                  if (var2) {
                     Throwable var13 = null;
                     Object var14 = null;

                     try {
                        BufferedReader var15 = new BufferedReader(new InputStreamReader(new FileInputStream(var32), StandardCharsets.UTF_8));

                        try {
                           var12 = EaglerContextRedacted.readContextRestricted(var3.getSource(var11), var15);
                        } finally {
                           if (var15 != null) {
                              var15.close();
                           }

                        }
                     } catch (Throwable var29) {
                        if (var13 == null) {
                           var13 = var29;
                        } else if (var13 != var29) {
                           var13.addSuppressed(var29);
                        }

                        throw var13;
                     }
                  } else {
                     List var34 = FileUtils.readLines(var32, "UTF-8");
                     var12 = UnifiedDiffUtils.parseUnifiedDiff(var34);
                  }

                  if (var12 == null) {
                     throw new IOException("Invalid DIFF file!");
                  }

                  this.diffs.put(var11, var12);
                  ++var5;
               } catch (Throwable var30) {
                  System.err.println("ERROR: could not read '" + var10 + "'!");
               }
            } else if (replacePattern.matcher(var10).matches()) {
               try {
                  this.diffs.put(removeExt(var10, "replace"), new DiffSet.ReplaceFunction(FileUtils.readFileToByteArray(var32), (DiffSet.ReplaceFunction)null));
                  ++var5;
               } catch (Throwable var27) {
                  System.err.println("ERROR: could not read '" + var10 + "'!");
               }
            } else if (deletePattern.matcher(var10).matches()) {
               this.diffs.put(removeExt(var10, "delete"), deleteFunction);
               ++var5;
            } else if (recreatePattern.matcher(var10).matches()) {
               try {
                  var11 = removeExt(var10, "recreate");
                  this.recreate.put(var11, FileUtils.readFileToByteArray(var32));
                  this.diffs.remove(var11);
                  ++var5;
               } catch (Throwable var26) {
                  System.err.println("ERROR: could not read '" + var10 + "'!");
               }
            }
         }

         return var5;
      }
   }

   private static String removeExt(String var0, String var1) {
      int var2 = var0.lastIndexOf("." + var1);
      return var2 != -1 ? var0.substring(0, var2) + var0.substring(var2 + var1.length() + 1, var0.length()) : var0;
   }

   public static class DeleteFunction {
      private DeleteFunction() {
      }

      // $FF: synthetic method
      DeleteFunction(DiffSet.DeleteFunction var1) {
         this();
      }
   }

   public static class ReplaceFunction {
      public final byte[] file;

      private ReplaceFunction(byte[] var1) {
         this.file = var1;
      }

      // $FF: synthetic method
      ReplaceFunction(byte[] var1, DiffSet.ReplaceFunction var2) {
         this(var1);
      }
   }

   public interface SourceProvider {
      List<String> getSource(String var1) throws IOException;
   }
}
