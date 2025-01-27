using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization;

namespace WebAPI.Entity
{

    /// <summary>
    /// Represents a generic person entity, which can be used as a base class for specific user roles such as Admin or User.
    /// </summary>
    public class Person
    {

        /// <summary>
        /// Unique identifier for the person.
        /// Serves as the primary key in the database.
        /// </summary>
        [Key]
        public int Id { get; set; }

        /// <summary>
        /// The username of the person.
        /// Validation ensures the username is between 1 and 64 characters.
        /// </summary>
        [StringLength(64, MinimumLength = 1, ErrorMessage = " Username is to long ")]
        [MaxLength(64)]
        [MinLength(1)]
        public required string Username { get; set; }

        /// <summary>
        /// The password for the person.
        /// Validation ensures the password is between 6 and 64 characters.
        /// </summary>
        [StringLength(64, MinimumLength = 1, ErrorMessage = " Password is to long ")]
        [MaxLength(64)]
        [MinLength(6,ErrorMessage = "Password must be 6 characters")]
        public required string Password { get; set; }

        /// <summary>
        /// The email address of the person.
        /// Validation ensures the email format is valid and its length is between 1 and 64 characters.
        /// </summary>
        [StringLength(64, MinimumLength = 1, ErrorMessage = " Email is to long ")]
        [MaxLength(64)]
        [EmailAddress(ErrorMessage = "Email inválido.")]
        public required string Email { get; set; }

        /// <summary>
        /// The role of the person, represented as an integer.
        /// This can be used to differentiate between different types of users (e.g., admin or regular user).
        /// </summary>
        public int Role { get; set; } 
    }

}
