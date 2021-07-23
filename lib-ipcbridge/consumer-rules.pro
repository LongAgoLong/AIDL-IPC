# copyright zhonghanwen
#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-ignorewarnings

-keep class com.leo.lib_interface.** {*;}
-keep class com.leo.aidl.** {*;}
-keep class com.google.gson.** {*;}
-dontwarn com.google.gson.**