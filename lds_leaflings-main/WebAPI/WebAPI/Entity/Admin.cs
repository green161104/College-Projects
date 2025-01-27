using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace WebAPI.Entity
{
    /// <summary>
    /// Represents an administrator in the system. Inherits common properties from the Person class.
    /// </summary>
    public class Admin : Person
    {

        /// <summary>
        /// The contact number of the administrator, which must contain exactly 9 numeric characters.
        /// The first character must be '9', followed by '1', '2', '3', or '6', and the remaining
        /// seven characters must also be numeric.
        /// </summary>
        [Required]
        [StringLength(9, MinimumLength = 9, ErrorMessage = "Contact must be exactly 9 characters.")]
        [MaxLength(9)]
        [MinLength(9)]
        [RegularExpression(@"^9[1236]\d{7}$", ErrorMessage = "Contact must start with 9, followed by 1, 2, 3, or 6, and then 7 numeric characters.")]
        public required string Contact { get; set; }

        /// <summary>
        /// A collection of plants associated with the administrator.
        /// Relationship: One administrator can be associated with many plants.
        /// </summary>
        [JsonIgnore]
        public ICollection<Plant> Plants { get; set; } = new List<Plant>(); // Inicializa a coleção

        /// <summary>
        /// A collection of tasks associated with the administrator.
        /// Relationship: One administrator can be associated with many tasks.
        /// </summary>
        [JsonIgnore]
        public ICollection<PlantTask> Tasks { get; set; } = new List<PlantTask>(); // Inicializa a coleção

        /// <summary>
        /// A collection of ads associated with the administrator.
        /// Relationship: One administrator can be associated with many ads.
        /// </summary>
        [JsonIgnore]
        public ICollection<Ad> Ads { get; set; } = new List<Ad>(); // Inicializa a coleção
        
    }
}
