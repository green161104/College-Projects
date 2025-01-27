using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace WebAPI.DTO.response
{
    public class DiaryResponseDto
    {
        public int Id { get; set; }
        public int UserPlantId { get; set; }
        public String Title { get; set; }
        public String CreationDate { get; set; }
    }
}
