using WebAPI.Entity;
using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;
using WebAPI.Entity.enums;
using System.ComponentModel.DataAnnotations.Schema;

namespace WebAPI.Entity
{
    /// <summary>
    /// Represents a plant entity with its associated properties, relationships, and constraints.
    /// </summary>
    public class Plant
    {

        /// <summary>
        /// Unique identifier for the plant.
        /// Serves as the primary key in the database.
        /// </summary>
        [Key]
        public int Id { get; set; } // Identificador único da planta

        /// <summary>
        /// Foreign key to associate the plant with an admin.
        /// Represents the admin responsible for creating or managing this plant.
        /// </summary>
        [ForeignKey("Admin")]
        public int? AdminID { get; set; } //Admin responsible for creating the plant

        /// <summary>
        /// The name of the plant.
        /// Validation ensures the name is between 1 and 64 characters.
        /// </summary>
        [StringLength(64, MinimumLength = 1, ErrorMessage = " Name is to long ")]
        [MaxLength(64)]
        [MinLength(1)]
        public required string Name { get; set; }

        /// <summary>
        /// The type of the plant.
        /// Uses the `TypesPlants` enum to define possible values (e.g., decorative, medicinal, etc.).
        /// </summary>
        public TypesPlants Type { get; set; }

        /// <summary>
        /// Suggested experience level for caring for the plant.
        /// Uses the `ExperienceLevels` enum to specify difficulty (e.g., beginner, intermediate, expert).
        /// </summary>
        public ExperienceLevels ExpSuggested { get; set; }

        /// <summary>
        /// The water needs of the plant.
        /// Uses the `WaterLevels` enum to classify requirements (e.g., low, medium, high).
        /// </summary>
        public WaterLevels WaterNeeds { get; set; }

        /// <summary>
        /// The luminosity level required by the plant.
        /// Uses the `LightLevel` enum to define light conditions (e.g., low, medium, high).
        /// </summary>
        public LightLevel LuminosityNeeded { get; set; }

        /// <summary>
        /// The Description of the plant.
        /// </summary>
        [MaxLength(4000, ErrorMessage = "The description is too long.")]
        public string Description { get; set; }

        /// <summary>
        /// The image of the plant.
        /// Validation ensures the image name is between 1 and 64 characters.
        /// Caminho ou URL da imagem
        /// </summary>
        [MaxLength(256, ErrorMessage = "The file path is too long.")]
        public string PlantImage { get; set; }

        /// <summary>
        /// The admin responsible for managing this plant.
        /// This is a navigation property for the relationship with the `Admin` entity.
        /// </summary>
        [JsonIgnore]
        public Admin? Admin { get; set; }

        /// <summary>
        /// Collection of relationships between plants and users.
        /// This represents the many-to-many relationship through the `UserPlant` entity.
        /// </summary>
        [JsonIgnore]
        public ICollection<UserPlant> UserPlants { get; set; } = new List<UserPlant>();

        /// <summary>
        /// Collection of tasks associated with the plant.
        /// Represents the tasks related to managing or caring for this plant.
        /// </summary>
        [JsonIgnore]
        public ICollection<WebAPI.Entity.PlantTask> Tasks { get; set; } = new List<WebAPI.Entity.PlantTask>(); 
    }
}
