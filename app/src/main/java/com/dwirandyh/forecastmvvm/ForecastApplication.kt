package com.dwirandyh.forecastmvvm

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import com.dwirandyh.forecastmvvm.data.network.ApixWeahterAPiService
import com.dwirandyh.forecastmvvm.data.db.ForecastDatabase
import com.dwirandyh.forecastmvvm.data.network.ConnectivityInterceptor
import com.dwirandyh.forecastmvvm.data.network.ConnectivityInterceptorImpl
import com.dwirandyh.forecastmvvm.data.network.WeatherNetworkDataSource
import com.dwirandyh.forecastmvvm.data.network.WeatherNetworkDataSourceImpl
import com.dwirandyh.forecastmvvm.data.provider.LocationProvider
import com.dwirandyh.forecastmvvm.data.provider.LocationProviderImpl
import com.dwirandyh.forecastmvvm.data.provider.UnitProvider
import com.dwirandyh.forecastmvvm.data.provider.UnitProviderImpl
import com.dwirandyh.forecastmvvm.data.repository.ForecastRepository
import com.dwirandyh.forecastmvvm.data.repository.ForecastRepositoryImpl
import com.dwirandyh.forecastmvvm.ui.weather.current.CurrentWeatherViewModelFactory
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ForecastApplication : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().weatherLocationDao() }

        // this is because connectivityinterceptor and there is class witch implement interface is interface and ConnectivityInterceptorImple is implementation of it
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }

        bind() from singleton { ApixWeahterAPiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(), instance(), instance(), instance()) }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        // provider because it doesn't need to be a singleton
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        // Set default value of preference from preference.xml
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}