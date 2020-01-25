package se.lu.humlab.langtrackapp.screen.surveyContainer.fillInTheBlankFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fill_blanks_item.view.*
import kotlinx.android.synthetic.main.fill_in_the_blanks_fragment.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.FillInTheBlanksFragmentBinding
import se.lu.humlab.langtrackapp.interfaces.OnFillInBlankInteractionListener

class FillInTheBlankFragment : Fragment(){

    private var listener: OnFillInBlankInteractionListener? = null
    lateinit var binding: FillInTheBlanksFragmentBinding
    lateinit var question: Question

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
            binding.fillInTheBlankTextView.text =
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
            FillInTheBlankFragment().apply {

            }
    }
}