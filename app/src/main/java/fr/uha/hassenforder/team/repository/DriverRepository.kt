package fr.uha.hassenforder.team.repository

import fr.uha.hassenforder.team.database.DriverDao
import kotlinx.coroutines.CoroutineDispatcher

class DriverRepository(
    private val dispatcher: CoroutineDispatcher,
    private val driverDao : DriverDao
) {
}