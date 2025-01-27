package fr.uha.hassenforder.team.database

import fr.uha.hassenforder.team.model.Driver
import fr.uha.hassenforder.team.model.License
import fr.uha.hassenforder.team.model.Shift
import fr.uha.hassenforder.team.model.ShiftDriverAssociation
import fr.uha.hassenforder.team.model.ShiftVehiculeAssociation
import fr.uha.hassenforder.team.model.Vehicle
import fr.uha.hassenforder.team.model.VehicleStatus
import java.util.Random

class FeedDatabase (
    private val db : ShiftDatabase
) {

    private suspend fun feedVehicles() : LongArray {
        val dao: VehicleDao = db.vehicleDAO()
        val ids = LongArray(4)
        ids[0] = dao.create(getRandomVehicle(VehicleStatus.IN_USE))
        ids[1] = dao.create(getRandomVehicle(VehicleStatus.AVAILABLE))
        ids[2] = dao.create(getRandomVehicle(VehicleStatus.MAINTENANCE))
        ids[3] = dao.create(getRandomVehicle(VehicleStatus.UNAVAILABLE))
        return ids
    }

    private suspend fun feedDrivers(): LongArray {
        val dao: DriverDao = db.driverDAO()
        val ids = LongArray(4)
        ids[0] = dao.create(getRandomDriver(License.A))
        ids[1] = dao.create(getRandomDriver(License.B))
        ids[2] = dao.create(getRandomDriver(License.A))
        ids[3] = dao.create(getRandomDriver(License.C))
        return ids
    }

    private suspend fun feedShifts(vids : LongArray, dids: LongArray) {
        val dao: ShiftDao = db.shiftDAO()
        val shift = getRandomShift(vids[0], dids[0])
        val sid = dao.upsert(shift)
        dao.addVehicle(ShiftVehiculeAssociation(sid, vids[0]))
        dao.addMember(ShiftDriverAssociation(sid, dids[0]))
        dao.addMember(ShiftDriverAssociation(sid, dids[3]))
    }

    @Suppress("unused")
    suspend fun populate(/*mode : Int*/) { //Taking off the mode parameter as it is not used and remove the '0' from populate() in SettingsViewModel
        val vids = feedVehicles()
        val dids = feedDrivers()
        feedShifts(vids, dids)
    }

    fun clear() {
        db.clearAllTables()
    }

    companion object {
        private val rnd: Random = Random()

        private val brandsAndModel: Array<String> = arrayOf(
            "Renault - Clio",
            "Peugeot - 208",
            "Citroën - C3",
            "Fiat - 500",
            "Ford - Fiesta",
            "Toyota - Supra",
            "Volkswagen - Golf",
        )
        private val matriculation: Array<String> = arrayOf(
            "AA-123-AA",
            "BB-456-BB",
            "CC-789-CC",
            "DD-012-DD",
            "EE-345-EE",
            "FF-678-FF",
            "GG-901-GG",
        )

        private val kilometers: Array<Int> = arrayOf(
            1000,
            2000,
            3000,
            4000,
            5000,
            6000,
            7000,
        )

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

        private fun getRandomBrandAndModel() : String {
            return brandsAndModel[rnd.nextInt(brandsAndModel.size)]
        }

        private fun getRandomMatriculation() : String {
            return matriculation[rnd.nextInt(matriculation.size)]
        }

        private fun getRandomKilometers() : Int {
            return kilometers[rnd.nextInt(kilometers.size)]
        }

        private fun getRandomVehicle(status: VehicleStatus): Vehicle {
            return Vehicle(
                0,
                getRandomBrandAndModel(),
                getRandomMatriculation(),
                getRandomKilometers(),
                status,
            )
        }

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

        private fun getRandomShift(vehicle: Long,driver: Long): Shift {
            return Shift(
                0,
                getRandomShiftLocation(),
                getRandomShiftDate(),
                getRandomShiftDate(),
                vehicle,
                driver,
            )
        }
    }
}