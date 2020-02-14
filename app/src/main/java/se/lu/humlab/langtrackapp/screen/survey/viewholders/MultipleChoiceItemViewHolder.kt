package se.lu.humlab.langtrackapp.screen.survey.viewholders

/*
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
}*/