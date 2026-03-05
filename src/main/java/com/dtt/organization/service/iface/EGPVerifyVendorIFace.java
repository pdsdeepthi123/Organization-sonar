package com.dtt.organization.service.iface;

import com.dtt.organization.constant.ApiResponses;

public interface EGPVerifyVendorIFace {
	ApiResponses verifyByEgpForVendor(String vendorId, String orgId);
}
