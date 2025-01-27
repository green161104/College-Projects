using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Linq;
using System.Net;
using System.Resources;
using WebAPI.DTO.request;
using WebAPI.DTO.response;
using WebAPI.Entity;
using WebAPI.Services;

namespace WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AdminController : ControllerBase
    {
        private readonly AdminService _adminService;

        public AdminController(AdminService adminService)
        {
            _adminService = adminService;
        }

        [HttpGet]
        [Authorize(Policy = "AdminPolicy")] // Apenas admins podem aceder
        public async Task<ActionResult<List<AdminResponseDto>>> GetAllAdmins(
            [FromQuery] int _limit = 5,
            [FromQuery] int _page = 1,
            [FromQuery] string _sort = "Id",
            [FromQuery] string _order = "ASC")
        {
            try
            {
                // Calculate the start and end indices
                int start = (_page - 1) * _limit;
                int end = _limit * _page;

                var admins = await _adminService.GetAllAdminsAsync(start, end, _sort, _order);
                int totalAdmins = await _adminService.GetTotalAdminsCountAsync();

                //Response.Headers.Append("Access-Control-Expose-Headers", "Content-Range");
                //Response.Headers.Append("Content-Range", $"posts 0-{admins.Count}/{admins.Count}");

                if (admins == null || admins.Count == 0)
                    return NoContent();


                var adminDtos = admins.Select(admin => new AdminResponseDto
                {
                    Id = admin.Id,
                    Username = admin.Username,
                    Contact = admin.Contact
                }).ToList();

                return Ok(new
                {
                    data = adminDtos,
                    total = totalAdmins
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

        // GET: api/admin/{id}
        [HttpGet("{id}")]
        [Authorize(Policy = "AdminPolicy")] // Apenas admins podem aceder
        public async Task<ActionResult<AdminResponseDto>> GetAdminById(int id)
        {
            try
            {
                var admin = await _adminService.GetAdminByIdAsync(id);
                if (admin is null)
                    return NotFound(new ErrorResponseDto { error = "Admin not found" });

                return Ok(new AdminResponseDto
                { 
                    Id = admin.Id,
                    Username = admin.Username,
                    Contact = admin.Contact
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

        [HttpPost]
        //[Authorize(Policy = "AdminPolicy")] // Apenas admins podem aceder
        //[Authorize]
        public async Task<ActionResult<AdminResponseDto>> AddAdmin(AdminRequestDto adminDto)
        {
            // Verifica se os dados do admin são válidos
            if (adminDto == null)
                return BadRequest(new ErrorResponseDto { error = "Invalid data.", details = "Admin data cannot be null." });

            try
            {
                // Cria o admin apenas com os dados do perfil básico
                var createdAdmin = await _adminService.AddAdminAsync(adminDto);

                if (createdAdmin == null)
                {
                    return BadRequest(new ErrorResponseDto
                    {
                        error = "Error while creating the Admin",
                        details = "Ensure all fields are filled correctly"
                    });
                }

                var adminResponse = new AdminResponseDto
                {
                    Id = createdAdmin.Id,
                    Username = createdAdmin.Username,
                    Contact = createdAdmin.Contact
                };

                // Retorna o admin recém-criado
                return CreatedAtAction(nameof(GetAdminById), new { id = createdAdmin.Id }, adminResponse);
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }     
        }

        [HttpPut("{id}")]
        [Authorize(Policy = "AdminPolicy")] // Apenas admins podem aceder
        public async Task<ActionResult<AdminResponseDto>> UpdateAdmin(int id, UpdateAdminDto adminDto)
        {
            // Verifica se os dados do admin são válidos
            if (adminDto == null)
                return BadRequest(new ErrorResponseDto { error = "Invalid data.", details = "Admin data cannot be null." });

            try
            {
                var updatedAdmin = await _adminService.UpdateAdminAsync(id, adminDto);

                if (updatedAdmin == null)
                    return NotFound();

                AdminResponseDto adminRespone = new AdminResponseDto
                {
                    Id = updatedAdmin.Id,
                    Username = updatedAdmin.Username,
                    Contact = updatedAdmin.Contact
                };

                return Ok(adminRespone);
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }

        // DELETE: api/admin/{id}
        [HttpDelete("{id}")]
        [Authorize(Policy = "AdminPolicy")] // Apenas admins podem aceder
        public async Task<ActionResult> DeleteAdmin(int id)
        {
            try
            {
                var deleteSuccessful = await _adminService.DeleteAdminAsync(id);

                if (!deleteSuccessful)
                    return NotFound(new ErrorResponseDto { error = "Admin not found" });

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
