package net.lax1dude.eaglercraft.v1_8.buildtools.gui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class TeaVMBinaries {
   public static final TeaVMBinaries.MavenJAREntry teavmCore = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-core/0.9.2/teavm-core-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmCli = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-cli/0.9.2/teavm-cli-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmTooling = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-tooling/0.9.2/teavm-tooling-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmPlatform = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-platform/0.9.2/teavm-platform-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmClasslib = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-classlib/0.9.2/teavm-classlib-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmInterop = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-interop/0.9.2/teavm-interop-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmJSO = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-jso/0.9.2/teavm-jso-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmJSOApis = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-jso-apis/0.9.2/teavm-jso-apis-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmJSOImpl = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-jso-impl/0.9.2/teavm-jso-impl-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmRelocatedLibsASM = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-relocated-libs-asm/0.9.2/teavm-relocated-libs-asm-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmRelocatedLibsASMAnalysis = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-relocated-libs-asm-analysis/0.9.2/teavm-relocated-libs-asm-analysis-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmRelocatedLibsASMCommons = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-relocated-libs-asm-commons/0.9.2/teavm-relocated-libs-asm-commons-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmRelocatedLibsASMTree = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-relocated-libs-asm-tree/0.9.2/teavm-relocated-libs-asm-tree-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmRelocatedLibsASMUtil = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-relocated-libs-asm-util/0.9.2/teavm-relocated-libs-asm-util-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmRelocatedLibsHPPC = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-relocated-libs-hppc/0.9.2/teavm-relocated-libs-hppc-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmRelocatedLibsRhino = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-relocated-libs-rhino/0.9.2/teavm-relocated-libs-rhino-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry asm = new TeaVMBinaries.MavenJAREntry("org/ow2/asm/asm/9.5/asm-9.5.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry asmAnalysis = new TeaVMBinaries.MavenJAREntry("org/ow2/asm/asm-analysis/9.5/asm-analysis-9.5.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry asmCommons = new TeaVMBinaries.MavenJAREntry("org/ow2/asm/asm-commons/9.5/asm-commons-9.5.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry asmTree = new TeaVMBinaries.MavenJAREntry("org/ow2/asm/asm-tree/9.5/asm-tree-9.5.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry asmUtil = new TeaVMBinaries.MavenJAREntry("org/ow2/asm/asm-util/9.5/asm-util-9.5.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry hppc = new TeaVMBinaries.MavenJAREntry("com/carrotsearch/hppc/0.9.1/hppc-0.9.1.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry rhino = new TeaVMBinaries.MavenJAREntry("org/mozilla/rhino/1.7.14/rhino-1.7.14.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmMetaprogrammingAPI = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-metaprogramming-api/0.9.2/teavm-metaprogramming-api-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmMetaprogrammingImpl = new TeaVMBinaries.MavenJAREntry("org/teavm/teavm-metaprogramming-impl/0.9.2/teavm-metaprogramming-impl-0.9.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmJodaTime = new TeaVMBinaries.MavenJAREntry("joda-time/joda-time/2.12.2/joda-time-2.12.2.jar", (TeaVMBinaries.MavenJAREntry)null);
   public static final TeaVMBinaries.MavenJAREntry teavmJZLIB = new TeaVMBinaries.MavenJAREntry("com/jcraft/jzlib/1.1.3/jzlib-1.1.3.jar", (TeaVMBinaries.MavenJAREntry)null);
   private static final TeaVMBinaries.MavenJAREntry[] jarsList;
   public static File teavmBridge;

   static {
      jarsList = new TeaVMBinaries.MavenJAREntry[]{teavmCore, teavmCli, teavmTooling, teavmPlatform, teavmClasslib, teavmInterop, teavmJSO, teavmJSOApis, teavmJSOImpl, teavmRelocatedLibsASM, teavmRelocatedLibsASMAnalysis, teavmRelocatedLibsASMCommons, teavmRelocatedLibsASMTree, teavmRelocatedLibsASMUtil, teavmRelocatedLibsHPPC, teavmRelocatedLibsRhino, asm, asmAnalysis, asmCommons, asmTree, asmUtil, hppc, rhino, teavmMetaprogrammingAPI, teavmMetaprogrammingImpl, teavmJodaTime, teavmJZLIB};
      teavmBridge = null;
   }

   public static void downloadFromMaven(String var0, File var1) throws TeaVMBinaries.MissingJARsException {
      int var2;
      for(var2 = 0; var2 < jarsList.length; ++var2) {
         jarsList[var2].file = null;
      }

      if (var0.lastIndexOf(47) != var0.length() - 1) {
         var0 = var0 + "/";
      }

      for(var2 = 0; var2 < jarsList.length; ++var2) {
         TeaVMBinaries.MavenJAREntry var3 = jarsList[var2];
         String var4 = var0 + var3.maven;

         try {
            File var5 = new File(var1, var3.jar);
            copyURLToFileCheck404(var4, var5);
            var3.file = var5;
         } catch (IOException var6) {
            System.err.println("Could not download JAR: " + var4);
            var6.printStackTrace();
            throw new TeaVMBinaries.MissingJARsException("The following JAR file could not be downloaded: " + var4, Arrays.asList(var4));
         }
      }

   }

   public static void loadFromDirectory(File var0) throws TeaVMBinaries.MissingJARsException {
      for(int var1 = 0; var1 < jarsList.length; ++var1) {
         jarsList[var1].file = null;
      }

      discoverJars(var0);
      ArrayList var4 = new ArrayList();

      for(int var2 = 0; var2 < jarsList.length; ++var2) {
         TeaVMBinaries.MavenJAREntry var3 = jarsList[var2];
         if (var3.file == null) {
            var4.add(var3.jar);
         }
      }

      if (var4.size() > 0) {
         throw new TeaVMBinaries.MissingJARsException(var4);
      }
   }

   private static void discoverJars(File var0) {
      File[] var1 = var0.listFiles();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         File var3 = var1[var2];
         if (var3.isDirectory()) {
            discoverJars(var3);
         } else {
            String var4 = var3.getName();

            for(int var5 = 0; var5 < jarsList.length; ++var5) {
               if (var4.equals(jarsList[var5].jar)) {
                  jarsList[var5].file = var3;
               }
            }
         }
      }

   }

   private static void copyURLToFileCheck404(String var0, File var1) throws IOException {
      System.out.println("downloading: " + var0);

      URL var2;
      try {
         var2 = new URL(var0);
      } catch (MalformedURLException var21) {
         throw new IOException("Invalid URL: " + var0, var21);
      }

      HttpURLConnection var3 = (HttpURLConnection)var2.openConnection();
      var3.setConnectTimeout(5000);
      var3.setReadTimeout(5000);
      int var4 = var3.getResponseCode();
      if (var4 != 200) {
         var3.disconnect();
         throw new IOException("Recieved response code: " + var4);
      } else {
         try {
            Throwable var5 = null;
            Object var6 = null;

            try {
               InputStream var7 = var3.getInputStream();

               try {
                  FileUtils.copyInputStreamToFile(var7, var1);
               } finally {
                  if (var7 != null) {
                     var7.close();
                  }

               }
            } catch (Throwable var23) {
               if (var5 == null) {
                  var5 = var23;
               } else if (var5 != var23) {
                  var5.addSuppressed(var23);
               }

               throw var5;
            }
         } finally {
            var3.disconnect();
         }

      }
   }

   public static boolean tryLoadTeaVMBridge() {
      String var0 = System.getProperty("eaglercraft.TeaVMBridge");
      File var1;
      if (var0 != null) {
         var1 = new File(var0);
      } else {
         try {
            var1 = new File((new File(URLDecoder.decode(TeaVMBinaries.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(), "UTF-8"))).getParent(), "TeaVMBridge.jar");
         } catch (UnsupportedEncodingException | URISyntaxException var3) {
            System.err.println("Failed to locate TeaVMBridge.jar relative to BuildTools jar!");
            var3.printStackTrace();
            return false;
         }
      }

      if (var1.exists()) {
         teavmBridge = var1;
         return true;
      } else {
         System.err.println("File does not exist: " + var1.getAbsolutePath());
         return false;
      }
   }

   public static File[] getTeaVMCompilerClasspath() {
      return new File[]{teavmCore.file, teavmCli.file, teavmTooling.file, teavmInterop.file, teavmRelocatedLibsASM.file, teavmRelocatedLibsASMAnalysis.file, teavmRelocatedLibsASMCommons.file, teavmRelocatedLibsASMTree.file, teavmRelocatedLibsASMUtil.file, teavmRelocatedLibsHPPC.file, teavmRelocatedLibsRhino.file, asm.file, asmAnalysis.file, asmCommons.file, asmTree.file, asmUtil.file, hppc.file, rhino.file, teavmMetaprogrammingAPI.file, teavmBridge};
   }

   public static String[] getTeaVMRuntimeClasspath() {
      return new String[]{teavmJodaTime.file.getAbsolutePath(), teavmJZLIB.file.getAbsolutePath(), teavmClasslib.file.getAbsolutePath(), teavmInterop.file.getAbsolutePath(), teavmJSO.file.getAbsolutePath(), teavmJSOApis.file.getAbsolutePath(), teavmJSOImpl.file.getAbsolutePath(), teavmMetaprogrammingAPI.file.getAbsolutePath(), teavmMetaprogrammingImpl.file.getAbsolutePath(), teavmPlatform.file.getAbsolutePath()};
   }

   public static class MavenJAREntry {
      public final String jar;
      public final String maven;
      public File file;

      private MavenJAREntry(String var1) {
         this.jar = var1.substring(var1.lastIndexOf(47) + 1);
         this.maven = var1;
      }

      // $FF: synthetic method
      MavenJAREntry(String var1, TeaVMBinaries.MavenJAREntry var2) {
         this(var1);
      }
   }

   public static class MissingJARsException extends RuntimeException {
      public final List<String> jars;

      public MissingJARsException(String var1, List<String> var2) {
         super(var1);
         this.jars = var2;
      }

      public MissingJARsException(List<String> var1) {
         this("The following JAR files were not found: " + String.join(", ", var1), var1);
      }
   }
}
