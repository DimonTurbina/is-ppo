package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.UUID

const val CHANGE_STATUS_USER = "CHANGE_STATUS_USER"

@DomainEvent(name = CHANGE_STATUS_USER)
class ChangeStatusUserEvent(
    val userId: UUID,
    val login: String,
    val password: String,
    createdAt: Long = System.currentTimeMillis(),
) : Event<UserAggregate> (
    name = CHANGE_STATUS_USER,
    createdAt = createdAt
)