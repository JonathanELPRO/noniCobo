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
import com.calyrsoft.ucbp1.features.github.data.api.GithubService
import com.calyrsoft.ucbp1.features.posts.data.api.PostsService
import com.calyrsoft.ucbp1.features.movie.data.api.TmdbService
import com.calyrsoft.ucbp1.features.dollar.data.database.AppRoomDatabase
import com.calyrsoft.ucbp1.features.dollar.data.datasource.DollarLocalDataSource
import com.calyrsoft.ucbp1.features.github.data.datasource.GithubRemoteDataSource
import com.calyrsoft.ucbp1.features.movie.data.datasource.MoviesRemoteDataSource
import com.calyrsoft.ucbp1.features.posts.data.datasource.PostsRemoteDataSource
import com.calyrsoft.ucbp1.features.dollar.data.datasource.RealTimeRemoteDataSource
import com.calyrsoft.ucbp1.features.dollar.data.repository.DollarRepository
import com.calyrsoft.ucbp1.features.github.data.repository.GithubRepository
import com.calyrsoft.ucbp1.features.profile.data.repository.LoginRepository
import com.calyrsoft.ucbp1.features.movie.data.repository.MoviesRepository
import com.calyrsoft.ucbp1.features.posts.data.repository.PostRepository
import com.calyrsoft.ucbp1.features.whatsapp.data.repository.WhatsappRepository
import com.calyrsoft.ucbp1.features.dollar.domain.repository.IDollarRepository
import com.calyrsoft.ucbp1.features.github.domain.repository.IGithubRepository
import com.calyrsoft.ucbp1.features.profile.domain.repository.ILoginRepository
import com.calyrsoft.ucbp1.features.movie.domain.repository.IMoviesRepository
import com.calyrsoft.ucbp1.features.posts.domain.repository.IPostRepository
import com.calyrsoft.ucbp1.features.whatsapp.domain.repository.IWhatsappRepository
import com.calyrsoft.ucbp1.features.dollar.domain.usecase.DeleteByTimeStampUseCase
import com.calyrsoft.ucbp1.features.profile.domain.usecase.FindByNameAndPasswordUseCase
import com.calyrsoft.ucbp1.features.profile.domain.usecase.FindByNameUseCase
import com.calyrsoft.ucbp1.features.github.domain.usecase.FindByNickNameUseCase
import com.calyrsoft.ucbp1.features.posts.domain.usecase.GetCommentsForOnePostUseCase
import com.calyrsoft.ucbp1.features.dollar.domain.usecase.GetDollarFromFireBaseInMyLocalDBUseCase
import com.calyrsoft.ucbp1.features.whatsapp.domain.usecase.GetFirstWhatsappNumberUseCase
import com.calyrsoft.ucbp1.features.dollar.domain.usecase.GetHistoryOfDollarsFromMyLocalDBUseCase
import com.calyrsoft.ucbp1.features.movie.domain.usecase.GetPopularMoviesUseCase
import com.calyrsoft.ucbp1.features.posts.domain.usecase.GetPostsUseCase
import com.calyrsoft.ucbp1.features.profile.domain.usecase.UpdateUserProfileUseCase
import com.calyrsoft.ucbp1.features.dollar.presentation.DollarViewModel
import com.calyrsoft.ucbp1.features.profile.presentation.ForgotPasswordViewModel
import com.calyrsoft.ucbp1.features.github.presentation.GithubViewModel
import com.calyrsoft.ucbp1.features.lodging.data.datasource.LodgingLocalDataSource
import com.calyrsoft.ucbp1.features.lodging.data.datasource.LodgingRemoteDataSource
import com.calyrsoft.ucbp1.features.lodging.data.datasource.RealTimeRemoteDataSource2
import com.calyrsoft.ucbp1.features.lodging.data.repository.LodgingRepository
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.AddLodgingUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetAddinRealTime
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetAllLodgingsByAdminUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetAllLodgingsFromSupaBaseUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetLodgingDetailsFromSupbaseUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetLodgingDetailsUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.ObserveAllLocalLodgingsUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.SearchByNameAndAdminIdUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.SearchLodgingByNameUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.UpsertLodgingUseCase
import com.calyrsoft.ucbp1.features.lodging.presentation.LodgingDetailsViewModel
import com.calyrsoft.ucbp1.features.lodging.presentation.LodgingEditorViewModel
import com.calyrsoft.ucbp1.features.lodging.presentation.LodgingListViewModel
import com.calyrsoft.ucbp1.features.movie.data.database.AppRoomDatabaseMovies
import com.calyrsoft.ucbp1.features.movie.data.datasource.MovieLocalDataSource
import com.calyrsoft.ucbp1.features.movie.domain.usecase.GetFavoritesUseCase
import com.calyrsoft.ucbp1.features.movie.domain.usecase.InserteMyFavoriteMovieUseCase
import com.calyrsoft.ucbp1.features.profile.presentation.LoginViewModel
import com.calyrsoft.ucbp1.features.movie.presentation.MoviesViewModel
import com.calyrsoft.ucbp1.features.posts.presentation.PostsViewModel
import com.calyrsoft.ucbp1.features.profile.data.datasource.LoginDataStore
import com.calyrsoft.ucbp1.features.profile.domain.usecase.GetTokenUseCase
import com.calyrsoft.ucbp1.features.profile.domain.usecase.GetUserNameUseCase
import com.calyrsoft.ucbp1.features.profile.domain.usecase.SaveTokenUseCase
import com.calyrsoft.ucbp1.features.profile.domain.usecase.SaveUserNameUseCase
import com.calyrsoft.ucbp1.features.profile.presentation.ProfileViewModel
import com.calyrsoft.ucbp1.features.reservation.data.datasource.ReservationLocalDataSource
import com.calyrsoft.ucbp1.features.reservation.data.repository.PaymentRepository
import com.calyrsoft.ucbp1.features.reservation.data.repository.ReservationRepository
import com.calyrsoft.ucbp1.features.reservation.domain.repository.IPaymentRepository
import com.calyrsoft.ucbp1.features.reservation.domain.repository.IReservationRepository
import com.calyrsoft.ucbp1.features.reservation.domain.usecase.CreateReservationUseCase
import com.calyrsoft.ucbp1.features.reservation.domain.usecase.GetReservationsByUserUseCase
import com.calyrsoft.ucbp1.features.reservation.domain.usecase.RecordAdvancePaymentUseCase
import com.calyrsoft.ucbp1.features.reservation.domain.usecase.RecordRemainingPaymentUseCase
import com.calyrsoft.ucbp1.features.reservation.presentation.ReservationViewModel
import com.calyrsoft.ucbp1.features.whatsapp.presentation.WhatsappViewModel
import com.calyrsoft.ucbp1.navigation.NavigationViewModel
import com.example.imperium_reality.features.register.data.api.LodgingService
import com.example.ucbp1.features.register.data.api.RegisterService
import com.example.ucbp1.interceptors.supabase.SupabaseAuthInterceptor
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
    single { AppRoomDatabase.getDatabase(get()) }
    single { get<AppRoomDatabase>().dollarDao() }

    single { AppRoomDatabaseMovies.getDatabase(get()) }
    single { get<AppRoomDatabaseMovies>().movieDao() }

    // OkHttpClient
    single {
        OkHttpClient.Builder()
            .addInterceptor(SupabaseAuthInterceptor(BuildConfig.SUPABASE_KEY))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    //SUPABASE
    single(named("SUPABASE")) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SUPABASE_URL) // e.g., "https://your-project.supabase.co/rest/v1/"
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



    // Services
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
    single<TmdbService> {
        get<Retrofit>(named("TMDB")).create(TmdbService::class.java)
    }
    single<PostsService> {
        get<Retrofit>(named("PPS")).create(PostsService::class.java)
    }

    //DATA STORE
    single { AuthDataStore(androidContext()) }
    single<IAuthDataStoreRepository>{ AuthDataStoreRepository(get()) }

    viewModel { AuthViewModel(get()) }

    // DataSource
    single{ GithubRemoteDataSource(get()) }
    single { MoviesRemoteDataSource(get()) }
    single { PostsRemoteDataSource(get()) }
    single { RealTimeRemoteDataSource() }
    single { MovieLocalDataSource(get())}
    single { DollarLocalDataSource(get()) }
    single { LoginDataStore(get()) }






    //repositorios
    single<IGithubRepository>{ GithubRepository(get()) }
    //eso de ahi crea un singleton de GithubRepository la cosa es que este singleton podra ser usado solo si los programadores ponenIGithubRepository
    single<ILoginRepository> { LoginRepository(get()) }
    single<IWhatsappRepository> { WhatsappRepository() }
    single<IMoviesRepository> { MoviesRepository(get(), get()) }
    single<IPostRepository> { PostRepository(get()) }
    single<IDollarRepository> { DollarRepository(get(), get()) }











    //casos de uso
    factory { FindByNickNameUseCase(get()) }
    //el get le inyecta la dependencia que necesita, en este caso le inyecta un singleton del github repository que creamos en la linea anterior
    factory { FindByNameAndPasswordUseCase(get()) }
    factory { FindByNameUseCase(get()) }
    factory { UpdateUserProfileUseCase(get()) }
    factory { GetDollarFromFireBaseInMyLocalDBUseCase(get()) }
    factory { GetFirstWhatsappNumberUseCase(get()) }
    factory { GetPopularMoviesUseCase(get()) }
    factory { GetPostsUseCase(get()) }
    factory { GetCommentsForOnePostUseCase(get()) }
    factory { GetHistoryOfDollarsFromMyLocalDBUseCase(get()) }
    factory { DeleteByTimeStampUseCase(get()) }
    factory { InserteMyFavoriteMovieUseCase(get()) }
    factory { GetFavoritesUseCase(get()) }
    factory { GetTokenUseCase(get()) }
    factory { GetUserNameUseCase(get()) }
    factory { SaveTokenUseCase(get()) }
    factory { SaveUserNameUseCase(get()) }
    factory{ GetUserRole(get()) }
    factory { SaveUserDataStore(get()) }











    //view models
    viewModel { GithubViewModel(get(), androidContext()) }
    //este get le inyecta el caso e uso que ya sabemos como crear en la linea anterior
    viewModel { LoginViewModel(get(),get(),get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { DollarViewModel(get(),get(),get()) }
    viewModel { ForgotPasswordViewModel(get(), get()) }
    viewModel { WhatsappViewModel(get ()) }
    viewModel { MoviesViewModel(get(), get(), get()) }
    viewModel { PostsViewModel(get(),get()) }
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
    single<LodgingService> {
        get<Retrofit>(named("SUPABASE")).create(LodgingService::class.java)
    }
    single<LoginService> {
        get<Retrofit>(named("SUPABASE")).create(LoginService::class.java)
    }
    single<GetUserService> {
        get<Retrofit>(named("SUPABASE")).create(GetUserService::class.java)
    }
    single{ RegisterRemoteDataSource(get(),get()) }
    single{ GetUserRemoteDataSource(get()) }

    single{ RealTimeRemoteDataSource2() }

    // --- REPOSITORIO ---
    single<IAuthRepository> { AuthRepository(get(),get(),get(),get()) }

    // --- CASOS DE USO ---
    factory { RegisterUserUseCase(get()) }
    factory { RegisterToSupabaseUserCase(get()) }
    factory { LoginUseCase(get()) }
    factory { LoginWithSupabaseUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }
    factory { GetCurrentUserByEmailUseCase(get()) }


    // --- VIEWMODEL ---
    viewModel { LoginViewModel2(get(),get(),get(),get()) }
    viewModel { RegisterViewModel(get(), get(),get(),androidContext()) }



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

    // --- VIEWMODELS ---
    viewModel { LodgingListViewModel(get(),get(),get(), get(),get(),get(),get()) }
    viewModel { LodgingDetailsViewModel(get(),get()) }
    viewModel { LodgingEditorViewModel(get(),get()) }





    // DAOs
    single { get<AppRoomDatabaseProject>().reservationDao() }
    single { get<AppRoomDatabaseProject>().paymentDao() }

    // DataSource
    single { ReservationLocalDataSource(get(), get()) }

    // Repositorios
    single<IReservationRepository> { ReservationRepository(get()) }
    single<IPaymentRepository> { PaymentRepository(get()) }

    // Casos de uso
    factory { CreateReservationUseCase(get()) }
    factory { GetReservationsByUserUseCase(get()) }
    factory { RecordAdvancePaymentUseCase(get()) }
    factory { RecordRemainingPaymentUseCase(get()) }

    // ViewModel
    viewModel { ReservationViewModel(get(), get(), get(), get()) }






}

//single = solo se creara una sola vez osea un singleton
//factory es que cada vez se crean nuevas instancias