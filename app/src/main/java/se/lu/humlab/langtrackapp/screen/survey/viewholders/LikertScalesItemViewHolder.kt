package se.lu.humlab.langtrackapp.screen.survey.viewholders

/*
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
}*/