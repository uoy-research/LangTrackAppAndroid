package se.lu.humlab.langtrackapp.screen.surveyContainer

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.survey_container_activity.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Assignment
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.data.model.Survey
import se.lu.humlab.langtrackapp.databinding.SurveyContainerActivityBinding
import se.lu.humlab.langtrackapp.interfaces.*
import se.lu.humlab.langtrackapp.popup.PopupAlert
import se.lu.humlab.langtrackapp.screen.surveyContainer.fillInTheBlankFragment.FillInTheBlankFragment
import se.lu.humlab.langtrackapp.screen.surveyContainer.footerFragment.FooterFragment
import se.lu.humlab.langtrackapp.screen.surveyContainer.header.HeaderFragment
import se.lu.humlab.langtrackapp.screen.surveyContainer.likertScaleFragment.LikertScaleFragment
import se.lu.humlab.langtrackapp.screen.surveyContainer.multipleChoiceFragment.MultipleChoiceFragment
import se.lu.humlab.langtrackapp.screen.surveyContainer.openEndedTextResponsesFragment.OpenEndedTextResponsesFragment
import se.lu.humlab.langtrackapp.screen.surveyContainer.singleMultipleAnswersFragment.SingleMultipleAnswersFragment
import se.lu.humlab.langtrackapp.util.loadFragment

class SurveyContainerActivity : AppCompatActivity(),
    OnQuestionInteractionListener{


    private lateinit var mBind : SurveyContainerActivityBinding
    private lateinit var viewModel : SurveyContainerViewModel
    private var questionList = mutableListOf<Question>()
    private lateinit var selectedQuestion: Question
    private lateinit var headerFragment: HeaderFragment
    private lateinit var likertScaleFragment: LikertScaleFragment
    private lateinit var fillInTheBlankFragment: FillInTheBlankFragment
    private lateinit var multipleChoiceFragment: MultipleChoiceFragment
    private lateinit var singleMultipleAnswersFragment: SingleMultipleAnswersFragment
    private lateinit var openEndedTextResponsesFragment: OpenEndedTextResponsesFragment
    private lateinit var footerFragment: FooterFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val theSurvey = intent.getParcelableExtra<Assignment>(ASSIGNMENT)

        mBind = DataBindingUtil.setContentView(this, R.layout.survey_container_activity)
        mBind.lifecycleOwner = this
        mBind.executePendingBindings()

        viewModel = ViewModelProviders.of(this,
            SurveyContainerViewModelFactory(this)
        ).get(SurveyContainerViewModel::class.java)
        mBind.viewModel = viewModel

        //create fragments
        headerFragment = HeaderFragment.newInstance()
        likertScaleFragment = LikertScaleFragment.newInstance()
        fillInTheBlankFragment = FillInTheBlankFragment.newInstance()
        multipleChoiceFragment = MultipleChoiceFragment.newInstance()
        singleMultipleAnswersFragment = SingleMultipleAnswersFragment.newInstance()
        openEndedTextResponsesFragment = OpenEndedTextResponsesFragment.newInstance()
        footerFragment = FooterFragment.newInstance()

        if (theSurvey != null){
            setSurvey(theSurvey!!)
        }else{
            showErrorPopup()
        }
    }

    private fun setSurvey(assignment: Assignment){
        val temp = assignment.survey.questions?.toMutableList()
        if (!temp.isNullOrEmpty()) {
            questionList = temp
            showQuestion(questionList.first().index)
        }else{
            //no questions - close Survey
            showErrorPopup()
        }
    }

    private fun showErrorPopup(){
        val alertFm = supportFragmentManager.beginTransaction()
        val width = (surveyContainer_layout.measuredWidth * 0.75).toInt()
        val alertPopup = PopupAlert.show(
            width = width,
            title = "Något gick fel!",
            textViewText = "Det går tyvärr inte att visa detta formulär.\nMeddelande skickat till humanist lab...",
            placecenter = true
        )
        alertPopup.setCompleteListener(object : OnBoolPopupReturnListener{
            override fun popupReturn(value: Boolean) {
                onBackPressed()
                //TODO: send info to backend
            }
        })
        alertPopup.show(alertFm, "alertPopup")
    }

    private fun showQuestion(index: Int){
        if (questionList.size > index) {

            for (question in questionList) {
                if (question.index == index) {
                    selectedQuestion = question
                    when (question.type) {

                        HEADER_VIEW -> {
                            headerFragment.question = question
                            loadFragment(headerFragment)
                            headerFragment.setText()
                        }
                        LIKERT_SCALES -> {
                            likertScaleFragment.question = question
                            loadFragment(likertScaleFragment)
                            likertScaleFragment.setQuestion()
                        }
                        FILL_IN_THE_BLANK -> {
                            fillInTheBlankFragment.question = question
                            loadFragment(fillInTheBlankFragment)
                            fillInTheBlankFragment.setQuestion()
                        }
                        MULTIPLE_CHOICE -> {
                            multipleChoiceFragment.question = question
                            loadFragment(multipleChoiceFragment)
                            multipleChoiceFragment.setQuestion()
                        }
                        SINGLE_MULTIPLE_ANSWERS -> {
                            singleMultipleAnswersFragment.question = question
                            loadFragment(singleMultipleAnswersFragment)
                            singleMultipleAnswersFragment.setQuestion(this)
                        }
                        OPEN_ENDED_TEXT_RESPONSES -> {
                            openEndedTextResponsesFragment.question = question
                            loadFragment(openEndedTextResponsesFragment)
                            openEndedTextResponsesFragment.setQuestion()
                        }
                        FOOTER_VIEW -> {
                            footerFragment.question = question
                            loadFragment(footerFragment)
                            footerFragment.setText()
                        }
                    }
                    return
                }
            }
        }
    }

    companion object {
        const val ASSIGNMENT = "assignment"
        const val HEADER_VIEW = "header"
        const val LIKERT_SCALES = "likert"
        const val FILL_IN_THE_BLANK = "blanks"
        const val MULTIPLE_CHOICE = "multi"
        const val SINGLE_MULTIPLE_ANSWERS = "single"
        const val OPEN_ENDED_TEXT_RESPONSES = "open"
        const val FOOTER_VIEW = "footer"

        fun start(context: Context, assignment: Assignment){
            context.startActivity(Intent(context, SurveyContainerActivity::class.java).apply {
                this.putExtra(ASSIGNMENT,assignment)
            })
        }
    }

    private fun resetPreviousOfQuestions(){
        for ((index, question) in questionList.withIndex()){
            if (index == 0){
                question.previous = index
            }else question.previous = index - 1
        }
    }

    // OnQuestionInteractionListener

    override fun goToNextItem(currentQuestion: Question) {
        showQuestion(index = currentQuestion.next)
    }

    override fun goToPrevoiusItem(currentQuestion: Question) {
        showQuestion(index = currentQuestion.previous)
        if (currentQuestion.previous < currentQuestion.index - 1) {
            resetPreviousOfQuestions()
        }
    }

    override fun cancelSurvey() {
        onBackPressed()
    }

    override fun goToNextItemWithSkipLogic(currentQuestion: Question) {
        for (question in questionList){
            if (question.index == currentQuestion.skip!!.goto){
                question.previous = currentQuestion.index
            }
        }
        showQuestion(index = currentQuestion.skip!!.goto)
    }

    override fun sendInSurvey(currentQuestion: Question) {
        //TODO: send in answer
        onBackPressed()
    }
}
