CREATE SCHEMA IF NOT EXISTS projection;

SET SEARCH_PATH TO projection;

CREATE TABLE IF NOT EXISTS project
(
    id UUID PRIMARY KEY,
    title text NOT NULL,
    user_id UUID NOT NULL,
    created_at BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS status
(
    id UUID PRIMARY KEY,
    name text NOT NULL,
    project_id UUID NOT NULL REFERENCES project(id),
    created_at BIGINT NOT NULL
    );

CREATE TABLE IF NOT EXISTS project_user
(
    project_id UUID NOT NULL REFERENCES project(id),
    user_id UUID NOT NULL,
    created_at BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS task
(
    id UUID PRIMARY KEY,
    name text NOT NULL,
    description text NOT NULL,
    tag_id UUID NOT NULL REFERENCES status(id),
    project_id UUID NOT NULL REFERENCES project(id),
    created_at BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS user_task
(
    user_id UUID NOT NULL,
    task_id UUID NOT NULL REFERENCES task(id),
    created_at BIGINT NOT NULL
)