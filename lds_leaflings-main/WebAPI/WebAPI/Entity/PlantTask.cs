using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization;

namespace WebAPI.Entity
{
    /// <summary>
    /// Represents a task associated with a plant, optionally created by an admin.
    /// </summary>
    public class PlantTask
    {
        /// <summary>
        /// Unique identifier for the task.
        /// Serves as the primary key in the database.
        /// </summary>
        [Key]
        public int Id { get; set; }

        /// <summary>
        /// Foreign key linking the task to an admin, if applicable.
        /// Represents the admin who created or manages this task.
        /// </summary>
        [ForeignKey("Admin")]
        public int? AdminId { get; set; }

        /// <summary>
        /// Foreign key linking the task to a specific plant.
        /// Indicates the plant this task is associated with.
        /// </summary>
        [ForeignKey("Plant")]
        public int PlantId { get; set; }

        /// <summary>
        /// The name of the task.
        /// Validation ensures the task name is between 1 and 64 characters.
        /// </summary>
        [StringLength(48, MinimumLength = 1, ErrorMessage = " Task Name is to long ")]
        [MaxLength(48)]
        [MinLength(1)]
        public required string TaskName { get; set; }

        /// <summary>
        /// A brief description of the task.
        /// Validation ensures the description is between 1 and 64 characters.
        /// </summary>
        [StringLength(96, MinimumLength = 1, ErrorMessage = " Task Description is to long ")]
        [MaxLength(96)]
        [MinLength(1)]
        public required string TaskDescription { get; set; }

        /// <summary>
        /// The admin who created or is responsible for this task.
        /// This is a navigation property and is excluded from JSON serialization.
        /// </summary>
        [JsonIgnore]
        public virtual Admin? Admin { get; set; }

        /// <summary>
        /// The plant this task is associated with.
        /// This is a navigation property and is excluded from JSON serialization.
        /// </summary>
        [JsonIgnore]
        public virtual Plant Plant { get; set; }
    }
}
