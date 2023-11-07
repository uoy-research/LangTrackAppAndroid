package uk.ac.york.langtrackapp.screen.surveyContainer.timeDuration

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
//import kotlinx.android.synthetic.main.time_duration_fragment.view.*
import uk.ac.york.langtrackapp.R
import uk.ac.york.langtrackapp.data.model.Answer
import uk.ac.york.langtrackapp.data.model.Question
import uk.ac.york.langtrackapp.databinding.TimeDurationFragmentBinding
import uk.ac.york.langtrackapp.interfaces.OnQuestionInteractionListener

class TimeDurationFragment : Fragment(){

    private var listener: OnQuestionInteractionListener? = null
    lateinit var binding: TimeDurationFragmentBinding
    lateinit var theQuestion: Question
    var theAnswer: Answer? = null
    private val listOfHours = arrayOf("0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24")
    private val listOfMinutes = arrayOf("00","05","10","15","20","25","30","35","40","45","50","55")
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
        v.number_picker_hour.setOnScrollListener { view, scrollState ->
            if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE){
                selectedHours = view.value
                listener?.setTimeDurationAnswer(getSelectedDurationInSeconds())
            }
        }
        v.number_picker_minutes.minValue = 0
        v.number_picker_minutes.maxValue = listOfMinutes.size - 1
        v.number_picker_minutes.displayedValues = listOfMinutes
        v.number_picker_minutes.setOnScrollListener { view, scrollState ->
            if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE){
                selectedMinutes = listOfMinutes[view.value].toInt()
                listener?.setTimeDurationAnswer(getSelectedDurationInSeconds())
            }
        }

        v.timeDurationBackButton.setOnClickListener {
            theAnswer = null
            listener?.prevoiusQuestion(theQuestion)
        }
        v.timeDurationNextButton.setOnClickListener {
            theAnswer = null
            listener?.nextQuestion(theQuestion)
        }
        return v
    }

    private fun getSelectedDurationInSeconds() : Int{
        var seconds: Int = selectedHours * 60 * 60
        seconds += (selectedMinutes * 60)
        return seconds
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnQuestionInteractionListener) {
            listener = context
            if (::binding.isInitialized) {
                setQuestion()
            }
        }else {
            throw RuntimeException(context.toString() + " must implement OnLikertScaleInteraktionListener")
        }
    }

    fun setQuestion(){
        if (::binding.isInitialized) {
            binding.timeDurationTextTextView.text = theQuestion.text
            if (theAnswer != null){
                if (theAnswer!!.timeDurationAnswer != null){
                    val hours = theAnswer!!.timeDurationAnswer!! / 60 / 60
                    val minutes = (theAnswer!!.timeDurationAnswer!! - (hours * 60 * 60)) / 60
                    binding.numberPickerHour.value = hours
                    var minString = minutes.toString()
                    if (minString == "0"){
                        minString = "00"
                    }else if (minString == "5"){
                        minString = "05"
                    }
                    val minIndex: Int = listOfMinutes.indexOf(minString)
                    binding.numberPickerMinutes.value = minIndex
                }
            }else{
                binding.numberPickerHour.value = 0
                binding.numberPickerMinutes.value = 0
            }
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
            TimeDurationFragment().apply {

            }
    }
}