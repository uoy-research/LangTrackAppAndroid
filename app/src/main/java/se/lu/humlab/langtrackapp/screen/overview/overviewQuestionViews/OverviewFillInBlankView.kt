package se.lu.humlab.langtrackapp.screen.overview.overviewQuestionViews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.overview_fill_in_blank_view_layout.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question

class OverviewFillInBlankView @JvmOverloads constructor(

    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)  {

    //TODO: check answer
    val tempChoice = "väljs"

    init {
        LayoutInflater.from(context).inflate(R.layout.overview_fill_in_blank_view_layout, this, true)
    }

    fun setText(question: Question){
        overviewFillBlankTextView.text = question.text
        if (question.fillBlanksChoises != null){
            for (choice in question.fillBlanksChoises!!){
                val textview = TextView(overviewFillBlankChoicesLayout.context)
                textview.text = choice
                if (choice == tempChoice){
                    textview.setTypeface(null, Typeface.BOLD)
                    textview.textSize = 17F
                    textview.setTextColor(resources.getColor(R.color.lta_blue,null))
                }
                overviewFillBlankChoicesLayout.addView(textview)
            }
        }
    }
}