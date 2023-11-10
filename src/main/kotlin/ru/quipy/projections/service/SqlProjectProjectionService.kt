package ru.quipy.projections.service

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import ru.quipy.dto.ProjectDto
import ru.quipy.dto.ProjectsDto
import ru.quipy.dto.TagDto
import ru.quipy.dto.TaskDto
import ru.quipy.projections.ProjectTable
import ru.quipy.projections.StatusTable
import ru.quipy.projections.TaskTable
import java.util.UUID

@Service
class SqlProjectProjectionService {
    fun getProjectById(projectId: UUID): ProjectDto =
        transaction {
            ProjectTable
                .slice(ProjectTable.id, ProjectTable.projectTitle, ProjectTable.createdAt, ProjectTable.creatorId)
                .select(ProjectTable.id eq projectId)
                .map {
                    ProjectDto(
                        id = it[ProjectTable.id].value,
                        title = it[ProjectTable.projectTitle],
                        createdAt = it[ProjectTable.createdAt].toString(),
                        creatorId = it[ProjectTable.creatorId],
                        tasks = getAllTasksInProject(projectId),
                        tags = getAllTagsProject(projectId),
                    )
                }
                .firstOrNull()
                ?: throw IllegalArgumentException("No such project $projectId")
        }
    fun getAllTasksInProject(projectId: UUID): List<TaskDto> =
        transaction {
            TaskTable
                .slice(TaskTable.id, TaskTable.name, TaskTable.description, TaskTable.tagId,
                    TaskTable.projectId, TaskTable.createdAt)
                .select(TaskTable.projectId eq projectId)
                .map {
                    TaskDto(
                        id = it[TaskTable.id].value,
                        name = it[TaskTable.name],
                        description = it[TaskTable.description],
                        tagId = it[TaskTable.tagId].value,
                        createdAt = it[TaskTable.createdAt].toString(),
                    )
                }
        }
    fun getAllTagsProject(projectId: UUID): List<TagDto> =
        transaction {
            StatusTable
                .slice(StatusTable.id, StatusTable.name, StatusTable.projectId, StatusTable.createdAt)
                .select(StatusTable.projectId eq projectId)
                .map {
                    TagDto(
                        id = it[StatusTable.id].value,
                        name = it[StatusTable.name],
                        createdAt = it[StatusTable.createdAt].toString(),
                    )
                }
        }
    fun getAllProjects(): List<ProjectsDto> =
        transaction {
            ProjectTable
                .slice(ProjectTable.id, ProjectTable.projectTitle, ProjectTable.createdAt, ProjectTable.creatorId)
                .selectAll()
                .map {
                    ProjectsDto(
                        id = it[ProjectTable.id].value,
                        title = it[ProjectTable.projectTitle],
                        createdAt = it[ProjectTable.createdAt].toString(),
                        creatorId = it[ProjectTable.creatorId],
                    )
                }
        }

    fun getProjectStatuses(projectId: UUID): List<TagDto> =
        transaction {
            StatusTable
                .slice(StatusTable.id, StatusTable.name, StatusTable.createdAt)
                .select(StatusTable.projectId eq projectId)
                .map {
                    TagDto(
                        id = it[StatusTable.id].value,
                        name = it[StatusTable.name],
                        createdAt = it[StatusTable.createdAt].toString(),
                    )
                }
        }


}