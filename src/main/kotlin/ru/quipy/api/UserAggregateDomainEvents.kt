package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.UUID

const val USER_STATUS_CHANGED_EVENT = "USER_STATUS_CHANGED_EVENT"

@DomainEvent(name = USER_STATUS_CHANGED_EVENT)
class ChangeStatusUserEvent(
    val userId: UUID,
    val login: String,
    val password: String,
    createdAt: Long = System.currentTimeMillis(),
) : Event<UserAggregate> (
    name = USER_STATUS_CHANGED_EVENT,
    createdAt = createdAt
)