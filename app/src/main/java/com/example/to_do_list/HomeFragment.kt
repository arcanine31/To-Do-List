package com.example.to_do_list

import android.os.Bundle
import android.provider.BaseColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.to_do_list.adapter.ScheduleAdapter
import com.example.to_do_list.database.DbContract
import com.example.to_do_list.database.ReaderDbHelper
import com.example.to_do_list.model.Schedule
import kotlinx.android.synthetic.main.fragment_home.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    lateinit var scheduleAdapter: ScheduleAdapter
    val lm = LinearLayoutManager(activity)

    lateinit var dbHelper: ReaderDbHelper
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = ReaderDbHelper(activity!!)
        initView()
    }

    override fun onResume() {
        super.onResume()

        initView()
    }

    private fun initView() {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
            DbContract.DataEntry.COLUMN_SCHED_NAME,
            DbContract.DataEntry.COLUMN_DETAIL,
            DbContract.DataEntry.COLUMN_DATE_DAY,
            DbContract.DataEntry.COLUMN_DATE_MONTH,
            DbContract.DataEntry.COLUMN_DATE_YEAR,
            DbContract.DataEntry.COLUMN_TIME_HOUR,
            DbContract.DataEntry.COLUMN_TIME_MINUTE)

        val cursor = db.query(
            DbContract.DataEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null,
        )

        val addSchedule = mutableListOf<Schedule>()
        with(cursor){
            while (moveToNext()){
                val id = getLong(getColumnIndex(BaseColumns._ID))
                val title = getString(getColumnIndexOrThrow(DbContract.DataEntry.COLUMN_SCHED_NAME))
                val detail = getString(getColumnIndexOrThrow(DbContract.DataEntry.COLUMN_DETAIL))
                val day = getInt(getColumnIndexOrThrow(DbContract.DataEntry.COLUMN_DATE_DAY))
                val month = getInt(getColumnIndexOrThrow(DbContract.DataEntry.COLUMN_DATE_MONTH))
                val year = getInt(getColumnIndexOrThrow(DbContract.DataEntry.COLUMN_DATE_YEAR))
                val hour = getInt(getColumnIndexOrThrow(DbContract.DataEntry.COLUMN_TIME_HOUR))
                val minute = getInt(getColumnIndexOrThrow(DbContract.DataEntry.COLUMN_TIME_MINUTE))
                addSchedule.add(Schedule(id, title, detail, hour, minute, day, month, year))
            }
        }
        rcview_item.layoutManager = lm
        scheduleAdapter = ScheduleAdapter(activity!!)
        rcview_item.adapter = scheduleAdapter
        scheduleAdapter.setSchedule(addSchedule)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}