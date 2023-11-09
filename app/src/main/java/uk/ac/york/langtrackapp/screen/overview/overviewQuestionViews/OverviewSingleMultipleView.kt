package uk.ac.york.langtrackapp.screen.overview.overviewQuestionViews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
//import kotlinx.android.synthetic.main.overview_single_multiple_view_layout.view.*
import uk.ac.york.langtrackapp.R
import uk.ac.york.langtrackapp.data.model.Question
import uk.ac.york.langtrackapp.databinding.OverviewSingleMultipleViewLayoutBinding

class OverviewSingleMultipleView @JvmOverloads constructor(

    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)  {

    lateinit var binding: OverviewSingleMultipleViewLayoutBinding

    init {
        var inflater = LayoutInflater.from(context)
        binding = OverviewSingleMultipleViewLayoutBinding.inflate(inflater, this, true)
    }

    fun setText(question: Question, answer: String?){
        binding.overviewSingleViewTextTextView.text = question.text
        if (question.singleMultipleAnswers != null) {
            for (choice in question.singleMultipleAnswers!!) {
                val  textView = RadioButton(binding.overviewSingleViewChoices.context)
                textView.text = choice
                if (choice == answer){
                    textView.textSize = 17F
                    textView.setTextColor(resources.getColor(R.color.lta_blue,null))
                    textView.isChecked = true
                }else{
                    textView.textSize = 15F
                    textView.setTextColor(resources.getColor(R.color.lta_text,null))
                    textView.isChecked = false
                }
                textView.isClickable = false
                binding.overviewSingleViewChoices.addView(textView)
            }
        }
    }
}