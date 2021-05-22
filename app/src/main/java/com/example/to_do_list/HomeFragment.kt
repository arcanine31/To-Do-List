package com.example.to_do_list

import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
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
    var titleHolder: String = ""
    lateinit var mPreferences: SharedPreferences
    private val sharePrefFile = "com.example.android.to_do_list"
    var mTitle: String = "DEFINE YOUR SCHEDULE"

    val TITLE_KEY = "title"

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
            tv_title_home.text = mTitle
            dbHelper = ReaderDbHelper(requireActivity())
        val prefs = requireActivity().getSharedPreferences(sharePrefFile, MODE_PRIVATE)
        if (prefs.contains(TITLE_KEY)){
                val loadedString = prefs.getString(TITLE_KEY, null)
                tv_title_home.setText(loadedString)
            }

            initView()
            initListener()
    }

    override fun onResume() {
        super.onResume()
        if (!titleHolder.isNullOrBlank()){
            tv_title_home.text = titleHolder
        }
        initListener()
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
        scheduleAdapter = ScheduleAdapter(requireActivity())
        rcview_item.adapter = scheduleAdapter
        scheduleAdapter.setSchedule(addSchedule)
    }

    fun initListener(){

        //used for sharedPref
        bt_edit_title.setOnClickListener{


            val alertdg = AlertDialog.Builder(activity)
            alertdg.setTitle("CHANGE TITLE")
            alertdg.setMessage("Change your title")

            val titleChange = EditText(activity)
            titleChange.width = 5000

            val layout = LinearLayout(activity)

            layout.addView(titleChange)
            alertdg.setView(layout)

            alertdg.setPositiveButton("Ok") { dialog, which ->
                titleHolder = titleChange.text.toString()

                val Editor = requireActivity().getSharedPreferences(sharePrefFile, MODE_PRIVATE).edit()
                Editor.putString(TITLE_KEY, titleHolder)
                Editor.apply()

                tv_title_home.text = titleHolder

            }

            alertdg.setNegativeButton("Cancel") { dialog, which ->

            }
            alertdg.show()
        }
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