package model;
/**
 * Represents an admin user in the system.
 * This class stores the admin user's credentials,
 */

public class AdminUser extends User {
	
	//Constructs an AdminUser with full details.
	public AdminUser(String firstName, String lastName, String username, String password) {
		 super(firstName, lastName, username, password);
    }
    //Constructs an AdminUser with only username and password
    public AdminUser( String username, String password) {
        super(username, password);
    }
  

    
}
