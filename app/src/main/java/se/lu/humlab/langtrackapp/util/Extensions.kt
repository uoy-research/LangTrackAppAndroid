package se.lu.humlab.langtrackapp.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import se.lu.humlab.langtrackapp.R
import java.text.SimpleDateFormat
import java.util.*

fun AppCompatActivity.loadFragment(fragment: Fragment){
    supportFragmentManager
        .beginTransaction()
        .replace(
            R.id.surveyContainer,
            fragment
        )
        .commit()
}

fun getDate(milli: Long): String{
    val formatter = SimpleDateFormat("dd MMMM yyyy    HH:mm",
        Locale("sv", "SE")
    )
    val calendar = Calendar.getInstance();
    calendar.timeInMillis = milli * 1000//TODO: temp, should be in milli
    return formatter.format(calendar.time)
}

fun pxToDp(px: Int, context: Context): Int{
    val density = context.resources.displayMetrics.density
    return Math.round(px.toFloat() / density)
}

fun dpToPx(dp: Int, context: Context): Int{
    val density = context.resources.displayMetrics.density
    return Math.round(dp.toFloat() * density)
}

//inString: 2020-03-16T22:15:25.820Z
fun String.toDate(dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss", timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date? {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this.substringBefore('.'))
}

fun Date.formatToReadable(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}
fun String?.asUri(): Uri? {
    try {
        return Uri.parse(this)
    } catch (e: Exception) {}
    return null
}
fun Uri?.openInBrowser(context: Context) {
    this ?: return // Do nothing if uri is null

    val browserIntent = Intent(Intent.ACTION_VIEW, this)
    ContextCompat.startActivity(context, browserIntent, null)
}

fun View.setMarginTop(topMargin: Int) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(params.leftMargin, topMargin, params.rightMargin, params.bottomMargin)
    layoutParams = params
}