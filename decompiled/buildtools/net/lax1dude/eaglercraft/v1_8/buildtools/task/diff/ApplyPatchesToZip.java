package net.lax1dude.eaglercraft.v1_8.buildtools.task.diff;

import com.github.difflib.patch.Patch;
import com.github.difflib.patch.PatchFailedException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ApplyPatchesToZip {
   public static final int patchContextLength = 3;

   public static void applyPatches(File var0, File var1, File var2, File var3, boolean var4, boolean var5) throws Throwable {
      if (!var2.isDirectory()) {
         FileUtils.copyFile(var0, var3);
      } else {
         Object var6;
         Throwable var8;
         FileInputStream var9;
         if (var1 != null) {
            System.out.println("Loading files from '" + var1.getName() + "'...");
            Throwable var7 = null;
            var8 = null;

            try {
               var9 = new FileInputStream(var1);

               try {
                  var6 = JARMemoryCache.loadJAR(var9);
               } finally {
                  if (var9 != null) {
                     var9.close();
                  }

               }
            } catch (Throwable var69) {
               if (var7 == null) {
                  var7 = var69;
               } else if (var7 != var69) {
                  var7.addSuppressed(var69);
               }

               throw var7;
            }
         } else {
            var6 = new WeakHashMap();
         }

         Object var74;
         if (var0 != null) {
            System.out.println("Loading files from '" + var0.getName() + "'...");
            var8 = null;
            var9 = null;

            try {
               FileInputStream var10 = new FileInputStream(var0);

               try {
                  var74 = JARMemoryCache.loadJAR(var10);
               } finally {
                  if (var10 != null) {
                     var10.close();
                  }

               }
            } catch (Throwable var71) {
               if (var8 == null) {
                  var8 = var71;
               } else if (var8 != var71) {
                  var8.addSuppressed(var71);
               }

               throw var8;
            }
         } else {
            var74 = new WeakHashMap();
         }

         System.out.println("Patching files in '" + var0.getName() + "'...");
         final HashMap var75 = new HashMap();
         var75.putAll((Map)var6);
         var75.putAll((Map)var74);
         DiffSet var76 = new DiffSet();
         int var77 = var76.loadFolder(var2, var5, var5 ? new DiffSet.SourceProvider() {
            public List<String> getSource(String var1) throws IOException {
               byte[] var2 = (byte[])var75.get(var1);
               if (var2 == null) {
                  throw new FileNotFoundException("Could not find source for: " + var1);
               } else {
                  return Lines.linesList(new String(var2, StandardCharsets.UTF_8));
               }
            }
         } : null);
         System.out.println("   loaded " + var77 + " patch files from the repo");
         System.out.println("   patching files...");
         System.out.print("   ");
         int var11 = 0;
         int var12 = 0;
         int var13 = 0;
         int var14 = 0;
         int var15 = 0;
         Throwable var16 = null;
         Object var17 = null;

         try {
            ZipOutputStream var18 = new ZipOutputStream(new FileOutputStream(var3));

            try {
               var18.setLevel(var4 ? 5 : 0);
               var18.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
               var18.write("Manifest-Version: 1.0\nCreated-By: Eaglercraft BuildTools\n".getBytes(StandardCharsets.UTF_8));
               Iterator var21 = var75.entrySet().iterator();

               Entry var20;
               while(var21.hasNext()) {
                  var20 = (Entry)var21.next();
                  String var19 = (String)var20.getKey();
                  if (!var19.startsWith("META-INF")) {
                     Object var22 = var76.diffs.get(var19);
                     if (var22 != null) {
                        if (var22 instanceof DiffSet.DeleteFunction) {
                           ++var13;
                        } else {
                           if (var22 instanceof DiffSet.ReplaceFunction) {
                              var18.putNextEntry(new ZipEntry(var19));
                              IOUtils.write(((DiffSet.ReplaceFunction)var22).file, var18);
                              ++var14;
                           } else if (var22 instanceof Patch) {
                              var18.putNextEntry(new ZipEntry(var19));
                              List var23 = Lines.linesList(new String((byte[])var20.getValue(), "UTF-8"));

                              try {
                                 var23 = ((Patch)var22).applyTo(var23);
                              } catch (PatchFailedException var67) {
                                 throw new IOException("Could not patch file \"" + var19 + "\"!", var67);
                              }

                              IOUtils.writeLines(var23, (String)null, var18, "UTF-8");
                              ++var15;
                           }

                           ++var11;
                           if (var11 % 75 == 74) {
                              System.out.print(".");
                           }
                        }
                     } else if (((Map)var74).containsKey(var19)) {
                        var18.putNextEntry(new ZipEntry(var19));
                        IOUtils.write((byte[])var20.getValue(), var18);
                        ++var11;
                        if (var11 % 75 == 74) {
                           System.out.print(".");
                        }
                     }
                  }
               }

               var21 = var76.recreate.entrySet().iterator();

               while(var21.hasNext()) {
                  var20 = (Entry)var21.next();
                  var18.putNextEntry(new ZipEntry((String)var20.getKey()));
                  IOUtils.write((byte[])var20.getValue(), var18);
                  ++var12;
                  ++var11;
                  if (var11 % 75 == 74) {
                     System.out.print(".");
                  }
               }
            } finally {
               if (var18 != null) {
                  var18.close();
               }

            }
         } catch (Throwable var73) {
            if (var16 == null) {
               var16 = var73;
            } else if (var16 != var73) {
               var16.addSuppressed(var73);
            }

            throw var16;
         }

         System.out.println();
         System.out.println("Patched " + var15 + " files");
         System.out.println("Restored " + var12 + " files");
         System.out.println("Replaced " + var14 + " files");
         System.out.println("Deleted " + var13 + " files");
         System.out.println();
      }
   }

   public static void writeIntegratedServerClass(String var0, String var1, OutputStream var2) throws IOException {
      ArrayList var3 = new ArrayList(Lines.linesList(var1));

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         String var5 = (String)var3.get(var4);
         if (var5.startsWith("import net.minecraft.")) {
            var3.set(var4, "import net.lax1dude.eaglercraft.v1_8.sp.server.classes." + var5.substring(7));
         } else {
            if (var5.contains(" class ") || var5.startsWith("class ")) {
               var3.addAll(var4 + 1, Arrays.asList("", "\tstatic {", "\t\t__checkIntegratedContextValid(\"" + var0 + "\");", "\t}", ""));
               var3.addAll(var4, Arrays.asList("import static net.lax1dude.eaglercraft.v1_8.sp.server.classes.ContextUtil.__checkIntegratedContextValid;", ""));
               break;
            }

            if (var5.startsWith("package ")) {
               var3.set(var4, "package net.lax1dude.eaglercraft.v1_8.sp.server.classes." + var5.substring(8));
            }
         }
      }

      IOUtils.writeLines(var3, (String)null, var2, "UTF-8");
   }
}
