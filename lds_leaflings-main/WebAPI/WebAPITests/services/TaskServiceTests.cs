using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.Services;

namespace WebAPITests.services
{
    public class TaskServiceTests : IDisposable
    {
        private readonly DataContext _context;
        private readonly TaskService _taskService;
        //private readonly IConfiguration _configuration;

        /// <summary>
        /// Assigns the context and configuration to the TaskServiceTests class
        /// </summary>
        public TaskServiceTests()
        {
            var webApiContext = new WebApiContext();

            _context = webApiContext.getWebApiContext();
            //_configuration = new ConfigurationBuilder().AddJsonFile("appsettings.json").Build();
            _taskService = new TaskService(_context);
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
        /// Test case to verify that the GetTaskById method returns a non-null task when a valid task ID is provided
        /// </summary>
        [Fact]
        public async void GetTaskByIdAsyncShouldNotBeNull()
        {
            var expectedTaskId = 1;
            var task = await _taskService.GetTaskById(expectedTaskId);

            Assert.NotNull(task);
        }

        /// <summary>
        /// Test case to verify that the GetTaskById method returns null when an invalid task ID is provided
        /// </summary>
        [Fact]
        public async void GetTaskByIdAsyncShouldBeNull()
        {
            var expectedTaskId = 100;
            var task = await _taskService.GetTaskById(expectedTaskId);

            Assert.Null(task);
        }

        /// <summary>
        /// Test case to verify that the GetAllTasks method returns a non-empty list of tasks
        /// </summary>
        [Fact]
        public async void GetAllTasksAsyncShouldNotBeEmpty()
        {
            var tasks = await _taskService.GetAllTasks();

            Assert.NotEmpty(tasks);
        }

        /// <summary>
        /// Test case to verify that the GetTasksByPlantId method returns a non-empty list of tasks when a valid plant ID is provided
        /// </summary>
        [Fact]
        public async void GetTasksByPlantIdAsyncShouldNotBeEmpty()
        {
            var expectedPlantId = 1;
            var tasks = await _taskService.GetTasksByPlantId(expectedPlantId);

            Assert.NotEmpty(tasks);
        }

        /// <summary>
        /// Test case to verify that the GetTasksByPlantId method returns an empty list of tasks when an invalid plant ID is provided
        /// </summary>
        [Fact]
        public async void GetTasksByPlantIdAsyncShouldBeEmpty()
        {
            var expectedPlantId = 100;
            var tasks = await _taskService.GetTasksByPlantId(expectedPlantId);

            Assert.Empty(tasks);
        }

        /// <summary>
        /// Test case to verify that the GetTasksByAdminId method returns a non-empty list of tasks when a valid admin ID is provided
        /// </summary>
        [Fact]
        public async void GetTasksByAdminIdAsyncShouldNotBeEmpty()
        {
            var expectedAdminId = 2;
            var tasks = await _taskService.GetTasksByAdminId(expectedAdminId);

            Assert.NotEmpty(tasks);
        }

        /// <summary>
        /// Test case to verify that the GetTasksByAdminId method returns an empty list of tasks when an invalid admin ID is provided
        /// </summary>
        [Fact]
        public async void GetTasksByAdminIdAsyncShouldBeEmpty()
        {
            var expectedAdminId = 100;
            var tasks = await _taskService.GetTasksByAdminId(expectedAdminId);

            Assert.Empty(tasks);
        }

        /// <summary>
        /// Test case to verify that the AddNewTask method returns a non-null task when a valid task is added
        /// </summary>
        [Fact]
        public async void AddTaskAsyncShouldNotBeNull()
        {
            var task =  new TaskRequestDto
            {
                PlantId = 1,
                AdminId = 1,
                TaskName = "Test Task",
                TaskDescription = "Test Task Description"
            };

            var addedTask = await _taskService.AddNewTask(task);

            Assert.NotNull(addedTask);
        }

        /// <summary>
        /// Test case to verify that the AddNewTask method throws an InvalidOperationException when invalid IDs are provided in the task request
        /// </summary>
        [Fact]
        public async void AddTaskAsyncShouldThrowException()
        {
            var task = new TaskRequestDto
            {
                PlantId = 100,
                AdminId = 100,
                TaskName = "Test Task",
                TaskDescription = "Test Task Description"
            };

            await Assert.ThrowsAsync<InvalidOperationException>(() => _taskService.AddNewTask(task));
        }

        /// <summary>
        /// Test case to verify that the UpdateTask method updates the task successfully when a valid task ID and updated task data are provided
        /// </summary>
        [Fact]
        public async void UpdateTaskAsyncShouldNotBeNull()
        {
            var taskIdToUpdate = 1;

            var task = new TaskRequestDto
            {
                PlantId = 1,
                AdminId = 1,
                TaskName = "Updated Test Task",
                TaskDescription = "Test Task Description"
            };

            var isUpdated = await _taskService.UpdateTask(taskIdToUpdate, task);
            var updatedTask = await _taskService.GetTaskById(taskIdToUpdate);

            Assert.True(isUpdated);
            Assert.Equal("Updated Test Task", updatedTask.TaskName);
        }

        /// <summary>
        /// Test case to verify that the UpdateTask method returns false when an invalid task ID is provided
        /// </summary>
        [Fact]
        public async void UpdateTask_ShouldReturnFalseForNonExistentTask()
        {
            // Arrange
            var nonExistentTaskId = 999;
            var updatedTask = new TaskRequestDto
            {
                TaskName = "Updated Task",
                TaskDescription = "Updated Description",
                AdminId = 1,
                PlantId = 1
            };

            // Act
            var result = await _taskService.UpdateTask(nonExistentTaskId, updatedTask);

            // Assert
            Assert.False(result);
        }

        /// <summary>
        /// Test case to verify that the DeleteTask method deletes the task successfully when a valid task ID is provided
        /// </summary>
        [Fact]
        public async void DeleteTask_ShouldDeleteAdminSuccessfully()
        {
            var expectedTaskId = 1;
            var isDeleted = await _taskService.DeleteTask(expectedTaskId);
            Assert.True(isDeleted);
        }

        /// <summary>
        /// Test case to verify that the DeleteTask method returns false when an invalid task ID is provided
        /// </summary>
        [Fact]
        public async void DeleteTask_ShouldReturnFalseIfAdminDoesNotExist()
        {
            var expectedTaskId = 100;
            var isDeleted = await _taskService.DeleteTask(expectedTaskId);
            Assert.False(isDeleted);
        }
    }
}
