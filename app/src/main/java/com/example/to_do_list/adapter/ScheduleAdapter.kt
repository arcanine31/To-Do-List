package com.example.to_do_list.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do_list.MainActivity
import com.example.to_do_list.R
import com.example.to_do_list.database.DbContract
import com.example.to_do_list.database.ReaderDbHelper
import com.example.to_do_list.model.Schedule
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_list.view.*


//Adapter to to fill item list with data from added data in database

class ScheduleAdapter(val context: Context): RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>(){

    private var schedules : List<Schedule> = ArrayList()
    var dbHelper: ReaderDbHelper =  ReaderDbHelper(context)
    var db : SQLiteDatabase =dbHelper.writableDatabase

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
        val bDelete: ImageButton = item.findViewById(R.id.btn_delete)






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
            val mDetail = mPrefs.getBoolean("detail", false)
            val mTime = mPrefs.getBoolean("time", false)
            val mDate = mPrefs.getBoolean("date", false)
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


            bDelete.setOnClickListener{


                val selection = "${BaseColumns._ID} LIKE ?"
                val selectionArg = arrayOf(m.id.toString())

                val alertdg = AlertDialog.Builder(context) //untuk munculin kotak alert
                alertdg.setTitle("You want to delete this activity")
                alertdg.setMessage("Confirm if you already done this or cancel this activity")

                val layout = LinearLayout(context)

                alertdg.setView(layout)

                alertdg.setPositiveButton("Ok") { dialog, which ->
                    val deleteRow = db.delete(DbContract.DataEntry.TABLE_NAME, selection, selectionArg)
                    if (deleteRow == -1){
                        Toast.makeText(context, "Hapus data gagal", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, "Hapus data berhasil", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)
                        (context as Activity).finish()


                    }

                }

                alertdg.setNegativeButton("Cancel") { dialog, which ->

                }
                alertdg.show()


            }


        }




    }
}
