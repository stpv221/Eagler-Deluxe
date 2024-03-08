package net.lax1dude.eaglercraft.v1_8.buildtools.gui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class MakeOfflineDownload {
   private static File currentJarFile = null;
   private static URLClassLoader classLoader = null;
   private static Method mainMethod = null;

   public static void compilerMain(File var0, String[] var1) throws InvocationTargetException {
      if (currentJarFile != null && !currentJarFile.equals(var0)) {
         throw new IllegalArgumentException("Cannot load two different MakeOfflineDownload versions into the same runtime");
      } else {
         if (mainMethod == null) {
            currentJarFile = var0;

            try {
               if (classLoader == null) {
                  classLoader = new URLClassLoader(new URL[]{var0.toURI().toURL()}, ClassLoader.getSystemClassLoader());
               }

               Class var2 = classLoader.loadClass("net.lax1dude.eaglercraft.v1_8.buildtools.workspace.MakeOfflineDownload");
               mainMethod = var2.getDeclaredMethod("main", String[].class);
            } catch (SecurityException | MalformedURLException var4) {
               throw new IllegalArgumentException("Illegal MakeOfflineDownload JAR path!", var4);
            } catch (NoSuchMethodException | ClassNotFoundException var5) {
               throw new IllegalArgumentException("MakeOfflineDownload JAR does not contain main class: 'net.lax1dude.eaglercraft.v1_8.buildtools.workspace.MakeOfflineDownload'", var5);
            }
         }

         try {
            mainMethod.invoke((Object)null, var1);
         } catch (IllegalArgumentException | IllegalAccessException var3) {
            throw new IllegalArgumentException("MakeOfflineDownload JAR does not contain valid 'main' method", var3);
         }
      }
   }

   public static void free() {
      if (classLoader != null) {
         try {
            classLoader.close();
            classLoader = null;
         } catch (IOException var1) {
            System.err.println("Memory leak, failed to release MakeOfflineDownload ClassLoader!");
            var1.printStackTrace();
         }
      }

   }
}
