package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.lang.IllegalArgumentException
import java.util.*

// Service's business logic
class ProjectAggregateState : AggregateState<UUID, ProjectAggregate> {
    private lateinit var projectId: UUID
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    lateinit var projectTitle: String
    lateinit var creatorId: String
    val projectStatuses: MutableMap<UUID, StatusEntity> = mutableMapOf()
    var members = mutableMapOf<UUID, UserEntity>()

    override fun getId() = projectId

    // State transition functions which is represented by the class member function
    @StateTransitionFunc
    fun projectCreatedApply(event: ProjectCreatedEvent) {
        projectId = event.projectId
        projectTitle = event.title
        creatorId = event.creatorId
        val taskStatus = StatusEntity(event.defaultStatusId, event.defaultStatusName, 0)
        projectStatuses[taskStatus.id] = taskStatus
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun statusCreatedApply(event: StatusCreatedEvent) {
        projectStatuses[event.statusId] = StatusEntity(event.statusId, event.statusName, 0)
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun addUser(event: UserAddedToProjectEvent){
            members[event.userId] = UserEntity(event.userId)
            updatedAt = createdAt
    }
    @StateTransitionFunc
    fun changeStatus(event: StatusChangedEvent){
        val count = projectStatuses[event.statusId]?.count
        projectStatuses[event.statusId] = StatusEntity(event.statusId, event.statusName, count)
        updatedAt = createdAt
    }
    @StateTransitionFunc
    fun deleteStatus(event: StatusDeletedEvent){
        projectStatuses.remove(event.statusId)
        updatedAt = createdAt
    }
}

data class TaskEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val statusAssigned: MutableSet<UUID>?
)

data class StatusEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    var count: Int?
){
    companion object {
        const val DEFAULT_STATUS_NAME = "CREATED"
    }
}

data class UserEntity(
    val id: UUID
)
/**
 * Demonstrates that the transition functions might be representer by "extension" functions, not only class members functions
 */
@StateTransitionFunc
fun ProjectAggregateState.statusAssignedApply(event: StatusAssignedEvent) {
    val key = projectStatuses.get(event.statusId)
        ?: throw IllegalArgumentException("No such status: ${event.statusId}")
    key.count = key.count?.plus(1)
    if(event.oldStatusId == null){
        return
    }
    val key2 = projectStatuses.get(event.oldStatusId)
        ?: throw IllegalArgumentException("No such status: ${event.oldStatusId}")
        key2.count = key2.count?.minus(1)
}
