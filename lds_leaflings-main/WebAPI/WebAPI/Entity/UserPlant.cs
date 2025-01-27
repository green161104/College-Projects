using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;
using WebAPI.Entity;

namespace WebAPI.Entity
{
    /// <summary>
    /// Represents the many-to-many relationship between users and plants.
    /// Each instance links a specific user (`User`) with a specific plant (`Plant`).
    /// </summary>
    public class UserPlant
    {
        /// <summary>
        /// Primary key for the `UserPlant` entity.
        /// While this is designed as a composite entity, the `Id` simplifies database management.
        /// </summary>
        [Key]
        public int Id { get; set; }

        /// <summary>
        /// Foreign key referencing the `Plant` entity.
        /// Represents the plant associated with this relationship.
        /// </summary>
        [ForeignKey("Plant")]
        public int PlantID { get; set; }

        /// <summary>
        /// Foreign key referencing the `User` entity (inherits from `Person`).
        /// Represents the user associated with this relationship.
        /// </summary>
        [ForeignKey("User")]
        public int PersonID { get; set; } // Identificador do Utilizador

        /// <summary>
        /// Navigation property for the `Plant` entity.
        /// Marked with `JsonIgnore` to prevent serialization in API responses, avoiding circular references.
        /// </summary>
        [JsonIgnore]  
        public Plant Plant { get; set; }

        /// <summary>
        /// Navigation property for the `User` entity.
        /// Marked with `JsonIgnore` to prevent serialization in API responses, avoiding circular references.
        /// </summary>
        [JsonIgnore]
        public User User { get; set; }
    }

}
