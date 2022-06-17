package com.yesmarketing.ptsacs.services.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;

import java.io.IOException;
import java.util.HashMap;

public class CustomResourceRecordDtoDeserializer extends JsonDeserializer<CustomResourceRecordDto> {

	@Override
	public CustomResourceRecordDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		CustomResourceRecordDto obj = new CustomResourceRecordDto();
		obj.fields = new HashMap<>();
		if (!node.equals(NullNode.getInstance())) {
			node.fields().forEachRemaining(e->{
				obj.fields.put(e.getKey(), e.getValue().asText());

				//if(!e.getKey().equals("customResourceName")) obj.fields.put(e.getKey(), e.getValue().asText());else obj.setCustomResourceName(e.getValue().asText());
			});
			return obj;
		}
		return null;
	}

}
