package fr.uha.hassenforder.team.repository

import fr.uha.hassenforder.team.database.ShiftDao
import kotlinx.coroutines.CoroutineDispatcher

class ShiftRepository(
    private val dispatcher: CoroutineDispatcher,
    private val shiftDao : ShiftDao
) {
}