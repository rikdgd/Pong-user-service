using Microsoft.AspNetCore.Mvc;
using Models;


namespace Controllers;



[ApiController]
[Route("[controller]")]
public class UserController : ControllerBase
{
    [HttpGet(Name = "GetUser")]
    public IActionResult GetTestUser()
    {
        return Ok(new UserModel(1, "test-user", "welcome123"));
    }
}