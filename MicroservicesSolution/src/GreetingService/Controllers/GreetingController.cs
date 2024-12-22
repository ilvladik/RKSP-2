using Microsoft.AspNetCore.Mvc;

namespace GreetingService.Controllers
{
    [Route("/api/hello")]
    public class GreetingController : ControllerBase
    {

        [HttpGet("{name}")]
        public IActionResult Hello([FromRoute] string name)
        {
            return Ok($"Hello, {name}!");
        }
    }
}
