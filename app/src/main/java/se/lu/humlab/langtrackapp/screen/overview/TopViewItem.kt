package se.lu.humlab.langtrackapp.screen.overview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.overview_top_view_layout.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Survey

class TopViewItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)  {


    init {
        LayoutInflater.from(context).inflate(R.layout.overview_top_view_layout, this, true)
    }

    fun setText(text: String){
        topViewItemText1.text = text
    }
}