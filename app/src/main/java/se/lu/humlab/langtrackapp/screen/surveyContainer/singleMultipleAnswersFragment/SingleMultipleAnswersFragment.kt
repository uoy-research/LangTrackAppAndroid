package se.lu.humlab.langtrackapp.screen.surveyContainer.singleMultipleAnswersFragment

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.single_multiple_answer_item.view.singleMultipleAnswerNextButton
import kotlinx.android.synthetic.main.single_multiple_answers_fragment.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Answer
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.SingleMultipleAnswersFragmentBinding
import se.lu.humlab.langtrackapp.interfaces.OnQuestionInteractionListener


class SingleMultipleAnswersFragment : Fragment(){

    private var listener: OnQuestionInteractionListener? = null
    lateinit var binding: SingleMultipleAnswersFragmentBinding
    lateinit var theQuestion: Question
    var theAnswer: Answer? = null
    var selectedRadioButton = 0

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
            listener?.nextQuestion(theQuestion)
            theAnswer = null
            /*if (question.skip != null){
                if (question.skip?.ifChosen == selectedRadioButton){
                    //listener?.goToNextItemWithSkipLogic(question)
                }else listener?.nextQuestion(current = question)
            }else listener?.nextQuestion(current = question)*/
        }
        v.singleMultipleAnswerBackButton.setOnClickListener {
            listener?.prevoiusQuestion(current = theQuestion)
            theAnswer = null
        }
        v.singleMultipleAnswerContainer.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1 ) {
                val radio: RadioButton? = group.findViewById(group.checkedRadioButtonId)
                if (radio != null) {
                    selectedRadioButton = radio.tag as Int
                    listener?.setSingleMultipleAnswer(selectedRadioButton)
                    println("selectedRadioButton: $selectedRadioButton")
                }
            }
        }
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnQuestionInteractionListener) {
            listener = context
            if (::binding.isInitialized) {
                //load survey
                setQuestion()
            }
        }else {
            throw RuntimeException(context.toString() + " must implement OnLikertScaleInteraktionListener")
        }
    }

    fun setQuestion(){
        if (::binding.isInitialized) {
            binding.singleMultipleAnswerTextTextView.text = theQuestion.text
            presentChoices()
        }
    }

    fun presentChoices(){
        binding.singleMultipleAnswerContainer.removeAllViews()
        if (theQuestion.singleMultipleAnswers != null) {
            binding.singleMultipleAnswerContainer.removeAllViews()
            for ((index, choice) in theQuestion.singleMultipleAnswers!!.withIndex()) {
                val radioButton = RadioButton(binding.singleMultipleAnswerContainer.context)
                radioButton.text = choice
                radioButton.tag = index
                radioButton.textSize = 17f
                binding.singleMultipleAnswerContainer.addView(radioButton)
                if (theAnswer?.singleMultipleAnswer ?: -99 == index) {
                    radioButton.isChecked = true
                    selectedRadioButton = index
                }else{
                    radioButton.isChecked = false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //update question
        setQuestion()
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