package com.tekskills.er_tekskills.utils

import com.tekskills.er_tekskills.data.model.CommentsListResponseItem
import com.tekskills.er_tekskills.data.model.MeetingPurposeResponse
import com.tekskills.er_tekskills.data.model.MeetingPurposeResponseData
import com.tekskills.er_tekskills.data.model.Pageable
import com.tekskills.er_tekskills.data.model.SortX
import com.tekskills.er_tekskills.data.model.UserCoordinates
import com.tekskills.er_tekskills.data.model.UserCordinates
import com.tekskills.er_tekskills.data.model.UserExpence

/**
 * Created by Ravi Kiran Palivela on 4/29/2024.
 * Description: $
 */
class DummyData {

//    fun createDummyData(): SuccessResource<MeetingPurposeResponse> {
//        val userExpences = listOf(
//            UserExpence(
//                createdAt = "2024-04-27",
//                createdBy = 2,
//                expensesUser = "Travel",
//                foodAmount = "0",
//                foodComments = "",
//                hotelAmount = "0",
//                hotelFromDate = "",
//                hotelToDate = "",
//                id = 5,
//                invoiceFile = "testt",
//                location = "",
//                modeOfTravel = "Train",
//                noOfDays = 0,
//                purposeId = 1,
//                returnFrom = "",
//                returnModeOfTravel = "",
//                returnTo = "",
//                returnTravelDate = "",
//                tenantId = 1,
//                travelDate = "2024-04-26",
//                travelFrom = "Hyderabad",
//                travelTo = "Visakhapatnam",
//                updatedAt = "2024-04-27",
//                updatedBy = 2
//            ),
//            UserExpence(
//                createdAt = "2024-04-27",
//                createdBy = 2,
//                expensesUser = "Travel",
//                foodAmount = "0",
//                foodComments = "",
//                hotelAmount = "0",
//                hotelFromDate = "",
//                hotelToDate = "",
//                id = 6,
//                invoiceFile = "testt",
//                location = "",
//                modeOfTravel = "",
//                noOfDays = 0,
//                purposeId = 1,
//                returnFrom = "Visakhapatnam",
//                returnModeOfTravel = "Train",
//                returnTo = "Hyderabad",
//                returnTravelDate = "2024-04-29",
//                tenantId = 1,
//                travelDate = "",
//                travelFrom = "",
//                travelTo = "",
//                updatedAt = "2024-04-27",
//                updatedBy = 2
//            ),
//            UserExpence(
//                createdAt = "2024-04-27",
//                createdBy = 2,
//                expensesUser = "Hotel",
//                foodAmount = "0",
//                foodComments = "",
//                hotelAmount = "RS.6000",
//                hotelFromDate = "2024-04-27",
//                hotelToDate = "2024-04-29",
//                id = 7,
//                invoiceFile = "",
//                location = "Visakhapatnam",
//                modeOfTravel = "",
//                noOfDays = 3,
//                purposeId = 1,
//                returnFrom = "",
//                returnModeOfTravel = "",
//                returnTo = "",
//                returnTravelDate = "",
//                tenantId = 1,
//                travelDate = "",
//                travelFrom = "",
//                travelTo = "",
//                updatedAt = "2024-04-27",
//                updatedBy = 2
//            ),
//            UserExpence(
//                createdAt = "2024-04-27",
//                createdBy = 2,
//                expensesUser = "foodexpence",
//                foodAmount = "Rs.2000",
//                foodComments = "teastes good",
//                hotelAmount = "0",
//                hotelFromDate = "",
//                hotelToDate = "",
//                id = 8,
//                invoiceFile = "",
//                location = "",
//                modeOfTravel = "",
//                noOfDays = 0,
//                purposeId = 1,
//                returnFrom = "",
//                returnModeOfTravel = "",
//                returnTo = "",
//                returnTravelDate = "",
//                tenantId = 1,
//                travelDate = "",
//                travelFrom = "",
//                travelTo = "",
//                updatedAt = "2024-04-27",
//                updatedBy = 2
//            )
//        )
//
//        val meetingPurposeResponseData =  MeetingPurposeResponse(
//            content = arrayListOf(
//                MeetingPurposeResponseData(
//                    advanceAmountRupees = 1000,
//                    createdAt = "2024-04-27",
//                    createdBy = 2,
//                    employeeId = 3,
//                    expenceType = "",
//                    financeComments = "",
//                    financeId = 5,
//                    financeStatus = "Approved",
//                    id = 1,
//                    managerComments = "approved status",
//                    managerId = 4,
//                    managerStatus = "Approved",
//                    noOfDays = "3",
//                    status = "Active",
//                    tenantId = 1,
//                    travelDeskComments = "",
//                    travelDeskId = 0,
//                    travelDeskStatus = "",
//                    updatedAt = "2024-04-29",
//                    updatedBy = 5,
//                    userCordinates = UserCoordinates(
//                        destinationLatitude = "37.7749",
//                        destinationLongitude = "-122.4194",
////                        id = 1,
//                        source = "Hyderabad",
//                        destination = "Nellore",
//                        sourceLatitude = "37.7749",
//                        sourceLongitude = "-122.4194",
////                        tenantId = 1,
//                        totalDistance = 100.0,
////                        updatedAt = "2024-04-27T17:12:14.75",
////                        updatedBy = 2,
////                        purposeId = 1
//                    ),
//                    userExpences = userExpences,
//                    visitDate = "2024-04-27",
//                    visitPurpose = "Meeting with clients",
//                    customerPhone = "9876543210",
//                    custmerEmail = "customer@gmail.com",
//                    customerName = "Customer 1",
//                    description = "Meeting with clients"
//                )
//            ),
//            empty = false,
//            first = true,
//            last = true,
//            number = 0,
//            numberOfElements = 1,
//            pageable = Pageable(
//                offset = 0,
//                pageNumber = 0,
//                pageSize = 0,
//                paged = false,
//                sort = SortX(
//                    empty = true,
//                    sorted = false,
//                    unsorted = true
//                ),
//                unpaged = true
//            ),
//            size = 0,
//            sort = SortX(
//                empty = true,
//                sorted = false,
//                unsorted = true
//            ),
//            totalElements = 1,
//            totalPages = 0
//        )
//
//        return SuccessResource.success(meetingPurposeResponseData)
//    }


//    fun createDummyDataBYID(): SuccessResource<MeetingPurposeResponseData> {
//        val userExpences = listOf(
//            UserExpence(
//                createdAt = "2024-04-27",
//                createdBy = 2,
//                expensesUser = "Travel",
//                foodAmount = "0",
//                foodComments = "",
//                hotelAmount = "0",
//                hotelFromDate = "",
//                hotelToDate = "",
//                id = 5,
//                invoiceFile = "testt",
//                location = "",
//                modeOfTravel = "Train",
//                noOfDays = 0,
//                purposeId = 1,
//                returnFrom = "",
//                returnModeOfTravel = "",
//                returnTo = "",
//                returnTravelDate = "",
//                tenantId = 1,
//                travelDate = "2024-04-26",
//                travelFrom = "Hyderabad",
//                travelTo = "Visakhapatnam",
//                updatedAt = "2024-04-27",
//                updatedBy = 2
//            ),
//            UserExpence(
//                createdAt = "2024-04-27",
//                createdBy = 2,
//                expensesUser = "Travel",
//                foodAmount = "0",
//                foodComments = "",
//                hotelAmount = "0",
//                hotelFromDate = "",
//                hotelToDate = "",
//                id = 6,
//                invoiceFile = "testt",
//                location = "",
//                modeOfTravel = "",
//                noOfDays = 0,
//                purposeId = 1,
//                returnFrom = "Visakhapatnam",
//                returnModeOfTravel = "Train",
//                returnTo = "Hyderabad",
//                returnTravelDate = "2024-04-29",
//                tenantId = 1,
//                travelDate = "",
//                travelFrom = "",
//                travelTo = "",
//                updatedAt = "2024-04-27",
//                updatedBy = 2
//            ),
//            UserExpence(
//                createdAt = "2024-04-27",
//                createdBy = 2,
//                expensesUser = "Hotel",
//                foodAmount = "0",
//                foodComments = "",
//                hotelAmount = "RS.6000",
//                hotelFromDate = "2024-04-27",
//                hotelToDate = "2024-04-29",
//                id = 7,
//                invoiceFile = "",
//                location = "Visakhapatnam",
//                modeOfTravel = "",
//                noOfDays = 3,
//                purposeId = 1,
//                returnFrom = "",
//                returnModeOfTravel = "",
//                returnTo = "",
//                returnTravelDate = "",
//                tenantId = 1,
//                travelDate = "",
//                travelFrom = "",
//                travelTo = "",
//                updatedAt = "2024-04-27",
//                updatedBy = 2
//            ),
//            UserExpence(
//                createdAt = "2024-04-27",
//                createdBy = 2,
//                expensesUser = "foodexpence",
//                foodAmount = "Rs.2000",
//                foodComments = "teastes good",
//                hotelAmount = "0",
//                hotelFromDate = "",
//                hotelToDate = "",
//                id = 8,
//                invoiceFile = "",
//                location = "",
//                modeOfTravel = "",
//                noOfDays = 0,
//                purposeId = 1,
//                returnFrom = "",
//                returnModeOfTravel = "",
//                returnTo = "",
//                returnTravelDate = "",
//                tenantId = 1,
//                travelDate = "",
//                travelFrom = "",
//                travelTo = "",
//                updatedAt = "2024-04-27",
//                updatedBy = 2
//            )
//        )
//
//        val meetingPurposeResponseData =
////            MeetingPurposeResponse(
////            content = arrayListOf(
//                MeetingPurposeResponseData(
//                    advanceAmountRupees = 1000,
//                    createdAt = "2024-04-27",
//                    createdBy = 2,
//                    employeeId = 3,
//                    expenceType = "",
//                    financeComments = "",
//                    financeId = 5,
//                    financeStatus = "Approved",
//                    id = 1,
//                    managerComments = "approved status",
//                    managerId = 4,
//                    managerStatus = "Approved",
//                    noOfDays = "3",
//                    status = "Active",
//                    tenantId = 1,
//                    travelDeskComments = "",
//                    travelDeskId = 0,
//                    travelDeskStatus = "",
//                    updatedAt = "2024-04-29",
//                    updatedBy = 5,
//                    userCordinates = UserCoordinates(
//                        destinationLatitude = "37.7749",
//                        destinationLongitude = "-122.4194",
////                        id = 1,
//                        source = "Hyderabad",
//                        destination = "Nellore",
//                        sourceLatitude = "37.7749",
//                        sourceLongitude = "-122.4194",
////                        tenantId = 1,
//                        totalDistance = 100.0,
////                        updatedAt = "2024-04-27T17:12:14.75",
////                        updatedBy = 2,
////                        purposeId = 1
//                    ),
//                    userExpences = userExpences,
//                    visitDate = "2024-04-27",
//                    visitPurpose = "Meeting with clients",
//                    customerPhone = "9876543210",
//                    custmerEmail = "customer@gmail.com",
//                    customerName = "Customer 1",
//                    description = "Meeting with clients"
//                )
////            )
////        ,
////            empty = false,
////            first = true,
////            last = true,
////            number = 0,
////            numberOfElements = 1,
////            pageable = Pageable(
////                offset = 0,
////                pageNumber = 0,
////                pageSize = 0,
////                paged = false,
////                sort = SortX(
////                    empty = true,
////                    sorted = false,
////                    unsorted = true
////                ),
////                unpaged = true
////            ),
////            size = 0,
////            sort = SortX(
////                empty = true,
////                sorted = false,
////                unsorted = true
////            ),
////            totalElements = 1,
////            totalPages = 0
////        )
//
//        return SuccessResource.success(meetingPurposeResponseData)
//    }

}

fun createDummyData(): List<CommentsListResponseItem> {
    val dummyData = mutableListOf<CommentsListResponseItem>()

    // Adding some dummy comments
    dummyData.add(
        CommentsListResponseItem(
            comment = "1000",
            commentDate = "2024-04-29",
            empRoleName = "Manager",
            employeeId = 1,
            employeeName = "John Doe",
            projectId = 123
        )
    )

    dummyData.add(
        CommentsListResponseItem(
            comment = "2000",
            commentDate = "2024-04-28",
            empRoleName = "Employee",
            employeeId = 2,
            employeeName = "Jane Smith",
            projectId = 123
        )
    )

    // Add more dummy data as needed...

    return dummyData
}