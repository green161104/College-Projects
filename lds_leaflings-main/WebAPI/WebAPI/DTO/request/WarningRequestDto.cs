namespace WebAPI.DTO.request
{
    public class WarningRequestDto
    {
        public int UserId { get; set; }

        public required string Location { get; set; }

        public required string Message { get; set; }

        public DateTime ReminderDate { get; set; }
    }
}
