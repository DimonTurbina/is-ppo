package ru.quipy.logic

import ru.quipy.api.*
import java.util.UUID

fun TaskAggregateState.createTask(projectId: UUID,
                                  taskId: UUID,
                                  taskName: String,
                                  statusId: UUID,
                                  description: String): TaskCreatedEvent{
    return  TaskCreatedEvent(projectId = projectId, taskId = taskId, description = description, statusId = statusId, taskName = taskName)
}
fun TaskAggregateState.changeTask(taskId: UUID, taskName: String) : TaskNameChangedEvent {
    if (name == taskName) {
        throw IllegalArgumentException("Task with this name already exists: $taskName")
    }
    return TaskNameChangedEvent(taskId = taskId, taskName = taskName)
}

fun TaskAggregateState.addUser(userId: UUID, taskId: UUID) : ExecutorsUpdatedEvent {
    if(executors.contains(userId)){
        throw IllegalArgumentException("User already exists: $userId")
    }
        return ExecutorsUpdatedEvent(userId = userId, taskId = taskId)
}

fun TaskAggregateState.statusAssignedToTaskEvent(projectId: UUID, taskId: UUID, statusId: UUID): StatusAssignedToTaskEvent {
    if(status == statusId){
        throw IllegalArgumentException("Status already exists: $statusId")
    }
    return StatusAssignedToTaskEvent(projectId = projectId, taskId = taskId, statusId = statusId)
}
