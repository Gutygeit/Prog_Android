package fr.uha.hassenforder.team.database

import fr.uha.hassenforder.team.model.Driver
import fr.uha.hassenforder.team.model.License
import fr.uha.hassenforder.team.model.Shift
import fr.uha.hassenforder.team.model.ShiftDriverAssociation
import java.util.Random

class FeedDatabase (
    private val db : ShiftDatabase
) {

    private suspend fun feedDrivers(): LongArray {
        val dao: DriverDao = db.driverDAO()
        val ids = LongArray(4)
        ids[0] = dao.create(getRandomDriver(License.A))
        ids[1] = dao.create(getRandomDriver(License.B))
        ids[2] = dao.create(getRandomDriver(License.A))
        ids[3] = dao.create(getRandomDriver(License.C))
        return ids
    }

    private suspend fun feedShifts(dids: LongArray) {
        val dao: ShiftDao = db.shiftDAO()
        val shift = getRandomShift(dids[0])
        val sid = dao.upsert(shift)
        dao.addMember(ShiftDriverAssociation(sid, dids[0]))
        dao.addMember(ShiftDriverAssociation(sid, dids[3]))
    }

    @Suppress("unused")
    suspend fun populate(mode : Int) {
        val dids = feedDrivers()
        feedShifts(dids)
    }

    fun clear() {
        db.clearAllTables()
    }

    companion object {
        private val rnd: Random = Random()
        private val firstNames: Array<String> = arrayOf(
            "Pierre",
            "Paul",
            "Jacques",
            "Jean",
            "Marie",
            "Louise",
            "Julie",
        )
        private val lastNames: Array<String> = arrayOf(
            "Dupont",
            "Durand",
            "Duchemin",
            "Dumas",
            "Dufour",
            "Dufourneau",
            "Dufourneau",
        )
        private val shiftLocations: Array<String> = arrayOf(
            "Mulhouse",
            "Colmar",
            "Strasbourg",
            "Paris",
            "Lyon",
            "Marseille",
            "Nice",
        )
        private val shiftDates: Array<String> = arrayOf(
            "2021-01-01",
            "2021-02-01",
            "2021-03-01",
            "2021-04-01",
            "2021-05-01",
            "2021-06-01",
            "2021-07-01",
        )

        private fun getRandomFirstName() : String {
            return firstNames[rnd.nextInt(firstNames.size)]
        }

        private fun getRandomLastName() : String {
            return lastNames[rnd.nextInt(lastNames.size)]
        }

        private fun getRandomPhone() : String {
            val tmp = StringBuilder()
            if (rnd.nextInt(1000) > 750) {
                tmp.append("36")
                tmp.append(rnd.nextInt(10))
                tmp.append(rnd.nextInt(10))
            } else {
                tmp.append("0")
                for (i in 0..8) {
                    tmp.append(rnd.nextInt(10))
                }
            }
            return tmp.toString()
        }

        private fun getRandomBetween(low: Int, high: Int): Int {
            return rnd.nextInt(high - low) + low
        }

        private fun getRandomDriver(license: License): Driver {
            return Driver(
                0,
                getRandomFirstName(),
                getRandomLastName(),
                getRandomPhone(),
                license,
            )
        }

        private fun getRandomShiftLocation(): String {
            return shiftLocations[rnd.nextInt(shiftLocations.size)]
        }

        private fun getRandomShiftDate(): String {
            return shiftDates[rnd.nextInt(shiftDates.size)]
        }

        private fun getRandomShift(driver: Long): Shift {
            return Shift(
                0,
                getRandomShiftLocation(),
                getRandomShiftDate(),
                getRandomBetween(1, 10),
                driver,
            )
        }
    }
}