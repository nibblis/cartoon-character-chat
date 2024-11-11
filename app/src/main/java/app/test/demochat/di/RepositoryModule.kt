package app.test.demochat.di

import app.test.demochat.data.repository.AuthRepository
import app.test.demochat.data.repository.ChatRepository
import app.test.demochat.data.repository.CountryRepository
import app.test.demochat.data.repository.UserRepository
import app.test.demochat.domain.usecase.auth.CheckAuthCodeUseCase
import app.test.demochat.domain.usecase.auth.RegisterUserUseCase
import app.test.demochat.domain.usecase.auth.SendAuthCodeUseCase
import app.test.demochat.domain.usecase.user.GetUserProfileUseCase
import app.test.demochat.domain.usecase.user.UpdateUserProfileUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {

    singleOf(::AuthRepository)
    singleOf(::UserRepository)
    singleOf(::ChatRepository)
    singleOf(::CountryRepository)

    singleOf(::SendAuthCodeUseCase)
    singleOf(::CheckAuthCodeUseCase)
    singleOf(::RegisterUserUseCase)
    singleOf(::GetUserProfileUseCase)
    singleOf(::UpdateUserProfileUseCase)
}