using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Net;
using WebAPI.DTO.request;
using WebAPI.DTO.response;
using WebAPI.Entity;
using WebAPI.Services;

namespace WebAPI.Controllers
{
    /// <summary>
    /// Handles HTTP requests related to tasks.
    /// </summary>
    [Route("api/[controller]")]
    [ApiController]
    public class TaskController : ControllerBase
    {
        private readonly TaskService _taskService;

        /// <summary>
        /// Initializes a new instance of the <see cref="TaskController"/> class.
        /// </summary>
        /// <param name="service">The task service used to interact with tasks.</param>
        public TaskController(TaskService service)
        {
            _taskService = service;
        }

        /// <summary>
        /// Retrieves all tasks from the database.
        /// </summary>
        /// <returns>A list of tasks.</returns>
        [HttpGet]
        [Authorize]
        public async Task<ActionResult<List<Entity.PlantTask>>> GetAllTasks()
        {
            try
            {

                var tasks = await _taskService.GetAllTasks();

                if (tasks == null || tasks.Count == 0)
                    return NoContent();

                return Ok(new
                {
                    data = tasks,
                    total = tasks.Count
                });
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{ex.Message}"
                });
            }
        }

        /// <summary>
        /// Retrieves a specific task by its identifier.
        /// </summary>
        /// <param name="id">The identifier of the task.</param>
        /// <returns>The task with the specified identifier.</returns>
        [HttpGet("{id}")]
        [Authorize]
        public async Task<ActionResult<Entity.PlantTask>> GetTaskById(int id)
        {
            try
            {
                var task = await _taskService.GetTaskById(id);
                if (task == null)
                    return NotFound(new ErrorResponseDto { error = "Task not found!" });

                return Ok(task);
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{ex.Message}"
                });
            }
        }

        /// <summary>
        /// Retrieves all tasks associated with a specific plant identifier.
        /// </summary>
        /// <param name="id">The identifier of the plant.</param>
        /// <returns>A list of tasks associated with the specified plant.</returns>
        [HttpGet("plant/{id}")]
        [Authorize]
        public async Task<ActionResult<List<Entity.PlantTask>>> GetTasksByPlantId(int id)
        {
            try
            {
                var tasks = await _taskService.GetTasksByPlantId(id);
                if (tasks == null || tasks.Count == 0)
                    return NotFound(new ErrorResponseDto { error = "This plant doesn't have tasks." });

                // Only append headers if HttpContext is not null (for testing purposes)
                //if (HttpContext != null)
                //{
                //    Response.Headers.Append("Access-Control-Expose-Headers", "Content-Range");
                //    Response.Headers.Append("Content-Range", $"tasks 0-{tasks.Count}/{tasks.Count}");
                //}
                return Ok(new
                {
                    data = tasks,
                    total = tasks.Count
                });
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{ex.Message}"
                });
            }
        }

        /// <summary>
        /// Retrieves all tasks associated with a specific admin identifier.
        /// </summary>
        /// <param name="id">The identifier of the admin.</param>
        /// <returns>A list of tasks associated with the specified admin.</returns>
        [HttpGet("admin/{id}")]
        [Authorize]
        public async Task<ActionResult<List<Entity.PlantTask>>> GetTasksByAdminId(int id)
        {
            try
            {
                var tasks = await _taskService.GetTasksByAdminId(id);
                if (tasks == null || tasks.Count == 0)
                    return NotFound(new ErrorResponseDto { error = "This admin hasn't created tasks." });

                // Only append headers if HttpContext is not null (for testing purposes)
                //if (HttpContext != null)
                //{
                //    Response.Headers.Append("Access-Control-Expose-Headers", "Content-Range");
                //    Response.Headers.Append("Content-Range", $"tasks 0-{tasks.Count}/{tasks.Count}");
                //}
                return Ok(new
                {
                    data = tasks,
                    total = tasks.Count
                });
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{ex.Message}"
                });
            }
        }

        /// <summary>
        /// Adds a new task to the database.
        /// </summary>
        /// <param name="taskDto">The data transfer object containing task details.</param>
        /// <returns>The created task.</returns>
        [HttpPost]
        [Authorize(Policy = "AdminPolicy")] // Apenas admins podem aceder
        public async Task<ActionResult<Entity.PlantTask>> AddTask(TaskRequestDto taskDto)
        {
            if (taskDto == null)
                return BadRequest(new ErrorResponseDto { error = "Invalid data" });

            try
            {
                var createdTask = await _taskService.AddNewTask(taskDto);
                return CreatedAtAction(nameof(GetTaskById), new { id = createdTask.Id }, createdTask);
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{ex.Message}"
                });
            }
        }

        /// <summary>
        /// Updates an existing task in the database.
        /// </summary>
        /// <param name="id">The identifier of the task to update.</param>
        /// <param name="taskDto">The data transfer object containing updated task details.</param>
        /// <returns>The updated task.</returns>
        [HttpPut("{id}")]
        [Authorize(Policy = "AdminPolicy")] // Apenas admins podem aceder
        public async Task<ActionResult<Entity.PlantTask>> UpdateTask(int id, TaskRequestDto taskDto)
        {
            if (taskDto == null)
                return BadRequest("Invalid data");

            try
            {
                var updatedTask = await _taskService.UpdateTask(id, taskDto);
                if (!updatedTask)
                    return NotFound(new ErrorResponseDto{ error = "Task not found." });

                return Ok(taskDto);
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{ex.Message}"
                }); ;
            }
        }

        /// <summary>
        /// Deletes a task from the database.
        /// </summary>
        /// <param name="id">The identifier of the task to delete.</param>
        /// <returns>No content if deletion was successful.</returns>
        [HttpDelete("{id}")]
        [Authorize(Policy = "AdminPolicy")] // Apenas admins podem aceder
        public async Task<ActionResult> DeleteTask(int id)
        {
            try
            {
                var successfulDelete = await _taskService.DeleteTask(id);
                if (!successfulDelete)
                    return NotFound(new ErrorResponseDto { error = "Task not found!" });

                return NoContent();
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{ex.Message}"
                });
            }
        }
    }
}
