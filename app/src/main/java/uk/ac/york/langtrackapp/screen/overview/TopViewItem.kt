package uk.ac.york.langtrackapp.screen.overview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
//import kotlinx.android.synthetic.main.overview_top_view_layout.view.*
import uk.ac.york.langtrackapp.R
import uk.ac.york.langtrackapp.data.model.Assignment
import uk.ac.york.langtrackapp.data.model.Survey
import uk.ac.york.langtrackapp.databinding.OverviewTopViewLayoutBinding

class TopViewItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)  {

    lateinit var binding: OverviewTopViewLayoutBinding

    init {
        var inflater = LayoutInflater.from(context)
        binding = OverviewTopViewLayoutBinding.inflate(inflater, this, true)
    }

    fun setText(assignment: Assignment){
        binding.topViewStatusText.text = "Status"
        //topViewAnsweredDateText.text = if (survey.answer != null) "Besvarad ${getDate(survey.respondeddate)}" else "Obesvarad"
        //topViewSentDateText.text = getDate(survey.date)//TODO: check date
        if (assignment.survey.answer != null){
            binding.overviewStatusImage.setImageResource(R.drawable.lta_icon_ground_light)
        }else {
            binding.overviewStatusImage.setImageResource(R.drawable.lta_icon_unanswered_light)
        }
    }
}