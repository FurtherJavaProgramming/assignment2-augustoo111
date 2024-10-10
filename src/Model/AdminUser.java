package model;

public class AdminUser extends User {
    private String adminSpecificField; 

    public AdminUser( String username, String password) {
        super(username, password);
        this.adminSpecificField = "Admin-specific data";  
    }
    // Admin-specific methods
    public String getAdminSpecificField() {
        return adminSpecificField;
    }

    public void setAdminSpecificField(String adminSpecificField) {
        this.adminSpecificField = adminSpecificField;
    }

    
}
