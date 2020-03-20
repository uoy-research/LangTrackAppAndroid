package se.lu.humlab.langtrackapp.screen.overview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.overview_top_view_layout.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Assignment
import se.lu.humlab.langtrackapp.data.model.Survey

class TopViewItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)  {


    init {
        LayoutInflater.from(context).inflate(R.layout.overview_top_view_layout, this, true)
    }

    fun setText(assignment: Assignment){
        topViewStatusText.text = "Status"
        //topViewAnsweredDateText.text = if (survey.answer != null) "Besvarad ${getDate(survey.respondeddate)}" else "Obesvarad"
        //topViewSentDateText.text = getDate(survey.date)//TODO: check date
        if (assignment.survey.answer != null){
            overviewStatusImage.setImageResource(R.drawable.lta_icon_ground_light)
        }else {
            overviewStatusImage.setImageResource(R.drawable.lta_icon_unanswered_light)
        }
    }
}