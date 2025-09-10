package com.calyrsoft.ucbp1.di

import com.calyrsoft.ucbp1.data.api.GithubService
import com.calyrsoft.ucbp1.data.datasource.GithubRemoteDataSource
import com.calyrsoft.ucbp1.data.repository.ExchangeRateRepository
import com.calyrsoft.ucbp1.data.repository.GithubRepository
import com.calyrsoft.ucbp1.data.repository.LoginRepository
import com.calyrsoft.ucbp1.data.repository.WhatsappRepository
import com.calyrsoft.ucbp1.domain.repository.IExchangeRateRepository
import com.calyrsoft.ucbp1.domain.repository.IGithubRepository
import com.calyrsoft.ucbp1.domain.repository.ILoginRepository
import com.calyrsoft.ucbp1.domain.repository.IWhatsappRepository
import com.calyrsoft.ucbp1.domain.usecase.FindByNameAndPasswordUseCase
import com.calyrsoft.ucbp1.domain.usecase.FindByNameUseCase
import com.calyrsoft.ucbp1.domain.usecase.FindByNickNameUseCase
import com.calyrsoft.ucbp1.domain.usecase.GetExchangeRateUseCase
import com.calyrsoft.ucbp1.domain.usecase.GetFirstWhatsappNumberUseCase
import com.calyrsoft.ucbp1.domain.usecase.UpdateUserProfileUseCase
import com.calyrsoft.ucbp1.presentation.ExchangeRateViewModel
import com.calyrsoft.ucbp1.presentation.ForgotPasswordViewModel
import com.calyrsoft.ucbp1.presentation.GithubViewModel
import com.calyrsoft.ucbp1.presentation.LoginViewModel
import com.calyrsoft.ucbp1.presentation.ProfileViewModel
import com.calyrsoft.ucbp1.presentation.WhatsappViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {


    // OkHttpClient
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }


    // Retrofit
    single {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    // GithubService
    single<GithubService> {
        get<Retrofit>().create(GithubService::class.java)
        //get<Retrofit>()busca en el contenedor un objeto que sea de tipo Retrofit.
        //
        //Como solo tienes uno registrado, Koin sabe perfectamente cuál retornar.
        //en caso de tener varios retrofit deberas especificar su nombre mas adelante
    }
    //lo de arriba dice oye koin crea un GithubService para que alguien mas lo use a futuro
    //la manera en la que lo tienes que crear esta entre {}
    //y lo que pasa entre {} es que retrofit tiene un metodo magico llamado create
    //que lo que hace es ver las rutas y metodos(GET,POST,etc) Qque se definierion en GithubService
    //y les da una implementacion por arte de magia porque como tal vos no los implementaste
    single{ GithubRemoteDataSource(get()) }

    //repositorios
    single<IGithubRepository>{ GithubRepository(get()) }
    //eso de ahi crea un singleton de GithubRepository la cosa es que este singleton podra ser usado solo si los programadores ponenIGithubRepository










    single<ILoginRepository> { LoginRepository() }
    single<IExchangeRateRepository> { ExchangeRateRepository() }
    single<IWhatsappRepository> { WhatsappRepository() }



    //casos de uso
    factory { FindByNickNameUseCase(get()) }
    //el get le inyecta la dependencia que necesita, en este caso le inyecta un singleton del github repository que creamos en la linea anterior
    factory { FindByNameAndPasswordUseCase(get()) }
    factory { FindByNameUseCase(get()) }
    factory { UpdateUserProfileUseCase(get()) }
    factory { GetExchangeRateUseCase(get()) }
    factory { GetFirstWhatsappNumberUseCase(get()) }




    //view models
    viewModel { GithubViewModel(get()) }
    //este get le inyecta el caso e uso que ya sabemos como crear en la linea anterior
    viewModel { LoginViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { ExchangeRateViewModel(get()) }
    viewModel { ForgotPasswordViewModel(get(), get()) }
    viewModel { WhatsappViewModel(get ()) }


}

//single = solo se creara una sola vez osea un singleton
//factory es que cada vez se crean nuevas instancias