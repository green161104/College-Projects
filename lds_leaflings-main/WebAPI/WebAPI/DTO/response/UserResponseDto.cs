using System.ComponentModel.DataAnnotations;
using WebAPI.Entity.enums;

namespace WebAPI.DTO.request
{
    public class UserResponseDto
    {
        public required int Id { get; set; }
        public required string Username { get; set; }
        public required string Email { get; set; }
        public required string Contact { get; set; }
        public bool RolePaid { get; set; }
        public required string Location { get; set; }
        public ExperienceLevels CareExperience { get; set; }
        public WaterLevels WaterAvailability { get; set; }
        public LightLevel LuminosityAvailability { get; set; }
        public string? UserAvatar { get; set; } 

    }
}
