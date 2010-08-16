
require conf/distro/include/distro_tracking_fields.inc

addtask distrodata_np
do_distrodata_np[nostamp] = "1"
python do_distrodata_np() {

	localdata = bb.data.createCopy(d)
        pn = bb.data.getVar("PN", d, True)
        bb.note("Package Name: %s" % pn)

	if pn.find("-native") != -1:
	    pnstripped = pn.split("-native")
	    bb.note("Native Split: %s" % pnstripped)
	    bb.data.setVar('OVERRIDES', "pn-" + pnstripped[0] + ":" + bb.data.getVar('OVERRIDES', d, True), localdata)
	    bb.data.update_data(localdata)

	if pn.find("-cross") != -1:
	    pnstripped = pn.split("-cross")
	    bb.note("cross Split: %s" % pnstripped)
	    bb.data.setVar('OVERRIDES', "pn-" + pnstripped[0] + ":" + bb.data.getVar('OVERRIDES', d, True), localdata)
	    bb.data.update_data(localdata)

	if pn.find("-initial") != -1:
	    pnstripped = pn.split("-initial")
	    bb.note("initial Split: %s" % pnstripped)
	    bb.data.setVar('OVERRIDES', "pn-" + pnstripped[0] + ":" + bb.data.getVar('OVERRIDES', d, True), localdata)
	    bb.data.update_data(localdata)

	"""generate package information from .bb file"""
	pname = bb.data.getVar('PN', localdata, True)
	pcurver = bb.data.getVar('PV', localdata, True)
	pdesc = bb.data.getVar('DESCRIPTION', localdata, True)
	pgrp = bb.data.getVar('SECTION', localdata, True)
	plicense = bb.data.getVar('LICENSE', localdata, True).replace(',','_')
	if bb.data.getVar('LIC_FILES_CHKSUM', localdata, True):
		pchksum="1"
	else:
		pchksum="0"

	if bb.data.getVar('RECIPE_STATUS', localdata, True):
		hasrstatus="1"
	else:
		hasrstatus="0"

	rstatus = bb.data.getVar('RECIPE_STATUS', localdata, True)
		
	pupver = bb.data.getVar('RECIPE_LATEST_VERSION', localdata, True)
	if pcurver == pupver:
		vermatch="1"
	else:
		vermatch="0"

	noupdate_reason = bb.data.getVar('RECIPE_NO_UPDATE_REASON', localdata, True)
	if noupdate_reason is None:
		noupdate="0"
	else:
		noupdate="1"

	ris = bb.data.getVar('RECIPE_INTEL_SECTION', localdata, True)
	maintainer = bb.data.getVar('RECIPE_MAINTAINER', localdata, True)
	rttr = bb.data.getVar('RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES', localdata, True)
	rlrd = bb.data.getVar('RECIPE_LATEST_RELEASE_DATE', localdata, True)
	dc = bb.data.getVar('DEPENDENCY_CHECK', localdata, True)
	rc = bb.data.getVar('RECIPE_COMMENTS', localdata, True)

	bb.note("DISTRO: %s,%s,%s,%s,%s,%s,%s,%s, %s, %s, %s\n" % \
		  (pname, maintainer, plicense, pchksum, hasrstatus, vermatch, pcurver, pupver, noupdate, noupdate_reason, rstatus))
}

addtask distrodata
do_distrodata[nostamp] = "1"
python do_distrodata() {
	"""initialize log files."""
	logpath = bb.data.getVar('LOG_DIR', d, 1)
	bb.utils.mkdirhier(logpath)
	logfile = os.path.join(logpath, "distrodata.%s.csv" % bb.data.getVar('DATETIME', d, 1))
	if not os.path.exists(logfile):
		slogfile = os.path.join(logpath, "distrodata.csv")
		if os.path.exists(slogfile):
			os.remove(slogfile)
		os.system("touch %s" % logfile)
		os.symlink(logfile, slogfile)

	localdata = bb.data.createCopy(d)
        pn = bb.data.getVar("PN", d, True)
        bb.note("Package Name: %s" % pn)

	if pn.find("-native") != -1:
	    pnstripped = pn.split("-native")
	    bb.note("Native Split: %s" % pnstripped)
	    bb.data.setVar('OVERRIDES', "pn-" + pnstripped[0] + ":" + bb.data.getVar('OVERRIDES', d, True), localdata)
	    bb.data.update_data(localdata)

	if pn.find("-cross") != -1:
	    pnstripped = pn.split("-cross")
	    bb.note("cross Split: %s" % pnstripped)
	    bb.data.setVar('OVERRIDES', "pn-" + pnstripped[0] + ":" + bb.data.getVar('OVERRIDES', d, True), localdata)
	    bb.data.update_data(localdata)

	if pn.find("-initial") != -1:
	    pnstripped = pn.split("-initial")
	    bb.note("initial Split: %s" % pnstripped)
	    bb.data.setVar('OVERRIDES', "pn-" + pnstripped[0] + ":" + bb.data.getVar('OVERRIDES', d, True), localdata)
	    bb.data.update_data(localdata)

	"""generate package information from .bb file"""
	pname = bb.data.getVar('PN', localdata, True)
	pcurver = bb.data.getVar('PV', localdata, True)
	pdesc = bb.data.getVar('DESCRIPTION', localdata, True)
	pgrp = bb.data.getVar('SECTION', localdata, True)
	plicense = bb.data.getVar('LICENSE', localdata, True).replace(',','_')
	if bb.data.getVar('LIC_FILES_CHKSUM', localdata, True):
		pchksum="1"
	else:
		pchksum="0"

	if bb.data.getVar('RECIPE_STATUS', localdata, True):
		hasrstatus="1"
	else:
		hasrstatus="0"

	rstatus = bb.data.getVar('RECIPE_STATUS', localdata, True)
		
	pupver = bb.data.getVar('RECIPE_LATEST_VERSION', localdata, True)
	if pcurver == pupver:
		vermatch="1"
	else:
		vermatch="0"

	noupdate_reason = bb.data.getVar('RECIPE_NO_UPDATE_REASON', localdata, True)
	if noupdate_reason is None:
		noupdate="0"
	else:
		noupdate="1"

	ris = bb.data.getVar('RECIPE_INTEL_SECTION', localdata, True)
	maintainer = bb.data.getVar('RECIPE_MAINTAINER', localdata, True)
	rttr = bb.data.getVar('RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES', localdata, True)
	rlrd = bb.data.getVar('RECIPE_LATEST_RELEASE_DATE', localdata, True)
	dc = bb.data.getVar('DEPENDENCY_CHECK', localdata, True)
	rc = bb.data.getVar('RECIPE_COMMENTS', localdata, True)

	lf = bb.utils.lockfile(logfile + ".lock")
	f = open(logfile, "a")
	f.write("%s,%s,%s,%s,%s,%s,%s,%s, %s, %s, %s\n" % \
		  (pname, maintainer, plicense, pchksum, hasrstatus, vermatch, pcurver, pupver, noupdate, noupdate_reason, rstatus))
	f.close()
	bb.utils.unlockfile(lf)
}

addtask distrodataall after do_distrodata
do_distrodataall[recrdeptask] = "do_distrodata"
do_distrodataall[nostamp] = "1"
do_distrodataall() {
	:
}

