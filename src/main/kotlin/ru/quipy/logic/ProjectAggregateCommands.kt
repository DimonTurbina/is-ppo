package ru.quipy.logic

import ru.quipy.api.*
import java.util.*


// Commands : takes something -> returns event
// Here the commands are represented by extension functions, but also can be the class member functions

fun ProjectAggregateState.create(id: UUID, title: String, creatorId: String): ProjectCreatedEvent {
    return ProjectCreatedEvent(
        projectId = id,
        title = title,
        creatorId = creatorId,
    )
}

fun ProjectAggregateState.createTag(name: String): TagCreatedEvent {
    if (projectTags.values.any { it.name == name }) {
        throw IllegalArgumentException("Tag already exists: $name")
    }
    return TagCreatedEvent(projectId = this.getId(), tagId = UUID.randomUUID(), tagName = name)
}

fun ProjectAggregateState.assignTagToTask(tagId: UUID, oldTagId: UUID?): TagAssignedToTaskEvent {
    if (!projectTags.containsKey(tagId)) {
        throw IllegalArgumentException("Tag doesn't exists: $tagId")
    }

    return TagAssignedToTaskEvent(projectId = this.getId(), tagId = tagId, oldTagId = oldTagId)
}

fun ProjectAggregateState.addUser(userId: UUID): AddUserToProjectEvent {
    if(participants.containsKey(userId)){
        throw IllegalArgumentException("User with id: $userId already exists in project")
    }
    return AddUserToProjectEvent(projectId = this.getId(), userId = userId)
}

fun ProjectAggregateState.deleteTag(tagId: UUID): DeleteTagEvent{
    if(!projectTags.containsKey(tagId)){
        throw IllegalArgumentException("Tag doesn't exists: $tagId")
    }
    val tag = projectTags.get(tagId)
    if(tag?.count!! > 0){
        throw IllegalArgumentException("Tag have tasks:")
    }
    return DeleteTagEvent(projectId = this.getId(), tagId = tagId)
}

fun ProjectAggregateState.changeTag(tagId: UUID, tagName: String): ChangeTagEvent{
    if(!projectTags.containsKey(tagId)){
        throw IllegalArgumentException("Tag doesn't exists: $tagId")
    }
    if(projectTags.values.any { it.name == tagName }){
        throw IllegalArgumentException("Tag already exists: $tagName")
    }
    return ChangeTagEvent(projectId = this.getId(), tagId = tagId, tagName = tagName)
}