package ru.quipy.dto

import java.util.UUID

data class ProjectDto(
    val id: UUID,
    val title: String,
    val createdAt: String,
    val creatorId: UUID,
    val tasks: List<TaskDto>,
    val tags: List<TagDto>
)

data class ProjectsDto(
    val id: UUID,
    val title: String,
    val createdAt: String,
    val creatorId: UUID
)
data class TaskDto(
    val id: UUID,
    val name: String,
    val description: String,
    val tagId: UUID,
    val createdAt: String
)

data class TagDto(
    val id: UUID,
    val name: String,
    val createdAt: String
)