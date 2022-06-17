package com.yesmarketing.ptsacs.services.service;

import com.yesmarketing.acsapi.admin.model.CustomResourceMetadata;
import com.yesmarketing.acsapi.admin.model.CustomResourceRecord;
import com.yesmarketing.ptsacs.services.dto.CustomResourceRecordCollectionDto;
import com.yesmarketing.ptsacs.services.dto.UpdateResourceDto;

import java.util.List;
import java.util.Map;


public interface CustomResourceService {

	String DEFAULT_LINE_COUNT = "25";

	String DEFAULT_LINE_START = "0";

	String FORCE_PAGINATION_PARAM = "_forcePagination";

	String FORCE_PAGINATION_VALUE = "true";

	String LINE_COUNT_PARAM = "_lineCount";

	String LINE_START_PARAM = "_lineStart";

	CustomResourceMetadata getMetadata(String company, String dataSet);

	String getMetadataString(String company, String dataSet);


	String getCount(String company, String dataSet);

	CustomResourceRecordCollectionDto getRecords(String company, String resource, String filter, Map<String, String> queryParameters);

	CustomResourceRecord getRecord(String company, String resource, String pKey);

	UpdateResourceDto updateRecord(String company, String resource, String pKey, CustomResourceRecord recordData);

	void deleteRecord(String company, String resource, String pKey);

	List<String> getAssociatedList(String company);

	List<String> getUnassociatedList(String company);

	CustomResourceRecord parseCustomResourceRecordJson(String recordStr);

	CustomResourceRecord createRecord(String company, String resource, CustomResourceRecord recordData);


	/////

	//// Get record by ACS Id
	CustomResourceRecord getRecordByAcsId(String company, String resourceName, String acsId);

	//// Get record by business key
	CustomResourceRecord getRecordByBusinessKey(String company, String resourceName, Map<String, String> queryParameters);

	//// Search for record
	List<CustomResourceRecord> searchRecords(String company, String resourceName, String filter, Map<String, String> queryParameters);

	//// Update by acsId
	CustomResourceRecord updateRecordByAcsId(String company, String resourceName, String acsId, CustomResourceRecord recordData);

	//// Delete record by acsId
	void deleteRecordByAcsId(String company, String resourceName, String acsId);



	void validateCreateRecord(String company,String resourceName, CustomResourceRecord customResourceRecord);
	void validateUpdateRecordByAcsId(String company,String resourceName, String acsId, CustomResourceRecord customResourceRecord);
	void validateDeleteRecordByAcsId(String company,String resourceName, String acsId);

}