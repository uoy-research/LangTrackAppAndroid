package se.lu.humlab.langtrackapp.screen.surveyContainer.multipleChoiceFragment

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
import android.widget.CheckBox
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.multiple_choice_fragment.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.MultipleChoiceFragmentBinding
import se.lu.humlab.langtrackapp.interfaces.OnQuestionInteractionListener

class MultipleChoiceFragment : Fragment(){

    private var listener: OnQuestionInteractionListener? = null
    lateinit var binding: MultipleChoiceFragmentBinding
    lateinit var question: Question

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.multiple_choice_fragment, container,false)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        val v = binding.root
        v.multipleChoiseFragmentNextButton.setOnClickListener {
            if (question.skip != null){
                if (question.skip?.ifChosen == getChoiceIfOnlyOneSelected() ?: -1){
                    listener?.goToNextItemWithSkipLogic(question)
                }else listener?.goToNextItem(currentQuestion = question)
            }else listener?.goToNextItem(currentQuestion = question)
        }
        v.multipleChoiseFragmentBackButton.setOnClickListener {
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
                setQuestion()
            }
        }else {
            throw RuntimeException(context.toString() + " must implement OnLikertScaleInteraktionListener")
        }
    }

    fun setQuestion(){
        if (::binding.isInitialized) {
            binding.multipleTextTextView.text = question.text
            showChoices()
        }
    }

    fun getChoiceIfOnlyOneSelected(): Int?{
        var choise = 0
        var number = 0
        for (view in binding.multipleRadioButtonContainer.children){
            if (view is CheckBox){
                if (view.isChecked ){
                    choise = view.tag as Int
                    number += 1
                }
            }
        }
        if (number == 1){
            return choise
        }else{
            return null
        }
    }

    fun showChoices(){
        if (question.multipleChoisesAnswers != null) {
            for ((index,choice) in question.multipleChoisesAnswers!!.withIndex()) {
                val checkBox = CheckBox(binding.multipleRadioButtonContainer.context)
                checkBox.tag = index
                checkBox.text = choice
                checkBox.textSize = 18F
                checkBox.setOnClickListener {
                    val theCheckbox = it as? CheckBox
                    println("radiobutton ${theCheckbox?.tag ?: -1} is set to ${theCheckbox?.isChecked}")
                }
                binding.multipleRadioButtonContainer.addView(checkBox)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //update question
        if (::binding.isInitialized) {
            setQuestion()
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MultipleChoiceFragment().apply {

            }
    }
}