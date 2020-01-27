package se.lu.humlab.langtrackapp.screen.surveyContainer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.SurveyContainerActivityBinding
import se.lu.humlab.langtrackapp.interfaces.*
import se.lu.humlab.langtrackapp.screen.survey.SurveyAdapter
import se.lu.humlab.langtrackapp.screen.surveyContainer.fillInTheBlankFragment.FillInTheBlankFragment
import se.lu.humlab.langtrackapp.screen.surveyContainer.footerFragment.FooterFragment
import se.lu.humlab.langtrackapp.screen.surveyContainer.header.HeaderFragment
import se.lu.humlab.langtrackapp.screen.surveyContainer.likertScaleFragment.LikertScaleFragment
import se.lu.humlab.langtrackapp.screen.surveyContainer.multipleChoiceFragment.MultipleChoiceFragment
import se.lu.humlab.langtrackapp.screen.surveyContainer.openEndedTextResponsesFragment.OpenEndedTextResponsesFragment
import se.lu.humlab.langtrackapp.screen.surveyContainer.singleMultipleAnswersFragment.SingleMultipleAnswersFragment
import se.lu.humlab.langtrackapp.util.loadFragment

class SurveyContainerActivity : AppCompatActivity(),
    OnHeaderInteractionListener,
    OnLikertScaleInteraktionListener,
    OnFillInBlankInteractionListener,
    OnMultipleChoiceInteractionListener,
    OnSingleMultipleInteractionListener,
    OnOpenEndedInteractionListener,
    OnFooterInteractionListener{


    private lateinit var mBind : SurveyContainerActivityBinding
    private lateinit var viewModel : SurveyContainerViewModel
    var questionList = mutableListOf<Question>()
    lateinit var headerFragment: HeaderFragment
    lateinit var likertScaleFragment: LikertScaleFragment
    lateinit var fillInTheBlankFragment: FillInTheBlankFragment
    lateinit var multipleChoiceFragment: MultipleChoiceFragment
    lateinit var singleMultipleAnswersFragment: SingleMultipleAnswersFragment
    lateinit var openEndedTextResponsesFragment: OpenEndedTextResponsesFragment
    lateinit var footerFragment: FooterFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        //TODO: här kommer json och görs om till survey
        val q0 = Question(
            type = SurveyAdapter.HEADER_VIEW,
            id = "id",
            previous = 0,
            index = 0,
            next = 1,
            title = "THE LANG-TRACK-APP\nHumanistlaboratoriet",
            text = "Hej (användarnamn)\n\nNu är det dags att svara på frågor om din språkinlärning!",
            description = ""
            )
        questionList.add(q0)
        val q1 = Question(
            type = SurveyAdapter.LIKERT_SCALES,
            id = "id",
            previous = 0,
            index = 1,
            next = 2,
            title = "LikertScale titel",
            text = "Här skrivs ett påstående som deltagaren graderar hur mycket det stämmer",
            description = "Hur mycket stämmer följande påstående?\n1: stämmer inte alls\n5: stämmer helt"
        )
        questionList.add(q1)
        val q2 = Question(
            type = SurveyAdapter.FILL_IN_THE_BLANK,
            id = "id",
            previous = 1,
            index = 2,
            next = 3,
            title = "FillInTheBlank titel",
            text = "Här är texten i FillInTheBlank",
            description = ""
        )
        questionList.add(q2)
        val q3 = Question(
            type = SurveyAdapter.MULTIPLE_CHOICE,
            id = "id",
            previous = 2,
            index = 3,
            next = 4,
            title = "MultipleChoise titel",
            text = "Här är texten i MultipleChoise",
            description = ""
        )
        questionList.add(q3)
        val q4 = Question(
            type = SurveyAdapter.SINGLE_MULTIPLE_ANSWERS,
            id = "id",
            previous = 3,
            index = 4,
            next = 5,
            title = "SingleMultipleAnswer titel",
            text = "Här är texten i SingleMultipleAnswer",
            description = ""
        )
        questionList.add(q4)
        val q5 = Question(
            type = SurveyAdapter.OPEN_ENDED_TEXT_RESPONSES,
            id = "id",
            previous = 4,
            index = 5,
            next = 6,
            title = "OpenEndedTextResponses titel",
            text = "Här är texten i OpenEndedTextResponses",
            description = ""
        )
        questionList.add(q5)
        val q6 = Question(
            type = SurveyAdapter.FOOTER_VIEW,
            id = "id",
            previous = 5,
            index = 6,
            next = 0,
            title = "Tack för dina svar.",
            text = "Om du är nöjd med dina svar välj 'Skicka in'\nannars kan du stega bak för att redigera.",
            description = ""
        )
        questionList.add(q6)

        questionList.sortByDescending { it.index }

        showQuestion(0)
    }

    fun showQuestion(index: Int){
        if (questionList.size > index) {

            for (question in questionList) {
                if (question.index == index) {
                    when (question.type) {

                        SurveyAdapter.HEADER_VIEW -> {
                            headerFragment.question = question
                            loadFragment(headerFragment)
                            headerFragment.setQuestion()
                        }
                        SurveyAdapter.LIKERT_SCALES -> {
                            likertScaleFragment.question = question
                            loadFragment(likertScaleFragment)
                            likertScaleFragment.setQuestion()
                        }
                        SurveyAdapter.FILL_IN_THE_BLANK -> {
                            fillInTheBlankFragment.question = question
                            loadFragment(fillInTheBlankFragment)
                            fillInTheBlankFragment.setQuestion()
                        }
                        SurveyAdapter.MULTIPLE_CHOICE -> {
                            multipleChoiceFragment.question = question
                            loadFragment(multipleChoiceFragment)
                        }
                        SurveyAdapter.SINGLE_MULTIPLE_ANSWERS -> {
                            singleMultipleAnswersFragment.question = question
                            loadFragment(singleMultipleAnswersFragment)
                        }
                        SurveyAdapter.OPEN_ENDED_TEXT_RESPONSES -> {
                            openEndedTextResponsesFragment.question = question
                            loadFragment(openEndedTextResponsesFragment)
                        }
                        SurveyAdapter.FOOTER_VIEW -> {
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
        fun start(context: Context){
            context.startActivity(Intent(context, SurveyContainerActivity::class.java))
        }
    }

    //OnHeaderInteractionListener
    override fun headerGoToNextItem(currentQuestion: Question) {
        println("SurveyContainerActivity participantClickedStart")
        showQuestion(index = currentQuestion.next)
    }

    override fun headerCancelPressed(currentQuestion: Question) {
        println("SurveyContainerActivity participantClickedCancel")
        onBackPressed()
    }

    //OnLikertScaleInteraktionListener
    override fun likertScaleGoToNextItem(currentQuestion: Question) {
        println("SurveyContainerActivity likertScaleGoToNextItem")
        showQuestion(index = currentQuestion.next)
    }

    override fun likertScaleGoToPrevoiusItem(currentQuestion: Question) {
        println("SurveyContainerActivity likertScaleGoToPrevoiusItem")
        showQuestion(index = currentQuestion.previous)
    }

    override fun fillInBlankGoToNextItem(currentQuestion: Question) {
        println("SurveyContainerActivity fillInBlankGoToNextItem")
        showQuestion(index = currentQuestion.next)
    }

    override fun fillInBlankGoToPrevoiusItem(currentQuestion: Question) {
        println("SurveyContainerActivity fillInBlankGoToPrevoiusItem")
        showQuestion(index = currentQuestion.previous)
    }

    override fun multipleChoiceGoToNextItem(currentQuestion: Question) {
        println("SurveyContainerActivity multipleChoiceGoToNextItem")
        showQuestion(index = currentQuestion.next)
    }

    override fun multipleChoiceGoToPrevoiusItem(currentQuestion: Question) {
        println("SurveyContainerActivity multipleChoiceGoToPrevoiusItem")
        showQuestion(index = currentQuestion.previous)
    }

    override fun singleMultipleGoToNextItem(currentQuestion: Question) {
        println("SurveyContainerActivity singleMultipleGoToNextItem")
        showQuestion(index = currentQuestion.next)
    }

    override fun singleMultipleGoToPrevoiusItem(currentQuestion: Question) {
        println("SurveyContainerActivity singleMultipleGoToPrevoiusItem")
        showQuestion(index = currentQuestion.previous)
    }

    override fun openEndedGoToNextItem(currentQuestion: Question) {
        println("SurveyContainerActivity openEndedGoToNextItem")
        showQuestion(index = currentQuestion.next)
    }

    override fun openEndedGoToPrevoiusItem(currentQuestion: Question) {
        println("SurveyContainerActivity openEndedGoToPrevoiusItem")
        showQuestion(index = currentQuestion.previous)
    }

    override fun footerGoToPreviousItem(currentQuestion: Question) {
        println("SurveyContainerActivity footerGoToPreviousItem")
        showQuestion(index = currentQuestion.previous)
    }

    override fun footerSendInSurvey(currentQuestion: Question) {
        println("SurveyContainerActivity footerSendInSurvey")
        onBackPressed()
    }
}
