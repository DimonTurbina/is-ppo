package ru.quipy.projections

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(
    aggregateClass = ProjectAggregate::class, subscriberName = "demo-subs-stream"
)
class AnnotationBasedProjectEventsSubscriber {

    val logger: Logger = LoggerFactory.getLogger(AnnotationBasedProjectEventsSubscriber::class.java)

    @SubscribeEvent
    fun taskCreatedSubscriber(event: TaskCreatedEvent) {
        logger.info("Task created: {}", event.taskName)
    }

    @SubscribeEvent
    fun statusCreatedSubscriber(event: StatusCreatedEvent) {
        logger.info("Status created: {}", event.statusName)
    }
    @SubscribeEvent
    fun projectCreated(event: ProjectCreatedEvent){
        logger.info("project with id ${event.id} created")
        transaction {
            ProjectTable.insert {
                it[id] = event.projectId
                it[projectTitle] = event.title
                it[createdAt] = event.createdAt
            }
            StatusTable.insert {
                it[id] = event.defaultStatusId
                it[name] = event.defaultStatusName
                it[projectId] = event.projectId
                it[createdAt] = event.createdAt
            }
        }
    }
    @SubscribeEvent
    fun createdTag(event: StatusCreatedEvent){
        logger.info("in project ${event.projectId} created status ${event.statusId}")

        transaction {
            StatusTable.insert {
                it[id] = event.statusId
                it[name] = event.statusName
                it[projectId] = event.projectId
                it[createdAt] = event.createdAt
            }
        }
    }

    @SubscribeEvent
    fun deletedTag(event: StatusDeletedEvent){
        logger.info("in project ${event.projectId} deleted status ${event.statusId}")

        transaction {
            StatusTable.deleteWhere {
                id eq event.statusId
            }
        }
    }

    @SubscribeEvent
    fun changedTag(event: StatusChangedEvent){
        logger.info("in project ${event.projectId} changed tag ${event.statusId}")

        transaction {
            StatusTable.update({StatusTable.id eq event.statusId}) {
                it[name] = event.statusName
            }
        }
    }
}