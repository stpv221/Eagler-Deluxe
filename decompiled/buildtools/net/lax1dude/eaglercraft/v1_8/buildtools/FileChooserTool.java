package net.lax1dude.eaglercraft.v1_8.buildtools;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class FileChooserTool {
   public static final JFileChooser fc = new JFileChooser();

   public static File load(boolean var0) {
      fc.setFileSelectionMode(var0 ? 1 : 0);
      fc.setMultiSelectionEnabled(false);
      fc.setFileHidingEnabled(false);
      fc.setDialogTitle("Eaglercraft Buildtools");
      JFrame var1 = new JFrame();
      var1.setBounds(0, 0, 50, 50);
      var1.setDefaultCloseOperation(0);
      var1.setAlwaysOnTop(true);
      var1.setTitle("File Chooser");
      var1.setLocationRelativeTo((Component)null);
      var1.setVisible(true);
      if (fc.showOpenDialog(var1) == 0) {
         var1.dispose();
         return fc.getSelectedFile();
      } else {
         var1.dispose();
         return null;
      }
   }
}
