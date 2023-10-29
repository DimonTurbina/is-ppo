package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.time.Duration
import java.util.UUID
class TaskAggregateState : AggregateState<UUID, TaskAggregate>{
    private lateinit var taskId: UUID
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()
    lateinit var name: String
    lateinit var description: String
    lateinit var status: UUID
    lateinit var duration: Duration


    var executors = mutableSetOf<UUID>()
    override fun getId() = taskId

    @StateTransitionFunc
    fun createTask(event: TaskCreatedEvent){
        taskId = event.taskId
        name = event.taskName
        description = event.description ?: ""
        status = event.statusId
    }

    @StateTransitionFunc
    fun changeTask(event: TaskNameChangeEvent){
        name = event.taskName
        updatedAt = event.createdAt
        duration = event.duration
        description = event.description
        status = event.status
    }

    @StateTransitionFunc
    fun addUser(event: ListExecutorsUpdatedEvent){
        executors.add(event.userId)
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun removeUser(event: ListExecutorsUpdatedEvent){
        executors.remove(event.userId)
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun tagAssignedToTaskEvent(event: AssignedTagToTaskEvent){
        status = event.statusId
    }
}