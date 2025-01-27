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
    public class DiaryController : ControllerBase
    {
        private readonly DiaryService _diaryService;

        public DiaryController(DiaryService diaryService)
        {
            _diaryService = diaryService;
        }


        // GET: api/Diary
        /// <summary>
        /// Asynchronous method to retrieve all diary entries from the database.
        /// </summary>
        /// <returns>
        /// An ActionResult containing an IEnumerable of Diary objects.
        /// </returns>
        [HttpGet]
        [Authorize]
        public async Task<ActionResult<IEnumerable<Diary>>> GetAllDiaries()
        {
            try
            {
                var diaries = await _diaryService.GetAllDiariesAsync();

                if (diaries == null || diaries.Count == 0)
                    return NoContent();

                var diarieDto = diaries.Select(d => new DiaryResponseDto
                {
                    Id = d.Id,
                    UserPlantId = d.UserPlantId,
                    Title = d.Title,
                    CreationDate = d.CreationDate.ToString("yyyy-MM-dd")
                }).ToList();

                return Ok(new
                {
                    data = diarieDto,
                    total = diarieDto.Count
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

        // GET: api/Diary/{id}
        /// <summary>
        /// Asynchronous method to retrieve a specific diary entry by its ID.
        /// </summary>
        /// <param name="id">The ID of the diary entry to retrieve.</param>
        /// <returns>
        /// An ActionResult containing the requested Diary object or a NotFound result if it does not exist.
        /// </returns>
        [HttpGet("{id}")]
        [Authorize]
        public async Task<ActionResult<Diary>> GetDiaryById(int id)
        {

            try
            {
                var diary = await _diaryService.GetDiaryByIdAsync(id);
                if (diary == null)
                {
                    return NotFound(new ErrorResponseDto { error = "Diary not Found"});
                }

                return Ok(diary);
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }    
        }

        // GET: api/Diary/userPlant/{userPlantId}
        /// <summary>
        /// Asynchronous method to retrieve all diary entries associated with a specific UserPlant.
        /// </summary>
        /// <param name="userPlantId">The UserPlant ID associated with the diaries to retrieve.</param>
        /// <returns>
        /// An ActionResult containing an IEnumerable of Diary objects or a NotFound result if no entries are found.
        /// </returns>
        [HttpGet("userPlant/{userPlantId}")]
        [Authorize]
        public async Task<ActionResult<IEnumerable<Diary>>> GetDiarieByUserPlantId(int userPlantId)
        {

            try
            {
                var diarie = await _diaryService.GetDiarieByUserPlantIdAsync(userPlantId);
                if (diarie == null)
                {
                    return NotFound(new ErrorResponseDto { error = "No diaries found for this UserPlantId." });
                }


                var diarieDto = new DiaryResponseDto
                {
                    Id = diarie.Id,
                    UserPlantId = diarie.UserPlantId,
                    Title = diarie.Title,
                    CreationDate = diarie.CreationDate.ToString("yyyy-MM-dd")
                };


                return Ok(diarieDto);
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }

        // POST: api/Diary
        /// <summary>
        /// Asynchronous method to create a new diary entry.
        /// </summary>
        /// <param name="diaryDto">The data transfer object containing information for the new diary entry.</param>
        /// <returns>
        /// The newly created Diary object or a BadRequest result if the data is invalid or creation fails.
        /// </returns>
        [HttpPost]
        [Authorize]
        public async Task<ActionResult<Diary>> CreateDiary(DiaryRequestDto diaryDto)
        {

            // Verifica se os dados do admin são válidos
            if (diaryDto == null)
                return BadRequest(new ErrorResponseDto { error = "Invalid Data" });

            try
            {
                var newDiary = await _diaryService.AddDiaryAsync(diaryDto);

                if (newDiary == null)
                {
                    return BadRequest(new ErrorResponseDto { error = "Unable to create the diary entry." });
                }

                return CreatedAtAction(nameof(GetDiaryById), new { id = newDiary.Id }, newDiary);
            }
            catch (InvalidOperationException ex)
            {
                return Conflict(new { Message = ex.Message }); // Retorna 409 Conflict
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {   
                    error = $"{e.Message}"
                });
            }
        }

        // PUT: api/Diary/{id}
        /// <summary>
        /// Asynchronous method to update an existing diary entry.
        /// </summary>
        /// <param name="id">The ID of the diary entry to update.</param>
        /// <param name="diaryDto">The DTO containing updated information for the diary entry.</param>
        /// <returns>
        /// An Ok result if successful, or NotFound if the entry does not exist or cannot be updated.
        /// </returns>
        [HttpPut("{id}")]
        [Authorize]
        public async Task<ActionResult<Diary>> UpdateDiary(int id, DiaryRequestDto diaryDto)
        {
            // Verifica se os dados do admin são válidos
            if (diaryDto == null)
                return BadRequest(new ErrorResponseDto { error = "Invalid Data" });

            try
            {
                var updated = await _diaryService.UpdateDiaryAsync(id, diaryDto);
                if (!updated)
                {
                    return NotFound(new ErrorResponseDto { error = "Diary not found or unable to update." });
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

        // DELETE: api/Diary/{id}
        /// <summary>
        /// Asynchronous method to delete a diary entry by its ID.
        /// </summary>
        /// <param name="id">The ID of the diary entry to delete.</param>
        /// <returns>
        /// NoContent result if successful, or NotFound if the entry does not exist or cannot be deleted.
        /// </returns>
        [HttpDelete("{id}")]
        [Authorize]
        public async Task<ActionResult> DeleteDiary(int id)
        {

            try
            {
                var deleted = await _diaryService.DeleteDiaryAsync(id);
                if (!deleted)
                {
                    return NotFound(new ErrorResponseDto { error = "Diary not found or unable to delete." });
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
