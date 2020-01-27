package se.lu.humlab.langtrackapp.screen.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Survey
import se.lu.humlab.langtrackapp.interfaces.OnSurveyRowClickedListener
import java.text.SimpleDateFormat
import java.util.*

class SurveyItemViewHolder(theItemView: View,
                           onRowClickedListener: OnSurveyRowClickedListener
): RecyclerView.ViewHolder(theItemView) {

    private var task: TextView = itemView.findViewById(R.id.surveyRecyclerTitleTextView)
    private var date: TextView = itemView.findViewById(R.id.surveyRecyclerDateTextView)
    private var cellLayout: ConstraintLayout = itemView.findViewById(R.id.surveyCellLayout)
    private lateinit var item: Survey

    init {
        theItemView.setOnClickListener { onRowClickedListener.rowClicked(item) }
    }

    fun bind(item: Survey, pos: Int){
        this.item = item
        task.text = this.item.title
        date.text = getDate(item.date)
    }

    fun getItem(): Survey {
        return item
    }

    private fun getDate(milli: Long): String{
        val formatter = SimpleDateFormat("dd MMMM yyyy    HH:mm",
            Locale("sv", "SE"))
        val calendar = Calendar.getInstance();
        calendar.timeInMillis = milli
        return formatter.format(calendar.time)
    }


    companion object {
        fun newInstance(parent: ViewGroup,
                        onRowClickedListener: OnSurveyRowClickedListener):SurveyItemViewHolder{
            return SurveyItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.recycle_surveyitem_row,
                    parent,
                    false
                ),
                onRowClickedListener
            )
        }
    }
}