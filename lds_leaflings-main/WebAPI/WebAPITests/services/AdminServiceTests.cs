using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.Entity;
using WebAPI.Entity.enums;
using WebAPI.Services;
using WebAPI.Utils;

namespace WebAPITests.services
{
    public class AdminServiceTests : IDisposable
    {
        private readonly DataContext _context;
        private readonly AdminService _adminService;
        private readonly Cryptography _cryptography;
        private readonly IConfiguration _configuration;

        public AdminServiceTests()
        {
            var webApiContext = new WebApiContext();

            _context = webApiContext.getWebApiContext();
            _configuration = new ConfigurationBuilder().AddJsonFile("appsettings.json").Build();
            _cryptography = new Cryptography(_configuration);
            _adminService = new AdminService(_context, _cryptography); 
        }

        public void Dispose()
        {
            _context.Database.EnsureDeleted();
            _context.Dispose();
        }

        /// <summary>
        /// Tests whether retrieving an admin by id returns successfully
        /// </summary>
        [Fact]
        public async void GetAdminByIdAsyncShouldNotBeNull()
        {
            var expectedAdminId = 1;
            var admin = await _adminService.GetAdminByIdAsync(expectedAdminId);

            Assert.NotNull(admin);
        }

        /// <summary>
        /// Tests whether getting an admin by id returns the correct admin
        /// </summary>
        [Fact]
        public async void GetAdminByIdAsync_ShouldReturnRightAdmin()
        {
            var expectedAdminId = 2;
            var expectedAdmin = new WebAPI.Entity.Admin
            {
                Id = expectedAdminId,
                Username = "Admin",
                Email = _cryptography.HashString("admin@admin.com"),
                Password = _cryptography.HashString("adminpassword"),
                Contact = "937929222",
                Role = 1
            };

            var obteinedAdmin = await _adminService.GetAdminByIdAsync(expectedAdminId);

            Assert.NotNull(obteinedAdmin);
            Assert.Equal(expectedAdmin.Id, obteinedAdmin.Id);
            Assert.Equal(expectedAdmin.Username, obteinedAdmin.Username);
            Assert.Equal(expectedAdmin.Email, obteinedAdmin.Email);
            Assert.Equal(expectedAdmin.Contact, obteinedAdmin.Contact);
            Assert.Equivalent(expectedAdmin, obteinedAdmin);
        }


        /// <summary>
        /// Tests whether getting all admins returns all admins
        /// </summary>
        [Fact]
        public async void GetAllAdminsAsync_ShouldReturnAllAdmins()
        {
            var totalAdmins = 3;
            var admins = await _adminService.GetAllAdminsAsync();

            Assert.Equal(totalAdmins, admins.Count);
            Assert.IsType<List<Admin>>(admins);
        }

        /// <summary>
        /// Tests whether getting an admin by id returns null if the admin does not exist
        /// </summary>
        [Fact]
        public async void GetAdminByIdAsync_ShouldReturnNullForNonExistentAdmin()
        {
            var admin = await _adminService.GetAdminByIdAsync(99);
            Assert.Null(admin);
        }

        /// <summary>
        /// Tests whether the return is correct for an id of zero
        /// </summary>
        [Fact]
        public async void GetAdminByIdAsync_ShouldReturnNullWithZeroId()
        {
            var nonexistentAdminId = 0;
            var actualAdmin = await _adminService.GetAdminByIdAsync(nonexistentAdminId);
            Assert.Null(actualAdmin);
        }

        /// <summary>
        /// Tests whether the return is correct for a negative id
        /// </summary>
        [Fact]
        public async void GetAdminByIdAsync_ShouldReturnNullWithNegativeId()
        {
            var nonexistentAdminId = -1;
            var admin = await _adminService.GetAdminByIdAsync(nonexistentAdminId);
            Assert.Null(admin);
        }


        /// <summary>
        /// Adding an admin should successfully add it if information is correct
        /// </summary>
        [Fact]
        public async void AddAdminAsync_ShouldAddAdminSuccessfully()
        {
            var newAdminDto = new AdminRequestDto
            {
                Username = "Admin3",
                Email = "admin3@admin.com",
                Password = "adminpass3",
                Contact = "111111111"
            };

            var addedAdmin = await _adminService.AddAdminAsync(newAdminDto);
            var receivedAdmin = await _adminService.GetAdminByIdAsync(addedAdmin.Id);

            Assert.NotNull(addedAdmin);
            Assert.Equal(addedAdmin.Username, receivedAdmin.Username);
            Assert.Equal(addedAdmin.Email, receivedAdmin.Email);
            // TODO ficava bem se tivesse um equals nas entities
            Assert.Equivalent(addedAdmin, receivedAdmin);
        }

        /// <summary>
        /// Tests if the appropriate exception (InvalidOperationException) is thrown when a user is created with an 
        /// email already in use 
        /// </summary>
        [Fact]
        public async void AddAdminAsync_ShouldThrowInvalidOperationExceptionIfEmailInUse()
        {
            var newAdmin = new AdminRequestDto
            {
                Username = "test2",
                Password = "test123",
                Email = "admin@admin.com",
                Contact = "Test"
            };

            await Assert.ThrowsAsync<InvalidOperationException>(async () =>
            {
                await _adminService.AddAdminAsync(newAdmin);
            });

        }

        /// <summary>
        /// Tests if the appropriate exception (InvalidOperationException) is thrown when an
        /// admin is created with empty or null fields
        /// </summary>
        [Fact]
        public async void AddAdminAsync_ShouldThrowInvalidOperationIfFieldsAreNullOrEmpty()
        {
            var newAdmin = new AdminRequestDto
            {
                Username = null,
                Password = "test123",
                Email = "adminnnn@adminnnn.com",
                Contact = "",
            };

            await Assert.ThrowsAsync<Microsoft.EntityFrameworkCore.DbUpdateException>(async () =>
            {
                await _adminService.AddAdminAsync(newAdmin);
            });

        }


        /// <summary>
        /// Tests whether the correct error is thrown when an email is invalid
        /// </summary>
        [Fact]
        public async void AddAdminAsync_ShouldThrowArgumentException_ForInvalidEmail()
        {
            var newAdmin = new AdminRequestDto
            {
                Username = "test",
                Password = "test123",
                Email = "invalid-email", // Invalid format
                Contact = "Test"
            };

            await Assert.ThrowsAsync<ArgumentException>(async () =>
            {
                await _adminService.AddAdminAsync(newAdmin);
            });
        }


        /// <summary>
        /// Tests whether the correct error is thrown when a password is too short
        /// </summary>
        [Fact]
        public async void AddAdminAsync_ShouldThrowArgumentException_ForShortPassword()
        {
            var newAdmin = new AdminRequestDto
            {
                Username = "test",
                Password = "short", // Too short
                Email = "test@gmail.com",
                Contact = "Test",
            };

            await Assert.ThrowsAsync<ArgumentException>(async () =>
            {
                await _adminService.AddAdminAsync(newAdmin);
            });
        }


        /// <summary>
        /// Updating an admin should correctly update their information
        /// </summary>
        [Fact]
        public async void UpdateAdminAsync_ShouldUpdateAdminDetails()
        {
            var adminIdToUpdate = 1;
            var adminDto = new UpdateAdminDto
            {
                Username = "Updated Admin",
                Contact = "935555555"
            };


            var isUpdated = await _adminService.UpdateAdminAsync(adminIdToUpdate, adminDto);
            var updatedAdmin = await _adminService.GetAdminByIdAsync(adminIdToUpdate);

            Assert.NotNull(isUpdated);
            Assert.Equal("Updated Admin", updatedAdmin.Username);
        }


        /// <summary>
        /// Tests whether updating an admin returns false if unsuccessful
        /// </summary>
        [Fact]
        public async void UpdateAdminAsync_ShouldReturnFalseIfAdminDoesNotExist()
        {
            var wrongAdminId = 99;
            var adminDto = new UpdateAdminDto
            {
                Username = "Non-Existent Admin",
                Contact = "939999999"
            };

            var isUpdated = await _adminService.UpdateAdminAsync(wrongAdminId, adminDto);

            Assert.Null(isUpdated);
        }


        /// <summary>
        /// tests whether attempting to update an admin whose id iss zero will return false
        /// </summary>
        [Fact]
        public async void UpdateAdminAsync_ShouldReturnFalseWhenIdIsZero()
        {
            var nonexistentId = 0;
            var updatedAdminInformation = new UpdateAdminDto
            {
                Username = "non-existent",
                Contact = "non-existent",
               
            };

            var unsuccess = await _adminService.UpdateAdminAsync(nonexistentId, updatedAdminInformation);
            Assert.Null(unsuccess);

        }

        /// <summary>
        /// Testing whether updating an admin returns false in case the admin does not exist
        /// </summary>
        [Fact]
        public async void UpdateAdminAsync_ShouldReturnFalseWhenIdIsNegative()
        {
            var nonexistentId = -1;
            var updatedAdminInformation = new UpdateAdminDto
            {
                Username = "non-existent",
                Contact = "non-existent"
            };

            var unsuccess = await _adminService.UpdateAdminAsync(nonexistentId, updatedAdminInformation);
            Assert.Null(unsuccess);

        }


        /// <summary>
        /// Tests whether deleting an admin effectively deletes them from the database
        /// </summary>
        [Fact]
        public async void DeleteAdminAsync_ShouldDeleteAdminSuccessfully()
        {
            var adminIdToDelete = 2;
            var isDeleted = await _adminService.DeleteAdminAsync(adminIdToDelete);

            Assert.True(isDeleted);
        }

        /// <summary>
        /// Tests wether the return is false when deleting is unsuccessful
        /// </summary>
        [Fact]
        public async void DeleteAdminAsync_ShouldReturnFalseIfAdminDoesNotExist()
        {
            var wrongAdminIdToDelete = 99;
            var isDeleted = await _adminService.DeleteAdminAsync(wrongAdminIdToDelete);

            Assert.False(isDeleted);
        }

        /// <summary>
        /// Tests whether deleting an admin will return false if unsucessful
        /// </summary>
        [Fact]
        public async void DeleteAdminAsync_ShouldReturnFalseWhenAdminIdisZero()
        { 
            var incorrectId = 0;
            var insuccess = await _adminService.DeleteAdminAsync(incorrectId);
            Assert.False(insuccess);

        }

        /// <summary>
        /// Tests whether deleting a user will return false if unsucessful
        /// </summary>
        [Fact]
        public async void DeleteAdminAsync_ShouldReturnFalseWhenAdminIdisNegative()
        {

            var incorrectId = -1;
            var insuccess = await _adminService.DeleteAdminAsync(incorrectId);
            Assert.False(insuccess);

        }

    }
}
