using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;
using WebAPI.Entity.enums;

namespace WebAPI.Entity
{
    /// <summary>
    /// Represents a user in the system, inheriting common properties from the `Person` class.
    /// Includes additional details specific to user roles, preferences, and relationships with plants.
    /// </summary>
    public class User : Person
    {
        /// <summary>
        /// The contact number of the user.
        /// Validation ensures the number is exactly 9 characters and follows a specific pattern:
        /// - Starts with '9'.
        /// - Second digit is 1, 2, 3, or 6.
        /// - Followed by 7 numeric characters.
        /// </summary>
        [Required]
        [StringLength(9, MinimumLength = 9, ErrorMessage = "Contact must be exactly 9 characters.")]
        [MaxLength(9)]
        [MinLength(9)]
        [RegularExpression(@"^9[1236]\d{7}$", ErrorMessage = "Contact must start with 9, followed by 1, 2, 3, or 6, and then 7 numeric characters.")]
        public required string Contact { get; set; }

        /// <summary>
        /// Indicates whether the user has a paid subscription or access level.
        /// </summary>
        public bool RolePaid { get; set; }

        /// <summary>
        /// The geographic location of the user.
        /// This field is mandatory.
        /// </summary>
        [StringLength(64, MinimumLength = 1, ErrorMessage = " Location is to long ")]
        [MaxLength(64)]
        [MinLength(1)]
        public required string Location { get; set; }

        /// <summary>
        /// The level of care experience the user has with plants.
        /// Uses the `ExperienceLevels` enum to define possible values.
        /// </summary>
        public ExperienceLevels CareExperience { get; set; }

        /// <summary>
        /// Indicates the user's availability to provide water to plants.
        /// Uses the `WaterLevels` enum to define possible values.
        /// </summary>
        public WaterLevels WaterAvailability { get; set; }

        /// <summary>
        /// Indicates the level of luminosity available in the user's environment.
        /// Uses the `LightLevel` enum to define possible values.
        /// </summary>
        public LightLevel LuminosityAvailability { get; set; }

        /// <summary>
        /// The image of the plant.
        /// Validation ensures the image name is between 1 and 64 characters.
        /// Caminho ou URL da imagem
        /// </summary>
        [MaxLength(256, ErrorMessage = "The file path is too long.")]
        public string UserAvatar { get; set; }

        /// <summary>
        /// Many-to-many relationship with `Plant` through `UserPlant`.
        /// This property is ignored in JSON serialization to prevent circular references.
        /// </summary>
        [JsonIgnore]
        public ICollection<UserPlant> UserPlants { get; set; } = new List<UserPlant>(); 
    }
}
