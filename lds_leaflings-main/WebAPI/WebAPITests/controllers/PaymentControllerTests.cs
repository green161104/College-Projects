using Azure;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using WebAPI.Controllers;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.DTO.response;
using WebAPI.Entity;
using WebAPI.Services;
using WebAPI.Utils;

namespace WebAPITests.controllers
{
    public class PaymentControllerTests : IDisposable
    {

        private readonly DataContext _context;
        private readonly IConfiguration _configuration;
        private readonly Cryptography _cryptography;
        private readonly AuthController _controller;
        private readonly PaymentController _paymentController;
        private readonly WebApiContext _webApiContext;
        private readonly PaymentService _paymentservice;

        public PaymentControllerTests()
        {
            _webApiContext = new WebApiContext();
            _context = _webApiContext.getWebApiContext();
            _configuration = new ConfigurationBuilder().AddJsonFile("appsettings.json").Build();
            _cryptography = new Cryptography(_configuration);
            _controller = new AuthController(_context, _configuration, _cryptography);
            _paymentservice = new PaymentService(_configuration, _context);
            _paymentController = new PaymentController(_paymentservice);
        }

        public void Dispose()
        {
            _context.Database.EnsureDeleted();
            _context.Dispose();
        }


        [Fact]
        public async void GetAllPayments_ReturnsOkResult()
        {

            // Act
            var result = await _paymentController.GetAllPayments();

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var task = Assert.IsType <List<Payment>> (okResult.Value);
        }

        [Fact]
        public async void GetPaymentById_ReturnsOkResult()
        {
            var expectPaymentID = 1;

            // Act
            var result = await _paymentController.GetPaymentById(expectPaymentID);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var payment = Assert.IsType<Payment>(okResult.Value);
        }

        [Fact]
        public async void GetPaymentById_ReturnsNotFoundResult()
        {
            var expectPaymentID = 1098;

            // Act
            var result = await _paymentController.GetPaymentById(expectPaymentID);

            // Assert
            var okResult = Assert.IsType<NotFoundObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(okResult.Value);
            Assert.Equal("Payment not found!", errorResponse.error);
        }

        [Fact]
        public async void AddPayment_ReturnOk()
        {
            PaymentRequestDto pr = new PaymentRequestDto()
            {
                UserId = 3,
                Title = "payment",
                CreationDate = DateTime.Now,
            };

            var result = await _paymentController.AddPayment(pr);

            var okResult = Assert.IsType<CreatedAtActionResult>(result.Result);
            var value = Assert.IsType<Payment>(okResult.Value);
        }


        [Fact]
        public async void AddPayment_ReturnBadRequest()
        {
           
            var result = await _paymentController.AddPayment(null);

            var okResult = Assert.IsType<BadRequestObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(okResult.Value);
            Assert.Equal("Invalid Data", errorResponse.error);
        }


        [Fact]
        public async void UpdatePayment_ReturnBadRequest()
        {

            var result = await _paymentController.UpdatePayment(1, null);

            var okResult = Assert.IsType<BadRequestObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(okResult.Value);
            Assert.Equal("Invalid Data", errorResponse.error);
        }

        [Fact]
        public async void UpdatePayment_ReturnNotFound()
        {
            PaymentRequestDto pr = new PaymentRequestDto()
            {
                UserId = 3,
                Title = "payment",
                CreationDate = DateTime.Now,
            };

            var result = await _paymentController.UpdatePayment(23312, pr);

            var okResult = Assert.IsType<NotFoundResult>(result.Result);
        }

        [Fact]
        public async void UpdatePayment_ReturnOkResult()
        {

            PaymentRequestDto pr = new PaymentRequestDto()
            {
                UserId = 3,
                Title = "payment",
                CreationDate = DateTime.Now,
            };

            var result = await _paymentController.UpdatePayment(1, pr);

            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var value = Assert.IsType<PaymentRequestDto>(okResult.Value);
        }

        [Fact]
        public async void DeletePayment_ReturnOkResult()
        {
            var result = await _paymentController.DeletePayment(1);

            var okResult = Assert.IsType<NoContentResult>(result.Result);
        }

        [Fact]
        public async void DeletePayment_ReturnNotFound()
        {
            var result = await _paymentController.DeletePayment(32131);

            var okResult = Assert.IsType<NotFoundObjectResult>(result.Result);
        }
    }
}
