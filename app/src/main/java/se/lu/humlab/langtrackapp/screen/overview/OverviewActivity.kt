package se.lu.humlab.langtrackapp.screen.overview

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
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
import se.lu.humlab.langtrackapp.util.getDate

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
            //changeSizeOfTopView()
            if (topViewIsShowing) {
                binding.topView.removeAllViews()
                unDimBackground(300L)
            }else{
                addTextToTopView()
            }
            topViewIsShowing = !topViewIsShowing
        }

        binding.dimBackgroundView.alpha = 0F
        binding.dimBackgroundView.isClickable = false

        if (theSurvey != null) {

        }
    }

    fun dimBackgroundClicked(view: View) {
        binding.topView.removeAllViews()
        unDimBackground(300L)
        binding.dimBackgroundView.isClickable = false
        topViewIsShowing = false
    }

    private fun addTextToTopView() {
        //TODO: add text with time of response
        val topView1 = TopViewItem(this)
        topView1.setText("Besvarad ${getDate(theSurvey?.respondeddate ?: 0)}")
        binding.topView.addView(topView1)

        var activeText = "Denna enkät är aktiv"
        if (theSurvey?.active == false) {
            activeText = "Denna enkät är inte längre aktiv"
        }
        val topView2 = TopViewItem(this)
        topView2.setText(activeText)
        binding.topView.addView(topView2)
        dimBackground(300L)
        binding.dimBackgroundView.isClickable = true
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
