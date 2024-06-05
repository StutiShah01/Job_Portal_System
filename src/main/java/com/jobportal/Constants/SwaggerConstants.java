package com.jobportal.Constants;

public class SwaggerConstants {
	
	 // Controller-level descriptions
    public static final String TAG_CONTROLLER = "Tag Controller";
    public static final String TAG_CONTROLLER_DESCRIPTION = "Operations related to managing tags in the Job Portal";

    // Operation summaries
    public static final String ADD_TAG_SUMMARY = "Add a new tag";
    public static final String ADD_TAG_DESCRIPTION = "Creates a new tag and returns the details of the created tag";
    public static final String GET_ALL_TAGS_SUMMARY = "Get all tags";
    public static final String GET_ALL_TAGS_DESCRIPTION = "Retrieves a paginated list of all tags";
    public static final String DELETE_TAG_SUMMARY_STRING="delete tag by tag Id";
    
    // Response descriptions
    public static final String RESPONSE_TAG_CREATED = "Tag created successfully";
    public static final String RESPONSE_ALL_TAGS_RETRIEVED = "All tags retrieved successfully"; 
    public static final String RESPONSE_TAG_DELETE="Tag Deleted Successfully";

}
