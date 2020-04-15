package se.lu.humlab.langtrackapp.screen.surveyContainer.openEndedTextResponsesFragment

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.open_ended_text_responses_fragment.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.OpenEndedTextResponsesFragmentBinding
import se.lu.humlab.langtrackapp.interfaces.OnQuestionInteractionListener

class OpenEndedTextResponsesFragment : Fragment(){

    private var listener: OnQuestionInteractionListener? = null
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
            listener?.nextQuestion(current = question)
        }
        v.openEndedTextBackButton.setOnClickListener {
            listener?.prevoiusQuestion(current = question)
        }
        v.openLayout.setOnClickListener {
            hideKeyboard()
        }
        v.openEditText.setOnKeyListener { v, keyCode, event ->
            if((event.action == KeyEvent.ACTION_DOWN)
                && (event.keyCode == KeyEvent.KEYCODE_ENTER)){
                hideKeyboard()
                return@setOnKeyListener true
            }

            false
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
            binding.openTextTextView.text = question.text
        }
    }

    override fun onResume() {
        super.onResume()
        //update question
        setQuestion()
    }

    fun hideKeyboard() {

        if (activity != null) {
            val imm =
                activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = binding.openEditText
            //If no view currently has focus, create a new one, just so we can grab a window token from it

            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
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