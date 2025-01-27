using Microsoft.EntityFrameworkCore;
using WebAPI.Data;
using WebAPI.Middleware;
using Microsoft.OpenApi.Models;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using System.Text;
using WebAPI.Services;
using System.Xml.Linq;
using WebAPI.Utils;
using Microsoft.Extensions.FileProviders;

var builder = WebApplication.CreateBuilder(args);


builder.Logging.AddDebug();
builder.Logging.AddConsole();


// Carrega as variáveis de ambiente definidas no sistema ou Docker
builder.Configuration.AddEnvironmentVariables();

// Adiciona o serviço singleton para Cryptography
builder.Services.AddSingleton<Cryptography>();

// Adiciona os serviços do controlador com configuração para serializar enums como strings
builder.Services.AddControllers()
    .AddJsonOptions(options =>
    {
        // Configuração para serializar enums como strings
            options.JsonSerializerOptions.Converters.Add(new System.Text.Json.Serialization.JsonStringEnumConverter());
    });

// Ativa o CORS
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowAllOrigins", builder =>
    {
        builder.AllowAnyOrigin() // Permite qualquer origem
               .AllowAnyMethod() // Permite qualquer método (GET, POST, etc.)
               .AllowAnyHeader(); // Permite qualquer cabeçalho
    });
});


// Configurações do Swagger
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new OpenApiInfo { Title = "WebAPI", Version = "v1" });

    // Configuração para JWT Auth
    c.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme
    {
        Name = "Authorization",
        Type = SecuritySchemeType.Http,
        Scheme = "Bearer",
        BearerFormat = "JWT",
        In = ParameterLocation.Header,
        Description = "Insira 'Bearer' [espaço] e o token JWT na caixa de texto abaixo.\n\nExemplo: 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'"
    });

    c.AddSecurityRequirement(new OpenApiSecurityRequirement
    {
        {
            new OpenApiSecurityScheme
            {
                Reference = new OpenApiReference
                {
                    Type = ReferenceType.SecurityScheme,
                    Id = "Bearer"
                }
            },
            new string[] {}
        }
    });
});


// Registro do PlantService no contêiner de DI
builder.Services.AddScoped<PlantService>();
builder.Services.AddScoped<UserService>();
builder.Services.AddScoped<AdminService>();
builder.Services.AddScoped<WarningService>();
builder.Services.AddScoped<UserPlantsService>();

builder.Services.AddScoped<AdService>();

builder.Services.AddScoped<DiaryService>();
builder.Services.AddScoped<LogService>();
builder.Services.AddScoped<PaymentService>();
builder.Services.AddScoped<TaskService>();

// adiciona o serviço do dbcontext com sql server
builder.Services.AddDbContext<DataContext>(options =>
{
    options.UseSqlServer(builder.Configuration.GetConnectionString("defaultconnection"));
});


// Configura a autenticação JWT
builder.Services.AddAuthentication(options =>
{
    options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
})
.AddJwtBearer(options =>
{
    options.TokenValidationParameters = new TokenValidationParameters
    {
        ValidateIssuer = true,
        ValidateAudience = true,
        ValidateLifetime = true,
        ValidateIssuerSigningKey = true,
        ValidIssuer = builder.Configuration["Jwt:Issuer"],
        ValidAudience = builder.Configuration["Jwt:Audience"],
        IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(builder.Configuration["Jwt:Key"]))
    };
});

// Configura a autorização com política "AdminPolicy"
builder.Services.AddAuthorization(options =>
{
    options.AddPolicy("AdminPolicy", policy => policy.RequireRole("admin"));
});

var app = builder.Build();

// Habilita o middleware CORS
app.UseCors("AllowAllOrigins");

// Registra o middleware customizado
app.UseMiddleware<CustomExceptionMiddleware>();
// Registra o middleware de tratamento de erros
app.UseMiddleware<ErrorHandlingMiddleware>();

// Adicionar aqui - Configuração de ficheiros estáticos
//app.UseStaticFiles(); // Adicione esta linha
// Configuração mais detalhada para ficheiros estáticos
app.UseStaticFiles(new StaticFileOptions
{
    FileProvider = new PhysicalFileProvider(
        Path.Combine(Directory.GetCurrentDirectory(), "wwwroot")),
    RequestPath = ""
});

// Configura o pipeline para o Swagger
//if (app.Environment.IsDevelopment())
//{
//    app.UseSwagger();
//    app.UseSwaggerUI(c =>
//    {
//        c.SwaggerEndpoint("/swagger/v1/swagger.json", "PlantMatchAPI v1");
//    });
//}
app.UseSwagger();
app.UseSwaggerUI(c =>
{
    c.SwaggerEndpoint("/swagger/v1/swagger.json", "LeaflingsAPI v1");
});

app.UseHttpsRedirection();

// Adiciona o middleware para forçar URLs minúsculas
app.Use(async (context, next) =>
{
    context.Request.Path = context.Request.Path.Value.ToLowerInvariant();
    await next.Invoke();
});

// Adiciona o middleware de autenticação
app.UseAuthentication();
app.UseAuthorization();

app.MapControllers();

app.Run();

