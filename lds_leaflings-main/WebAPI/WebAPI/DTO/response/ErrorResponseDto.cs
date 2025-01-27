namespace WebAPI.DTO.response
{
    public class ErrorResponseDto
    {
        public required string error { get; set; }
        public string? details { get; set; }
    }
}
