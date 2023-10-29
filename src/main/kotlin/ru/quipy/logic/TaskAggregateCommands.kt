package ru.quipy.logic

import ru.quipy.api.*
import java.util.UUID

fun TaskAggregateState.createTask(projectId: UUID,
                                  taskId: UUID,
                                  taskName: String,
                                  tagId: UUID,
                                  description: String): TaskCreatedEvent{
    return  TaskCreatedEvent(projectId = projectId, taskId = taskId, description = description, tagId = tagId, taskName = taskName)
}
fun TaskAggregateState.changeTask(taskId: UUID, taskName: String) : TaskNameChangeEvent {
    if (name == taskName) {
        throw IllegalArgumentException("Task with this name already exists: $taskName")
    }
    return TaskNameChangeEvent(taskId = taskId, taskName = taskName)
}

fun TaskAggregateState.addUser(userId: UUID, taskId: UUID) : ListExecutorsUpdatedEvent {
    if(executors.contains(userId)){
        throw IllegalArgumentException("User already exists: $userId")
    }
        return ListExecutorsUpdatedEvent(userId = userId, taskId = taskId)
}

fun TaskAggregateState.tagAssignedToTaskEvent(projectId: UUID, taskId: UUID, tagId: UUID): AssignedTagToTaskEvent {
    if(status == tagId){
        throw IllegalArgumentException("tag already exists: $tagId")
    }
    return AssignedTagToTaskEvent(projectId = projectId, taskId = taskId, tagId = tagId)
}
