package ru.quipy.projections
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import java.util.UUID


const val SCHEMA = "projection"

object ProjectTable : UUIDTable(name = "$SCHEMA.project"){
    var projectTitle = text("title")
    var creatorId = uuid("user_id")
    val createdAt = long("created_at")
}
object StatusTable : UUIDTable(name = "$SCHEMA.status"){
    var name = text("name")
    var projectId = reference("project_id", ProjectTable.id)
    val createdAt = long("created_at")
}
object TaskTable : UUIDTable(name = "$SCHEMA.task"){
    var name = text("name")
    var description = text("description")
    var tagId = reference("tag_id", StatusTable.id)
    var projectId = reference("project_id", ProjectTable.id)
    val createdAt = long("created_at")
}