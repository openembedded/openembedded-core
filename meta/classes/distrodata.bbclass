include conf/distro/include/package_regex.inc
addhandler distro_eventhandler
distro_eventhandler[eventmask] = "bb.event.BuildStarted"
python distro_eventhandler() {
    import oe.distro_check as dc
    logfile = dc.create_log_file(e.data, "distrodata.csv")
    lf = bb.utils.lockfile("%s.lock" % logfile)
    f = open(logfile, "a")
    f.write("Package,Description,Owner,License,VerMatch,Version,Upsteam,Reason,Recipe Status,Distro 1,Distro 2,Distro 3\n")
    f.close()
    bb.utils.unlockfile(lf)

    return
}

addtask distrodata_np
do_distrodata_np[nostamp] = "1"
python do_distrodata_np() {
        localdata = bb.data.createCopy(d)
        pn = d.getVar("PN", True)
        bb.note("Package Name: %s" % pn)

        import oe.distro_check as dist_check
        tmpdir = d.getVar('TMPDIR', True)
        distro_check_dir = os.path.join(tmpdir, "distro_check")
        datetime = localdata.getVar('DATETIME', True)
        dist_check.update_distro_data(distro_check_dir, datetime)

        if pn.find("-native") != -1:
            pnstripped = pn.split("-native")
            bb.note("Native Split: %s" % pnstripped)
            localdata.setVar('OVERRIDES', "pn-" + pnstripped[0] + ":" + d.getVar('OVERRIDES', True))
            bb.data.update_data(localdata)

        if pn.find("-cross") != -1:
            pnstripped = pn.split("-cross")
            bb.note("cross Split: %s" % pnstripped)
            localdata.setVar('OVERRIDES', "pn-" + pnstripped[0] + ":" + d.getVar('OVERRIDES', True))
            bb.data.update_data(localdata)

        if pn.find("-crosssdk") != -1:
            pnstripped = pn.split("-crosssdk")
            bb.note("cross Split: %s" % pnstripped)
            localdata.setVar('OVERRIDES', "pn-" + pnstripped[0] + ":" + d.getVar('OVERRIDES', True))
            bb.data.update_data(localdata)

        if pn.startswith("nativesdk-"):
            pnstripped = pn.replace("nativesdk-", "")
            bb.note("NativeSDK Split: %s" % pnstripped)
            localdata.setVar('OVERRIDES', "pn-" + pnstripped + ":" + d.getVar('OVERRIDES', True))
            bb.data.update_data(localdata)


        if pn.find("-initial") != -1:
            pnstripped = pn.split("-initial")
            bb.note("initial Split: %s" % pnstripped)
            localdata.setVar('OVERRIDES', "pn-" + pnstripped[0] + ":" + d.getVar('OVERRIDES', True))
            bb.data.update_data(localdata)

        """generate package information from .bb file"""
        pname = localdata.getVar('PN', True)
        pcurver = localdata.getVar('PV', True)
        pdesc = localdata.getVar('DESCRIPTION', True)
        if pdesc is not None:
                pdesc = pdesc.replace(',','')
                pdesc = pdesc.replace('\n','')

        pgrp = localdata.getVar('SECTION', True)
        plicense = localdata.getVar('LICENSE', True).replace(',','_')

        rstatus = localdata.getVar('RECIPE_COLOR', True)
        if rstatus is not None:
                rstatus = rstatus.replace(',','')

        pupver = localdata.getVar('RECIPE_UPSTREAM_VERSION', True)
        if pcurver == pupver:
                vermatch="1"
        else:
                vermatch="0"
        noupdate_reason = localdata.getVar('RECIPE_NO_UPDATE_REASON', True)
        if noupdate_reason is None:
                noupdate="0"
        else:
                noupdate="1"
                noupdate_reason = noupdate_reason.replace(',','')

        maintainer = localdata.getVar('RECIPE_MAINTAINER', True)
        rlrd = localdata.getVar('RECIPE_UPSTREAM_DATE', True)
        result = dist_check.compare_in_distro_packages_list(distro_check_dir, localdata)

        bb.note("DISTRO: %s,%s,%s,%s,%s,%s,%s,%s,%s\n" % \
                  (pname, pdesc, maintainer, plicense, vermatch, pcurver, pupver, noupdate_reason, rstatus))
        line = pn
        for i in result:
            line = line + "," + i
        bb.note("%s\n" % line)
}

addtask distrodata
do_distrodata[nostamp] = "1"
python do_distrodata() {
        logpath = d.getVar('LOG_DIR', True)
        bb.utils.mkdirhier(logpath)
        logfile = os.path.join(logpath, "distrodata.csv")

        import oe.distro_check as dist_check
        localdata = bb.data.createCopy(d)
        tmpdir = d.getVar('TMPDIR', True)
        distro_check_dir = os.path.join(tmpdir, "distro_check")
        datetime = localdata.getVar('DATETIME', True)
        dist_check.update_distro_data(distro_check_dir, datetime)

        pn = d.getVar("PN", True)
        bb.note("Package Name: %s" % pn)

        if pn.find("-native") != -1:
            pnstripped = pn.split("-native")
            bb.note("Native Split: %s" % pnstripped)
            localdata.setVar('OVERRIDES', "pn-" + pnstripped[0] + ":" + d.getVar('OVERRIDES', True))
            bb.data.update_data(localdata)

        if pn.startswith("nativesdk-"):
            pnstripped = pn.replace("nativesdk-", "")
            bb.note("NativeSDK Split: %s" % pnstripped)
            localdata.setVar('OVERRIDES', "pn-" + pnstripped + ":" + d.getVar('OVERRIDES', True))
            bb.data.update_data(localdata)

        if pn.find("-cross") != -1:
            pnstripped = pn.split("-cross")
            bb.note("cross Split: %s" % pnstripped)
            localdata.setVar('OVERRIDES', "pn-" + pnstripped[0] + ":" + d.getVar('OVERRIDES', True))
            bb.data.update_data(localdata)

        if pn.find("-crosssdk") != -1:
            pnstripped = pn.split("-crosssdk")
            bb.note("cross Split: %s" % pnstripped)
            localdata.setVar('OVERRIDES', "pn-" + pnstripped[0] + ":" + d.getVar('OVERRIDES', True))
            bb.data.update_data(localdata)

        if pn.find("-initial") != -1:
            pnstripped = pn.split("-initial")
            bb.note("initial Split: %s" % pnstripped)
            localdata.setVar('OVERRIDES', "pn-" + pnstripped[0] + ":" + d.getVar('OVERRIDES', True))
            bb.data.update_data(localdata)

        """generate package information from .bb file"""
        pname = localdata.getVar('PN', True)
        pcurver = localdata.getVar('PV', True)
        pdesc = localdata.getVar('DESCRIPTION', True)
        if pdesc is not None:
                pdesc = pdesc.replace(',','')
                pdesc = pdesc.replace('\n','')

        pgrp = localdata.getVar('SECTION', True)
        plicense = localdata.getVar('LICENSE', True).replace(',','_')

        rstatus = localdata.getVar('RECIPE_COLOR', True)
        if rstatus is not None:
                rstatus = rstatus.replace(',','')

        pupver = localdata.getVar('RECIPE_UPSTREAM_VERSION', True)
        if pcurver == pupver:
                vermatch="1"
        else:
                vermatch="0"

        noupdate_reason = localdata.getVar('RECIPE_NO_UPDATE_REASON', True)
        if noupdate_reason is None:
                noupdate="0"
        else:
                noupdate="1"
                noupdate_reason = noupdate_reason.replace(',','')

        maintainer = localdata.getVar('RECIPE_MAINTAINER', True)
        rlrd = localdata.getVar('RECIPE_UPSTREAM_DATE', True)
        # do the comparison
        result = dist_check.compare_in_distro_packages_list(distro_check_dir, localdata)

        lf = bb.utils.lockfile("%s.lock" % logfile)
        f = open(logfile, "a")
        f.write("%s,%s,%s,%s,%s,%s,%s,%s,%s" % \
                  (pname, pdesc, maintainer, plicense, vermatch, pcurver, pupver, noupdate_reason, rstatus))
        line = ""
        for i in result:
            line = line + "," + i
        f.write(line + "\n")
        f.close()
        bb.utils.unlockfile(lf)
}

addtask distrodataall after do_distrodata
do_distrodataall[recrdeptask] = "do_distrodataall do_distrodata"
do_distrodataall[recideptask] = "do_${BB_DEFAULT_TASK}"
do_distrodataall[nostamp] = "1"
do_distrodataall() {
        :
}

addhandler checkpkg_eventhandler
checkpkg_eventhandler[eventmask] = "bb.event.BuildStarted bb.event.BuildCompleted"
python checkpkg_eventhandler() {
    def parse_csv_file(filename):
        package_dict = {}
        fd = open(filename, "r")
        lines = fd.read().rsplit("\n")
        fd.close()

        first_line = ''
        index = 0
        for line in lines:
            #Skip the first line
            if index == 0:
                first_line = line
                index += 1
                continue
            elif line == '':
                continue
            index += 1
            package_name = line.rsplit("\t")[0]
            if '-native' in package_name or 'nativesdk-' in package_name:
                original_name = package_name.rsplit('-native')[0]
                if original_name == '':
                    original_name = package_name.rsplit('nativesdk-')[0]
                if original_name in package_dict:
                    continue
                else:
                    package_dict[package_name] = line
            else:
                new_name = package_name + "-native"
                if not(new_name in package_dict):
                    new_name = 'nativesdk-' + package_name
                if new_name in package_dict:
                    del package_dict[new_name]
                package_dict[package_name] = line

        fd = open(filename, "w")
        fd.write("%s\n"%first_line)
        for el in package_dict:
            fd.write(package_dict[el] + "\n")
        fd.close()

        del package_dict

    if bb.event.getName(e) == "BuildStarted":
        import oe.distro_check as dc
        logfile = dc.create_log_file(e.data, "checkpkg.csv")

        lf = bb.utils.lockfile("%s.lock" % logfile)
        f = open(logfile, "a")
        f.write("Package\tVersion\tUpver\tLicense\tSection\tHome\tRelease\tDepends\tBugTracker\tPE\tDescription\tStatus\tTracking\tURI\tMAINTAINER\tNoUpReason\n")
        f.close()
        bb.utils.unlockfile(lf)
    elif bb.event.getName(e) == "BuildCompleted":
        import os
        filename = "tmp/log/checkpkg.csv"
        if os.path.isfile(filename):
            lf = bb.utils.lockfile("%s.lock"%filename)
            parse_csv_file(filename)
            bb.utils.unlockfile(lf)
    return
}

addtask checkpkg
do_checkpkg[nostamp] = "1"
python do_checkpkg() {
        localdata = bb.data.createCopy(d)
        import re
        import tempfile
        import subprocess

        """first check whether a uri is provided"""
        src_uri = d.getVar('SRC_URI', True)
        if not src_uri:
                return

        """initialize log files."""
        logpath = d.getVar('LOG_DIR', True)
        bb.utils.mkdirhier(logpath)
        logfile = os.path.join(logpath, "checkpkg.csv")

        """generate package information from .bb file"""
        pname = d.getVar('PN', True)

        if pname.find("-native") != -1:
            if d.getVar('BBCLASSEXTEND', True):
                    return
            pnstripped = pname.split("-native")
            bb.note("Native Split: %s" % pnstripped)
            localdata.setVar('OVERRIDES', "pn-" + pnstripped[0] + ":" + d.getVar('OVERRIDES', True))
            bb.data.update_data(localdata)

        if pname.startswith("nativesdk-"):
            if d.getVar('BBCLASSEXTEND', True):
                    return
            pnstripped = pname.replace("nativesdk-", "")
            bb.note("NativeSDK Split: %s" % pnstripped)
            localdata.setVar('OVERRIDES', "pn-" + pnstripped + ":" + d.getVar('OVERRIDES', True))
            bb.data.update_data(localdata)

        if pname.find("-cross") != -1:
            pnstripped = pname.split("-cross")
            bb.note("cross Split: %s" % pnstripped)
            localdata.setVar('OVERRIDES', "pn-" + pnstripped[0] + ":" + d.getVar('OVERRIDES', True))
            bb.data.update_data(localdata)

        if pname.find("-initial") != -1:
            pnstripped = pname.split("-initial")
            bb.note("initial Split: %s" % pnstripped)
            localdata.setVar('OVERRIDES', "pn-" + pnstripped[0] + ":" + d.getVar('OVERRIDES', True))
            bb.data.update_data(localdata)

        pdesc = localdata.getVar('DESCRIPTION', True)
        pgrp = localdata.getVar('SECTION', True)
        if localdata.getVar('PRSPV', True):
                pversion = localdata.getVar('PRSPV', True)
        else:
                pversion = localdata.getVar('PV', True)
        plicense = localdata.getVar('LICENSE', True)
        psection = localdata.getVar('SECTION', True)
        phome = localdata.getVar('HOMEPAGE', True)
        prelease = localdata.getVar('PR', True)
        pdepends = localdata.getVar('DEPENDS', True)
        pbugtracker = localdata.getVar('BUGTRACKER', True)
        ppe = localdata.getVar('PE', True)
        psrcuri = localdata.getVar('SRC_URI', True)
        maintainer = localdata.getVar('RECIPE_MAINTAINER', True)

        """ Get upstream version version """
        pupver = None
        pstatus = "ErrUnknown"
        found = 0

        for uri in src_uri.split():
            m = re.compile('(?P<type>[^:]*)').match(uri)
            if not m:
                raise MalformedUrl(uri)
            elif m.group('type') in ('http', 'https', 'ftp', 'cvs', 'svn', 'git'):
                found = 1
                psrcuri = uri
                pproto = m.group('type')
                break
        if not found:
                pproto = "file"

        if pproto in ['http', 'https', 'ftp', 'git']:
            try:
                ud = bb.fetch2.FetchData(psrcuri, d)
                pupver = ud.method.latest_versionstring(ud, d)
                if pproto == 'git':
                    if pupver == "":
                        pupver = pversion.rsplit("+")[0]
                    if re.search(pversion, "gitrAUTOINC"):
                        pupver += "+gitrAUTOINC+"
                    else:
                        pupver += "+gitAUTOINC+"
                    latest_revision = ud.method.latest_revision(ud, d, ud.names[0])
                    pupver += latest_revision[:10]
            except Exception as inst:
                bb.warn("%s: unexpected error: %s" % (pname, repr(inst)))
                pstatus = "ErrAccess"
        elif pproto == "file":
            """Local files are always updated"""
            pupver = pversion
        else:
            pstatus = "ErrUnsupportedProto"
            bb.note("do_checkpkg, protocol %s isn't implemented" % pproto)

        if not pupver:
            pupver = "N/A"
        elif pupver == pversion:
            pstatus = "MATCH"
        else:
            pstatus = "UPDATE"

        """Read from manual distro tracking fields as alternative"""
        pmver = d.getVar("RECIPE_UPSTREAM_VERSION", True)
        if not pmver:
            pmver = "N/A"
            pmstatus = "ErrNoRecipeData"
        elif pmver == pupver:
            pmstatus = "MATCH"
        else:
            pmstatus = "UPDATE"

        pdepends = "".join(pdepends.split("\t"))
        pdesc = "".join(pdesc.split("\t"))
        no_upgr_reason = d.getVar('RECIPE_NO_UPDATE_REASON', True)
        lf = bb.utils.lockfile("%s.lock" % logfile)
        f = open(logfile, "a")
        f.write("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n" % \
                  (pname,pversion,pupver,plicense,psection, phome,prelease, pdepends,pbugtracker,ppe,pdesc,pstatus,pmver,psrcuri,maintainer, no_upgr_reason))
        f.close()
        bb.utils.unlockfile(lf)
}

addtask checkpkgall after do_checkpkg
do_checkpkgall[recrdeptask] = "do_checkpkgall do_checkpkg"
do_checkpkgall[recideptask] = "do_${BB_DEFAULT_TASK}"
do_checkpkgall[nostamp] = "1"
do_checkpkgall() {
        :
}

addhandler distro_check_eventhandler
distro_check_eventhandler[eventmask] = "bb.event.BuildStarted"
python distro_check_eventhandler() {
    """initialize log files."""
    import oe.distro_check as dc
    result_file = dc.create_log_file(e.data, "distrocheck.csv")
    return
}

addtask distro_check
do_distro_check[nostamp] = "1"
python do_distro_check() {
    """checks if the package is present in other public Linux distros"""
    import oe.distro_check as dc
    import shutil
    if bb.data.inherits_class('native', d) or bb.data.inherits_class('cross', d) or bb.data.inherits_class('sdk', d) or bb.data.inherits_class('crosssdk', d) or bb.data.inherits_class('nativesdk',d):
        return

    localdata = bb.data.createCopy(d)
    bb.data.update_data(localdata)
    tmpdir = d.getVar('TMPDIR', True)
    distro_check_dir = os.path.join(tmpdir, "distro_check")
    logpath = d.getVar('LOG_DIR', True)
    bb.utils.mkdirhier(logpath)
    result_file = os.path.join(logpath, "distrocheck.csv")
    datetime = localdata.getVar('DATETIME', True)
    dc.update_distro_data(distro_check_dir, datetime)

    # do the comparison
    result = dc.compare_in_distro_packages_list(distro_check_dir, d)

    # save the results
    dc.save_distro_check_result(result, datetime, result_file, d)
}

addtask distro_checkall after do_distro_check
do_distro_checkall[recrdeptask] = "do_distro_checkall do_distro_check"
do_distro_checkall[recideptask] = "do_${BB_DEFAULT_TASK}"
do_distro_checkall[nostamp] = "1"
do_distro_checkall() {
        :
}
#
#Check Missing License Text.
#Use this task to generate the missing license text data for pkg-report system,
#then we can search those recipes which license text isn't exsit in common-licenses directory
#
addhandler checklicense_eventhandler
checklicense_eventhandler[eventmask] = "bb.event.BuildStarted"
python checklicense_eventhandler() {
    """initialize log files."""
    import oe.distro_check as dc
    logfile = dc.create_log_file(e.data, "missinglicense.csv")
    lf = bb.utils.lockfile("%s.lock" % logfile)
    f = open(logfile, "a")
    f.write("Package\tLicense\tMissingLicense\n")
    f.close()
    bb.utils.unlockfile(lf)
    return
}

addtask checklicense
do_checklicense[nostamp] = "1"
python do_checklicense() {
    import shutil
    logpath = d.getVar('LOG_DIR', True)
    bb.utils.mkdirhier(logpath)
    pn = d.getVar('PN', True)
    logfile = os.path.join(logpath, "missinglicense.csv")
    generic_directory = d.getVar('COMMON_LICENSE_DIR', True)
    license_types = d.getVar('LICENSE', True)
    for license_type in ((license_types.replace('+', '').replace('|', '&')
                          .replace('(', '').replace(')', '').replace(';', '')
                          .replace(',', '').replace(" ", "").split("&"))):
        if not os.path.isfile(os.path.join(generic_directory, license_type)):
            lf = bb.utils.lockfile("%s.lock" % logfile)
            f = open(logfile, "a")
            f.write("%s\t%s\t%s\n" % \
                (pn,license_types,license_type))
            f.close()
            bb.utils.unlockfile(lf)
    return
}

addtask checklicenseall after do_checklicense
do_checklicenseall[recrdeptask] = "do_checklicenseall do_checklicense"
do_checklicenseall[recideptask] = "do_${BB_DEFAULT_TASK}"
do_checklicenseall[nostamp] = "1"
do_checklicenseall() {
        :
}


