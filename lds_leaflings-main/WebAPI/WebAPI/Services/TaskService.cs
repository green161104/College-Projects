using Microsoft.EntityFrameworkCore;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.Entity;

namespace WebAPI.Services
{
    /// <summary>
    /// Provides methods to manage tasks in the system.
    /// </summary>
    public class TaskService
    {
        private readonly DataContext _context;

        /// <summary>
        /// Initializes a new instance of the <see cref="TaskService"/> class.
        /// </summary>
        /// <param name="context">The data context used to interact with the database.</param>
        public TaskService(DataContext context)
        {
            _context = context;
        }

        /// <summary>
        /// Retrieves all tasks from the database, including related plant and admin data.
        /// </summary>
        /// <returns>A list of tasks.</returns>
        public async Task<List<Entity.PlantTask>> GetAllTasks()
        {
            return await _context.Task
               .Include(t => t.Plant)
               .Include(t => t.Admin)
               .ToListAsync();
        }

        /// <summary>
        /// Retrieves a specific task by its identifier.
        /// </summary>
        /// <param name="id">The identifier of the task.</param>
        /// <returns>The task with the specified identifier, or null if not found.</returns>
        public async Task<Entity.PlantTask?> GetTaskById(int id)
        {
            return await _context.Task.SingleOrDefaultAsync(t => t.Id == id);
        }

        /// <summary>
        /// Retrieves all tasks associated with a specific plant identifier.
        /// </summary>
        /// <param name="id">The identifier of the plant.</param>
        /// <returns>A list of tasks associated with the specified plant.</returns>
        public async Task<List<Entity.PlantTask>> GetTasksByPlantId(int id)
        {
            return await _context.Task
                .Include(t => t.Plant)
                .Include(t => t.Admin)
                .Where(t => t.PlantId == id)
                .ToListAsync();
        }

        /// <summary>
        /// Retrieves all tasks associated with a specific admin identifier.
        /// </summary>
        /// <param name="id">The identifier of the admin.</param>
        /// <returns>A list of tasks associated with the specified admin.</returns>
        public async Task<List<Entity.PlantTask>> GetTasksByAdminId(int id)
        {
            return await _context.Task
                .Include(t => t.Plant)
                .Include(t => t.Admin)
                .Where(t => t.AdminId == id)
                .ToListAsync();
        }

        /// <summary>
        /// Adds a new task to the database.
        /// </summary>
        /// <param name="task">The task DTO containing the task data.</param>
        /// <returns>The newly created task.</returns>
        /// <exception cref="InvalidOperationException">Thrown when the specified admin or plant does not exist.</exception>
        public async Task<Entity.PlantTask> AddNewTask(TaskRequestDto task)
        {
            // Verify that Admin and Plant exist
            var admin = await _context.Admin.FindAsync(task.AdminId);
            var plant = await _context.Plant.FindAsync(task.PlantId);

            if (admin == null || plant == null)
            {
                throw new InvalidOperationException("Admin or Plant not found for the provided IDs.");
            }

            var newTask = new Entity.PlantTask
            {
                AdminId = task.AdminId,
                PlantId = task.PlantId,
                TaskDescription = task.TaskDescription,
                TaskName = task.TaskName
            };

            _context.Task.Add(newTask);
            await _context.SaveChangesAsync();

            return newTask;
        }

        /// <summary>
        /// Updates an existing task in the database.
        /// </summary>
        /// <param name="id">The identifier of the task to update.</param>
        /// <param name="task">The task DTO containing the updated task data.</param>
        /// <returns>True if the task was updated successfully; otherwise, false.</returns>
        public async Task<bool> UpdateTask(int id, TaskRequestDto task)
        {
            var existingTask = await _context.Task.FindAsync(id);

            if (existingTask == null) return false;

            existingTask.TaskName = task.TaskName;
            existingTask.PlantId = task.PlantId;
            existingTask.AdminId = task.AdminId;
            existingTask.TaskDescription = task.TaskDescription;

            await _context.SaveChangesAsync();

            return true;
        }

        /// <summary>
        /// Deletes a task from the database.
        /// </summary>
        /// <param name="id">The identifier of the task to delete.</param>
        /// <returns>True if the task was deleted successfully; otherwise, false.</returns>
        public async Task<bool> DeleteTask(int id)
        {
            var task = await _context.Task.FindAsync(id);
            if (task == null) return false;

            _context.Task.Remove(task);
            await _context.SaveChangesAsync();

            return true;
        }
    }
}
