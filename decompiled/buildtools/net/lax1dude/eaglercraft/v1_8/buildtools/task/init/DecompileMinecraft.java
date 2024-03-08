package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildTools;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.diff.ApplyPatchesToZip;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.formatter.EclipseFormatter;
import net.lax1dude.eaglercraft.v1_8.buildtools.util.JARSubprocess;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class DecompileMinecraft {
   public static boolean decompileMinecraft(File var0, File var1, File var2, File var3, boolean var4) throws Throwable {
      File var5 = new File(var2, "minecraft_classes.jar");
      System.out.println();
      System.out.println("Extracting '" + var1.getAbsolutePath() + "\" to \"" + var5.getAbsolutePath() + "\"...");
      int var6 = 0;

      try {
         Throwable var7 = null;
         Object var8 = null;

         try {
            ZipInputStream var9 = new ZipInputStream(new FileInputStream(var1));

            try {
               ZipOutputStream var10 = new ZipOutputStream(new FileOutputStream(var5));

               try {
                  var10.setLevel(0);
                  var10.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
                  var10.write("Manifest-Version: 1.0\nCreated-By: Eaglercraft BuildTools\n".getBytes(StandardCharsets.UTF_8));

                  ZipEntry var11;
                  while((var11 = var9.getNextEntry()) != null) {
                     String var12;
                     if (!var11.isDirectory() && (var12 = var11.getName()).endsWith(".class")) {
                        ZipEntry var13 = new ZipEntry(var12);
                        var10.putNextEntry(var13);
                        IOUtils.copy(var9, var10);
                        ++var6;
                     }
                  }
               } finally {
                  if (var10 != null) {
                     var10.close();
                  }

               }
            } catch (Throwable var89) {
               if (var7 == null) {
                  var7 = var89;
               } else if (var7 != var89) {
                  var7.addSuppressed(var89);
               }

               if (var9 != null) {
                  var9.close();
               }

               throw var7;
            }

            if (var9 != null) {
               var9.close();
            }
         } catch (Throwable var90) {
            if (var7 == null) {
               var7 = var90;
            } else if (var7 != var90) {
               var7.addSuppressed(var90);
            }

            throw var7;
         }
      } catch (IOException var91) {
         System.err.println("ERROR: failed to extract \"" + var1.getAbsolutePath() + "\" to \"" + var5.getAbsolutePath() + "\"!");
         var91.printStackTrace();
         if (var5.exists()) {
            var5.delete();
         }

         return false;
      }

      System.out.println("Extracted " + var6 + " class files.");
      File var93 = new File(var2, "minecraft_specialsource.jar");
      System.out.println();
      System.out.println("Running SpecialSource...");
      int var92 = JARSubprocess.runJava(var0, new String[]{"-cp", var5.getAbsolutePath() + JARSubprocess.classPathSeperator + "runtime.jar", "net.md_5.specialsource.SpecialSource", "-i", var5.getAbsolutePath(), "-o", var93.getAbsolutePath(), "-m", "minecraft.srg", "--kill-source"}, "   [SpecialSource]");
      var5.delete();
      if (var92 != 0) {
         System.err.println("ERROR: MCP SpecialSource execution failed!");
         return false;
      } else {
         System.out.println("SpecialSource completed successfully.");
         System.out.println();
         File var94 = new File(var2, "minecraft_mcinjector.jar");
         System.out.println("Running MCInjector...");
         var92 = JARSubprocess.runJava(var0, new String[]{"-cp", var5.getAbsolutePath() + JARSubprocess.classPathSeperator + "runtime.jar", "de.oceanlabs.mcp.mcinjector.MCInjector", "--jarIn", var93.getAbsolutePath(), "--jarOut", var94.getAbsolutePath(), "--mapIn", "minecraft.exc", "--jsonIn", "exceptor.json", "--lvt", "STRIP"}, "   [MCInjector]");
         var93.delete();
         if (var92 != 0) {
            System.err.println("ERROR: MCP MCInjector execution failed!");
            return false;
         } else {
            System.out.println("MCInjector completed successfully.");
            System.out.println();
            File var95 = new File(var2, "fernflower.tmp");
            if (var95.isFile()) {
               var95.delete();
            } else if (var95.isDirectory()) {
               FileUtils.deleteDirectory(var95);
            }

            if (!var95.mkdir()) {
               System.err.println("ERROR: Could not create Fernflower output directory!");
               return false;
            } else {
               System.out.println("Decompiling with Fernflower...");
               System.out.println("This will take a while, go get a drink or something lol.");
               System.out.println();
               var92 = JARSubprocess.runJava(var0, new String[]{"-jar", "fernflower.jar", "-din=1", "-rbr=1", "-dgs=1", "-asc=1", "-rsy=1", "-iec=1", "-ren=0", "-jvn=1", "-udv=1", "-ump=1", "-log=WARN", var94.getAbsolutePath(), var95.getAbsolutePath()}, "   [Fernflower]");
               var94.delete();
               if (var92 != 0) {
                  System.err.println("ERROR: Fernflower decompiler failed!");
                  return false;
               } else {
                  System.out.println("Decompiler completed successfully.");
                  System.out.println();
                  File[] var96 = var95.listFiles();
                  File var97 = null;

                  for(int var98 = 0; var98 < var96.length; ++var98) {
                     if (var96[var98].getName().endsWith(".jar")) {
                        if (var96[var98].getName().equalsIgnoreCase("minecraft_mcinjector.jar")) {
                           var97 = var96[var98];
                        } else if (var97 == null) {
                           var97 = var96[var98];
                        }
                     }
                  }

                  if (var97 == null) {
                     System.err.println("Could not find Fernflower output jar! (in " + var95.getAbsolutePath() + ")");
                     return false;
                  } else {
                     File var99 = new File(var2, "minecraft_src.jar");
                     System.out.println("Formatting source for patches...");
                     System.out.println("   (Using default Eclipse format)");
                     System.out.print("   ");
                     var6 = 0;
                     Throwable var14 = null;
                     File var15 = null;

                     try {
                        ZipInputStream var16 = new ZipInputStream(new FileInputStream(var97));

                        try {
                           ZipOutputStream var17 = new ZipOutputStream(new FileOutputStream(var99));

                           try {
                              var17.setLevel(5);
                              var17.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
                              var17.write("Manifest-Version: 1.0\nCreated-By: Eaglercraft BuildTools\n".getBytes(StandardCharsets.UTF_8));

                              ZipEntry var18;
                              while((var18 = var16.getNextEntry()) != null) {
                                 String var19;
                                 if ((var19 = var18.getName()).endsWith(".java")) {
                                    String var20 = IOUtils.toString(var16, "UTF-8");
                                    var20 = EclipseFormatter.processSource(var20, "\n");
                                    ZipEntry var21 = new ZipEntry(var19);
                                    var17.putNextEntry(var21);
                                    IOUtils.write(var20, var17, "UTF-8");
                                    ++var6;
                                    if (var6 % 75 == 74) {
                                       System.out.print(".");
                                    }
                                 } else if (!var19.startsWith("META-INF")) {
                                    ZipEntry var104 = new ZipEntry(var19);
                                    var17.putNextEntry(var104);
                                    IOUtils.copy(var16, var17, 4096);
                                 }
                              }
                           } finally {
                              if (var17 != null) {
                                 var17.close();
                              }

                           }
                        } catch (Throwable var86) {
                           if (var14 == null) {
                              var14 = var86;
                           } else if (var14 != var86) {
                              var14.addSuppressed(var86);
                           }

                           if (var16 != null) {
                              var16.close();
                           }

                           throw var14;
                        }

                        if (var16 != null) {
                           var16.close();
                        }
                     } catch (Throwable var87) {
                        if (var14 == null) {
                           var14 = var87;
                        } else if (var14 != var87) {
                           var14.addSuppressed(var87);
                        }

                        throw var14;
                     }

                     System.out.println();
                     System.out.println("Formatted " + var6 + " classes.");
                     System.out.println();

                     try {
                        FileUtils.deleteDirectory(var95);
                     } catch (IOException var84) {
                     }

                     File var100 = new File(var2, "minecraft_src_patch.jar");

                     try {
                        ApplyPatchesToZip.applyPatches(var99, (File)null, new File(EaglerBuildTools.repositoryRoot, "patches/minecraft"), var100, true, true);
                     } catch (Throwable var83) {
                        System.err.println("ERROR: Could not apply 'patches' directory to: " + var100.getName());
                        var83.printStackTrace();
                        return false;
                     }

                     var15 = new File(var2, "minecraft_src_javadoc.jar");
                     CSVMappings var101 = var4 ? new CSVMappings() : null;
                     if (!InsertJavaDoc.processSource(var100, var15, var0, var101)) {
                        System.err.println("ERROR: Could not create javadoc!");
                        return false;
                     } else {
                        File var102 = new File(var2, "minecraft_res.jar");
                        if (!LoadResources.loadResources(var1, var3, var102, var0, new File(var2, "minecraft_languages.zip"))) {
                           System.err.println("ERROR: Could not copy resources!");
                           return false;
                        } else {
                           File var103 = new File(var2, "minecraft_res_patch.jar");

                           try {
                              ApplyPatchesToZip.applyPatches(var102, (File)null, new File(EaglerBuildTools.repositoryRoot, "patches/resources"), var103, true, true);
                              return true;
                           } catch (Throwable var82) {
                              System.err.println("ERROR: Could not apply 'patches' directory to: " + var103.getName());
                              var82.printStackTrace();
                              return false;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
