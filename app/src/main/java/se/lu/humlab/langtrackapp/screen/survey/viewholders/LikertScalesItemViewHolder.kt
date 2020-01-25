package se.lu.humlab.langtrackapp.screen.survey.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import se.lu.humlab.langtrackapp.interfaces.OnLikertScalesItemClickedListener
import se.lu.humlab.langtrackapp.R
import se.lu.humlab.langtrackapp.data.model.Question

class LikertScalesItemViewHolder(theItemView: View, onAttRowClickedListener: OnLikertScalesItemClickedListener
): RecyclerView.ViewHolder(theItemView) {

    lateinit var question : Question

    init {
        theItemView.findViewById<Button>(R.id.likertScalesNextButton)
            .setOnClickListener { onAttRowClickedListener.goToNextItem() }

        theItemView.findViewById<Button>(R.id.likertScalesPreviousButton)
            .setOnClickListener { onAttRowClickedListener.goToPrevoiusItem() }
    }

    fun bind(item: Question){
        this.question = item
    }

    companion object {
        fun newInstance(parent: ViewGroup,
                        onAttRowClickedListener: OnLikertScalesItemClickedListener
        ): LikertScalesItemViewHolder {
            return LikertScalesItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.likert_scales_item,
                    parent,
                    false
                ),
                onAttRowClickedListener
            )
        }
    }
}