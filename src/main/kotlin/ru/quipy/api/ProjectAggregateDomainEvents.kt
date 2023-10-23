package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import ru.quipy.logic.TagEntity.Companion.DEFAULT_TAG
import java.util.*

const val PROJECT_CREATED_EVENT = "PROJECT_CREATED_EVENT"
const val TAG_CREATED_EVENT = "TAG_CREATED_EVENT"
const val TAG_ASSIGNED_TO_TASK_EVENT= "TAG_ASSIGNED_TO_TASK_EVENT"
const val TASK_ASSIGNED_TO_PROJ= "TASK_ASSIGNED_TO_PROJ"
const val ADD_USER_TO_PROJECT_EVENT = "ADD_USER_TO_PROJECT_EVENT"
const val CHANGE_TAG_EVENT = "CHANGE_TAG_EVENT"
const val DELETE_TAG_EVENT = "DELETE_TAG_EVENT"

// API
@DomainEvent(name = PROJECT_CREATED_EVENT)
class ProjectCreatedEvent(
    val projectId: UUID,
    val title: String,
    val creatorId: String,
    val defaultTagId: UUID = UUID.randomUUID(),
    val defaultTagName: String = DEFAULT_TAG,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = PROJECT_CREATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TAG_CREATED_EVENT)
class TagCreatedEvent(
    val projectId: UUID,
    val tagId: UUID,
    val tagName: String,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = TAG_CREATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TAG_ASSIGNED_TO_TASK_EVENT)
class TagAssignedToTaskEvent(
    val projectId: UUID,
    val taskId: UUID,
    val tagId: UUID,
    val oldTagId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = TAG_ASSIGNED_TO_TASK_EVENT,
    createdAt = createdAt
)

@DomainEvent(name = TASK_ASSIGNED_TO_PROJ)
class TaskAssignedToProjEvent(
        val projectId: UUID,
        val taskId: UUID,
        createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
        name = TASK_ASSIGNED_TO_PROJ,
        createdAt = createdAt
)

@DomainEvent(name = ADD_USER_TO_PROJECT_EVENT)
class AddUserToProjectEvent(
    val projectId: UUID,
    val userId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = ADD_USER_TO_PROJECT_EVENT,
    createdAt = createdAt
)

@DomainEvent(name = CHANGE_TAG_EVENT)
class ChangeTagEvent(
    val projectId: UUID,
    val tagId: UUID,
    val tagName: String,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = CHANGE_TAG_EVENT,
    createdAt = createdAt
)

@DomainEvent(name = DELETE_TAG_EVENT)
class DeleteTagEvent(
    val projectId: UUID,
    val tagId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = DELETE_TAG_EVENT,
    createdAt = createdAt
)


