package net.lax1dude.eaglercraft.v1_8.buildtools.task.diff;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.IOUtils;

public class JARMemoryCache {
   public static Map<String, byte[]> loadJAR(InputStream var0) throws IOException {
      HashMap var1 = new HashMap();
      ZipInputStream var2 = new ZipInputStream(var0);

      ZipEntry var3;
      while((var3 = var2.getNextEntry()) != null) {
         if (!var3.isDirectory()) {
            String var4 = var3.getName();
            if (var4.startsWith("/")) {
               var4 = var4.substring(1);
            }

            if (!var4.startsWith("META-INF")) {
               byte[] var5 = IOUtils.toByteArray(var2);
               var1.put(var4, var5);
            }
         }
      }

      return var1;
   }
}
