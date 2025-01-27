using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using WebAPI.Controllers;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.Services;

namespace WebAPITests.controllers
{
    public class WarningControllerTests
    {
        private readonly DataContext _context;
        private readonly WarningService _warningService;
        private readonly WarningController _warningController;

        public WarningControllerTests()
        {
            var webApiContext = new WebApiContext();
            _context = webApiContext.getWebApiContext();
            _warningService = new WarningService(_context);
            _warningController = new WarningController(_warningService);
        }

        [Fact]
        public async void Warning_GetAllWarningsFromUser_ReturnsList()
        {
            // Arrange
            var userId = 4;
            var expectedWarningsCount = 2;

            // Act
            var result = await _warningController.GetUserWarnings(userId);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var data = okResult.Value as dynamic;

            Assert.NotNull(data);
            Assert.Equal(expectedWarningsCount, data.total);
        }
        [Fact]
        public async void Warning_GetAllWarningsFromUser_ReturnsEmptyListWhenUserDontExisst()
        {
            // Arrange
            var userId = 99;

            // Act
            var result = await _warningController.GetUserWarnings(userId);

            // Assert
            var okResult = Assert.IsType<NoContentResult>(result.Result);
        }

        [Fact]
        public async void Warning_CreateWarning_ReturnsWarning()
        {
            // Arrange
            var warning = new WarningRequestDto
            {
                UserId = 1,
                Location = "Test Location",
                Message = "Test Message"
            };

            // Act
            var result = await _warningController.CreateWarning(warning);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var data = okResult.Value as dynamic;

            Assert.NotNull(data);
            Assert.Equal(warning.UserId, data.UserId);
            Assert.Equal(warning.Location, data.Location);
            Assert.Equal(warning.Message, data.Message);
        }

        [Fact]
        public async void Warning_CreateWarning_ReturnsBadRequest()
        {
            // Act
            var result = await _warningController.CreateWarning(null);

            // Assert
            Assert.IsType<BadRequestObjectResult>(result.Result);
        }
    }
}
