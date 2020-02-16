package se.lu.humlab.langtrackapp.screen.overview.overviewQuestionViews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.overview_single_multiple_view_layout.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question

class OverviewSingleMultipleView @JvmOverloads constructor(

    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)  {

    init {
        LayoutInflater.from(context).inflate(R.layout.overview_single_multiple_view_layout, this, true)
    }

    fun setText(question: Question){
        //TODO: check answer
        val tempChoice = "Val 4"
        overviewSingleViewTextTextView.text = question.text
        if (question.singleMultipleAnswers != null) {
            for (choice in question.singleMultipleAnswers!!) {
                val  textView = TextView(overviewSingleViewChoices.context)
                textView.text = choice
                if (choice == tempChoice){
                    textView.setTypeface(null, Typeface.BOLD)
                    textView.textSize = 17F
                }
                overviewSingleViewChoices.addView(textView)
            }
        }
    }
}