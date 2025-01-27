using WebAPI.Entity.enums;

namespace WebAPI.DTO.response
{
    public class PlantResponseDto
    {
        public required int Id { get; set; }    
        public required string Name { get; set; }
        public required TypesPlants Type { get; set; }
        public ExperienceLevels ExpSuggested { get; set; }
        public WaterLevels WaterNeeds { get; set; }
        public LightLevel LuminosityNeeded { get; set; }
        public  string Description { get; set; }
        public  string PlantImage { get; set; }
    }
}
