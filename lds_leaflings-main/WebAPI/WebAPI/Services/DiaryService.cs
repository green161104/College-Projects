using Microsoft.EntityFrameworkCore;
using WebAPI.Data;
using WebAPI.Entity;
using WebAPI.DTO.request;
using WebAPI.DTO.response;

namespace WebAPI.Services
{
    public class DiaryService
    {
        private readonly DataContext _context;

        public DiaryService(DataContext context)
        {
            _context = context;
        }

        // Get all diaries
        /// <summary>
        /// Asynchronous method to retrieve all records from the Diary table in the database.
        /// </summary>
        /// <returns>
        /// A list of <see cref="Diary"/> objects containing all data stored in the Diary table.
        /// </returns>
        /// <remarks>
        /// This method performs a query on the Diary table (_context.Diary) and returns all records as an IEnumerable collection of <see cref="Diary"/>.
        /// The query is executed asynchronously, using the <see cref="ToListAsync"/> method to avoid blocking the execution thread.
        /// </remarks>
        public async Task<List<Diary>> GetAllDiariesAsync()
        {
            return await _context.Diary.ToListAsync();
        }

        // Get all diaries for a specific UserPlant
        /// <summary>
        /// Asynchronous method to retrieve the diary of a specific UserPlant.
        /// </summary>
        /// <param name="userPlantId">ID of the UserPlant associated with the diaries to be retrieved.</param>
        /// <returns>
        /// An IEnumerable collection of <see cref="Diary"/> objects filtered by the specified UserPlant ID.
        /// </returns>
        public async Task<Diary?> GetDiarieByUserPlantIdAsync(int userPlantId)
        {
            return await _context.Diary
                .FirstOrDefaultAsync(d => d.UserPlantId == userPlantId);
        }

        // Get a specific diary by ID
        /// <summary>
        /// Asynchronous method to retrieve a specific record from the Diary table in the database based on its ID.
        /// </summary>
        /// <param name="id">ID of the diary record to be retrieved.</param>
        /// <returns>
        /// A <see cref="Diary"/> object representing the diary associated with the specified ID, or null if not found.
        /// </returns>
        public async Task<Diary?> GetDiaryByIdAsync(int id)
        {
            return await _context.Diary.FirstOrDefaultAsync(d => d.Id == id);
        }

        // Add a new diary entry
        /// <summary>
        /// Asynchronous method to add a new record to the Diary table in the database.
        /// </summary>
        /// <param name="diaryDto">DTO containing the data to create a new diary record.</param>
        /// <returns>
        /// The newly created <see cref="Diary"/> object with populated properties, including its generated ID.
        /// </returns>
        /// <exception cref="InvalidOperationException">
        /// Thrown when a diary entry for the specified <c>UserPlantId</c> already exists.
        /// This method does not allow multiple diary entries for the same plant under the same user.
        /// </exception>
        public async Task<Diary> AddDiaryAsync(DiaryRequestDto diaryDto)
        {
            // Verifica se já existe um diário para o mesmo UserPlantId
            var existingDiary = await _context.Diary
                .FirstOrDefaultAsync(d => d.UserPlantId == diaryDto.UserPlantId);

            var userPlantExists = await _context.UserPlant.AnyAsync(up => up.Id == diaryDto.UserPlantId);
            if (!userPlantExists)
            {
                throw new InvalidOperationException("This plant does not exist.");
            }

            if (existingDiary != null)
            {
                throw new InvalidOperationException("A diary entry already exists for this UserPlant.");
            }

            var newDiary = new Diary
            {
                UserPlantId = diaryDto.UserPlantId,
                Title = diaryDto.Title
            };

            _context.Diary.Add(newDiary);
            await _context.SaveChangesAsync();

            return newDiary;
        }

        // Update an existing diary entry
        /// <summary>
        /// Asynchronous method to update an existing record in the Diary table in the database.
        /// </summary>
        /// <param name="id">ID of the diary record to be updated.</param>
        /// <param name="diaryDto">DTO containing the new data to update the diary record.</param>
        /// <returns>
        /// A boolean indicating if the update was successful (true) or if the diary record was not found (false).
        /// </returns>
        public async Task<bool> UpdateDiaryAsync(int id, DiaryRequestDto diaryDto)
        {
            var existingDiary = await _context.Diary.FindAsync(id);

            if (existingDiary == null) return false;

            existingDiary.Title = diaryDto.Title;

            await _context.SaveChangesAsync();

            return true;
        }

        // Deletes a record in the Diary table
        /// <summary>
        /// Asynchronous method to delete a record from the Diary table in the database.
        /// </summary>
        /// <param name="id">ID of the diary record to be deleted.</param>
        /// <returns>
        /// A boolean indicating if the deletion was successful (true) or if the diary record was not found (false).
        /// </returns>
        public async Task<bool> DeleteDiaryAsync(int id)
        {
            var diary = await _context.Diary.FindAsync(id);
            if (diary == null) return false;

            _context.Diary.Remove(diary);
            await _context.SaveChangesAsync();

            return true;
        }
    }
}
