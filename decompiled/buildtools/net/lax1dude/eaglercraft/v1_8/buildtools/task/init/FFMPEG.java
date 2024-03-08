package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import org.apache.commons.io.FileUtils;

public class FFMPEG {
   public static final boolean windows = System.getProperty("os.name").toLowerCase().contains("windows");
   public static String foundFFMPEG = null;

   public static void confirmFFMPEG() {
      if (checkFFMPEGOnPath()) {
         foundFFMPEG = "ffmpeg";
      } else {
         File var0;
         if (windows) {
            var0 = new File("mcp918/ffmpeg.exe");
            if (!var0.isFile()) {
               System.out.println();
               System.out.println("ERROR: 'ffmpeg.exe' wasn't found in the 'mcp918' folder!");
               System.out.println();
               System.out.println("Please visit one of the following URLs to download it:");
               System.out.println(" - https://www.gyan.dev/ffmpeg/builds/");
               System.out.println(" - https://github.com/BtbN/FFmpeg-Builds/releases");
               System.out.println();
               System.out.println("Locate 'bin/ffmpeg.exe' in the .zip file you download and");
               System.out.println("place it in the 'mcp918' folder and press enter to continue");
               System.out.println();

               try {
                  while(System.in.read() != 10) {
                     try {
                        Thread.sleep(20L);
                     } catch (InterruptedException var2) {
                     }
                  }
               } catch (IOException var3) {
               }

               confirmFFMPEG();
            } else {
               foundFFMPEG = var0.getAbsolutePath();
            }
         } else {
            while(true) {
               var0 = new File("mcp918/ffmpeg");
               if (var0.isFile() && var0.canExecute()) {
                  foundFFMPEG = var0.getAbsolutePath();
                  return;
               }

               System.out.println();
               System.out.println("ERROR: ffmpeg is not installed on this system!");
               System.out.println();
               System.out.println("Please install it to continue, you can use the package");
               System.out.println("manager on most distros to do this automatically:");
               System.out.println(" - Debian: apt install ffmpeg");
               System.out.println(" - Ubuntu: apt install ffmpeg");
               System.out.println(" - Fedora: dnf install ffmpeg");
               System.out.println(" - Arch: pacman -S ffmpeg");
               System.out.println();
               System.out.println("Alternatively, place the 'ffmpeg' executable in the");
               System.out.println("'mcp918' folder of this repository");
               System.out.println();
               System.out.println("Make sure it has chmod +x");
               System.out.println();
               System.out.println("Press enter to continue once it has installed");
               System.out.println();

               try {
                  while(System.in.read() != 10) {
                     try {
                        Thread.sleep(20L);
                     } catch (InterruptedException var1) {
                     }
                  }
               } catch (IOException var4) {
               }

               if (checkFFMPEGOnPath()) {
                  foundFFMPEG = "ffmpeg";
                  break;
               }
            }
         }

      }
   }

   public static int run(File var0, String... var1) {
      String[] var2 = new String[var1.length + 1];
      System.arraycopy(var1, 0, var2, 1, var1.length);
      if (foundFFMPEG == null) {
         confirmFFMPEG();
      }

      var2[0] = foundFFMPEG;
      ProcessBuilder var3 = new ProcessBuilder(var2);
      var3.directory(var0);
      var3.redirectOutput(Redirect.INHERIT);
      var3.redirectError(Redirect.INHERIT);

      try {
         Process var4 = var3.start();

         while(true) {
            try {
               return var4.waitFor();
            } catch (InterruptedException var5) {
            }
         }
      } catch (IOException var6) {
         System.err.println("Could not start ffmpeg process!");
         var6.printStackTrace();
         return -1;
      }
   }

   public static byte[] encodeOgg(File var0, byte[] var1, int var2, int var3, boolean var4) throws IOException {
      File var5 = new File(var0, "temp.src.ogg");
      FileUtils.writeByteArrayToFile(var5, var1);
      File var6 = new File(var0, "temp.dst.ogg");
      int var7;
      if (var4) {
         var7 = run(var0, "-y", "-v", "error", "-i", "temp.src.ogg", "-c:a", "libvorbis", "-ac", "2", "-apply_phase_inv", "1", "-b:a", var3 + "k", "-ar", "" + var2, "temp.dst.ogg");
      } else {
         var7 = run(var0, "-y", "-v", "error", "-i", "temp.src.ogg", "-c:a", "libvorbis", "-ac", "1", "-apply_phase_inv", "0", "-b:a", var3 + "k", "-ar", "" + var2, "temp.dst.ogg");
      }

      var5.delete();
      if (var7 != 0) {
         throw new IOException("FFMPEG returned error code: " + var7);
      } else {
         byte[] var8 = FileUtils.readFileToByteArray(var6);
         var6.delete();
         return var8;
      }
   }

   public static boolean checkFFMPEGOnPath() {
      ProcessBuilder var0 = new ProcessBuilder(new String[]{"ffmpeg", "-version"});

      Process var1;
      try {
         var1 = var0.start();
      } catch (IOException var3) {
         return false;
      }

      while(true) {
         try {
            int var2 = var1.waitFor();
            return var2 == 0;
         } catch (InterruptedException var4) {
         }
      }
   }
}
