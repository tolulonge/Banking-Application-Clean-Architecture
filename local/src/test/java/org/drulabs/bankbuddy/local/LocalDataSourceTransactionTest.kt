package org.drulabs.bankbuddy.local

import io.reactivex.Completable
import io.reactivex.Observable
import org.drulabs.bankbuddy.data.repository.LocalDataSource
import org.drulabs.bankbuddy.local.database.TransactionDAO
import org.drulabs.bankbuddy.local.database.UserInfoDAO
import org.drulabs.bankbuddy.local.mapper.TransactionDataLocalMapper
import org.drulabs.bankbuddy.local.mapper.UserInfoDataLocalMapper
import org.drulabs.bankbuddy.local.source.LocalDataSourceImpl
import org.drulabs.bankbuddy.local.utils.TestDataGenerator
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class LocalDataSourceTransactionTest {

    private val userInfoMapper = UserInfoDataLocalMapper()
    private val transactionMapper = TransactionDataLocalMapper()

    @Mock
    private lateinit var userInfoDao: UserInfoDAO

    @Mock
    private lateinit var transactionDao: TransactionDAO

    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        localDataSource = LocalDataSourceImpl(
            userInfoMapper,
            transactionMapper,
            userInfoDao,
            transactionDao
        )
    }

    @Test
    fun test_getUserTransactions_success() {
        val userIdentifier = "1BFC9A38E6C7"
        val limit = 20
        val mockTransactions = TestDataGenerator.generateTransactions()

        Mockito.`when`(transactionDao.getUserTransactions(limit))
            .thenReturn(Observable.just(mockTransactions))

        localDataSource.getUserTransactions(userIdentifier, limit)
            .test()
            .assertSubscribed()
            .assertValue { transactions ->
                mockTransactions.containsAll(
                    transactions.map {
                        transactionMapper.to(it)
                    }
                )
            }
    }

    @Test
    fun test_saveUserTransactions_success() {
        val userIdentifier = "1BFC9A38E6C7"
        val mockTransactions = TestDataGenerator.generateTransactions()

        localDataSource.saveUserTransactions(
            userIdentifier,
            mockTransactions.map {
                transactionMapper.from(it)
            }
        )

        Mockito.verify(transactionDao, times(1))
            .addTransactions(mockTransactions)
    }

    @Test
    fun test_getTransactionById_success() {
        val mockTransaction = TestDataGenerator.generateTransactions()[0]
        val transactionId = mockTransaction.transactionId

        Mockito.`when`(transactionDao.getTransactionById(transactionId))
            .thenReturn(Observable.just(mockTransaction))

        localDataSource.getTransaction(transactionId)
            .test()
            .assertSubscribed()
            .assertValue {
                mockTransaction == transactionMapper.to(it)
            }
    }

    @Test
    fun test_getTransactionById_error() {
        val transactionId = "1234abcde"
        val errorMsg = "ERROR"

        Mockito.`when`(transactionDao.getTransactionById(transactionId))
            .thenReturn(Observable.error(Throwable(errorMsg)))

        localDataSource.getTransaction(transactionId)
            .test()
            .assertSubscribed()
            .assertError {
                it.message == errorMsg
            }
            .assertNotComplete()
    }


    @Test
    fun test_getFilteredTransactions_success() {
        val userIdentifier = "1BFC9A38E6C7"
        val mockTransactions = TestDataGenerator.generateTransactions()

        Mockito.`when`(transactionDao.getUserTransactions(40))
            .thenReturn(Observable.just(mockTransactions))

        val testObserver = localDataSource.getFilteredTransactions(
            userIdentifier = userIdentifier,
            credit = true,
            debit = false,
            flagged = true
        ).test()

        testObserver.assertSubscribed()
            .assertValue {
                it.contains(transactionMapper.from(mockTransactions[1]))
                        && it.size == 1

            }.assertComplete()
    }

    @Test
    fun test_updateFlaggedStatus_success() {
        val transactionLocal = TestDataGenerator.generateTransactions()[0]
        val transactionData = transactionMapper.from(transactionLocal)

        Mockito.`when`(transactionDao.updateTransaction(transactionLocal))
            .thenReturn(Completable.complete())

        localDataSource.updateTransaction(transactionData)
            .test()
            .assertSubscribed()
            .assertComplete()

        Mockito.verify(transactionDao, times(1))
            .updateTransaction(transactionLocal)
    }

    @Test
    fun test_updateFlaggedStatus_error() {
        val transactionLocal = TestDataGenerator.generateTransactions()[0]
        val transactionData = transactionMapper.from(transactionLocal)

        val errorMsg = "ERROR"

        Mockito.`when`(transactionDao.updateTransaction(transactionLocal))
            .thenReturn(Completable.error(Throwable(errorMsg)))

        localDataSource.updateTransaction(transactionData)
            .test()
            .assertSubscribed()
            .assertError {
                it.message == errorMsg
            }.assertNotComplete()
    }
}
