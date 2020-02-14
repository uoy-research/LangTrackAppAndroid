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
    var questionList = mutableListOf<Question>()
    lateinit var selectedQuestion: Question
    lateinit var headerFragment: HeaderFragment
    lateinit var likertScaleFragment: LikertScaleFragment
    lateinit var fillInTheBlankFragment: FillInTheBlankFragment
    lateinit var multipleChoiceFragment: MultipleChoiceFragment
    lateinit var singleMultipleAnswersFragment: SingleMultipleAnswersFragment
    lateinit var openEndedTextResponsesFragment: OpenEndedTextResponsesFragment
    lateinit var footerFragment: FooterFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val theSurvey = intent.getParcelableExtra<Survey>(SURVEY)

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
            setSurvey(theSurvey)
        }else{
            showErrorPopup()
        }
    }

    fun setSurvey(survey: Survey){
        val temp = survey.questions?.toMutableList()
        if (!temp.isNullOrEmpty()) {
            questionList = temp
            showQuestion(questionList.first().index)
        }else{
            //no questions - close Survey
            showErrorPopup()
        }
    }

    fun showErrorPopup(){
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
            }
        })
        alertPopup.show(alertFm, "alertPopup")
    }

    fun showQuestion(index: Int){
        if (questionList.size > index) {

            for (question in questionList) {
                if (question.index == index) {
                    selectedQuestion = question
                    when (question.type) {

                        HEADER_VIEW -> {
                            headerFragment.question = question
                            loadFragment(headerFragment)
                            headerFragment.setQuestion()
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
                        }
                        SINGLE_MULTIPLE_ANSWERS -> {
                            singleMultipleAnswersFragment.question = question
                            loadFragment(singleMultipleAnswersFragment)
                        }
                        OPEN_ENDED_TEXT_RESPONSES -> {
                            openEndedTextResponsesFragment.question = question
                            loadFragment(openEndedTextResponsesFragment)
                        }
                        FOOTER_VIEW -> {
                            footerFragment.question = question
                            loadFragment(footerFragment)
                        }
                    }
                    return
                }
            }
        }
    }

    companion object {
        val SURVEY = "survey"

        const val HEADER_VIEW = "header"
        const val LIKERT_SCALES = "likert"
        const val FILL_IN_THE_BLANK = "blanks"
        const val MULTIPLE_CHOICE = "multiple"
        const val SINGLE_MULTIPLE_ANSWERS = "single"
        const val OPEN_ENDED_TEXT_RESPONSES = "open"
        const val FOOTER_VIEW = "footer"

        fun start(context: Context, survey: Survey){
            context.startActivity(Intent(context, SurveyContainerActivity::class.java).apply {
                this.putExtra(SURVEY,survey)
            })
        }
    }

    // OnQuestionInteractionListener

    override fun goToNextItem(currentQuestion: Question) {
        showQuestion(index = currentQuestion.next)
    }

    override fun goToPrevoiusItem(currentQuestion: Question) {
        showQuestion(index = currentQuestion.previous)
    }

    override fun cancelSurvey() {
        onBackPressed()
    }

    override fun goToNextItemWithSkipLogic(currentQuestion: Question, nextIndex: Int) {
        //TODO: set next and previous in the questions related
    }

    override fun sendInSurvey(currentQuestion: Question) {
        //TODO: send in answer
        onBackPressed()
    }
}
