using Microsoft.EntityFrameworkCore;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.Entity;

namespace WebAPI.Services
{
    public class LogService
    {
        private readonly DataContext _context;

        public LogService(DataContext context)
        {
            _context = context;
        }

        // Get all logs
        /// <summary>
        /// Asynchronous method to retrieve all records from the Log table in the database.
        /// </summary>
        /// <returns>
        /// An IEnumerable collection of <see cref="Log"/> objects including related Diary entries.
        /// </returns>
        public async Task<List<Log>> GetAllLogsAsync()
        {
            return await _context.Log.Include(l => l.Diary).ToListAsync();
        }

        // Get all logs for a specific diary
        /// <summary>
        /// Asynchronous method to retrieve all log records associated with a specific Diary.
        /// </summary>
        /// <param name="diaryId">ID of the Diary associated with the logs to be retrieved.</param>
        /// <returns>
        /// An IEnumerable collection of <see cref="Log"/> objects filtered by the specified Diary ID.
        /// </returns>
        public async Task<List<Log>> GetLogsByDiaryIdAsync(int diaryId)
        {
            return await _context.Log
                .Where(l => l.DiaryId == diaryId)
                .ToListAsync();
        }

        // Get log by ID
        /// <summary>
        /// Asynchronous method to retrieve a specific log record from the Log table based on its ID.
        /// </summary>
        /// <param name="id">ID of the log record to be retrieved.</param>
        /// <returns>
        /// A <see cref="Log"/> object representing the log associated with the specified ID, including its related Diary entry, or null if not found.
        /// </returns>
        public async Task<Log?> GetLogByIdAsync(int id)
        {
            return await _context.Log.Include(l => l.Diary)
                .FirstOrDefaultAsync(l => l.Id == id);
        }

        // Add a new log
        /// <summary>
        /// Asynchronous method to add a new record to the Log table in the database.
        /// </summary>
        /// <param name="logDto">DTO containing the data to create a new log record.</param>
        /// <returns>
        /// The newly created <see cref="Log"/> object with populated properties, including its generated ID, or null if the specified Diary does not exist.
        /// </returns>
        public async Task<Log?> AddLogAsync(LogRequestDto logDto)
        {
            // Verifica se o diário existe
            var diaryExists = await _context.Diary.AnyAsync(d => d.Id == logDto.DiaryId);
            if (!diaryExists) return null;

            var newLog = new Log
            {
                DiaryId = logDto.DiaryId,
                LogDate = DateTime.Now,
                LogDescription = logDto.LogDescription
            };

            _context.Log.Add(newLog);
            await _context.SaveChangesAsync();

            return newLog;
        }

        // Update an existing log
        /// <summary>
        /// Asynchronous method to update an existing log record in the Log table in the database.
        /// </summary>
        /// <param name="id">ID of the log record to be updated.</param>
        /// <param name="logDto">DTO containing the new data to update the log record.</param>
        /// <returns>
        /// A boolean indicating if the update was successful (true) or if the log record was not found (false).
        /// </returns>
        public async Task<bool> UpdateLogAsync(int id, LogRequestDto logDto)
        {
            var existingLog = await _context.Log.FindAsync(id);
            if (existingLog == null) return false;

            existingLog.LogDescription = logDto.LogDescription;

            await _context.SaveChangesAsync();
            return true;
        }

        // Delete a log
        /// <summary>
        /// Asynchronous method to delete a log record from the Log table in the database.
        /// </summary>
        /// <param name="id">ID of the log record to be deleted.</param>
        /// <returns>
        /// A boolean indicating if the deletion was successful (true) or if the log record was not found (false).
        /// </returns>
        public async Task<bool> DeleteLogAsync(int id)
        {
            var log = await _context.Log.FindAsync(id);
            if (log == null) return false;

            _context.Log.Remove(log);
            await _context.SaveChangesAsync();

            return true;
        }


    }
}
