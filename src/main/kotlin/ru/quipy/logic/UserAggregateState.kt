package ru.quipy.logic

import ru.quipy.api.UserAggregate
import ru.quipy.api.ChangeStatusUserEvent
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

class UserAggregateState : AggregateState<UUID, UserAggregate> {
    private lateinit var userId: UUID
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    lateinit var login: String
    lateinit var password: String
    lateinit var name: String
    lateinit var projects: MutableSet<UUID>
    override fun getId() = userId

    @StateTransitionFunc
    fun userCreatedApply(event: ChangeStatusUserEvent) {
        userId = event.userId
        login = event.login
        password = event.password
        name = event.userName
        projects = event.projects
        updatedAt = createdAt
    }

}