package se.lu.humlab.langtrackapp.screen.overview

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.overview_activity.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Survey
import se.lu.humlab.langtrackapp.databinding.OverviewActivityBinding

class OverviewActivity : AppCompatActivity() {

    lateinit var binding: OverviewActivityBinding
    private lateinit var viewModel :OverviewViewModel
    var topViewIsShowing = false
    var theSurvey: Survey? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.overview_activity)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        viewModel = ViewModelProviders.of(this,
            OverviewViewModelFactory(this)
        ).get(OverviewViewModel::class.java)
        theSurvey = intent.getParcelableExtra(SURVEY)
        binding.overviewText.text = theSurvey?.title ?: "hej"

        binding.overviewText.setOnClickListener {
            changeSizeOfTopView()
        }

        binding.dimBackgroundView.alpha = 0F
        binding.dimBackgroundView.isClickable = false

        addTextToTopView()
    }

    fun dimBackgroundClicked(view: View){
        changeSizeOfTopView()
    }

    private fun addTextToTopView() {
        //TODO: add text with time of response
        val respondedText = if (theSurvey?.responded == true)
            "Denna enkät är besvarad" else "Denna enkät är inte besvarad"
        val textview = TextView(this)
        textview.textSize = 18F
        textview.setTextColor(getColor(R.color.lta_text))
        textview.text = respondedText
        binding.topView.addView(textview)
    }

    fun pxToDp(px: Int): Int{
        val density = resources.displayMetrics.density
        return Math.round(px.toFloat() / density)
    }

    fun dpToPx(dp: Int): Int{
        val density = resources.displayMetrics.density
        return Math.round(dp.toFloat() * density)
    }

    fun changeSizeOfTopView(){

        val time = 300L

        if (topViewIsShowing){
            unDimBackground(time)
            val param = topView.layoutParams
            val anim = ValueAnimator.ofInt(pxToDp(param.height), 1)
            anim.addUpdateListener {
                val values = it.animatedValue as Int
                param.height = dpToPx(values)
                topView.layoutParams = param
            }
            anim.duration = time
            anim.start()
            topViewIsShowing = false
            binding.dimBackgroundView.isClickable = false
        }else {
            dimBackground(time)
            val param = topView.layoutParams
            val anim = ValueAnimator.ofInt(pxToDp(param.height), 150)
            anim.addUpdateListener {
                val values = it.animatedValue as Int
                param.height = dpToPx(values)
                topView.layoutParams = param
            }
            anim.duration = time
            anim.start()
            anim.doOnEnd {
                println("animating finished")
            }
            topViewIsShowing = true
            binding.dimBackgroundView.isClickable = true
        }
    }

    fun dimBackground(time:Long){
        val anim = ObjectAnimator.ofFloat(binding.dimBackgroundView,
            "alpha",0.75F)
        anim.duration = time
        anim.start()
    }

    fun unDimBackground(time: Long){
        val anim = ObjectAnimator.ofFloat(binding.dimBackgroundView,
            "alpha",0F)
        anim.duration = time
        anim.start()
    }


    companion object {
        const val SURVEY = "overviewsurvey"

        fun start(context: Context, survey: Survey) {
            context.startActivity(Intent(context, OverviewActivity::class.java).apply {
                this.putExtra(SURVEY, survey)
            })
        }
    }
}
