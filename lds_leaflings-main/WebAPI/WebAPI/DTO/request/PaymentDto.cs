namespace WebAPI.DTO.request
{
    internal class PaymentDto : PaymentRequestDto
    {
        public int UserId { get; set; }
        public string Title { get; set; }
        public DateTime CreationDate { get; set; }
    }
}