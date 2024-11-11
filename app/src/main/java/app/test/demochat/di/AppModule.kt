package app.test.demochat.di

import app.test.demochat.data.api.AuthenticatedApiClient
import app.test.demochat.data.interceptor.AuthInterceptor
import app.test.demochat.data.interceptor.TokenAuthenticator
import app.test.demochat.data.local.PreferencesManager
import app.test.demochat.data.local.UserDataStore
import app.test.demochat.presentation.navigation.INavigation
import app.test.demochat.presentation.navigation.NavigationProvider
import app.test.demochat.presentation.navigation.items.AuthItem
import app.test.demochat.presentation.navigation.items.ChatListItem
import app.test.demochat.presentation.navigation.items.ChatNavItem
import app.test.demochat.presentation.navigation.items.EditProfileItem
import app.test.demochat.presentation.navigation.items.ProfileItem
import app.test.demochat.presentation.navigation.items.RegistrationItem
import app.test.demochat.presentation.navigation.items.VerifyCodeItem
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf(::PreferencesManager)
    singleOf(::UserDataStore)

    singleOf(::AuthInterceptor)
    single {
        TokenAuthenticator(
            get(),
            lazy { get<AuthenticatedApiClient>() }
        )
    }

    single { AuthItem() } bind INavigation::class
    single { ChatListItem() } bind INavigation::class
    single { ChatNavItem() } bind INavigation::class
    single { ProfileItem() } bind INavigation::class
    single { EditProfileItem() } bind INavigation::class
    single { RegistrationItem() } bind INavigation::class
    single { VerifyCodeItem() } bind INavigation::class

    single { NavigationProvider() }
}