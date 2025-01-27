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
    public class UserServiceTests : IDisposable
    {
        private readonly DataContext _context;
        private readonly UserService _userService;
        private readonly Cryptography _cryptography;
        private readonly IConfiguration _configuration;

        public UserServiceTests()
        {
            var webApiContext = new WebApiContext();
            _context = webApiContext.getWebApiContext();
            _configuration = new ConfigurationBuilder().AddJsonFile("appsettings.json").Build();
            _cryptography = new Cryptography(_configuration);
            _userService = new UserService(_context, _cryptography);
        }

        public void Dispose()
        {
            _context.Database.EnsureDeleted();
            _context.Dispose();
        }

        /// <summary>
        /// Testes whether all users are corrrectly returned
        /// </summary>
        [Fact]
        public async void GetAllUsersAsync_ShouldReturnAllUsers()
        {
            var expectedNumberOfUsers = 5;
            var actualUsers = await _userService.GetAllUsersAsync();
            Assert.Equal(expectedNumberOfUsers, actualUsers.Count);
            Assert.IsType<List<User>>(actualUsers);
        }

        /// <summary>
        /// Tests whether getting a user by their ID returns the correct user
        /// </summary>
        [Fact]
        public async void GetUserByIdAsync_ShouldReturnRightUser()
        {
            var expectedUserId = 4;
            var actualUser = await _userService.GetUserByIdAsync(expectedUserId);
            Assert.NotNull(actualUser);
            Assert.IsType<User>(actualUser);
            Assert.Equal(actualUser.Id, expectedUserId);
        }

        /// <summary>
        /// Tests whether getting a user by their ID returns null when there is no user associated with the ID
        /// </summary>
        [Fact]
        public async void GetUserByIdAsync_ShouldReturnNullWithWrongId()
        {
            var nonexistentUserId = 44;
            var actualUser = await _userService.GetUserByIdAsync(nonexistentUserId);
            Assert.Null(actualUser);
        }

        /// <summary>
        /// Tests whether the return is correct for an id of zero
        /// </summary>
        [Fact]
        public async void GetUserByIdAsync_ShouldReturnNullWithZeroId()
        {
            var nonexistentUserId = 0;
            var actualUser = await _userService.GetUserByIdAsync(nonexistentUserId);
            Assert.Null(actualUser);
        }

        /// <summary>
        /// Tests whether the return is correct for a negative id
        /// </summary>
        [Fact]
        public async void GetUserByIdAsync_ShouldReturnNullWithNegativeId()
        {
            var nonexistentUserId = -1;
            var actualUser = await _userService.GetUserByIdAsync(nonexistentUserId);
            Assert.Null(actualUser);
        }

        /// <summary>
        /// Tests whether adding a user correctly returns the newly created user with the correct data
        /// </summary>
        [Fact]
        public async void AddUserAsync_ShouldCorrectlyAddUser()
        {
            var newUser = new UserRequestDto
            {
                Username = "test",
                Password = "test123",
                Email = "test@gmail.com",
                Contact = "Test",
                Location = "test",
                CareExperience = WebAPI.Entity.enums.ExperienceLevels.Beginner,
                WaterAvailability = WebAPI.Entity.enums.WaterLevels.Medium,
                LuminosityAvailability = WebAPI.Entity.enums.LightLevel.Medium
            };

            var newUserAdded = await _userService.AddUserAsync(newUser);
            var userInDatabase = await _userService.GetUserByIdAsync(newUserAdded.Id);
            Assert.NotNull(newUserAdded);
            Assert.IsType<User>(newUserAdded);
            Assert.IsType<User>(userInDatabase);
            Assert.Equivalent(userInDatabase, newUserAdded);
            Assert.False(userInDatabase.RolePaid);
            Assert.Equal(newUser.Username, userInDatabase.Username);
        }

        /// <summary>
        /// Tests if the appropriate exception (InvalidOperationException) is thrown when a user is created with an 
        /// email already in use 
        /// </summary>
        [Fact]
        public async void AddUserAsync_ShouldThrowInvalidOperationExceptionIfEmailInUse()
        {
            var newUser = new UserRequestDto
            {
                Username = "test2",
                Password = "test123",
                Email = "user2@user2.com",
                Contact = "Test",
                Location = "test",
                CareExperience = WebAPI.Entity.enums.ExperienceLevels.Beginner,
                WaterAvailability = WebAPI.Entity.enums.WaterLevels.Medium,
                LuminosityAvailability = WebAPI.Entity.enums.LightLevel.Medium
            };

            await Assert.ThrowsAsync<InvalidOperationException>(async () =>
            {
                await _userService.AddUserAsync(newUser);
            });

        }


        /// <summary>
        /// Tests if the appropriate exception (DbUpdateException) is thrown when a user is created with null fields
        /// </summary>
        [Fact]
        public async void AddUserAsync_ShouldThrowInvalidOperationIfFieldsAreNullOrEmpty()
        {
            var newUser = new UserRequestDto
            {
                Username = null,
                Password = "test123",
                Email = "user5@user5.com",
                Contact = "2973891",
                Location = "test",
                CareExperience = WebAPI.Entity.enums.ExperienceLevels.Beginner,
                WaterAvailability = WebAPI.Entity.enums.WaterLevels.Medium,
                LuminosityAvailability = WebAPI.Entity.enums.LightLevel.Medium
            };

            await Assert.ThrowsAsync<Microsoft.EntityFrameworkCore.DbUpdateException>(async () =>
            {
                await _userService.AddUserAsync(newUser);
            });

        }


        /// <summary>
        /// Tests whether the correct error is thrown when an email is invalid
        /// </summary>
        [Fact]
        public async void AddUserAsync_ShouldThrowArgumentException_ForInvalidEmail()
        {
            var newUser = new UserRequestDto
            {
                Username = "test",
                Password = "test123",
                Email = "invalid-email", // Invalid format
                Contact = "Test",
                Location = "test",
                CareExperience = ExperienceLevels.Beginner,
                WaterAvailability = WaterLevels.Medium,
                LuminosityAvailability = LightLevel.Medium
            };

            await Assert.ThrowsAsync<ArgumentException>(async () =>
            {
                await _userService.AddUserAsync(newUser);
            });
        }


        /// <summary>
        /// Tests whether the correct error is thrown when a password is too short
        /// </summary>
        [Fact]
        public async void AddUserAsync_ShouldThrowArgumentException_ForShortPassword()
        {
            var newUser = new UserRequestDto
            {
                Username = "test",
                Password = "short", // Too short
                Email = "test@gmail.com",
                Contact = "Test",
                Location = "test",
                CareExperience = ExperienceLevels.Beginner,
                WaterAvailability = WaterLevels.Medium,
                LuminosityAvailability = LightLevel.Medium
            };

            await Assert.ThrowsAsync<ArgumentException>(async () =>
            {
                await _userService.AddUserAsync(newUser);
            });
        }



        /// <summary>
        /// Tests whether a user is correctly updated when passed correct information
        /// </summary>
        [Fact]
        public async void UpdateUserAsync_ShouldCorrectlyUpdateUser()
        {

            var updatedId = 4;
            var updatedUserInformation = new UserRequestDto
            {
                Username = "updated",
                Password = "user123",
                Email = "user@user.com",
                Contact = "updated",
                Location = "updated",
                CareExperience = WebAPI.Entity.enums.ExperienceLevels.Beginner,
                WaterAvailability = WebAPI.Entity.enums.WaterLevels.Medium,
                LuminosityAvailability = WebAPI.Entity.enums.LightLevel.Medium
            };

            var success = await _userService.UpdateUserAsync(updatedId, updatedUserInformation);
            var updatedUserInDb = await _userService.GetUserByIdAsync(updatedId);
            Assert.Equal(updatedUserInDb.Username, updatedUserInformation.Username);
            Assert.True(success);

        }

        /// <summary>
        /// Tests whether the update method returns false when unseccessful
        /// </summary>
        [Fact]
        public async void UpdateUserAsync_ShouldReturnFalseWhenUserDoesNotExist()
        {
            var nonexistentId = 999;
            var updatedUserInformation = new UserRequestDto
            {
                Username = "non-existent",
                Password = "non-existent",
                Email = "nonexitent@email.com",
                Contact = "non-existent",
                Location = "non-existent",
                CareExperience = WebAPI.Entity.enums.ExperienceLevels.Beginner,
                WaterAvailability = WebAPI.Entity.enums.WaterLevels.Medium,
                LuminosityAvailability = WebAPI.Entity.enums.LightLevel.Medium
            };

            var unsuccess = await _userService.UpdateUserAsync(nonexistentId, updatedUserInformation);
            Assert.False(unsuccess);

        }


        /// <summary>
        /// Tests whether the update method returns false when unseccessful
        /// </summary>
        [Fact]
        public async void UpdateUserAsync_ShouldReturnFalseWhenIdIsZero()
        {
            var nonexistentId = 0;
            var updatedUserInformation = new UserRequestDto
            {
                Username = "non-existent",
                Password = "non-existent",
                Email = "nonexitent@email.com",
                Contact = "non-existent",
                Location = "non-existent",
                CareExperience = WebAPI.Entity.enums.ExperienceLevels.Beginner,
                WaterAvailability = WebAPI.Entity.enums.WaterLevels.Medium,
                LuminosityAvailability = WebAPI.Entity.enums.LightLevel.Medium
            };

            var unsuccess = await _userService.UpdateUserAsync(nonexistentId, updatedUserInformation);
            Assert.False(unsuccess);

        }


        /// <summary>
        /// Tests whether the update method returns false when unseccessful
        /// </summary>
        [Fact]
        public async void UpdateUserAsync_ShouldReturnFalseWhenIdIsNegative()
        {
            var nonexistentId = -1;
            var updatedUserInformation = new UserRequestDto
            {
                Username = "non-existent",
                Password = "non-existent",
                Email = "nonexitent@email.com",
                Contact = "non-existent",
                Location = "non-existent",
                CareExperience = WebAPI.Entity.enums.ExperienceLevels.Beginner,
                WaterAvailability = WebAPI.Entity.enums.WaterLevels.Medium,
                LuminosityAvailability = WebAPI.Entity.enums.LightLevel.Medium
            };

            var unsuccess = await _userService.UpdateUserAsync(nonexistentId, updatedUserInformation);
            Assert.False(unsuccess);

        }




        /// <summary>
        /// Tests whether deleting a user will return true if sucessful
        /// </summary>
        [Fact]
        public async void DeleteUserAsync_ShouldReturnTrueWhenUserExists()
        {

            var correctId = 4;
            var success = await _userService.DeleteUserAsync(correctId);
            Assert.Null(await _userService.GetUserByIdAsync(correctId));
            Assert.True(success);

        }

        /// <summary>
        /// Tests whether deleting a user will return false if unsucessful
        /// </summary>
        [Fact]
        public async void DeleteUserAsync_ShouldReturnFalseWhenUserNonExistent()
        {

            var incorrectId = 55;
            var insuccess = await _userService.DeleteUserAsync(incorrectId);
            Assert.False(insuccess);

        }

        /// <summary>
        /// Tests whether deleting a user will return false if unsucessful
        /// </summary>
        [Fact]
        public async void DeleteUserAsync_ShouldReturnFalseWhenUserIdisZero()
        {

            var incorrectId = 0;
            var insuccess = await _userService.DeleteUserAsync(incorrectId);
            Assert.False(insuccess);

        }

        /// <summary>
        /// Tests whether deleting a user will return false if unsucessful
        /// </summary>
        [Fact]
        public async void DeleteUserAsync_ShouldReturnFalseWhenUserIdisNegative()
        {

            var incorrectId = -1;
            var insuccess = await _userService.DeleteUserAsync(incorrectId);
            Assert.False(insuccess);

        }

        /// <summary>
        /// Tests whether the algorithm is correctly matching plants according to their characteristics and the user's
        /// </summary>
        [Fact]
        public async void MatchPlantsToUserAsync_ShouldProvideCorrectLists()
        {
            var correctId = 5;
            var user = await _userService.GetUserByIdAsync(correctId);
            var matches = await _userService.MatchPlantsToUserAsync(user);
            Assert.Equal(2, matches.perfectMatches.Count);
            Assert.Equal(5, matches.mediumMatches.Count);
            Assert.Empty(matches.noMatches);
            Assert.Equal(2, matches.weakMatches.Count);
        }

        /// <summary>
        /// Tests whether the algorithm returns empty lists when the user doesn't exist
        /// </summary>
        [Fact]
        public async void MatchPlantsToUserAsync_UserDoesntExist_ShouldProvidEmptyLists()
        {
            var correctId = 999;
            var user = await _userService.GetUserByIdAsync(correctId);
            var matches = await _userService.MatchPlantsToUserAsync(user);
            Assert.Empty(matches.perfectMatches);
            Assert.Empty(matches.mediumMatches);
            Assert.Empty(matches.noMatches);
            Assert.Empty(matches.weakMatches);
        }

    }
}
