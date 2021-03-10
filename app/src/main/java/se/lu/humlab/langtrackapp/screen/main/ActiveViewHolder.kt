package se.lu.humlab.langtrackapp.screen.main

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Assignment
import se.lu.humlab.langtrackapp.interfaces.OnExpiredListener
import se.lu.humlab.langtrackapp.interfaces.OnSurveyRowClickedListener
import se.lu.humlab.langtrackapp.util.toDate
import java.util.*

class ActiveViewHolder(theItemView: View,
                       onRowClickedListener: OnSurveyRowClickedListener
): RecyclerView.ViewHolder(theItemView) {

    private var task: TextView = itemView.findViewById(R.id.activerSurveyRecyclerTitleTextView)
    private var date: TextView = itemView.findViewById(R.id.activeSurveyRecyclerDateTextView)
    private var cellLayout: ConstraintLayout = itemView.findViewById(R.id.activeSurveyCellLayout)
    private lateinit var item: Assignment
    lateinit var mainHandler: Handler
    lateinit var expiryListener: OnExpiredListener


    private val repeatUpdatingText = object : Runnable {
        override fun run() {
            setRemainingTime()
            mainHandler.postDelayed(this, 1000 * 60)
        }
    }

    init {
        theItemView.setOnClickListener { onRowClickedListener.rowClicked(item) }
    }

    fun setCallbacks(){
        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(repeatUpdatingText)
    }

    fun bind(item: Assignment, pos: Int, listener: OnExpiredListener){
        this.expiryListener = listener
        this.item = item
        task.text = task.context.getString(R.string.surveyToAnswer)
        setRemainingTime()
    }

    fun setRemainingTime(){
        val endTimeInSec = (item.expireAt.toDate()?.time ?: 0) / 1000
        val now = Date().time / 1000
        if (endTimeInSec != 0L){
            val diff = (endTimeInSec - now)
            if (diff > 0){
                val days = (diff / 86400).toInt()
                val hours = ((diff % 86400) / 3600).toInt()
                val minutes = (((diff % 3600) / 60) % 60).toInt()

                var dayString = date.context.getString(R.string.days)
                if (days == 1){
                    dayString = date.context.getString(R.string.day)
                }
                var hourString = date.context.getString(R.string.hours)
                if (hours == 1){
                    hourString = date.context.getString(R.string.hour)
                }
                var minuteString = date.context.getString(R.string.minutes)
                if (minutes == 1){
                    minuteString = date.context.getString(R.string.minute)
                }

                if (days > 0){
                    date.text = "%s %2d %s %2d %s %s %2d %s".format(
                        date.context.getString(R.string.timeLeft),
                        days,
                        dayString,
                        hours,
                        hourString,
                        date.context.getString(R.string.and),
                        minutes,
                        minuteString)
                }else if (hours > 0){
                    date.text = "%s %2d %s %s %2d %s".format(
                        date.context.getString(R.string.timeLeft),
                        hours,
                        hourString,
                        date.context.getString(R.string.and),
                        minutes,
                        minuteString)
                }else{date.text = "%s %2d %s".format(
                    date.context.getString(R.string.timeLeft),
                    minutes,
                    minuteString)
                }
            }else{
                expiryListener.assignmentExpired()
            }
        }else {
            date.text = ""
        }
    }



    fun removeCallbacks(){
        mainHandler.removeCallbacks(repeatUpdatingText)
        mainHandler.removeCallbacksAndMessages(null)
    }

    fun getItem(): Assignment {
        return item
    }


    companion object {
        fun newInstance(parent: ViewGroup,
                        onRowClickedListener: OnSurveyRowClickedListener
        ):ActiveViewHolder{
            return ActiveViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.recycle_active_row,
                    parent,
                    false
                ),
                onRowClickedListener
            )
        }
    }
}