package se.lu.humlab.langtrackapp.screen.overview

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Survey
import se.lu.humlab.langtrackapp.databinding.OverviewActivityBinding
import se.lu.humlab.langtrackapp.screen.overview.overviewQuestionViews.*
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity.Companion.FILL_IN_THE_BLANK
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity.Companion.FOOTER_VIEW
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity.Companion.HEADER_VIEW
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity.Companion.LIKERT_SCALES
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity.Companion.MULTIPLE_CHOICE
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity.Companion.OPEN_ENDED_TEXT_RESPONSES
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity.Companion.SINGLE_MULTIPLE_ANSWERS

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
                unDimBackground()
            }else{
                addTextToTopView()
            }
            topViewIsShowing = !topViewIsShowing
        }

        binding.dimBackgroundView.alpha = 0F
        binding.dimBackgroundView.isClickable = false

        if (theSurvey != null) {
            presentQuestionsInScrollview()
        }
    }

    private fun presentQuestionsInScrollview(){
        for (question in theSurvey!!.questions!!){
            val selectedAnswer = theSurvey!!.answer?.get(question.index)

            when (question.type){
                LIKERT_SCALES -> {
                    val likert = OverviewLikertView(this)
                    //likert.setText(question, selectedAnswer ?: -1)
                    binding.overviewQuestionContainer.addView(likert)
                }
                FILL_IN_THE_BLANK -> {
                    val likert = OverviewFillInBlankView(this)
                    likert.setText(question)
                    binding.overviewQuestionContainer.addView(likert)
                }
                MULTIPLE_CHOICE -> {
                    val likert = OverviewMultipleChoiceView(this)
                    likert.setText(question)
                    binding.overviewQuestionContainer.addView(likert)
                }
                SINGLE_MULTIPLE_ANSWERS -> {
                    val likert = OverviewSingleMultipleView(this)
                    val selectedChoice = if (selectedAnswer != null) {
                        question.singleMultipleAnswers?.get(selectedAnswer) ?: ""
                    }else{
                        null
                    }
                        likert.setText(question, selectedChoice)//TODO: not showing correct answer
                        binding.overviewQuestionContainer.addView(likert)
                }
                OPEN_ENDED_TEXT_RESPONSES -> {
                    val likert = OverviewOpenEndedView(this)
                    likert.setText(question)
                    binding.overviewQuestionContainer.addView(likert)
                }
            }
        }
    }

    fun dimBackgroundClicked(view: View) {
        binding.topView.removeAllViews()
        unDimBackground()
        binding.dimBackgroundView.isClickable = false
        topViewIsShowing = false
    }

    private fun addTextToTopView() {
        val topView1 = TopViewItem(this)
        if (theSurvey != null) {
            topView1.setText(theSurvey!!)
        }
        binding.topView.addView(topView1)
        dimBackground()
        binding.dimBackgroundView.isClickable = true
    }


    fun dimBackground(){
        val anim = ObjectAnimator.ofFloat(binding.dimBackgroundView,
            "alpha",0.75F)
        anim.duration = 300L
        anim.start()
    }

    fun unDimBackground(){
        val anim = ObjectAnimator.ofFloat(binding.dimBackgroundView,
            "alpha",0F)
        anim.duration = 450L
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
