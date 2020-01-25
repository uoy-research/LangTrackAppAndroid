package se.lu.humlab.langtrackapp.screen.survey

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import se.lu.humlab.langtrackapp.data.model.Question
import se.lu.humlab.langtrackapp.interfaces.*
import se.lu.humlab.langtrackapp.screen.survey.viewholders.*

class SurveyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var onRowClickedListener: OnLikertScalesItemClickedListener
    private lateinit var onFillBlanksItemClickedListener: OnFillBlanksItemClickedListener
    private lateinit var onMultipleChoiceItemClickedListener: OnMultipleChoiceItemClickedListener
    private lateinit var onSingleMultipleAnswersItemClickedListener: OnSingleMultipleAnswersItemClickedListener
    private lateinit var onOpenEndedTextItemClickedListener: OnOpenEndedTextItemClickedListener

    private var questions: List<Question> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        LIKERT_SCALES -> LikertScalesItemViewHolder.newInstance(parent, onRowClickedListener)
        FILL_IN_THE_BLANK -> FillBlanksItemViewHolder.newInstance(parent, onFillBlanksItemClickedListener)
        MULTIPLE_CHOICE -> MultipleChoiceItemViewHolder.newInstance(parent, onMultipleChoiceItemClickedListener)
        SINGLE_MULTIPLE_ANSWERS -> SingleMultipleAnswersItemViewHolder.newInstance(parent, onSingleMultipleAnswersItemClickedListener)
        OPEN_ENDED_TEXT_RESPONSES -> OpenEndedTextResponsesItemViewHolder.newInstance(parent, onOpenEndedTextItemClickedListener)
        else -> throw IllegalArgumentException()
    }

    fun setQuestions(questions: List<Question>){
        this.questions = questions
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    fun getItemAt(position: Int) : Question{
        return questions[position]
    }

    override fun getItemViewType(position: Int) = questions[position].type

    //LikertScalesItemViewHolder
    private fun onQuestionIsLikertScales(holder: RecyclerView.ViewHolder, position: Int) {
        val headerRow = holder as LikertScalesItemViewHolder
        headerRow.bind(questions[position])
    }

    //FillBlanksItemViewHolder
    private fun onQuestionIsFillInBlanks(holder: RecyclerView.ViewHolder, position: Int) {
        val headerRow = holder as FillBlanksItemViewHolder
        headerRow.bind(questions[position])
    }

    //MultipleChoiceItemViewHolder
    private fun onQuestionIsMultipleChoice(holder: RecyclerView.ViewHolder, position: Int){
        val headerRow = holder as MultipleChoiceItemViewHolder
        headerRow.bind(questions[position])
    }

    //SingleMultipleAnswersItemViewHolder
    private fun onQuestionIsSingleMultipleAnswers(holder: RecyclerView.ViewHolder, position: Int){
        val headerRow = holder as SingleMultipleAnswersItemViewHolder
        headerRow.bind(questions[position])
    }

    //OpenEndedTextResponsesItemViewHolder
    private fun onQuestionIsOpenEndedTextResponses(holder: RecyclerView.ViewHolder, position: Int){
        val headerRow = holder as OpenEndedTextResponsesItemViewHolder
        headerRow.bind(questions[position])
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            LIKERT_SCALES -> onQuestionIsLikertScales(holder,position)
            FILL_IN_THE_BLANK -> onQuestionIsFillInBlanks(holder,position)
            MULTIPLE_CHOICE -> onQuestionIsMultipleChoice(holder,position)
            SINGLE_MULTIPLE_ANSWERS -> onQuestionIsSingleMultipleAnswers(holder,position)
            OPEN_ENDED_TEXT_RESPONSES -> onQuestionIsOpenEndedTextResponses(holder,position)
            else -> throw IllegalArgumentException()
        }
    }

    fun setLikertScalesItemClickedListener(onRowClickedListener: OnLikertScalesItemClickedListener){
        this.onRowClickedListener = onRowClickedListener
    }
    fun setFillBlanksItemClickedListener(listener: OnFillBlanksItemClickedListener){
        this.onFillBlanksItemClickedListener = listener
    }
    fun setMultipleChoiceItemClickedListener(listener: OnMultipleChoiceItemClickedListener){
        this.onMultipleChoiceItemClickedListener = listener
    }
    fun setSingleMultipleAnswersItemClickedListener(listener: OnSingleMultipleAnswersItemClickedListener){
        this.onSingleMultipleAnswersItemClickedListener = listener
    }
    fun setOpenEndedTextItemClickedListener(listener: OnOpenEndedTextItemClickedListener){
        this.onOpenEndedTextItemClickedListener = listener
    }

    companion object {
        const val HEADER_VIEW = 0
        const val LIKERT_SCALES = 1
        const val FILL_IN_THE_BLANK = 2
        const val MULTIPLE_CHOICE = 3
        const val SINGLE_MULTIPLE_ANSWERS = 4
        const val OPEN_ENDED_TEXT_RESPONSES = 5
        const val FOOTER_VIEW = 6
    }
}