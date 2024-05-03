package com.tekskills.er_tekskills.utils

import com.tekskills.er_tekskills.data.model.CommentsListResponseItem

/**
 * Created by Ravi Kiran Palivela on 4/29/2024.
 * Description: $
 */
class DummyData {
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