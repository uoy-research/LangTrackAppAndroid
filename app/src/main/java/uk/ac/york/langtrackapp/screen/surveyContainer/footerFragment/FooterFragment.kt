package uk.ac.york.langtrackapp.screen.surveyContainer.footerFragment

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
//import kotlinx.android.synthetic.main.footer_fragment.view.*
import uk.ac.york.langtrackapp.R
import uk.ac.york.langtrackapp.data.model.Question
import uk.ac.york.langtrackapp.databinding.FooterFragmentBinding
import uk.ac.york.langtrackapp.interfaces.OnQuestionInteractionListener

class FooterFragment : Fragment(){

    private var listener: OnQuestionInteractionListener? = null
    lateinit var binding: FooterFragmentBinding
    lateinit var question: Question

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.footer_fragment, container,false)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        val v = binding.root
        binding.footerNextButton.setOnClickListener {
            listener?.sendInSurvey()
        }
        binding.footerBackButton.setOnClickListener {
            listener?.prevoiusQuestion(current = question)
        }
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnQuestionInteractionListener) {
            listener = context
            if (::binding.isInitialized) {
                //load survey
                setText()
            }
        }else {
            throw RuntimeException(context.toString() + " must implement OnLikertScaleInteraktionListener")
        }
    }

    fun setText(){
        if (::binding.isInitialized) {
            binding.footerTextView.text = question.text
        }
    }

    override fun onResume() {
        super.onResume()
        //update question
        setText()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FooterFragment().apply {

            }
    }
}