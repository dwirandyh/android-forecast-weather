package com.dwirandyh.forecastmvvm

import android.app.Application
import com.dwirandyh.forecastmvvm.data.ApixWeahterAPiService
import com.dwirandyh.forecastmvvm.data.db.ForecastDatabase
import com.dwirandyh.forecastmvvm.data.network.ConnectivityInterceptor
import com.dwirandyh.forecastmvvm.data.network.ConnectivityInterceptorImpl
import com.dwirandyh.forecastmvvm.data.network.WeatherNetworkDataSource
import com.dwirandyh.forecastmvvm.data.network.WeatherNetworkDataSourceImpl
import com.dwirandyh.forecastmvvm.repository.ForecastRepository
import com.dwirandyh.forecastmvvm.repository.ForecastRepositoryImpl
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class ForecastApplication : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }

        // this is because connectivityinterceptor and there is class witch implement interface is interface and ConnectivityInterceptorImple is implementation of it
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ApixWeahterAPiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(), instance()) }
    }
}