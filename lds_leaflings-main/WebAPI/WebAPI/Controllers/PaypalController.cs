using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json.Linq;
using System.Text;
using System.Text.Json.Nodes;
using WebAPI.Data;
using WebAPI.DTO;
using WebAPI.DTO.request;
using WebAPI.DTO.response;
using WebAPI.Entity;
using WebAPI.Services;

namespace WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PaypalController : ControllerBase
    {
        DataContext _dbContext;
        PaymentService _paymentService;


        public PaypalController(DataContext context, PaymentService service)
        {
            _dbContext = context;
            _paymentService = service;

        }


        [HttpPost("create-order")]
        public async Task<ActionResult<CreateOrderResponse>> CreateOrder([FromBody] CreateOrderRequest request)
        {
            var totalAmount = request.Amount.ToString();
            if (totalAmount == null)
            {
                return new JsonResult(new { Id = "" });
            }

            JsonObject createOrderRequest = new JsonObject();
            createOrderRequest.Add("intent", "CAPTURE");

            JsonObject amount = new JsonObject();
            amount.Add("currency_code", "EUR");
            amount.Add("value", totalAmount);

            JsonObject purchaseUnit = new JsonObject();
            purchaseUnit.Add("amount", amount);

            JsonArray purchaseUnits = new JsonArray();
            purchaseUnits.Add(purchaseUnit);

            createOrderRequest.Add("purchase_units", purchaseUnits);

            string token = await _paymentService.GetPaypalAccessToken();
            string url = _paymentService.getPaypalUrl() + "/v2/checkout/orders";

            using (var client = new HttpClient())
            {
                client.DefaultRequestHeaders.Add("Authorization", "Bearer " + token);

                var requestMessage = new HttpRequestMessage(HttpMethod.Post, url);
                requestMessage.Content = new StringContent(createOrderRequest.ToString(), null, "application/json");

                var httpResponse = await client.SendAsync(requestMessage);

                if (httpResponse.IsSuccessStatusCode)
                {
                    var strResponse = await httpResponse.Content.ReadAsStringAsync();
                    var jsonResponse = JsonNode.Parse(strResponse);

                    if (jsonResponse != null)
                    {
                        string paypalOrderId = jsonResponse["id"]?.ToString() ?? "";

                        return new JsonResult(new { Id = paypalOrderId });
                    }
                }
            }


            return new JsonResult("error");
        }



        [HttpPost("complete-order")]
        public async Task<IActionResult> CompleteOrder([FromBody] CompleteOrderRequest data)
        {
            var orderId = data.OrderID;
            var userId = data.UserID;

            if (string.IsNullOrEmpty(orderId) || userId == null)
            {
                return BadRequest(new ErrorResponseDto { error = "Invalid data" });
            }

            // Step 1: Get PayPal token
            string token = await _paymentService.GetPaypalAccessToken();
            if (string.IsNullOrEmpty(token))
            {
                return StatusCode(StatusCodes.Status500InternalServerError, "Could not retrieve PayPal access token.");
            }

            string url = _paymentService.getPaypalUrl() + "/v2/checkout/orders/" + orderId + "/capture";

            using (var client = new HttpClient())
            {
                client.DefaultRequestHeaders.Add("Authorization", "Bearer " + token);

                var requestMessage = new HttpRequestMessage(HttpMethod.Post, url);
                requestMessage.Content = new StringContent("", null, "application/json"); // Empty payload

                var httpResponse = await client.SendAsync(requestMessage);

                if (!httpResponse.IsSuccessStatusCode)
                {
                    var errorResponse = await httpResponse.Content.ReadAsStringAsync();
                    return StatusCode((int)httpResponse.StatusCode, $"Payment capture failed: {errorResponse}");
                }

                var strResponse = await httpResponse.Content.ReadAsStringAsync();
                var jsonResponse = JsonNode.Parse(strResponse);

                if (jsonResponse != null && jsonResponse["status"]?.ToString() == "COMPLETED")
                {
                    // Update User rolePaid and Save Payment in DB
                    var user = await _dbContext.User.FindAsync(userId);
                    if (user == null)
                    {
                        return NotFound(new ErrorResponseDto { error = "User not found" });
                    }

                    // Update user rolePaid attribute
                    user.RolePaid = true;

                    // Create and save payment record
                    Payment payment = await _paymentService.AddNewPayment(new PaymentRequestDto
                    {
                        UserId = userId,
                        Title = "PayPal Payment",
                        CreationDate = DateTime.Now
                    });

                    await _dbContext.SaveChangesAsync();

                    return Ok(new ErrorResponseDto { error = "Payment successful, rolePaid updated." });
                }
            }

            return BadRequest(new ErrorResponseDto { error = "Error completing payment." });
        }

    }
}

    public class CreateOrderRequest
{
    public decimal Amount { get; set; }
}

public class CompleteOrderRequest
{
    public int UserID { get; set; }
    public string OrderID { get; set; }
}

public class CreateOrderResponse
{
    public string Id { get; set; }
}



