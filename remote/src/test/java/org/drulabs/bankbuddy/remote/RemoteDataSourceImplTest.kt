package org.drulabs.bankbuddy.remote

import org.drulabs.bankbuddy.remote.api.BankingService
import org.drulabs.bankbuddy.remote.mapper.TransactionDataNetworkMapper
import org.drulabs.bankbuddy.remote.mapper.UserInfoDataNetworkMapper
import org.drulabs.bankbuddy.remote.model.ResponseWrapper
import org.drulabs.bankbuddy.remote.source.RemoteDataSourceImpl
import org.drulabs.bankbuddy.remote.utils.TestDataGenerator
import io.reactivex.Observable
import org.drulabs.bankbuddy.data.repository.RemoteDataSource
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class RemoteDataSourceImplTest {

    @Mock
    private lateinit var bankingService: BankingService

    private val userInfoMapper = UserInfoDataNetworkMapper()
    private val transactionMapper = TransactionDataNetworkMapper()

    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        remoteDataSource = RemoteDataSourceImpl(
            userInfoMapper,
            transactionMapper,
            bankingService
        )
    }

    @Test
    fun test_getUserInfo_success() {
        val userInfoNetwork = TestDataGenerator.generateUserInfo()
        val transactions = TestDataGenerator.generateTransactions()
        val userIdentifier = "AEZ19EDH2QZ"

        val mockResponse = ResponseWrapper(
            userInfoNetwork,
            transactions
        )

        Mockito.`when`(bankingService.getUserInformation(userIdentifier))
            .thenReturn(Observable.just(mockResponse))

        remoteDataSource.getUserInfo(userIdentifier)
            .test()
            .assertSubscribed()
            .assertValue {
                val data = userInfoMapper.to(it)
                data == userInfoNetwork
            }
            .assertComplete()

        Mockito.verify(bankingService, times(1))
            .getUserInformation(userIdentifier)
    }

    @Test
    fun test_getUserInfo_error() {
        val userIdentifier = "AEZ19EDH2QZ"
        val errorMsg = "ERROR"

        Mockito.`when`(bankingService.getUserInformation(userIdentifier))
            .thenReturn(Observable.error(Throwable(errorMsg)))

        remoteDataSource.getUserInfo(userIdentifier)
            .test()
            .assertSubscribed()
            .assertError {
                it.message == errorMsg
            }
            .assertNotComplete()
    }

    @Test
    fun test_getUserTransactions_success() {
        val userInfoNetwork = TestDataGenerator.generateUserInfo()
        val transactions = TestDataGenerator.generateTransactions()
        val userIdentifier = "AEZ19EDH2QZ"
        val limit = 10

        val mockResponse = ResponseWrapper(
            userInfoNetwork,
            transactions
        )

        Mockito.`when`(bankingService.getUserInformation(userIdentifier))
            .thenReturn(Observable.just(mockResponse))

        remoteDataSource.getUserTransactions(userIdentifier, limit)
            .test()
            .assertSubscribed()
            .assertValue { transactionsList ->
                transactionsList.containsAll(
                    transactions.map { transactionMapper.from(it) }
                )
            }
            .assertComplete()
    }

    @Test
    fun test_getUserTransactions_error() {
        val userIdentifier = "AEZ19EDH2QZ"
        val errorMsg = "ERROR"
        val limit = 10

        Mockito.`when`(bankingService.getUserInformation(userIdentifier))
            .thenReturn(Observable.error(Throwable(errorMsg)))

        remoteDataSource.getUserTransactions(userIdentifier, limit)
            .test()
            .assertSubscribed()
            .assertError {
                it.message == errorMsg
            }
            .assertNotComplete()
    }

}