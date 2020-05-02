package se.lu.humlab.langtrackapp.screen.overview.overviewQuestionViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.overview_time_duration_view_layout.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Answer
import se.lu.humlab.langtrackapp.data.model.Question

class OverviewTimDurationView  @JvmOverloads constructor(


    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    lateinit var question: Question
    var answer : Answer? = null
    private var answeredHour = 0
    private var answeredMinutes = 0


    init {
        LayoutInflater.from(context).inflate(R.layout.overview_time_duration_view_layout, this, true)
    }

    fun setText(question: Question, answer: Answer?) {
        this.question = question
        this.answer = answer
        setHoursAndMinutes()
        overviewDurationViewTextTextView.text = question.text

        if (answeredMinutes == 0 && answeredHour == 0){
            overviewDurationViewSelectedTextView.text = "0:0"
        }else if (answeredMinutes == 0){
            if (answeredHour == 1){
                overviewDurationViewSelectedTextView.text = "$answeredHour timme"
            }else{
                overviewDurationViewSelectedTextView.text = "$answeredHour timmar"
            }
        }else{
            if (answeredHour == 0){
                overviewDurationViewSelectedTextView.text = "$answeredMinutes minuter"
            }else{
                if (answeredHour == 1){
                    overviewDurationViewSelectedTextView.text = "$answeredHour timme och $answeredMinutes minuter"
                }else{
                    overviewDurationViewSelectedTextView.text = "$answeredHour timmar och $answeredMinutes minuter"
                }
            }
        }
    }

    fun setHoursAndMinutes(){
        if (answer != null) {
            if (answer!!.timeDurationAnswer ?: 0 != 0) {
                answeredHour = answer!!.timeDurationAnswer!! / (60 * 60)
                answeredMinutes = (answer!!.timeDurationAnswer!! - (answeredHour * 60 * 60)) / 60
            }
        }
    }
}