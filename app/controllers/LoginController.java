package controllers;

import play.mvc.*;
import play.libs.Json;

import java.sql.*;
import java.util.Map;
import java.util.HashMap;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import play.data.DynamicForm;
import play.data.Form;
import play.db.*;
import views.html.*;
public class LoginController extends Controller {
	
	private Database db;
	
	@Inject
	public LoginController(Database b) {
		db = b;
	}
    
    public Result displayPage() {
    	return ok(views.html.login.render());
    }
    
    public Result login() {
    	JsonNode json = request().body().asJson();
    	String username = null;
    	String password = null;
    	if (json != null)  {
    		username = json.findPath("username").textValue();
    		password = json.findPath("password").textValue();
    	}        
        
		Map<String, Boolean> map = new HashMap<>();
		map.put("criteria", false);
		map.put("loggedin", false);
		map.put("failed", false);
		Connection con = null;
		Statement state = null;
		
		if (username == null && password == null) {
		}
		else if ((username = username.trim()).length() == 0
				|| (password = password.trim()).length() == 0) {
			map.put("criteria", true);
		}
		else if (success(username, password)) {
			map.put("loggedin", true);
		}
		else {
			map.put("failed", true);
		}
    	
    	return ok(Json.toJson(map));
    }
    
    private boolean success (String username, String password) {
		   Connection con = null;
		   Statement state = null;
		   try {
			   con = db.getConnection();
			   state = con.createStatement();
			   ResultSet set = state.executeQuery("SELECT * FROM Users WHERE BINARY username='" + username + "' AND BINARY password='" + password + "'");
			   return set.next();
		   } catch (SQLException e) {
			e.printStackTrace();
		} finally {	
			   try {
				if (state != null) { state.close(); }
				if (con != null) { con.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
			}
			   
		   }
		   return false;
	   }

}
