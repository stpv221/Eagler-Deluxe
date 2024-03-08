package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildTools;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.diff.Lines;
import net.lax1dude.eaglercraft.v1_8.buildtools.util.FileReaderUTF;
import org.apache.commons.io.IOUtils;

public class InsertJavaDoc {
   public static final String enumImport = "import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;";
   private static final String[] typeModifiersFields = new String[]{"public", "private", "protected", "static", "final", "volatile", "transient"};
   private static final String[] typeModifiersMethods = new String[]{"public", "private", "protected", "static", "final", "synchronized", "abstract", "default"};
   private static final Pattern illegalCharactersNotATypeName = Pattern.compile("[^a-zA-Z0-9_\\-\\$\\[\\]<>\\.]");

   private static boolean isTypeModifierField(String var0) {
      for(int var1 = 0; var1 < typeModifiersFields.length; ++var1) {
         if (typeModifiersFields[var1].equals(var0)) {
            return true;
         }
      }

      return false;
   }

   private static boolean isTypeModifierMethod(String var0) {
      for(int var1 = 0; var1 < typeModifiersMethods.length; ++var1) {
         if (typeModifiersMethods[var1].equals(var0)) {
            return true;
         }
      }

      return false;
   }

   public static boolean processSource(File var0, File var1, File var2, CSVMappings var3) throws Throwable {
      return processSource(var0, var1, var2, var3, true);
   }

   public static boolean processSource(File var0, File var1, File var2, CSVMappings var3, boolean var4) throws Throwable {
      System.out.println("Adding javadoc...");
      if (var3 == null) {
         System.out.println("(writing enums only, skipping field/method annotations)");
      }

      ArrayList var5 = null;
      Throwable var6 = null;
      HashMap var7 = null;

      try {
         BufferedReader var8 = new BufferedReader(new FileReaderUTF(new File(EaglerBuildTools.repositoryRoot, "patches/minecraft/output_license.txt")));

         try {
            var5 = new ArrayList();
            var5.add("/**+");

            while(true) {
               String var9;
               if ((var9 = var8.readLine()) == null) {
                  var5.add(" * ");
                  var5.add(" */");
                  break;
               }

               var5.add(" * " + var9);
            }
         } finally {
            if (var8 != null) {
               var8.close();
            }

         }
      } catch (Throwable var209) {
         if (var6 == null) {
            var6 = var209;
         } else if (var6 != var209) {
            var6.addSuppressed(var209);
         }

         throw var6;
      }

      HashMap var210 = new HashMap();
      var7 = new HashMap();
      if (var3 != null) {
         File var211 = new File(var2, "methods.csv");

         Throwable var10;
         FileReaderUTF var11;
         try {
            Throwable var213 = null;
            var10 = null;

            try {
               var11 = new FileReaderUTF(var211);

               try {
                  var3.loadMethodsFile(var11);
               } finally {
                  if (var11 != null) {
                     var11.close();
                  }

               }
            } catch (Throwable var197) {
               if (var213 == null) {
                  var213 = var197;
               } else if (var213 != var197) {
                  var213.addSuppressed(var197);
               }

               throw var213;
            }
         } catch (IOException var198) {
            System.err.println("ERROR: failed to read \"" + var211.getAbsolutePath() + "\"!");
            var198.printStackTrace();
            return false;
         }

         File var214 = new File(var2, "fields.csv");

         try {
            var10 = null;
            var11 = null;

            try {
               FileReaderUTF var12 = new FileReaderUTF(var214);

               try {
                  var3.loadFieldsFile(var12);
               } finally {
                  if (var12 != null) {
                     var12.close();
                  }

               }
            } catch (Throwable var200) {
               if (var10 == null) {
                  var10 = var200;
               } else if (var10 != var200) {
                  var10.addSuppressed(var200);
               }

               throw var10;
            }
         } catch (IOException var201) {
            System.err.println("ERROR: failed to read \"" + var214.getAbsolutePath() + "\"!");
            var201.printStackTrace();
            return false;
         }

         var10 = null;
         var11 = null;

         try {
            BufferedReader var218 = new BufferedReader(new FileReaderUTF(new File(var2, "joined.srg")));

            String var13;
            try {
               while((var13 = var218.readLine()) != null) {
                  int var14;
                  String var16;
                  if (var13.startsWith("MD:")) {
                     var13 = var13.trim();
                     var14 = var13.lastIndexOf(32);
                     int var15 = var13.lastIndexOf(32, var14 - 1);
                     var13 = var13.substring(var15 + 1, var14);
                     var14 = var13.lastIndexOf(47);
                     var16 = var13.substring(0, var14);
                     String var17 = var13.substring(var14 + 1);
                     CSVMappings.Symbol var18 = (CSVMappings.Symbol)var3.csvMethodsMappings.get(var17);
                     if (var18 != null && var18.comment != null && var18.comment.length() > 0) {
                        Object var19 = (List)var210.get(var16);
                        if (var19 == null) {
                           var210.put(var16, var19 = new ArrayList());
                        }

                        ((List)var19).add(var18);
                     }
                  } else if (var13.startsWith("FD:")) {
                     var13 = var13.trim();
                     var14 = var13.lastIndexOf(32);
                     var13 = var13.substring(var14 + 1);
                     var14 = var13.lastIndexOf(47);
                     String var222 = var13.substring(0, var14);
                     var16 = var13.substring(var14 + 1);
                     CSVMappings.Symbol var225 = (CSVMappings.Symbol)var3.csvFieldsMappings.get(var16);
                     if (var225 != null && var225.comment != null && var225.comment.length() > 0) {
                        Object var227 = (List)var7.get(var222);
                        if (var227 == null) {
                           var7.put(var222, var227 = new ArrayList());
                        }

                        ((List)var227).add(var225);
                     }
                  }
               }
            } finally {
               if (var218 != null) {
                  var218.close();
               }

            }
         } catch (Throwable var207) {
            if (var10 == null) {
               var10 = var207;
            } else if (var10 != var207) {
               var10.addSuppressed(var207);
            }

            throw var10;
         }
      }

      OpenGLEnumManager.loadEnumMap();
      System.out.print("   ");
      int var212 = 0;
      int var215 = 0;
      int var217 = 0;
      final int[] var216 = new int[1];
      Consumer var219 = new Consumer<Integer>() {
         public void accept(Integer var1) {
            int[] var10000 = var216;
            var10000[0] += var1;
         }
      };

      try {
         Throwable var220 = null;
         Object var221 = null;

         try {
            ZipInputStream var223 = new ZipInputStream(new FileInputStream(var0));

            try {
               ZipOutputStream var224 = new ZipOutputStream(new FileOutputStream(var1));

               try {
                  var224.setLevel(var4 ? 5 : 0);
                  var224.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
                  var224.write("Manifest-Version: 1.0\nCreated-By: Eaglercraft BuildTools\n".getBytes(StandardCharsets.UTF_8));

                  ZipEntry var226;
                  while((var226 = var223.getNextEntry()) != null) {
                     if (!var226.isDirectory()) {
                        String var228 = var226.getName();
                        if (!var228.endsWith(".java")) {
                           if (!var228.startsWith("META-INF")) {
                              ZipEntry var230 = new ZipEntry(var228);
                              var224.putNextEntry(var230);
                              IOUtils.copy(var223, var224, 4096);
                           }
                        } else {
                           String var229 = IOUtils.toString(var223, "UTF-8");
                           ArrayList var20 = new ArrayList();
                           var20.addAll(Lines.linesList(var229));
                           if (var5 != null) {
                              for(int var21 = 0; var21 < var20.size(); ++var21) {
                                 String var22 = (String)var20.get(var21);
                                 if (!var22.startsWith("import ") && (var22.contains(" class ") || var22.contains(" enum ") || var22.contains(" interface ") || var22.contains(" @interface ") || var22.startsWith("class ") || var22.startsWith("enum ") || var22.startsWith("interface ") || var22.startsWith("@interface "))) {
                                    var20.addAll(var21, var5);
                                    int var10000 = var21 + var5.size();
                                    break;
                                 }
                              }
                           }

                           String var231 = var228.substring(0, var228.length() - 5);
                           if (var231.startsWith("net/lax1dude/eaglercraft/v1_8/sp/server/classes/")) {
                              var231 = var231.substring(48);
                           }

                           List var232 = var3 == null ? null : (List)var210.get(var231);
                           List var23 = var3 == null ? null : (List)var7.get(var231);
                           int var24;
                           String var27;
                           if (var232 != null || var23 != null) {
                              label5497:
                              for(var24 = 0; var24 < var20.size(); ++var24) {
                                 String var25 = (String)var20.get(var24);
                                 boolean var26 = var25.endsWith(";");
                                 var27 = var25;

                                 String var28;
                                 for(var28 = ""; var27.length() > 0 && Character.isWhitespace(var27.charAt(0)); var27 = var27.substring(1)) {
                                    var28 = var28 + var27.charAt(0);
                                 }

                                 String[] var29 = var27.split("\\s+");
                                 boolean var30 = false;
                                 boolean var31 = false;
                                 boolean var32 = false;

                                 for(int var33 = 0; var33 < var29.length; ++var33) {
                                    if (var29[var33].length() > 0) {
                                       boolean var34 = false;
                                       boolean var35 = false;
                                       if (isTypeModifierField(var29[var33])) {
                                          var34 = true;
                                          var30 = true;
                                       }

                                       if (!var26 && isTypeModifierMethod(var29[var33])) {
                                          var35 = true;
                                          var31 = true;
                                       }

                                       if (!var34 && !var35) {
                                          if (var32) {
                                             boolean var36 = false;
                                             int var38;
                                             if (var30 && var33 < var29.length - 1 && var29[var33 + 1].equals("=")) {
                                                if (var23 == null) {
                                                   break;
                                                }

                                                int var237 = 0;
                                                var38 = var23.size();

                                                while(true) {
                                                   if (var237 >= var38) {
                                                      continue label5497;
                                                   }

                                                   CSVMappings.Symbol var238 = (CSVMappings.Symbol)var23.get(var237);
                                                   if (var238.name.equals(var29[var33])) {
                                                      List var239 = wordWrapComment(var238.comment, var28);
                                                      var20.addAll(var24, var239);
                                                      var24 += var239.size();
                                                      ++var217;
                                                      continue label5497;
                                                   }

                                                   ++var237;
                                                }
                                             }

                                             int var236;
                                             if (((var236 = var29[var33].indexOf(40)) == -1 || var33 <= 0) && !var31 || var232 == null || var236 <= 0) {
                                                break;
                                             }

                                             String var37 = var29[var33].substring(0, var236);
                                             var38 = 0;
                                             int var39 = var232.size();

                                             while(true) {
                                                if (var38 >= var39) {
                                                   continue label5497;
                                                }

                                                CSVMappings.Symbol var40 = (CSVMappings.Symbol)var232.get(var38);
                                                if (var40.name.equals(var37)) {
                                                   List var41 = wordWrapComment(var40.comment, var28);
                                                   var20.addAll(var24, var41);
                                                   var24 += var41.size();
                                                   ++var215;
                                                   continue label5497;
                                                }

                                                ++var38;
                                             }
                                          }

                                          if (illegalCharactersNotATypeName.matcher(var29[var33]).find()) {
                                             break;
                                          }

                                          var32 = true;
                                       }
                                    }
                                 }
                              }
                           }

                           var24 = var216[0];
                           int var233 = 0;

                           int var234;
                           for(var234 = var20.size(); var233 < var234; ++var233) {
                              var20.set(var233, OpenGLEnumManager.insertIntoLine((String)var20.get(var233), var219));
                           }

                           if (var24 != var216[0]) {
                              var233 = 0;

                              for(var234 = var20.size(); var233 < var234; ++var233) {
                                 var27 = (String)var20.get(var233);
                                 if (var27.startsWith("package")) {
                                    var20.addAll(var233 + 1, Arrays.asList("", "import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;"));
                                    break;
                                 }
                              }
                           }

                           ZipEntry var235 = new ZipEntry(var228);
                           var224.putNextEntry(var235);
                           IOUtils.write(String.join(System.lineSeparator(), var20), var224, "UTF-8");
                           ++var212;
                           if (var212 % 75 == 74) {
                              System.out.print(".");
                           }
                        }
                     }
                  }
               } finally {
                  if (var224 != null) {
                     var224.close();
                  }

               }
            } catch (Throwable var203) {
               if (var220 == null) {
                  var220 = var203;
               } else if (var220 != var203) {
                  var220.addSuppressed(var203);
               }

               if (var223 != null) {
                  var223.close();
               }

               throw var220;
            }

            if (var223 != null) {
               var223.close();
            }
         } catch (Throwable var204) {
            if (var220 == null) {
               var220 = var204;
            } else if (var220 != var204) {
               var220.addSuppressed(var204);
            }

            throw var220;
         }
      } catch (IOException var205) {
         System.err.println("Failed to process jar '" + var0.getName() + "' and write it to '" + var1.getName() + "!");
         var205.printStackTrace();
         return false;
      }

      System.out.println();
      System.out.println("Added " + var216[0] + " OpenGL enums");
      if (var3 != null) {
         System.out.println("Added " + var215 + " comments to methods");
         System.out.println("Added " + var217 + " comments to fields");
      }

      System.out.println();
      return true;
   }

   private static List<String> wordWrapComment(String var0, String var1) {
      String[] var2 = var0.split("\\s+");
      ArrayList var3 = new ArrayList();
      var3.add(var1 + "/**+");
      String var4 = "";

      for(int var5 = 0; var5 < var2.length; ++var5) {
         if (var4.length() > 0 && var2[var5].length() + var4.length() > 60) {
            var3.add(var1 + " * " + var4);
            var4 = "";
         }

         var4 = var4 + (var4.length() > 0 ? " " + var2[var5] : var2[var5]);
      }

      if (var4.length() > 0) {
         var3.add(var1 + " * " + var4);
      }

      var3.add(var1 + " */");
      return var3;
   }

   public static String stripDocForDiff(String var0) {
      List var1 = Lines.linesList(var0);
      OpenGLEnumManager.loadEnumMap();
      ArrayList var2 = new ArrayList();
      boolean var3 = false;
      int var4 = 0;

      for(int var5 = var1.size(); var4 < var5; ++var4) {
         String var6 = (String)var1.get(var4);
         if (var6.trim().startsWith("/**+")) {
            while(var4 < var5 && !((String)var1.get(var4)).endsWith("*/")) {
               ++var4;
            }
         } else {
            String var7 = OpenGLEnumManager.stripFromLine(var6);
            if (var7 != null) {
               var2.add(var7);
               var3 = true;
            } else {
               var2.add(var6);
            }
         }
      }

      if (var3) {
         var4 = var2.indexOf("import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;");
         if (var4 != -1) {
            if (var4 - 1 >= 0 && ((String)var2.get(var4 - 1)).trim().length() == 0 && var2.size() > 1) {
               --var4;
               var2.remove(var4);
               var2.remove(var4);
            } else {
               var2.remove(var4);
            }
         }
      }

      return String.join(System.lineSeparator(), var2);
   }
}
