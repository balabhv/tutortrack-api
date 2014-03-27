package com.tutortrack.api;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;

import java.util.List;

public class Student {

	public static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public String name, email, password, umsid;
	
	public Student() {}
	
	public void setName(String newName) { name = newName; }
	public void setEmail(String newEmail) { email = newEmail; }
	public void setPassword(String newPass) { password = newPass; }
	public void setUMSID(String newID) { umsid = newID; }
	
	public String getName() { return name; }
	public String getEmail() { return email; }
	public String getPassword() { return password; }
	public String getUMSID() { return umsid; }

	public Student(String name, String email, String password, String umsid) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.umsid = umsid;
	}

	public static Student getStudentFromDatabase(String email, String password) {
	
		Query q = new Query("Student")
					.addFilter("email",
						Query.FilterOperator.EQUAL,
						email)
					.addFilter("password",
						Query.FilterOperator.EQUAL,
						password);
		List<Entity> results = datastore.prepare(q)
                                      .asList(FetchOptions.Builder.withDefaults());
		Entity s = results.get(0);
		return new Student((String) s.getProperty("name"), (String) s.getProperty("email"), (String) s.getProperty("password"), (String) s.getProperty("umsid"));
	}
  
	private static boolean addStudentToDatabaseHelper(String name, String email, String password, String umsid) {
	
		Entity s = new Entity("Student");
		s.setProperty("name", name);
		s.setProperty("email", email);
		s.setProperty("password", password);
		s.setProperty("umsid", umsid);
		
		try {
		
			datastore.put(s);
			
		} catch (Exception e) {
			return false;
		} finally {
			return true;
		}
	}
	
	public static boolean addStudentToDatabase(Student s) {
		return addStudentToDatabaseHelper(s.name, s.email, s.password, s.umsid);
	}
	
}