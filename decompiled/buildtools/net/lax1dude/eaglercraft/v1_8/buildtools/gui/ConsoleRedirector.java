package net.lax1dude.eaglercraft.v1_8.buildtools.gui;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ConsoleRedirector extends OutputStream {
   private final OutputStream stdout;
   private final boolean err;

   public ConsoleRedirector(boolean var1) {
      this.stdout = var1 ? System.err : System.out;
      this.err = var1;
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.stdout.write(var1, var2, var3);
      String var4 = new String(var1, var2, var3, StandardCharsets.US_ASCII);
      if (this.err) {
         CompileLatestClientGUI.frame.logError(var4);
      } else {
         CompileLatestClientGUI.frame.logInfo(var4);
      }

   }

   public void write(int var1) throws IOException {
      this.stdout.write(var1);
      this.write0(var1);
   }

   private void write0(int var1) throws IOException {
      char var2 = (char)var1;
      if (var2 != '\r') {
         if (this.err && var2 != '\n') {
            CompileLatestClientGUI.frame.logError(new String(new char[]{var2}));
         } else {
            CompileLatestClientGUI.frame.logInfo(new String(new char[]{var2}));
         }
      }

   }
}
