package se.lu.humlab.langtrackapp.screen.surveyContainer.singleMultipleAnswersFragment

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.single_multiple_answer_item.view.singleMultipleAnswerNextButton
import kotlinx.android.synthetic.main.single_multiple_answers_fragment.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.SingleMultipleAnswersFragmentBinding
import se.lu.humlab.langtrackapp.interfaces.OnQuestionInteractionListener


class SingleMultipleAnswersFragment : Fragment(){

    private var listener: OnQuestionInteractionListener? = null
    lateinit var binding: SingleMultipleAnswersFragmentBinding
    lateinit var question: Question

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.single_multiple_answers_fragment, container,false)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        val v = binding.root
        v.singleMultipleAnswerNextButton.setOnClickListener {
            listener?.goToNextItem(currentQuestion = question)
        }
        v.singleMultipleAnswerBackButton.setOnClickListener {
            listener?.goToPrevoiusItem(currentQuestion = question)
        }
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnQuestionInteractionListener) {
            listener = context
            if (::binding.isInitialized) {
                //load survey
                setQuestion(context)
            }
        }else {
            throw RuntimeException(context.toString() + " must implement OnLikertScaleInteraktionListener")
        }
    }

    fun setQuestion(context: Context){
        if (::binding.isInitialized) {
            binding.singleMultipleAnswerTextTextView.text = question.text
            binding.singleMultipleAnswerTitleTextView.text = question.title
            presentChoices(context)
        }
    }

    fun presentChoices(context: Context){
        if (question.singleMultipleAnswers != null) {
            binding.singleMultipleAnswerContainer.removeAllViews()
            for (choice in question.singleMultipleAnswers!!) {
                println("presentChoices")
                val radioButton = RadioButton(context)
                radioButton.text = choice
                radioButton.textSize = 17f
                binding.singleMultipleAnswerContainer.addView(radioButton)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //update question
        setQuestion(binding.singleMultipleAnswerContainer.context)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SingleMultipleAnswersFragment().apply {

            }
    }
}