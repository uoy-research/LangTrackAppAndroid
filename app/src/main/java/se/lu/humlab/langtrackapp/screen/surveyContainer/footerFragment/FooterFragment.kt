package se.lu.humlab.langtrackapp.screen.surveyContainer.footerFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.footer_fragment.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.FooterFragmentBinding
import se.lu.humlab.langtrackapp.interfaces.OnFooterInteractionListener

class FooterFragment : Fragment(){

    private var listener: OnFooterInteractionListener? = null
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
        v.footerNextButton.setOnClickListener {
            listener?.footerGoToNextItem(currentQuestion = question)
        }
        v.footerBackButton.setOnClickListener {
            listener?.footerSendInSurvey(currentQuestion = question)
        }
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFooterInteractionListener) {
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
            binding.footerTextView.text = "Här kommer texten:\n\n${question.title}\n${question.text}"
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
            FooterFragment().apply {

            }
    }
}