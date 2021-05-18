package com.example.to_do_list.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do_list.R
import com.example.to_do_list.model.Schedule
import kotlinx.android.synthetic.main.item_list.view.*
import org.w3c.dom.Text

class ScheduleAdapter (val context: Context): RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>(){

    private var schedules : List<Schedule> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return ScheduleViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list, parent, false))
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bindModel(schedules[position])
    }

    override fun getItemCount(): Int {
        return schedules.size
    }

    fun setSchedule(data: List<Schedule>){
        schedules = data
    }

    inner class ScheduleViewHolder(item: View): RecyclerView.ViewHolder(item){
        val txtTitle: TextView = item.findViewById(R.id.tv_sched_title)
        val txtDetail: TextView = item.findViewById(R.id.tv_sched_detail)
        val txtDay: TextView = item.findViewById(R.id.tv_sched_day)
        val txtMonth: TextView = item.findViewById(R.id.tv_sched_month)
        val txtYear: TextView = item.findViewById(R.id.tv_sched_year)
        val txtHour: TextView = item.findViewById(R.id.tv_sched_hour)
        val txtMinutes: TextView = item.findViewById(R.id.tv_sched_minutes)

        fun bindModel(m: Schedule){
            txtTitle.text =m.title
            txtDetail.text =m.detail
            txtDay.text = m.day.toString()
            txtMonth.text = m.month.toString()
            txtYear.text = m.year.toString()
            txtHour.text = m.hour.toString()
            txtMinutes.text = m.minute.toString()
        }

    }
}