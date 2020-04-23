package se.lu.humlab.langtrackapp.screen.overview.overviewQuestionViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.overview_multiple_choice_view_layout.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question

class OverviewMultipleChoiceView @JvmOverloads constructor(

    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)  {

    init {
        LayoutInflater.from(context).inflate(R.layout.overview_multiple_choice_view_layout, this, true)
    }

    fun setText(question: Question, choices: List<String>){
        overviewMultipleViewTextTextView.text = question.text
        if (question.multipleChoisesAnswers != null) {
            for (answer in question.multipleChoisesAnswers!!) {
                val checkbox = CheckBox(overviewMultipleViewChoices.context)
                checkbox.text = answer
                checkbox.isClickable = false
                if (choices.contains(answer)){
                    checkbox.textSize = 17F
                    checkbox.setTextColor(resources.getColor(R.color.lta_blue,null))
                    checkbox.isChecked = true
                }else{
                    checkbox.textSize = 15F
                    checkbox.setTextColor(resources.getColor(R.color.lta_text,null))
                    checkbox.isChecked = false
                }
                overviewMultipleViewChoices.addView(checkbox)
            }
        }
    }
}