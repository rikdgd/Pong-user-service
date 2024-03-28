using Pong_user_service.Models;


namespace Pong_user_service;

public class DbMock 
{
    private List<UserModel> _store;
    
    
    public DbMock()
    {
        _store = new List<UserModel>();
    }
    public DbMock(List<UserModel> users)
    {
        _store = [.. users];
    }
    
    public UserModel GetUserById(int id)
    {
        return new UserModel(id, "Ron", "welcome123");
    }
}