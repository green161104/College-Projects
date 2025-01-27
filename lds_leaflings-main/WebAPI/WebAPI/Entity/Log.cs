using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization;

namespace WebAPI.Entity
{
    /// <summary>
    /// Represents a log entry for tracking actions or changes related to a specific diary entry.
    /// </summary>
    public class Log
    {
        /// <summary>
        /// The unique identifier for the log entry.
        /// </summary>
        [Key]
        public int Id { get; set; }

        /// <summary>
        /// The foreign key referencing the Diary entity.
        /// Represents the diary entry associated with this log.
        /// </summary>
        [ForeignKey("Diary")]
        public int DiaryId { get; set; }

        /// <summary>
        /// The date and time when the log entry was created.
        /// Defaults to the current date and time when the entry is created.
        /// </summary>
        public DateTime LogDate { get; set; } = DateTime.Now;

        /// <summary>
        /// The description of the log entry, providing details about the event or action.
        /// Defaults to an empty string if not specified.
        /// </summary>
        [StringLength(500, MinimumLength = 1, ErrorMessage = " Log description is to long ")]
        [MaxLength(500)]
        [MinLength(1)]
        public String LogDescription { get; set; }

        /// <summary>
        /// The diary entity associated with this log entry.
        /// This property is ignored during JSON serialization.
        /// </summary>
        [JsonIgnore]
        public Diary Diary { get; set; }
    }
}
