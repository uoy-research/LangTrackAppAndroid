package se.lu.humlab.langtrackapp.screen.survey.viewholders

/*
class SingleMultipleAnswersItemViewHolder(theItemView: View, listener: OnSingleMultipleAnswersItemClickedListener
): RecyclerView.ViewHolder(theItemView) {

    lateinit var question : Question
    var spinner: Spinner
    var skipSpinner: Spinner
    var chosenAnswer = ""
    var skipLayout: ConstraintLayout

    init {
        theItemView.findViewById<Button>(R.id.singleMultipleAnswerNextButton)
            .setOnClickListener { listener.goToNextItem() }
        theItemView.findViewById<Button>(R.id.singleMultipleAnswerPreviousButton)
            .setOnClickListener { listener.goToPrevoiusItem() }

        skipLayout = theItemView.findViewById(R.id.singleMultipleAnswerSkipLayout)
        skipLayout.visibility = View.GONE
        spinner = theItemView.findViewById(R.id.single_multiple_answer_spinner)
        val templist = mutableListOf<String>()
        templist.add("Svar 1")
        templist.add("Svar 2")
        templist.add("Svar 3")
        templist.add("Svar 4")
        val adapter =
            ArrayAdapter(theItemView.context, R.layout.multiple_answer_spinner_item, templist.toList())
        adapter.setDropDownViewResource(R.layout.multiple_answer_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                chosenAnswer = parent.getItemAtPosition(position) as String
                when (position){
                    2 -> skipLayout.visibility = View.VISIBLE
                    else -> skipLayout.visibility = View.GONE
                }
                println("position: $position")
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


        skipSpinner = theItemView.findViewById(R.id.single_multiple_answer_skip_spinner)
        val templist2 = mutableListOf<String>()
        templist2.add("ExtraSvar 1")
        templist2.add("ExtraSvar 2")
        templist2.add("ExtraSvar 3")
        templist2.add("ExtraSvar 4")
        val skipAdapter =
            ArrayAdapter(theItemView.context, R.layout.multiple_answer_spinner_item, templist2.toList())
        adapter.setDropDownViewResource(R.layout.multiple_answer_spinner_dropdown_item)
        skipSpinner.adapter = skipAdapter
        skipSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                println("skipPosition: $position")
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }
    fun bind(item: Question){
        this.question = item
    }

    companion object {
        fun newInstance(parent: ViewGroup,
                        listener: OnSingleMultipleAnswersItemClickedListener
        ): SingleMultipleAnswersItemViewHolder {
            return SingleMultipleAnswersItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.single_multiple_answer_item,
                    parent,
                    false
                ),
                listener
            )
        }
    }
}*/