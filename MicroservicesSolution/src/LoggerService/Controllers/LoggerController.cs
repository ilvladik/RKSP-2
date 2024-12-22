using LoggerService.Data;
using LoggerService.Models;
using Microsoft.AspNetCore.Mvc;
using System.Runtime.CompilerServices;

namespace LoggerService.Controllers
{
    [Route("/api")]
    public class LoggerController(ApplicationDbContext context) : ControllerBase
    {


        [HttpGet("logs")]
        public IActionResult GetAll()
        {
            var logs = context.Logs
                .ToList();
            return Ok(logs);
        }

        [HttpPost("logs")]
        public IActionResult Create([FromBody] LogDto logDto)
        {
            var log = new Log(logDto.Info);
            context.Logs.Add(log);
            context.SaveChanges();
            return Ok(log);
        }


    }

    public record LogDto(string Info);
}
