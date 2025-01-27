using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using WebAPI.DTO.response;
using WebAPI.Entity.enums;

namespace WebAPI.Entity
{
    public class PlantMatchResponseDTO
    {
        public List<PlantResponseDto> PerfectMatches { get; set; }
        public List<PlantResponseDto> AverageMatches { get; set; }
        public List<PlantResponseDto> WeakMatches { get; set; }
    }
}
