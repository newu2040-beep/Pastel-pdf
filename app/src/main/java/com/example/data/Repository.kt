package com.example.data

import kotlinx.coroutines.flow.Flow

class Repository(private val db: AppDatabase) {
    val activities: Flow<List<ActivityEntity>> = db.activityDao().getAllActivities()
    val profile: Flow<ProfileEntity?> = db.profileDao().getProfile()

    suspend fun insertActivity(fileName: String, actionType: String) {
        db.activityDao().insertActivity(ActivityEntity(fileName = fileName, actionType = actionType))
    }

    suspend fun updateProfile(profile: ProfileEntity) {
        db.profileDao().saveProfile(profile)
    }
}
