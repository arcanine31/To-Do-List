package com.example.to_do_list.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class ReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    private val SQL_CREATE_ENTRY = "CREATE TABLE ${DbContract.DataEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${DbContract.DataEntry.COLUMN_SCHED_NAME} TEXT," +
            "${DbContract.DataEntry.COLUMN_DETAIL} TEXT," +
            "${DbContract.DataEntry.COLUMN_DATE_DAY} INTEGER," +
            "${DbContract.DataEntry.COLUMN_DATE_MONTH} INTEGER," +
            "${DbContract.DataEntry.COLUMN_DATE_YEAR} INTEGER," +
            "${DbContract.DataEntry.COLUMN_TIME_HOUR} INTEGER," +
            "${DbContract.DataEntry.COLUMN_TIME_MINUTE} INTEGER)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

//    override fun onDelete(db: SQLiteDatabase) {
//        db.execSQL(SQL_CREATE_ENTRY)
//    }

    companion object{
        const val DATABASE_NAME = "plant.db"
        const val DATABASE_VERSION = 1
    }


}