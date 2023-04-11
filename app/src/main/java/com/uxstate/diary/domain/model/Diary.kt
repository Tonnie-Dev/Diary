package com.uxstate.diary.domain.model


import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId


//Our class inherits from Realm Obj which is an interface

//we are not allowed to specify properties inside the constructor

//the properties are var

//The @PrimaryKey is from Realm

//ObjectId.create() autogenerates unique identifiers


class Diary : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var ownerID: String = ""
    var title: String = ""
    var description: String = ""
    var images :RealmList<String> = realmListOf()
    var date:RealmInstant = RealmInstant.from(System.currentTimeMillis(), 0)

}