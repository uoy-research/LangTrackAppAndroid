package se.lu.humlab.langtrackapp.screen.overview.overviewQuestionViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.overview_likert_view_layout.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.data.model.Survey

class OverviewLikertView @JvmOverloads constructor(

    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)  {
    init {
        LayoutInflater.from(context).inflate(R.layout.overview_likert_view_layout, this, true)
    }

    fun setText(question: Question, answer: Int){
        overviewLikertViewDescriptionTextView.text = "${question.likertMin}\n${question.likertMax}"
        overviewLikertViewTextTextView.text = question.text
        setRadiobutton(answer)
    }

    fun setRadiobutton(selected: Int){
        overviewLikertViewRadioButton1.isChecked = false
        overviewLikertViewRadioButton2.isChecked = false
        overviewLikertViewRadioButton3.isChecked = false
        overviewLikertViewRadioButton4.isChecked = false
        overviewLikertViewRadioButton5.isChecked = false
        overviewLikertViewRadioButtonNA.isChecked = false
        when(selected){
            0 -> overviewLikertViewRadioButton1.isChecked = true
            1 -> overviewLikertViewRadioButton2.isChecked = true
            2 -> overviewLikertViewRadioButton3.isChecked = true
            3 -> overviewLikertViewRadioButton4.isChecked = true
            4 -> overviewLikertViewRadioButton5.isChecked = true
            5 -> overviewLikertViewRadioButtonNA.isChecked = true
        }
    }
}