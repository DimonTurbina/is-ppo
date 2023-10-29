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

    @PostMapping("/{projectId}/statuses/createStatus/{statusName}")
    fun createStatus(@PathVariable projectId: UUID, @PathVariable statusName: String) : StatusCreatedEvent{
        return projectEsService.update(projectId) {
            it.createStatus(statusName)
        }
    }

    @PostMapping("/{projectId}/{taskId}/addStatus/{statusId}")
    fun assignStatusToTask(@PathVariable projectId: UUID, @PathVariable taskId: UUID, @PathVariable statusId: UUID):
            StatusAssignedToTaskEvent {
        val task = taskEsService.getState(taskId)
            ?: throw IllegalArgumentException("No such task: $taskId")
        val project = projectEsService.getState(projectId)
            ?: throw IllegalArgumentException("No such project: $projectId")
        if(project.projectStatus.containsKey(statusId)) {
            projectEsService.update(projectId) {
                it.assignStatusToTask(statusId, task.status)
            }
            return taskEsService.update(taskId) {
                it.statusAssignedToTaskEvent(projectId, taskId, statusId)
            }
        }
        else{
            throw IllegalArgumentException("No such status: $statusId")
        }
    }

    @DeleteMapping("/{projectId}/statuses/deleteStatus/{statusId}")
    fun deleteStatus(@PathVariable projectId: UUID, @PathVariable statusId: UUID) : StatusDeletedEvent{
        return projectEsService.update(projectId) {
            it.deleteStatus(statusId)
        }
    }

    @PostMapping("/{projectId}/statuses/changeStatus/{statusId}/{newStatusName}")
    fun changeStatus(@PathVariable projectId: UUID, @PathVariable statusId: UUID, @PathVariable newStatusName: String) : StatusChangedEvent{
        return projectEsService.update(projectId) {
            it.changeStatus(statusId, newStatusName)
        }
    }

    @PostMapping("/{projectId}/addUser/{userId}")
    fun addUser(@PathVariable projectId: UUID, @PathVariable userId: UUID) : UserAddedToProjectEvent{
        return projectEsService.update(projectId){
            it.addUser(userId)
        }
    }
}