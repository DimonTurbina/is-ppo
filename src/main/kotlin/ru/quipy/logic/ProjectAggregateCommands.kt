package ru.quipy.logic

import ru.quipy.api.*
import java.util.*


// Commands : takes something -> returns event
// Here the commands are represented by extension functions, but also can be the class member functions

fun ProjectAggregateState.create(id: UUID, title: String, creatorId: String): ProjectCreatedEvent {
    return ProjectCreatedEvent(
        projectId = id,
        title = title,
        creatorId = creatorId,
    )
}

fun ProjectAggregateState.createStatus(name: String): StatusCreatedEvent {
    if (projectStatuses.values.any { it.name == name }) {
        throw IllegalArgumentException("Status already exists: $name")
    }
    return StatusCreatedEvent(projectId = this.getId(), statusId = UUID.randomUUID(), statusName = name)
}

fun ProjectAggregateState.assignStatusToTask(statusId: UUID, oldStatusId: UUID?): StatusAssignedEvent {
    if (!projectStatuses.containsKey(statusId)) {
        throw IllegalArgumentException("Status doesn't exists: $statusId")
    }

    return StatusAssignedEvent(projectId = this.getId(), statusId = statusId, oldStatusId = oldStatusId)
}

fun ProjectAggregateState.addUser(userId: UUID): UserAddedToProjectEvent {
    if(members.containsKey(userId)){
        throw IllegalArgumentException("User with id: $userId already exists in project")
    }
    return UserAddedToProjectEvent(projectId = this.getId(), userId = userId)
}

fun ProjectAggregateState.deleteStatus(statusId: UUID): StatusDeletedEvent{
    if(!projectStatuses.containsKey(statusId)){
        throw IllegalArgumentException("Status doesn't exists: $statusId")
    }
    val status = projectStatuses.get(statusId)
    if(status?.count!! > 0){
        throw IllegalArgumentException("Status have tasks:")
    }
    return StatusDeletedEvent(projectId = this.getId(), statusId = statusId)
}

fun ProjectAggregateState.changeStatus(statusId: UUID, statusName: String): StatusChangedEvent{
    if(!projectStatuses.containsKey(statusId)){
        throw IllegalArgumentException("Status doesn't exists: $statusId")
    }
    if(projectStatuses.values.any { it.name == statusName }){
        throw IllegalArgumentException("Status already exists: $statusName")
    }
    return StatusChangedEvent(projectId = this.getId(), statusId = statusId, statusName = statusName)
}