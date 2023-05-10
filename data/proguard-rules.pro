
-keepclassmembers class <1>.<2> {
  <1>.<2>$Companion Companion;
}

-keepclassmembers class * implements android.os.Parcelable { *; }
-keepnames class * extends android.os.Parcelable
-keepnames class * extends java.io.Serializable

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.* { *; }
-keepattributes Signature
-keepattributes Exceptions