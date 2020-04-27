package se.lu.humlab.langtrackapp.util

import android.content.Context
import android.content.pm.PackageManager

fun getVersionNumber(context: Context): String{
    var version = ""
    try {
        val pInfo =
            context.packageManager.getPackageInfo(context.packageName, 0)
        version = pInfo.versionName

    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return "Version: $version"
}