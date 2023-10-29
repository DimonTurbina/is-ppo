package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import ru.quipy.logic.StatusEntity.Companion.DEFAULT_TAG
import java.lang.IllegalArgumentException
import java.util.*

// Service's business logic
class ProjectAggregateState : AggregateState<UUID, ProjectAggregate> {
    private lateinit var projectId: UUID
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    lateinit var projectTitle: String
    lateinit var creatorId: String
    val taskStatuses: MutableMap<UUID, StatusEntity> = mutableMapOf()
    var members = mutableMapOf<UUID, UserEntity>()
    lateinit var tasks: MutableSet<UUID>
    override fun getId() = projectId

    // State transition functions which is represented by the class member function
    @StateTransitionFunc
    fun projectCreatedApply(event: ProjectCreatedEvent) {
        projectId = event.projectId
        projectTitle = event.title
        creatorId = event.creatorId
        val taskStatus = StatusEntity(event.defaultTagId, event.defaultTagName, "#0000FF", 0)
        taskStatuses[taskStatus.id] = taskStatus
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun tagCreatedApply(event: TagCreatedEvent) {
        taskStatuses[event.tagId] = StatusEntity(event.tagId, event.tagName, "#0000FF",0)
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun addUser(event: AddUserToProjectEvent){
            members[event.userId] = UserEntity(event.userId)
            updatedAt = createdAt
    }


    @StateTransitionFunc
    fun changeTag(event: ChangeTagEvent){
        val count = taskStatuses[event.tagId]?.count
        taskStatuses[event.tagId] = StatusEntity(event.tagId, event.tagName, "#0000FF", count)
        updatedAt = createdAt
    }
    @StateTransitionFunc
    fun deleteTag(event: DeleteTagEvent){
        taskStatuses.remove(event.tagId)
        updatedAt = createdAt
    }
}

data class TaskEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val tagsAssigned: MutableSet<UUID>?
)

data class StatusEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val color: String,
    var count: Int?
){
    companion object {
        const val DEFAULT_TAG = "CREATED"
    }
}

data class UserEntity(
    val id: UUID
)
/**
 * Demonstrates that the transition functions might be representer by "extension" functions, not only class members functions
 */
@StateTransitionFunc
fun ProjectAggregateState.tagAssignedApply(event: TagAssignedToTaskEvent) {
    val key = taskStatuses.get(event.tagId)
        ?: throw IllegalArgumentException("No such tag: ${event.tagId}")
    key.count = key.count?.plus(1)
    val key2 = taskStatuses.get(event.oldTagId)
        ?: throw IllegalArgumentException("No such tag: ${event.oldTagId}")
    if(key2.name != DEFAULT_TAG){
        key2.count = key2.count?.minus(1)
    }
}
