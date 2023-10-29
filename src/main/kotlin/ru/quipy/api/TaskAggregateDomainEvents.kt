package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.UUID


const val EXECUTORS_UPDATED_EVENT = "EXECUTORS_UPDATED_EVENT"
const val TASK_NAME_CHANGED_EVENT = "TASK_NAME_CHANGED_EVENT"
const val TASK_CREATED_EVENT = "TASK_CREATED_EVENT"
const val STATUS_ASSIGNED_TO_TASK_EVENT = "STATUS_ASSIGNED_TO_TASK_EVENT"

@DomainEvent(name = EXECUTORS_UPDATED_EVENT)
class ExecutorsUpdatedEvent(
    val userId: UUID,
    val taskId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
    name = EXECUTORS_UPDATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TASK_CREATED_EVENT)
class TaskCreatedEvent(
    val projectId: UUID,
    val taskId: UUID,
    val taskName: String,
    val statusId: UUID,
    val description: String,
    createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
    name = TASK_CREATED_EVENT,
    createdAt = createdAt
)

@DomainEvent(name = TASK_NAME_CHANGED_EVENT)
class TaskNameChangedEvent(
    val taskId: UUID,
    val taskName: String,
    createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
    name = TASK_NAME_CHANGED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = STATUS_ASSIGNED_TO_TASK_EVENT)
class StatusAssignedToTaskEvent(
    val projectId: UUID,
    val taskId: UUID,
    val statusId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
    name = STATUS_ASSIGNED_TO_TASK_EVENT,
    createdAt = createdAt
)