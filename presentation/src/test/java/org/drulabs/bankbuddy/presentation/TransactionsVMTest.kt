package org.drulabs.bankbuddy.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.drulabs.bankbuddy.domain.repository.BankingRepository
import org.drulabs.bankbuddy.domain.usecases.FilterTransactionsTask
import org.drulabs.bankbuddy.domain.usecases.GetUserTransactionsTask
import org.drulabs.bankbuddy.domain.usecases.TransactionStatusUpdaterTask
import org.drulabs.bankbuddy.presentation.mapper.TransactionEntityMapper
import org.drulabs.bankbuddy.presentation.model.Status
import org.drulabs.bankbuddy.presentation.utils.TestDataGenerator
import org.drulabs.bankbuddy.presentation.viewmodels.TransactionsVM
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class TransactionsVMTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: BankingRepository

    private lateinit var transactionsVM: TransactionsVM
    private val transactionMapper = TransactionEntityMapper()

    private val transactions = TestDataGenerator.generateTransactions()
    private val transactionEntities =
        transactions.map { transactionMapper.from(it) }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(repository.getUserTransactions(anyString(), anyInt()))
            .thenReturn(Observable.just(transactionEntities))

        val getUserTransactionsTask = getTransactions()
        val filteredTransactionsTask = getFilteredTransactions()
        val statusUpdaterTask = getStatusUpdaterTask()

        transactionsVM = TransactionsVM(
            "123",
            transactionMapper,
            getUserTransactionsTask,
            filteredTransactionsTask,
            statusUpdaterTask
        )
    }

    @Test
    fun test_getUserTransactions_success() {
        Mockito.`when`(
            repository.getFilteredTransactions(
                anyString(),
                anyBoolean(),
                anyBoolean(),
                anyBoolean()
            )
        ).thenReturn(Observable.just(transactionEntities))

        val transactionListSource = transactionsVM.transactionListSource

        transactionListSource.observeForever { /*Do Nothing*/ }

        assertTrue(
            transactionListSource.value?.status == Status.SUCCESS
                    && transactionListSource.value?.data == transactions
        )
    }

    @Test
    fun test_getUserTransactions_error() {

        val errorMsg = "fetch transaction error"

        Mockito.`when`(
            repository.getFilteredTransactions(
                anyString(),
                anyBoolean(),
                anyBoolean(),
                anyBoolean()
            )
        ).thenReturn(Observable.error(Throwable(errorMsg)))

        val transactionListSource = transactionsVM.transactionListSource

        transactionListSource.observeForever { /*Do Nothing*/ }

        assertTrue(
            transactionListSource.value?.status == Status.ERROR
                    && transactionListSource.value?.message == errorMsg
        )
    }

    @Test
    fun test_filterTransactions_success() {

        Mockito.`when`(
            repository.getFilteredTransactions(
                anyString(),
                anyBoolean(),
                anyBoolean(),
                anyBoolean()
            )
        ).thenReturn(Observable.just(transactionEntities))

        val transactionListSource = transactionsVM.transactionListSource

        transactionListSource.observeForever { /*Do Nothing*/ }

        transactionsVM.filterTransactions(
            credit = false,
            debit = true,
            flagged = true
        )

        assertTrue(
            transactionListSource.value?.status == Status.SUCCESS
                    && transactionListSource.value?.data == transactions
        )
    }

    @Test
    fun test_filterTransactions_error() {

        val errorMsg = "fetch transaction error"

        Mockito.`when`(
            repository.getFilteredTransactions(
                anyString(),
                anyBoolean(),
                anyBoolean(),
                anyBoolean()
            )
        ).thenReturn(Observable.error(Throwable(errorMsg)))

        val transactionListSource = transactionsVM.transactionListSource

        transactionListSource.observeForever { /*Do Nothing*/ }

        transactionsVM.filterTransactions(
            credit = false,
            debit = true,
            flagged = true
        )

        assertTrue(
            transactionListSource.value?.status == Status.ERROR
                    && transactionListSource.value?.message == errorMsg
        )
    }

    @Test
    fun test_transactionStatusUpdater_success() {

        val transaction = transactions[0].copy(flagged = false)
        val transactionEntity = transactionMapper.from(transaction).copy(flagged = true)

        Mockito.`when`(repository.updateTransaction(transactionEntity))
            .thenReturn(Completable.complete())

        transactionsVM.toggleFlaggedStatus(transaction)

        Mockito.verify(repository, times(1))
            .updateTransaction(transactionEntity)
    }

    @Test
    fun test_transactionStatusUpdater_error() {

        val errorMsg = "Error in updating transaction"

        val transaction = transactions[0].copy(flagged = false)
        val transactionEntity = transactionMapper.from(transaction).copy(flagged = true)

        Mockito.`when`(repository.updateTransaction(transactionEntity))
            .thenReturn(Completable.error(Throwable(errorMsg)))

        transactionsVM.toggleFlaggedStatus(transaction)

        Mockito.verify(repository, times(1))
            .updateTransaction(transactionEntity)
    }

    //=====================================//
    // Helper methods for use case creation//
    //=====================================//
    private fun getTransactions(): GetUserTransactionsTask {
        return GetUserTransactionsTask(
            repository,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    private fun getStatusUpdaterTask(): TransactionStatusUpdaterTask {
        return TransactionStatusUpdaterTask(
            repository,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    private fun getFilteredTransactions(): FilterTransactionsTask {
        return FilterTransactionsTask(
            repository,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

}