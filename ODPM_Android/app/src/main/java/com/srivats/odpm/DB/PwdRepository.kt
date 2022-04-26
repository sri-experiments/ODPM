package com.srivats.odpm.DB

import androidx.lifecycle.LiveData

class PwdRepository(private val pwdDatabaseDao: PwdDatabaseDao) {

    val readAllData: LiveData<List<PwdItem>> = pwdDatabaseDao.getAll()

    suspend fun addSitePwd(pwdItem: PwdItem) {
        pwdDatabaseDao.insert(pwdItem)
    }

    suspend fun updateSitePwd(pwdItem: PwdItem) {
        pwdDatabaseDao.update(pwdItem)
    }

    suspend fun deleteSitePwd(pwdItem: PwdItem) {
        pwdDatabaseDao.delete(pwdItem)
    }

    suspend fun deleteAllSitePwd() {
        pwdDatabaseDao.deleteAllSitePwd()
    }
}