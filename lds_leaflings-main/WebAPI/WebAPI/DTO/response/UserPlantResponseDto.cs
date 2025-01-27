namespace WebAPI.DTO.response
{
    public class UserPlantResponseDto
    {
        public int Id { get; set; }

        public int UserId { get; set; }

        public PlantResponseDto Plant { get; set; }

    }
}
