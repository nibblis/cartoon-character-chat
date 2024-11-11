package app.test.demochat.di

import app.test.demochat.data.model.AuthParams
import app.test.demochat.data.model.UserParams
import app.test.demochat.presentation.screens.auth.AuthViewModel
import app.test.demochat.presentation.screens.auth.verify_code.VerifyCodeViewModel
import app.test.demochat.presentation.screens.chat.ChatViewModel
import app.test.demochat.presentation.screens.chatList.ChatListViewModel
import app.test.demochat.presentation.screens.main.MainViewModel
import app.test.demochat.presentation.screens.profile.ProfileViewModel
import app.test.demochat.presentation.screens.profile.edit.EditProfileViewModel
import app.test.demochat.presentation.screens.registration.RegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get(), getAll()) }
    viewModelOf(::AuthViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::EditProfileViewModel)
    viewModelOf(::VerifyCodeViewModel)
    viewModelOf(::ChatListViewModel)

    viewModel { (authParams: AuthParams) ->
        VerifyCodeViewModel(authParams, get(), get(), get())
    }
    viewModel { (userParams: UserParams) ->
        RegistrationViewModel(userParams, get(), get(), get())
    }
    viewModel { (chatId: String) ->
        ChatViewModel(chatId, get(), get())
    }
}