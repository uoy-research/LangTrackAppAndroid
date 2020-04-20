package se.lu.humlab.langtrackapp.screen.surveyContainer.fillInTheBlankFragment

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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fill_in_the_blanks_fragment.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Answer
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.FillInTheBlanksFragmentBinding
import se.lu.humlab.langtrackapp.interfaces.OnQuestionInteractionListener

class FillInTheBlankFragment : Fragment(){

    private var listener: OnQuestionInteractionListener? = null
    lateinit var binding: FillInTheBlanksFragmentBinding
    lateinit var spinner: Spinner
    lateinit var theQuestion: Question
    var theSentence: FillInWordSentence? = null
    var theChosenWordIndex : Int? = null
    var theAnswer: Answer? = null
    var check = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("FillInTheBlankFragment onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fill_in_the_blanks_fragment, container,false)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        val v = binding.root
        spinner = binding.choiceSpinner
        if (theQuestion.fillBlanksChoises != null && ::binding.isInitialized) {
            setAdapter()
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if(++check > 1) {
                        theChosenWordIndex = position
                        listener?.setFillBlankAnswer(theChosenWordIndex)
                        if (theSentence != null) {
                            setSentence(theChosenWordIndex)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
        v.fillInTheBlankNextButton.setOnClickListener {
            theAnswer = null
            listener?.nextQuestion(theQuestion)
        }
        v.fillInTheBlankBackButton.setOnClickListener {
            theAnswer = null
            listener?.prevoiusQuestion(current = theQuestion)
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("FillInTheBlankFragment onViewCreated")
        setQuestion()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        println("FillInTheBlankFragment onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        println("FillInTheBlankFragment onStart")
    }

    override fun onPause() {
        super.onPause()
        println("FillInTheBlankFragment onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("FillInTheBlankFragment onDestroy")
    }
    fun setAdapter(){
        addEmptyWordToTopOfList()
        val adapter =
            ArrayAdapter(
                spinner.context,
                R.layout.choice_spinner_item,
                theQuestion.fillBlanksChoises!!
            )
        adapter.setDropDownViewResource(R.layout.choice_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setInitAnswer(){
        val answerIndex = theAnswer?.fillBlankAnswer ?: -99
        theChosenWordIndex = null
        if (answerIndex >= 0){
            val answerWord = theQuestion.fillBlanksChoises?.get(answerIndex)
            if (answerWord != null){
                if (theSentence != null){
                    theChosenWordIndex = answerIndex
                    check++
                    spinner.setSelection(answerIndex,false)
                }
            }
        }
    }


    private fun addEmptyWordToTopOfList(){
        if (theQuestion.fillBlanksChoises?.first() != "_____") {
            theQuestion.fillBlanksChoises!!.add(0, "_____")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        println("FillInTheBlankFragment onAttach")
        if (context is OnQuestionInteractionListener) {
            listener = context
            if (::binding.isInitialized) {
                //load survey
                //setQuestion()
            }
        }else {
            throw RuntimeException(context.toString() + " must implement OnFillInBlankInteractionListener")
        }
    }

    fun setQuestion(){
        println("FillInTheBlankFragment setQuestion")
        if (::binding.isInitialized) {
            check = 0
            getTextAsList(theQuestion.text)
            setAdapter()
            if (theSentence != null){
                if (theChosenWordIndex != null){
                    setSentence(theChosenWordIndex)
                }else {
                    setSentence(null)
                }
            }
            setInitAnswer()
        }
    }


    fun setSentence(indexOfWord: Int?){
        if (indexOfWord == null){
            binding.fillInTheBlankTextView.text = theSentence!!.listWithWords.joinToString(separator = " ")
            binding.fillInTheBlankSpinnerTitle
        }else if (theChosenWordIndex != null){
            val tempListWithWords = theSentence!!.listWithWords.toMutableList()
            tempListWithWords[theSentence!!.indexForMissingWord] = theQuestion.fillBlanksChoises?.get(theChosenWordIndex!!) ?: ""
            binding.fillInTheBlankTextView.text = tempListWithWords.joinToString(separator = " ")
        }else{
            val tempListWithWords = theSentence!!.listWithWords.toMutableList()
            tempListWithWords[theSentence!!.indexForMissingWord] = theQuestion.fillBlanksChoises?.get(indexOfWord) ?: ""
            binding.fillInTheBlankTextView.text = tempListWithWords.joinToString(separator = " ")
        }
    }

    fun getTextAsList(theText: String){
        theSentence = null
        val listWithWords = theText.split(" ")

        var ind = -99
        for ((i,word) in listWithWords.withIndex()){
            if (word == "_____"){
                ind = i
            }

        }
        val theSentence = FillInWordSentence(
            listWithWords = listWithWords,
            indexForMissingWord = ind
        )
        this.theSentence = theSentence
    }


    override fun onResume() {
        super.onResume()
        println("FillInTheBlankFragment onResume")
        //update question
        //setQuestion()
    }

    override fun onDetach() {
        println("FillInTheBlankFragment onDetach")
        super.onDetach()
        listener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FillInTheBlankFragment().apply {

            }
    }
}

data class FillInWordSentence (
    var listWithWords: List<String>,
    var indexForMissingWord: Int
)