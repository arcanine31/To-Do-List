package com.example.to_do_list

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.to_do_list.database.DbContract
import com.example.to_do_list.database.ReaderDbHelper
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.fragment_add.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private val CHANNEL_ID = "to_do_list"
private val notificationId = 101

/**
 * A simple [Fragment] subclass.
 * Use the [AddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFragment : Fragment() {

    lateinit var dbHelper: ReaderDbHelper
    lateinit var db: SQLiteDatabase

    var mMinute: Int = 0
    var mHour: Int = 0
    var mDay: Int = 0
    var mMonth: Int = 0
    var mYear: Int = 0
    var newRowid: Long = 0L


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
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = ReaderDbHelper(activity!!)
        db = dbHelper.writableDatabase
        createNotificationChannel()
        initListener()
    }

    private fun initListener() {
        val now = Calendar.getInstance()

        btn_cal.setOnClickListener {
            val currentYear: Int = now.get(Calendar.YEAR)
            val currentMonth: Int = now.get(Calendar.MONTH)
            val currentDay: Int = now.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                DatePickerDialog.newInstance(DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->


                    mDay = dayOfMonth
                    mMonth = monthOfYear
                    mYear = year

                    val messageM: String = "" + mDay + "/" + mMonth + "/" + mYear
                    til_scheddate.setText(messageM)


                }, currentYear, currentMonth, currentDay)

            datePickerDialog.setTitle("Pick Date")
            datePickerDialog.setAccentColor(Color.BLUE)
            datePickerDialog.setOkText("Confirm")
            datePickerDialog.setCancelText("Cancel")
            fragmentManager?.let { manager ->
                datePickerDialog.show(manager, "DatePickerDialog")
            }
        }

        btn_clock.setOnClickListener {
            val currentHour: Int = now.get(Calendar.HOUR_OF_DAY)
            val currentMinute: Int = now.get(Calendar.MINUTE)

            val timePickerDialog =
                TimePickerDialog.newInstance({ view, hour, minute, is24hourMode ->


                    mMinute = minute
                    mHour = hour

                    val messageT: String = "" + mHour + ":" + mMinute
                    til_schedtime.setText(messageT)

                }, currentHour, currentMinute, true)
            timePickerDialog.setTitle("Pick Date")
            timePickerDialog.setAccentColor(Color.BLUE)
            timePickerDialog.setOkText("Confirm")
            timePickerDialog.setCancelText("Cancel")
            fragmentManager?.let { manager ->
                timePickerDialog.show(manager, "TimePickerDialog")
            }

        }

        btn_add.setOnClickListener {
            initAddData()
            if (newRowid != -1L){
                createNotification()
            }

            val frHome: FragmentTransaction = activity!!
                .getSupportFragmentManager().beginTransaction()
            frHome.replace(R.id.frag_container, HomeFragment())
            frHome.commit()

        }
    }

    fun initAddData() {
        var title = til_schedtitle.text.toString()
        var detail = til_scheddetail.text.toString()
        var date = til_schedtime.text
        var time = til_schedtime.text
        var day = mDay
        var month = mMonth
        var year = mYear
        var hour = mHour
        var minutes = mMinute


        if (title.isNullOrEmpty()) {
            til_schedtitle.error = "Can't be empty"
            til_schedtitle.requestFocus()
        } else if (date.isNullOrEmpty()) {
            til_scheddate.error = "Silakan isi kebutuhan tanaman"
            til_scheddate.requestFocus()
        } else if (time.isNullOrEmpty()) {
            til_schedtime.error = "Silakan isi jam"
            til_schedtime.requestFocus()
        } else {
            val values = ContentValues().apply {
                put(DbContract.DataEntry.COLUMN_SCHED_NAME, title)
                put(DbContract.DataEntry.COLUMN_DETAIL, detail)
                put(DbContract.DataEntry.COLUMN_DATE_DAY, day)
                put(DbContract.DataEntry.COLUMN_DATE_MONTH, month)
                put(DbContract.DataEntry.COLUMN_DATE_YEAR, year)
                put(DbContract.DataEntry.COLUMN_TIME_HOUR, hour)
                put(DbContract.DataEntry.COLUMN_TIME_MINUTE, minutes)
            }

            newRowid = db.insert(DbContract.DataEntry.TABLE_NAME, null, values)
            if (newRowid == -1L) {
                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()

            }

        }

    }

    fun createNotification(){
        var title = til_schedtitle.text.toString()
        var detail = til_scheddetail.text.toString()
        var date = til_scheddate.text.toString()
        var time = til_schedtime.text.toString()

        val intent = Intent(activity!!, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(activity, 0, intent, 0)

        val charSeq: CharSequence = "Activity Name : " +
                title + "\nDetail : " + detail + "\nDate : " +
                date + "\nTime : " + time

        val builder = NotificationCompat.Builder(activity!!, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("" + title + " has been added")
                .setContentText(detail)
                .setStyle(NotificationCompat.BigTextStyle().bigText(charSeq))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(activity!!)){
            notify(notificationId, builder.build())
        }

    }

    fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "To do list notification"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = activity!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}