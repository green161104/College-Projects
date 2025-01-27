using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Net;
using WebAPI.DTO.request;
using WebAPI.DTO.response;
using WebAPI.Entity;
using WebAPI.Services;

namespace WebAPI.Controllers
{
    /// <summary>
    /// Controller responsible for handling payment-related operations.
    /// </summary>
    [Route("api/[controller]")]
    [ApiController]
    public class PaymentController : ControllerBase
    {
        private readonly PaymentService _paymentService;

        /// <summary>
        /// Initializes a new instance of the <see cref="PaymentController"/> class.
        /// </summary>
        /// <param name="paymentService">Service for payment-related operations.</param>
        public PaymentController(PaymentService paymentService)
        {
            _paymentService = paymentService;
        }

        /// <summary>
        /// Retrieves all payments.
        /// </summary>
        /// <returns>A list of all payments.</returns>
        [HttpGet]
        [Authorize]
        public async Task<ActionResult<List<Payment>>> GetAllPayments()
        {
            try
            {
                var payments = await _paymentService.GetAllPaymentsAsync();

              //  Response.Headers.Append("Access-Control-Expose-Headers", "Content-Range");
                // Response.Headers.Append("Content-Range", $"payments 0-{payments.Count}/{payments.Count}");
                return Ok(payments);
                if (payments == null || payments.Count == 0)
                    return NoContent();


                return Ok(new
                {
                    data = payments,
                    total = payments.Count
                });
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{ex.Message}"
                });
            }
        }

        /// <summary>
        /// Retrieves a specific payment by ID.
        /// </summary>
        /// <param name="id">The payment ID.</param>
        /// <returns>The payment with the specified ID.</returns>
        [HttpGet("{id}")]
        [Authorize]
        public async Task<ActionResult<Payment>> GetPaymentById(int id)
        {
            try
            {
                var payment = await _paymentService.GetPaymentById(id);
                if (payment == null) return NotFound(new ErrorResponseDto { error = "Payment not found!"});

                return Ok(payment);
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{ex.Message}"
                });
            }
        }

        /// <summary>
        /// Retrieves all payments made by a specific user.
        /// </summary>
        /// <param name="id">The user ID.</param>
        /// <returns>A list of payments made by the specified user.</returns>
        [HttpGet("user/{id}")]
        [Authorize]
        public async Task<ActionResult<List<Payment>>> GetPaymentsByUserId(int id)
        {
            try
            {
                var payments = await _paymentService.GetPaymentsByUserId(id);

                return Ok(new
                {
                    data = payments,
                    total = payments.Count
                });
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{ex.Message}"
                });
            }
        }

        /// <summary>
        /// Adds a new payment.
        /// </summary>
        /// <param name="paymentDto">The payment data transfer object.</param>
        /// <returns>The created payment.</returns>
        [HttpPost]
        [Authorize]
        public async Task<ActionResult<Payment>> AddPayment(PaymentRequestDto paymentDto)
        {
            if (paymentDto == null) return BadRequest(new ErrorResponseDto { error = "Invalid Data"});

            try
            {
                var createdPayment = await _paymentService.AddNewPayment(paymentDto);
                if (createdPayment == null) return BadRequest(new ErrorResponseDto { error = "Invalid Data"});

                return CreatedAtAction(nameof(GetPaymentById), new { id = createdPayment.Id }, createdPayment);
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{ex.Message}"
                });
            }
        }

        /// <summary>
        /// Updates an existing payment.
        /// </summary>
        /// <param name="id">The payment ID.</param>
        /// <param name="paymentDto">The payment data transfer object.</param>
        /// <returns>The updated payment.</returns>
        [HttpPut("{id}")]
        [Authorize]
        public async Task<ActionResult<Payment>> UpdatePayment(int id, PaymentRequestDto paymentDto)
        {
            if (paymentDto == null) return BadRequest(new ErrorResponseDto { error = "Invalid Data" });

            try
            {
                var updatedPayment = await _paymentService.UpdatePayment(id, paymentDto);
                if (!updatedPayment) return NotFound();

                return Ok(paymentDto);
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{ex.Message}"
                });
            }
        }

        /// <summary>
        /// Deletes an existing payment by ID.
        /// </summary>
        /// <param name="id">The payment ID.</param>
        /// <returns>No content if deletion is successful.</returns>
        [HttpDelete("{id}")]
        [Authorize]
        public async Task<ActionResult<bool>> DeletePayment(int id)
        {
            try
            {
                var successfulDelete = await _paymentService.DeletePayment(id);
                if (!successfulDelete) return NotFound(new ErrorResponseDto { error = "Payment not found!" });

                return NoContent();
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{ex.Message}"
                });
            }
        }


    }
}
