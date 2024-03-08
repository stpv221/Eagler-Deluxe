package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class OpenGLEnumManager {
   private static boolean hasLoaded = false;
   public static final Set<String> classNames = new HashSet();
   public static final Map<String, String> enumsForGLStateManager = new HashMap();
   public static final Map<String, Map<Integer, String>> enumsForFunctionKV = new HashMap();
   public static final Map<String, Map<String, Integer>> enumsForFunctionVK = new HashMap();

   public static boolean loadEnumMap() {
      if (hasLoaded) {
         return true;
      } else {
         hasLoaded = true;

         try {
            String var0 = "/lang/enums.json";
            System.out.println("Loading OpenGL enums: " + var0);
            int var1 = 0;
            int var2 = 0;
            Throwable var4 = null;
            Object var5 = null;

            String var3;
            try {
               InputStream var6 = OpenGLEnumManager.class.getResourceAsStream(var0);

               try {
                  if (var6 == null) {
                     throw new FileNotFoundException("classpath:/" + var0);
                  }

                  var3 = IOUtils.toString(var6, "UTF-8");
               } finally {
                  if (var6 != null) {
                     var6.close();
                  }

               }
            } catch (Throwable var42) {
               if (var4 == null) {
                  var4 = var42;
               } else if (var4 != var42) {
                  var4.addSuppressed(var42);
               }

               throw var4;
            }

            JSONArray var44 = (new JSONObject(var3)).getJSONArray("enums");
            Iterator var46 = var44.toList().iterator();

            List var7;
            while(var46.hasNext()) {
               var5 = var46.next();
               var7 = (List)var5;
               ArrayList var8 = new ArrayList();
               HashMap var9 = new HashMap();
               HashMap var10 = new HashMap();
               Map var11 = (Map)var7.get(0);
               Iterator var13 = var11.entrySet().iterator();

               while(var13.hasNext()) {
                  Entry var12 = (Entry)var13.next();
                  classNames.add((String)var12.getKey());
                  List var14 = (List)var12.getValue();
                  Iterator var16 = var14.iterator();

                  while(var16.hasNext()) {
                     Object var15 = var16.next();
                     var8.add((String)var12.getKey() + "." + (String)var15);
                  }
               }

               Map var56 = (Map)var7.get(1);
               Iterator var59 = var56.entrySet().iterator();

               while(var59.hasNext()) {
                  Entry var57 = (Entry)var59.next();
                  Map var60 = (Map)var57.getValue();

                  for(Iterator var17 = var60.entrySet().iterator(); var17.hasNext(); ++var2) {
                     Entry var61 = (Entry)var17.next();
                     Integer var18 = Integer.parseInt((String)var61.getKey());
                     var9.put(var18, (String)var61.getValue());
                     var10.put((String)var61.getValue(), var18);
                  }
               }

               var59 = var8.iterator();

               while(var59.hasNext()) {
                  String var58 = (String)var59.next();
                  if (!enumsForFunctionKV.containsKey(var58)) {
                     ++var1;
                     enumsForFunctionKV.put(var58, var9);
                     enumsForFunctionVK.put(var58, var10);
                  }
               }
            }

            String var45 = "/lang/statemgr.json";
            System.out.println("Loading OpenGL enums: " + var45);
            Throwable var47 = null;
            var7 = null;

            try {
               InputStream var51 = OpenGLEnumManager.class.getResourceAsStream(var45);

               try {
                  if (var51 == null) {
                     throw new FileNotFoundException("classpath:/" + var45);
                  }

                  var3 = IOUtils.toString(var51, "UTF-8");
               } finally {
                  if (var51 != null) {
                     var51.close();
                  }

               }
            } catch (Throwable var40) {
               if (var47 == null) {
                  var47 = var40;
               } else if (var47 != var40) {
                  var47.addSuppressed(var40);
               }

               throw var47;
            }

            JSONObject var48 = (new JSONObject(var3)).getJSONObject("statemgr_mappings");
            Iterator var52 = var48.toMap().entrySet().iterator();

            while(var52.hasNext()) {
               Entry var49 = (Entry)var52.next();
               String var53 = (String)var49.getKey();
               String var55 = (String)var49.getValue();
               enumsForGLStateManager.put(var53, var55);
               if (!enumsForFunctionKV.containsKey(var53) && enumsForFunctionKV.containsKey(var55)) {
                  enumsForFunctionKV.put(var53, (Map)enumsForFunctionKV.get(var55));
                  enumsForFunctionVK.put(var53, (Map)enumsForFunctionVK.get(var55));
                  ++var1;
               }
            }

            var52 = enumsForGLStateManager.keySet().iterator();

            while(var52.hasNext()) {
               String var50 = (String)var52.next();
               int var54 = var50.indexOf(46);
               if (var54 != -1) {
                  classNames.add(var50.substring(0, var54));
               }
            }

            System.out.println("Loaded " + var2 + " enums for " + var1 + " functions");
            return true;
         } catch (Throwable var43) {
            System.err.println("ERROR: could not load opengl enum map!");
            var43.printStackTrace();
            return false;
         }
      }
   }

   public static String insertIntoLine(String var0, Consumer<Integer> var1) {
      int var2 = var0.indexOf(46);
      if (var2 != -1) {
         String var3 = var0.substring(0, var2);
         String var4 = var3.trim();
         if (classNames.contains(var4)) {
            String var5 = var0.substring(var2 + 1);
            int var6 = var5.indexOf(40);
            if (var6 != -1) {
               String var7 = var5.substring(var6 + 1);
               var5 = var5.substring(0, var6);
               int var8 = var7.lastIndexOf(41);
               String var9 = "";
               if (var8 == -1) {
                  var8 = var7.length();
               } else {
                  var9 = var7.substring(var8);
               }

               Map var10 = (Map)enumsForFunctionKV.get(var4 + "." + var5);
               if (var10 != null) {
                  var7 = var7.substring(0, var8);
                  String[] var11 = var7.split(", ");
                  int var12 = 0;

                  for(int var13 = 0; var13 < var11.length; ++var13) {
                     Integer var14;
                     try {
                        var14 = Integer.valueOf(var11[var13]);
                     } catch (NumberFormatException var16) {
                        continue;
                     }

                     String var15 = (String)var10.get(var14);
                     if (var15 != null) {
                        var11[var13] = var15;
                        ++var12;
                     }
                  }

                  if (var12 > 0) {
                     var0 = var3 + "." + var5 + "(" + String.join(", ", var11) + var9;
                     if (var1 != null) {
                        var1.accept(var12);
                     }
                  }
               }
            }
         }
      }

      return var0;
   }

   public static String stripFromLine(String var0) {
      int var1 = var0.indexOf(46);
      if (var1 != -1) {
         String var2 = var0.substring(0, var1);
         String var3 = var2.trim();
         if (classNames.contains(var3)) {
            String var4 = var0.substring(var1 + 1);
            int var5 = var4.indexOf(40);
            if (var5 != -1) {
               String var6 = var4.substring(var5 + 1);
               var4 = var4.substring(0, var5);
               int var7 = var6.lastIndexOf(41);
               String var8 = "";
               if (var7 == -1) {
                  var7 = var6.length();
               } else {
                  var8 = var6.substring(var7);
               }

               Map var9 = (Map)enumsForFunctionVK.get(var3 + "." + var4);
               if (var9 != null) {
                  var6 = var6.substring(0, var7);
                  String[] var10 = var6.split(", ");
                  int var11 = 0;

                  for(int var12 = 0; var12 < var10.length; ++var12) {
                     Integer var13 = (Integer)var9.get(var10[var12]);
                     if (var13 != null) {
                        var10[var12] = var13.toString();
                        ++var11;
                     }
                  }

                  if (var11 > 0) {
                     return var2 + "." + var4 + "(" + String.join(", ", var10) + var8;
                  }
               }
            }
         }
      }

      return null;
   }
}
