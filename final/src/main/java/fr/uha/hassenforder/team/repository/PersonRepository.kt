package fr.uha.hassenforder.team.repository

import androidx.annotation.WorkerThread
import fr.uha.hassenforder.team.database.PersonDao
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.model.PersonWithDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PersonRepository(
    private val dispatcher: CoroutineDispatcher,
    private val personDao: PersonDao,
) {

    fun getAll(): Flow<List<Person>> {
        return personDao.getAll()
    }

    fun getAllWithDetails(): Flow<List<PersonWithDetails>> {
        return personDao.getAllWithDetails()
    }

    fun getPersonById(id: Long): Flow<Person?> {
        return personDao.getPersonById(id)
    }

    @WorkerThread
    suspend fun create(person: Person): Long = withContext(dispatcher)  {
        return@withContext personDao.create(person)
    }

    @WorkerThread
    @Suppress("unused")
    suspend fun update(oldPerson: Person, newPerson: Person) : Long = withContext(dispatcher)  {
        return@withContext personDao.update(newPerson)
    }

    @WorkerThread
    suspend fun delete(person: Person) = withContext(dispatcher) {
        personDao.delete(person)
    }

}
