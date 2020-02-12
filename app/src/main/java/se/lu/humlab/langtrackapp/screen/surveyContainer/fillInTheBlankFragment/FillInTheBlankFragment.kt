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
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fill_in_the_blanks_fragment.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.FillInTheBlanksFragmentBinding
import se.lu.humlab.langtrackapp.interfaces.OnFillInBlankInteractionListener

class FillInTheBlankFragment : Fragment(){

    private var listener: OnFillInBlankInteractionListener? = null
    lateinit var binding: FillInTheBlanksFragmentBinding
    lateinit var question: Question
    var theSentence: FillInWordSentence? = null
    var theChosenWordIndex : Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fill_in_the_blanks_fragment, container,false)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        val v = binding.root
        v.fillInTheBlankNextButton.setOnClickListener {
            listener?.fillInBlankGoToNextItem(currentQuestion = question)
        }
        v.fillInTheBlankBackButton.setOnClickListener {
            listener?.fillInBlankGoToPrevoiusItem(currentQuestion = question)
        }
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFillInBlankInteractionListener) {
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
            getTextAsList(question.text)
            if (theSentence != null){
                setSentence(null)
                setButtons()
            }
        }
    }

    fun setButtons(){
        if (::binding.isInitialized) {
            if (question.fillBlanksChoises != null) {
                for ((i, choice) in question.fillBlanksChoises!!.withIndex()) {
                    val button = Button(binding.buttonContainer.context)
                    button.text = choice
                    button.tag = i
                    button.setOnClickListener {
                        theChosenWordIndex = it.tag as? Int
                        setSentence(theChosenWordIndex)
                    }
                    binding.buttonContainer.addView(button)
                }
                val button = Button(binding.buttonContainer.context)
                button.text = "-inget-"
                button.setOnClickListener {
                    setSentence(null)
                    theChosenWordIndex = null
                }
                binding.buttonContainer.addView(button)
            }
        }
    }


    fun setSentence(indexOfWord: Int?){
        if (indexOfWord == null){
            binding.fillInTheBlankTextView.text = theSentence!!.listWithWords.joinToString(separator = " ")
        }else if (theChosenWordIndex != null){
            val tempListWithWords = theSentence!!.listWithWords.toMutableList()
            tempListWithWords[theSentence!!.indexForMissingWord] = question.fillBlanksChoises?.get(theChosenWordIndex!!) ?: ""
            binding.fillInTheBlankTextView.text = tempListWithWords.joinToString(separator = " ")
        }else{
            val tempListWithWords = theSentence!!.listWithWords.toMutableList()
            tempListWithWords[theSentence!!.indexForMissingWord] = question.fillBlanksChoises?.get(indexOfWord) ?: ""
            binding.fillInTheBlankTextView.text = tempListWithWords.joinToString(separator = " ")
        }
    }

    fun getTextAsList(theText: String){
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
            FillInTheBlankFragment().apply {

            }
    }
}

data class FillInWordSentence (
    var listWithWords: List<String>,
    var indexForMissingWord: Int
)