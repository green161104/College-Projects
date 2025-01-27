using Microsoft.AspNetCore.Mvc;
using WebAPI.Entity;
using WebAPI.Data;
using System.Net;
using WebAPI.Utils;
using WebAPI.DTO.response;
using System.Security.Claims;
using WebAPI.Services;
using WebAPI.DTO.request; // Adicione esta linha para incluir o contexto


namespace WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthController : ControllerBase
    {
        private readonly AuthService _authService;
        private readonly UserService _userService;
        private readonly AdminService _adminService;

        public AuthController(DataContext context, IConfiguration configuration, Cryptography cryptography)
        {
            _authService = new AuthService(context, configuration, cryptography);
            _userService = new UserService(context, cryptography);
            _adminService = new AdminService(context, cryptography);
        }

        /// <summary>
        /// Handles the login process by authenticating a user based on the provided login model.
        /// </summary>
        /// <param name="model">The login model containing user credentials.</param>
        /// <returns>An IActionResult indicating the result of the login attempt.</returns>
        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginModel model)
        {

            if (model == null || string.IsNullOrEmpty(model.Email) || string.IsNullOrEmpty(model.Password))
            {
                return BadRequest(new ErrorResponseDto { error = "Email and Password not provided" });
            }

            try
            {
                var (token, userId, rolePaid) = await _authService.AuthenticateAsync(model);

                if (token == null)
                {
                    return Unauthorized(new ErrorResponseDto { error = "Invalid Credentials" });
                }

                return Ok(new
                {
                    auth = true,
                    Token = token,
                    UserId = userId,
                    RolePaid = rolePaid,
                    Expiration = DateTime.Now.AddMinutes(60)
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

        [HttpGet("{authToken}")]
        public async Task<IActionResult> IsAuthenticated(string authToken)
        {
            if (string.IsNullOrWhiteSpace(authToken))
            {
                return BadRequest(new ErrorResponseDto { error = "Token is required" });
            }

            try
            {
                // Validate the token and extract claims
                var claimsPrincipal = _authService.ValidateToken(authToken);
                if (claimsPrincipal == null)
                {
                    return Unauthorized(new ErrorResponseDto { error = "Invalid or expired token" });
                }

                // Extract user ID and role from claims
                var userId = claimsPrincipal.FindFirstValue(ClaimTypes.NameIdentifier);
                var role = claimsPrincipal.FindFirstValue(ClaimTypes.Role);

                if (string.IsNullOrEmpty(userId) || string.IsNullOrEmpty(role))
                {
                    return Unauthorized(new ErrorResponseDto { error = "Invalid token structure" });
                }

                // Convert user ID to an integer
                var id = int.Parse(userId);

                // Check role and fetch the corresponding user
                dynamic user;
                if (role == "admin")
                {
                    user = await _adminService.GetAdminByIdAsync(id);
                    if (user == null)
                    {
                        return NotFound(new ErrorResponseDto { error = "Admin not found" });
                    }
                }
                else
                {
                    user = await _userService.GetUserByIdAsync(id);
                    if (user == null)
                    {
                        return NotFound(new ErrorResponseDto { error = "User not found" });
                    }
                }

                // Generate a new token
                var claims = new List<Claim>
                {
                    new Claim(ClaimTypes.NameIdentifier, user.Id.ToString()),
                    new Claim(ClaimTypes.Role, role)
                };

                // Add additional claim if the user has a RolePaid property
                if (role == "user" && user is User userEntity)
                {
                    claims.Add(new Claim("rolePaid", userEntity.RolePaid.ToString()));
                }

                var newToken = _authService.GenerateToken(claims);

                return Ok(new
                {
                    auth = true,
                    Token = newToken,
                    UserId = user.Id,
                    RolePaid = role == "user" ? (user as User)?.RolePaid : null,
                    Expiration = DateTime.Now.AddMinutes(60)
                });
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = ex.Message
                });
            }
        }
    }
}
