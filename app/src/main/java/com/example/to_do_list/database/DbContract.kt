package com.example.to_do_list.database

import android.provider.BaseColumns
// database creator for database table used in app

object DbContract {

    object DataEntry: BaseColumns {
        const val TABLE_NAME = "schedule"
        const val COLUMN_SCHED_NAME = "name"
        const val COLUMN_DETAIL = "detail"
        const val COLUMN_DATE_DAY = "day"
        const val COLUMN_DATE_MONTH = "month"
        const val COLUMN_DATE_YEAR = "year"
        const val COLUMN_TIME_HOUR = "hour"
        const val COLUMN_TIME_MINUTE = "minutes"
    }
}