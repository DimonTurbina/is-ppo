package ru.quipy.projections

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
    aggregateClass = TaskAggregate::class, subscriberName = "demo-subs-stream1"
)
class AnnotationBasedTaskEventsSubscriber {
    val logger: Logger = LoggerFactory.getLogger(AnnotationBasedTaskEventsSubscriber::class.java)
    @SubscribeEvent
    fun assignedTagToTask(event: StatusAssignedToTaskEvent){
        logger.info("in project ${event.projectId} status assigned ${event.statusId} to task $event.ta")
        transaction {
            TaskTable.update({TaskTable.id eq event.taskId }) {
                it[tagId] = event.statusId
            }
        }
    }

    @SubscribeEvent
    fun createdTask(event: TaskCreatedEvent){
        logger.info("in project ${event.projectId} created task ${event.taskId}")

        transaction {
            TaskTable.insert {
                it[id] = event.taskId
                it[name] = event.taskName
                it[description] = event.description
                it[tagId] = event.statusId
                it[projectId] = event.projectId
                it[createdAt] = event.createdAt
            }
        }
    }

    @SubscribeEvent
    fun changedTask(event: TaskNameChangedEvent){
        logger.info("in task ${event.taskId} changed name on ${event.taskName}")

        transaction {
            TaskTable.update({ TaskTable.id eq event.taskId }) {
                it[name] = event.name
            }
        }
    }
}