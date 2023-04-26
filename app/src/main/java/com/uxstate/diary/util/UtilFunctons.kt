package com.uxstate.diary.util

import io.realm.kotlin.types.RealmInstant
import java.time.Instant
import java.time.LocalDateTime
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


fun Instant.toStringDate() :String{

    val localDateTime = LocalDateTime.ofInstant(this, ZoneId.systemDefault())

    val pattern = "dd MMM yyy hh:mm a"

    val dateTimeFormatter = DateTimeFormatter.ofPattern(pattern)

    return localDateTime.format(dateTimeFormatter)

}





