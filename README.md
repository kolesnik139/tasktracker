# Task Tracker Project

This is a sample application that provides with REST API to perform CRUD operations on tasks.
It uses an in-memory database (H2) to store the data. If you want to use another relational database, such as MySQL or PostgreSQL,
you should change the database connection settings in application.properties.
## How to Run

* Clone this repository
* Make sure you are using JDK 17 or higher, and Maven 3.x
* Open console and run one by one:
```
cd project_root_folder
mvn clean package
java -jar target\tasktracker-0.0.1-SNAPSHOT.jar
```
* Check the stdout to make sure no exceptions are thrown

Once the application runs you should see something like this

```
2023-09-28T12:21:09.625+03:00  INFO 5920 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2023-09-28T12:21:09.646+03:00  INFO 5920 --- [           main] c.p.tasktracker.TasktrackerApplication   : Started TasktrackerApplication in 3.88 seconds (process running for 4.394)
```

## How to Use
You can use the endpoints below with the URL http://localhost:8080.

You can import TaskTracker_demo.postman_collection.json into Postman to try it out.

### Add a Task
POST /api/tasks

Body:
```
{
    "statusId": 1,
    "title": "Title 1",
    "description": "Description 1",
    "dueDate": "2023-09-01"
}
```
```
statusId: Required positive integer. Should be valid ststus id form statuses table
title: Required string up to 255 characters
description: String
dueDate: Date
```

Success Response:
```
Code: 201 Created
Content: "Task added successfully!"
```
Error Response:
```
Code: 400 Bad Request
Content: ["error1", "error2", ...]
```
### Update Task Status
PUT /api/tasks/{taskId}/status

URL Params:
```
taskId: ID of the task to be updated
```
Query Params:
```
statusId: New status ID for the task
```
Success Response:

```
Code: 200 OK
Content: "Task status updated successfully!"
```
Error Response:
```
Code: 404 Not Found
Content: "Failed to update task status. Task not found."
```
### Deactivate a Task
DELETE /api/tasks/{taskId}

URL Params:
```
taskId: ID of the task to be deactivated
```
Success Response:
```
Code: 200 OK
Content: "Task deleted successfully!"
```
Error Response:
```
Code: 404 Not Found
Content: "Failed to delete task. Task not found."
```
### Retrieve a Task by ID
GET /api/tasks/{id}

URL Params:
```
id: ID of the task
```
Success Response:
```
Code: 200 OK
Content: {
    "id": 1,
    "statusId": 1,
    "title": "Title 1",
    "description": "Description 1",
    "dueDate": "2023-09-01"
}
```
Error Response:
```
Code: 404 Not Found
```
### Get Tasks with Filters/Sort/Pagination
GET /api/tasks

Query Params:
```
pageNum: The page number (default: 0)
pageSize: Number of tasks per page (default: 10)
sortField: Field to sort by (optional)
sortOrder: Order to sort (ASC or DESC) (optional)
id: Filter tasks by ID (default: -1)
statusId: Filter tasks by status ID (default: -1)
title: Filter tasks by title (optional)
description: Filter tasks by description (optional)
dueDate: Filter tasks by due date (format: yyyy-MM-dd) (optional)
```
Success Response:
```
Code: 200 OK
Content:{
    "content": [
        {
            "id": 1,
            "statusId": 1,
            "title": "Title 5",
            "description": "Description 1",
            "dueDate": "2023-09-25"
        },
    ...
    ],
    "pageNum": 0,
    "pageSize": 10,
    "totalItems": 4,
    "sortField": "id",
    "sortOrder": "DESC"
}
```
### Retrieve All Task Statuses
GET /api/statuses

Success Response:
```
Code: 200 OK
Content: [
    {
        "id": 1,
        "name": "To Do"
    },
    {
        "id": 2,
        "name": "In Progress"
    },
    ...
]
```