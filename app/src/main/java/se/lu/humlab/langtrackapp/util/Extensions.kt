package se.lu.humlab.langtrackapp.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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

fun Date.formatToReadable(dateFormat: String = "yyyy-MM-dd HH:mm", timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}