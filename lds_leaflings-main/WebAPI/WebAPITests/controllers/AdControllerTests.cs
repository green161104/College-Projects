using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Runtime.CompilerServices;
using WebAPI.Controllers;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.DTO.response;
using WebAPI.Entity;
using WebAPI.Services;

namespace WebAPITests.controllers
{
    public class AdControllerTests : IDisposable
    {
        private readonly WebApiContext _webApiContext;
        private readonly DataContext _context;
        private readonly AdService _adService;
        private readonly AdController _controller;

        /// <summary>
        /// Assigns the context and configuration to the AdControllerTests class
        /// </summary>
        public AdControllerTests()
        {
            // Initialize in-memory database and services
            _webApiContext = new WebApiContext();
            _context = _webApiContext.getWebApiContext();
            _adService = new AdService(_context);
            _controller = new AdController(_adService);
        }

        /// <summary>
        /// Dispose method to delete the database after the tests are run
        /// </summary>
        public void Dispose()
        {
            // Clean up the in-memory database
            _context.Database.EnsureDeleted();
            _context.Dispose();
        }

        /// <summary>
        /// Test that verifies if the `GetAllAds` method returns an OK result with a list of ads
        /// </summary>
        [Fact]
        public async void GetAllAds_ReturnsOkResult_WithListOfAds()
        {
            // Act
            var result = await _controller.GetAllAds();

            // Assert
            Assert.IsType<ActionResult<List<Ad>>>(result);

        }

        /// <summary>
        /// Test that verifies if the `GetAdById` method returns an OK result with the expected ad
        /// </summary>
        [Fact]
        public async void GetAdById_ReturnsOkResult_WithAd()
        {
            var expectedAdId = 1;

            // Act
            var result = await _controller.GetAdById(expectedAdId);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var ad = Assert.IsType<Ad>(okResult.Value);
            Assert.Equal(1, ad.Id);
        }

        /// <summary>
        /// Test that verifies if the `GetAdById` method returns a NotFound result with the expected message when the ad is not found
        /// </summary>
        [Fact]
        public async void GetAdById_ReturnsNotFound_WithMessage()
        {
            var expectedAdId = 100;

            // Act
            var result = await _controller.GetAdById(expectedAdId);

            // Assert
            var notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(notFoundResult.Value);
            Assert.Equal("Ad not found", errorResponse.error);
        }

        /// <summary>
        /// Test that verifies if the `GetAdStartDate` method returns an OK result with the expected start date
        /// </summary>
        [Fact]
        public async void GetAdStartDate_ReturnsOkResult_WithStartDate()
        {
            var expectedAdId = 1;

            // Act
            var result = await _controller.GetAdStartDate(expectedAdId);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var startDate = Assert.IsType<DateTime>(okResult.Value);
            Assert.Equal(DateTime.Now.Date, startDate.Date);
        }

        /// <summary>
        /// Test that verifies if the `GetAdStartDate` method returns an error result when the ad is not found
        /// </summary>
        [Fact]
        public async void GetAdStartDate_ReturnsError()
        {
            var expectedAdId = 100;

            // Act
            var result = await _controller.GetAdStartDate(expectedAdId);

            // Assert
            var objectResult = Assert.IsType<ObjectResult>(result.Result);
            Assert.Equal(StatusCodes.Status500InternalServerError, objectResult.StatusCode);
        }

        /// <summary>
        /// Test that verifies if the `GetAdEndDate` method returns an OK result with the expected end date
        /// </summary>
        [Fact]
        public async void GetAdEndDate_ReturnsOkResult_WithEndDate()
        {
            var expectedAdId = 1;

            // Act
            var result = await _controller.GetAdEndDate(expectedAdId);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var endDate = Assert.IsType<DateTime>(okResult.Value);
            Assert.Equal(DateTime.Now.AddDays(1).Date, endDate.Date);
        }

        /// <summary>
        /// Test that verifies if the `GetAdEndDate` method returns an error result when the ad is not found
        /// </summary>
        [Fact]
        public async void GetAdEndDate_ReturnsError()
        {
            var expectedAdId = 100;

            // Act
            var result = await _controller.GetAdEndDate(expectedAdId);

            // Assert
            var objectResult = Assert.IsType<ObjectResult>(result.Result);
            Assert.Equal(StatusCodes.Status500InternalServerError, objectResult.StatusCode);
        }

        /// <summary>
        /// Test that verifies if the `GetAdCreator` method returns an OK result with the expected admin ID
        /// </summary>
        [Fact]
        public async void GetAdCreator_ReturnsOkResult_WithAdminId()
        {
            var expectedAdId = 2;

            // Act
            var result = await _controller.GetAdCreator(expectedAdId);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var adminId = Assert.IsType<int>(okResult.Value);
            Assert.Equal(1, adminId);
        }

        /// <summary>
        /// Test that verifies if the `createAd` method returns a CreatedAtActionResult with the created ad
        /// </summary>
        [Fact]
        public async void CreateAd_ReturnsCreatedAtActionResult_WhenAdAdded()
        {
            var newAd = new AdRequestDto
            {
                AdminId = 1,
                StartDate = DateTime.Now,
                EndDate = DateTime.Now.AddDays(1),
                isActive = true,
                AdFile = null
            };

            // Act
            var result = await _controller.createAd(newAd);

            // Assert
            var createdAtActionResult = Assert.IsType<CreatedAtActionResult>(result.Result);
            var createdAd = Assert.IsAssignableFrom<Ad>(createdAtActionResult.Value);
            Assert.Equal(newAd.AdminId, createdAd.AdminID);
            Assert.Equal(newAd.StartDate, createdAd.StartDate);
            Assert.Equal(newAd.EndDate, createdAd.EndDate);
            Assert.Equal(newAd.isActive, createdAd.isActive);
            
        }

        /// <summary>
        /// Test that verifies if the `createAd` method returns a BadRequestObjectResult when the ad data is invalid
        /// </summary>
        [Fact]
        public async void CreateAd_ReturnsBadRequest_WithMessage()
        {
            // Act
            var result = await _controller.createAd(null);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result.Result);
            Assert.Equal("invalid data.", badRequestResult.Value);
        }

        /// <summary>
        /// Test that verifies if the `updateAd` method returns an OkObjectResult when the ad update is successful
        /// </summary>
        [Fact]
        public async void UpdateAd_ReturnsOkResult_WhenUpdateSucceeds()
        {
            var adId = 1;
            var updatedAd = new AdRequestDto
            {
                AdminId = 2,
                StartDate = DateTime.Now.AddDays(1),
                EndDate = DateTime.Now.AddDays(2),
                isActive = false,
                AdFile = null
            };

            // Act
            var result = await _controller.updateAd(adId, updatedAd);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var isUpdateSuccess = Assert.IsType<Ad>(okResult.Value);
            Assert.NotNull(isUpdateSuccess);
        }

        /// <summary>
        /// Test that verifies if the `updateAd` method returns a BadRequestObjectResult when the ad data is invalid
        /// </summary>
        [Fact]
        public async void UpdateAd_ReturnsBadRequest_WithMessage()
        {
            var expectedAd = 1;
            // Act
            var result = await _controller.updateAd(expectedAd, null);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(badRequestResult.Value);
            Assert.Equal("Invalid Data", errorResponse.error);
        }

        /// <summary>
        /// Test that verifies if the `DeleteAd` method returns a NoContentResult when the ad is deleted successfully
        /// </summary>
        [Fact]
        public async void DeleteAd_ReturnsNoContentResult_WhenAdDeleted()
        {
            var adId = 1;

            // Act
            var result = await _controller.DeleteAd(adId);

            // Assert
            Assert.IsType<NoContentResult>(result);
        }

        /// <summary>
        /// Test that verifies if the `DeleteAd` method returns a NotFoundObjectResult when the ad is not found
        /// </summary>
        [Fact]
        public async void DeleteAd_ReturnsNotFound_WhenAdNotFound()
        {
            var nonExistentAdId = 100;

            // Act
            var result = await _controller.DeleteAd(nonExistentAdId);

            // Assert
            var notFoundResult = Assert.IsType<NotFoundObjectResult>(result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(notFoundResult.Value);
            Assert.Equal("Ad not found", errorResponse.error);

        }

        /// <summary>
        /// Test that verifies if the `GetAdCount` method returns an OkObjectResult with the expected ad count
        /// </summary>
        [Fact]
        public async void GetAdCount_ReturnsOkResult_WithAdCount()
        {
            // Act
            var result = await _controller.GetAdCount();

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var adCount = Assert.IsType<int>(okResult.Value);
            Assert.Equal(3, adCount);
        }

    }
}
