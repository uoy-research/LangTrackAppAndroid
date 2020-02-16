package se.lu.humlab.langtrackapp.screen.overview.overviewQuestionViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question

class OverviewFillInBlankView @JvmOverloads constructor(

    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)  {

    init {
        LayoutInflater.from(context).inflate(R.layout.overview_fill_in_blank_view_layout, this, true)
    }

    fun setText(question: Question){

    }
}