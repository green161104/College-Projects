using Microsoft.EntityFrameworkCore;
using WebAPI.Data;
using WebAPI.Entity;
using WebAPI.Utils;
using WebAPI.DTO.request;
using System.ComponentModel.DataAnnotations;
using Microsoft.Data.SqlClient;
using System.Reflection;

namespace WebAPI.Services
{
    public class UserService
    {
        private readonly DataContext _context;
        private readonly Cryptography _cryptography;
        private readonly Images _images;

        public UserService(DataContext context, Cryptography cryptography)
        {
            _context = context;
            _cryptography = cryptography;
        }

        public UserService(DataContext context, Cryptography cryptography, IWebHostEnvironment env)
        {
            _context = context;
            _cryptography = cryptography;
            _images = new Images(env);
        }

        /// <summary>
        /// Returns all users in the database
        /// </summary>
        /// <returns>A list of users</returns>
        public async Task<List<User>> GetAllUsersAsync(int start = 0, int end = 10, string sortField = "id", string sortOrder = "ASC")
        {
            sortField = char.ToUpper(sortField[0]) + sortField.Substring(1); // Capitalize the sortField
            sortOrder = sortOrder.ToUpper(); // Convert sortOrder to uppercase

            bool sortFieldExists = SortFieldExists(sortField);

            IQueryable<User> query = _context.User
                .Skip(start)
                .Take((end - start) + 1);


            // apply sort by the sortFild
            if (sortOrder == "ASC" && sortFieldExists)
            {
                query = query.OrderBy(p => EF.Property<object>(p, sortField));
            }
            else if (sortOrder == "DESC" && sortFieldExists)
            {
                query = query.OrderByDescending(p => EF.Property<object>(p, sortField));
            }


            return await query.ToListAsync();
        }

        /// <summary>
        /// Fetches a specific user by their id
        /// </summary>
        /// <param name="id">Id of the user to be found</param>
        /// <returns>The user if found, null if not</returns>
        public async Task<User?> GetUserByIdAsync(int id)
        {
            if (id < 1) { return null; }
            try
            {
                //return await _context.Users.FindAsync(id);
                User user = await _context.User.FirstOrDefaultAsync(u => u.Id == id);
                return user;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        /// <summary>
        /// Persists a new user in the database
        /// </summary>
        /// <param name="userDto">User data transfer object representing the new user</param>
        /// <returns>the new user that has been created</returns>
        /// <exception cref="InvalidOperationException">If an email is provided that is already in use</exception>
        public async Task<User> AddUserAsync(UserRequestDto userDto)
        {
            // Email format validation

            if (!new EmailAddressAttribute().IsValid(userDto.Email))
            {
                throw new ArgumentException("Invalid email format.");
            }

            // Password length validation
            if (string.IsNullOrWhiteSpace(userDto.Password) || userDto.Password.Length < 6)
            {
                throw new ArgumentException("Password must be at least 6 characters long.");
            }

            // Check if email is already in use
            var existingUser = await _context.User.FirstOrDefaultAsync(u => u.Email == _cryptography.HashString(userDto.Email));
            if (existingUser != null)
            {
                throw new InvalidOperationException("Email already in use.");
            }

            // Guarda a imagem no servidor
            String imagePath = "";
            if (userDto.UserAvatar != null)
            {
                imagePath = await _images.SaveImageAsync(userDto.UserAvatar);

                if (imagePath == null)
                    throw new Exception("Image upload failed.");
            }

            var newUser = new User
            {
                Username = userDto.Username,
                Email = _cryptography.HashString(userDto.Email),
                Password = _cryptography.HashString(userDto.Password),
                Contact = userDto.Contact,
                Location = userDto.Location,
                CareExperience = userDto.CareExperience,
                WaterAvailability = userDto.WaterAvailability,
                LuminosityAvailability = userDto.LuminosityAvailability,
                UserAvatar = imagePath
            };

            _context.User.Add(newUser);
            await _context.SaveChangesAsync();

            return newUser;
        }

        /// <summary>
        /// Updates a specific user according to the provided information
        /// </summary>
        /// <param name="id">Unique identifier of the user to be updated</param>
        /// <param name="userDto">Object containing the updated information</param>
        /// <returns>True of successful, false if not</returns>
        public async Task<bool> UpdateUserAsync(int id, UserRequestDto userDto)
        {
            var existingUser = await _context.User.FindAsync(id);
            if (existingUser == null) return false;


            /* if (!new EmailAddressAttribute().IsValid(userDto.Email))
             {
                 throw new ArgumentException("Invalid email format.");
             }*/

            // Password length validation
            if (string.IsNullOrWhiteSpace(userDto.Password) || userDto.Password.Length < 6)
            {
                throw new ArgumentException("Password must be at least 6 characters long.");
            }

            // Check if email is already in use
            /* var existingEmail = await _context.User.FirstOrDefaultAsync(u => u.Email == _cryptography.HashString(userDto.Email));
             if (existingEmail != null && existingEmail.Email != existingUser.Email)
             {
                 throw new InvalidOperationException("Email already in use.");
             }*/

            // Store the current image path before potentially updating
            string oldImagePath = existingUser.UserAvatar;

            // Se uma nova imagem for fornecida, guarda-a
            if (userDto.UserAvatar != null)
            {
                var imagePath = await _images.SaveImageAsync(userDto.UserAvatar);

                if (imagePath == null)
                    throw new Exception("Image upload failed.");

                // Atualiza o caminho da imagem
                existingUser.UserAvatar = imagePath;

                // Delete the old image if it exists
                if (!string.IsNullOrEmpty(oldImagePath))
                {
                    // Extract just the filename from the full path
                    string oldFileName = Path.GetFileName(oldImagePath);
                    _images.DeleteImage(oldFileName);
                }

            }

            existingUser.Username = userDto.Username;
            existingUser.Email = _cryptography.HashString(userDto.Email);
            existingUser.Contact = userDto.Contact;
            existingUser.Location = userDto.Location;
            existingUser.CareExperience = userDto.CareExperience;
            existingUser.WaterAvailability = userDto.WaterAvailability;
            existingUser.LuminosityAvailability = userDto.LuminosityAvailability;

            await _context.SaveChangesAsync();

            return true;
        }

        /// <summary>
        /// Updates a specific user preferences according to the provided information
        /// </summary>
        /// <param name="id">Unique identifier of the user that preferences will be updated</param>
        /// <param name="preferencesDto">Object containing the updated preferences information</param>
        /// <returns>True of successful, false if not</returns>
        public async Task<bool> UpdateUserPreferencesAsync(int id, UserPreferencesRequestDto preferencesDto)
        {
            var existingUser = await _context.User.FindAsync(id);
            if (existingUser == null) return false;


            existingUser.CareExperience = preferencesDto.CareExperience;
            existingUser.WaterAvailability = preferencesDto.WaterAvailability;
            existingUser.LuminosityAvailability = preferencesDto.LuminosityAvailability;

            await _context.SaveChangesAsync();

            return true;
        }

        /// <summary>
        /// Updates only the password for a specific user
        /// </summary>
        /// <param name="id">Unique identifier of the user</param>
        /// <param name="oldPassword">Current password for verification</param>
        /// <param name="newPassword">New password to be set</param>
        /// <returns>True if successful, false if not</returns>
        /// <exception cref="ArgumentException">If the password format is invalid</exception>
        /// <exception cref="InvalidOperationException">If the old password is incorrect</exception>
        public async Task<bool> UpdatePasswordAsync(int id, string oldPassword, string newPassword)
        {
            var existingUser = await _context.User.FindAsync(id);
            if (existingUser == null) return false;

            // Verify old password
            if (existingUser.Password != _cryptography.HashString(oldPassword))
            {
                throw new InvalidOperationException("Current password is incorrect.");
            }

            // Password length validation
            if (string.IsNullOrWhiteSpace(newPassword) || newPassword.Length < 6)
            {
                throw new ArgumentException("New password must be at least 6 characters long.");
            }

            // Update password
            existingUser.Password = _cryptography.HashString(newPassword);
            await _context.SaveChangesAsync();

            return true;
        }

        public async Task<User?> UpdateUserInformation(int id, string newUsername, string location, string contact)
        {
            var existingUser = await _context.User.FindAsync(id);
            if (existingUser == null) return null;

            // Password length validation
            if (string.IsNullOrWhiteSpace(newUsername) | string.IsNullOrWhiteSpace(location) | string.IsNullOrWhiteSpace(contact))
            {
                throw new ArgumentException("New data can't be empty!");
            }

            // Update password
            existingUser.Username = newUsername;
            existingUser.Location = location;
            existingUser.Contact = contact;
            await _context.SaveChangesAsync();

            return existingUser;
        }


        /// <summary>
        /// Updates only the user's avatar image
        /// </summary>
        /// <param name="id">Unique identifier of the user</param>
        /// <param name="newImage">New image file to be uploaded</param>
        /// <returns>True if successful, false if not</returns>
        /// <exception cref="Exception">If image upload fails</exception>
        public async Task<bool> UpdateImageAsync(int id, IFormFile newImage)
        {
            var existingUser = await _context.User.FindAsync(id);
            if (existingUser == null) return false;

            // Store the current image path before updating
            string oldImagePath = existingUser.UserAvatar;

            // Save new image
            var imagePath = await _images.SaveImageAsync(newImage);
            if (imagePath == null)
            {
                throw new Exception("Image upload failed.");
            }

            // Update the image path
            existingUser.UserAvatar = imagePath;

            // Delete the old image if it exists
            if (!string.IsNullOrEmpty(oldImagePath))
            {
                string oldFileName = Path.GetFileName(oldImagePath);
                _images.DeleteImage(oldFileName);
            }

            await _context.SaveChangesAsync();
            return true;
        }


        /// <summary>
        /// Deletes a user from the databse
        /// </summary>
        /// <param name="id">Identifier of the user to be deleted</param>
        /// <returns>True if sucessfull, null if not</returns>
        public async Task<bool> DeleteUserAsync(int id)
        {
            var user = await _context.User.FindAsync(id);
            if (user == null) return false;

            // Delete the old image if it exists
            string oldImagePath = user.UserAvatar;
            if (!string.IsNullOrEmpty(oldImagePath))
            {
                // Extract just the filename from the full path
                string oldFileName = Path.GetFileName(oldImagePath);
                _images.DeleteImage(oldFileName);
            }

            _context.User.Remove(user);
            await _context.SaveChangesAsync();

            return true;
        }

        /// <summary>
        /// Matches plants to users according to eachother's characteristics
        /// </summary>
        /// <param name="user">User whose characteristics will be matched</param>
        /// <returns>The lists of plants, categorized by how good of a match they make to the user</returns>
        public async Task<(List<Plant> perfectMatches, List<Plant> mediumMatches, List<Plant> weakMatches, List<Plant> noMatches)> MatchPlantsToUserAsync(User user)
        {


            // Get all plants from the database
            var allPlants = await _context.Plant.ToListAsync();

            var perfectMatches = new List<Plant>();
            var mediumMatches = new List<Plant>();
            var weakMatches = new List<Plant>();
            var noMatches = new List<Plant>();

            if (user == null)
            {
                return (perfectMatches, mediumMatches, weakMatches, noMatches);
            }

            foreach (var plant in allPlants)
            {
                // Count matching characteristics
                int matchCount = 0;

                // Check experience level match
                if (user.CareExperience >= plant.ExpSuggested)
                    matchCount++;

                // Check water availability match
                if (user.WaterAvailability >= plant.WaterNeeds)
                    matchCount++;

                // Check luminosity match
                if (user.LuminosityAvailability >= plant.LuminosityNeeded)
                    matchCount++;

                // Categorize plant based on match count
                switch (matchCount)
                {
                    case 3:
                        perfectMatches.Add(plant);
                        break;
                    case 2:
                        mediumMatches.Add(plant);
                        break;
                    case 1:
                        weakMatches.Add(plant);
                        break;
                    default:
                        // No matches, don't add to any list
                        noMatches.Add(plant);
                        break;
                }
            }

            return (perfectMatches, mediumMatches, weakMatches, noMatches);
        }

        public async Task<int> GetTotalUsersCountAsync()
        {
            return await _context.User.CountAsync();
        }


        private bool SortFieldExists(string sortField)
        {
            // Use BindingFlags to perform a case-insensitive search for the property
            PropertyInfo? property = typeof(User).GetProperty(sortField, BindingFlags.IgnoreCase | BindingFlags.Public | BindingFlags.Instance);
            return property != null;
        }

    }
}
