using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json.Linq;
using System.Text;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.Entity;
using WebAPI.Utils;

namespace WebAPI.Services
{
    /// <summary>
    /// Provides methods for managing payments in the database.
    /// </summary>
    public class PaymentService
    {
        private string PaypalClientId { get; set; } = "";
        private string PaypalClientSecret { get; set; } = "";
        private string PaypalUrl { get; set; } = "";

        private readonly DataContext _context;

        private readonly UserService _userService;
        private readonly Cryptography _cryptography;

        /// <summary>
        /// Initializes a new instance of the <see cref="PaymentService"/> class.
        /// </summary>
        /// <param name="context">The data context used to interact with the database.</param>
        public PaymentService(IConfiguration configuration, DataContext context)
        {
            _context = context;
            _userService  = new UserService(_context, _cryptography);
            PaypalClientId = configuration["PaypalSettings:ClientId"]!;
            PaypalClientSecret = configuration["PaypalSettings:Secret"]!;
            PaypalUrl = configuration["PaypalSettings:Url"]!;
        }

        /// <summary>
        /// Retrieves a payment by its identifier.
        /// </summary>
        /// <param name="id">The identifier of the payment.</param>
        /// <returns>The payment with the specified identifier.</returns>
        public async Task<Payment?> GetPaymentById(int id)
        {
            return await _context.Payment.SingleOrDefaultAsync(p => p.Id == id);
        }

        /// <summary>
        /// Retrieves all payments from the database.
        /// </summary>
        /// <returns>A list of all payments.</returns>
        public async Task<List<Payment>> GetAllPaymentsAsync()
        {
            return await _context.Payment
                .Include(up => up.User)
                .ToListAsync();
        }

        /// <summary>
        /// Retrieves all payments associated with a specific user identifier.
        /// </summary>
        /// <param name="userId">The identifier of the user.</param>
        /// <returns>A list of payments associated with the specified user.</returns>
        public async Task<List<Payment>> GetPaymentsByUserId(int userId)
        {
            return await _context.Payment
                .Include(up => up.User)
                .Where(up => up.User.Id == userId)
                .ToListAsync();
        }

        /// <summary>
        /// Adds a new payment to the database.
        /// </summary>
        /// <param name="payment">The data transfer object containing payment details.</param>
        /// <returns>The created payment.</returns>
        public async Task<Payment> AddNewPayment(PaymentRequestDto payment)
        {

            if( payment.UserId < 1 ||_userService.GetUserByIdAsync(payment.UserId) == null)
            {
                return null;
            }

            var newPayment = new Payment
            {
                Title = payment.Title,
                UserId = payment.UserId,
                CreationDate = payment.CreationDate,
            };
            _context.Payment.Add(newPayment);
            await _context.SaveChangesAsync();
            return newPayment;
        }

        /// <summary>
        /// Updates an existing payment in the database.
        /// </summary>
        /// <param name="id">The identifier of the payment to update.</param>
        /// <param name="payment">The data transfer object containing updated payment details.</param>
        /// <returns>True if the update was successful; otherwise, false.</returns>
        public async Task<bool> UpdatePayment(int id, PaymentRequestDto payment)
        {
            var existingPayment = await _context.Payment.FindAsync(id);

            if (existingPayment == null) return false;

            existingPayment.Title = payment.Title;
            existingPayment.UserId = payment.UserId;
            existingPayment.CreationDate = payment.CreationDate;

            await _context.SaveChangesAsync();

            return true;
        }

        /// <summary>
        /// Deletes a payment from the database.
        /// </summary>
        /// <param name="id">The identifier of the payment to delete.</param>
        /// <returns>True if the deletion was successful; otherwise, false.</returns>
        public async Task<bool> DeletePayment(int id)
        {
            var payment = await _context.Payment.FindAsync(id);
            if (payment == null) return false;

            _context.Payment.Remove(payment);
            await _context.SaveChangesAsync();

            return true;
        }


        public async Task<string> GetPaypalAccessToken()
        {
            string url = PaypalUrl + "/v1/oauth2/token";
            using (var client = new HttpClient())
            {
                string credentials64 =
                    Convert.ToBase64String(Encoding.UTF8.GetBytes($"{PaypalClientId}:{PaypalClientSecret}"));

                client.DefaultRequestHeaders.Add("Authorization", "Basic " + credentials64);

                var requestMessage = new HttpRequestMessage(HttpMethod.Post, url);
                requestMessage.Content = new StringContent("grant_type=client_credentials", Encoding.UTF8,
                    "application/x-www-form-urlencoded");

                var httpResponse = await client.SendAsync(requestMessage);
                if (!httpResponse.IsSuccessStatusCode)
                {
                    return null;
                }

                var responseContent = await httpResponse.Content.ReadAsStringAsync();
                var tokenObj = JObject.Parse(responseContent);
                string accessToken = tokenObj["access_token"].ToString();

                return accessToken;
            }
        }

        public string getPaypalUrl()
        {
            return PaypalUrl;
        }
    }

}
