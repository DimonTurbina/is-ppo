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
    if (taskStatuses.values.any { it.name == name }) {
        throw IllegalArgumentException("Tag already exists: $name")
    }
    return TagCreatedEvent(projectId = this.getId(), tagId = UUID.randomUUID(), tagName = name)
}

fun ProjectAggregateState.assignTagToTask(tagId: UUID, taskId: UUID, oldTagId: UUID): TagAssignedToTaskEvent {
    if (!taskStatuses.containsKey(tagId)) {
        throw IllegalArgumentException("Tag doesn't exists: $tagId")
    }

    return TagAssignedToTaskEvent(projectId = this.getId(), tagId = tagId, taskId = taskId, oldTagId = oldTagId)
}

fun ProjectAggregateState.assignTaskToProject(taskId: UUID,): TaskAssignedToProjEvent {
    if (!tasks.contains(taskId)) {
        throw IllegalArgumentException("Tag doesn't exists: $taskId")
    }

    return TaskAssignedToProjEvent(projectId = this.getId(), taskId = taskId)
}

fun ProjectAggregateState.addUser(userId: UUID): AddUserToProjectEvent {
    if(members.containsKey(userId)){
        throw IllegalArgumentException("User with id: $userId already exists in project")
    }
    return AddUserToProjectEvent(projectId = this.getId(), userId = userId)
}

fun ProjectAggregateState.deleteTag(tagId: UUID): DeleteTagEvent{
    if(!taskStatuses.containsKey(tagId)){
        throw IllegalArgumentException("Tag doesn't exists: $tagId")
    }
    val tag = taskStatuses.get(tagId)
    if(tag?.count!! > 0){
        throw IllegalArgumentException("Tag have tasks:")
    }
    return DeleteTagEvent(projectId = this.getId(), tagId = tagId)
}

fun ProjectAggregateState.changeTag(tagId: UUID, tagName: String): ChangeTagEvent{
    if(!taskStatuses.containsKey(tagId)){
        throw IllegalArgumentException("Tag doesn't exists: $tagId")
    }
    if(taskStatuses.values.any { it.name == tagName }){
        throw IllegalArgumentException("Tag already exists: $tagName")
    }
    return ChangeTagEvent(projectId = this.getId(), tagId = tagId, tagName = tagName)
}