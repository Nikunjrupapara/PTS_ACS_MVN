package com.dataaxle.pts.acscustompages.model.form;

import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.dataaxle.pts.acscustompages.model.ListCustomResourceConfig;
import com.dataaxle.pts.acscustompages.model.ListCustomResourceResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * This class is used to hold all the information required to display a list with pagination on a HTML page
 * @param <T>
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ListDetails<T> implements Serializable {

	private static final long serialVersionUID = 2649390592804719322L;

	/**
	 * The current page number
	 */
	int currentPage;

	/**
	 * The total number of pages
	 */
	int totalPages;

	/**
	 * The total number of records matching the search criteria used in the request
	 */
	int totalRecords;

	/**
	 * Controls whether to display the paging controls
	 */
	boolean displayPaging;

	/**
	 * The data to display
	 */
	List<T> data;

	public static ListDetails<CustomResourceRecord> of(ListCustomResourceConfig config, ListCustomResourceResponse response) {
		int currentPage = response.getPageNumber();
		int totalPages = (response.getTotalRecords() / config.getPageSize()) + 1;
		return new ListDetails<>(currentPage, totalPages, response.getTotalRecords(), config.isUsePagination(),
			response.getRecords());
	}

	public static ListDetails<CustomResourceRecord> emptyList() {
		return new ListDetails<>(0, 0, 0, false, Collections.emptyList());
	}
}
