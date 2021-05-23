package com.example.to_do_list

import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.to_do_list.adapter.ScheduleAdapter
import com.example.to_do_list.database.DbContract
import com.example.to_do_list.database.ReaderDbHelper
import com.example.to_do_list.model.Schedule
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_list.*
import org.w3c.dom.Text


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

// Displayed Fragment when Home menu is selected
class HomeFragment : Fragment() {

    lateinit var scheduleAdapter: ScheduleAdapter  //inisialisasi adapter untuk recycler view schedule
    val lm = LinearLayoutManager(activity) //inisialisasi linearlayout
    var titleHolder: String = "" //inisialisasi title yang nanti bisa di edit edit
    lateinit var mPreferences: SharedPreferences
    private val sharePrefFile = "com.example.android.to_do_list"
    var mTitle: String = "DEFINE YOUR SCHEDULE"

    val TITLE_KEY = "title"

    lateinit var dbHelper: ReaderDbHelper //database
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

    override fun onCreateView( //menampilkan layout ketika pertama kali masuk ke HomeFragment
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            tv_title_home.text = mTitle
            dbHelper = ReaderDbHelper(requireActivity()) //database dapat diakses jika fragment terikat dengan activity
        val prefs = requireActivity().getSharedPreferences(sharePrefFile, MODE_PRIVATE)

//        checking if TITLE_KEY is holding value, if so then the title is changed
        if (prefs.contains(TITLE_KEY)){
                val loadedString = prefs.getString(TITLE_KEY, null)
                tv_title_home.setText(loadedString) //ganti judul dengan yang user mau
            }

            initView()
            initListener()
    }

//    will called when entering resume phase from closing dialog to edit title
    override fun onResume() {
        super.onResume()
        if (!titleHolder.isBlank()){ //kalau title nya tidak kosong
            tv_title_home.text = titleHolder
        }
        initListener()
        initView()

    }


//    populate recycleview using data from database contained in item_list
    private fun initView() { //inisialisasi tampilan
        val db = dbHelper.readableDatabase
        val projection = arrayOf( //mengisi data ke database
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

//    Adding all data from beginning to the end of database
        val addSchedule = mutableListOf<Schedule>() //list dapat diubah-ubah
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

//    edit title listener button

    fun initListener(){
//          used for sharedPref on title below recycleView container
//        will display dialog to receive text input from user to change title
        bt_edit_title.setOnClickListener{


            val alertdg = AlertDialog.Builder(activity) //untuk munculin kotak alert
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