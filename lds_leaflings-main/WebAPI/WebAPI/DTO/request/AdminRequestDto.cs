using System.ComponentModel.DataAnnotations;

namespace WebAPI.DTO.request
{
    public class AdminRequestDto
    {

        [Required]
        public required string Username { get; set; }

        [Required]
        [EmailAddress(ErrorMessage = "Invalid email format")]
        public required string Email { get; set; }

        [Required]
        [MinLength(8, ErrorMessage = "Password must be at least 6 characters long")]
        public required string Password { get; set; }

        [Required]
        public required string Contact { get; set; }
    }
}
