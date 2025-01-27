using System.ComponentModel.DataAnnotations;

namespace WebAPI.DTO.request
{
    public class PasswordUpdateRequestDto
    {
        [Required]
        public string OldPassword { get; set; }

        [Required]
        [MinLength(6, ErrorMessage = "Password must be at least 6 characters long")]
        public string NewPassword { get; set; }
    }
}
