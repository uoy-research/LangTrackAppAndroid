package se.lu.humlab.langtrackapp.screen.survey.viewholders

/*
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
}*/