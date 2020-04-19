package se.lu.humlab.langtrackapp.screen.surveyContainer.timeDuration

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.time_duration_fragment.view.*
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Answer
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.databinding.TimeDurationFragmentBinding
import se.lu.humlab.langtrackapp.interfaces.OnQuestionInteractionListener

class TimeDurationFragment : Fragment(){

    private var listener: OnQuestionInteractionListener? = null
    lateinit var binding: TimeDurationFragmentBinding
    lateinit var theQuestion: Question
    var theAnswer: Answer? = null
    val listOfHours = arrayOf("0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24")
    val listOfMinutes = arrayOf("00","05","10","15","20","25","30","35","40","45","50","55")
    var selectedHours = 0
    var selectedMinutes = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.time_duration_fragment, container,false)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        val v = binding.root
        v.number_picker_hour.minValue = 0
        v.number_picker_hour.maxValue = listOfHours.size - 1
        v.number_picker_hour.displayedValues = listOfHours
        v.number_picker_hour.setOnValueChangedListener { _, oldVal, newVal ->
            selectedHours = newVal
            println("You picked $newVal hours, getSelectedDurationInSeconds: ${getSelectedDurationInSeconds()}")
        }
        v.number_picker_minutes.minValue = 0
        v.number_picker_minutes.maxValue = listOfMinutes.size - 1
        v.number_picker_minutes.displayedValues = listOfMinutes
        v.number_picker_minutes.setOnValueChangedListener { _, oldVal, newVal ->
            selectedMinutes = listOfMinutes[newVal].toInt()
            println("You picked ${listOfMinutes[newVal]} minutes, getSelectedDurationInSeconds: ${getSelectedDurationInSeconds()}")
        }

        v.timeDurationBackButton.setOnClickListener {
            listener?.prevoiusQuestion(theQuestion)
            theAnswer = null
        }
        v.timeDurationNextButton.setOnClickListener {
            listener?.nextQuestion(theQuestion)
            theAnswer = null
        }
        return v
    }

    private fun getSelectedDurationInSeconds() : Int{
        var seconds: Int = selectedHours * 60
        seconds += selectedMinutes
        return seconds
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
            binding.timeDurationTextTextView.text = theQuestion.text
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
            TimeDurationFragment().apply {

            }
    }
}