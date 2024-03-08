package net.lax1dude.eaglercraft.v1_8.buildtools.task.diff;

import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.ChangeDelta;
import com.github.difflib.patch.Chunk;
import com.github.difflib.patch.DeleteDelta;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.InsertDelta;
import com.github.difflib.patch.Patch;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EaglerContextRedacted {
   // $FF: synthetic field
   private static volatile int[] $SWITCH_TABLE$com$github$difflib$patch$DeltaType;

   public static void writeContextRedacted(Patch<String> var0, PrintWriter var1) {
      Date var2 = new Date();
      var1.println();
      var1.println("# Eagler Context Redacted Diff");
      var1.println("# Copyright (c) " + (new SimpleDateFormat("yyyy")).format(var2) + " lax1dude. All rights reserved.");
      var1.println();
      var1.println("# Version: 1.0");
      var1.println("# Author: lax1dude");
      var1.println();
      int var3 = 0;
      int var4 = 0;
      List var5 = var0.getDeltas();
      int var6 = 0;

      for(int var7 = var5.size(); var6 < var7; ++var6) {
         AbstractDelta var8 = (AbstractDelta)var5.get(var6);
         DeltaType var9 = var8.getType();
         String var10;
         String var11;
         switch($SWITCH_TABLE$com$github$difflib$patch$DeltaType()[var9.ordinal()]) {
         case 1:
            var10 = "> CHANGE";
            var11 = "~ ";
            break;
         case 2:
            var10 = "> DELETE";
            var11 = "- ";
            break;
         case 3:
            var10 = "> INSERT";
            var11 = "+ ";
            break;
         case 4:
            continue;
         default:
            throw new IllegalArgumentException("Invalid type " + var9 + " for delta " + var6);
         }

         Chunk var12 = var8.getSource();
         int var13 = var12.getPosition();
         int var14 = var12.getLines().size();
         int var15 = var13 - var3;
         Chunk var16 = var8.getTarget();
         int var17 = var16.getPosition();
         List var18 = var16.getLines();
         int var19 = var18.size();
         int var20 = var17 - var4;
         var1.println(var10 + "  " + var20 + (var19 > 0 ? " : " + (var20 + var19) : "") + "  @  " + var15 + (var14 > 0 ? " : " + (var15 + var14) : ""));
         var1.println();
         if (var19 > 0) {
            int var21 = 0;

            for(int var22 = var18.size(); var21 < var22; ++var21) {
               var1.println(var11 + (String)var18.get(var21));
            }

            var1.println();
         }

         var3 = var13 + var14;
         var4 = var17 + var19;
      }

      var1.println("> EOF");
   }

   public static Patch<String> readContextRestricted(List<String> var0, BufferedReader var1) throws IOException {
      Patch var2 = new Patch();
      DeltaType var3 = null;
      int var4 = 0;
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      ArrayList var8 = null;
      int var9 = 0;
      int var10 = 0;

      while(true) {
         while(true) {
            String var11;
            if ((var11 = var1.readLine()) != null) {
               if (var11.length() < 2) {
                  continue;
               }

               if (var11.charAt(1) != ' ') {
                  throw new IOException("Unknown line type: " + var11.substring(0, 2));
               }

               char var12 = var11.charAt(0);
               String var13 = var11.substring(2);
               String var16;
               switch(var12) {
               case '#':
                  int var14 = var13.indexOf(58);
                  if (var14 > 0) {
                     String var17 = var13.substring(0, var14).trim().toLowerCase();
                     if (var17.equals("version")) {
                        var16 = var13.substring(var14 + 1).trim();
                        if (!var16.equals("1.0")) {
                           throw new IOException("Unsupported format version: " + var16);
                        }
                     }
                  }
                  continue;
               case '+':
                  if (var3 != DeltaType.INSERT) {
                     throw new IOException("Read an unexpected INSERT line in a " + var3 + " block: " + var11);
                  }

                  if (var8 == null) {
                     var8 = new ArrayList();
                  }

                  var8.add(var13);
                  continue;
               case '-':
                  if (var3 != DeltaType.DELETE) {
                     throw new IOException("Read an unexpected DELETE line in a " + var3 + " block: " + var11);
                  }

                  if (var8 == null) {
                     var8 = new ArrayList();
                  }

                  var8.add(var13);
                  continue;
               case '>':
                  String[] var15 = var13.trim().split("[\\s]+");
                  if (var15.length != 1 || !var15[0].equals("EOF")) {
                     if (var15.length < 4 || (!var15[2].equals("@") || var15.length != 4 && (var15.length != 6 || !var15[4].equals(":"))) && (!var15[2].equals(":") || (var15.length != 6 || !var15[4].equals("@")) && (var15.length != 8 || !var15[4].equals("@") || !var15[6].equals(":")))) {
                        throw new IOException("Invalid block: [ " + String.join(" ", var15) + " ]");
                     }

                     if (var3 != null) {
                        var9 += var4;
                        var10 += var6;
                        var2.addDelta(makeDelta(var3, var9, var5, var10, var7, var0, var8));
                        var9 += var5;
                        var10 += var7;
                     }

                     label106: {
                        switch((var16 = var15[0]).hashCode()) {
                        case -2130463047:
                           if (var16.equals("INSERT")) {
                              var3 = DeltaType.INSERT;
                              break label106;
                           }
                           break;
                        case 1986660272:
                           if (var16.equals("CHANGE")) {
                              var3 = DeltaType.CHANGE;
                              break label106;
                           }
                           break;
                        case 2012838315:
                           if (var16.equals("DELETE")) {
                              var3 = DeltaType.DELETE;
                              break label106;
                           }
                        }

                        throw new IOException("Unknown line block: " + var15[0]);
                     }

                     var8 = null;
                     var6 = parseInt(var15[1]);
                     if (var15[2].equals(":")) {
                        var7 = parseInt(var15[3]) - var6;
                        var4 = parseInt(var15[5]);
                        if (var15.length == 8) {
                           var5 = parseInt(var15[7]) - var4;
                        } else {
                           var5 = 0;
                        }
                     } else {
                        var7 = 0;
                        var4 = parseInt(var15[3]);
                        if (var15.length == 6) {
                           var5 = parseInt(var15[5]) - var4;
                        } else {
                           var5 = 0;
                        }
                     }
                     continue;
                  }
                  break;
               case '~':
                  if (var3 != DeltaType.CHANGE) {
                     throw new IOException("Read an unexpected CHANGE line in a " + var3 + " block: " + var11);
                  }

                  if (var8 == null) {
                     var8 = new ArrayList();
                  }

                  var8.add(var13);
                  continue;
               default:
                  throw new IOException("Unknown line type: " + var12);
               }
            }

            if (var3 != null) {
               var9 += var4;
               var10 += var6;
               var2.addDelta(makeDelta(var3, var9, var5, var10, var7, var0, var8));
               int var10000 = var9 + var5;
               var10000 = var10 + var7;
            }

            return var2;
         }
      }
   }

   private static int parseInt(String var0) throws IOException {
      try {
         return Integer.parseInt(var0);
      } catch (NumberFormatException var1) {
         throw new IOException("Value is not a valid integer: \"" + var0 + "\"");
      }
   }

   private static AbstractDelta<String> makeDelta(DeltaType var0, int var1, int var2, int var3, int var4, List<String> var5, List<String> var6) throws IOException {
      ArrayList var7 = new ArrayList(var2);

      for(int var8 = 0; var8 < var2; ++var8) {
         var7.add((String)var5.get(var1 + var8));
      }

      if (var6 == null) {
         var6 = new ArrayList(0);
      }

      if (var4 != ((List)var6).size()) {
         throw new IOException(var0 + " block at sourceStart " + var1 + " is " + var4 + " lines long but only " + ((List)var6).size() + " lines were read!");
      } else {
         switch($SWITCH_TABLE$com$github$difflib$patch$DeltaType()[var0.ordinal()]) {
         case 1:
            return new ChangeDelta(new Chunk(var1, var7), new Chunk(var3, (List)var6));
         case 2:
            return new DeleteDelta(new Chunk(var1, var7), new Chunk(var3, (List)var6));
         case 3:
            return new InsertDelta(new Chunk(var1, var7), new Chunk(var3, (List)var6));
         default:
            throw new IllegalArgumentException("Invalid delta type: " + var0);
         }
      }
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$github$difflib$patch$DeltaType() {
      int[] var10000 = $SWITCH_TABLE$com$github$difflib$patch$DeltaType;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[DeltaType.values().length];

         try {
            var0[DeltaType.CHANGE.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[DeltaType.DELETE.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[DeltaType.EQUAL.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[DeltaType.INSERT.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$com$github$difflib$patch$DeltaType = var0;
         return var0;
      }
   }
}
