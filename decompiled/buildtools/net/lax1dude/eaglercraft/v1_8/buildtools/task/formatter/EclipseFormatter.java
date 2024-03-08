package net.lax1dude.eaglercraft.v1_8.buildtools.task.formatter;

import org.eclipse.jdt.internal.formatter.DefaultCodeFormatter;
import org.eclipse.jdt.internal.formatter.DefaultCodeFormatterOptions;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

public class EclipseFormatter {
   private static final DefaultCodeFormatter formatter = new DefaultCodeFormatter(DefaultCodeFormatterOptions.getEclipseDefaultSettings());

   public static String processSource(String var0, String var1) {
      try {
         Document var2 = new Document();
         var2.set(var0);
         TextEdit var3 = formatter.format(4104, var0, 0, var0.length(), 0, var1);
         var3.apply(var2);
         return var2.get();
      } catch (Throwable var4) {
         System.err.println("Code formatting failed!");
         var4.printStackTrace();
         return null;
      }
   }
}
