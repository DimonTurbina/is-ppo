package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.UUID


const val LIST_EXECUTORS_UPDATE = "LIST_EXECUTORS_UPDATE"
const val CHANGE_NAME_TASK_IN_PROJECT = "CHANGE_NAME_TASK_IN_PROJECT"
const val TASK_CREATED_EVENT = "TASK_CREATED_EVENT"
const val ASSIGNED_TAG_TO_TASK_EVENT = "ASSIGNED_TAG_TO_TASK_EVENT"

@DomainEvent(name = LIST_EXECUTORS_UPDATE)
class ListExecutorsUpdatedEvent(
    val userId: UUID,
    val taskId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
    name = LIST_EXECUTORS_UPDATE,
    createdAt = createdAt,
)

@DomainEvent(name = TASK_CREATED_EVENT)
class TaskCreatedEvent(
    val projectId: UUID,
    val taskId: UUID,
    val taskName: String,
    val tagId: UUID,
    val description: String,
    createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
    name = TASK_CREATED_EVENT,
    createdAt = createdAt
)

@DomainEvent(name = CHANGE_NAME_TASK_IN_PROJECT)
class TaskNameChangeEvent(
    val taskId: UUID,
    val taskName: String,
    createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
    name = CHANGE_NAME_TASK_IN_PROJECT,
    createdAt = createdAt,
)

@DomainEvent(name = ASSIGNED_TAG_TO_TASK_EVENT)
class AssignedTagToTaskEvent(
    val projectId: UUID,
    val taskId: UUID,
    val tagId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
    name = ASSIGNED_TAG_TO_TASK_EVENT,
    createdAt = createdAt
)