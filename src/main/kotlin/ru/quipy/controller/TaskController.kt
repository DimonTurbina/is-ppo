package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
import java.lang.IllegalArgumentException
import java.time.Duration
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
        val taskStatus = proj.taskStatuses.entries
            .filter { it.value.name == StatusEntity.DEFAULT_TAG }
            .map { it.key }

        val taskId = UUID.randomUUID()
        projectEsService.update(projectId){
            it.assignTaskToProject(taskId)
        }
        proj.tasks.add(taskId)
        return taskEsService.create { it.createTask(projectId, taskId, taskName, taskStatus.first(), description) }
    }

    @PostMapping("/{projectId}/tasks/deleteTask/{taskId}")
    fun deleteTask(@PathVariable projectId: UUID, @PathVariable taskId: UUID) : TaskDeletedEvent {
        val proj = projectEsService.getState(projectId)
                ?: throw IllegalArgumentException("No such project: $projectId")

        taskEsService.getState(taskId)
        proj.tasks.remove(projectId)
        return taskEsService.create { it.deleteTask(projectId, taskId) }
    }

    @PostMapping("/{projectId}/{taskId}/changeTask/{newName}{duration}{description}{status}")
    fun changeTask(@PathVariable projectId: UUID, @PathVariable taskId: UUID, @PathVariable newName: String, @PathVariable duration: Duration, @PathVariable description: String, @PathVariable status : UUID) : TaskNameChangeEvent? {
        return taskEsService.update(taskId){
            it.changeTask(taskId, newName, duration, description, status)
        }
    }

    @PostMapping("/{taskId}/addUser/{userId}")
    fun addUser(@PathVariable taskId: UUID, @PathVariable userId: UUID) : ListExecutorsUpdatedEvent?{
        return taskEsService.update(taskId){
            it.addUser(userId, taskId)
        }
    }

    @PostMapping("/{taskId}/removeUser/{userId}")
    fun removeUser(@PathVariable taskId: UUID, @PathVariable userId: UUID) : ListExecutorsUpdatedEvent?{
        return taskEsService.update(taskId){
            it.removeUser(userId, taskId)
        }
    }

    @GetMapping("/{taskId}")
    fun getTask (@PathVariable taskId: UUID): TaskAggregateState? {
        return taskEsService.getState(taskId)
    }
}