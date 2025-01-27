using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization;

namespace WebAPI.Entity
{
    /// <summary>
    /// Represents a payment entity, which tracks user-related payment details.
    /// </summary>
    public class Payment
    {

        /// <summary>
        /// Unique identifier for the payment record.
        /// This serves as the primary key in the database.
        /// </summary>
        [Key]
        public int Id { get; set; }

        /// <summary>
        /// The ID of the associated user.
        /// Establishes a foreign key relationship with the User entity.
        /// </summary>
        [ForeignKey("User")]
        public int UserId { get; set; }

        /// <summary>
        /// The title or description of the payment.
        /// Validation ensures it has a length between 1 and 64 characters.
        /// </summary>
        [StringLength(64, MinimumLength = 1, ErrorMessage = " Title to Long ")]
        [MaxLength(64)]
        [MinLength(1)]
        public String Title { get; set; } = String.Empty;

        /// <summary>
        /// The date and time when the payment was created.
        /// Defaults to the current date and time upon creation.
        /// </summary>
        public DateTime CreationDate { get; set; } = DateTime.Now;

        /// <summary>
        /// The associated user entity for this payment.
        /// This property is ignored during JSON serialization to prevent circular references.
        /// </summary>
        [JsonIgnore]
        public User User { get; set; }
    }
}
