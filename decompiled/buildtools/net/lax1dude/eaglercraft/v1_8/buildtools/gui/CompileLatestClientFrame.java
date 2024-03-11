package net.lax1dude.eaglercraft.v1_8.buildtools.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.FFMPEG;

public class CompileLatestClientFrame {
   public JFrame frmCompileLatestClient;
   public JTextField textField_ModCoderPack;
   public JTextField textField_JarFilePath;
   public JTextField textField_AssetsIndexJSON;
   public JTextField textField_MavenRepoCustomURL;
   public JTextField textField_MavenRepoLocal;
   public JTextField textField_OutputDirectory;
   public JTextField textField_RepositoryPath;
   public JRadioButton rdbtnMavenRepoLocal;
   public JRadioButton rdbtnMavenRepoCustom;
   public JRadioButton rdbtnMavenRepoSonatype;
   public JRadioButton rdbtnMavenRepoBintray;
   public JRadioButton rdbtnMavenRepoCentral;
   public JTextPane txtpnLogOutput;
   public JLabel lblProgressState;
   public JButton btnBack;
   public JButton btnNext;
   public JPanel pagesRoot;
   public JScrollPane scrollPane;
   public JTextPane txtpnfuckOffreview;
   public JCheckBox chckbxOutputOfflineDownload;
   public JCheckBox chckbxKeepTemporaryFiles;
   public JTextField textField_pathToFFmpeg;
   public JButton btnBrowsePathToFFmpeg;
   public JCheckBox chckbxUsePathFFmpeg;
   public JCheckBox chckbxAgreeLicense;
   public JTextArea txtrLicenseText;
   public JScrollPane scrollPane_LicenseText;
   public int page = 0;
   public boolean compiling;
   public boolean finished;
   private StringBuilder logAccumPrev = new StringBuilder();
   private StringBuilder logAccum = new StringBuilder();
   private Element logAccumBody = null;
   private volatile boolean logDirty = false;
   private volatile boolean isError = false;
   private boolean knowFoundFFmpeg = false;
   private boolean foundFFmpeg = false;
   private boolean hasAskedFFmpegOnPath = false;

   public CompileLatestClientFrame() {
      this.initialize();
   }

   public void setPage(int var1) {
      if (var1 < 0) {
         var1 = 0;
      }

      if (var1 > 10) {
         var1 = 10;
      }

      if (this.page != var1) {
         if (this.page < var1) {
            String var2;
            File var3;
            switch(this.page) {
            case 1:
               if (!this.chckbxAgreeLicense.isSelected()) {
                  JOptionPane.showMessageDialog(this.frmCompileLatestClient, "You must agree to the license to continue!", "Permission Denied", 0);
                  return;
               }
               break;
            case 2:
               var2 = this.textField_RepositoryPath.getText().trim();
               if (var2.length() == 0) {
                  JOptionPane.showMessageDialog(this.frmCompileLatestClient, "Please select a folder!", "Invalid Path", 0);
                  return;
               }

               if (!(new File(var2)).isDirectory()) {
                  JOptionPane.showMessageDialog(this.frmCompileLatestClient, "The path \"" + var2 + "\" is not a folder!", "Invalid Path", 0);
                  return;
               }
               break;
            case 3:
               var2 = this.textField_ModCoderPack.getText().trim();
               if (var2.length() == 0) {
                  JOptionPane.showMessageDialog(this.frmCompileLatestClient, "Please select a file!", "Invalid Path", 0);
                  return;
               }

               if (!(new File(var2)).isFile()) {
                  JOptionPane.showMessageDialog(this.frmCompileLatestClient, "The path \"" + var2 + "\" is not a file!", "Invalid Path", 0);
                  return;
               }
               break;
            case 4:
               var2 = this.textField_JarFilePath.getText().trim();
               if (var2.length() == 0) {
                  JOptionPane.showMessageDialog(this.frmCompileLatestClient, "Please select a file!", "Invalid Path", 0);
                  return;
               }

               if (!(new File(var2)).isFile()) {
                  JOptionPane.showMessageDialog(this.frmCompileLatestClient, "The path \"" + var2 + "\" is not a file!", "Invalid Path", 0);
                  return;
               }
               break;
            case 5:
               var2 = this.textField_AssetsIndexJSON.getText().trim();
               if (var2.length() == 0) {
                  JOptionPane.showMessageDialog(this.frmCompileLatestClient, "Please select a file!", "Invalid Path", 0);
                  return;
               }

               if (!(new File(var2)).isFile()) {
                  JOptionPane.showMessageDialog(this.frmCompileLatestClient, "The path \"" + var2 + "\" is not a file!", "Invalid Path", 0);
                  return;
               }
               break;
            case 6:
               if (this.rdbtnMavenRepoLocal.isSelected()) {
                  var2 = this.textField_MavenRepoLocal.getText().trim();
                  if (var2.length() == 0) {
                     JOptionPane.showMessageDialog(this.frmCompileLatestClient, "Please select a folder!", "Invalid Path", 0);
                     return;
                  }

                  var3 = new File(var2);
                  if (!var3.isDirectory()) {
                     JOptionPane.showMessageDialog(this.frmCompileLatestClient, "The path \"" + var2 + "\" is not a folder!", "Invalid Path", 0);
                     return;
                  }

                  try {
                     TeaVMBinaries.loadFromDirectory(var3);
                  } catch (TeaVMBinaries.MissingJARsException var6) {
                     JOptionPane.showMessageDialog(this.frmCompileLatestClient, "The following JARs are missing:\n - " + String.join("\n - ", var6.jars), "Invalid Path", 0);
                     return;
                  }
               } else if (this.rdbtnMavenRepoCustom.isSelected()) {
                  var2 = this.textField_MavenRepoCustomURL.getText().trim();
                  if (var2.length() == 0) {
                     JOptionPane.showMessageDialog(this.frmCompileLatestClient, "Please enter a URL!", "Invalid URL", 0);
                     return;
                  }

                  try {
                     new URL(var2);
                  } catch (MalformedURLException var5) {
                     JOptionPane.showMessageDialog(this.frmCompileLatestClient, "MalformedURLException: " + var5.getMessage(), "Invalid URL", 0);
                     return;
                  }
               }
               break;
            case 7:
               if (!this.chckbxUsePathFFmpeg.isSelected()) {
                  var2 = this.textField_pathToFFmpeg.getText().trim();
                  if (var2.length() == 0) {
                     JOptionPane.showMessageDialog(this.frmCompileLatestClient, "Please select a file!", "Invalid Path", 0);
                     return;
                  }

                  if (!(new File(var2)).isFile()) {
                     JOptionPane.showMessageDialog(this.frmCompileLatestClient, "The path \"" + var2 + "\" is not a file!", "Invalid Path", 0);
                     return;
                  }
               } else if (!this.isFFmpegOnPath()) {
                  JOptionPane.showMessageDialog(this.frmCompileLatestClient, "FFmpeg is not on your system's PATH!", "Error", 0);
                  this.chckbxUsePathFFmpeg.setSelected(false);
                  this.chckbxUsePathFFmpeg.setEnabled(false);
                  this.textField_pathToFFmpeg.setEnabled(true);
                  this.btnBrowsePathToFFmpeg.setEnabled(true);
                  return;
               }
               break;
            case 8:
               var2 = this.textField_OutputDirectory.getText().trim();
               if (var2.length() == 0) {
                  JOptionPane.showMessageDialog(this.frmCompileLatestClient, "Please select a folder!", "Invalid Path", 0);
                  return;
               }

               var3 = new File(var2);
               if (!var3.isDirectory()) {
                  JOptionPane.showMessageDialog(this.frmCompileLatestClient, "The path \"" + var2 + "\" is not a folder!", "Invalid Path", 0);
                  return;
               }

               if (var3.list().length > 0 && JOptionPane.showConfirmDialog(this.frmCompileLatestClient, "The directory \"" + var2 + "\" is not empty, would you like to continue?\nThe existing files will be deleted.", "Confirm", 0) != 0) {
                  return;
               }
            }
         }

         CardLayout var8 = (CardLayout)this.pagesRoot.getLayout();
         switch(var1) {
         case 0:
         default:
            var8.show(this.pagesRoot, "pageHome");
            break;
         case 1:
            var8.show(this.pagesRoot, "pageLicense");
            break;
         case 2:
            var8.show(this.pagesRoot, "pageBrowseRepositoryPath");
            break;
         case 3:
            var8.show(this.pagesRoot, "pageModCoderPack");
            break;
         case 4:
            var8.show(this.pagesRoot, "pageBrowseJarFile");
            break;
         case 5:
            var8.show(this.pagesRoot, "pageBrowseAssetsIndexJSON");
            break;
         case 6:
            var8.show(this.pagesRoot, "pageMavenRepo");
            break;
         case 7:
            var8.show(this.pagesRoot, "pageFFmpeg");
            if (this.isFFmpegOnPath()) {
               this.chckbxUsePathFFmpeg.setEnabled(true);
               if (!this.hasAskedFFmpegOnPath) {
                  this.hasAskedFFmpegOnPath = true;
                  EventQueue.invokeLater(new Runnable() {
                     public void run() {
                        if (JOptionPane.showConfirmDialog(CompileLatestClientFrame.this.frmCompileLatestClient, "FFmpeg was found on your system's PATH!\nwould you like to select it automatically?", "Confirmation", 0) == 0) {
                           CompileLatestClientFrame.this.chckbxUsePathFFmpeg.setSelected(true);
                           CompileLatestClientFrame.this.textField_pathToFFmpeg.setEnabled(false);
                           CompileLatestClientFrame.this.btnBrowsePathToFFmpeg.setEnabled(false);
                           CompileLatestClientFrame.this.setPage(CompileLatestClientFrame.this.page + 1);
                        }

                     }
                  });
               }
            } else {
               this.chckbxUsePathFFmpeg.setEnabled(false);
            }
            break;
         case 8:
            var8.show(this.pagesRoot, "pageOutputDirectory");
            break;
         case 9:
            this.txtpnfuckOffreview.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;margin:7px 0px;\">Are these correct?</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; Repository Path:</span> " + htmlentities((new File(this.textField_RepositoryPath.getText())).getAbsolutePath()) + "</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\">" + "<span style=\"font-weight:bold;\">&emsp;&bull; Mod Coder Pack:</span> " + htmlentities((new File(this.textField_ModCoderPack.getText())).getAbsolutePath()) + "</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\">" + "<span style=\"font-weight:bold;\">&emsp;&bull; 1.8.8.jar path:</span> " + htmlentities((new File(this.textField_JarFilePath.getText())).getAbsolutePath()) + "</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\">" + "<span style=\"font-weight:bold;\">&emsp;&bull; 1.8.json path:</span> " + htmlentities((new File(this.textField_AssetsIndexJSON.getText())).getAbsolutePath()) + "</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\">" + "<span style=\"font-weight:bold;\">&emsp;&bull; TeaVM Maven:</span> " + htmlentities(this.rdbtnMavenRepoLocal.isSelected() ? (new File(this.textField_MavenRepoLocal.getText())).getAbsolutePath() : this.getRepositoryURL()) + "</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\">" + "<span style=\"font-weight:bold;\">&emsp;&bull; FFmpeg:</span> " + (this.isFFmpegOnPath() ? "<i>(Already Installed)</i>" : htmlentities((new File(this.textField_pathToFFmpeg.getText())).getAbsolutePath())) + "</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\">" + "<span style=\"font-weight:bold;\">&emsp;&bull; Output Directory:</span> " + htmlentities((new File(this.textField_OutputDirectory.getText())).getAbsolutePath()) + "</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\">" + "<span style=\"font-weight:bold;\">&emsp;&bull; Make Offline Download:</span> " + (this.chckbxOutputOfflineDownload.isSelected() ? "Yes" : "No") + "</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\">" + "<span style=\"font-weight:bold;\">&emsp;&bull; Keep Temporary Files:</span> " + (this.chckbxKeepTemporaryFiles.isSelected() ? "Yes" : "No") + "</p>\r\n" + "<p style=\"font-size:13px;margin-top:6px;\">&nbsp;Press the \"Compile >>\" button to confirm</p>\r\n</body>\r\n</html>");
            var8.show(this.pagesRoot, "pageConfirmSettings");
            break;
         case 10:
            var8.show(this.pagesRoot, "pageLogOutput");
            this.lblProgressState.setText("Compiling, Please Wait...");
            this.compiling = true;
            Point var7 = this.frmCompileLatestClient.getLocation();
            this.frmCompileLatestClient.setLocation(var7.x - 150, var7.y - 150);
            Dimension var4 = this.frmCompileLatestClient.getSize();
            this.frmCompileLatestClient.setSize(var4.width + 300, var4.height + 300);
            this.frmCompileLatestClient.setResizable(true);
            (new Thread(new Runnable() {
               public void run() {
                  CompileLatestClientGUI.runCompiler();
               }
            }, "Compiler Thread")).start();
         }

         this.page = var1;
         this.btnBack.setEnabled(var1 > 0 && var1 != 10);
         this.btnNext.setEnabled(var1 != 10 && (var1 != 1 || this.chckbxAgreeLicense.isSelected()));
         this.btnNext.setText(var1 != 10 ? (var1 != 9 ? "Next >>" : "Compile >>") : "Finish");
      }

   }

   public static File getMinecraftDir() {
      String var0 = System.getProperty("os.name").toLowerCase();
      if (var0.contains("win")) {
         String var1 = System.getenv("APPDATA");
         if (var1 == null) {
            var1 = System.getProperty("user.home", ".");
         }

         return new File(var1, ".minecraft");
      } else {
         return !var0.contains("osx") && !var0.contains("macos") ? new File(System.getProperty("user.home", "."), ".minecraft") : new File(System.getProperty("user.home", "."), "Library/Application Support/.minecraft");
      }
   }

   private void initialize() {
      this.frmCompileLatestClient = new JFrame();
      this.frmCompileLatestClient.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent var1) {
            if (CompileLatestClientFrame.this.compiling && !CompileLatestClientFrame.this.finished) {
               if (JOptionPane.showConfirmDialog(CompileLatestClientFrame.this.frmCompileLatestClient, "Compilier is still running, do you really want to exit?", "Confirmation", 0) == 0) {
                  System.exit(0);
               }
            } else {
               System.exit(0);
            }

         }
      });
      this.frmCompileLatestClient.setIconImage(Toolkit.getDefaultToolkit().getImage(CompileLatestClientFrame.class.getResource("/icon/icon32.png")));
      this.frmCompileLatestClient.setTitle("Compile Latest Client");
      this.frmCompileLatestClient.setResizable(false);
      this.frmCompileLatestClient.getContentPane().setBackground(Color.WHITE);
      this.frmCompileLatestClient.getContentPane().setLayout(new BorderLayout(0, 0));
      JPanel var1 = new JPanel();
      var1.setBackground(Color.WHITE);
      this.frmCompileLatestClient.getContentPane().add(var1, "South");
      var1.setLayout(new BorderLayout(0, 0));
      JPanel var2 = new JPanel();
      var2.setBackground(Color.WHITE);
      FlowLayout var3 = (FlowLayout)var2.getLayout();
      var3.setVgap(10);
      var3.setHgap(10);
      var3.setAlignment(0);
      var1.add(var2, "East");
      this.btnBack = new JButton("<< Back");
      this.btnBack.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            CompileLatestClientFrame.this.setPage(CompileLatestClientFrame.this.page - 1);
         }
      });
      var2.add(this.btnBack);
      this.btnBack.setEnabled(false);
      this.btnNext = new JButton("Next >>");
      this.btnNext.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (CompileLatestClientFrame.this.finished) {
               System.exit(0);
            } else {
               CompileLatestClientFrame.this.setPage(CompileLatestClientFrame.this.page + 1);
            }

         }
      });
      var2.add(this.btnNext);
      this.lblProgressState = new JLabel("");
      this.lblProgressState.setFont(new Font("Dialog", 1, 16));
      this.lblProgressState.setBorder(new EmptyBorder(0, 10, 0, 10));
      var1.add(this.lblProgressState, "Center");
      JPanel var4 = new JPanel();
      var4.setBackground(Color.WHITE);
      this.frmCompileLatestClient.getContentPane().add(var4, "North");
      var4.setLayout(new BorderLayout(0, 0));
      JPanel var5 = new JPanel();
      var5.setBackground(Color.WHITE);
      var4.add(var5, "Center");
      var5.setLayout(new BorderLayout(0, 0));
      JLabel var6 = new JLabel("EaglercraftX 1.8 Client Compiler");
      var6.setVerticalAlignment(3);
      var6.setPreferredSize(new Dimension(152, 24));
      var6.setFont(new Font("Dialog", 1, 14));
      var5.add(var6, "North");
      JLabel var7 = new JLabel("Copyright (c) 2022-2024 lax1dude");
      var7.setVerticalAlignment(1);
      var7.setPreferredSize(new Dimension(27, 24));
      var7.setFont(new Font("Dialog", 0, 14));
      var5.add(var7, "South");
      JLabel var8 = new JLabel("");
      var8.setBorder(new EmptyBorder(8, 8, 8, 16));
      var8.setIcon(new ImageIcon(CompileLatestClientFrame.class.getResource("/icon/eagler.png")));
      var4.add(var8, "West");
      JPanel var9 = new JPanel();
      var9.setPreferredSize(new Dimension(10, 1));
      var9.setBackground(Color.DARK_GRAY);
      var4.add(var9, "North");
      var9.setLayout((LayoutManager)null);
      JPanel var10 = new JPanel();
      var10.setBackground(Color.DARK_GRAY);
      var10.setPreferredSize(new Dimension(10, 1));
      var4.add(var10, "South");
      var10.setLayout((LayoutManager)null);
      JPanel var11 = new JPanel();
      var11.setBackground(Color.WHITE);
      this.frmCompileLatestClient.getContentPane().add(var11, "Center");
      var11.setLayout(new BorderLayout(0, 0));
      this.pagesRoot = new JPanel();
      this.pagesRoot.setBackground(Color.WHITE);
      var11.add(this.pagesRoot, "Center");
      this.pagesRoot.setLayout(new CardLayout(0, 0));
      JPanel var12 = new JPanel();
      var12.setBackground(Color.WHITE);
      this.pagesRoot.add(var12, "pageHome");
      var12.setLayout(new BorderLayout(0, 0));
      JTextPane var13 = new JTextPane();
      var13.setEditable(false);
      var13.setMargin(new Insets(10, 10, 10, 10));
      var13.setContentType("text/html");
      var13.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;\">Welcome to the EaglercraftX 1.8 Client Compiler</p>\r\n<p style=\"font-size:11px;\">This tool will allow you to automatically compile the latest version of the EaglercraftX 1.8 client using the files in this repository.</p>\r\n<p style=\"font-size:11px;\">You are required to download several required files manually in order to better respect the Microsoft/Mojang TOS. The links to these files will be provided.</p>\r\n<p style=\"font-size:11px;\">To view or modify portions of the EaglercraftX 1.8 source code directly, please use the other batch files to generate a gradle project instead of compiling the javascript files directly</p>\r\n<p style=\"font-size:11px;\">If you are from Microsoft/Mojang or the developer of MCP trying to get dirt on me, please just let me live my life, the repository does not contain your intellectual property. Using this code to play your game for free is not the default behavior of the gateway plugin or this compiler utility and is not encouraged by the documentation</p>\r\n</body>\r\n</html>");
      var12.add(var13, "Center");
      JPanel var14 = new JPanel();
      var14.setBackground(Color.WHITE);
      this.pagesRoot.add(var14, "pageLicense");
      var14.setLayout(new BorderLayout(0, 0));
      this.scrollPane_LicenseText = new JScrollPane();
      this.scrollPane_LicenseText.setBorder((Border)null);
      this.scrollPane_LicenseText.setVerticalScrollBarPolicy(22);
      this.scrollPane_LicenseText.setHorizontalScrollBarPolicy(31);
      var14.add(this.scrollPane_LicenseText, "Center");
      this.txtrLicenseText = new JTextArea();
      this.txtrLicenseText.setMargin(new Insets(10, 10, 10, 10));
      this.txtrLicenseText.setAutoscrolls(false);
      this.txtrLicenseText.setText(loadLicense());
      this.txtrLicenseText.setFont(new Font("Dialog", 0, 14));
      this.txtrLicenseText.setLineWrap(true);
      this.txtrLicenseText.setEditable(false);
      this.txtrLicenseText.setWrapStyleWord(true);
      this.scrollPane_LicenseText.setViewportView(this.txtrLicenseText);
      JPanel var15 = new JPanel();
      var14.add(var15, "South");
      var15.setLayout(new BorderLayout(0, 0));
      this.chckbxAgreeLicense = new JCheckBox("I agree to these terms and conditions");
      this.chckbxAgreeLicense.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent var1) {
            CompileLatestClientFrame.this.btnNext.setEnabled(CompileLatestClientFrame.this.chckbxAgreeLicense.isSelected());
         }
      });
      var15.add(this.chckbxAgreeLicense);
      this.chckbxAgreeLicense.setMargin(new Insets(7, 7, 7, 7));
      this.chckbxAgreeLicense.setFont(new Font("Dialog", 0, 14));
      this.chckbxAgreeLicense.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
      this.chckbxAgreeLicense.setBackground(Color.WHITE);
      JPanel var16 = new JPanel();
      var16.setBackground(Color.DARK_GRAY);
      var16.setPreferredSize(new Dimension(10, 1));
      var15.add(var16, "North");
      JPanel var17 = new JPanel();
      var17.setBackground(Color.WHITE);
      this.pagesRoot.add(var17, "pageBrowseRepositoryPath");
      var17.setLayout(new BorderLayout(0, 0));
      JTextPane var18 = new JTextPane();
      var18.setEditable(false);
      var18.setContentType("text/html");
      var18.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;\">EaglercraftX 1.8 Source Code</p>\r\n<p style=\"font-size:11px;font-weight:bold;\">It is assumed that you are running this tool in the root directory in your copy of the EaglercraftX 1.8 source code repository</p>\r\n<p style=\"font-size:11px;\">If this is not the case, please use the file chooser below to select where the repository is located</p>\r\n</body>\r\n</html>");
      var18.setMargin(new Insets(10, 10, 10, 10));
      var17.add(var18, "Center");
      JPanel var19 = new JPanel();
      var19.setBorder(new EmptyBorder(0, 20, 40, 20));
      var19.setBackground(Color.WHITE);
      var17.add(var19, "South");
      var19.setLayout(new BorderLayout(0, 0));
      JPanel var20 = new JPanel();
      var20.setPreferredSize(new Dimension(10, 40));
      var20.setBackground(Color.WHITE);
      var19.add(var20, "Center");
      var20.setLayout(new BorderLayout(0, 0));
      JLabel var21 = new JLabel("EaglercraftX 1.8 Repository Path:");
      var21.setFont(new Font("Dialog", 1, 12));
      var21.setPreferredSize(new Dimension(162, 20));
      var20.add(var21, "North");
      JPanel var22 = new JPanel();
      var22.setPreferredSize(new Dimension(10, 20));
      var22.setBackground(Color.WHITE);
      var20.add(var22, "South");
      var22.setLayout(new BorderLayout(0, 0));
      JButton var23 = new JButton("Browse...");
      var23.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            String var2 = CompileLatestClientFrame.this.textField_RepositoryPath.getText().trim();
            if (var2.length() == 0) {
               var2 = (new File("")).getAbsolutePath();
            }

            JFileChooser var3 = new JFileChooser(new File(var2));
            var3.setMultiSelectionEnabled(false);
            var3.setFileHidingEnabled(false);
            var3.setFileSelectionMode(1);
            if (var3.showOpenDialog(CompileLatestClientFrame.this.frmCompileLatestClient) == 0) {
               CompileLatestClientFrame.this.textField_RepositoryPath.setText(var3.getSelectedFile().getAbsolutePath());
            }

         }
      });
      var22.add(var23, "East");
      JPanel var24 = new JPanel();
      var24.setBackground(Color.WHITE);
      var24.setBorder(new EmptyBorder(0, 0, 0, 10));
      var22.add(var24, "Center");
      var24.setLayout(new BorderLayout(0, 0));
      this.textField_RepositoryPath = new JTextField();
      var24.add(this.textField_RepositoryPath, "Center");
      this.textField_RepositoryPath.setText((new File("")).getAbsolutePath());
      this.textField_RepositoryPath.setColumns(10);
      JPanel var25 = new JPanel();
      var25.setBackground(Color.WHITE);
      this.pagesRoot.add(var25, "pageModCoderPack");
      var25.setLayout(new BorderLayout(0, 0));
      JTextPane var26 = new JTextPane();
      var26.setMargin(new Insets(10, 10, 10, 10));
      var26.setEditable(false);
      var26.setContentType("text/html");
      var26.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;\">Mod Coder Pack</p>\r\n<p style=\"font-size:11px;\">A copy of Mod Coder Pack v9.18 is required to compile the EaglercraftX 1.8 client</p>\r\n<p style=\"font-size:11px;\">According to the Mod Coder Pack LICENSE.txt, \"You are NOT allowed to: release modified or unmodified versions of MCP anywhere\" so a copy of the files are not included in this repository, you're gonna have to download them separately</p>\r\n<p style=\"font-size:11px;font-weight:bold;\">The official download link is at: <a href=\"http://www.modcoderpack.com/\">http://www.modcoderpack.com/</a></p>\r\n<p style=\"font-size:11px;font-weight:bold;\">Visit the link and download \"mcp918.zip\" and select it below</p>\r\n</body>\r\n</html>");
      var26.addHyperlinkListener(new HyperlinkListener() {
         public void hyperlinkUpdate(HyperlinkEvent var1) {
            if (var1.getEventType() == EventType.ACTIVATED) {
               try {
                  Desktop.getDesktop().browse(var1.getURL().toURI());
               } catch (Throwable var2) {
               }
            }

         }
      });
      var25.add(var26, "Center");
      JPanel var27 = new JPanel();
      var27.setBorder(new EmptyBorder(0, 20, 40, 20));
      var27.setAlignmentY(0.0F);
      var27.setBackground(Color.WHITE);
      var25.add(var27, "South");
      var27.setLayout(new BorderLayout(0, 0));
      JPanel var28 = new JPanel();
      var28.setBackground(Color.WHITE);
      var28.setPreferredSize(new Dimension(10, 40));
      var27.add(var28, "South");
      var28.setLayout(new BorderLayout(0, 0));
      JLabel var29 = new JLabel("path to mcp918.zip:");
      var29.setVerticalAlignment(1);
      var29.setFont(new Font("Dialog", 1, 12));
      var29.setPreferredSize(new Dimension(46, 20));
      var28.add(var29, "North");
      JPanel var30 = new JPanel();
      var30.setPreferredSize(new Dimension(10, 20));
      var30.setBackground(Color.WHITE);
      var28.add(var30, "South");
      var30.setLayout(new BorderLayout(0, 0));
      JPanel var31 = new JPanel();
      var31.setBorder(new EmptyBorder(0, 0, 0, 10));
      var31.setBackground(Color.WHITE);
      var30.add(var31, "Center");
      var31.setLayout(new BorderLayout(0, 0));
      this.textField_ModCoderPack = new JTextField();
      var31.add(this.textField_ModCoderPack, "Center");
      this.textField_ModCoderPack.setColumns(10);
      JButton var32 = new JButton("Browse...");
      var32.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            String var2 = CompileLatestClientFrame.this.textField_ModCoderPack.getText().trim();
            if (var2.length() == 0) {
               File var3 = new File(System.getProperty("user.home", "."), "Downloads");
               if (!var3.exists()) {
                  var3 = new File(System.getProperty("user.home", "."));
               }

               var2 = var3.getAbsolutePath();
            }

            JFileChooser var4 = new JFileChooser(new File(var2));
            var4.setMultiSelectionEnabled(false);
            var4.setFileHidingEnabled(false);
            var4.setFileSelectionMode(0);
            if (var4.showOpenDialog(CompileLatestClientFrame.this.frmCompileLatestClient) == 0) {
               CompileLatestClientFrame.this.textField_ModCoderPack.setText(var4.getSelectedFile().getAbsolutePath());
            }

         }
      });
      var30.add(var32, "East");
      JPanel var33 = new JPanel();
      var33.setBackground(Color.WHITE);
      this.pagesRoot.add(var33, "pageBrowseJarFile");
      var33.setLayout(new BorderLayout(0, 0));
      JTextPane var34 = new JTextPane();
      var34.setContentType("text/html");
      var34.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;\">Minecraft 1.8 JAR File</p>\r\n<p style=\"font-size:11px;\">Obviously the JAR file containing the original Minecraft 1.8 bytecode is required to compile EaglercraftX 1.8, but it must again be downloaded separately from this repository because Microsoft/Mojang does not allow it to be redistributed without permission</p>\r\n<p style=\"font-size:11px;font-weight:bold;\">To download it, BUY MINECRAFT, install the Minecraft launcher, make a new launcher profile for MINECRAFT 1.8.8, and launch it at least once.</p>\r\n<p style=\"font-size:11px;font-weight:bold;\">Use the file chooser below to navigate to your \".minecraft\" folder, open the folder named \"versions\", then open the folder within it called \"1.8.8\", and then within that folder select the JAR file called \"1.8.8.jar\"</p>\r\n<p style=\"font-size:11px;\">You can also download \"Client Jar\" from this link: <a href=\"https://mcversions.net/download/1.8.8/\">https://mcversions.net/download/1.8.8/</a></p>\r\n</body>\r\n</html>");
      var34.setMargin(new Insets(10, 10, 10, 10));
      var34.setEditable(false);
      var34.addHyperlinkListener(new HyperlinkListener() {
         public void hyperlinkUpdate(HyperlinkEvent var1) {
            if (var1.getEventType() == EventType.ACTIVATED) {
               try {
                  Desktop.getDesktop().browse(var1.getURL().toURI());
               } catch (Throwable var2) {
               }
            }

         }
      });
      var33.add(var34, "Center");
      JPanel var35 = new JPanel();
      var35.setBorder(new EmptyBorder(0, 20, 10, 20));
      var35.setBackground(Color.WHITE);
      var33.add(var35, "South");
      var35.setLayout(new BorderLayout(0, 0));
      JPanel var36 = new JPanel();
      var36.setPreferredSize(new Dimension(10, 40));
      var36.setBackground(Color.WHITE);
      var35.add(var36, "Center");
      var36.setLayout(new BorderLayout(0, 0));
      JLabel var37 = new JLabel("path to 1.12.2.jar:");
      var37.setFont(new Font("Dialog", 1, 12));
      var37.setVerticalAlignment(1);
      var37.setPreferredSize(new Dimension(46, 20));
      var36.add(var37, "North");
      JPanel var38 = new JPanel();
      var38.setBackground(Color.WHITE);
      var38.setPreferredSize(new Dimension(10, 20));
      var36.add(var38, "South");
      var38.setLayout(new BorderLayout(0, 0));
      JButton var39 = new JButton("Browse...");
      var39.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            String var2 = CompileLatestClientFrame.this.textField_JarFilePath.getText().trim();
            if (var2.length() == 0) {
               File var3 = CompileLatestClientFrame.getMinecraftDir();
               File var4 = new File(var3, "versions/1.12.2");
               if (!var4.exists()) {
                  var4 = var3;
               }

               if (!var3.exists()) {
                  var4 = new File("");
               }

               var2 = var4.getAbsolutePath();
            }

            JFileChooser var5 = new JFileChooser(new File(var2));
            var5.setMultiSelectionEnabled(false);
            var5.setFileHidingEnabled(false);
            var5.setFileSelectionMode(0);
            if (var5.showOpenDialog(CompileLatestClientFrame.this.frmCompileLatestClient) == 0) {
               CompileLatestClientFrame.this.textField_JarFilePath.setText(var5.getSelectedFile().getAbsolutePath());
            }

         }
      });
      var38.add(var39, "East");
      JPanel var40 = new JPanel();
      var40.setBorder(new EmptyBorder(0, 0, 0, 10));
      var40.setBackground(Color.WHITE);
      var38.add(var40, "Center");
      var40.setLayout(new BorderLayout(0, 0));
      this.textField_JarFilePath = new JTextField();
      this.textField_JarFilePath.setText("");
      var40.add(this.textField_JarFilePath, "Center");
      this.textField_JarFilePath.setColumns(10);
      this.frmCompileLatestClient.setBounds(100, 100, 640, 480);
      this.frmCompileLatestClient.setDefaultCloseOperation(0);
      JPanel var41 = new JPanel();
      var41.setBackground(Color.WHITE);
      this.pagesRoot.add(var41, "pageBrowseAssetsIndexJSON");
      var41.setLayout(new BorderLayout(0, 0));
      JTextPane var42 = new JTextPane();
      var42.setEditable(false);
      var42.setMargin(new Insets(10, 10, 10, 10));
      var42.setContentType("text/html");
      var42.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;\">Minecraft 1.8 Assets Index JSON</p>\r\n<p style=\"font-size:11px;\">Some of Minecraft 1.8's assets are not included in the 1.8.8.jar file, they are downloaded separately, but are identified by their SHA-1 checksums. An additional JSON file must also be downloaded in order to map the checksums to their internal filenames</p>\r\n<p style=\"font-size:11px;font-weight:bold;\">Complete the previous step, then use the file chooser below to navigate to your \".minecraft\" folder, open the folder named \"assets\", then open the folder within it called \"indexes\", and then within that folder select the JSON file called \"1.8.json\"</p>\r\n<p style=\"font-size:11px;\">You can also download the JSON file from Mojang here:<br /><a style=\"font-size:9px;\" href=\"https://launchermeta.mojang.com/v1/packages/f6ad102bcaa53b1a58358f16e376d548d44933ec/1.8.json\">https://launchermeta.mojang.com/v1/packages/f6ad102bcaa53b1a58358f16e376d548d44933ec/1.8.json</a></p>\r\n</body>\r\n</html>");
      var42.addHyperlinkListener(new HyperlinkListener() {
         public void hyperlinkUpdate(HyperlinkEvent var1) {
            if (var1.getEventType() == EventType.ACTIVATED) {
               try {
                  Desktop.getDesktop().browse(var1.getURL().toURI());
               } catch (Throwable var2) {
               }
            }

         }
      });
      var41.add(var42, "Center");
      JPanel var43 = new JPanel();
      var43.setBorder(new EmptyBorder(0, 20, 30, 20));
      var43.setBackground(Color.WHITE);
      var41.add(var43, "South");
      var43.setLayout(new BorderLayout(0, 0));
      JPanel var44 = new JPanel();
      var44.setPreferredSize(new Dimension(10, 40));
      var44.setBackground(Color.WHITE);
      var43.add(var44, "Center");
      var44.setLayout(new BorderLayout(0, 0));
      JLabel var45 = new JLabel("path to 1.8.json:");
      var45.setFont(new Font("Dialog", 1, 12));
      var45.setPreferredSize(new Dimension(46, 20));
      var44.add(var45, "North");
      JPanel var46 = new JPanel();
      var46.setBackground(Color.WHITE);
      var46.setPreferredSize(new Dimension(10, 20));
      var44.add(var46, "South");
      var46.setLayout(new BorderLayout(0, 0));
      JButton var47 = new JButton("Browse...");
      var47.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            String var2 = CompileLatestClientFrame.this.textField_AssetsIndexJSON.getText().trim();
            if (var2.length() == 0) {
               File var3 = CompileLatestClientFrame.getMinecraftDir();
               File var4 = new File(var3, "assets/indexes");
               if (!var4.exists()) {
                  var4 = var3;
               }

               if (!var3.exists()) {
                  var4 = new File("");
               }

               var2 = var4.getAbsolutePath();
            }

            JFileChooser var5 = new JFileChooser(new File(var2));
            var5.setMultiSelectionEnabled(false);
            var5.setFileHidingEnabled(false);
            var5.setFileSelectionMode(0);
            if (var5.showOpenDialog(CompileLatestClientFrame.this.frmCompileLatestClient) == 0) {
               CompileLatestClientFrame.this.textField_AssetsIndexJSON.setText(var5.getSelectedFile().getAbsolutePath());
            }

         }
      });
      var46.add(var47, "East");
      JPanel var48 = new JPanel();
      var48.setBackground(Color.WHITE);
      var48.setBorder(new EmptyBorder(0, 0, 0, 10));
      var46.add(var48, "Center");
      var48.setLayout(new BorderLayout(0, 0));
      this.textField_AssetsIndexJSON = new JTextField();
      var48.add(this.textField_AssetsIndexJSON, "Center");
      this.textField_AssetsIndexJSON.setColumns(10);
      JPanel var49 = new JPanel();
      var49.setBackground(Color.WHITE);
      this.pagesRoot.add(var49, "pageMavenRepo");
      var49.setLayout(new BorderLayout(0, 0));
      JTextPane var50 = new JTextPane();
      var50.setContentType("text/html");
      var50.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;\">TeaVM Java to JavaScript Compiler</p>\r\n<p style=\"font-size:11px;\">EaglercraftX 1.8 uses TeaVM 0.9.2 to compile java to javascript. It's not included in the eagler repository to save space, so it must be downloaded from a public maven repository via HTTP, or loaded from a temporary local directory</p>\r\n</body>\r\n</html>");
      var50.setEditable(false);
      var50.setMargin(new Insets(10, 10, 10, 10));
      var49.add(var50, "North");
      JPanel var51 = new JPanel();
      var51.setBackground(Color.WHITE);
      var49.add(var51, "Center");
      var51.setLayout((LayoutManager)null);
      JLabel var52 = new JLabel("Download via HTTP:");
      var52.setFont(new Font("Dialog", 1, 12));
      var52.setBounds(12, 0, 329, 20);
      var51.add(var52);
      ButtonGroup var53 = new ButtonGroup();
      this.rdbtnMavenRepoCentral = new JRadioButton("https://repo1.maven.org/maven2/");
      this.rdbtnMavenRepoCentral.setFont(new Font("Dialog", 0, 12));
      this.rdbtnMavenRepoCentral.setSelected(true);
      this.rdbtnMavenRepoCentral.setBackground(Color.WHITE);
      this.rdbtnMavenRepoCentral.setBounds(28, 27, 474, 23);
      var53.add(this.rdbtnMavenRepoCentral);
      var51.add(this.rdbtnMavenRepoCentral);
      this.rdbtnMavenRepoBintray = new JRadioButton("(DEPRECATED) https://jcenter.bintray.com/");
      this.rdbtnMavenRepoBintray.setFont(new Font("Dialog", 0, 12));
      this.rdbtnMavenRepoBintray.setBackground(Color.WHITE);
      this.rdbtnMavenRepoBintray.setBounds(28, 52, 456, 23);
      var53.add(this.rdbtnMavenRepoBintray);
      var51.add(this.rdbtnMavenRepoBintray);
      this.rdbtnMavenRepoSonatype = new JRadioButton("https://oss.sonatype.org/content/repositories/releases/");
      this.rdbtnMavenRepoSonatype.setFont(new Font("Dialog", 0, 12));
      this.rdbtnMavenRepoSonatype.setBackground(Color.WHITE);
      this.rdbtnMavenRepoSonatype.setBounds(28, 78, 456, 23);
      var53.add(this.rdbtnMavenRepoSonatype);
      var51.add(this.rdbtnMavenRepoSonatype);
      this.rdbtnMavenRepoCustom = new JRadioButton("");
      this.rdbtnMavenRepoCustom.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent var1) {
            CompileLatestClientFrame.this.textField_MavenRepoCustomURL.setEnabled(CompileLatestClientFrame.this.rdbtnMavenRepoCustom.isSelected());
         }
      });
      this.rdbtnMavenRepoCustom.setFont(new Font("Dialog", 0, 12));
      this.rdbtnMavenRepoCustom.setBackground(Color.WHITE);
      this.rdbtnMavenRepoCustom.setBounds(28, 104, 21, 23);
      var53.add(this.rdbtnMavenRepoCustom);
      var51.add(this.rdbtnMavenRepoCustom);
      this.textField_MavenRepoCustomURL = new JTextField();
      this.textField_MavenRepoCustomURL.setEnabled(false);
      this.textField_MavenRepoCustomURL.setBounds(51, 106, 195, 20);
      var51.add(this.textField_MavenRepoCustomURL);
      this.textField_MavenRepoCustomURL.setColumns(10);
      JLabel var54 = new JLabel("Local directory:");
      var54.setFont(new Font("Dialog", 1, 12));
      var54.setBounds(12, 134, 329, 20);
      var51.add(var54);
      this.rdbtnMavenRepoLocal = new JRadioButton("");
      this.rdbtnMavenRepoLocal.setFont(new Font("Dialog", 0, 12));
      this.rdbtnMavenRepoLocal.setBackground(Color.WHITE);
      this.rdbtnMavenRepoLocal.setBounds(28, 162, 21, 23);
      var53.add(this.rdbtnMavenRepoLocal);
      var51.add(this.rdbtnMavenRepoLocal);
      this.textField_MavenRepoLocal = new JTextField();
      this.textField_MavenRepoLocal.setEnabled(false);
      this.textField_MavenRepoLocal.setColumns(10);
      this.textField_MavenRepoLocal.setBounds(51, 163, 195, 20);
      var51.add(this.textField_MavenRepoLocal);
      final JButton var55 = new JButton("Browse...");
      var55.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            String var2 = CompileLatestClientFrame.this.textField_MavenRepoLocal.getText().trim();
            if (var2.length() == 0) {
               var2 = (new File("")).getAbsolutePath();
            }

            JFileChooser var3 = new JFileChooser(new File(var2));
            var3.setMultiSelectionEnabled(false);
            var3.setFileHidingEnabled(false);
            var3.setFileSelectionMode(1);
            if (var3.showOpenDialog(CompileLatestClientFrame.this.frmCompileLatestClient) == 0) {
               CompileLatestClientFrame.this.textField_MavenRepoLocal.setText(var3.getSelectedFile().getAbsolutePath());
            }

         }
      });
      var55.setEnabled(false);
      var55.setBounds(256, 162, 89, 23);
      var51.add(var55);
      this.rdbtnMavenRepoLocal.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent var1) {
            CompileLatestClientFrame.this.textField_MavenRepoLocal.setEnabled(CompileLatestClientFrame.this.rdbtnMavenRepoLocal.isSelected());
            var55.setEnabled(CompileLatestClientFrame.this.rdbtnMavenRepoLocal.isSelected());
         }
      });
      JPanel var56 = new JPanel();
      var56.setBackground(Color.WHITE);
      this.pagesRoot.add(var56, "pageFFmpeg");
      var56.setLayout(new BorderLayout(0, 0));
      JTextPane var57 = new JTextPane();
      var57.addHyperlinkListener(new HyperlinkListener() {
         public void hyperlinkUpdate(HyperlinkEvent var1) {
            if (var1.getEventType() == EventType.ACTIVATED) {
               URL var2 = var1.getURL();
               if (var2.getHost().equals("check_ffmpeg")) {
                  if (CompileLatestClientFrame.this.isFFmpegOnPath()) {
                     JOptionPane.showMessageDialog(CompileLatestClientFrame.this.frmCompileLatestClient, "FFmpeg was found on your system's path!\nIt will be selected automatically", "Success", 1);
                     CompileLatestClientFrame.this.chckbxUsePathFFmpeg.setEnabled(true);
                     CompileLatestClientFrame.this.chckbxUsePathFFmpeg.setSelected(true);
                     CompileLatestClientFrame.this.textField_pathToFFmpeg.setEnabled(false);
                     CompileLatestClientFrame.this.btnBrowsePathToFFmpeg.setEnabled(false);
                     CompileLatestClientFrame.this.hasAskedFFmpegOnPath = true;
                     CompileLatestClientFrame.this.setPage(CompileLatestClientFrame.this.page + 1);
                  } else {
                     JOptionPane.showMessageDialog(CompileLatestClientFrame.this.frmCompileLatestClient, "FFmpeg was not found on your system's path!", "Error", 0);
                  }
               } else {
                  try {
                     Desktop.getDesktop().browse(var1.getURL().toURI());
                  } catch (Throwable var3) {
                  }
               }
            }

         }
      });
      var57.setEditable(false);
      var57.setMargin(new Insets(10, 10, 10, 10));
      var57.setContentType("text/html");
      var57.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;\">FFmpeg Audio Encoder</p>\r\n<p style=\"font-size:11px;\">A tool called FFmpeg is required to compress minecraft's audio so it's smaller, the tool is an excessively large single standalone executable file so it is not included in this repository and must be downloaded externally</p>\r\n<p style=\"font-size:11px;font-weight:bold;\">Download the FFmpeg executable here: <a href=\"https://ffmpeg.org/download.html\">https://ffmpeg.org/download.html</a></p>\r\n<p style=\"font-size:11px;\">Select where you downloaded the file below</p>\r\n<p style=\"font-size:11px;font-weight:bold;\">If you are on linux and have an FFmpeg package available, install it and click <a href=\"https://check_ffmpeg/\">here</a> to detect it on your path automatically</p>\r\n</body>\r\n</html>");
      var56.add(var57, "Center");
      JPanel var58 = new JPanel();
      var58.setBorder(new EmptyBorder(0, 20, 40, 20));
      var58.setBackground(Color.WHITE);
      var56.add(var58, "South");
      var58.setLayout(new BorderLayout(0, 0));
      JPanel var59 = new JPanel();
      var59.setPreferredSize(new Dimension(10, 40));
      var59.setBackground(Color.WHITE);
      var58.add(var59, "Center");
      var59.setLayout(new BorderLayout(0, 0));
      JLabel var60 = new JLabel(FFMPEG.windows ? "path to ffmpeg.exe:" : "path to ffmpeg:");
      var60.setPreferredSize(new Dimension(76, 20));
      var60.setFont(new Font("Dialog", 1, 12));
      var59.add(var60, "North");
      JPanel var61 = new JPanel();
      var61.setBackground(Color.WHITE);
      var61.setPreferredSize(new Dimension(10, 20));
      var59.add(var61, "South");
      var61.setLayout(new BorderLayout(0, 0));
      JPanel var62 = new JPanel();
      var62.setBorder(new EmptyBorder(0, 0, 0, 10));
      var62.setBackground(Color.WHITE);
      var61.add(var62, "Center");
      var62.setLayout(new BorderLayout(0, 0));
      this.textField_pathToFFmpeg = new JTextField();
      this.textField_pathToFFmpeg.setText("");
      var62.add(this.textField_pathToFFmpeg, "Center");
      this.textField_pathToFFmpeg.setColumns(10);
      JPanel var63 = new JPanel();
      var61.add(var63, "East");
      var63.setLayout(new BorderLayout(0, 0));
      this.btnBrowsePathToFFmpeg = new JButton("Browse...");
      var63.add(this.btnBrowsePathToFFmpeg);
      this.chckbxUsePathFFmpeg = new JCheckBox("Use FFmpeg on PATH");
      this.chckbxUsePathFFmpeg.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent var1) {
            if (CompileLatestClientFrame.this.chckbxUsePathFFmpeg.isSelected()) {
               CompileLatestClientFrame.this.textField_pathToFFmpeg.setEnabled(false);
               CompileLatestClientFrame.this.btnBrowsePathToFFmpeg.setEnabled(false);
            } else {
               CompileLatestClientFrame.this.textField_pathToFFmpeg.setEnabled(true);
               CompileLatestClientFrame.this.btnBrowsePathToFFmpeg.setEnabled(true);
            }

         }
      });
      this.chckbxUsePathFFmpeg.setFont(new Font("Dialog", 1, 12));
      this.chckbxUsePathFFmpeg.setBackground(Color.WHITE);
      this.chckbxUsePathFFmpeg.setMargin(new Insets(2, 10, 2, 2));
      var63.add(this.chckbxUsePathFFmpeg, "East");
      this.btnBrowsePathToFFmpeg.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            String var2 = CompileLatestClientFrame.this.textField_pathToFFmpeg.getText().trim();
            if (var2.length() == 0) {
               File var3 = new File(System.getProperty("user.home", "."), "Downloads");
               if (!var3.exists()) {
                  var3 = new File(System.getProperty("user.home", "."));
               }

               var2 = var3.getAbsolutePath();
            }

            JFileChooser var4 = new JFileChooser(new File(var2));
            var4.setMultiSelectionEnabled(false);
            var4.setFileHidingEnabled(false);
            var4.setFileSelectionMode(0);
            if (var4.showOpenDialog(CompileLatestClientFrame.this.frmCompileLatestClient) == 0) {
               CompileLatestClientFrame.this.textField_pathToFFmpeg.setText(var4.getSelectedFile().getAbsolutePath());
            }

         }
      });
      JPanel var64 = new JPanel();
      var64.setBackground(Color.WHITE);
      this.pagesRoot.add(var64, "pageOutputDirectory");
      var64.setLayout(new BorderLayout(0, 0));
      JTextPane var65 = new JTextPane();
      var65.setEditable(false);
      var65.setContentType("text/html");
      var65.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;\">Output Directory</p>\r\n<p style=\"font-size:11px;\">Select the destination directory where you would like this tool to save the compiled files to once this tool is finished</p>\r\n<p style=\"font-size:11px;\">The the tool will generate an \"index.html\" file, a \"classes.js\" file, a \"classes.js.map\" file, an \"assets.epk\" file, a \"lang\" directory to hold additional .lang files, and optionally an offline download version of the client that does not require an HTTP server</p>\r\n</body>\r\n</html>");
      var65.setMargin(new Insets(10, 10, 10, 10));
      var64.add(var65, "Center");
      JPanel var66 = new JPanel();
      var66.setBorder(new EmptyBorder(0, 20, 40, 20));
      var66.setBackground(Color.WHITE);
      var64.add(var66, "South");
      var66.setLayout(new BoxLayout(var66, 1));
      JPanel var67 = new JPanel();
      var67.setPreferredSize(new Dimension(10, 40));
      var67.setBackground(Color.WHITE);
      var66.add(var67);
      var67.setLayout(new BorderLayout(0, 0));
      JLabel var68 = new JLabel("compiler output directory:");
      var68.setPreferredSize(new Dimension(124, 20));
      var68.setFont(new Font("Dialog", 1, 12));
      var67.add(var68, "North");
      JPanel var69 = new JPanel();
      var69.setBackground(Color.WHITE);
      var69.setPreferredSize(new Dimension(10, 20));
      var67.add(var69, "South");
      var69.setLayout(new BorderLayout(0, 0));
      JButton var70 = new JButton("Browse...");
      var70.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            String var2 = CompileLatestClientFrame.this.textField_OutputDirectory.getText().trim();
            if (var2.length() == 0) {
               var2 = (new File("")).getAbsolutePath();
            }

            JFileChooser var3 = new JFileChooser(new File(var2));
            var3.setMultiSelectionEnabled(false);
            var3.setFileHidingEnabled(false);
            var3.setFileSelectionMode(1);
            if (var3.showOpenDialog(CompileLatestClientFrame.this.frmCompileLatestClient) == 0) {
               CompileLatestClientFrame.this.textField_OutputDirectory.setText(var3.getSelectedFile().getAbsolutePath());
            }

         }
      });
      var69.add(var70, "East");
      JPanel var71 = new JPanel();
      var71.setBorder(new EmptyBorder(0, 0, 0, 10));
      var71.setBackground(Color.WHITE);
      var69.add(var71, "Center");
      var71.setLayout(new BorderLayout(0, 0));
      this.textField_OutputDirectory = new JTextField();
      var71.add(this.textField_OutputDirectory, "Center");
      this.textField_OutputDirectory.setColumns(10);
      JPanel var72 = new JPanel();
      var72.setBackground(Color.WHITE);
      var66.add(var72);
      var72.setLayout(new BorderLayout(0, 0));
      this.chckbxOutputOfflineDownload = new JCheckBox("Generate Offline Download");
      this.chckbxOutputOfflineDownload.setSelected(true);
      var72.add(this.chckbxOutputOfflineDownload);
      this.chckbxOutputOfflineDownload.setBackground(Color.WHITE);
      this.chckbxOutputOfflineDownload.setPreferredSize(new Dimension(97, 30));
      JPanel var73 = new JPanel();
      var73.setBackground(Color.WHITE);
      var66.add(var73);
      var73.setLayout(new BorderLayout(0, 0));
      this.chckbxKeepTemporaryFiles = new JCheckBox("Keep Temporary Files");
      this.chckbxKeepTemporaryFiles.setBackground(Color.WHITE);
      this.chckbxKeepTemporaryFiles.setPreferredSize(new Dimension(129, 30));
      var73.add(this.chckbxKeepTemporaryFiles, "North");
      JPanel var74 = new JPanel();
      var74.setBackground(Color.WHITE);
      this.pagesRoot.add(var74, "pageConfirmSettings");
      var74.setLayout(new BorderLayout(0, 0));
      this.txtpnfuckOffreview = new JTextPane();
      this.txtpnfuckOffreview.setEditable(false);
      this.txtpnfuckOffreview.setContentType("text/html");
      this.txtpnfuckOffreview.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;margin:7px 0px;\">Are these correct?</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; Repository Path:</span> path here</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; Mod Coder Pack:</span> path here</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; 1.8.8.jar path:</span> path here</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; 1.8.json path:</span> path here</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; TeaVM Maven:</span> path here</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; FFmpeg:</span> path here</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; Output Directory:</span> path here</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; Make Offline Download:</span> yes/no</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; Keep Temporary Files:</span> yes/no</p>\r\n<p style=\"font-size:13px;margin-top:6px;\">&nbsp;Press the \"Compile >>\" button to confirm</p>\r\n</body>\r\n</html>");
      this.txtpnfuckOffreview.setMargin(new Insets(10, 10, 10, 10));
      var74.add(this.txtpnfuckOffreview, "Center");
      JPanel var75 = new JPanel();
      var75.setBackground(Color.WHITE);
      this.pagesRoot.add(var75, "pageLogOutput");
      var75.setLayout(new BorderLayout(0, 0));
      this.scrollPane = new JScrollPane();
      this.scrollPane.setBorder((Border)null);
      this.scrollPane.setBackground(Color.WHITE);
      this.scrollPane.setVerticalScrollBarPolicy(22);
      this.scrollPane.setHorizontalScrollBarPolicy(32);
      var75.add(this.scrollPane, "Center");
      this.txtpnLogOutput = new JTextPane();
      this.txtpnLogOutput.setAutoscrolls(false);
      this.txtpnLogOutput.setMargin(new Insets(10, 10, 10, 10));
      this.txtpnLogOutput.setContentType("text/html");
      this.txtpnLogOutput.setText("<html><head><title>shit</title><style type=\"text/css\">pre{font-family:\"Consolas\",\"Andale Mono\",monospace;}</style></head><body id=\"logContainer\" style=\"margin:0px;\"><pre></pre></body></html>");
      this.txtpnLogOutput.setEditable(false);
      this.scrollPane.setViewportView(this.txtpnLogOutput);
      JPopupMenu var76 = new JPopupMenu();
      addPopup(this.txtpnLogOutput, var76);
      JMenuItem var77 = new JMenuItem("Select All");
      var77.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            CompileLatestClientFrame.this.txtpnLogOutput.selectAll();
         }
      });
      var76.add(var77);
      JSeparator var78 = new JSeparator();
      var76.add(var78);
      JMenuItem var79 = new JMenuItem("Copy");
      var79.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (CompileLatestClientFrame.this.txtpnLogOutput.getSelectionStart() == CompileLatestClientFrame.this.txtpnLogOutput.getSelectionEnd()) {
               CompileLatestClientFrame.this.txtpnLogOutput.selectAll();
            }

            CompileLatestClientFrame.this.txtpnLogOutput.copy();
         }
      });
      var79.setFont(var79.getFont().deriveFont(1));
      var76.add(var79);
      JPanel var80 = new JPanel();
      var80.setBackground(Color.DARK_GRAY);
      var80.setPreferredSize(new Dimension(10, 1));
      var11.add(var80, "South");
      var80.setLayout((LayoutManager)null);
   }

   public void logInfo(String var1) {
      var1 = htmlentities2(var1);
      synchronized(this.logAccum) {
         if (this.logAccum.length() > 0) {
            if (this.isError) {
               this.isError = false;
               this.logAccum.append("</pre><pre>");
            }

            this.logAccum.append(var1);
         } else if (this.isError) {
            this.isError = false;
            this.logAccum.append("<pre>");
            this.logAccum.append(var1);
         } else {
            this.logAccumPrev.append(var1);
         }

         this.logDirty = true;
      }
   }

   public void logError(String var1) {
      var1 = htmlentities2(var1);
      synchronized(this.logAccum) {
         if (this.logAccum.length() > 0) {
            if (!this.isError) {
               this.isError = true;
               this.logAccum.append("</pre><pre style=\"color:#BB0000;\">");
            }

            this.logAccum.append(var1);
         } else if (!this.isError) {
            this.isError = true;
            this.logAccum.append("<pre style=\"color:#BB0000;\">");
            this.logAccum.append(var1);
         } else {
            this.logAccumPrev.append(var1);
         }

         this.logDirty = true;
      }
   }

   public void launchLogUpdateThread() {
      Thread var1 = new Thread(new Runnable() {
         public void run() {
            while(true) {
               try {
                  while(true) {
                     Thread.sleep(100L);
                     synchronized(CompileLatestClientFrame.this.logAccum) {
                        if (CompileLatestClientFrame.this.logDirty) {
                           EventQueue.invokeAndWait(new Runnable() {
                              public void run() {
                                 HTMLDocument var1 = (HTMLDocument)CompileLatestClientFrame.this.txtpnLogOutput.getDocument();
                                 if (CompileLatestClientFrame.this.logAccumBody == null) {
                                    CompileLatestClientFrame.this.logAccumBody = var1.getElement("logContainer");
                                 }

                                 if (CompileLatestClientFrame.this.logAccumPrev.length() > 0) {
                                    try {
                                       var1.insertString(CompileLatestClientFrame.this.logAccumBody.getElement(CompileLatestClientFrame.this.logAccumBody.getElementCount() - 1).getEndOffset() - 1, CompileLatestClientFrame.this.logAccumPrev.toString(), (AttributeSet)null);
                                    } catch (BadLocationException var4) {
                                    }

                                    CompileLatestClientFrame.this.logAccumPrev = new StringBuilder();
                                 }

                                 if (CompileLatestClientFrame.this.logAccum.length() > 0) {
                                    CompileLatestClientFrame.this.logAccum.append("</pre>");

                                    try {
                                       var1.insertBeforeEnd(CompileLatestClientFrame.this.logAccumBody, CompileLatestClientFrame.this.logAccum.toString());
                                    } catch (BadLocationException var2) {
                                    } catch (IOException var3) {
                                    }

                                    CompileLatestClientFrame.this.logAccum = new StringBuilder();
                                 }

                              }
                           });
                           EventQueue.invokeAndWait(new Runnable() {
                              public void run() {
                                 JScrollBar var1 = CompileLatestClientFrame.this.scrollPane.getVerticalScrollBar();
                                 var1.setValue(var1.getMaximum());
                                 CompileLatestClientFrame.this.scrollPane.getHorizontalScrollBar().setValue(0);
                              }
                           });
                           CompileLatestClientFrame.this.logDirty = false;
                        }
                     }
                  }
               } catch (Throwable var3) {
               }
            }
         }
      }, "LazyLogUpdateThread");
      var1.setDaemon(true);
      var1.start();
   }

   public void finishCompiling(final boolean var1, final String var2) {
      try {
         EventQueue.invokeAndWait(new Runnable() {
            public void run() {
               if (!CompileLatestClientFrame.this.finished) {
                  CompileLatestClientFrame.this.lblProgressState.setText(var1 ? "Compilation Failed!" : "Compilation Succeeded!");
                  CompileLatestClientFrame.this.lblProgressState.setForeground(var1 ? new Color(136, 0, 0) : new Color(0, 136, 0));
                  CompileLatestClientFrame.this.btnNext.setEnabled(true);
                  CompileLatestClientFrame.this.finished = true;
                  if (var1) {
                     JOptionPane.showMessageDialog(CompileLatestClientFrame.this.frmCompileLatestClient, CompileLatestClientFrame.wordWrap("Failed to Compile Client!\nReason: " + var2), "Error", 0);
                  } else {
                     JOptionPane.showMessageDialog(CompileLatestClientFrame.this.frmCompileLatestClient, "Finished Compiling", "Success", 1);
                  }
               }

            }
         });
      } catch (InterruptedException | InvocationTargetException var4) {
         var4.printStackTrace();
      }

   }

   private static void addPopup(Component var0, final JPopupMenu var1) {
      var0.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent var1x) {
            if (var1x.isPopupTrigger()) {
               this.showMenu(var1x);
            }

         }

         public void mouseReleased(MouseEvent var1x) {
            if (var1x.isPopupTrigger()) {
               this.showMenu(var1x);
            }

         }

         private void showMenu(MouseEvent var1x) {
            var1.show(var1x.getComponent(), var1x.getX(), var1x.getY());
         }
      });
   }

   public String getRepositoryURL() {
      if (this.rdbtnMavenRepoCustom.isSelected()) {
         return this.textField_MavenRepoCustomURL.getText();
      } else if (this.rdbtnMavenRepoSonatype.isSelected()) {
         return "https://oss.sonatype.org/content/repositories/releases/";
      } else if (this.rdbtnMavenRepoBintray.isSelected()) {
         return "https://jcenter.bintray.com/";
      } else {
         return this.rdbtnMavenRepoCentral.isSelected() ? "https://repo1.maven.org/maven2/" : null;
      }
   }

   private boolean isFFmpegOnPath() {
      if (!this.knowFoundFFmpeg && (this.foundFFmpeg = FFMPEG.checkFFMPEGOnPath())) {
         this.knowFoundFFmpeg = true;
      }

      return this.foundFFmpeg;
   }

   private static String htmlentities(String var0) {
      return var0.replace("<", "&lt;").replace(">", "&gt;");
   }

   private static String htmlentities2(String var0) {
      return var0.replace("</pre>", "[/pre]");
   }

   private static String wordWrap(String var0) {
      StringBuilder var1;
      for(var1 = new StringBuilder(); var0.length() > 100; var0 = var0.substring(100)) {
         var1.append(var0.substring(0, 100)).append('\n');
      }

      var1.append(var0);
      return var1.toString();
   }

   private static String loadLicense() {
      try {
         Throwable var0 = null;
         Object var1 = null;

         try {
            BufferedReader var2 = new BufferedReader(new InputStreamReader(CompileLatestClientFrame.class.getResourceAsStream("/lang/LICENSE.txt"), StandardCharsets.UTF_8));

            try {
               StringBuilder var3 = new StringBuilder();
               char[] var4 = new char[4096];

               int var5;
               while((var5 = var2.read(var4)) != -1) {
                  var3.append(var4, 0, var5);
               }

               String var10000 = var3.toString();
               return var10000;
            } finally {
               if (var2 != null) {
                  var2.close();
               }

            }
         } catch (Throwable var13) {
            if (var0 == null) {
               var0 = var13;
            } else if (var0 != var13) {
               var0.addSuppressed(var13);
            }

            throw var0;
         }
      } catch (IOException var14) {
         return "Could not load LICENSE text!\n\nPlease read the file named \"LICENSE\" in the root directory of the repository before continuing";
      }
   }
}
