package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
import java.lang.IllegalArgumentException
import java.util.*

@RestController
@RequestMapping("/projects")
class ProjectController(
    val taskEsService: EventSourcingService<UUID, TaskAggregate, TaskAggregateState>,
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>
) {
    @PostMapping("/{projectTitle}")
    fun createProject(@PathVariable projectTitle: String, @RequestParam creatorId: String) : ProjectCreatedEvent {
        return projectEsService.create { it.create(UUID.randomUUID(), projectTitle, creatorId) }
    }

    @GetMapping("/{projectId}")
    fun getAccount(@PathVariable projectId: UUID) : ProjectAggregateState? {
        return projectEsService.getState(projectId)
    }

    @PostMapping("/{projectId}/tags/createTag/{tagName}")
    fun createTag(@PathVariable projectId: UUID, @PathVariable tagName: String) : TagCreatedEvent{
        return projectEsService.update(projectId) {
            it.createTag(tagName)
        }
    }

    @PostMapping("/{projectId}/{taskId}/addTag/{tagId}")
    fun assignTagToTask(@PathVariable projectId: UUID, @PathVariable taskId: UUID, @PathVariable tagId: UUID):
            AssignedTagToTaskEvent {
        val task = taskEsService.getState(taskId)
            ?: throw IllegalArgumentException("No such task: $taskId")
        projectEsService.update(projectId)  {
            it.assignTagToTask(tagId, taskId, task.status)
        }
        return taskEsService.update(taskId){
            it.tagAssignedToTaskEvent(projectId, taskId, tagId)
        }
    }

    @DeleteMapping("/{projectId}/tags/deleteTag/{tagId}")
    fun deleteTag(@PathVariable projectId: UUID, @PathVariable tagId: UUID) : DeleteTagEvent{
        return projectEsService.update(projectId) {
            it.deleteTag(tagId)
        }
    }

    @PostMapping("/{projectId}/tags/changeTag/{tagId}/{newTagName}")
    fun changeTag(@PathVariable projectId: UUID, @PathVariable tagId: UUID, @PathVariable newTagName: String) : ChangeTagEvent{
        return projectEsService.update(projectId) {
            it.changeTag(tagId, newTagName)
        }
    }

    @PostMapping("/{projectId}/addUser/{userId}")
    fun addUser(@PathVariable projectId: UUID, @PathVariable userId: UUID) : AddUserToProjectEvent{
        return projectEsService.update(projectId){
            it.addUser(userId)
        }
    }
}