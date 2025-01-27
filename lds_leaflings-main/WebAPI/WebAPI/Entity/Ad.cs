using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization;

namespace WebAPI.Entity
{
    /// <summary>
    /// Represents an advertisement in the system.
    /// </summary>
    public class Ad
    {

        /// <summary>
        /// The unique identifier for the advertisement.
        /// </summary>
        [Key]
        public int Id { get; set; }

        /// <summary>
        /// The foreign key referencing the Admin entity.
        /// Represents the administrator who created the advertisement.
        /// </summary>
        [ForeignKey("Admin")]
        public int AdminID { get; set; }

        /// <summary>
        /// Indicates whether the advertisement is currently active.
        /// </summary>
        public bool isActive { get; set; }

        /// <summary>
        /// The start date of the advertisement's visibility.
        /// </summary>
        public DateTime StartDate { get; set; }

        /// <summary>
        /// The end date of the advertisement's visibility.
        /// </summary>
        public DateTime EndDate { get; set; }

        /// <summary>
        /// The file name or path associated with the advertisement image.
        /// Must be between 1 and 64 characters.
        /// </summary>
        [MaxLength(256, ErrorMessage = "The file path is too long.")]
        public String AdFile { get; set; }

        /// <summary>
        /// The administrator entity associated with this advertisement.
        /// This property is ignored during JSON serialization.
        /// </summary>
        [JsonIgnore]
        public Admin Admin { get; set; }
    }
}
