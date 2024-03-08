package net.lax1dude.eaglercraft.v1_8.buildtools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LicensePrompt {
   public static void main(String[] var0) {
      System.out.println();
      display();
   }

   public static void display() {
      System.out.println("WARNING: You must agree to the LICENSE before running this command");
      System.out.println();

      try {
         Throwable var0 = null;
         Object var1 = null;

         try {
            BufferedReader var2 = new BufferedReader(new InputStreamReader(LicensePrompt.class.getResourceAsStream("/lang/LICENSE_console_wrapped.txt"), StandardCharsets.UTF_8));

            String var3;
            try {
               while((var3 = var2.readLine()) != null) {
                  if (var3.equals("<press enter>")) {
                     pressEnter();
                  } else {
                     System.out.println(var3);
                  }
               }
            } finally {
               if (var2 != null) {
                  var2.close();
               }

            }
         } catch (Throwable var11) {
            if (var0 == null) {
               var0 = var11;
            } else if (var0 != var11) {
               var0.addSuppressed(var11);
            }

            throw var0;
         }
      } catch (IOException var12) {
         System.err.println();
         System.err.println("ERROR: could not display license text");
         System.err.println("Please read the \"LICENSE\" file before using this software");
         System.err.println();
         pressEnter();
      }

   }

   private static void pressEnter() {
      System.out.println();
      System.out.println("(press ENTER to continue)");

      try {
         while(System.in.read() != 10) {
         }

      } catch (IOException var1) {
         throw new RuntimeException("Unexpected IOException reading STDIN", var1);
      }
   }
}
