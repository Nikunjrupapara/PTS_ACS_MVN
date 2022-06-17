package com.dataaxle.pts.acscustompages.model.converter;

import com.dataaxle.pts.acscustompages.exception.UnsupportedDataTypeException;
import com.dataaxle.pts.acscustompages.model.AdobeDataType;
import org.springframework.util.StringUtils;

public interface AdobeConverter<R> {

	R convert(Object value);

	static AdobeConverter<?> getInstance(AdobeDataType acsDataType) {
		switch (acsDataType) {
			case BOOLEAN:
				return (AdobeConverter<Boolean>) value -> Boolean.parseBoolean(value.toString());
			case BYTE:
				return (AdobeConverter<Byte>) value -> Byte.parseByte(value.toString());
			case DATE:
				return new DateConverter();
			case DATETIME:
				return new DateTimeConverter();
			case DOUBLE:
				return (AdobeConverter<Double>) value -> Double.parseDouble(StringUtils.hasText(value.toString()) ? value.toString() : "0");
			case FLOAT:
				return (AdobeConverter<Float>) value -> Float.parseFloat(StringUtils.hasText(value.toString()) ? value.toString() : "0");
			case INT64:
				return (AdobeConverter<Long>) value -> Long.parseLong(StringUtils.hasText(value.toString()) ? value.toString() : "0");
			case SHORT:
				return (AdobeConverter<Short>) value -> Short.parseShort(StringUtils.hasText(value.toString()) ? value.toString() : "0");
			case LONG:
				return (AdobeConverter<Integer>) value -> Integer.parseInt(StringUtils.hasText(value.toString()) ? value.toString() : "0");
			case MEMO:
			case STRING:
			case UUID:
				return (AdobeConverter<String>) Object::toString;
		}
		throw new UnsupportedDataTypeException(acsDataType.getDisplayName());
	}
}
