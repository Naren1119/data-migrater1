package io.mosip.packet.core.dto.packet.type;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleType {
	
	private String language;
	private String value;

}
