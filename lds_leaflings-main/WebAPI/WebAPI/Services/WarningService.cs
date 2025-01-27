using Microsoft.EntityFrameworkCore;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.Entity;

namespace WebAPI.Services
{
    public class WarningService
    {
        private readonly DataContext _context;

        public WarningService(DataContext context)
        {
            _context = context;
        }

        /// <summary>
        /// Retrieves all warnings associated with a specific user.
        /// </summary>
        /// <param name="userId">The ID of the user whose warnings are to be retrieved.</param>
        /// <returns>A list of Warning objects.</returns>
        public async Task<List<Warning>> GetAllWarningsFromUserAsync(int userId)
        {
            return await _context.Warning.Where(u => u.UserId == userId).ToListAsync();
        }

        /// <summary>
        /// Retrieves a warning by its ID.
        /// </summary>
        /// <param name="id">The ID of the warning to retrieve.</param>
        /// <returns>The Warning object if found; otherwise, null.</returns>
        public async Task<Warning?> GetWarningIdAsync(int id)
        {
            //return await _context.Users.FindAsync(id);
            return await _context.Warning.FirstOrDefaultAsync(u => u.Id == id);
        }

        /// <summary>
        /// Adds a new warning based on the provided WarningDto.
        /// </summary>
        /// <param name="warningDto">The WarningDto object containing the warning details.</param>
        /// <returns>The newly created Warning object.</returns>
        public async Task<Warning> AddWarningAsync(WarningRequestDto warningDto)
        {
            var newWarning = new Warning
            {
                UserId = warningDto.UserId,
                Location = warningDto.Location,
                Message = warningDto.Message
            };

            _context.Warning.Add(newWarning);
            await _context.SaveChangesAsync();

            return newWarning;
        }

        /// <summary>
        /// Updates an existing warning with new details.
        /// </summary>
        /// <param name="id">The ID of the warning to update.</param>
        /// <param name="warningDto">The WarningDto object containing the updated warning details.</param>
        /// <returns>True if the update was successful; otherwise, false.</returns>
        public async Task<bool> UpdateWarningAsync(int id, WarningRequestDto warningDto)
        {
            var existingWarning = await _context.Warning.FindAsync(id);

            if (existingWarning == null) return false;

            existingWarning.UserId = warningDto.UserId;
            existingWarning.Location = warningDto.Location;
            existingWarning.Message = warningDto.Message;
           
            await _context.SaveChangesAsync();

            return true;
        }

        /// <summary>
        /// Deletes a warning by its ID.
        /// </summary>
        /// <param name="id">The ID of the warning to delete.</param>
        /// <returns>True if the deletion was successful; otherwise, false.</returns>
        public async Task<bool> DeleteWarningAsync(int id)
        {
            var warning = await _context.Warning.FindAsync(id);
            if (warning == null) return false;

            _context.Warning.Remove(warning);
            await _context.SaveChangesAsync();

            return true;
        }
    }
}
