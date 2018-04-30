package com.twt.service.schedule2.model.audit

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import retrofit2.http.GET
import retrofit2.http.Query

interface AuditApi {
    @GET("v1/auditClass/audit")
    fun getMyAudit(@Query("user_number") userNumber: String = CommonPreferences.studentid): Deferred<CommonBody<List<AuditCourse>>>

    @GET("v1/auditClass/popular")
    fun getPopluarAudit(): Deferred<CommonBody<List<AuditPopluar>>>

    @GET("v1/auditClass/college")
    fun getAuditCollege(@Query("with_class") withClass: Int = 1): Deferred<CommonBody<List<AuditCollegeData>>>

    companion object : AuditApi by ServiceFactory()
}

private val auditPopluarLocal = Cache.hawk<List<AuditPopluar>>("schedule2_audit")
private val auditPopluarRemote = Cache.from(AuditApi.Companion::getPopluarAudit).map(CommonBody<List<AuditPopluar>>::data)
val auditPopluarLiveData = RefreshableLiveData.use(auditPopluarLocal, auditPopluarRemote)

val auditCourseLiveData = object : RefreshableLiveData<List<AuditCourse>, CacheIndicator>() {

    override fun observe(owner: LifecycleOwner, observer: Observer<List<AuditCourse>>) {
        super.observe(owner, observer)
        AuditCourseManager.getAuditListLive().observe(owner, observer)
    }

    override fun refresh(vararg indicators: CacheIndicator, callback: suspend (RefreshState<CacheIndicator>) -> Unit) {
        if (indicators == CacheIndicator.REMOTE) {
            async(CommonPool) {
                try {
                    AuditCourseManager.refreshAuditClasstable()
                    callback(RefreshState.Success(CacheIndicator.REMOTE))
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback(RefreshState.Failure(e))
                }
            }
        }
    }

    override fun onActive() {
        refresh(CacheIndicator.REMOTE)
    }

    override fun cancel() {
        // no need to impl
    }

}