package com.uxstate.diary.util

import io.realm.kotlin.types.RealmInstant
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun RealmInstant.toInstant(): Instant {

    val sec: Long = this.epochSeconds
    val nano: Int = this.nanosecondsOfSecond

    return if (sec >= 0)
        Instant.ofEpochSecond(sec, nano.toLong())
    else
        Instant.ofEpochSecond((sec - 1), 1_000_000 + nano.toLong())
}


fun Instant.toStringTime():String  {

    val localDateTime = LocalDateTime.ofInstant(this, ZoneId.systemDefault())

    val pattern = "hh:mm a"

    //Java Time API formatter
    val dateTimeFormatter = DateTimeFormatter.ofPattern(pattern)

    return localDateTime.format(dateTimeFormatter)
}


fun Instant.toStringDateTime() :String{

    val localDateTime = LocalDateTime.ofInstant(this, ZoneId.systemDefault())

    val pattern = "dd MMM yyyy hh:mm a"

    val dateTimeFormatter = DateTimeFormatter.ofPattern(pattern)

    return localDateTime.format(dateTimeFormatter)

}


fun LocalDateTime.toStringDateTime():String {

    val pattern = "dd MMM yyyy hh:mm a"
    val formatter = DateTimeFormatter.ofPattern(pattern)

    return this.format(formatter)

}

fun LocalDate.toStringDate():String {

    val pattern = "dd MMM yyyy"
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return  this.format(formatter)
}


fun LocalTime.toStringTime():String {

    val pattern = "hh:mm a"

    val formatter = DateTimeFormatter.ofPattern(pattern )

    return this.format(formatter)
}

fun Instant.toRealmInstant():RealmInstant{


    val sec:Long = this.epochSecond
    val nano:Int = this.nano

    return if (sec>=0){

        RealmInstant.from(sec,nano)


    }else{

        RealmInstant.from(epochSeconds =sec +1,  nanosecondAdjustment = -1_000_000 + nano )
    }
}





