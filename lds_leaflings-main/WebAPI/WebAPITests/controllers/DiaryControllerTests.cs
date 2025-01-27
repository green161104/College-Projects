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
    public class DiaryControllerTests : IDisposable
    {
        private readonly WebApiContext _webApiContext;
        private readonly DataContext _context;
        private readonly DiaryService _diaryService;
        private readonly DiaryController _controller;

        public DiaryControllerTests()
        {
            // Initialize in-memory database and services
            _webApiContext = new WebApiContext();
            _context = _webApiContext.getWebApiContext();
            _diaryService = new DiaryService(_context);
            _controller = new DiaryController(_diaryService);
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
        /// Test to verify that GetAllDiaries returns a non-null result.
        /// </summary>
        [Fact]
        public async void GetAllDiariesAsyncShouldNotBeNull()
        {
            // Act
            var result = await _controller.GetAllDiaries();

            // Assert
            Assert.IsType<ActionResult<IEnumerable<Diary>>>(result);
        }

        /// <summary>
        /// Test to verify that GetDiariesByUserPlantId returns non-null diaries when a valid ID is given.
        /// </summary>
        [Fact]
        public async void GetDiariesByUserPlantIdAsyncShouldNotBeNull()
        {
            // Arrange
            var userPlantId = 1;

            // Act
            var diaries = await _controller.GetDiarieByUserPlantId(userPlantId);

            // Assert
            Assert.NotNull(diaries);
        }

        /// <summary>
        /// Test to verify that GetDiariesByUserPlantId returns a NotFound result when an invalid ID is given.
        /// </summary>
        [Fact]
        public async void GetDiariesByUserPlantIdAsync_ReturnsNotFound_WithMessage()
        {
            // Arrange
            var userPlantId = 100;

            // Act
            var result = await _controller.GetDiarieByUserPlantId(userPlantId);

            // Assert
            var notFoundResult = Assert.IsType<NotFoundObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(notFoundResult.Value);
            Assert.Equal("No diaries found for this UserPlantId.", errorResponse.error);
        }

        /// <summary>
        /// Test to verify that GetDiaryById returns a diary for an existing ID.
        /// </summary>
        [Fact]
        public async void GetDiaryByIdAsyncShouldNotBeNull()
        {
            // Arrange
            var diaryId = 1;

            // Act
            var diary = await _controller.GetDiaryById(diaryId);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(diary.Result);
            var diaryFinal = Assert.IsType<Diary>(okResult.Value);
            Assert.Equal(1, diaryFinal.Id);
        }

        /// <summary>
        /// Test to verify that GetDiaryById returns null when the diary ID does not exist.
        /// </summary>
        [Fact]
        public async void GetDiaryByIdAsyncShouldBeNull()
        {
            // Arrange
            var diaryId = 100;

            // Act
            var diary = await _controller.GetDiaryById(diaryId);

            // Assert
            Assert.Null(diary.Value);
        }

        /// <summary>
        /// Test to verify that AddDiaryEntry successfully creates a diary.
        /// </summary>
        [Fact]
        public async void AddDiaryEntryAsyncShouldNotBeNull()
        {
            // Arrange
            var diary = new DiaryRequestDto
            {
                UserPlantId = 3,
                Title = "Test Diary Entry"
            };

            // Act
            var result = await _controller.CreateDiary(diary);

            // Assert
            Assert.NotNull(result);
        }

        /// <summary>
        /// Test to verify that AddDiaryEntry returns a BadRequest when the provided data is null.
        /// </summary>
        [Fact]
        public async void AddDiaryEntryAsyncShouldReturnBadRequest()
        {
            // Act
            var result = await _controller.CreateDiary(null);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(badRequestResult.Value);
            Assert.Equal("Invalid Data", errorResponse.error);
        }

        /// <summary>
        /// Test to verify that UpdateDiary updates an existing diary and returns a non-null result.
        /// </summary>
        [Fact]
        public async void UpdateDiaryAsyncShouldNotBeNull()
        {
            // Arrange
            var diaryId = 1;

            var diary = new DiaryRequestDto
            {
                UserPlantId = 1,
                Title = "Updated",
            };

            // Act
            var result = await _controller.UpdateDiary(diaryId, diary);

            // Assert
            Assert.NotNull(result);
        }

        /// <summary>
        /// Test to verify that UpdateDiary returns a BadRequest when invalid data is provided.
        /// </summary>
        [Fact]
        public async void UpdateDiaryAsyncShouldBeNull()
        {
            // Arrange
            var diaryId = 1;


            // Act
            var result = await _controller.UpdateDiary(diaryId, null);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(badRequestResult.Value);
            Assert.Equal("Invalid Data", errorResponse.error);
        }

        /// <summary>
        /// Test to verify that DeleteDiary deletes an existing diary and returns a non-null result.
        /// </summary>
        [Fact]
        public async void DeleteDiaryAsyncShouldNotBeNull()
        {
            // Arrange
            var diaryId = 1;

            // Act
            var result = await _controller.DeleteDiary(diaryId);

            // Assert
            Assert.NotNull(result);
        }

        /// <summary>
        /// Test to verify that DeleteDiary returns a NotFound result when the diary ID does not exist.
        /// </summary>
        [Fact]
        public async void DeleteDiaryAsyncShouldBeNull()
        {
            // Arrange
            var diaryId = 100;

            // Act
            var result = await _controller.DeleteDiary(diaryId);

            // Assert
            Assert.IsType<NotFoundObjectResult>(result);
        }
    }
}
