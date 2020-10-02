package iamutkarshtiwari.github.io.ananas.editimage.utils;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0015\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J)\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u0018\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\tH\u0002J#\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0007\u00a2\u0006\u0002\u0010\u0012J\u0010\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\u0015H\u0007\u00a8\u0006\u0016"}, d2 = {"Liamutkarshtiwari/github/io/ananas/editimage/utils/PermissionUtils;", "", "()V", "checkPermission", "", "activity", "Landroid/app/Activity;", "permissions", "", "", "requestCode", "", "(Landroid/app/Activity;[Ljava/lang/String;I)Z", "hasPermission", "context", "Landroid/content/Context;", "permission", "hasPermissions", "(Landroid/content/Context;[Ljava/lang/String;)Z", "isAllGranted", "grantResults", "", "ananas_debug"})
public final class PermissionUtils {
    public static final iamutkarshtiwari.github.io.ananas.editimage.utils.PermissionUtils INSTANCE = null;
    
    private final boolean hasPermission(android.content.Context context, java.lang.String permission) {
        return false;
    }
    
    public static final boolean hasPermissions(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions) {
        return false;
    }
    
    public final boolean checkPermission(@org.jetbrains.annotations.NotNull()
    android.app.Activity activity, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, int requestCode) {
        return false;
    }
    
    /**
     * Checks all given permissions have been granted.
     *
     * @param grantResults results
     * @return returns true if all permissions have been granted.
     */
    public static final boolean isAllGranted(@org.jetbrains.annotations.NotNull()
    int[] grantResults) {
        return false;
    }
    
    private PermissionUtils() {
        super();
    }
}