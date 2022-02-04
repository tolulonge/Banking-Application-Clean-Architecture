package org.drulabs.bankbuddy.presentation.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.functions.Function
import org.drulabs.bankbuddy.domain.entities.UserInfoEntity
import org.drulabs.bankbuddy.domain.usecases.GetUserInfoTask
import org.drulabs.bankbuddy.presentation.mapper.Mapper
import org.drulabs.bankbuddy.presentation.model.Resource
import org.drulabs.bankbuddy.presentation.model.UserInfo
import org.drulabs.bankbuddy.presentation.qualifier.UserIdentity
import javax.inject.Inject

class HomeVM @Inject internal constructor(
    @UserIdentity private val userIdentifier: String,
    private val userInfoMapper: Mapper<UserInfoEntity, UserInfo>,
    private val getUserInfoTask: GetUserInfoTask
) : ViewModel() {

    val userInfoResource: LiveData<Resource<UserInfo>>
        get() = getUserInfoTask
            .buildUseCase(userIdentifier)
            .map { userInfoMapper.to(it) }
            .map { Resource.success(it) }
            .startWith(Resource.loading())
            .onErrorResumeNext(
                Function {
                    Observable.just(Resource.error(it.localizedMessage))
                }
            ).toFlowable(BackpressureStrategy.LATEST)
            .toLiveData()
}
