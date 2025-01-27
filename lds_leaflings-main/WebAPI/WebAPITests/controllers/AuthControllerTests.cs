using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Testing;
using Microsoft.Extensions.Configuration;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using WebAPI.Controllers;
using WebAPI.Data;
using WebAPI.DTO.response;
using WebAPI.Entity;
using WebAPI.Utils;

namespace WebAPITests.controllers
{
    public class AuthControllerTests : IDisposable
    {
        private readonly DataContext _context;
        private readonly IConfiguration _configuration;
        private readonly Cryptography _cryptography;
        private readonly AuthController _controller;
        private readonly WebApiContext _webApiContext;

        public AuthControllerTests()
        {
            _webApiContext = new WebApiContext();
            _context = _webApiContext.getWebApiContext();
            _configuration = new ConfigurationBuilder().AddJsonFile("appsettings.json").Build();
            _cryptography = new Cryptography(_configuration);
            _controller = new AuthController(_context, _configuration, _cryptography);
        }

        public void Dispose()
        {
            _context.Database.EnsureDeleted();
            _context.Dispose();
        }

        [Fact]
        public async void Login_ValidCredentialsForAdmin_ReturnsOkResult()
        {
            // Arrange
            var loginModel = new LoginModel
            {
                Email = "admin@admin.com",
                Password = "adminpassword"
            };

            // Act
            var result = await _controller.Login(loginModel);

            // Assert
            Assert.IsType<OkObjectResult>(result);
        }

        [Fact]
        public async void Login_InvalidCredentialsForAdmin_ReturnsUnauthorized()
        {
            // Arrange
            var loginModel = new LoginModel
            {
                Email = "wrong@email.com",
                Password = "wrongpassword"
            };

            // Act
            var result = await _controller.Login(loginModel);

            // Assert
            var unauthorizedResult = Assert.IsType<UnauthorizedObjectResult>(result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(unauthorizedResult.Value);
            Assert.Equal("Invalid Credentials", errorResponse.error);
        }

        [Fact]
        public async void Login_NullModel_ReturnsBadRequest()
        {
            // Act
            var result = await _controller.Login(null);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(badRequestResult.Value);
            Assert.Equal("Email and Password not provided", errorResponse.error);
        }

        [Fact]
        public async void Login_EmptyCredentials_ReturnsBadRequest()
        {
            // Arrange
            var loginModel = new LoginModel
            {
                Email = "",
                Password = ""
            };

            // Act
            var result = await _controller.Login(loginModel);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(badRequestResult.Value);
            Assert.Equal("Email and Password not provided", errorResponse.error);
        }

        [Theory]
        [InlineData("admin@admin.com", "")]
        [InlineData("", "admin")]
        public async void Login_PartialCredentials_ReturnsBadRequest(string email, string password)
        {
            // Arrange
            var loginModel = new LoginModel
            {
                Email = email,
                Password = password
            };

            // Act
            var result = await _controller.Login(loginModel);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(badRequestResult.Value);
            Assert.Equal("Email and Password not provided", errorResponse.error);
        }

        // Helper class to match the anonymous type returned by the controller
        private class Anonymous
        {
            public bool auth { get; set; }
            public string? Token { get; set; }
            public DateTime Expiration { get; set; }
        }
    }
}
