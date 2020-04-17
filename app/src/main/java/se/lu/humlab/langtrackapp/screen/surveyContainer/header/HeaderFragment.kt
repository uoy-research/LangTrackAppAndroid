package se.lu.humlab.langtrackapp.screen.surveyContainer.header

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
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.header_fragment.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.HeaderFragmentBinding
import se.lu.humlab.langtrackapp.interfaces.OnQuestionInteractionListener
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerViewModel
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerViewModelFactory

class HeaderFragment : Fragment(){

    private var listener: OnQuestionInteractionListener? = null
    lateinit var binding: HeaderFragmentBinding
    lateinit var question: Question
    private lateinit var viewModel : SurveyContainerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //return super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.header_fragment, container,false)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        viewModel = ViewModelProviders.of(this,
            SurveyContainerViewModelFactory(binding.root.context)
        ).get(SurveyContainerViewModel::class.java)
        val v = binding.root
        v.headerCancelButton.setOnClickListener {
            listener?.closeSurvey()
        }
        v.headerStartButton.setOnClickListener {
            listener?.nextQuestion(current = question)
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
            throw RuntimeException(context.toString() + " must implement OnHeaderInteractionListener")
        }
    }

    fun setText(){
        if (context is OnQuestionInteractionListener) {
            if (::binding.isInitialized) {
                binding.headerTitleTextView.text = question.title
                binding.headerTextTextView.setText(
                    getString(
                        R.string.headerGreetingText,
                        viewModel.getCurrentUser().userName,
                        question.text
                    )
                )
            }
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
            HeaderFragment().apply {

            }
    }
}