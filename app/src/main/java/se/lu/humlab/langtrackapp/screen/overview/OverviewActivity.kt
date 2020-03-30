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
import se.lu.humlab.langtrackapp.data.model.Assignment
import se.lu.humlab.langtrackapp.data.model.Survey
import se.lu.humlab.langtrackapp.databinding.OverviewActivityBinding
import se.lu.humlab.langtrackapp.screen.overview.overviewQuestionViews.*
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
    var theAssignment: Assignment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.overview_activity)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        viewModel = ViewModelProviders.of(this,
            OverviewViewModelFactory(this)
        ).get(OverviewViewModel::class.java)
        theAssignment = intent.getParcelableExtra(ASSIGNMENT)
        binding.overviewText.text = theAssignment?.survey?.title ?: "hej"

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

        if (theAssignment != null) {
            presentQuestionsInScrollview()
        }
    }

    private fun presentQuestionsInScrollview(){
        if (!theAssignment!!.survey.questions.isNullOrEmpty()) {
            val questionsWithoutHeaderAndFooter = theAssignment!!.survey.questions!!
                .filter { it.type != HEADER_VIEW &&
                    it.type != FOOTER_VIEW
            }
            for (question in questionsWithoutHeaderAndFooter) {

                val selectedAnswerIndex = if(!theAssignment!!.dataset?.answers.isNullOrEmpty())
                    theAssignment!!.dataset!!.answers.indexOfFirst { it.index == question.index } else null
                var selectedAnswer: Answer? = null
                try {
                    selectedAnswer = theAssignment!!.dataset!!.answers[selectedAnswerIndex!!]
                } catch (e: Exception){
                    println("presentQuestionsInScrollview, e: ${e.localizedMessage}")}

                when (question.type) {
                    LIKERT_SCALES -> {
                        val likert = OverviewLikertView(this)
                        if (selectedAnswer?.likertAnswer ?: -1 != -1 &&
                                selectedAnswer?.likertAnswer ?: -1 >= 0 &&
                                selectedAnswer?.likertAnswer ?: -1 < 5){
                            likert.setText(question, selectedAnswer?.likertAnswer!!)
                        }
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
                        if (selectedAnswer?.multipleChoiceAnswer != null){
                            for (wordIndex in selectedAnswer.multipleChoiceAnswer!!) {
                                try {
                                    templist.add(question.multipleChoisesAnswers?.get(wordIndex)!!)
                                } catch (e: Exception) {
                                    println("presentQuestionsInScrollview, e: ${e.localizedMessage}")
                                }
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
                            try {
                                theChoice =
                                    question.singleMultipleAnswers!!.get(selectedAnswer.singleMultipleAnswer!!)
                            }catch (e: Exception){println("presentQuestionsInScrollview, e: ${e.localizedMessage}")}

                        }
                        likert.setText(question, theChoice)
                        binding.overviewQuestionContainer.addView(likert)
                    }
                    OPEN_ENDED_TEXT_RESPONSES -> {
                        val likert = OverviewOpenEndedView(this)
                        var theText = ""
                        if (selectedAnswer?.openEndedAnswer != null) {
                            try {
                                theText = selectedAnswer.openEndedAnswer ?: ""
                            }catch (e: Exception){println("presentQuestionsInScrollview, e: ${e.localizedMessage}")}
                        }
                        likert.setText(question, theText)
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
        if (theAssignment != null) {
            topView1.setText(theAssignment!!)
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
        const val ASSIGNMENT = "overviewsurvey"

        fun start(context: Context, assignment: Assignment) {
            context.startActivity(Intent(context, OverviewActivity::class.java).apply {
                this.putExtra(ASSIGNMENT, assignment)
            })
        }
    }
}
