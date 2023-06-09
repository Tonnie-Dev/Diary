package com.uxstate.mongo.repository

import android.annotation.SuppressLint
import com.uxstate.model.Diary
import com.uxstate.util.Constants.APP_ID
import com.uxstate.util.RequestState
import com.uxstate.util.toInstant
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
@SuppressLint("NewApi")
 object MongoDB : MongoRepository {
    private val app = App.create(APP_ID)
    private val user = app.currentUser

    private lateinit var realm: Realm

    init {
        configureTheRealm()

    }

    override fun configureTheRealm() {

        if (user != null) {

            val config = SyncConfiguration.Builder(user, setOf(Diary::class))
                    .initialSubscriptions {

                        sub ->

                        add(
                                query = sub.query<Diary>("ownerId == $0", user.id)
                        )

                    }
                    .log(LogLevel.ALL)
                    .build()

            realm = Realm.open(config)

        }
    }


    override fun getAllDiaries(): Flow<Diaries> {


        return authenticateAndInvokeMongoFlowOp(user) {
            try {
                realm.query<Diary>(query = "ownerId == $0", user!!.id)
                        .sort(property = "date", sortOrder = Sort.DESCENDING)
                        .asFlow()
                        .map { result ->

                            RequestState.Success(data = result.list.groupBy {
                                it.date.toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                            })
                        }

            } catch (e: Exception) {


                flow { emit(RequestState.Error(e)) }
            }
        }
    }

    override fun getSelectedDiary(diaryId: ObjectId): Flow<RequestState<Diary>> {


        return authenticateAndInvokeMongoFlowOp(user) {

            try {

                /*
                 -we call realm object to query diary from the database

                - realm. query Returns a RealmQuery matching the predicate represented by query.

                - we specify the column (_id) and column index (0)

                - we then pass in the diaryId to match
                */
                realm.query<Diary>(query = "_id == $0", diaryId)

                        .asFlow()
                        .map {

                            RequestState.Success(data = it.list.first())

                        }

                //RequestState.Success(diary)
            } catch (e: Exception) {


                flow { emit(RequestState.Error(e)) }
            }

        }


    }


    override fun getFilteredDiaries(zonedDateTime: ZonedDateTime): Flow<Diaries> {

        return authenticateAndInvokeMongoFlowOp(user) {

            try {

                //range for diaries created on the same day
                val tomorrowMidNight = LocalDateTime.of(
                        zonedDateTime.toLocalDate()
                                .plusDays(1), LocalTime.MIDNIGHT
                )
                        .toEpochSecond(zonedDateTime.offset)

                val todayMidNight = LocalDateTime.of(
                        zonedDateTime.toLocalDate(), LocalTime.MIDNIGHT
                )
                        .toEpochSecond(zonedDateTime.offset)

                realm.query<Diary>(
                        "ownerId == $0 AND date < $1 AND date > $2",
                        user!!.id,
                        RealmInstant.from(tomorrowMidNight,0),
                        RealmInstant.from(todayMidNight,0)
                ).asFlow().map {result ->
                    RequestState.Success(
                            //it is ResultsChange<Diary>
                            //list() is from realm
                            data = result .list.groupBy {

                                //it is diary
                                diary ->
                                diary.date.toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                            }
                    )
                }

            } catch (e: Exception) {


                flow { emit(RequestState.Error(e)) }
            }
        }

    }

    override suspend fun insertDiary(diary: Diary): RequestState<Diary> {

        return authenticateAndInvokeMongoOp(user) {

            realm.write {

                try {
                    val addedDiary = copyToRealm(diary.apply { ownerId = user!!.id })

                    RequestState.Success(data = addedDiary)
                } catch (e: Exception) {
                    RequestState.Error(e)

                }

            }
        }

    }

    override suspend fun updateDiary(diary: Diary): RequestState<Diary> {
        return authenticateAndInvokeMongoOp(user) {
            realm.write {

                /*
                - find() returns all the objects that fulfill the query
                - first() returns the first matching object
                */
                val queriedDiary = query<Diary>("_id == $0", diary._id)
                        .find()
                        .first()

                queriedDiary.title = diary.title
                queriedDiary.description = diary.description
                queriedDiary.mood = diary.mood
                queriedDiary.images = diary.images
                queriedDiary.date = diary.date
                RequestState.Success(data = queriedDiary)

            }
        }
    }

    override suspend fun deleteDiary(id: ObjectId): RequestState<Diary> {

        return authenticateAndInvokeMongoOp(user) {
            realm.write {

                val diary =
                    query<Diary>(query = "_id == $0 AND ownerId == $1", id, user!!.id)
                            .find()
                            .first()
                try {

                    delete(diary)

                    RequestState.Success(data = diary)

                } catch (e: Exception) {

                    //first() throws NoSuchElementException
                    RequestState.Error(e)
                }

            }
        }
    }

    override suspend fun deleteAllDiaries(): RequestState<Boolean> {
        return authenticateAndInvokeMongoOp(user = user) {

            realm.write {

                //retrieve diaries to delete
                val diaries = this.query<Diary>("ownerId == $0", user!!.id)
                        .find()

                try {
                    //call delete
                    delete(diaries)
                    RequestState.Success(data = true)

                } catch (e: Exception) {

                    RequestState.Error(e)
                }
            }

        }
    }

}

private fun <T> authenticateAndInvokeMongoFlowOp(
    user: User?,
    mongoRepoOperation: () -> Flow<RequestState<T>>
): Flow<RequestState<T>> {

    return if (user != null) {

        mongoRepoOperation.invoke()
    } else {

        flow {

            emit(
                    RequestState.Error(UserNotAuthenticatedException())
            )
        }
    }
}


private suspend fun <T> authenticateAndInvokeMongoOp(
    user: User?,
    mongoRepoOperation: suspend () -> RequestState<T>
): RequestState<T> {

    return if (user != null) {

        mongoRepoOperation.invoke()
    } else {

        RequestState.Error(UserNotAuthenticatedException())
    }
}


private class UserNotAuthenticatedException : Exception("User is not Logged In")