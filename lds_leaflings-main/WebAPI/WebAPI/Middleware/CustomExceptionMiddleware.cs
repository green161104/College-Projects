namespace WebAPI.Middleware
{
    public class CustomExceptionMiddleware
    {
        private readonly RequestDelegate _next;
        public CustomExceptionMiddleware(RequestDelegate next)
        {
            _next = next;
        }

        public async Task InvokeAsync(HttpContext context)
        {
            try
            {
                await _next(context);
            }
            catch (Exception ex)
            {
                // Log the exception (you can use a logging framework here)
                context.Response.StatusCode = 500;
                await context.Response.WriteAsync("An unexpected error occurred");
            }
        }
    }
}
