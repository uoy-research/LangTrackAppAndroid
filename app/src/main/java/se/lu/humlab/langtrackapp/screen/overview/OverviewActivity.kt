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
import se.lu.humlab.langtrackapp.data.model.Answer
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.data.model.Survey
import se.lu.humlab.langtrackapp.databinding.OverviewActivityBinding
import se.lu.humlab.langtrackapp.screen.main.SurveyAdapter
import se.lu.humlab.langtrackapp.screen.overview.overviewQuestionViews.*
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity.Companion.FILL_IN_THE_BLANK
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity.Companion.FOOTER_VIEW
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity.Companion.HEADER_VIEW
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity.Companion.LIKERT_SCALES
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity.Companion.MULTIPLE_CHOICE
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity.Companion.OPEN_ENDED_TEXT_RESPONSES
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity.Companion.SINGLE_MULTIPLE_ANSWERS
import java.lang.Exception

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
        if (!theSurvey!!.questions.isNullOrEmpty()) {
            val questionsWithoutHeaderAndFooter = theSurvey!!.questions!!
                .filter { it.type != HEADER_VIEW &&
                    it.type != FOOTER_VIEW
            }
            for (question in questionsWithoutHeaderAndFooter) {

                val selectedAnswerIndex = if(!theSurvey!!.answer.isNullOrEmpty())
                    theSurvey!!.answer!!.indexOfFirst { it.index == question.index } else null
                var selectedAnswer: Answer? = null
                try {
                    selectedAnswer = theSurvey!!.answer!![selectedAnswerIndex!!]
                } catch (e: Exception){
                    println("presentQuestionsInScrollview, e: ${e.localizedMessage}")}

                when (question.type) {
                    LIKERT_SCALES -> {
                        val likert = OverviewLikertView(this)
                        /*if (selectedAnswer != -1 &&
                                selectedAnswer > 0 &&
                                selectedAnswer <= 5){
                            likert.setText(question, selectedAnswer)
                        }*/
                        binding.overviewQuestionContainer.addView(likert)
                    }
                    FILL_IN_THE_BLANK -> {
                        val likert = OverviewFillInBlankView(this)
                        val selectedWord =
                            if (selectedAnswer != null){
                                try {
                                    question.fillBlanksChoises?.get(selectedAnswer.fillBlankAnswer!!)
                                }catch (e: Exception){null}
                            }  else null
                        likert.setText(question, selectedWord)
                        binding.overviewQuestionContainer.addView(likert)
                    }
                    MULTIPLE_CHOICE -> {
                        val likert = OverviewMultipleChoiceView(this)
                        val templist = mutableListOf<String>()
                        for (wordIndex in selectedAnswer!!.multipleChoiceAnswer!!){
                            try {
                                templist.add(question.multipleChoisesAnswers?.get(wordIndex)!!)
                            }catch (e: Exception){
                                println("presentQuestionsInScrollview, e: ${e.localizedMessage}")
                            }
                        }
                        likert.setText(question, templist)
                        binding.overviewQuestionContainer.addView(likert)
                    }
                    SINGLE_MULTIPLE_ANSWERS -> {
                        val likert = OverviewSingleMultipleView(this)
                        var theChoice: String? = null
                        if (selectedAnswer?.singleMultipleAnswer != null &&
                            question.singleMultipleAnswers != null) {
                            theChoice =
                                question.singleMultipleAnswers!!.get(selectedAnswer.singleMultipleAnswer!!)
                        }
                        likert.setText(question, theChoice)
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
