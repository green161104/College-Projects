using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WebAPI.Controllers;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.DTO.response;
using WebAPI.Entity;
using WebAPI.Services;

namespace WebAPITests.controllers
{
    public class TaskControllerTests : IDisposable
    {
        private readonly WebApiContext _webApiContext;
        private readonly DataContext _context;
        private readonly TaskService _taskService;
        private readonly TaskController _controller;

        /// <summary>
        /// Constructor that initializes the necessary dependencies for the TaskControllerTests class
        /// </summary>
        public TaskControllerTests()
        {
            _webApiContext = new WebApiContext();
            _context = _webApiContext.getWebApiContext();
            _taskService = new TaskService(_context);
            _controller = new TaskController(_taskService);
        }

        /// <summary>
        /// Dispose method to delete the database after the tests are run
        /// </summary>
        public void Dispose()
        {
            _context.Database.EnsureDeleted();
            _context.Dispose();
        }

        /// <summary>
        /// Tests the GetAllTasks method to ensure it returns an OK result with a list of tasks
        /// </summary>
        [Fact]
        public async void GetAllTasks_ReturnsOkResult_WithListOfTasks()
        {
            // Act
            var result = await _controller.GetAllTasks();

            // Assert
            Assert.IsType<ActionResult<List<WebAPI.Entity.PlantTask>>>(result);
        }

        /// <summary>
        /// Tests the GetTaskById method to ensure it returns an OK result with the expected task
        /// </summary>
        [Fact]
        public async void GetTaskById_ReturnsOkResult_WithTask()
        {
            var expectedTaskId = 1;

            // Act
            var result = await _controller.GetTaskById(expectedTaskId);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var task = Assert.IsType<WebAPI.Entity.PlantTask>(okResult.Value);
            Assert.Equal(1, task.Id);
        }

        /// <summary>
        /// Tests the GetTaskById method to ensure it returns a NotFound result when the task is not found
        /// </summary>
        [Fact]
        public async void GetTaskById_ReturnsNotFound_WithMessage()
        {
            var expectedTaskId = 100;

            // Act
            var result = await _controller.GetTaskById(expectedTaskId);

            // Assert
            var notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(notFoundResult.Value);
            Assert.Equal("Task not found!", errorResponse.error);
        }

        /// <summary>
        /// Tests the GetTasksByPlantId method to ensure it returns an OK result with a list of tasks
        /// </summary>
        [Fact]
        public async void GetTasksByPlantId_ReturnsOkResult_WithListOfTasks()
        {
            // Act: Fetch tasks by PlantId
            var result = await _controller.GetTasksByPlantId(2);

            // Assert: Ensure the result is an OkObjectResult
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var data = okResult.Value as dynamic;
            var tasks = Assert.IsType<List<WebAPI.Entity.PlantTask>>(data.data);

            // Ensure the correct number of tasks is returned
            Assert.NotNull(tasks);
            Assert.Equal(1, tasks.Count); // Adjust based on your seeded data
        }

        /// <summary>
        /// Tests the GetTasksByPlantId method to ensure it returns a NotFound result when the plant has no tasks
        /// </summary>
        [Fact]
        public async void GetTasksByPlantId_ReturnsNotFound_WithMessage()
        {
            var expectedPlantId = 3;

            // Act
            var result = await _controller.GetTasksByPlantId(expectedPlantId);

            // Assert
            var notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(notFoundResult.Value);
            Assert.Equal("This plant doesn't have tasks.", errorResponse.error);
        }

        /// <summary>
        /// Tests the GetTasksByAdminId method to ensure it returns an OK result with a list of tasks
        /// </summary>
        [Fact]
        public async void GetTasksByAdminId_ReturnsOkResult_WithListOfTasks()
        {
            // Act: Fetch tasks by AdminId
            var result = await _controller.GetTasksByAdminId(2);

            // Assert: Ensure the result is an OkObjectResult
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var data = okResult.Value as dynamic;
            var tasks = Assert.IsType<List<WebAPI.Entity.PlantTask>>(data.data);

            // Check that tasks are returned and match the expected count
            Assert.NotNull(tasks);
            Assert.Equal(3, tasks.Count); // Ensure this matches your seeded data
        }

        /// <summary>
        /// Tests the GetTasksByAdminId method to ensure it returns a NotFound result when the admin has no tasks
        /// </summary>
        [Fact]
        public async void GetTasksByAdminId_ReturnsNotFound_WithMessage()
        {
            var expectedAdminId = 3;

            // Act
            var result = await _controller.GetTasksByAdminId(expectedAdminId);

            // Assert
            var notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(notFoundResult.Value);
            Assert.Equal("This admin hasn't created tasks.", errorResponse.error);
        }

        /// <summary>
        /// Tests the AddTask method to ensure it returns a Created result with the newly created task
        /// </summary>
        [Fact]
        public async void AddTask_ReturnsOkResult_WithTask()
        {
            var newtask = new WebAPI.DTO.request.TaskRequestDto
            {
                AdminId = 1,
                PlantId = 1,
                TaskName = "TaskName4",
                TaskDescription = "TaskDescription4"
            };

            // Act
            var result = await _controller.AddTask(newtask);

            // Assert
            var createdAtActionResult = Assert.IsType<CreatedAtActionResult>(result.Result);
            var createdTask = Assert.IsAssignableFrom<WebAPI.Entity.PlantTask>(createdAtActionResult.Value);
            Assert.Equal(newtask.AdminId, createdTask.AdminId);
            Assert.Equal(newtask.PlantId, createdTask.PlantId);
            Assert.Equal(newtask.TaskName, createdTask.TaskName);
            Assert.Equal(newtask.TaskDescription, createdTask.TaskDescription);
        }

        /// <summary>
        /// Tests the AddTask method to ensure it returns a BadRequest result when the input is invalid
        /// </summary>
        [Fact]
        public async void AddTask_ReturnsBadRequest_WithMessage()
        {
            // Act
            var result = await _controller.AddTask(null);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(badRequestResult.Value);
            Assert.Equal("Invalid data", errorResponse.error);
        }

        /// <summary>
        /// Tests the UpdateTask method to ensure it returns an OK result with the updated task
        /// </summary>
        [Fact]
        public async void UpdateTask_ReturnsOkResult_WithTask()
        {
            var id = 1;
            var taskDto = new WebAPI.DTO.request.TaskRequestDto
            {
                AdminId = 1,
                PlantId = 1,
                TaskName = "Updated TaskName",
                TaskDescription = "Updated TaskDescription"
            };

            // Act
            var result = await _controller.UpdateTask(id, taskDto);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var returnedTaskDto = Assert.IsType<TaskRequestDto>(okResult.Value);

            // Verify that the returned DTO matches the updated values
            Assert.Equal(taskDto.AdminId, returnedTaskDto.AdminId);
            Assert.Equal(taskDto.PlantId, returnedTaskDto.PlantId);
            Assert.Equal(taskDto.TaskName, returnedTaskDto.TaskName);
            Assert.Equal(taskDto.TaskDescription, returnedTaskDto.TaskDescription);
        }

        /// <summary>
        /// Tests the UpdateTask method to ensure it returns a BadRequest result when the input is invalid
        /// </summary>
        [Fact]
        public async void UpdateTask_ReturnsBadRequest_WithMessage()
        {
            var expectedTask = 1;
            // Act
            var result = await _controller.UpdateTask(expectedTask, null);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result.Result);
            Assert.Equal("Invalid data", badRequestResult.Value);
        }

        /// <summary>
        /// Tests the UpdateTask method to ensure it returns a NotFound result when the task is not found
        /// </summary>
        [Fact]
        public async void UpdateTask_ReturnsNotFound_WithMessage()
        {
            var task = new WebAPI.DTO.request.TaskRequestDto
            {
                AdminId = 2,
                PlantId = 1,
                TaskName = "TaskName4",
                TaskDescription = "TaskDescription4"
            };

            // Act
            var result = await _controller.UpdateTask(4, task);

            // Assert
            var notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(notFoundResult.Value);
            Assert.Equal("Task not found.", errorResponse.error);
        }

        /// <summary>
        /// Tests the DeleteTask method to ensure it returns a NoContent result when the task is successfully deleted
        /// </summary>
        [Fact]
        public async void DeleteTask_ReturnsNoContent_WhenTaskDeleted()
        {

            var deletedId = 1;
            
            // Act: Delete the task
            var result = await _controller.DeleteTask(deletedId);

            // Assert: Check that the result is NoContent
            Assert.IsType<NoContentResult>(result);

            // Verify the task has been deleted
            var deletedTask = await _context.Task.FindAsync(deletedId);
            Assert.Null(deletedTask);
        }

        /// <summary>
        /// Tests the DeleteTask method to ensure it returns a NotFound result when the task is not found
        /// </summary>
        [Fact]
        public async void DeleteTask_ReturnsNotFound_WhenTaskNotFound()
        {
            var nonExistentTaskId = 100;

            // Act
            var result = await _controller.DeleteTask(nonExistentTaskId);

            // Assert
            var notFoundResult = Assert.IsType<NotFoundObjectResult>(result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(notFoundResult.Value);
            Assert.Equal("Task not found!", errorResponse.error);
        }

    }
}
