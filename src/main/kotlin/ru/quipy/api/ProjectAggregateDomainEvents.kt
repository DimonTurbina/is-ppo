package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import ru.quipy.logic.StatusEntity.Companion.DEFAULT_STATUS_NAME
import java.util.*

const val PROJECT_CREATED_EVENT = "PROJECT_CREATED_EVENT"
const val STATUS_CREATED_EVENT = "STATUS_CREATED_EVENT"
const val STATUS_ASSIGNED_EVENT= "STATUS_ASSIGNED_EVENT"
const val USER_ADDED_TO_PROJECT_EVENT = "USER_ADDED_TO_PROJECT_EVENT"
const val STATUS_CHANGED_EVENT = "STATUS_CHANGED_EVENT"
const val DELETE_STATUS_EVENT = "DELETE_STATUS_EVENT"
// API
@DomainEvent(name = PROJECT_CREATED_EVENT)
class ProjectCreatedEvent(
    val projectId: UUID,
    val title: String,
    val creatorId: String,
    val defaultStatusId: UUID = UUID.randomUUID(),
    val defaultStatusName: String = DEFAULT_STATUS_NAME,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = PROJECT_CREATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = STATUS_CREATED_EVENT)
class StatusCreatedEvent(
    val projectId: UUID,
    val statusId: UUID,
    val statusName: String,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = STATUS_CREATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = STATUS_ASSIGNED_EVENT)
class StatusAssignedEvent(
    val projectId: UUID,
    val statusId: UUID,
    val oldStatusId: UUID?,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = STATUS_ASSIGNED_EVENT,
    createdAt = createdAt
)

@DomainEvent(name = USER_ADDED_TO_PROJECT_EVENT)
class UserAddedToProjectEvent(
    val projectId: UUID,
    val userId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = USER_ADDED_TO_PROJECT_EVENT,
    createdAt = createdAt
)

@DomainEvent(name = STATUS_CHANGED_EVENT)
class StatusChangedEvent(
    val projectId: UUID,
    val statusId: UUID,
    val statusName: String,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = STATUS_CHANGED_EVENT,
    createdAt = createdAt
)

@DomainEvent(name = DELETE_STATUS_EVENT)
class StatusDeletedEvent(
    val projectId: UUID,
    val statusId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = DELETE_STATUS_EVENT,
    createdAt = createdAt
)


