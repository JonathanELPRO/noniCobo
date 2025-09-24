package com.calyrsoft.ucbp1.di

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
import com.calyrsoft.ucbp1.features.dollar.data.repository.LoginRepository
import com.calyrsoft.ucbp1.features.movie.data.repository.MoviesRepository
import com.calyrsoft.ucbp1.features.posts.data.repository.PostRepository
import com.calyrsoft.ucbp1.features.dollar.data.repository.WhatsappRepository
import com.calyrsoft.ucbp1.features.dollar.domain.repository.IDollarRepository
import com.calyrsoft.ucbp1.features.github.domain.repository.IGithubRepository
import com.calyrsoft.ucbp1.features.dollar.domain.repository.ILoginRepository
import com.calyrsoft.ucbp1.features.movie.domain.repository.IMoviesRepository
import com.calyrsoft.ucbp1.features.posts.domain.repository.IPostRepository
import com.calyrsoft.ucbp1.features.dollar.domain.repository.IWhatsappRepository
import com.calyrsoft.ucbp1.features.dollar.domain.usecase.DeleteByTimeStampUseCase
import com.calyrsoft.ucbp1.features.dollar.domain.usecase.FindByNameAndPasswordUseCase
import com.calyrsoft.ucbp1.features.dollar.domain.usecase.FindByNameUseCase
import com.calyrsoft.ucbp1.features.github.domain.usecase.FindByNickNameUseCase
import com.calyrsoft.ucbp1.features.posts.domain.usecase.GetCommentsForOnePostUseCase
import com.calyrsoft.ucbp1.features.dollar.domain.usecase.GetDollarFromFireBaseInMyLocalDBUseCase
import com.calyrsoft.ucbp1.features.dollar.domain.usecase.GetFirstWhatsappNumberUseCase
import com.calyrsoft.ucbp1.features.dollar.domain.usecase.GetHistoryOfDollarsFromMyLocalDBUseCase
import com.calyrsoft.ucbp1.features.movie.domain.usecase.GetPopularMoviesUseCase
import com.calyrsoft.ucbp1.features.posts.domain.usecase.GetPostsUseCase
import com.calyrsoft.ucbp1.features.dollar.domain.usecase.UpdateUserProfileUseCase
import com.calyrsoft.ucbp1.features.dollar.presentation.DollarViewModel
import com.calyrsoft.ucbp1.features.dollar.presentation.ForgotPasswordViewModel
import com.calyrsoft.ucbp1.features.github.presentation.GithubViewModel
import com.calyrsoft.ucbp1.features.dollar.presentation.LoginViewModel
import com.calyrsoft.ucbp1.features.movie.presentation.MoviesViewModel
import com.calyrsoft.ucbp1.features.posts.presentation.PostsViewModel
import com.calyrsoft.ucbp1.features.dollar.presentation.ProfileViewModel
import com.calyrsoft.ucbp1.features.dollar.presentation.WhatsappViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
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

    // DataSource
    single{ GithubRemoteDataSource(get()) }
    single { MoviesRemoteDataSource(get()) }
    single { PostsRemoteDataSource(get()) }
    single { RealTimeRemoteDataSource() }



    //repositorios
    single<IGithubRepository>{ GithubRepository(get()) }
    //eso de ahi crea un singleton de GithubRepository la cosa es que este singleton podra ser usado solo si los programadores ponenIGithubRepository
    single<ILoginRepository> { LoginRepository() }
    single<IWhatsappRepository> { WhatsappRepository() }
    single<IMoviesRepository> { MoviesRepository(get()) }
    single<IPostRepository> { PostRepository(get()) }
    single<IDollarRepository> { DollarRepository(get(), get()) }

    single { AppRoomDatabase.getDatabase(get()) }
    single { get<AppRoomDatabase>().dollarDao() }
    single { DollarLocalDataSource(get()) }







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








    //view models
    viewModel { GithubViewModel(get(), androidContext()) }
    //este get le inyecta el caso e uso que ya sabemos como crear en la linea anterior
    viewModel { LoginViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { DollarViewModel(get(),get(),get()) }
    viewModel { ForgotPasswordViewModel(get(), get()) }
    viewModel { WhatsappViewModel(get ()) }
    viewModel { MoviesViewModel(get()) }
    viewModel { PostsViewModel(get(),get()) }





}

//single = solo se creara una sola vez osea un singleton
//factory es que cada vez se crean nuevas instancias