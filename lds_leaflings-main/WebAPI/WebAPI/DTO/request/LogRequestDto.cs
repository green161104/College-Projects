namespace WebAPI.DTO.request
{
    public class LogRequestDto
    {
        public int DiaryId { get; set; }

        public string LogDescription { get; set; } = string.Empty;
    }
}
