package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import net.lax1dude.eaglercraft.v1_8.buildtools.decompiler.ParameterSplitter;
import net.lax1dude.eaglercraft.v1_8.buildtools.util.FileReaderUTF;
import net.lax1dude.eaglercraft.v1_8.buildtools.util.FileWriterUTF;

public class GenerateEXCs {
   public static boolean generateEXCs(File var0, File var1, CSVMappings var2) {
      System.out.println();
      System.out.println("Generating \"" + var1.getName() + "\" from \"" + var0.getName() + "\"...");
      File var3 = new File(var0, "params.csv");

      HashSet var5;
      try {
         Throwable var4 = null;
         var5 = null;

         try {
            FileReaderUTF var6 = new FileReaderUTF(var3);

            try {
               var2.loadParamsFile(var6);
            } finally {
               if (var6 != null) {
                  var6.close();
               }

            }
         } catch (Throwable var93) {
            if (var4 == null) {
               var4 = var93;
            } else if (var4 != var93) {
               var4.addSuppressed(var93);
            }

            throw var4;
         }
      } catch (IOException var94) {
         System.err.println("ERROR: failed to read \"" + var3.getAbsolutePath() + "\"!");
         var94.printStackTrace();
         return false;
      }

      HashMap var101 = new HashMap();
      var5 = new HashSet();
      int var102 = 0;
      int var7 = 0;

      try {
         Throwable var8 = null;
         Object var9 = null;

         try {
            BufferedReader var10 = new BufferedReader(new FileReaderUTF(new File(var0, "joined.exc")));

            try {
               PrintWriter var11 = new PrintWriter(new FileWriterUTF(var1));

               try {
                  label2487:
                  while(true) {
                     String var12;
                     String var14;
                     int var16;
                     int var17;
                     String var20;
                     int var21;
                     String var111;
                     String var113;
                     String[] var117;
                     if ((var12 = var10.readLine()) == null) {
                        var11.println();
                        var11.println("# auto generated entries start here:");
                        Throwable var103 = null;
                        var14 = null;

                        try {
                           BufferedReader var107 = new BufferedReader(new FileReaderUTF(new File(var0, "joined.srg")));

                           try {
                              while(true) {
                                 int var24;
                                 String var26;
                                 CSVMappings.Param[] var118;
                                 int var119;
                                 do {
                                    while(true) {
                                       do {
                                          do {
                                             do {
                                                if ((var12 = var107.readLine()) == null) {
                                                   break label2487;
                                                }
                                             } while(!var12.startsWith("MD:"));

                                             var16 = var12.lastIndexOf(32);
                                          } while(var16 <= 0);

                                          var17 = var12.lastIndexOf(32, var16 - 1);
                                          var111 = var12.substring(var17 + 1, var16);
                                          var20 = var12.substring(var16 + 1);
                                          var113 = var111.substring(var111.lastIndexOf(47) + 1);
                                          var21 = var113.lastIndexOf(95);
                                          if (var21 != -1 && var113.lastIndexOf(95, var21 - 1) > 0) {
                                             var113 = var113.substring(0, var21);
                                          }
                                       } while(!var5.add(var113));

                                       var117 = ParameterSplitter.getParameterSigArray(var20, "par");
                                       var118 = (CSVMappings.Param[])var2.csvParamsForFunction.get(var113);
                                       if (var118 != null) {
                                          var24 = 0;

                                          for(var119 = 0; var119 < var118.length; ++var119) {
                                             if (var118[var119] != null) {
                                                ++var24;
                                             }
                                          }
                                          break;
                                       }

                                       if (var117 != null && var117.length > 0) {
                                          var7 += var117.length;
                                          var24 = var111.lastIndexOf(47);
                                          String var25 = var111.substring(0, var24);
                                          var26 = var111.substring(var24 + 1);
                                          CSVMappings.Symbol var27 = (CSVMappings.Symbol)var2.csvMethodsMappings.get(var26);
                                          if (var27 != null) {
                                             var26 = var27.name;
                                          }

                                          var111 = var25 + "." + var26;
                                          var11.println(var111 + var20 + "=|" + String.join(",", var117));
                                       }
                                    }
                                 } while(var24 <= 0);

                                 var24 = 0;

                                 for(var119 = 0; var119 < var118.length; ++var119) {
                                    if (var118[var119] != null) {
                                       int var120 = var24++;
                                       if (var120 < var117.length) {
                                          var117[var120] = var118[var119].name;
                                          ++var102;
                                       }
                                    }
                                 }

                                 var119 = var111.lastIndexOf(47);
                                 var26 = var111.substring(0, var119);
                                 String var121 = var111.substring(var119 + 1);
                                 CSVMappings.Symbol var28 = (CSVMappings.Symbol)var2.csvMethodsMappings.get(var121);
                                 if (var28 != null) {
                                    var121 = var28.name;
                                 }

                                 var111 = var26 + "." + var121;
                                 var11.println(var111 + var20 + "=|" + String.join(",", var117));
                              }
                           } finally {
                              if (var107 != null) {
                                 var107.close();
                              }

                           }
                        } catch (Throwable var96) {
                           if (var103 == null) {
                              var103 = var96;
                           } else if (var103 != var96) {
                              var103.addSuppressed(var96);
                           }

                           throw var103;
                        }
                     }

                     int var13 = var12.lastIndexOf(124);
                     if (var13 != -1) {
                        var14 = var12.substring(0, var13);
                        String var15 = null;
                        var16 = var14.indexOf(40);
                        if (var16 != -1) {
                           var15 = var14.substring(0, var16);
                           var15 = var14.substring(var15.lastIndexOf(46) + 1);
                        }

                        if (var15 != null) {
                           var5.add(var15);
                        }

                        if (var13 != var12.length() - 1) {
                           var101.clear();
                           String[] var108 = var12.substring(var13 + 1).split(",");
                           String[] var110 = new String[var108.length];
                           int var112 = 0;

                           int var114;
                           for(var114 = 0; var114 < var108.length; ++var114) {
                              CSVMappings.Param var116 = (CSVMappings.Param)var2.csvParamsMappings.get(var108[var114]);
                              if (var116 != null) {
                                 var110[var114] = var116.name;
                                 ++var102;
                                 ++var112;
                              }
                           }

                           if (var112 != var108.length) {
                              if (var16 != -1) {
                                 var20 = var14.substring(var16);
                                 var20 = var20.substring(0, var20.indexOf(61));
                                 var7 += ParameterSplitter.getParameterArray(var20, var110);
                              }

                              for(var114 = 0; var114 < var110.length; ++var114) {
                                 if (var110[var114] == null) {
                                    var110[var114] = "param0" + var114;
                                 }
                              }
                           }

                           var12 = var14 + "|" + String.join(",", var110);
                        } else if (var15 != null) {
                           var17 = var15.indexOf(95);
                           int var18 = var15.lastIndexOf(95);
                           if (var18 > var17) {
                              var15 = var15.substring(0, var18 - 1);
                           }

                           CSVMappings.Param[] var19 = (CSVMappings.Param[])var2.csvParamsForFunction.get(var15);
                           var20 = null;
                           if (var16 != -1) {
                              var20 = var14.substring(var16);
                              var20 = var20.substring(0, var20.indexOf(61));
                           }

                           if (var19 == null) {
                              if (var20 != null) {
                                 String[] var115 = ParameterSplitter.getParameterSigArray(var20, "par");
                                 if (var115 != null) {
                                    var12 = var14 + "|" + String.join(",", var115);
                                 }

                                 var7 += var115.length;
                              }
                           } else {
                              var21 = 0;

                              for(int var22 = 0; var22 < var19.length; ++var22) {
                                 if (var19[var22] != null) {
                                    ++var21;
                                 }
                              }

                              var117 = new String[var21];
                              var21 = 0;

                              for(int var23 = 0; var23 < var19.length; ++var23) {
                                 if (var19[var23] != null) {
                                    var117[var21++] = var19[var23].name;
                                    ++var102;
                                 }
                              }

                              var12 = var14 + "|" + String.join(",", var117);
                           }
                        }
                     }

                     int var104 = var12.indexOf(40);
                     if (var104 != -1) {
                        int var106 = var12.lastIndexOf(46, var104);
                        if (var106 != -1) {
                           String var105 = var12.substring(var106 + 1, var104);
                           CSVMappings.Symbol var109 = (CSVMappings.Symbol)var2.csvMethodsMappings.get(var105);
                           if (var109 != null) {
                              var111 = var12.substring(0, var106);
                              var113 = var12.substring(var104);
                              var12 = var111 + "." + var109.name + var113;
                           }
                        }
                     }

                     var11.println(var12);
                  }
               } finally {
                  if (var11 != null) {
                     var11.close();
                  }

               }
            } catch (Throwable var98) {
               if (var8 == null) {
                  var8 = var98;
               } else if (var8 != var98) {
                  var8.addSuppressed(var98);
               }

               if (var10 != null) {
                  var10.close();
               }

               throw var8;
            }

            if (var10 != null) {
               var10.close();
            }
         } catch (Throwable var99) {
            if (var8 == null) {
               var8 = var99;
            } else if (var8 != var99) {
               var8.addSuppressed(var99);
            }

            throw var8;
         }
      } catch (IOException var100) {
         System.err.println("ERROR: failed to write \"" + var1.getName() + "\" from \"joined.exc\"!");
         var100.printStackTrace();
         return false;
      }

      System.out.println("   - Deobf " + var102 + " params to \"" + var1.getName() + "\"");
      System.out.println("   - Generate " + var7 + " params to \"" + var1.getName() + "\"");
      return true;
   }
}
