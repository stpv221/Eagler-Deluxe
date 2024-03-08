package net.lax1dude.eaglercraft.v1_8.buildtools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileReaderUTF extends InputStreamReader {
   public FileReaderUTF(File var1) throws FileNotFoundException {
      super(new FileInputStream(var1), StandardCharsets.UTF_8);
   }
}
