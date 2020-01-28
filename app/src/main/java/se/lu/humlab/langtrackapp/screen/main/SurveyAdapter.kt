package se.lu.humlab.langtrackapp.screen.main

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import se.lu.humlab.langtrackapp.data.model.Survey
import se.lu.humlab.langtrackapp.interfaces.OnSurveyRowClickedListener

class SurveyAdapter : RecyclerView.Adapter<SurveyItemViewHolder>() {
    private lateinit var onRowClickedListener: OnSurveyRowClickedListener
    private var items: List<Survey> = ArrayList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SurveyItemViewHolder {
        return SurveyItemViewHolder.newInstance(p0,onRowClickedListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getTask(position: Int): Survey {
        return items[position]
    }

    override fun onBindViewHolder(holder: SurveyItemViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    fun setTasks(items: List<Survey>){
        this.items = items
        notifyDataSetChanged()
    }

    fun setOnRowClickedListener(onRowClickedListener: OnSurveyRowClickedListener){
        this.onRowClickedListener = onRowClickedListener
    }
}