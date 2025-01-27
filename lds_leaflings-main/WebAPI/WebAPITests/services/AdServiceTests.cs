using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WebAPI.DTO.request;
using WebAPI.Data;
using WebAPI.Services;
using WebAPI.Entity;


namespace WebAPITests.services
{
    public class AdServiceTests : IDisposable
    {
        private DataContext _context;
        private readonly AdService _adService;
        //private readonly IConfiguration _configuration;

        /// <summary>
        /// Assigns the context and configuration to the AdServiceTests class
        /// </summary>
        public AdServiceTests()
        {
            var webApiContext = new WebApiContext();

            _context = webApiContext.getWebApiContext();
            //_configuration = new ConfigurationBuilder().AddJsonFile("appsettings.json").Build();
            _adService = new AdService(_context);
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
        /// Test case to verify that the GetAllAdsAsync method returns all the ads in the system
        /// </summary>
        [Fact]
        public async void GetAllAdsAsync_ShouldReturnAllAdmins()
        {
            var totalAds = 3;
            var ads = await _adService.GetAllAds();

            Assert.Equal(totalAds, ads.Count);
        }

        /// <summary>
        /// Test case to verify that the GetAdByIDAsync method returns a non-null ad when a valid ad ID is provided
        /// </summary>
        [Fact]
        public async void GetAdByIdAsyncShouldNotBeNull()
        {
            var expectedAdId = 1;
            var ad = await _adService.GetAdByID(expectedAdId);

            Assert.NotNull(ad);
        }

        /// <summary>
        /// Test case to verify that the GetAdByIDAsync method returns null when an invalid ad ID is provided
        /// </summary>
        [Fact]
        public async void GetAdByIdAsyncShouldBeNull()
        {
            var expectedAdId = 100;
            var ad = await _adService.GetAdByID(expectedAdId);

            Assert.Null(ad);
        }

        /// <summary>
        /// Test case to verify that the GetAdCountAsync method returns the correct count of ads in the system
        /// </summary>
        [Fact]
        public async void GetAdCountAsync_ShouldReturnCorrectCount()
        {
            var totalAds = 3;
            var adCount = await _adService.GetAdCount();

            Assert.Equal(totalAds, adCount);
        }

        /// <summary>
        /// Test case to verify that the GetAdStartAsync method throws a KeyNotFoundException when an invalid ad ID is provided
        /// </summary>
        [Fact]
        public async void GetAdStartAsync_ShouldThrowKeyNotFoundException()
        {
            var expectedAdId = 100;

            await Assert.ThrowsAsync<KeyNotFoundException>(() => _adService.GetAdStart(expectedAdId));
        }

        /// <summary>
        /// Test case to verify that the GetAdEndAsync method throws a KeyNotFoundException when an invalid ad ID is provided
        /// </summary>
        [Fact]
        public async void GetAdEndAsync_ShouldThrowKeyNotFoundException()
        {
            var expectedAdId = 100;

            await Assert.ThrowsAsync<KeyNotFoundException>(() => _adService.GetAdEnd(expectedAdId));
        }

        /// <summary>
        /// Test case to verify that the GetAdCreator method returns the correct admin ID for a given ad ID
        /// </summary>
        [Fact]
        public async void GetAdCreator_ShouldReturnCorrectAdminId()
        {
            // Arrange
            var adId = 1;
            var expectedAdminId = 2;

            // Act
            var adminId = await _adService.GetAdCreator(adId);

            // Assert
            Assert.Equal(expectedAdminId, adminId);
        }

        /// <summary>
        /// Test case to verify that the GetAdCreator method throws a KeyNotFoundException when an invalid ad ID is provided
        /// </summary>
        [Fact]
        public async void GetAdCreator_ShouldThrowKeyNotFoundException()
        {
            var expectedAdId = 100;

            await Assert.ThrowsAsync<KeyNotFoundException>(() => _adService.GetAdCreator(expectedAdId));
        }

        /// <summary>
        /// Test case to verify that the UpdateAdAsync method successfully updates an existing ad
        /// </summary>
        [Fact]
        public async void UpdateAdAsync_ShouldUpdateAdSuccessfully()
        {
            // Arrange
            var adId = 1;
            var updatedAd = new AdRequestDto
            {
                AdminId = 2,
                StartDate = DateTime.Now.AddDays(1),
                EndDate = DateTime.Now.AddDays(31),
                isActive = false,
                AdFile = null
            };

            // Act
            var result = await _adService.UpdateAdAsync(adId, updatedAd);
            var modified = await _adService.GetAdByID(adId);

            // Assert
            Assert.Equal(updatedAd.AdminId, modified.AdminID);
            Assert.Equal(updatedAd.isActive, modified.isActive);
        }

        /// <summary>
        /// Test case to verify that the UpdateAdAsync method returns false when the ad does not exist
        /// </summary>
        [Fact]
        public async void UpdateAdAsync_ShouldReturnFalseIfAdDoesNotExist()
        {
            // Arrange
            var adId = 100;
            var updatedAd = new AdRequestDto
            {
                AdminId = 2,
                StartDate = DateTime.Now.AddDays(1),
                EndDate = DateTime.Now.AddDays(31),
                isActive = false,
                AdFile = null
            };

            // Act
            var result = await _adService.UpdateAdAsync(adId, updatedAd);

            // Assert
            Assert.Null(result);
        }

        /// <summary>
        /// Test case to verify that the DeleteAdAsync method successfully deletes an existing ad
        /// </summary>
        [Fact]
        public async void DeleteAdAsync_ShouldDeleteAdSuccessfully()
        {
            // Arrange
            var adId = 1;

            // Act
            var result = await _adService.deleteAdAsync(adId);
            var deletedAd = await _adService.GetAdByID(adId);

            // Assert
            Assert.True(result);
            Assert.Null(deletedAd);
        }

        /// <summary>
        /// Test case to verify that the DeleteAdAsync method returns false when the ad does not exist
        /// </summary>
        [Fact]
        public async void DeleteAdAsync_ShouldReturnFalseIfAdDoesNotExist()
        {
            // Arrange
            var adId = 100;

            // Act
            var result = await _adService.deleteAdAsync(adId);

            // Assert
            Assert.False(result);
        }

    }
}
