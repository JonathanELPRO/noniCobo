package com.calyrsoft.ucbp1.di

import com.calyrsoft.ucbp1.data.repository.GithubRepository
import com.calyrsoft.ucbp1.data.repository.LoginRepository
import com.calyrsoft.ucbp1.domain.repository.IGithubRepository
import com.calyrsoft.ucbp1.domain.repository.ILoginRepository
import com.calyrsoft.ucbp1.domain.usecase.FindByNameAndPasswordUseCase
import com.calyrsoft.ucbp1.domain.usecase.FindByNameUseCase
import com.calyrsoft.ucbp1.domain.usecase.FindByNickNameUseCase
import com.calyrsoft.ucbp1.domain.usecase.UpdateUserProfileUseCase
import com.calyrsoft.ucbp1.presentation.GithubViewModel
import com.calyrsoft.ucbp1.presentation.LoginViewModel
import com.calyrsoft.ucbp1.presentation.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //repositorios
    single<IGithubRepository>{ GithubRepository() }
    //eso de ahi crea un singleton de GithubRepository la cosa es que este singleton podra ser usado solo si los programadores ponenIGithubRepository
    single<ILoginRepository> { LoginRepository() }


    //casos de uso
    factory { FindByNickNameUseCase(get()) }
    //el get le inyecta la dependencia que necesita, en este caso le inyecta un singleton del github repository que creamos en la linea anterior
    factory { FindByNameAndPasswordUseCase(get()) }
    factory { FindByNameUseCase(get()) }
    factory { UpdateUserProfileUseCase(get()) }



    //view models
    viewModel { GithubViewModel(get()) }
    //este get le inyecta el caso e uso que ya sabemos como crear en la linea anterior
    viewModel { LoginViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }

}

//single = solo se creara una sola vez osea un singleton
//factory es que cada vez se crean nuevas instancias