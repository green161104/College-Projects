using System.ComponentModel.DataAnnotations;

namespace WebAPI.Entity
{
    /// <summary>
    /// Represents the data model for user login credentials.
    /// Used to capture and validate email and password input.
    /// </summary>
    public class LoginModel
    {
        /// <summary>
        /// The email address of the user attempting to log in.
        /// Validation ensures that the email is required, properly formatted, 
        /// and within the specified length constraints (1 to 64 characters).
        /// </summary>
        [Required(ErrorMessage = "Email é obrigatório.")]
        [EmailAddress(ErrorMessage = "Email inválido.")]
        [StringLength(64, MinimumLength = 1, ErrorMessage = " Email is to long ")]
        [MaxLength(64)]
        [MinLength(1)]
        public required string Email { get; set; }

        /// <summary>
        /// The password of the user attempting to log in.
        /// Validation ensures that the password is required, has a minimum length of 6 characters, 
        /// and does not exceed the maximum length of 64 characters.
        /// </summary>
        [Required(ErrorMessage = "Password é obrigatória.")]
        [MinLength(6, ErrorMessage = "A Password deve ter pelo menos 6 caracteres.")]
        [StringLength(64, MinimumLength = 1, ErrorMessage = " Password is to long ")]
        [MaxLength(64)]
        public required string Password { get; set; }
    }
}
