package org.drulabs.bankbuddy.local.database

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.drulabs.bankbuddy.local.model.TransactionLocal
import org.drulabs.bankbuddy.local.model.UserInfoLocal


@Database(
    entities = [UserInfoLocal::class, TransactionLocal::class],
    version = 1,
    exportSchema = false
)
abstract class BankBuddyDB : RoomDatabase() {

    companion object {
        private val LOCK = Any()
        private const val DATABASE_NAME = "bank_buddy.db"
        @Volatile
        private var INSTANCE: BankBuddyDB? = null

        fun getInstance(@NonNull context: Context): BankBuddyDB {
            if (INSTANCE == null) {
                synchronized(LOCK) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context,
                            BankBuddyDB::class.java,
                            DATABASE_NAME
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    abstract fun getTransactionDao(): TransactionDAO

    abstract fun getUserInfoDao(): UserInfoDAO

}

