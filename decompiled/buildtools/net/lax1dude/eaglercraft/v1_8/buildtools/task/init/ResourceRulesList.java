package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResourceRulesList {
   private final List<ResourceRulesList.ResourceRule> list;
   private static final ResourceRulesList.ResourceRule defaultRule;

   static {
      defaultRule = new ResourceRulesList.ResourceRule("", true, ResourceRulesList.Action.EXCLUDE, 16000, 48, false);
   }

   public static ResourceRulesList loadResourceRules(File var0) throws IOException {
      ArrayList var1 = new ArrayList();

      try {
         JSONArray var2 = (new JSONObject(FileUtils.readFileToString(var0, StandardCharsets.UTF_8))).getJSONArray("rules");
         int var3 = 0;

         for(int var4 = var2.length(); var3 < var4; ++var3) {
            JSONObject var5 = var2.getJSONObject(var3);

            String var7;
            boolean var9;
            ResourceRulesList.Action var10;
            int var11;
            int var12;
            boolean var13;
            for(Iterator var6 = var5.keys(); var6.hasNext(); var1.add(new ResourceRulesList.ResourceRule(var7, var9, var10, var11, var12, var13))) {
               var7 = (String)var6.next();
               JSONObject var8 = var5.getJSONObject(var7);
               var9 = var7.endsWith("*");
               if (var9) {
                  var7 = var7.substring(0, var7.length() - 1);
               }

               var10 = ResourceRulesList.Action.valueOf(var8.getString("action").toUpperCase());
               var11 = 16000;
               var12 = 48;
               var13 = false;
               if (var10 == ResourceRulesList.Action.ENCODE) {
                  JSONObject var14 = var8.optJSONObject("ffmpeg", (JSONObject)null);
                  if (var14 != null) {
                     var11 = var14.optInt("samples", var11);
                     var12 = var14.optInt("bitrate", var12);
                     var13 = var14.optBoolean("stereo", var13);
                  }
               }
            }
         }
      } catch (JSONException var15) {
         throw new IOException("Invalid JSON file: " + var0.getAbsolutePath(), var15);
      }

      return new ResourceRulesList(var1);
   }

   private ResourceRulesList(List<ResourceRulesList.ResourceRule> var1) {
      this.list = var1;
   }

   public ResourceRulesList.ResourceRule get(String var1) {
      int var2 = 0;

      for(int var3 = this.list.size(); var2 < var3; ++var2) {
         ResourceRulesList.ResourceRule var4 = (ResourceRulesList.ResourceRule)this.list.get(var2);
         if (var4.wildcard) {
            if (var1.startsWith(var4.path)) {
               return var4;
            }
         } else if (var1.equals(var4.path)) {
            return var4;
         }
      }

      return defaultRule;
   }

   public static enum Action {
      INCLUDE,
      EXCLUDE,
      ENCODE,
      LANGUAGES_ZIP;
   }

   public static class ResourceRule {
      private final String path;
      private final boolean wildcard;
      public final ResourceRulesList.Action action;
      public final int ffmpegSamples;
      public final int ffmpegBitrate;
      public final boolean ffmpegStereo;

      protected ResourceRule(String var1, boolean var2, ResourceRulesList.Action var3, int var4, int var5, boolean var6) {
         this.path = var1;
         this.wildcard = var2;
         this.action = var3;
         this.ffmpegSamples = var4;
         this.ffmpegBitrate = var5;
         this.ffmpegStereo = var6;
      }
   }
}
