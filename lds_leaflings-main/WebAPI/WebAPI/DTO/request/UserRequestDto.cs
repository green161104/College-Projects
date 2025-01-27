using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;
using WebAPI.Entity.enums;

namespace WebAPI.DTO.request
{
    public class UserRequestDto
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

        [Required]
        public required string Location { get; set; }

        [Required]
        public ExperienceLevels CareExperience { get; set; }

        [Required]
        public WaterLevels WaterAvailability { get; set; }

        [Required]
        public LightLevel LuminosityAvailability { get; set; }

        /// <summary>
        /// Ficheiro de imagem enviado pelo cliente
        /// </summary>
        public IFormFile? UserAvatar { get; set; }

    }
}
