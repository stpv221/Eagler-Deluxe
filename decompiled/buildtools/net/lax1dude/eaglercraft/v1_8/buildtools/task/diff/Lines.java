package net.lax1dude.eaglercraft.v1_8.buildtools.task.diff;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Lines {
   public static final Pattern splitPattern = Pattern.compile("(\\r\\n|\\n|\\r)");

   public static String[] linesArray(String var0) {
      return splitPattern.split(var0);
   }

   public static List<String> linesList(String var0) {
      return Arrays.asList(splitPattern.split(var0));
   }
}
