package com.uxstate.data.repository

import com.uxstate.diary.domain.model.Diary
import com.uxstate.diary.domain.repository.MongoRepository
import com.uxstate.diary.util.Constants.APP_ID
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration

object MongoDB : MongoRepository {
    private val app = App.create(APP_ID)
    private val user = app.currentUser

    private lateinit var realm: Realm
    override fun configureTheRealm() {
        //super.configureTheRealm()

        if (user != null) {
            val config = SyncConfiguration.Builder(user, setOf(Diary::class))
                    .initialSubscriptions {

                        sub ->

                        add(
                                query = sub.query("ownerId == $0", user.id),
                                name = "User's Diaries"
                        )
                    }
                    .log(LogLevel.ALL)
                 .build()

            realm = Realm.open(config)
        }
    }
}