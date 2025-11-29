package com.calyrsoft.ucbp1.di

import com.calyrsoft.ucbp1.BuildConfig
import com.calyrsoft.ucbp1.dataStore.AuthDataStore
import com.calyrsoft.ucbp1.features.auth.data.api.GetUserService
import com.calyrsoft.ucbp1.features.auth.data.api.LoginService
import com.calyrsoft.ucbp1.features.auth.data.database.AppRoomDatabaseProject
import com.calyrsoft.ucbp1.features.auth.data.datasource.AuthLocalDataSource
import com.calyrsoft.ucbp1.features.auth.data.datasource.GetUserRemoteDataSource
import com.calyrsoft.ucbp1.features.auth.data.datasource.RegisterRemoteDataSource
import com.calyrsoft.ucbp1.features.auth.data.repository.AuthDataStoreRepository
import com.calyrsoft.ucbp1.features.auth.data.repository.AuthRepository
import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthDataStoreRepository
import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthRepository
import com.calyrsoft.ucbp1.features.auth.domain.usecase.GetCurrentUserByEmailUseCase
import com.calyrsoft.ucbp1.features.auth.domain.usecase.GetCurrentUserUseCase
import com.calyrsoft.ucbp1.features.auth.domain.usecase.GetUserRole
import com.calyrsoft.ucbp1.features.auth.domain.usecase.LoginUseCase
import com.calyrsoft.ucbp1.features.auth.domain.usecase.LoginWithSupabaseUseCase
import com.calyrsoft.ucbp1.features.auth.domain.usecase.RegisterToSupabaseUserCase
import com.calyrsoft.ucbp1.features.auth.domain.usecase.RegisterUserUseCase
import com.calyrsoft.ucbp1.features.auth.domain.usecase.SaveUserDataStore
import com.calyrsoft.ucbp1.features.auth.presentation.AuthViewModel
import com.calyrsoft.ucbp1.features.auth.presentation.LoginViewModel2
import com.calyrsoft.ucbp1.features.auth.presentation.RegisterViewModel
import com.calyrsoft.ucbp1.features.whatsapp.data.repository.WhatsappRepository
import com.calyrsoft.ucbp1.features.whatsapp.domain.repository.IWhatsappRepository
import com.calyrsoft.ucbp1.features.whatsapp.domain.usecase.GetFirstWhatsappNumberUseCase
import com.calyrsoft.ucbp1.features.lodging.data.datasource.LodgingLocalDataSource
import com.calyrsoft.ucbp1.features.lodging.data.datasource.LodgingRemoteDataSource
import com.calyrsoft.ucbp1.features.lodging.data.datasource.RealTimeRemoteDataSource2
import com.calyrsoft.ucbp1.features.lodging.data.repository.LodgingRepository
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.AddLodgingUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.EditLodgingUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetAddinRealTime
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetAllLodgingsByAdminUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetAllLodgingsFromSupaBaseUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetLodgingDetailsFromSupbaseUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetLodgingDetailsUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.ObserveAllLocalLodgingsUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.SearchByNameAndAdminIdUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.SearchLodgingByNameUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.UploadImageToSupabaseUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.UpsertLodgingUseCase
import com.calyrsoft.ucbp1.features.lodging.presentation.LodgingDetailsViewModel
import com.calyrsoft.ucbp1.features.lodging.presentation.LodgingEditorViewModel
import com.calyrsoft.ucbp1.features.lodging.presentation.LodgingListViewModel
import com.calyrsoft.ucbp1.features.payments.data.repository.PaymentRepository
import com.calyrsoft.ucbp1.features.payments.domain.repository.IPaymentRepository
import com.calyrsoft.ucbp1.features.payments.domain.usecase.SendPaymentWhatsAppUseCase
import com.calyrsoft.ucbp1.features.payments.presentation.PaymentViewModel
import com.calyrsoft.ucbp1.features.profile.data.api.UpdateService
import com.calyrsoft.ucbp1.features.profile.data.datasource.UpdateRemoteDataSource
import com.calyrsoft.ucbp1.features.profile.data.repository.UpdateRepository
import com.calyrsoft.ucbp1.features.profile.domain.repository.IUpdateRepository
import com.calyrsoft.ucbp1.features.profile.domain.usecase.UpdateUserPasswordUseCase
import com.calyrsoft.ucbp1.features.profile.domain.usecase.UpdateUserUseCase
import com.calyrsoft.ucbp1.features.profile.presentation.ProfileViewModel
import com.calyrsoft.ucbp1.features.whatsapp.presentation.WhatsappViewModel
import com.calyrsoft.ucbp1.navigation.NavigationViewModel
import com.example.imperium_reality.features.register.data.api.LodgingService
import com.example.ucbp1.features.register.data.api.RegisterService
import com.example.ucbp1.interceptors.supabase.SupabaseAuthInterceptor
import com.calyrsoft.ucbp1.features.supabase.SupabaseStorageDataSource
import com.calyrsoft.ucbp1.features.supabase.SupabaseStorageService
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {


    //BASES DE DATOS

    // OkHttpClient
    single {
        OkHttpClient.Builder()
            .addInterceptor(SupabaseAuthInterceptor(BuildConfig.SUPABASE_KEY, get()))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    //SUPABASE
    single(named("SUPABASE")) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SUPABASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Retrofits
    single {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single(named("TMDB")) {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single(named("PPS")) {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }




    //lo de arriba dice oye koin crea un GithubService para que alguien mas lo use a futuro
    //la manera en la que lo tienes que crear esta entre {}
    //y lo que pasa entre {} es que retrofit tiene un metodo magico llamado create
    //que lo que hace es ver las rutas y metodos(GET,POST,etc) Qque se definierion en GithubService
    //y les da una implementacion por arte de magia porque como tal vos no los implementaste


    //DATA STORE
    single { AuthDataStore(androidContext()) }
    single<IAuthDataStoreRepository>{ AuthDataStoreRepository(get()) }

    viewModel { AuthViewModel(get()) }

    // DataSource







    //repositorios
    //eso de ahi crea un singleton de GithubRepository la cosa es que este singleton podra ser usado solo si los programadores ponenIGithubRepository
    single<IWhatsappRepository> { WhatsappRepository() }











    //casos de uso
    //el get le inyecta la dependencia que necesita, en este caso le inyecta un singleton del github repository que creamos en la linea anterior
    factory { GetFirstWhatsappNumberUseCase(get()) }
    factory{ GetUserRole(get()) }
    factory { SaveUserDataStore(get()) }



    //view models
    //este get le inyecta el caso e uso que ya sabemos como crear en la linea anterior
    viewModel { WhatsappViewModel(get ()) }
    viewModel { NavigationViewModel() }


    //proyecto
    // --- BASE DE DATOS ---


    single<AppRoomDatabaseProject> { AppRoomDatabaseProject.getDatabase(get()) }
    single { get<AppRoomDatabaseProject>().userDao() }

    // --- DATA SOURCE ---
    single { AuthLocalDataSource(get()) }

    single<RegisterService> {
        get<Retrofit>(named("SUPABASE")).create(RegisterService::class.java)
    }
    single<SupabaseStorageService> {
        get<Retrofit>(named("SUPABASE")).create(SupabaseStorageService::class.java)
    }
    single { SupabaseStorageDataSource(get()) }
    single<LodgingService> {
        get<Retrofit>(named("SUPABASE")).create(LodgingService::class.java)
    }
    single<LoginService> {
        get<Retrofit>(named("SUPABASE")).create(LoginService::class.java)
    }
    single<GetUserService> {
        get<Retrofit>(named("SUPABASE")).create(GetUserService::class.java)
    }
    single<UpdateService> {
        get<Retrofit>(named("SUPABASE")).create(UpdateService::class.java)
    }

    single{ RegisterRemoteDataSource(get(),get()) }
    single{ UpdateRemoteDataSource(get()) }
    single{ GetUserRemoteDataSource(get()) }


    single{ RealTimeRemoteDataSource2() }

    // --- REPOSITORIO ---
    single<IAuthRepository> { AuthRepository(get(),get(),get(),get()) }
    single<IUpdateRepository> { UpdateRepository(get()) }
    // --- CASOS DE USO ---
    factory { RegisterUserUseCase(get()) }
    factory { RegisterToSupabaseUserCase(get()) }
    factory { LoginUseCase(get()) }
    factory { LoginWithSupabaseUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }
    factory { GetCurrentUserByEmailUseCase(get()) }
    factory{ UpdateUserUseCase(get()) }
    factory{ UpdateUserPasswordUseCase(get()) }


    // --- VIEWMODEL ---
    viewModel { LoginViewModel2(get(),get(),get(),get()) }
    viewModel { RegisterViewModel(get(), get(),get(),androidContext()) }
    viewModel { ProfileViewModel(get(),get(),get()) }


    // --- BASE DE DATOS ---
    single { get<AppRoomDatabaseProject>().lodgingDao() }

    // --- DATA SOURCE ---
    single { LodgingLocalDataSource(get()) }
    // REMOTE DATA ROURCE
    single { LodgingRemoteDataSource(get()) }
    // --- REPOSITORIO ---
    single<ILodgingRepository> { LodgingRepository(get(),get(),get()) }

    // --- CASOS DE USO ---
    factory { GetLodgingDetailsUseCase(get()) }
    factory { UpsertLodgingUseCase(get()) }
    factory { AddLodgingUseCase(get()) }
    factory { GetAllLodgingsByAdminUseCase(get()) }
    factory{ GetLodgingDetailsFromSupbaseUseCase(get()) }
    factory{ GetAllLodgingsFromSupaBaseUseCase(get())}
    factory{ ObserveAllLocalLodgingsUseCase(get())}
    factory{ GetAddinRealTime(get()) }
    factory{ SearchLodgingByNameUseCase(get()) }
    factory { SearchByNameAndAdminIdUseCase(get()) }
    factory{ EditLodgingUseCase(get()) }
    factory { UploadImageToSupabaseUseCase(get(), androidContext()) }

    // --- VIEWMODELS ---
    viewModel { LodgingListViewModel(get(),get(),get(), get(),get(),get(),get()) }
    viewModel { LodgingDetailsViewModel(get(),get()) }
    viewModel { LodgingEditorViewModel(get(),get(),get()) }


    //payments
    // --- REPOSITORIO ---
    single<IPaymentRepository> { PaymentRepository(androidContext()) }

    // --- CASOS DE USO ---
    factory { SendPaymentWhatsAppUseCase(get()) }

    // ---VIEWMODELS ---
    viewModel { PaymentViewModel(get()) }














}

//single = solo se creara una sola vez osea un singleton
//factory es que cada vez se crean nuevas instancias