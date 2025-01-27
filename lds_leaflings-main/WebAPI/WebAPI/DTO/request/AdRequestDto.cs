namespace WebAPI.DTO.request
{
    public class AdRequestDto
    {
        public int AdminId { get; set; }
        public bool isActive { get; set; }
        public DateTime StartDate { get; set; }
        public DateTime EndDate { get; set; }

        /// <summary>
        /// Ficheiro de imagem enviado pelo cliente
        /// </summary>
        public IFormFile? AdFile { get; set; }
    }
}
