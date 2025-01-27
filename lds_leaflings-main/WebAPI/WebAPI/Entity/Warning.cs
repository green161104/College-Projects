using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization;

namespace WebAPI.Entity
{
    /// <summary>
    /// Represents a warning or reminder entity associated with a specific user.
    /// </summary>
    public class Warning
    {
        /// <summary>
        /// Primary key for the `Warning` entity.
        /// Unique identifier for each warning record.
        /// </summary>
        [Key]
        public int Id { get; set; }

        /// <summary>
        /// Foreign key referencing the `User` entity.
        /// Links the warning to the user it applies to.
        /// </summary>
        [ForeignKey("User")]
        public int UserId { get; set; }

        /// <summary>
        /// Location associated with the warning (e.g., geographic or environmental context).
        /// Required, with a maximum length of 64 characters.
        /// </summary>
        [StringLength(64, MinimumLength = 1, ErrorMessage = " Location is to long ")]
        [MaxLength(64)]
        [MinLength(1)]
        public required string Location { get; set; }

        /// <summary>
        /// The message or content of the warning.
        /// Provides the user with relevant details about the warning.
        /// Required, with a maximum length of 255 characters.
        /// </summary>
        [StringLength(255, MinimumLength = 1, ErrorMessage = " Message is to long ")]
        [MaxLength(255)]
        [MinLength(1)]
        public required string Message { get; set; }

        /// <summary>
        /// The date and time when the warning should be triggered or is relevant.
        /// Used to manage reminders or notifications.
        /// </summary>
        public DateTime ReminderDate { get; set; }

        /// <summary>
        /// Navigation property for the associated `User` entity.
        /// Marked with `JsonIgnore` to prevent serialization in API responses, avoiding circular references.
        /// </summary>
        [JsonIgnore]
        public User User { get; set; }
    }
}
