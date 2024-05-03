package com.tekskills.er_tekskills.data.remote

class APIEndPoint {
    companion object {
        const val GET_PENDING_ACTION_GRAPH = "api/dashboard/action/graph"
        const val GET_PENDING_ESCALATION_GRAPH = "api/dashboard/esc/graph"
        const val GET_PENDING_ACTION_GRAPH_BY_ID = "api/dashboard/action-project"
        const val GET_CLIENT_ESCALATION_GRAPH_BY_ID = "api/dashboard/client-esc"
        const val POST_ADD_OPPORTUNITY = "api/projects"

        const val POST_LOGIN = "api/public/authenticate"
        const val POST_ADD_CLIENT = "api/projects/add-client"
        const val POST_ADD_EMPLOYEE = "api/public/employees/me"
        const val POST_ASSIGN_PROJECT = "api/projects/assign-project"
        const val POST_CLIENT_ESCALATION = "api/action/clientescalationa"
        const val POST_ADD_PROJECT = "api/projects/add-project"
        const val POST_ADD_MOM = "/api/action/mom"
        const val POST_ADD_Action_Item = "/api/action"
        const val POST_ADD_COMMENT = "api/comment"

        const val GET_CLIENTS = "api/projects/findallclients"

        const val GET_PROJECTS = "api/projects/findallproject"
        const val GET_ASSIGN_PROJECTS = "api/projects/get-assign-project"
        const val GET_CLIENT_EXIST = "api/projects/find-clientname/"
        const val GET_ACTION_ITEM_PROJECT = "api/action/project/"
        const val GET_SRMANAGEMENT = "api/public/employees/getmanagement"
        const val GET_ACCOUNT_MANAGER = "api/public/employees/getaccountmanager"
        const val GET_PROJECT_MANAGER = "api/public/employees/getprojectmanager"
        const val GET_PRACTICE_MANAGER = "api/public/employees/getpracticehead"
        const val GET_ACTION_ITEM_PROJECT_ID = "api/action/project"
        const val GET_ACTION_ITEM_BY_ID ="api/action/pending-action/"
        const val GET_PROJECT_ID = "api/projects/get-project"
        const val GET_MOM_PROJECT = "api/action/mombyproject"
        const val GET_MOM_ACTION_ITEM_BY_ID = "/pi/action/mom"
        const val GET_CLIENT_ESCALATION = "api/action/clientescalationa"
        const val GET_CLIENT_ESCALATION_BY_ID = "api/action/clientescalationa"
        const val GET_COMMENT_BY_ID = "api/comment"
        const val GET_OPPORTUNITY_BY_PROJECT_ID = "api/projects/get-projectbyId"



        const val POST_USER_LOGIN = "api/public/authenticate"
        const val GET_ME = "api/me"
        const val POST_ADD_MEETING_PURPOSE = "api/purpose"
        const val GET_MEETING_PURPOSE = "api/purpose"


    }
}