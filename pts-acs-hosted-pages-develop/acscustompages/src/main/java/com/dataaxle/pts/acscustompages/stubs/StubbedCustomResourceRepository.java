package com.dataaxle.pts.acscustompages.stubs;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.ACS_ID;

import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ListCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.ListCustomResourceResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class StubbedCustomResourceRepository {

	private static final Table<String, String, GetCustomResourceResponse> records = HashBasedTable.create();

	private static final Table<String, String, CustomResourceRecord> listOfRecords = HashBasedTable.create();

	static {
		resideo_coupon_codes();
		boscovs_coupon_codes();
		boscovs_cusNpOrganizations();
	}

	private static void resideo_coupon_codes() {
		String customResource = "cusCoupon_codes";
		Arrays.asList("filter001", "filter002")
			.forEach(code -> addRecord(customResource, code, new GetCustomResourceResponse(new CustomResourceRecord(
				Map.of(
					"PKey", UUID.randomUUID().toString(),
					"acsId", UUID.randomUUID().toString(),
					"promo_code", code,
					"alias_promo_code", code,
					"product", "FreeFilter",
					"status", "",
					"created", LocalDateTime.now(ZoneId.of("US/Central")),
					"lastModified", LocalDateTime.now(ZoneId.of("US/Central"))
				)))));
		String ffinvprd1 = "ffinvprd1";
		addRecord(customResource, ffinvprd1, new GetCustomResourceResponse(new CustomResourceRecord(
			Map.of(
				"PKey", UUID.randomUUID().toString(),
				"acsId", UUID.randomUUID().toString(),
				"promo_code", ffinvprd1,
				"alias_promo_code", ffinvprd1,
				"product", "Another Product",
				"status", "",
				"created", LocalDateTime.now(ZoneId.of("US/Central")),
				"lastModified", LocalDateTime.now(ZoneId.of("US/Central"))
			))));
		String ffredeemd = "ffredeemd";
		addRecord(customResource, ffredeemd, new GetCustomResourceResponse(new CustomResourceRecord(
			Map.of(
				"PKey", UUID.randomUUID().toString(),
				"acsId", UUID.randomUUID().toString(),
				"promo_code", ffredeemd,
				"alias_promo_code", ffredeemd,
				"product", "FreeFilter",
				"status", "R",
				"created", LocalDateTime.now(ZoneId.of("US/Central")),
				"lastModified", LocalDateTime.now(ZoneId.of("US/Central"))
			))));
	}

	private static void boscovs_coupon_codes() {
		String customResource = "cusCouponLog";
		String barcode = "49300224010";
		String email = "charlesb@yesmail.com";
		String customerIdHash = Hashing.sha256().hashString(email, StandardCharsets.UTF_8).toString();
		String customerUuid = "2e4bbe0d-00a3-4f30-b703-20a9a4ae6bb1";
		String recordId = String.format("%s|%s|%s", barcode, customerIdHash, customerUuid);
		addRecord(customResource, recordId, new GetCustomResourceResponse(new CustomResourceRecord(
			Map.ofEntries(
				new AbstractMap.SimpleEntry<>("PKey", UUID.randomUUID().toString()),
				new AbstractMap.SimpleEntry<>("accessedCount", "0"),
				new AbstractMap.SimpleEntry<>("acsId", UUID.randomUUID().toString()),
				new AbstractMap.SimpleEntry<>("barcode", barcode),
				new AbstractMap.SimpleEntry<>("created", LocalDateTime.now()),
				new AbstractMap.SimpleEntry<>("customerId", email),
				new AbstractMap.SimpleEntry<>("customerIdHash", customerIdHash),
				new AbstractMap.SimpleEntry<>("customerUuid", customerUuid),
				new AbstractMap.SimpleEntry<>("expireDate", "11/26/2021"),
				new AbstractMap.SimpleEntry<>("image", "https://www.boscovs.com/wcsstore/boscovs/images/html/122917_charge_10off_savingcertificate/10pcpnhdr.jpg"),
				new AbstractMap.SimpleEntry<>("instructions", "You pick the date!<BR>Valid all day, ONE day:____________________________________<BR><BR>Co-worker, please enter date redeemed and scan after each eligible item."),
				new AbstractMap.SimpleEntry<>("lastModified", LocalDateTime.now()),
				new AbstractMap.SimpleEntry<>("legal", "*This certificate is valid in the departments listed above and must be presented with each purchase made on the day of your choice during this event. This certificate is non-transferable. Not eligible for exchanges or refunds. Discount cannot be used on the following items: Incredible Doorbusters, Nike, Adidas, New Balance, Champion, Koolaburra by Uggs, Columbia, US Vision, Vionic, Motherhood Maternity, Fine Jewelry Trunk Shows, Gift Cards, iComfort by Serta, Sealy Hybrid, Stearns and Foster, Beautyrest Black and Tempur-Pedic mattresses. See Sales Associate for details. Cannot be combined with any other coupon, offer or discount. Offer not valid on prior purchases."),
				new AbstractMap.SimpleEntry<>("limit", "5"),
				new AbstractMap.SimpleEntry<>("onlineCode", "15% Off<BR>Online Code: <font color=\"blue\">WELCOME</font>")
			))));
	}

	private static void boscovs_cusNpOrganizations() {
		List<String> pathElements = List.of(/*".", "src", "main", "resources",*/ "json", "boscovs", "fhf");
		String customResource = "cusNpOrganizations";
		List<CustomResourceRecord> records = getRecords(pathElements, customResource);
		records.forEach(rec -> listOfRecords.put(customResource,
			(String)rec.getValue("npID").orElse(UUID.randomUUID().toString()), rec));
	}

	private static List<CustomResourceRecord> getRecords(List<String> pathElements, String customResource) {
		String fileName = String.format("%s.json", customResource);
		try {
			String recordsStr = getDataFromFile(pathElements, fileName);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(recordsStr);
			List<CustomResourceRecord> records = new ArrayList<>();
			rootNode.forEach(objectNode -> {
				CustomResourceRecord record = new CustomResourceRecord();
				objectNode.fieldNames().forEachRemaining(name -> record.addValue(name, objectNode.get(name).asText()));
				records.add(record);
			});
			return records;
		} catch (IOException e) {
			LOG.error("Error processing file!", e);
		}
		return Collections.emptyList();
	}

	private static String getDataFromFile(List<String> pathElements, String fileName) throws IOException {
		StringBuilder fullPath = new StringBuilder();
		pathElements.forEach(element -> fullPath.append(element).append(File.separator));
		fullPath.append(fileName);
		Resource dataFileResource = new ClassPathResource(fullPath.toString());
		File dataFile = dataFileResource.getFile();
		return new String(Files.readAllBytes(dataFile.toPath()));
	}

	public static GetCustomResourceResponse getRecordById(String customResource, String recordId) {
		if (records.contains(customResource, recordId)) {
			return records.get(customResource, recordId);
		}
		throw new ResourceNotFoundException(String.format("%s -> %s", customResource, recordId));
	}

	public static GetCustomResourceResponse getRecordByAcsId(String customResource, String acsId) {
		if (records.containsRow(customResource)) {
			return records.row(customResource).values().stream()
				.filter(rec -> rec.getField(ACS_ID).equals(acsId))
				.findFirst()
				.orElseThrow(() -> new ResourceNotFoundException(String.format("%s: %s", customResource, acsId)));
		}
		throw new ResourceNotFoundException(String.format("%s: %s", customResource, acsId));
	}

	public static GetCustomResourceResponse create(String company, String customResource, String recordId, CustomResourceRecord record) {
		if (records.contains(customResource, recordId)) {
			throw new RuntimeException(String.format("Record with id %s already exists", recordId));
		}
		GetCustomResourceResponse response = new GetCustomResourceResponse(record);
		records.put(customResource, recordId, response);
		return response;
	}

	public static GetCustomResourceResponse update(String company, String customResource, String recordId, CustomResourceRecord record) {
		CustomResourceRecord existing = getRecordByAcsId(customResource, recordId).getRecord();
		Map<String, Object> updated = new HashMap<>(existing.getValues());
		updated.putAll(record.getValues());
		existing.setValues(updated);
		return new GetCustomResourceResponse(existing);
	}

	public static ListCustomResourceResponse listRecords(ListCustomResourceRequest request) {
		Map<String, CustomResourceRecord> resourceRecords = listOfRecords.row(request.getCustomResource());
		Comparator<CustomResourceRecord> comparator = CustomResourceRecord.comparator(request.getSorting());
		// assumes only one filter parameter and that it is a string
		String filterFieldName = new ArrayList<>(request.getParameters().keySet()).get(0);
		String filterFieldValue = new ArrayList<>(request.getParameters().values()).get(0);
		List<CustomResourceRecord> records = resourceRecords.values().stream()
			.filter(rec -> rec.getValue(filterFieldName).orElse("").equals(filterFieldValue))
			.sorted(comparator)
			.collect(Collectors.toList());
		int totalRecords = records.size();
		records = new ArrayList<>(records.subList(request.getStartIndex(),
			Math.min(totalRecords, request.getStartIndex() + request.getPageSize())));
		boolean moreRecords = totalRecords > request.getStartIndex() + request.getPageSize();
		return new ListCustomResourceResponse(request.getPageNumber(), records.size(), totalRecords, moreRecords, records);
	}

	private static void addRecord(String customResourceName, String recordId, GetCustomResourceResponse response) {
		records.put(customResourceName, recordId, response);
	}
}
