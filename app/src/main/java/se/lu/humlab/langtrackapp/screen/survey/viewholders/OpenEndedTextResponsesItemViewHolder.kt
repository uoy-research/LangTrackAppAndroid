package se.lu.humlab.langtrackapp.screen.survey.viewholders

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.interfaces.OnOpenEndedTextItemClickedListener

class OpenEndedTextResponsesItemViewHolder(theItemView: View, listener: OnOpenEndedTextItemClickedListener
): RecyclerView.ViewHolder(theItemView) {


    lateinit var question : Question
    lateinit var editText : EditText

    init {
        theItemView.findViewById<Button>(R.id.openEndedTextResponsesNextButton)
            .setOnClickListener {
                listener.goToNextItem()
                listener.hideTheKeyboard(editText)
            }
        theItemView.findViewById<Button>(R.id.openEndedTextResponsesPreviousButton)
            .setOnClickListener {
                listener.goToPrevoiusItem()
                listener.hideTheKeyboard(editText)
            }
        editText = theItemView.findViewById(R.id.openEndedTextResponsesEditText)
    }
    fun bind(item: Question){
        this.question = item
    }

    companion object {
        fun newInstance(parent: ViewGroup,
                        listener: OnOpenEndedTextItemClickedListener
        ): OpenEndedTextResponsesItemViewHolder {
            return OpenEndedTextResponsesItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.open_ended_text_responses_item,
                    parent,
                    false
                ),
                listener
            )
        }
    }
}