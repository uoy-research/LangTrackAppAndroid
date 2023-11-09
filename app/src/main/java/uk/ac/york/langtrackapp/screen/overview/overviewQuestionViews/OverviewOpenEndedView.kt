package uk.ac.york.langtrackapp.screen.overview.overviewQuestionViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
//import kotlinx.android.synthetic.main.overview_open_ended_view_layout.view.*
import uk.ac.york.langtrackapp.R
import uk.ac.york.langtrackapp.data.model.Question
import uk.ac.york.langtrackapp.databinding.OverviewOpenEndedViewLayoutBinding

class OverviewOpenEndedView @JvmOverloads constructor(

    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)  {

    lateinit var binding: OverviewOpenEndedViewLayoutBinding

    init {
        var inflater = LayoutInflater.from(context)
        binding = OverviewOpenEndedViewLayoutBinding.inflate(inflater, this, true)
    }

    fun setText(question: Question, text: String){
        binding.overviewOpenEnteredText.text = text
        binding.overviewOpenTextTextView.text = question.text
    }
}