package com.dtt.organization.restcontroller;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.dto.BenificiariesDto;
import com.dtt.organization.service.iface.BeneficiaryIface;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
public class BeneficirayController {

    private final BeneficiaryIface beneficiaryIface;

    public BeneficirayController(BeneficiaryIface beneficiaryIface) {
        this.beneficiaryIface = beneficiaryIface;
    }

    @PostMapping("/api/add/beneficiaries")
    public ApiResponses addBeneficiary(@RequestBody BenificiariesDto benificiariesDto) {
        return beneficiaryIface.addBeneficiary(benificiariesDto);
    }

    @GetMapping("/api/get/active/beneficiary-privileges")
    public ApiResponses getPrivileges() {
        return beneficiaryIface.findPrivilegeByStatus();
    }

    @GetMapping("/api/get/all/beneficiaries")
    public ApiResponses getAllBeneficiaries() {
        return beneficiaryIface.getAllBeneficiaries();
    }

    @GetMapping("/api/get/all/beneficiaries/by/sponsor-id/{id}")
    public ApiResponses getAllBeneficiariesBySponsor(@PathVariable("id") String sponsorId) {
        return beneficiaryIface.getAllBeneficiariesBySponsor(sponsorId);
    }

    @GetMapping("/api/get/beneficiary/by/id/{id}")
    public ApiResponses getBeneficiaryById(@PathVariable("id") int beneficiaryId) {
        return beneficiaryIface.getBeneficiaryById(beneficiaryId);
    }

    @PostMapping("/api/update/beneficiaries")
    public ApiResponses updateBeneficiary(@RequestBody BenificiariesDto benificiariesDto) {
        return beneficiaryIface.updateBeneficiary(benificiariesDto);
    }

    @PostMapping("/api/dlink/beneficiaries/{id}")
    public ApiResponses dlink(@PathVariable int id) {
        return beneficiaryIface.dlink(id);
    }

    @GetMapping("/api/verify/onboarding-sponsorship")
    public ApiResponses verifyOnBoardingSponsorship(@RequestParam String suid) {
        return beneficiaryIface.verifyOnBoardingSponsor(suid);
    }

    @GetMapping("/api/link-sponsor")
    public ApiResponses linkSponsor(@RequestParam String beneficiaryDigitalId, @RequestParam int id) {
        return beneficiaryIface.linkSponsor(beneficiaryDigitalId, id);
    }

    @PostMapping("/api/link-all-sponsor")
    public ApiResponses linkSponsor(@RequestBody BenificiariesDto benificiariesDto) {
        return beneficiaryIface.linkAllSponsor(benificiariesDto);
    }

    @GetMapping("/api/get/all/sponsors")
    public ApiResponses getAllSponsorBySuid(@RequestParam String suid) {
        return beneficiaryIface.getAllSponsersBySuid(suid);
    }

    @GetMapping("api/change/status/{id}")
    public ApiResponses changeStatusForSSP(@PathVariable int id) {
        return beneficiaryIface.changeStatusForSSP(id);
    }

    @PostMapping("/api/add/multiple/beneficiaries")
    public ApiResponses addMultipleBeneficiaries(@RequestBody List<BenificiariesDto> multipleBenificiariesDto) {
        return beneficiaryIface.addMultipleBeneficiaries(multipleBenificiariesDto);
    }

    @GetMapping("api/get/vendors/by/vendor-id/{vendorId}")
    public ApiResponses getVendorsByVendorId(@PathVariable String vendorId) {
        return beneficiaryIface.getVendorsByVendorId(vendorId);
    }

    @GetMapping("/api/download/spotbugs")
    public ResponseEntity<InputStreamResource> downloadSpotbugsReport() throws IOException {

        String filePath = "build/reports/spotbugs/main.html";

        File file = new File(filePath);
        if (!file.exists()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=spotbugs-report.html")
                .contentType(MediaType.TEXT_HTML)
                .contentLength(file.length())
                .body(resource);
    }
}