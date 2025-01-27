using WebAPI.Entity.enums;

namespace WebAPI.DTO.request
{
    public class PlantRequestDto
    {
        public int AdminID { get; set; } // Identificador do admin que criou a planta
        public string Name { get; set; } = string.Empty; // Nome da planta   
        public TypesPlants Type { get; set; } // Representa o tipo de planta
        public ExperienceLevels ExpSuggested { get; set; }
        public WaterLevels WaterNeeds { get; set; }
        public LightLevel LuminosityNeeded { get; set; }
        public string Description { get; set; }
        /// <summary>
        /// Ficheiro de imagem enviado pelo cliente
        /// </summary>
        public IFormFile? PlantImage { get; set; } = null;

    }
}
