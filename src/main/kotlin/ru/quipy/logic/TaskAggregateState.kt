package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.UUID
class TaskAggregateState : AggregateState<UUID, TaskAggregate>{
    private lateinit var taskId: UUID
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()
    lateinit var name: String
    lateinit var description: String
    lateinit var status: UUID
    lateinit var projectId: UUID

    var executors = mutableSetOf<UUID>()
    override fun getId() = taskId

    @StateTransitionFunc
    fun createTask(event: TaskCreatedEvent){
        taskId = event.taskId
        projectId = event.projectId
        name = event.taskName
        description = event.description
        status = event.statusId
    }

    @StateTransitionFunc
    fun changeTask(event: TaskNameChangedEvent){
        name = event.taskName
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun addUser(event: ExecutorsUpdatedEvent){
        executors.add(event.userId)
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun statusAssignedToTaskEvent(event: StatusAssignedToTaskEvent){
        status = event.statusId
    }
}