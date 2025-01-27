using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WebAPI.Controllers;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.DTO.response;
using WebAPI.Entity;
using WebAPI.Services;

namespace WebAPITests.controllers
{
    public class LogControllerTests : IDisposable
    {
        private readonly WebApiContext _webApiContext;
        private readonly DataContext _context;
        private readonly LogService _logService;
        private readonly LogController _controller;

        public LogControllerTests()
        {
            // Initialize in-memory database and services
            _webApiContext = new WebApiContext();
            _context = _webApiContext.getWebApiContext();
            _logService = new LogService(_context);
            _controller = new LogController(_logService);
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
        /// Test case for GetAllLogs method, checking if the result is not null    
        /// </summary>
        [Fact]
        public async void GetAllLogsAsyncShouldNotBeNull()
        {
            // Act
            var result = await _controller.GetAllLogs();

            // Assert
            Assert.IsType<ActionResult<IEnumerable<Log>>>(result);
        }

        /// <summary>
        /// Test case for GetLogsByDiaryId method, ensuring it doesn't return null
        /// </summary>
        [Fact]
        public async void GetLogsByDiaryIdAsyncShouldNotBeNull()
        {
            // Arrange
            var diaryId = 1;

            // Act
            var result = await _controller.GetLogsByDiaryId(diaryId);

            // Assert
            Assert.IsType<ActionResult<IEnumerable<Log>>>(result);
        }

        /// <summary>
        /// Test case for GetLogsByDiaryId when no logs are found, expecting a "Not Found" message
        /// </summary>
        [Fact]
        public async void GetLogsByDiaryIdAsync_ReturnsNotFound_WithMessage()
        {
            // Arrange
            var diaryId = 100;

            // Act
            var result = await _controller.GetLogsByDiaryId(diaryId);

            // Assert
            var notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(notFoundResult.Value);
            Assert.Equal("No logs found for this DiaryId.", errorResponse.error);
        }

        /// <summary>
        /// Test case for GetLogById method, checking if the log returned has the correct Id
        /// </summary>
        [Fact]
        public async void GetLogByIdAsyncShouldNotBeNull()
        {
            // Arrange
            var logId = 1;

            // Act
            var log = await _controller.GetLogById(logId);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(log.Result);
            var logFinal = Assert.IsType<Log>(okResult.Value);
            Assert.Equal(1, logFinal.Id);
        }

        /// <summary>
        /// Test case for GetLogById method when the log is not found, expecting a "Not Found" message
        /// </summary>
        [Fact]
        public async void GetLogByIdAsync_ReturnsNotFound_WithMessage()
        {
            // Arrange
            var logId = 100;

            // Act
            var result = await _controller.GetLogById(logId);

            // Assert
            var notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(notFoundResult.Value);
            Assert.Equal("Log not Found", errorResponse.error);
        }

        /// <summary>
        /// Test case for AddLog method, ensuring that a new log is returned upon creation
        /// </summary>
        [Fact]
        public async void AddLogAsyncShouldReturnNewLog()
        {
            // Arrange
            var log = new LogRequestDto
            {
                DiaryId = 1,
                LogDescription = "Test Log"
            };

            // Act
            var result = await _controller.CreateLog(log);

            // Assert
            var createdResult = Assert.IsType<CreatedAtActionResult>(result.Result);
            var logFinal = Assert.IsType<Log>(createdResult.Value);
            Assert.Equal(log.DiaryId, logFinal.DiaryId);
            Assert.Equal(log.LogDescription, logFinal.LogDescription);
        }

        /// <summary>
        /// Test case for AddLog method when invalid data is provided (null), expecting a BadRequest
        /// </summary>
        [Fact]
        public async void AddLogAsyncShouldReturnBadRequest()
        {
            // Act
            var result = await _controller.CreateLog(null);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(badRequestResult.Value);
            Assert.Equal("Invalid Data", errorResponse.error);
        }

        /// <summary>
        /// Test case for UpdateLog method, checking if it successfully updates the log
        /// </summary>
        [Fact]
        public async void UpdateLogAsyncShouldNotBeNull()
        {
            // Arrange
            var logId = 1;
            var log = new LogRequestDto
            {
                DiaryId = 1,
                LogDescription = "Updated"
            };

            // Act
            var result = await _controller.UpdateLog(logId, log);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var isUpdated = Assert.IsType<bool>(okResult.Value);  // Expecting a bool, not a Log object
            Assert.True(isUpdated); // Assert that the update was successful
        }

        /// <summary>
        /// Test case for UpdateLog method when invalid data is passed (null), expecting a BadRequest
        /// </summary>
        [Fact]
        public async void UpdateLogAsync_ReturnsNotFound_WithMessage()
        {
            // Arrange
            var logId = 1;

            // Act
            var result = await _controller.UpdateLog(logId, null);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(badRequestResult.Value);
            Assert.Equal("Invalid data.", errorResponse.error);
        }

        /// <summary>
        /// Test case for DeleteLog method, ensuring that it returns NoContent when a log is deleted successfully
        /// </summary>
        [Fact]
        public async void DeleteLogAsyncShouldNotBeNull()
        {
            // Arrange
            var logId = 1;

            // Act
            var result = await _controller.DeleteLog(logId);

            // Assert
            Assert.IsType<NoContentResult>(result);
        }

        /// <summary>
        /// Test case for DeleteLog method when the log is not found, expecting a NotFound response
        /// </summary>
        [Fact]
        public async void DeleteLogAsync_ReturnsNotFound_WithMessage()
        {
            // Arrange
            var logId = 100;

            // Act
            var result = await _controller.DeleteLog(logId);

            // Assert
            var notFoundResult = Assert.IsType<NotFoundObjectResult>(result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(notFoundResult.Value);
            Assert.Equal("Log not found or unable to delete.", errorResponse.error);
        }
    }
}
