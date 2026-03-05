package com.dtt.organization.service.iface;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.dto.OrganisationPrivilegesRequestDto;
import com.dtt.organization.dto.UpdateOrganizationPrivilegeDto;
import com.dtt.organization.dto.UpdateOrganizationPrivilegesListDto;

public interface OrganizationPrivilegesIface {

    ApiResponses getPrivilegesByOrgId(String orgId);

    ApiResponses requestPrivilege(OrganisationPrivilegesRequestDto organisationPrivilegesRequestDto);

    ApiResponses updatePrivilege(UpdateOrganizationPrivilegeDto updateOrganizationPrivilegeDto);

    ApiResponses getAllPrivileges();

    ApiResponses getOrganizationPrivilegeById(int id);

    ApiResponses getPrivilegesByOrganization(String orgId);

    ApiResponses updateOrganizationPrivilegeList(UpdateOrganizationPrivilegesListDto updateOrganizationPrivilegesListDto);

    ApiResponses getPrivilegesNames();
}
