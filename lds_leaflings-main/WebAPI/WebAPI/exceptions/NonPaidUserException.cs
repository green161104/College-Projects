namespace WebAPI.exceptions
{
    public class NonPaidUserException : Exception
    {
        public NonPaidUserException()
        {
        }

        public NonPaidUserException(string message) : base(message)
        {
        }
    }
}
