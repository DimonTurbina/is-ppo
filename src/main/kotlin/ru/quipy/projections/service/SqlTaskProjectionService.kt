package ru.quipy.projections.service

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import ru.quipy.dto.TaskDto
import ru.quipy.projections.TaskTable
import java.util.*

@Service
class SqlTaskProjectionService {
    fun getTaskById(taskId: UUID): TaskDto =
        transaction {
            TaskTable
                .slice(TaskTable.id, TaskTable.name, TaskTable.description, TaskTable.tagId, TaskTable.createdAt)
                .select(TaskTable.id eq taskId)
                .map {
                    TaskDto(
                        id = it[TaskTable.id].value,
                        name = it[TaskTable.name],
                        description = it[TaskTable.description],
                        tagId = it[TaskTable.tagId].value,
                        createdAt = it[TaskTable.createdAt].toString(),
                    )
                }
                .firstOrNull()
                ?: throw IllegalArgumentException("No such task $taskId")
        }
}