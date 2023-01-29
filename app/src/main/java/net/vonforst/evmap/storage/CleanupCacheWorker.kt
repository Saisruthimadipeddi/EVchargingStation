package net.vonforst.evmap.storage

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import net.vonforst.evmap.api.createApi

class CleanupCacheWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val dao = AppDatabase.getInstance(applicationContext).chargeLocationsDao()
        for (type in listOf("openchargemap", "goingelectric")) {
            val api = createApi(type, applicationContext)
            dao.deleteOutdatedIfNotFavorite(type, api.cacheLimitDate())
        }
        return Result.success()
    }
}