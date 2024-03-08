package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVMappings {
   public final Map<String, CSVMappings.Symbol> csvFieldsMappings = new HashMap();
   public final Map<String, CSVMappings.Symbol> csvMethodsMappings = new HashMap();
   public final Map<String, CSVMappings.Param> csvParamsMappings = new HashMap();
   public final Map<String, CSVMappings.Param[]> csvParamsForFunction = new HashMap();

   public void loadMethodsFile(Reader var1) throws IOException {
      this.loadSymbols(var1, this.csvMethodsMappings, "methods.csv");
   }

   public void loadFieldsFile(Reader var1) throws IOException {
      this.loadSymbols(var1, this.csvFieldsMappings, "fields.csv");
   }

   private void loadSymbols(Reader var1, Map<String, CSVMappings.Symbol> var2, String var3) throws IOException {
      try {
         CSVParser var4 = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(var1);
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            CSVRecord var6 = (CSVRecord)var5.next();
            String var7 = var6.get("searge");
            String var8 = var6.get("name");
            int var9 = Integer.parseInt(var6.get("side"));
            String var10 = var6.get("desc");
            var2.put(var7, new CSVMappings.Symbol(var8, var9, var10));
         }

         System.out.println("   Loaded " + var2.size() + " symbols from " + var3);
      } catch (Throwable var11) {
         var11.printStackTrace();
         throw new IOException("Invalid " + var3 + " file!");
      }
   }

   public void loadParamsFile(Reader var1) throws IOException {
      try {
         CSVParser var2 = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(var1);
         Iterator var3 = var2.iterator();

         while(true) {
            String var6;
            int var7;
            String var8;
            int var9;
            int var10;
            do {
               do {
                  do {
                     if (!var3.hasNext()) {
                        System.out.println("   Loaded " + this.csvParamsMappings.size() + " symbols from params.csv");
                        return;
                     }

                     CSVRecord var4 = (CSVRecord)var3.next();
                     String var5 = var4.get("param");
                     var6 = var4.get("name");
                     var7 = Integer.parseInt(var4.get("side"));
                     this.csvParamsMappings.put(var5, new CSVMappings.Param(var6, var7));
                     var8 = var5.substring(var5.indexOf(95) + 1);
                  } while(var8.startsWith("i"));

                  var9 = var8.indexOf(95);
               } while(var9 == -1);

               var10 = -1;
               String var11 = var8.substring(var9 + 1);
               if (var11.length() >= 2) {
                  try {
                     var10 = Integer.parseInt(var11.substring(0, var11.length() - 1));
                  } catch (NumberFormatException var14) {
                  }
               }
            } while(var10 < 0);

            var8 = "func_" + var8.substring(0, var9);
            CSVMappings.Param[] var12 = (CSVMappings.Param[])this.csvParamsForFunction.get(var8);
            if (var12 == null || var12.length <= var10) {
               CSVMappings.Param[] var13 = new CSVMappings.Param[var10 + 1];
               if (var12 != null) {
                  System.arraycopy(var12, 0, var13, 0, var12.length);
               }

               var12 = var13;
            }

            var12[var10] = new CSVMappings.Param(var6, var7);
            this.csvParamsForFunction.put(var8, var12);
         }
      } catch (Throwable var15) {
         throw new IOException("Invalid params.csv file!");
      }
   }

   public static class Param {
      public final String name;
      public final int mod;

      public Param(String var1, int var2) {
         this.name = var1;
         this.mod = var2;
      }
   }

   public static class Symbol {
      public final String name;
      public final int mod;
      public final String comment;

      public Symbol(String var1, int var2, String var3) {
         this.name = var1;
         this.mod = var2;
         this.comment = var3;
      }
   }
}
