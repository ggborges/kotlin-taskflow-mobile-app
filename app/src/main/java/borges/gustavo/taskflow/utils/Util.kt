package borges.gustavo.taskflow.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Util {
    fun formatCalendarDateTime(calendar: Calendar): String {
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return "%02d/%02d/%04d às %02d:%02d".format(day, month, year, hour, minute)
    }

    fun parseDateTime(dateTime: String?): Long? {
        // Converter String para Long (timestamp)
        return dateTime?.let {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(it)?.time
        }
    }

    fun formatDateTime(timestamp: Long?): String? {
        // Converter Long (timestamp) para String
        return timestamp?.let {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(it))
        }
    }
}