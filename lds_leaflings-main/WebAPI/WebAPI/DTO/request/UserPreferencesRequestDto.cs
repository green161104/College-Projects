using System.ComponentModel.DataAnnotations;
using WebAPI.Entity.enums;

namespace WebAPI.DTO.request
{
    public class UserPreferencesRequestDto
    {
        public ExperienceLevels CareExperience { get; set; }

        public WaterLevels WaterAvailability { get; set; }

        public LightLevel LuminosityAvailability { get; set; }
    }
}
