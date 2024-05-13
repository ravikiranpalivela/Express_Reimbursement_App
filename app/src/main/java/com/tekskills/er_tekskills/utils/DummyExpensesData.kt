package com.tekskills.er_tekskills.utils

import com.tekskills.er_tekskills.data.model.UserExpence
import java.util.Random

/**
 * Created by Ravi Kiran Palivela on 5/3/2024.
 * Description: $
 */
class DummyExpensesData() {
//    fun createDummyData(): List<UserExpence> {
//        val dummyData = mutableListOf<UserExpence>()
//
//        // Adding some dummy user expenses
//        for (i in 1..20) {
//            dummyData.add(
//                UserExpence(
//                    createdAt = getRandomDate(),
//                    createdBy = getRandomInt(1, 10),
//                    expensesUser = getRandomExpensesUser(),
//                    foodAmount = "Rs."+getRandomInt(0, 5000),
//                    foodComments = getRandomFoodComments(),
//                    hotelAmount = "Rs."+getRandomInt(0, 10000),
//                    hotelFromDate = getRandomDate(),
//                    hotelToDate = getRandomDate(),
//                    id = i,
//                    invoiceFile = "test",
//                    location = getRandomLocation(),
//                    modeOfTravel = getRandomModeOfTravel(),
//                    noOfDays = getRandomInt(1, 5),
//                    purposeId = getRandomInt(1, 5),
//                    returnFrom = getRandomLocation(),
//                    returnModeOfTravel = getRandomModeOfTravel(),
//                    returnTo = getRandomLocation(),
//                    returnTravelDate = getRandomDate(),
//                    tenantId = 1,
//                    travelDate = getRandomDate(),
//                    travelFrom = getRandomLocation(),
//                    travelTo = getRandomLocation(),
//                    updatedAt = getRandomDate(),
//                    updatedBy = getRandomInt(1, 10)
//                )
//            )
//        }
//
//        return dummyData
//    }

    fun getRandomInt(min: Int, max: Int): Int {
        return Random().nextInt((max - min) + 1) + min
    }

    fun getRandomDate(): String {
        val year = getRandomInt(2020, 2024)
        val month = getRandomInt(1, 12)
        val day = getRandomInt(1, 28)
        return String.format("%04d-%02d-%02d", year, month, day)
    }

    fun getRandomExpensesUser(): String {
        val users = listOf("Travel", "Hotel", "foodexpence")
        return users.random()
    }

    fun getRandomFoodComments(): String {
        val comments = listOf("Delicious", "Tasty", "Yummy", "Healthy")
        return comments.random()
    }

    fun getRandomLocation(): String {
        val locations = listOf("Hyderabad", "Visakhapatnam", "Chennai", "Bangalore", "Mumbai")
        return locations.random()
    }

    fun getRandomModeOfTravel(): String {
        val modes = listOf("Train", "Flight", "Bus", "Car")
        return modes.random()
    }

    fun main() {
        val dummyData = createDummyData()
        dummyData.forEach { println(it) }
    }
}