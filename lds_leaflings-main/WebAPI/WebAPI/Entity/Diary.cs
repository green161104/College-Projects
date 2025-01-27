using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization;

namespace WebAPI.Entity
{
    /// <summary>
    /// Represents a diary entry for tracking information related to a specific user-plant association.
    /// </summary>
    public class Diary
    {
        /// <summary>
        /// The unique identifier for the diary entry.
        /// </summary>
        [Key]
        public int Id { get; set; }

        /// <summary>
        /// The foreign key referencing the UserPlant entity.
        /// Represents the specific user-plant relationship associated with this diary entry.
        /// </summary>
        [ForeignKey("UserPlant")]
        public int UserPlantId { get; set; }

        /// <summary>
        /// The title of the diary entry. Defaults to an empty string if not specified.
        /// </summary>
        /// 
        [StringLength(64, MinimumLength = 1, ErrorMessage = " Title to Long ")]
        [MaxLength(64)]
        [MinLength(1)]
        public String Title { get; set; }

        /// <summary>
        /// The creation date and time of the diary entry.
        /// Defaults to the current date and time when the entry is created.
        /// </summary>
        public DateTime CreationDate { get; set; } = DateTime.Now;

        /// <summary>
        /// The user-plant entity associated with this diary entry.
        /// This property is ignored during JSON serialization.
        /// </summary>
        [JsonIgnore]
        public UserPlant UserPlant { get; set; }
    }
}
