package com.example.to_do_list.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do_list.R
import com.example.to_do_list.model.Schedule
import kotlinx.android.synthetic.main.item_list.view.*
import org.w3c.dom.Text


//Adapter to to fill item list with data from added data in database

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
        val vDate: LinearLayout = item.findViewById(R.id.layout_date)
        val vTime: ConstraintLayout = item.findViewById(R.id.layout_time)


        fun bindModel(m: Schedule){
            txtTitle.text =m.title
            txtDetail.text =m.detail
            txtDay.text = m.day.toString()
            txtMonth.text = m.month.toString()
            txtYear.text = m.year.toString()
            txtHour.text = m.hour.toString()
            txtMinutes.text = m.minute.toString()

//            Below used for assigned preference on preferenceFragmentCompat by fetching defaultSharedPref

            val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
            val mDetail = mPrefs.getBoolean("detail",false)
            val mTime = mPrefs.getBoolean("time",false)
            val mDate = mPrefs.getBoolean("date",false)
//            if detail was checked, detail will visible on itemlist

            if (mPrefs.contains("detail")){
                if (mDetail){
                    txtDetail.visibility = View.VISIBLE
                }else{
                    txtDetail.visibility = View.INVISIBLE
                }
            }
//            if time was checked, time will visible on itemlist
            if (mPrefs.contains("time")){
                if (mTime){
                    vTime.visibility = View.VISIBLE
                }else{
                    vTime.visibility = View.GONE
                }
            }
//            if date was checked, date will visible on itemlist
            if (mPrefs.contains("date")){
                if (mDate){
                    vDate.visibility = View.VISIBLE
                }else{
                    vDate.visibility = View.GONE
                }
            }


        }

    }
}