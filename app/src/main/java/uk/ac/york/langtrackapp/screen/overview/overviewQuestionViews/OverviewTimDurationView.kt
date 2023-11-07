package uk.ac.york.langtrackapp.screen.overview.overviewQuestionViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
//import kotlinx.android.synthetic.main.overview_time_duration_view_layout.view.*
import uk.ac.york.langtrackapp.R
import uk.ac.york.langtrackapp.data.model.Answer
import uk.ac.york.langtrackapp.data.model.Question
import uk.ac.york.langtrackapp.util.formatStringWithArabicNumbers

class OverviewTimDurationView  @JvmOverloads constructor(


    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    lateinit var question: Question
    var answer : Answer? = null
    private var answeredHour = 0
    private var answeredMinutes = 0


    init {
        LayoutInflater.from(context)
            .inflate(R.layout.overview_time_duration_view_layout, this, true)
    }

    fun setText(question: Question, answer: Answer?) {
        this.question = question
        this.answer = answer
        setHoursAndMinutes()
        overviewDurationViewTextTextView.text = question.text

        if (answeredMinutes == 0 && answeredHour == 0){
            overviewDurationViewSelectedTextView.text = context.getString(R.string.noDuration)
        }else if (answeredMinutes == 0){
            if (answeredHour == 1){
                overviewDurationViewSelectedTextView.text =
                    context.getString(R.string.oneHour, answeredHour)
            }else{
                overviewDurationViewSelectedTextView.text =
                    context.getString(R.string.manyHours, answeredHour)
            }
        }else{
            if (answeredHour == 0){
                overviewDurationViewSelectedTextView.text =
                    context.getString(R.string.numberOfMinutes, answeredMinutes)
            }else{
                if (answeredHour == 1){
                    overviewDurationViewSelectedTextView.text =
                        context.getString(R.string.numberOfHourAndMinutes, answeredHour, answeredMinutes)
                    //resources.formatStringWithArabicNumbers(R.string.numberOfHourAndMinutes, answeredHour, answeredMinutes)
                }else{
                    overviewDurationViewSelectedTextView.text =
                        context.getString(R.string.numberOfHoursAndMinutes, answeredHour, answeredMinutes)
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