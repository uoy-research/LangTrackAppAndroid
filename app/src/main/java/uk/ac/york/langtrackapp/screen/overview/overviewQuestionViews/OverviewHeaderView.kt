package uk.ac.york.langtrackapp.screen.overview.overviewQuestionViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
//import kotlinx.android.synthetic.main.overview_header.view.*
import uk.ac.york.langtrackapp.R
import uk.ac.york.langtrackapp.data.model.Question
import uk.ac.york.langtrackapp.databinding.OverviewFillInBlankViewLayoutBinding
import uk.ac.york.langtrackapp.databinding.OverviewHeaderBinding

class OverviewHeaderView @JvmOverloads constructor(

    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)  {

    lateinit var binding: OverviewHeaderBinding

    init {
        var inflater = LayoutInflater.from(context)
        binding = OverviewHeaderBinding.inflate(inflater, this, true)
    }

    fun setText(question: Question){
        binding.overviewHeaderTextView.text = question.text
    }
}