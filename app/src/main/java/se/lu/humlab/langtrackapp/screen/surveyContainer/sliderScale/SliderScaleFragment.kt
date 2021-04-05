package se.lu.humlab.langtrackapp.screen.surveyContainer.sliderScale

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.likert_scale_fragment.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Answer
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.SliderScaleFragmentBinding
import se.lu.humlab.langtrackapp.interfaces.OnQuestionInteractionListener

class SliderScaleFragment : Fragment(){


    private var listener: OnQuestionInteractionListener? = null
    lateinit var binding: SliderScaleFragmentBinding
    var theAnswer: Answer? = null
    lateinit var question: Question

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.slider_scale_fragment, container,false)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        val v = binding.root

        binding.sliderScaleNextButton.setOnClickListener {
            theAnswer = null
            listener?.nextQuestion(question)
        }
        binding.sliderScaleBackButton.setOnClickListener {
            theAnswer = null
            listener?.prevoiusQuestion(current = question)
        }
        binding.sliderScaleCheckboxNA.setOnClickListener {
            if (binding.sliderScaleCheckboxNA.isChecked){
                binding.sliderSeekBar.isEnabled = false
            }else{
                binding.sliderSeekBar.isEnabled = true
            }
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
            throw RuntimeException("$context must implement OnLikertScaleInteraktionListener")
        }
    }

    fun setQuestion(){
        if (::binding.isInitialized) {
            binding.sliderScaleDescriptionTextView.text = question.description
            binding.sliderScaleMin.text = question.likertMin
            binding.sliderScaleMax.text = question.likertMax
        }
    }

    override fun onResume() {
        super.onResume()
        setQuestion()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SliderScaleFragment().apply {

            }
    }
}