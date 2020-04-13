package se.lu.humlab.langtrackapp.screen.main

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
    private var activeIndicator: ImageView = itemView.findViewById(R.id.activeRowIndicator)
    private var cellLayout: ConstraintLayout = itemView.findViewById(R.id.activeSurveyCellLayout)
    private lateinit var item: Assignment
    lateinit var mainHandler: Handler
    lateinit var expiryListener: OnExpiredListener


    private val repeatUpdatingText = object : Runnable {
        override fun run() {
            setRemainingTime()
            println("running repeatUpdatingText in object ${item.survey.title}")
            mainHandler.postDelayed(this, 1000 * 60)
        }
    }

    init {
        theItemView.setOnClickListener { onRowClickedListener.rowClicked(item) }

        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(repeatUpdatingText)

        /*countDownHandler.postDelayed(object : Runnable {
            override fun run() {
                setRemainingTime()
                println("running countDownHandler in object ${item.survey.name}")
                countDownHandler.postDelayed(this, 1000 * 60)//1 min delay
            }
        }, 0)*/
    }


    fun bind(item: Assignment, pos: Int, listener: OnExpiredListener){
        this.expiryListener = listener
        this.item = item
        task.text = "Enkät att besvara"
        setRemainingTime()
        activeIndicator.visibility = View.VISIBLE
    }

    fun setRemainingTime(){
        val endTimeInSec = (item.expireAt.toDate()?.time ?: 0) / 1000
        val now = Date().time / 1000
        if (endTimeInSec != 0L){
            val diff = endTimeInSec - now
            if (diff > 0){
                val minutes = ((diff / 60) % 60).toInt()
                val hours = (diff / 3600).toInt()
                if (hours == 0) {
                    if(minutes < 10) {
                        date.text = "Tid kvar: %1d minuter".format(minutes)
                    }else {
                        date.text = "Tid kvar: %2d minuter".format(minutes)
                    }
                }else {
                    var hourString = "timme"
                    if (hours > 1){
                        hourString = "timmar"
                    }
                    if(minutes < 10) {
                        date.text = "Tid kvar: %2d %s och %1d minuter".format(hours,hourString, minutes)
                    }else {
                        date.text = "Tid kvar: %2d %s och %2d minuter".format(hours,hourString, minutes)
                    }
                }
            }else{
                removeCallbacks()
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