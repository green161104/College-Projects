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
    public class LogServiceTests : IDisposable
    {
        private readonly DataContext _context;
        private readonly LogService _logService;

        public LogServiceTests()
        {
            var webApiContext = new WebApiContext();

            _context = webApiContext.getWebApiContext();
            _logService = new LogService(_context);
        }

        /// <summary>
        /// Dispose method to delete the database after the tests are run
        /// </summary>
        public void Dispose()
        {
            _context.Database.EnsureDeleted();
            _context.Dispose();
        }

        /// <summary>
        /// Test to ensure that all logs can be retrieved successfully.
        /// Asserts that logs are not null and not empty.
        /// </summary>
        [Fact]
        public async void GetAllLogsAsyncShouldNotBeNull()
        {
            var logs = await _logService.GetAllLogsAsync();

            Assert.NotNull(logs);
            Assert.NotEmpty(logs);
        }

        /// <summary>
        /// Test to ensure that logs associated with a specific diaryId can be retrieved.
        /// Asserts that logs are not null and not empty.
        /// </summary>
        [Fact]
        public async void GetLogsByDiaryIdAsyncShouldNotBeNull()
        {
            var diaryId = 1;
            var logs = await _logService.GetLogsByDiaryIdAsync(diaryId);

            Assert.NotNull(logs);
            Assert.NotEmpty(logs);
        }

        /// <summary>
        /// Test to ensure that when no logs exist for an invalid diaryId, an empty collection is returned.
        /// </summary>
        [Fact]
        public async void GetLogsByDiaryIdAsyncShouldReturnEmptyCollectionForInvalidDiaryId()
        {
            var diaryId = 100;
            var logs = await _logService.GetLogsByDiaryIdAsync(diaryId);

            Assert.NotNull(logs);
            Assert.Empty(logs);
        }

        /// <summary>
        /// Test to retrieve a specific log by its logId.
        /// Asserts that the log is not null when found.
        /// </summary>
        [Fact]
        public async void GetLogByIdAsyncShouldNotBeNull()
        {
            var logId = 1;
            var log = await _logService.GetLogByIdAsync(logId);

            Assert.NotNull(log);
        }

        /// <summary>
        /// Test to ensure that when a logId is invalid, null is returned.
        /// </summary>
        [Fact]
        public async void GetLogByIdAsyncShouldBeNullForInvalidLogId()
        {
            var logId = 100;
            var log = await _logService.GetLogByIdAsync(logId);

            Assert.Null(log);
        }

        /// <summary>
        /// Test to add a new log entry.
        /// Asserts that a new log is added and its details match the provided data.
        /// </summary>
        [Fact]
        public async void AddLogAsyncShouldReturnNewLog()
        {
            var log = new LogRequestDto
            {
                DiaryId = 1,
                LogDescription = "Test Log"
            };

            var newLog = await _logService.AddLogAsync(log);

            Assert.NotNull(newLog);
            Assert.Equal(log.DiaryId, newLog.DiaryId);
            Assert.Equal(log.LogDescription, newLog.LogDescription);
        }

        /// <summary>
        /// Test to update an existing log entry.
        /// Asserts that the log is updated and the changes are reflected.
        /// </summary>
        [Fact]
        public async void UpdateLogAsyncShouldNotBeNull()
        {
            var logId = 1;

            var log = new LogRequestDto
            {
                DiaryId = 1,
                LogDescription = "Updated"
            };

            var isUpdated = await _logService.UpdateLogAsync(logId, log);
            var updatedLog = await _logService.GetLogByIdAsync(logId);

            Assert.True(isUpdated);
            Assert.Equal("Updated", updatedLog.LogDescription);

        }

        /// <summary>
        /// Test to ensure that an invalid logId cannot be updated.
        /// Asserts that the update operation returns false.
        /// </summary>
        [Fact]
        public async void UpdateLogAsyncShouldBeNull()
        {
            var logId = 100;

            var log = new LogRequestDto
            {
                DiaryId = 1,
                LogDescription = "Updated"
            };

            var isUpdated = await _logService.UpdateLogAsync(logId, log);

            Assert.False(isUpdated);
        }

        /// <summary>
        /// Test to delete a log entry.
        /// Asserts that the log is deleted and no longer exists in the system.
        /// </summary>
        [Fact]
        public async void DeleteLogAsyncShouldReturnTrue()
        {
            var logId = 1;

            var isDeleted = await _logService.DeleteLogAsync(logId);
            var deletedLog = await _logService.GetLogByIdAsync(logId);

            Assert.True(isDeleted);
            Assert.Null(deletedLog);
        }

        /// <summary>
        /// Test to ensure that deleting a non-existent log returns false.
        /// </summary>
        [Fact]
        public async void DeleteLogAsyncShouldReturnFalse()
        {
            var logId = 100;

            var isDeleted = await _logService.DeleteLogAsync(logId);

            Assert.False(isDeleted);
        }
    }
}
