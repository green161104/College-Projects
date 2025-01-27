using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.Entity;
using WebAPI.Services;

namespace WebAPITests.services
{
    public class WarningServiceTests : IDisposable
    {
        private readonly DataContext _context;
        private readonly WarningService _warningService;


        public WarningServiceTests()
        {
            var webApiContext = new WebApiContext();
            _context = webApiContext.getWebApiContext();
            _warningService = new WarningService(_context);
       }

        public void Dispose()
        {
            _context.Database.EnsureDeleted();
            _context.Dispose();
        }

        [Fact]
        public async void GetAllWarnigsFromUserAsyncShouldNotBeEmpty()
        {
            // Arrange 
            var userId = 4;
            var expectedWarningsCount = 2;

            // Act
            var warnings = await _warningService.GetAllWarningsFromUserAsync(userId);

            // Assert
            Assert.NotEmpty(warnings);
            Assert.Equal(expectedWarningsCount, warnings.Count);
        }

        [Fact]
        public async void GetAllWarnigsFromUserAsyncAsyncShouldShouldNotAcceptNonExistentUserIds()
        {
            // Arrange
            var nonExistentUser = 99;

            // Act
            var warning = await _warningService.GetAllWarningsFromUserAsync(nonExistentUser);

            // Assert
            Assert.Empty(warning);
        }

        [Fact]
        public async void GetWarningIdAsyncShouldNotReturnNull()
        {
            // Arrange
            var warningId = 1;

            // Act
            var warning = await _warningService.GetWarningIdAsync(warningId);

            // Assert
            Assert.NotNull(warning);
        }

        [Fact]
        public async void GetWarningIdAsyncShouldNotAcceptNonExistentWarningIds()
        {
            // Arrange
            var nonExistentWarningId = 99;

            // Act
            var warning = await _warningService.GetWarningIdAsync(nonExistentWarningId);

            // Assert
            Assert.Null(warning);
        }

        [Fact]
        public async void AddWarningAsyncShouldAddWarning()
        {
            // Arrange
            var warningDto = new WarningRequestDto
            {
                UserId = 1,
                Location = "Test Location",
                Message = "Test Message"
            };

            // Act
            var warning = await _warningService.AddWarningAsync(warningDto);

            // Assert
            Assert.NotNull(warning);
            Assert.Equal(warningDto.UserId, warning.UserId);
            Assert.Equal(warningDto.Location, warning.Location);
            Assert.Equal(warningDto.Message, warning.Message);
        }

        [Fact]
        public async void UpdateWarningAsyncShouldUpdateWarning()
        {
            // Arrange
            var warningId = 1;
            var warningDto = new WarningRequestDto
            {
                UserId = 1,
                Location = "Updated Location",
                Message = "Updated Message"
            };

            // Act
            var isUpdated = await _warningService.UpdateWarningAsync(warningId, warningDto);
            var updatedWarning = await _warningService.GetWarningIdAsync(warningId);

            // Assert
            Assert.True(isUpdated);
            Assert.Equal(warningDto.UserId, updatedWarning.UserId);
            Assert.Equal(warningDto.Location, updatedWarning.Location);
            Assert.Equal(warningDto.Message, updatedWarning.Message);
        }

        [Fact]
        public async void UpdateWarningAsyncShouldReturnFalseForNonExistentWarning()
        {
            // Arrange
            var nonExistentWarningId = 99;
            var warningDto = new WarningRequestDto
            {
                UserId = 1,
                Location = "Updated Location",
                Message = "Updated Message"
            };

            // Act
            var isUpdated = await _warningService.UpdateWarningAsync(nonExistentWarningId, warningDto);

            // Assert
            Assert.False(isUpdated);
        }

        [Fact]
        public async void DeleteWarningAsyncShouldDeleteWarning()
        {
            // Arrange
            var warningId = 1;

            // Act
            var isDeleted = await _warningService.DeleteWarningAsync(warningId);
            var deletedWarning = await _warningService.GetWarningIdAsync(warningId);

            // Assert
            Assert.True(isDeleted);
            Assert.Null(deletedWarning);
        }

        [Fact]
        public async void DeleteWarningAsyncShouldReturnFalseForNonExistentWarning()
        {
            // Arrange
            var nonExistentWarningId = 99;

            // Act
            var isDeleted = await _warningService.DeleteWarningAsync(nonExistentWarningId);

            // Assert
            Assert.False(isDeleted);
        }
    }
}
