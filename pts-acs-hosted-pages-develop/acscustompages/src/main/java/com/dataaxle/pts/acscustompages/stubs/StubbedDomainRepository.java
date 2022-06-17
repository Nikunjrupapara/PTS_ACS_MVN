package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.model.Domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StubbedDomainRepository {

	private static final Map<String, Domain> domainMap = new HashMap<>();

	static {
		addDomain(buildDomain("test.boscovs.com", "boscovs", "boscovs"));
		//
		addDomain(buildDomain("test.kalmbachmedia.com", "kalmbachmedia", "kalmbachmedia"));

		// Kodak Moments
		addDomain(buildDomain("test.kodakmoments.com", "kodakmoments", "kodakmoments"));


		// Dollar tree
		addDomain(buildDomain("test.mailing.dollartree.com", "dollartree", "dollartree"));

		// Dillards
		addDomain(buildDomain("test.web.mktgdillards.com", "dillards", "dillards"));


		// Mcrewards
		addDomain(buildDomain("test.mcrewardspoland.com", "mcrewardspoland", "mcrewardspoland"));
		addDomain(buildDomain("test.mcrewardsbrazil.com", "mcrewardsbrazil", "mcrewardsbrazil"));


		// US Bank (trancore)
		addDomain(buildDomain("test.quickencard.com", "usbank", "QKN"));
		addDomain(buildDomain("test.edwardjonescreditcard.com", "usbank", "EJ"));
		addDomain(buildDomain("test.creditcard.acg.aaa.com", "usbank", "ACG"));
		addDomain(buildDomain("test.associatedbankcreditcard.com", "usbank", "ASB"));
		addDomain(buildDomain("test.myaccountaccess.com", "usbank", "ELA"));
		addDomain(buildDomain("test.fidelityrewards.com", "usbank", "FID"));
		addDomain(buildDomain("test.ameriprisecreditcard.com", "usbank", "AMP"));
		addDomain(buildDomain("test.mybmwcreditcard.com", "usbank", "BMW"));
		addDomain(buildDomain("test.myminicreditcard.com", "usbank", "MNI"));
		addDomain(buildDomain("test.mybmwmotorradcard.com", "usbank", "RAD"));

		addDomain(buildDomain("test.flycardpreferred.com", "usbank", "ELL"));

		// QA domains
		addDomain(buildDomain("usb-bmw.ptsacs.data-axle.com", "usbank", "BMW"));

		// production domains
		addDomain(buildDomain("email.mybmwcreditcard.com", "usbank", "BMW"));

		// Resideo
		addDomain(buildDomain("test.resideo.com", "resideo", "resideo"));
		addDomain(buildDomain("test.honeywellhome.com", "resideo", "honeywellhome"));
		addDomain(buildDomain("resideo.testlocal.com", "resideo", "resideo"));
		addDomain(buildDomain("honeywellhome.testlocal.com", "resideo", "honeywellhome"));

		// QA domains
		addDomain(buildDomain("resideo-qa.ptsacs.data-axle.com", "resideo", "resideo"));
		addDomain(buildDomain("honeywellhome-qa.ptsacs.data-axle.com", "resideo", "honeywellhome"));

		// Production
		addDomain(buildDomain("email.resideo.com", "resideo", "resideo"));
		addDomain(buildDomain("email.honeywellhome.com", "resideo", "honeywellhome"));

		// ymnewsolutions
		addDomain((buildDomain("test.ymnewsolutions.com", "ymnewsolutions", "ymnewsolutions")));
	}

	public static Optional<Domain> findById(String domain) {
		if (domainMap.containsKey(domain)) {
			return Optional.of(domainMap.get(domain));
		}
		return Optional.empty();
	}

	private static void addDomain(Domain domain) {
		domainMap.put(domain.getName(), domain);
	}

	private static Domain buildDomain(String domain, String company, String brand) {
		return new Domain(domain, company, brand);
	}
}
