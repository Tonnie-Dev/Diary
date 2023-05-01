package com.uxstate.diary.data.repository

import com.uxstate.diary.domain.model.Diary
import com.uxstate.diary.domain.repository.Diaries
import com.uxstate.diary.domain.repository.MongoRepository
import com.uxstate.diary.util.Constants.APP_ID
import com.uxstate.diary.util.RequestState
import com.uxstate.diary.util.toInstant
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import timber.log.Timber
import java.time.ZoneId

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


        return if (user != null) {


            try {
                realm.query<Diary>(query = "ownerId == $0", user.id)
                        .sort(property = "date", sortOrder = Sort.DESCENDING)
                        .asFlow()
                        .map { result ->
                            Timber.i("List size is: ${result.list.size}")
                            Timber.i("The data is: ${result.list}")
                            RequestState.Success(data = result.list.groupBy {
                                it.date.toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                            })
                        }

            } catch (e: Exception) {

                Timber.i("Error Caught")
                flow { emit(RequestState.Error(e)) }
            }


        } else {
            Timber.i(" The User is Null")
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override fun getSelectedDiary(diaryId: ObjectId): Flow<RequestState<Diary>> {


        return if (user != null) {

            try {


                /*
                 -we call realm object to query diary from the database

                - realm. query Returns a RealmQuery matching the predicate represented by query.

                - we specify the column (_id) and column index (0)

                - we then pass in the diaryId to match

                - find() returns all the objects that fulfill the query

                - first() returns the first matching object
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
        } else {

            flow {

                emit(
                        RequestState.Error(UserNotAuthenticatedException())
                )

            }

        }
    }

    override suspend fun insertDiary(diary: Diary): RequestState<Diary> {

        return if (user != null) {

            realm.write {

                try {

                    val addedDiary = copyToRealm(diary.apply { ownerId = user.id })

                    RequestState.Success(data = addedDiary)
                } catch (e: Exception) {
                    RequestState.Error(e)

                }

            }
        } else {

            RequestState.Error(UserNotAuthenticatedException())
        }

    }

    override suspend fun updateDiary(diary: Diary): RequestState<Diary> {
        return authenticateAndInvokeMongoOp(user) {
            realm.write {

                val queriedDiary = query<Diary>("_id == $0", diary._id)
                        .first()
                        .find()


                if (queriedDiary != null) {

                    queriedDiary.title = diary.title
                    queriedDiary.description = diary.description
                    queriedDiary.mood = diary.mood
                    queriedDiary.images = diary.images
                    queriedDiary.date = diary.date
                    RequestState.Success(data = queriedDiary)
                } else {

                    RequestState.Error(Exception("Queried Diary Doesn't Exist"))
                }
            }
        }
    }

    override suspend fun deleteDiary(id: ObjectId): RequestState<Diary> {
        return authenticateAndInvokeMongoOp(user) {
            realm.write {

                try {
                    val diary =
                        query<Diary>(query = "_id == $0 AND ownerId == $1", id, user?.id)
                                .find()
                                .first()
                    delete(diary)
                    RequestState.Success(data = diary)
                } catch (e: Exception) {

                    //first() throws NosuchElementException
                    RequestState.Error(e)
                }

            }
        }
    }


    /* override suspend fun updateDiary(diary: Diary): RequestState<Diary> {
         return if (user != null) {

             realm.write {

                 val queriedDiary = query<Diary>("_id == $0", diary._id)
                         .first()
                         .find()


                 if (queriedDiary != null) {

                     queriedDiary.title = diary.title
                     queriedDiary.description = diary.description
                     queriedDiary.mood = diary.mood
                     queriedDiary.images = diary.images
                     queriedDiary.date = diary.date
                     RequestState.Success(data = queriedDiary)
                 } else {

                     RequestState.Error(Exception("Queried Diary Doesn't Exist"))
                 }
             }
         } else {

             RequestState.Error(error = UserNotAuthenticatedException())
         }
     }*/


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