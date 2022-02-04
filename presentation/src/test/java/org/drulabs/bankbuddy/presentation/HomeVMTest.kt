package org.drulabs.bankbuddy.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.drulabs.bankbuddy.domain.repository.BankingRepository
import org.drulabs.bankbuddy.domain.usecases.GetUserInfoTask
import org.drulabs.bankbuddy.presentation.mapper.UserInfoEntityMapper
import org.drulabs.bankbuddy.presentation.model.Status
import org.drulabs.bankbuddy.presentation.utils.TestDataGenerator
import org.drulabs.bankbuddy.presentation.viewmodels.HomeVM
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class HomeVMTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: BankingRepository

    private lateinit var homeVM: HomeVM
    private val userInfoMapper = UserInfoEntityMapper()

    private val userInfo = TestDataGenerator.generateUserInfo()
    private val userInfoEntity = userInfoMapper.from(userInfo)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val getUserInfoTask = GetUserInfoTask(
            repository,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )

        homeVM = HomeVM(
            userInfo.accountNumber,
            userInfoMapper,
            getUserInfoTask
        )
    }

    @Test
    fun test_getUserInfo_success() {

        Mockito.`when`(repository.getUserInfo(anyString()))
            .thenReturn(Observable.just(userInfoEntity))

        val userInfoResource = homeVM.userInfoResource

        userInfoResource.observeForever { /*Do nothing*/ }

        assertTrue(
            userInfoResource.value?.status == Status.SUCCESS
                    && userInfoResource.value?.data == userInfo
        )
    }

    @Test
    fun test_getUserInfo_error() {
        val errorMsg = "user info error in fetching data"
        Mockito.`when`(repository.getUserInfo(anyString()))
            .thenReturn(Observable.error(Throwable(errorMsg)))

        val userInfoResource = homeVM.userInfoResource

        userInfoResource.observeForever { /*Do nothing*/ }

        assertTrue(
            userInfoResource.value?.status == Status.ERROR
                    && userInfoResource.value?.message == errorMsg
        )
    }

}