package uk.ac.york.langtrackapp.screen.overview.overviewQuestionViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
//import kotlinx.android.synthetic.main.overview_slider_scale_view_layout.view.*
import uk.ac.york.langtrackapp.R
import uk.ac.york.langtrackapp.data.model.Question

class OverviewSliderScaleView@JvmOverloads constructor(

    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)  {
    init {
        LayoutInflater.from(context).inflate(R.layout.overview_slider_scale_view_layout, this, true)

        //disable checkbox
        overviewSliderScaleCheckboxNA.isActivated = false

        //disable seekbar touch
        overviewSliderSeekBar.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return true
            }
        })
    }

    fun setText(question: Question, answer: Int){
        println("answer: $answer")
        overviewSliderScaleViewTextTextView.text = question.text
        overviewSliderScaleMax.text = question.likertMax
        overviewSliderScaleMin.text = question.likertMin
        overviewSliderScaleValueTextView.text = ""
        if (answer == -1){
            overviewSliderScaleCheckboxNA.isChecked = true
            overviewSliderSeekBar.isEnabled = false
        }else if ((0..100).contains(answer)){
            overviewSliderScaleValueTextView.text = answer.toString()
            overviewSliderScaleCheckboxNA.isChecked = false
            overviewSliderSeekBar.isEnabled = true
            overviewSliderSeekBar.progress = answer
        }else{
            overviewSliderScaleCheckboxNA.isChecked = false
            overviewSliderSeekBar.isEnabled = false
        }
    }

}