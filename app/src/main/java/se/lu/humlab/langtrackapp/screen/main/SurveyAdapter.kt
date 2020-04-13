package se.lu.humlab.langtrackapp.screen.main

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import se.lu.humlab.langtrackapp.data.model.Assignment
import se.lu.humlab.langtrackapp.data.model.Survey
import se.lu.humlab.langtrackapp.interfaces.OnExpiredListener
import se.lu.humlab.langtrackapp.interfaces.OnSurveyRowClickedListener
import se.lu.humlab.langtrackapp.util.toDate
import java.util.*
import kotlin.collections.ArrayList

class SurveyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val EXPIRED_ASSIGNMENT = 0
    private val ACTIVE_ASSIGNMENT = 1
    private lateinit var onRowClickedListener: OnSurveyRowClickedListener
    //private var items: List<Survey> = ArrayList()
    private var items: List<Assignment> = ArrayList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return when (p1){
            ACTIVE_ASSIGNMENT -> ActiveViewHolder.newInstance(p0,onRowClickedListener)
            else -> SurveyItemViewHolder.newInstance(p0,onRowClickedListener)
        }

    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is ActiveViewHolder){
            holder.removeCallbacks()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getTask(position: Int): Assignment {
        return items[position]
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ActiveViewHolder -> holder.bind(items[position], position, object: OnExpiredListener{
                override fun assignmentExpired() {
                    notifyDataSetChanged()
                }
            })
            is SurveyItemViewHolder -> holder.bind(items[position], position)
        }

    }

    override fun getItemViewType(position: Int): Int {
        val theAssignment = getTask(position)
        return if (theAssignment.isActive()){
            ACTIVE_ASSIGNMENT
        }else{
            EXPIRED_ASSIGNMENT
        }
    }

    /*fun setTasks(items: List<Survey>){
        this.items = items
        notifyDataSetChanged()
    }*/
     fun setAssignments(items: List<Assignment>){
         this.items = items
         notifyDataSetChanged()
     }

    fun setOnRowClickedListener(onRowClickedListener: OnSurveyRowClickedListener){
        this.onRowClickedListener = onRowClickedListener
    }
}