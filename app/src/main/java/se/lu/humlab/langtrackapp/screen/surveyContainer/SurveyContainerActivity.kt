package se.lu.humlab.langtrackapp.screen.surveyContainer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.SurveyContainerActivityBinding
import se.lu.humlab.langtrackapp.interfaces.OnHeaderInteractionListener
import se.lu.humlab.langtrackapp.interfaces.OnLikertScaleInteraktionListener
import se.lu.humlab.langtrackapp.screen.survey.SurveyAdapter
import se.lu.humlab.langtrackapp.screen.surveyContainer.header.HeaderFragment
import se.lu.humlab.langtrackapp.screen.surveyContainer.likertScale.LikertScaleFragment
import se.lu.humlab.langtrackapp.util.loadFragment

class SurveyContainerActivity : AppCompatActivity(),
    OnHeaderInteractionListener,
    OnLikertScaleInteraktionListener {


    private lateinit var mBind : SurveyContainerActivityBinding
    private lateinit var viewModel : SurveyContainerViewModel
    var questionList = mutableListOf<Question>()
    var theSurvey = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBind = DataBindingUtil.setContentView(this, R.layout.survey_container_activity)
        mBind.lifecycleOwner = this
        mBind.executePendingBindings()

        viewModel = ViewModelProviders.of(this,
            SurveyContainerViewModelFactory(this)
        ).get(SurveyContainerViewModel::class.java)
        mBind.viewModel = viewModel

        //TODO: här kommer json och görs om till survey
        val q0 = Question(
            type = SurveyAdapter.HEADER_VIEW,
            id = "id",
            previous = 0,
            index = 0,
            next = 1,
            title = "Header titel",
            text = "Här är texten i header",
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
            text = "Här är texten i LikertScale",
            description = ""
        )
        questionList.add(q1)

        //här skapas fragments och läggs in i surveylist
        for (question in questionList){
            when (question.type){

                SurveyAdapter.HEADER_VIEW -> {
                    var headerFragment = HeaderFragment.newInstance(question = question)
                    println("question.title: ${question.title}")
                    theSurvey.add(headerFragment)
                }
                SurveyAdapter.LIKERT_SCALES -> {
                    val likertFragment = LikertScaleFragment.newInstance(question = question)
                    theSurvey.add(likertFragment)
                }
                SurveyAdapter.FILL_IN_THE_BLANK -> {

                }
                SurveyAdapter.MULTIPLE_CHOICE -> {

                }
                SurveyAdapter.SINGLE_MULTIPLE_ANSWERS -> {

                }
                SurveyAdapter.OPEN_ENDED_TEXT_RESPONSES -> {

                }
                SurveyAdapter.FOOTER_VIEW -> {

                }
            }
        }
        loadFragment(theSurvey.first())
    }

    companion object {
        fun start(context: Context){
            context.startActivity(Intent(context, SurveyContainerActivity::class.java))
        }
    }

    //OnHeaderInteractionListener
    override fun participantClickedStart(currentQuestion: Question) {
        println("SurveyContainerActivity participantClickedStart")
        loadFragment(theSurvey[currentQuestion.next])
    }

    override fun participantClickedCancel(currentQuestion: Question) {
        println("SurveyContainerActivity participantClickedCancel")
        onBackPressed()
    }

    //OnLikertScaleInteraktionListener
    override fun likertScaleGoToNextItem(currentQuestion: Question) {
        println("SurveyContainerActivity likertScaleGoToNextItem")
    }

    override fun likertScaleGoToPrevoiusItem(currentQuestion: Question) {
        println("SurveyContainerActivity likertScaleGoToPrevoiusItem")
        loadFragment(theSurvey[currentQuestion.previous])
    }
}
