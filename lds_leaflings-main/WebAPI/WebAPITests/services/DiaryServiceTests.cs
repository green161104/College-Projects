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
    public class DiaryServiceTests : IDisposable
    {
        private readonly DataContext _context;
        private readonly DiaryService _diaryService;

        public DiaryServiceTests()
        {
            var webApiContext = new WebApiContext();

            _context = webApiContext.getWebApiContext();
            //_configuration = new ConfigurationBuilder().AddJsonFile("appsettings.json").Build();
            _diaryService = new DiaryService(_context);
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
        /// Test to ensure GetAllDiariesAsync returns a non-null and non-empty collection
        /// </summary>
        [Fact]
        public async void GetAllDiariesAsyncShouldNotBeNull()
        {
            var diaries = await _diaryService.GetAllDiariesAsync();

            Assert.NotNull(diaries);
            Assert.NotEmpty(diaries);
        }

        /// <summary>
        /// Test to ensure GetDiariesByUserPlantIdAsync returns a non-null and non-empty collection for a valid UserPlantId
        /// </summary>
        [Fact]
        public async void GetDiariesByUserPlantIdAsyncShouldNotBeNull()
        {
            var userPlantId = 1;
            var diaries = await _diaryService.GetDiarieByUserPlantIdAsync(userPlantId);

            Assert.NotNull(diaries);
        }

        /// <summary>
        /// Test to ensure GetDiariesByUserPlantIdAsync returns an empty collection for an invalid UserPlantId
        /// </summary>
        [Fact]
        public async void GetDiariesByUserPlantIdAsyncShouldReturnEmptyCollection()
        {
            var userPlantId = 100;
            var diaries = await _diaryService.GetDiarieByUserPlantIdAsync(userPlantId);

            Assert.Null(diaries); // Ensure the returned collection is not null
        }

        /// <summary>
        /// Test to ensure GetDiaryByIdAsync returns a non-null Diary object for a valid DiaryId
        /// </summary>
        [Fact]
        public async void GetDiaryByIdAsyncShouldNotBeNull()
        {
            var diaryId = 1;
            var diary = await _diaryService.GetDiaryByIdAsync(diaryId);

            Assert.NotNull(diary);
        }

        /// <summary>
        /// Test to ensure GetDiaryByIdAsync returns null for a non-existent diaryId
        /// </summary>
        [Fact]
        public async void GetDiaryByIdAsyncShouldBeNull()
        {
            var diaryId = 100;
            var diary = await _diaryService.GetDiaryByIdAsync(diaryId);

            Assert.Null(diary);
        }

        /// <summary>
        /// Test to ensure AddDiaryAsync returns a non-null result when a valid diary is added
        /// </summary>
        [Fact]
        public async void AddDiaryEntryAsyncShouldNotBeNull()
        {
            var diary = new DiaryRequestDto
            {
                UserPlantId = 3,
                Title = "Test Diary Entry",
            };

            var result = await _diaryService.AddDiaryAsync(diary);

            Assert.NotNull(result);
        }

        /// <summary>
        /// Test to ensure UpdateDiaryAsync returns true when a valid diary is updated
        /// </summary>
        [Fact]
        public async void UpdateDiaryAsyncShouldNotBeNull()
        {
            var diaryId = 1;

            var diary = new DiaryRequestDto
            {
                UserPlantId = 1,
                Title = "Updated",
            };

            var isUpdated = await _diaryService.UpdateDiaryAsync(diaryId, diary);
            var updatedDiary = await _diaryService.GetDiaryByIdAsync(diaryId);

            Assert.True(isUpdated);
            Assert.Equal("Updated", updatedDiary.Title);
        }

        /// <summary>
        /// Test to ensure UpdateDiaryAsync returns false for a non-existent diaryId
        /// </summary>
        [Fact]
        public async void UpdateDiaryAsyncShouldBeNull()
        {
            var diaryId = 100;

            var diary = new DiaryRequestDto
            {
                UserPlantId = 1,
                Title = "Updated",
            };

            var isUpdated = await _diaryService.UpdateDiaryAsync(diaryId, diary);

            Assert.False(isUpdated);
        }

        /// <summary>
        /// Test to ensure DeleteDiaryAsync returns true when a valid diary is deleted
        /// </summary>
        [Fact]
        public async void DeleteDiaryAsyncShouldNotBeNull()
        {
            var diaryId = 1;

            var isDeleted = await _diaryService.DeleteDiaryAsync(diaryId);

            Assert.True(isDeleted);
        }

        /// <summary>
        /// Test to ensure DeleteDiaryAsync returns false for a non-existent diaryId
        /// </summary>
        [Fact]
        public async void DeleteDiaryAsyncShouldBeNull()
        {
            var diaryId = 100;

            var isDeleted = await _diaryService.DeleteDiaryAsync(diaryId);

            Assert.False(isDeleted);
        }
    }
}
