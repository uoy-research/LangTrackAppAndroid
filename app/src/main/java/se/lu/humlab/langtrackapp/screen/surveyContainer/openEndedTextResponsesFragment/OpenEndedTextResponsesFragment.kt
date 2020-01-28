package se.lu.humlab.langtrackapp.screen.surveyContainer.openEndedTextResponsesFragment

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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.open_ended_text_responses_fragment.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.OpenEndedTextResponsesFragmentBinding
import se.lu.humlab.langtrackapp.interfaces.OnOpenEndedInteractionListener

class OpenEndedTextResponsesFragment : Fragment(){

    private var listener: OnOpenEndedInteractionListener? = null
    lateinit var binding: OpenEndedTextResponsesFragmentBinding
    lateinit var question: Question

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.open_ended_text_responses_fragment, container,false)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        val v = binding.root
        v.openEndedTextNextButton.setOnClickListener {
            listener?.openEndedGoToNextItem(currentQuestion = question)
        }
        v.openEndedTextBackButton.setOnClickListener {
            listener?.openEndedGoToPrevoiusItem(currentQuestion = question)
        }
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnOpenEndedInteractionListener) {
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
            binding.openEndedTextTextView.text =
                "${question.title}\n\n${question.text}"
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
            OpenEndedTextResponsesFragment().apply {

            }
    }
}