package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class StatController extends Controller {
	
	private Database db;
	
	@Inject
	public StatController(Database b) {
		db = b;
	}
	
	public Result updateStats() {
    	JsonNode json = request().body().asJson();
    	String username = null;
    	if (json != null)  {
    		username = json.findPath("username").textValue();
    	}
    	if (username != null) {
    		incrementLogin(username);
    	}
		return ok();
	}
	
	private void incrementLogin(String username) {
		try (Connection con = db.getConnection();
			 Statement state = con.createStatement()){
			state.execute("UPDATE Users SET login=login+1 WHERE username='" + username + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
