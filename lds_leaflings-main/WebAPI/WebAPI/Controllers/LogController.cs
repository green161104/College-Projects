using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Net;
using WebAPI.DTO.request;
using WebAPI.DTO.response;
using WebAPI.Entity;
using WebAPI.Services;

namespace WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class LogController : ControllerBase
    {
        private readonly LogService _logService;

        public LogController(LogService logService)
        {
            _logService = logService;
        }

        // GET: api/Log
        /// <summary>
        /// Asynchronous method to retrieve all log entries from the database.
        /// </summary>
        /// <returns>
        /// An ActionResult containing an IEnumerable of Log objects.
        /// </returns>
        [HttpGet]
        [Authorize]
        public async Task<ActionResult<IEnumerable<Log>>> GetAllLogs()
        {
            try
            {
                var logs = await _logService.GetAllLogsAsync();

                if (logs == null || logs.Count == 0)
                    return NoContent();

                return Ok(new
                {
                    data = logs,
                    total = logs.Count
                });
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }

        // GET: api/Log/{id}
        /// <summary>
        /// Asynchronous method to retrieve a specific log entry by its ID.
        /// </summary>
        /// <param name="id">The ID of the log entry to retrieve.</param>
        /// <returns>
        /// An ActionResult containing the requested Log object or a NotFound result if it does not exist.
        /// </returns>
        [HttpGet("{id}")]
        [Authorize]
        public async Task<ActionResult<Log>> GetLogById(int id)
        {
            try
            {
                var log = await _logService.GetLogByIdAsync(id);
                if (log == null)
                {
                    return NotFound(new ErrorResponseDto { error = "Log not Found"});
                }

                return Ok(log);
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }

        // GET: api/Log/diary/{diaryId}
        /// <summary>
        /// Asynchronous method to retrieve all log entries associated with a specific Diary.
        /// </summary>
        /// <param name="diaryId">The Diary ID associated with the logs to retrieve.</param>
        /// <returns>
        /// An ActionResult containing an IEnumerable of Log objects or a NotFound result if no entries are found.
        /// </returns>
        [HttpGet("diary/{diaryId}")]
        [Authorize]
        public async Task<ActionResult<IEnumerable<Log>>> GetLogsByDiaryId(int diaryId)
        {
            try
            {
                var logs = await _logService.GetLogsByDiaryIdAsync(diaryId);
                if (logs == null || !logs.Any())
                {
                    return NotFound(new ErrorResponseDto { error = "No logs found for this DiaryId." });    
                }

                return Ok(logs);
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }

        // POST: api/Log
        /// <summary>
        /// Asynchronous method to create a new log entry.
        /// </summary>
        /// <param name="logDto">The data transfer object containing information for the new log entry.</param>
        /// <returns>
        /// The newly created Log object or a BadRequest result if the data is invalid or creation fails.
        /// </returns>
        [HttpPost]
        [Authorize]
        public async Task<ActionResult<Log>> CreateLog(LogRequestDto logDto)
        {
            if (logDto == null)
                return BadRequest(new ErrorResponseDto { error = "Invalid Data" });

            try
            {
                var newLog = await _logService.AddLogAsync(logDto);
                if (newLog == null)
                {
                    return BadRequest(new ErrorResponseDto { error = "Diary not found, unable to create log" });
                }

                return CreatedAtAction(nameof(GetLogById), new { id = newLog.Id }, newLog);
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }

        // PUT: api/Log/{id}
        /// <summary>
        /// Asynchronous method to update an existing log entry.
        /// </summary>
        /// <param name="id">The ID of the log entry to update.</param>
        /// <param name="logDto">The DTO containing updated information for the log entry.</param>
        /// <returns>
        /// A NoContent result if successful, or NotFound if the entry does not exist or cannot be updated.
        /// </returns>
        [HttpPut("{id}")]
        [Authorize]
        public async Task<ActionResult<Log>> UpdateLog(int id, LogRequestDto logDto)
        {
            if (logDto == null)
                return BadRequest(new ErrorResponseDto { error = "Invalid data." });

            try
            {
                var updated = await _logService.UpdateLogAsync(id, logDto);
                if (!updated)
                {
                    return NotFound(new ErrorResponseDto { error = "Log not found or unable to update." });
                }

                return Ok(updated);
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }

        // DELETE: api/Log/{id}
        /// <summary>
        /// Asynchronous method to delete a log entry by its ID.
        /// </summary>
        /// <param name="id">The ID of the log entry to delete.</param>
        /// <returns>
        /// NoContent result if successful, or NotFound if the entry does not exist or cannot be deleted.
        /// </returns>
        [HttpDelete("{id}")]
        [Authorize]
        public async Task<ActionResult> DeleteLog(int id)
        {
            try
            {
                var deleted = await _logService.DeleteLogAsync(id);
                if (!deleted)
                {
                    return NotFound(new ErrorResponseDto { error = "Log not found or unable to delete." });
                }

                return NoContent();
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }
    }
}
