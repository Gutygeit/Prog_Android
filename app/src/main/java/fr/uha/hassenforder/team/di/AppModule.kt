package fr.uha.hassenforder.team.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.uha.hassenforder.team.database.DriverDao
import fr.uha.hassenforder.team.database.ShiftDao
import fr.uha.hassenforder.team.database.ShiftDatabase
import fr.uha.hassenforder.team.repository.DriverRepository
import fr.uha.hassenforder.team.repository.ShiftRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = ShiftDatabase.create(appContext)

    @Singleton
    @Provides
    fun providePersonDao(db: ShiftDatabase) = db.driverDAO()

    @Singleton
    @Provides
    fun providePersonRepository(
        ioDispatcher: CoroutineDispatcher,
        driverDao: DriverDao
    ) = DriverRepository(ioDispatcher, driverDao)

    @Singleton
    @Provides
    fun provideTeamDao(db: ShiftDatabase) = db.shiftDAO()

    @Singleton
    @Provides
    fun provideTeamRepository(
        ioDispatcher: CoroutineDispatcher,
        shiftDao: ShiftDao
    ) = ShiftRepository(ioDispatcher, shiftDao)

}