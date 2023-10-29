package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import ru.quipy.logic.TagEntity.Companion.DEFAULT_TAG
import java.lang.IllegalArgumentException
import java.util.*

// Service's business logic
class ProjectAggregateState : AggregateState<UUID, ProjectAggregate> {
    private lateinit var projectId: UUID
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    lateinit var projectTitle: String
    lateinit var creatorId: String
    val projectTags: MutableMap<UUID, TagEntity> = mutableMapOf()
    var participants = mutableMapOf<UUID, UserEntity>()

    override fun getId() = projectId

    // State transition functions which is represented by the class member function
    @StateTransitionFunc
    fun projectCreatedApply(event: ProjectCreatedEvent) {
        projectId = event.projectId
        projectTitle = event.title
        creatorId = event.creatorId
        val taskTag = TagEntity(event.defaultTagId, event.defaultTagName, 0)
        projectTags[taskTag.id] = taskTag
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun tagCreatedApply(event: TagCreatedEvent) {
        projectTags[event.tagId] = TagEntity(event.tagId, event.tagName, 0)
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun addUser(event: AddUserToProjectEvent){
            participants[event.userId] = UserEntity(event.userId)
            updatedAt = createdAt
    }
    @StateTransitionFunc
    fun changeTag(event: ChangeTagEvent){
        val count = projectTags[event.tagId]?.count
        projectTags[event.tagId] = TagEntity(event.tagId, event.tagName, count)
        updatedAt = createdAt
    }
    @StateTransitionFunc
    fun deleteTag(event: DeleteTagEvent){
        projectTags.remove(event.tagId)
        updatedAt = createdAt
    }
}

data class TaskEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val tagsAssigned: MutableSet<UUID>?
)

data class TagEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
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
    val key = projectTags.get(event.tagId)
        ?: throw IllegalArgumentException("No such tag: ${event.tagId}")
    key.count = key.count?.plus(1)
    if(event.oldTagId == null){
        return
    }
    val key2 = projectTags.get(event.oldTagId)
        ?: throw IllegalArgumentException("No such tag: ${event.oldTagId}")
        key2.count = key2.count?.minus(1)
}
