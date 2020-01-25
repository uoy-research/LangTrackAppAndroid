package se.lu.humlab.langtrackapp.screen.survey

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.open_ended_text_responses_item.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.SurveyActivityBinding
import se.lu.humlab.langtrackapp.helpers.StartSnapHelper
import se.lu.humlab.langtrackapp.interfaces.*

class SurveyActivity : AppCompatActivity() {

    private lateinit var mBind : SurveyActivityBinding
    private lateinit var viewModel : SurveyViewModel
    private lateinit var adapter: SurveyAdapter
    private lateinit var recycler : RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBind = DataBindingUtil.setContentView(this, R.layout.survey_activity)
        mBind.lifecycleOwner = this
        mBind.executePendingBindings()

        viewModel = ViewModelProviders.of(this,
            SurveyViewModelFactory(this)
        ).get(SurveyViewModel::class.java)
        mBind.viewModel = viewModel

        linearLayoutManager = LinearLayoutManager(this)
        recycler = findViewById(R.id.questionsRecycler)
        recycler.layoutManager = linearLayoutManager
        val startSnapHelper = StartSnapHelper()
        startSnapHelper.attachToRecyclerView(recycler)

        adapter = SurveyAdapter()

        setQuestionsClickListeners()

        recycler = mBind.questionsRecycler
        recycler.adapter = adapter


        setTestQuestions()

        recycler.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (adapter.getItemAt(linearLayoutManager.findFirstVisibleItemPosition()).type == SurveyAdapter.OPEN_ENDED_TEXT_RESPONSES) {
                val text = findViewById<EditText>(R.id.openEndedTextResponsesEditText)
                if (text != null) {
                    hideKeyboard(text)
                }
            }
        }
    }

    fun goToNextItemInRecycler(){
        if(linearLayoutManager.findFirstVisibleItemPosition() + 1 <= adapter.itemCount - 1)
            recycler.smoothScrollToPosition(linearLayoutManager.findFirstVisibleItemPosition() + 1)
    }

    fun goToPreviousItemInRecycler(){
        if(linearLayoutManager.findFirstVisibleItemPosition() - 1 >= 0)
            recycler.smoothScrollToPosition(linearLayoutManager.findFirstVisibleItemPosition() - 1)
    }

    fun Activity.hideKeyboard(editText: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0);
    }

    fun setQuestionsClickListeners(){
        adapter.setFillBlanksItemClickedListener(object : OnFillBlanksItemClickedListener{
            override fun goToNextItem() {
                goToNextItemInRecycler()
            }

            override fun goToPrevoiusItem() {
                goToPreviousItemInRecycler()
            }

        })
        adapter.setLikertScalesItemClickedListener(object : OnLikertScalesItemClickedListener{
            override fun goToNextItem() {
                goToNextItemInRecycler()
            }

            override fun goToPrevoiusItem() {
                goToPreviousItemInRecycler()
            }
        })
        adapter.setMultipleChoiceItemClickedListener(object : OnMultipleChoiceItemClickedListener{

            override fun goToNextItem() {
                goToNextItemInRecycler()
            }

            override fun goToPrevoiusItem() {
                goToPreviousItemInRecycler()
            }

        })
        adapter.setSingleMultipleAnswersItemClickedListener(object : OnSingleMultipleAnswersItemClickedListener{
            override fun goToNextItem() {
                goToNextItemInRecycler()
            }

            override fun goToPrevoiusItem() {
                goToPreviousItemInRecycler()
            }

        })
        adapter.setOpenEndedTextItemClickedListener(object : OnOpenEndedTextItemClickedListener{
            override fun goToNextItem() {
                goToNextItemInRecycler()
            }

            override fun goToPrevoiusItem() {
                goToPreviousItemInRecycler()
            }

            override fun hideTheKeyboard(editText: EditText) {
                hideKeyboard(editText)
            }

        })
    }

    fun setTestQuestions(){
        var questions = mutableListOf<Question>()
        val temp1 = Question()
        temp1.type = SurveyAdapter.LIKERT_SCALES
        questions.add(temp1)
        val temp2 = Question()
        temp2.type = SurveyAdapter.FILL_IN_THE_BLANK
        questions.add(temp2)
        val temp3 = Question()
        temp3.type = SurveyAdapter.MULTIPLE_CHOICE
        questions.add(temp3)
        val temp4 = Question()
        temp4.type = SurveyAdapter.SINGLE_MULTIPLE_ANSWERS
        questions.add(temp4)
        val temp5 = Question()
        temp5.type = SurveyAdapter.OPEN_ENDED_TEXT_RESPONSES
        questions.add(temp5)
        adapter.setQuestions(questions)

    }

    companion object {
        fun start(context: Context){
            context.startActivity(Intent(context, SurveyActivity::class.java))
        }
    }
}
