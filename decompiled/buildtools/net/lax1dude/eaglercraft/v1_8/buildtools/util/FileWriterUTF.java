package net.lax1dude.eaglercraft.v1_8.buildtools.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class FileWriterUTF extends OutputStreamWriter {
   public FileWriterUTF(File var1) throws IOException {
      super(new FileOutputStream(var1), StandardCharsets.UTF_8);
   }
}
