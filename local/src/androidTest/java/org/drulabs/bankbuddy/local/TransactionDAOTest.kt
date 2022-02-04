package org.drulabs.bankbuddy.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.drulabs.bankbuddy.local.database.BankBuddyDB
import org.drulabs.bankbuddy.local.database.TransactionDAO
import org.drulabs.bankbuddy.local.utils.TestData
import org.junit.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class TransactionDAOTest {

    private lateinit var bankBuddyDB: BankBuddyDB
    private lateinit var transactionDAO: TransactionDAO

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        bankBuddyDB = Room.inMemoryDatabaseBuilder(context, BankBuddyDB::class.java)
            .allowMainThreadQueries()
            .build()

        transactionDAO = bankBuddyDB.getTransactionDao()
    }

    @After
    fun tearDown() {
        transactionDAO.clearCachedTransactions().subscribe()
        bankBuddyDB.close()
    }

    @Test
    fun test_saveAndRetrieveUserTransactions() {

        val transactions = TestData.generateTransactions()
        val transactionCount = transactions.size

        transactionDAO.addTransactions(transactions)

        transactionDAO.getUserTransactions(20)
            .test()
            .assertValue {
                transactions.containsAll(it)
                        && it.size == transactionCount
            }.assertNotComplete() // As Room Observables are kept alive
    }

    @Test
    fun test_updateTransactionFlaggedStatus() {
        val transactions = TestData.generateTransactions()
        val sampleTransaction = transactions[0].copy(flagged = true)

        transactionDAO.addTransactions(transactions)

        assert(!transactions[0].flagged)

        transactionDAO.updateTransaction(sampleTransaction).subscribe()

        transactionDAO.getTransactionById(sampleTransaction.transactionId)
            .test()
            .assertSubscribed()
            .assertValue { it.flagged }
            .assertNotComplete() // As Room Observables are kept alive
    }

    @Test
    fun test_clearCachedTransactions() {
        val limit = 40
        val transactions = TestData.generateTransactions()

        transactionDAO.addTransactions(transactions)

        transactionDAO.getUserTransactions(limit)
            .test()
            .assertValue {
                it.size == transactions.size
            }

        transactionDAO.clearCachedTransactions().subscribe()

        transactionDAO.getUserTransactions(limit)
            .test()
            .assertValue { it.isEmpty() }
    }

}