package se.lu.humlab.langtrackapp.screen.survey.viewholders

/*
class FillBlanksItemViewHolder(theItemView: View, listener: OnFillBlanksItemClickedListener
): RecyclerView.ViewHolder(theItemView) {

    lateinit var question : Question

    init {
        theItemView.findViewById<Button>(R.id.fillInTheBlanksNextButton)
            .setOnClickListener { listener.goToNextItem() }
        theItemView.findViewById<Button>(R.id.fillInTheBlanksPreviousButton)
            .setOnClickListener { listener.goToPrevoiusItem() }
    }

    fun bind(item: Question){
        this.question = item
    }

    companion object {
        fun newInstance(parent: ViewGroup,
                        onAttRowClickedListener: OnFillBlanksItemClickedListener
        ): FillBlanksItemViewHolder {
            return FillBlanksItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.fill_blanks_item,
                    parent,
                    false
                ),
                onAttRowClickedListener
            )
        }
    }
}*/