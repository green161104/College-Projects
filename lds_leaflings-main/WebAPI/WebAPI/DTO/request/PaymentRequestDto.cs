namespace WebAPI.DTO.request
{
    public class PaymentRequestDto
    {
        public int UserId { get; set; }

        public string Title { get; set; } = string.Empty;

        public DateTime CreationDate { get; set; } = DateTime.Now;
    }
}
