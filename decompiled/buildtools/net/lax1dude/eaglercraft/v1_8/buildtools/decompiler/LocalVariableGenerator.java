package net.lax1dude.eaglercraft.v1_8.buildtools.decompiler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

public class LocalVariableGenerator extends SignatureVisitor {
   public static final Map<Character, String> primitiveNames = new HashMap();
   public static final Set<String> illegalVariableNames = new HashSet();
   private String baseClass = null;
   private boolean isArray = false;
   private String typeParam1 = null;
   private boolean typeParam1IsArray = false;
   private String typeParam2 = null;
   private boolean typeParam2IsArray = false;
   public static final SignatureVisitor nopVisitor;

   static {
      primitiveNames.put('Z', "Flag");
      primitiveNames.put('C', "Char");
      primitiveNames.put('B', "Byte");
      primitiveNames.put('S', "Short");
      primitiveNames.put('I', "Int");
      primitiveNames.put('F', "Float");
      primitiveNames.put('J', "Long");
      primitiveNames.put('D', "Double");
      primitiveNames.put('V', "Void");
      illegalVariableNames.addAll(Arrays.asList("abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "continue", "const", "default", "do", "double", "else", "enum", "exports", "extends", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "var", "void", "volatile", "while", "string"));
      nopVisitor = new SignatureVisitor(327680) {
      };
   }

   LocalVariableGenerator() {
      super(327680);
   }

   public static String createName(String var0) {
      SignatureReader var1 = new SignatureReader(var0);
      LocalVariableGenerator var2 = new LocalVariableGenerator();
      var1.acceptType(var2);
      return var2.getResult();
   }

   private String removePath(String var1) {
      int var2 = var1.lastIndexOf(47);
      int var3 = var1.lastIndexOf(36);
      if (var3 > var2 && var3 != var1.length() - 1) {
         var2 = var3;
      }

      if (var2 != -1) {
         var1 = var1.substring(var2 + 1);
      }

      if (var1.length() == 0 || Character.isDigit(var1.charAt(0))) {
         var1 = "obj" + var1;
      }

      return var1;
   }

   String getResult() {
      String var1;
      if (this.baseClass == null) {
         var1 = "Object";
      } else {
         var1 = this.removePath(this.baseClass);
      }

      if (this.typeParam1 == null && this.typeParam2 == null) {
         if (this.isArray) {
            var1 = "ArrayOf" + var1;
         }
      } else if (this.isArray) {
         var1 = var1 + "Array";
      }

      if (this.typeParam1 != null && this.typeParam2 == null) {
         if (this.typeParam1IsArray) {
            this.typeParam1 = this.typeParam1 + "Array";
         }

         var1 = var1 + "Of" + this.removePath(this.typeParam1);
      } else if (this.typeParam1 != null && this.typeParam2 != null) {
         if (this.typeParam1IsArray) {
            this.typeParam1 = this.typeParam1 + "Array";
         }

         if (this.typeParam2IsArray) {
            this.typeParam2 = this.typeParam2 + "Array";
         }

         var1 = var1 + "Of" + this.removePath(this.typeParam1) + "And" + this.removePath(this.typeParam2);
      }

      return var1;
   }

   public SignatureVisitor visitArrayType() {
      if (this.baseClass == null) {
         this.isArray = true;
         return new LocalVariableGenerator.ArrayTypeVisitor();
      } else {
         return nopVisitor;
      }
   }

   public void visitBaseType(char var1) {
      if (this.baseClass == null) {
         this.baseClass = (String)primitiveNames.get(var1);
      }

   }

   public SignatureVisitor visitClassBound() {
      return nopVisitor;
   }

   public void visitClassType(String var1) {
      if (this.baseClass == null) {
         this.baseClass = var1;
      }

   }

   public void visitEnd() {
   }

   public SignatureVisitor visitExceptionType() {
      return nopVisitor;
   }

   public void visitFormalTypeParameter(String var1) {
   }

   public void visitInnerClassType(String var1) {
   }

   public SignatureVisitor visitInterface() {
      return nopVisitor;
   }

   public SignatureVisitor visitInterfaceBound() {
      return nopVisitor;
   }

   public SignatureVisitor visitParameterType() {
      return nopVisitor;
   }

   public SignatureVisitor visitReturnType() {
      return nopVisitor;
   }

   public SignatureVisitor visitSuperclass() {
      return nopVisitor;
   }

   public void visitTypeArgument() {
   }

   public SignatureVisitor visitTypeArgument(char var1) {
      if (this.typeParam1 == null) {
         return new LocalVariableGenerator.TypeParamVisitor(1);
      } else {
         return (SignatureVisitor)(this.typeParam2 == null ? new LocalVariableGenerator.TypeParamVisitor(2) : nopVisitor);
      }
   }

   public void visitTypeVariable(String var1) {
   }

   public static String nextLocalVariableNameFromString(Map<String, Integer> var0, String var1, String var2) {
      String var3 = var1.length() == 1 ? (String)primitiveNames.get(var1.charAt(0)) : null;
      if (var3 == null) {
         var3 = createName(var1);
      }

      String var4 = var3.toLowerCase();
      Integer var5 = (Integer)var0.get(var4);
      if (var5 == null) {
         var0.put(var4, 1);
         if (Character.isDigit(var3.charAt(var3.length() - 1))) {
            var3 = var3 + "_1";
         } else {
            var3 = illegalVariableNames.contains(var3.toLowerCase()) ? var3 + "1" : var3;
         }
      } else {
         int var6 = var5 + 1;
         var0.put(var4, var6);
         if (!Character.isDigit(var3.charAt(var3.length() - 1)) && !var3.contains("And") && var3.length() <= 16) {
            var3 = var3 + var6;
         } else {
            var3 = var3 + "_" + var6;
         }
      }

      return var2 == null ? camelCase(var3) : var2 + var3;
   }

   public static String nextLocalVariableName(Map<String, Integer> var0, LocalVariableGenerator var1, String var2) {
      String var3 = var1.getResult();
      String var4 = var3.toLowerCase();
      Integer var5 = (Integer)var0.get(var4);
      if (var5 == null) {
         var0.put(var4, 1);
         if (Character.isDigit(var3.charAt(var3.length() - 1))) {
            var3 = var3 + "_1";
         } else {
            var3 = illegalVariableNames.contains(var3.toLowerCase()) ? var3 + "1" : var3;
         }
      } else {
         int var6 = var5 + 1;
         var0.put(var4, var6);
         if (!Character.isDigit(var3.charAt(var3.length() - 1)) && !var3.contains("And") && var3.length() <= 16) {
            var3 = var3 + var6;
         } else {
            var3 = var3 + "_" + var6;
         }
      }

      return var2 == null ? camelCase(var3) : var2 + var3;
   }

   public static String camelCase(String var0) {
      if (var0 != null && var0.length() > 0) {
         return var0.length() > 1 && Character.isUpperCase(var0.charAt(0)) && Character.isUpperCase(var0.charAt(1)) ? "var" + var0 : var0.substring(0, 1).toLowerCase() + var0.substring(1);
      } else {
         return "name";
      }
   }

   private class ArrayTypeVisitor extends SignatureVisitor {
      protected ArrayTypeVisitor() {
         super(327680);
      }

      public void visitBaseType(char var1) {
         if (LocalVariableGenerator.this.baseClass == null) {
            LocalVariableGenerator.this.baseClass = (String)LocalVariableGenerator.primitiveNames.get(var1);
         }

      }

      public void visitClassType(String var1) {
         if (LocalVariableGenerator.this.baseClass == null) {
            LocalVariableGenerator.this.baseClass = var1;
         }

      }

      public SignatureVisitor visitArrayType() {
         if (LocalVariableGenerator.this.baseClass == null) {
            LocalVariableGenerator.this.baseClass = "array";
         }

         return LocalVariableGenerator.nopVisitor;
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

      public SignatureVisitor visitParameterType() {
         return LocalVariableGenerator.nopVisitor;
      }

      public SignatureVisitor visitTypeArgument(char var1) {
         return LocalVariableGenerator.nopVisitor;
      }

      public SignatureVisitor visitReturnType() {
         return LocalVariableGenerator.nopVisitor;
      }
   }

   private class TypeParamVisitor extends SignatureVisitor {
      private boolean hasVisited = false;
      private final int firstOrSecond;

      protected TypeParamVisitor(int var2) {
         super(327680);
         this.firstOrSecond = var2;
      }

      public void visitBaseType(char var1) {
         if (!this.hasVisited) {
            if (this.firstOrSecond == 1) {
               if (LocalVariableGenerator.this.typeParam1 == null) {
                  LocalVariableGenerator.this.typeParam1 = (String)LocalVariableGenerator.primitiveNames.get(var1);
               }
            } else if (this.firstOrSecond == 2 && LocalVariableGenerator.this.typeParam2 == null) {
               LocalVariableGenerator.this.typeParam2 = (String)LocalVariableGenerator.primitiveNames.get(var1);
            }

            this.hasVisited = true;
         }

      }

      public void visitClassType(String var1) {
         if (!this.hasVisited) {
            if (this.firstOrSecond == 1) {
               if (LocalVariableGenerator.this.typeParam1 == null) {
                  LocalVariableGenerator.this.typeParam1 = var1;
               }
            } else if (this.firstOrSecond == 2 && LocalVariableGenerator.this.typeParam2 == null) {
               LocalVariableGenerator.this.typeParam2 = var1;
            }

            this.hasVisited = true;
         }

      }

      public SignatureVisitor visitArrayType() {
         if (!this.hasVisited) {
            if (this.firstOrSecond == 1) {
               if (LocalVariableGenerator.this.typeParam1 == null) {
                  LocalVariableGenerator.this.typeParam1IsArray = true;
                  return new LocalVariableGenerator.TypeParamVisitor.TypeParamArrayVisitor();
               }
            } else if (this.firstOrSecond == 2 && LocalVariableGenerator.this.typeParam2 == null) {
               LocalVariableGenerator.this.typeParam2IsArray = true;
               return new LocalVariableGenerator.TypeParamVisitor.TypeParamArrayVisitor();
            }
         }

         return LocalVariableGenerator.nopVisitor;
      }

      private class TypeParamArrayVisitor extends SignatureVisitor {
         protected TypeParamArrayVisitor() {
            super(327680);
         }

         public void visitBaseType(char var1) {
            if (!TypeParamVisitor.this.hasVisited) {
               if (TypeParamVisitor.this.firstOrSecond == 1) {
                  if (LocalVariableGenerator.this.typeParam1 == null) {
                     LocalVariableGenerator.this.typeParam1 = (String)LocalVariableGenerator.primitiveNames.get(var1);
                  }
               } else if (TypeParamVisitor.this.firstOrSecond == 2 && LocalVariableGenerator.this.typeParam2 == null) {
                  LocalVariableGenerator.this.typeParam2 = (String)LocalVariableGenerator.primitiveNames.get(var1);
               }

               TypeParamVisitor.this.hasVisited = true;
            }

         }

         public void visitClassType(String var1) {
            if (!TypeParamVisitor.this.hasVisited) {
               if (TypeParamVisitor.this.firstOrSecond == 1) {
                  if (LocalVariableGenerator.this.typeParam1 == null) {
                     LocalVariableGenerator.this.typeParam1 = var1;
                  }
               } else if (TypeParamVisitor.this.firstOrSecond == 2 && LocalVariableGenerator.this.typeParam2 == null) {
                  LocalVariableGenerator.this.typeParam2 = var1;
               }

               TypeParamVisitor.this.hasVisited = true;
            }

         }

         public SignatureVisitor visitArrayType() {
            if (!TypeParamVisitor.this.hasVisited) {
               if (TypeParamVisitor.this.firstOrSecond == 1) {
                  if (LocalVariableGenerator.this.typeParam1 == null) {
                     LocalVariableGenerator.this.typeParam1 = "array";
                  }
               } else if (TypeParamVisitor.this.firstOrSecond == 2 && LocalVariableGenerator.this.typeParam2 == null) {
                  LocalVariableGenerator.this.typeParam2 = "array";
               }

               TypeParamVisitor.this.hasVisited = true;
            }

            return LocalVariableGenerator.nopVisitor;
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

         public SignatureVisitor visitParameterType() {
            return LocalVariableGenerator.nopVisitor;
         }

         public SignatureVisitor visitTypeArgument(char var1) {
            return LocalVariableGenerator.nopVisitor;
         }

         public SignatureVisitor visitReturnType() {
            return LocalVariableGenerator.nopVisitor;
         }
      }
   }
}
