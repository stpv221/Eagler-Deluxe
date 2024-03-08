package net.lax1dude.eaglercraft.v1_8.buildtools.decompiler;

import java.util.ArrayList;
import java.util.HashMap;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

public class ParameterSplitter extends SignatureVisitor {
   protected static final ArrayList<LocalVariableGenerator> ret = new ArrayList();
   protected static final HashMap<String, Integer> usedLocals = new HashMap();

   protected ParameterSplitter() {
      super(327680);
   }

   public static int getParameterArray(String var0, String[] var1) {
      SignatureReader var2 = new SignatureReader(var0);
      ParameterSplitter var3 = new ParameterSplitter();
      ret.clear();
      usedLocals.clear();
      var2.accept(var3);
      int var4 = ret.size();
      if (var4 > var1.length) {
         var4 = var1.length;
      }

      int var5 = 0;

      for(int var6 = 0; var6 < var4; ++var6) {
         if (var1[var6] == null) {
            var1[var6] = LocalVariableGenerator.nextLocalVariableName(usedLocals, (LocalVariableGenerator)ret.get(var6), "par");
            ++var5;
         }
      }

      return var5;
   }

   public static String[] getParameterSigArray(String var0, String var1) {
      SignatureReader var2 = new SignatureReader(var0);
      ParameterSplitter var3 = new ParameterSplitter();
      ret.clear();
      usedLocals.clear();
      var2.accept(var3);
      String[] var4 = new String[ret.size()];

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var4[var5] = LocalVariableGenerator.nextLocalVariableName(usedLocals, (LocalVariableGenerator)ret.get(var5), var1);
      }

      return var4;
   }

   public SignatureVisitor visitParameterType() {
      LocalVariableGenerator var1 = new LocalVariableGenerator();
      ret.add(var1);
      return var1;
   }

   public SignatureVisitor visitClassBound() {
      return LocalVariableGenerator.nopVisitor;
   }

   public SignatureVisitor visitExceptionType() {
      return LocalVariableGenerator.nopVisitor;
   }

   public SignatureVisitor visitInterface() {
      return LocalVariableGenerator.nopVisitor;
   }

   public SignatureVisitor visitInterfaceBound() {
      return LocalVariableGenerator.nopVisitor;
   }

   public SignatureVisitor visitTypeArgument(char var1) {
      return LocalVariableGenerator.nopVisitor;
   }

   public SignatureVisitor visitReturnType() {
      return LocalVariableGenerator.nopVisitor;
   }

   public SignatureVisitor visitArrayType() {
      return LocalVariableGenerator.nopVisitor;
   }

   public void visitBaseType(char var1) {
   }

   public void visitClassType(String var1) {
   }

   public void visitEnd() {
   }

   public void visitFormalTypeParameter(String var1) {
   }

   public void visitInnerClassType(String var1) {
   }

   public SignatureVisitor visitSuperclass() {
      return LocalVariableGenerator.nopVisitor;
   }

   public void visitTypeArgument() {
   }

   public void visitTypeVariable(String var1) {
   }
}
