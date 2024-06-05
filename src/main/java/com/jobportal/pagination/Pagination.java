package com.jobportal.pagination;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {
	
	 private List<?> list;
	
	 private boolean hasprevious;
	 
	 private boolean hasNext;
	 
	 private int totalCount;
	 
	 private int currentPage;
	 
	 private int pageSize;
	

}
