package com.example.to_do_list.model

import android.icu.text.CaseMap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//@Parcelize
class Schedule {
    var id: Long = 0
    var title: String = ""
    var detail: String = ""
    var hour: Int = 0
    var minute: Int = 0
    var day: Int = 0
    var month: Int = 0
    var year: Int = 0

    constructor(id: Long, title: String, detail: String, hour: Int, minute: Int, day: Int, month: Int, year: Int){
        this.id = id
        this.title = title
        this.detail = detail
        this.hour = hour
        this.minute = minute
        this.day = day
        this.month = month
        this.year = year
    }
}

//) : Parcelable

//class Schedule {
//
//
//}