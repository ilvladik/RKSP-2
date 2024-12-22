using Microsoft.AspNetCore.Mvc;
using NoteService.Data;
using NoteService.Models;
using System.Text.Json;

namespace NoteService.Controllers
{
    [Route("/api")]
    public class NoteController(ApplicationDbContext context, HttpClient httpClient, IConfiguration config) : ControllerBase
    {
        private readonly string _logServiceConnectionString = config.GetConnectionString("LoggerService");

        [HttpGet("notes")]
        public IActionResult GetAll()
        {

            httpClient.PostAsync(_logServiceConnectionString + "/api/logs", 
                JsonContent.Create(new { info = "Get Notes" }));
            return Ok(context.Notes.ToList());
        }

        [HttpGet("notes/{id}")]
        public IActionResult GetOne(string id)
        {
            httpClient.PostAsync(_logServiceConnectionString + "/api/logs",
                JsonContent.Create(new { info = "Get one Note" }));
            if (Guid.TryParse(id, out var noteId))
            {
                var note = context.Notes.SingleOrDefault(note => note.Id == noteId);
                if (note != null)
                    return Ok(note);
                return NotFound();
            }
            return NotFound();
        }

        [HttpPost("notes")]
        public async Task<IActionResult> Create([FromBody] NoteDto noteDto)
        {
            var message = await httpClient.PostAsync(_logServiceConnectionString + "/api/logs", 
                JsonContent.Create(new { info = "Create Note" }));
            var note = new Note(noteDto.Title, noteDto.Description);
            context.Notes.Add(note);
            context.SaveChanges();
            return Ok(note);
        }

        [HttpDelete("notes/{id}")]
        public IActionResult Delete(string id)
        {
            if (Guid.TryParse(id, out var noteId))
            {
                httpClient.PostAsync(_logServiceConnectionString + "/api/logs",
                    JsonContent.Create(new { info = "Delete Note" }));
                var note = context.Notes.SingleOrDefault(note => note.Id == noteId);
                if (note != null)
                {
                    context.Notes.Remove(note);
                    context.SaveChanges();
                    return NoContent();
                }
                return NotFound();
            }
            return NotFound();
        }
    }

    public record NoteDto(string Title, string Description);
}
