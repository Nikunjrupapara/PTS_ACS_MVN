package com.yesmarketing.ptsacs.services.dto;


import com.yesmarketing.acsapi.model.Location;
import com.yesmarketing.acsapi.model.OrgUnit;
import com.yesmarketing.acsapi.model.PostalAddress;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Deprecated
@Data
@NoArgsConstructor
public class UpdateProfileDto extends CreateProfileDto implements Serializable {
	private static final long serialVersionUID = -8985720269402010215L;
}
