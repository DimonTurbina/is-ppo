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
        val taskStatus = proj.projectTags.entries
            .filter { it.value.name == TagEntity.DEFAULT_TAG }
            .map { it.key }
        projectEsService.update(projectId) {
            it.assignTagToTask(taskStatus.first(), null)
        }
        return taskEsService.create { it.createTask(projectId, UUID.randomUUID(), taskName, taskStatus.first(), description) }
    }

    @PostMapping("/{projectId}/{taskId}/changeTask/{newName}")
    fun changeTask(@PathVariable projectId: UUID, @PathVariable taskId: UUID, @PathVariable newName: String) : TaskNameChangeEvent? {
        return taskEsService.update(taskId){
            it.changeTask(taskId, newName)
        }
    }

    @PostMapping("/{taskId}/addUser/{userId}")
    fun addUser(@PathVariable taskId: UUID, @PathVariable userId: UUID) : ListExecutorsUpdatedEvent?{
        return taskEsService.update(taskId){
            it.addUser(userId, taskId)
        }
    }

    @GetMapping("/{taskId}")
    fun getTask (@PathVariable taskId: UUID): TaskAggregateState? {
        return taskEsService.getState(taskId)
    }
}