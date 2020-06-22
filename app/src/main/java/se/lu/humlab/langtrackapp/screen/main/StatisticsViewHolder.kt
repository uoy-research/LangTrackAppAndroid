package se.lu.humlab.langtrackapp.screen.main

import android.content.res.ColorStateList
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.util.dpToPx
import kotlin.math.round


class StatisticsViewHolder(theItemView: View) : RecyclerView.ViewHolder(theItemView) {

    private var cellLayout: ConstraintLayout = itemView.findViewById(R.id.statisticsCellLayout)
    private var progressBar: ProgressBar = itemView.findViewById(R.id.statisticProgressbar)
    private var progressBackgroundBar: ProgressBar = itemView.findViewById(R.id.statisticBackgroundProgressbar)
    private var percentText: TextView = itemView.findViewById(R.id.statisticPercentTextView)
    private var statisticsText: TextView = itemView.findViewById(R.id.statisticsTextView)
    private var statisticsEmojiText: TextView = itemView.findViewById(R.id.statisticsEmojiTextView)

    fun bind(answered: Int, unanswered: Int){
        setChartAndEmoji(answered,unanswered)
        statisticsText.text = "Du har besvarat $answered av dina ${answered + unanswered} enkäter"
    }

    private fun setChartAndEmoji(answered: Int, unanswered: Int){
        if (unanswered != 0) {
            val percent = 100 * (answered.toDouble() / (answered + unanswered).toDouble())
            val percentRounded = round(10 * percent) / 10
            percentText.text = "$percentRounded%"
            if (percentRounded >= 90){
                statisticsEmojiText.text = "🌟"
                setColor("green")
            }else if (percentRounded >= 80){
                statisticsEmojiText.text = "😁"
                setColor("green")
            }else if (percentRounded >= 50){
                statisticsEmojiText.text = "👍"
                setColor("yellow")
            }else if (percentRounded >= 25){
                statisticsEmojiText.text = "😏"
                setColor("brown")
            }else{
                statisticsEmojiText.text = "😬"
                setColor("gray")
            }
            progressBar.progress = percentRounded.toInt()
        }
    }
    fun setColor(color: String){
        var theColor = statisticsEmojiText.context.getColor(R.color.lta_green)
        when (color){
            "green" -> theColor = statisticsEmojiText.context.getColor(R.color.lta_green)
            "yellow" -> theColor = statisticsEmojiText.context.getColor(R.color.lta_yellow)
            "brown" -> theColor = statisticsEmojiText.context.getColor(R.color.lta_brown)
            "gray" -> theColor = statisticsEmojiText.context.getColor(R.color.lta_light_grey)
        }
        progressBar.progressTintList = ColorStateList.valueOf(theColor)
    }

    companion object {
        fun newInstance(parent: ViewGroup):StatisticsViewHolder{
            return StatisticsViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.recycle_statistics_row,
                    parent,
                    false
                )
            )
        }
    }
}