using System.ComponentModel.DataAnnotations;
using WebAPI.Entity;

namespace WebAPI.DTO.request
{
    public class TaskRequestDto
    {
        public int AdminId { get; set; }

        public int PlantId { get; set; }

        public required string TaskName { get; set; }

        public required string TaskDescription { get; set; }
    }
}
