package se.lu.humlab.langtrackapp.screen.surveyContainer.singleMultipleAnswersFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.single_multiple_answer_item.view.*
import kotlinx.android.synthetic.main.single_multiple_answer_item.view.singleMultipleAnswerNextButton
import kotlinx.android.synthetic.main.single_multiple_answers_fragment.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.SingleMultipleAnswersFragmentBinding
import se.lu.humlab.langtrackapp.interfaces.OnSingleMultipleInteractionListener

class SingleMultipleAnswersFragment : Fragment(){

    private var listener: OnSingleMultipleInteractionListener? = null
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
            listener?.singleMultipleGoToNextItem(currentQuestion = question)
        }
        v.singleMultipleAnswerBackButton.setOnClickListener {
            listener?.singleMultipleGoToPrevoiusItem(currentQuestion = question)
        }
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSingleMultipleInteractionListener) {
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
            binding.singleMultipleAnswerTextView.text =
                "Här kommer texten:\n\n${question.title}\n${question.text}"
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