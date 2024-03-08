package net.lax1dude.eaglercraft.v1_8.buildtools.task.teavm;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import net.lax1dude.eaglercraft.v1_8.buildtools.gui.TeaVMBinaries;

public class TeaVMBridge {
   private static URLClassLoader classLoader = null;

   public static boolean compileTeaVM(Map<String, Object> var0) throws TeaVMBridge.TeaVMClassLoadException, TeaVMBridge.TeaVMRuntimeException {
      File[] var1 = TeaVMBinaries.getTeaVMCompilerClasspath();
      URL[] var2 = new URL[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         try {
            var2[var3] = var1[var3].toURI().toURL();
         } catch (MalformedURLException var10) {
            throw new TeaVMBridge.TeaVMClassLoadException("Could not resolve URL for: " + var1[var3].getAbsolutePath(), var10);
         }
      }

      Method var14 = null;

      try {
         if (classLoader == null) {
            classLoader = new URLClassLoader(var2, ClassLoader.getSystemClassLoader());
         }

         Class var4 = classLoader.loadClass("net.lax1dude.eaglercraft.v1_8.buildtools.task.teavm.TeaVMBridgeImpl");
         Method[] var16 = var4.getDeclaredMethods();

         for(int var6 = 0; var6 < var16.length; ++var6) {
            Method var7 = var16[var6];
            if (var7.getName().equals("compileTeaVM")) {
               var14 = var7;
            }
         }

         if (var14 == null) {
            throw new NoSuchMethodException("compileTeaVM");
         }
      } catch (NoSuchMethodException | ClassNotFoundException | TeaVMBridge.TeaVMClassLoadException var11) {
         throw new TeaVMBridge.TeaVMClassLoadException("Could not link TeaVM compiler!", var11);
      } catch (RuntimeException var12) {
         String var5 = var12.getMessage();
         if (var5.startsWith("[TeaVMBridge]")) {
            throw new TeaVMBridge.TeaVMRuntimeException(var5.substring(13).trim(), var12.getCause());
         }

         throw new TeaVMBridge.TeaVMRuntimeException("Uncaught exception was thrown!", var12);
      } catch (Throwable var13) {
         throw new TeaVMBridge.TeaVMRuntimeException("Uncaught exception was thrown!", var13);
      }

      try {
         Object var15 = var14.invoke((Object)null, var0);
         return var15 != null && var15 instanceof Boolean && (Boolean)var15;
      } catch (InvocationTargetException var8) {
         throw new TeaVMBridge.TeaVMRuntimeException("Uncaught exception was thrown!", var8.getCause());
      } catch (Throwable var9) {
         throw new TeaVMBridge.TeaVMRuntimeException("Failed to invoke 'compileTeaVM'!", var9);
      }
   }

   public static void free() {
      if (classLoader != null) {
         try {
            classLoader.close();
            classLoader = null;
         } catch (IOException var1) {
            System.err.println("Memory leak, failed to release TeaVM JAR ClassLoader!");
            var1.printStackTrace();
         }
      }

   }

   public static class TeaVMClassLoadException extends RuntimeException {
      public TeaVMClassLoadException(String var1, Throwable var2) {
         super(var1, var2);
      }

      public TeaVMClassLoadException(String var1) {
         super(var1);
      }
   }

   public static class TeaVMRuntimeException extends RuntimeException {
      public TeaVMRuntimeException(String var1, Throwable var2) {
         super(var1, var2);
      }

      public TeaVMRuntimeException(String var1) {
         super(var1);
      }
   }
}
