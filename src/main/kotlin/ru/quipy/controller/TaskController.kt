package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
import java.lang.IllegalArgumentException
import java.util.*

@RestController
@RequestMapping("/tasks")
class TaskController(
    val taskEsService: EventSourcingService<UUID, TaskAggregate, TaskAggregateState>,
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>
){
    @PostMapping("/{projectId}/tasks/createTask/{taskName}")
    fun createTask(@PathVariable projectId: UUID, @PathVariable taskName: String, @RequestParam description: String) : TaskCreatedEvent {
        val proj = projectEsService.getState(projectId)
            ?: throw IllegalArgumentException("No such project: $projectId")
        val taskStatus = proj.projectStatuses.entries
            .filter { it.value.name == StatusEntity.DEFAULT_STATUS_NAME }
            .map { it.key }
        projectEsService.update(projectId) {
            it.assignStatusToTask(taskStatus.first(), null)
        }
        return taskEsService.create { it.createTask(projectId, UUID.randomUUID(), taskName, taskStatus.first(), description) }
    }

    @PostMapping("/{projectId}/{taskId}/changeTask/{newName}")
    fun changeTask(@PathVariable projectId: UUID, @PathVariable taskId: UUID, @PathVariable newName: String) : TaskNameChangedEvent? {
        return taskEsService.update(taskId){
            it.changeTask(taskId, newName)
        }
    }

    @PostMapping("/{taskId}/addUser/{userId}")
    fun addUser(@PathVariable taskId: UUID, @PathVariable userId: UUID) : ExecutorsUpdatedEvent?{
        return taskEsService.update(taskId){
            it.addUser(userId, taskId)
        }
    }

    @GetMapping("/{taskId}")
    fun getTask (@PathVariable taskId: UUID): TaskAggregateState? {
        return taskEsService.getState(taskId)
    }
}