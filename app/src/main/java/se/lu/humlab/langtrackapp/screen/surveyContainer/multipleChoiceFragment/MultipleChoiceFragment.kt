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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.multiple_choice_fragment.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Answer
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.MultipleChoiceFragmentBinding
import se.lu.humlab.langtrackapp.interfaces.OnQuestionInteractionListener
import se.lu.humlab.langtrackapp.util.setMarginTop

class MultipleChoiceFragment : Fragment(){

    private var listener: OnQuestionInteractionListener? = null
    lateinit var binding: MultipleChoiceFragmentBinding
    lateinit var theQuestion: Question
    var theAnswer: Answer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.multiple_choice_fragment, container,false)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        val v = binding.root
        v.multipleChoiseFragmentNextButton.setOnClickListener {
            theAnswer = null
            binding.multipleIndicatorBottom.elevation = 0f
            listener?.nextQuestion(theQuestion)
        }
        v.multipleChoiseFragmentBackButton.setOnClickListener {
            theAnswer = null
            binding.multipleIndicatorBottom.elevation = 0f
            listener?.prevoiusQuestion(current = theQuestion)
        }
        binding.multipleScroll.setOnScrollChangeListener { _, _, _, _, _ ->
            binding.multipleIndicatorTop.isSelected = binding.multipleScroll.canScrollVertically(-1)
            binding.multipleIndicatorBottom.isSelected = binding.multipleScroll.canScrollVertically(1)
        }

        binding.multipleScroll.viewTreeObserver.addOnGlobalLayoutListener {
            binding.multipleIndicatorBottom.isSelected = binding.multipleScroll.canScrollVertically(1)
        }
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnQuestionInteractionListener) {
            listener = context
            if (::binding.isInitialized) {
                setQuestion()
            }
        }else {
            throw RuntimeException(context.toString() + " must implement OnLikertScaleInteraktionListener")
        }
    }

    fun setBottomShadow(){
        val actualHeight = binding.multipleScrollLayout.measuredHeight
        val contentHeight = binding.multipleScroll.measuredHeight
        println("actualHeight: $actualHeight")
        println("contentHeight: $contentHeight")
        binding.multipleScroll.isSelected = contentHeight > actualHeight
    }

    fun setQuestion(){
        if (::binding.isInitialized) {
            binding.multipleTextTextView.text = theQuestion.text
            showChoices()
            //setBottomShadow()
        }
    }

    fun showChoices(){
        if (theQuestion.multipleChoisesAnswers != null) {
            binding.multipleRadioButtonContainer.removeAllViews()
            for ((index,choice) in theQuestion.multipleChoisesAnswers!!.withIndex()) {
                val checkBox = CheckBox(binding.multipleRadioButtonContainer.context)
                checkBox.tag = index
                checkBox.text = choice
                checkBox.textSize = 18F
                checkBox.setOnClickListener {
                    val theCheckbox = it as? CheckBox
                    if (theCheckbox != null){
                        if (theCheckbox.tag is Int) {
                            val theTag = theCheckbox.tag as? Int ?: -99
                            if (theTag != -99){
                                if (theCheckbox.isChecked){
                                    if (theAnswer == null){
                                        theAnswer = Answer()
                                    }
                                    if (theAnswer?.multipleChoiceAnswer.isNullOrEmpty()) {
                                        if (theAnswer!!.multipleChoiceAnswer == null) {
                                            theAnswer!!.multipleChoiceAnswer = mutableListOf()
                                        }
                                        theAnswer!!.multipleChoiceAnswer!!.add(theTag)
                                    }else{
                                        if (theAnswer?.multipleChoiceAnswer != null) {
                                            if (!theAnswer!!.multipleChoiceAnswer!!.contains(theTag)) {
                                                theAnswer!!.multipleChoiceAnswer!!.add(theTag)
                                            }
                                        }
                                    }
                                }else{
                                    if (theAnswer?.multipleChoiceAnswer != null) {
                                        theAnswer!!.multipleChoiceAnswer!!.remove(theTag)
                                    }
                                }
                                listener?.setMultipleAnswersAnswer(theAnswer?.multipleChoiceAnswer)
                                setNextButton()
                            }
                        }
                    }
                }

                binding.multipleRadioButtonContainer.addView(checkBox)
                checkBox.setMarginTop(10)
                checkBox.isChecked = theAnswer?.multipleChoiceAnswer?.contains(index) == true
            }
        }
        setNextButton()
    }


    private fun setNextButton(){
        binding.multipleChoiseFragmentNextButton.isEnabled =
            !theAnswer?.multipleChoiceAnswer.isNullOrEmpty()
    }

    override fun onResume() {
        super.onResume()
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