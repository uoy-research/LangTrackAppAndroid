package se.lu.humlab.langtrackapp.screen.survey.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.interfaces.OnMultipleChoiceItemClickedListener

class MultipleChoiceItemViewHolder(theItemView: View, listener: OnMultipleChoiceItemClickedListener
): RecyclerView.ViewHolder(theItemView) {

    lateinit var question : Question

    init {
        theItemView.findViewById<Button>(R.id.multipleChoiceNextButton)
            .setOnClickListener { listener.goToNextItem() }
        theItemView.findViewById<Button>(R.id.multipleChoicePreviousButton)
            .setOnClickListener { listener.goToPrevoiusItem() }
    }

    fun bind(item: Question){
        this.question = item
    }

    companion object {
        fun newInstance(parent: ViewGroup,
                        listener: OnMultipleChoiceItemClickedListener
        ): MultipleChoiceItemViewHolder {
            return MultipleChoiceItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.multiple_choice_item,
                    parent,
                    false
                ),
                listener
            )
        }
    }
}