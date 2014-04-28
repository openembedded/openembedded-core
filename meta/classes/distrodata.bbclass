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

        """
        sanity check to ensure same name and type. Match as many patterns as possible
        such as:
                gnome-common-2.20.0.tar.gz (most common format)
                gtk+-2.90.1.tar.gz
                xf86-input-synaptics-12.6.9.tar.gz
                dri2proto-2.3.tar.gz
                blktool_4.orig.tar.gz
                libid3tag-0.15.1b.tar.gz
                unzip552.tar.gz
                icu4c-3_6-src.tgz
                genext2fs_1.3.orig.tar.gz
                gst-fluendo-mp3
        """
        prefix1 = "[a-zA-Z][a-zA-Z0-9]*([\-_][a-zA-Z]\w+)*\+?[\-_]"        # match most patterns which uses "-" as separator to version digits
        prefix2 = "[a-zA-Z]+"                        # a loose pattern such as for unzip552.tar.gz
        prefix3 = "[0-9]+[\-]?[a-zA-Z]+"                        # a loose pattern such as for 80325-quicky-0.4.tar.gz
        prefix = "(%s|%s|%s)" % (prefix1, prefix2, prefix3)
        ver_regex = "(([A-Z]*\d+[a-zA-Z]*[\.\-_]*)+)"#"((\d+[\.\-_[a-z]])+)"
        # src.rpm extension was added only for rpm package. Can be removed if the rpm
        # packaged will always be considered as having to be manually upgraded
        suffix = "(tar\.gz|tgz|tar\.bz2|tar\.lz4|zip|xz|rpm|bz2|lz4|orig\.tar\.gz|tar\.xz|src\.tar\.gz|src\.tgz|svnr\d+\.tar\.bz2|stable\.tar\.gz|src\.rpm)"

        suffixtuple = ("tar.gz", "tgz", "zip", "tar.bz2", "tar.xz", "tar.lz4", "bz2", "lz4", "orig.tar.gz", "src.tar.gz", "src.rpm", "src.tgz", "svnr\d+.tar.bz2", "stable.tar.gz", "src.rpm")
        sinterstr = "(?P<name>%s?)v?(?P<ver>%s)(\-source)?" % (prefix, ver_regex)
        sdirstr = "(?P<name>%s)\.?v?(?P<ver>%s)(\-source)?[\.\-](?P<type>%s$)" % (prefix, ver_regex, suffix)

        def parse_inter(s):
                m = re.search(sinterstr, s)
                if not m:
                        return None
                else:
                        return (m.group('name'), m.group('ver'), "")

        def parse_dir(s):
                m = re.search(sdirstr, s)
                if not m:
                        return None
                else:
                        return (m.group('name'), m.group('ver'), m.group('type'))

        def modelate_version(version):
                if version[0] in ['.', '-']:
                        if version[1].isdigit():
                                version = version[1] + version[0] + version[2:len(version)]
                        else:
                                version = version[1:len(version)]

                version = re.sub('\-', '.', version)
                version = re.sub('_', '.', version)
                version = re.sub('(rc)+', '.-1.', version)
                version = re.sub('(alpha)+', '.-3.', version)
                version = re.sub('(beta)+', '.-2.', version)
                if version[0] == 'v':
                        version = version[1:len(version)]
                return version

        """
        Check whether 'new' is newer than 'old' version. We use existing vercmp() for the
        purpose. PE is cleared in comparison as it's not for build, and PV is cleared too
        for simplicity as it's somehow difficult to get from various upstream format
        """
        def __vercmp(old, new):
                (on, ov, ot) = old
                (en, ev, et) = new
                if on != en or (et and et not in suffixtuple):
                        return False
                ov = modelate_version(ov)
                ev = modelate_version(ev)

                result = bb.utils.vercmp(("0", ov, ""), ("0", ev, ""))
                if result < 0:
                    return True
                else:
                    return False

        """
        wrapper for fetch upstream directory info
                'url'        - upstream link customized by regular expression
                'd'        - database
                'tmpf'        - tmpfile for fetcher output
        We don't want to exit whole build due to one recipe error. So handle all exceptions 
        gracefully w/o leaking to outer. 
        """
        def internal_fetch_wget(url, ud, d, tmpf):
                status = "ErrFetchUnknown"

                agent = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.12) Gecko/20101027 Ubuntu/9.10 (karmic) Firefox/3.6.12"
                fetchcmd = "/usr/bin/env wget -t 1 --passive-ftp -O %s --user-agent=\"%s\" '%s'" % (tmpf.name, agent, url)
                try:
                        fetcher = bb.fetch2.wget.Wget(d)
                        fetcher._runwget(ud, d, fetchcmd, True)
                        status = "SUCC"
                except bb.fetch2.BBFetchException, e:
                        status = "ErrFetch"

                return status

        """
        Check on middle version directory such as "2.4/" in "http://xxx/2.4/pkg-2.4.1.tar.gz", 
                'url'        - upstream link customized by regular expression
                'd'        - database
                'curver' - current version
        Return new version if success, or else error in "Errxxxx" style
        """
        def check_new_dir(url, curver, ud, d):
                pn = d.getVar('PN', True)
                f = tempfile.NamedTemporaryFile(delete=False, prefix="%s-1-" % pn)
                status = internal_fetch_wget(url, ud, d, f)
                fhtml = f.read()
                if status == "SUCC" and len(fhtml):
                        newver = parse_inter(curver)

                        """
                        match "*4.1/">*4.1/ where '*' matches chars
                        N.B. add package name, only match for digits
                        """
                        regex = d.getVar('REGEX', True)
                        if regex == '':
                                regex = "^%s" %prefix
                        m = re.search("^%s" % regex, curver)
                        if m:
                                s = "%s[^\d\"]*?(\d+[\.\-_])+\d+/?" % m.group()
                        else:
                                s = "(\d+[\.\-_])+\d+/?"

                        searchstr = "[hH][rR][eE][fF]=\"%s\">" % s

                        reg = re.compile(searchstr)
                        valid = 0
                        for line in fhtml.split("\n"):
                                if line.find(curver) >= 0:
                                        valid = 1
                                m = reg.search(line)
                                if m:
                                        ver = m.group().split("\"")[1]
                                        ver = ver.strip("/")
                                        ver = parse_inter(ver)
                                        if ver and __vercmp(newver, ver) == True:
                                                newver = ver

                        """Expect a match for curver in directory list, or else it indicates unknown format"""
                        if not valid:
                                status = "ErrParseInterDir"
                        else:
                                """rejoin the path name"""
                                status = newver[0] + newver[1]
                elif not len(fhtml):
                        status = "ErrHostNoDir"

                f.close()
                if status != "ErrHostNoDir" and re.match("Err", status):
                        logpath = d.getVar('LOG_DIR', True)
                        subprocess.call("cp %s %s/" % (f.name, logpath), shell=True)
                os.unlink(f.name)
                return status

        """
        Check on the last directory to search '2.4.1' in "http://xxx/2.4/pkg-2.4.1.tar.gz", 
                'url'        - upstream link customized by regular expression
                'd'        - database
                'curname' - current package name
        Return new version if success, or else error in "Errxxxx" style
        """
        def check_new_version(url, curname, ud, d):
                """possible to have no version in pkg name, such as spectrum-fw"""
                if not re.search("\d+", curname):
                        return pcurver
                pn = d.getVar('PN', True)
                newver_regex = d.getVar('REGEX', True)
                f = tempfile.NamedTemporaryFile(delete=False, prefix="%s-2-" % pn)
                status = internal_fetch_wget(url, ud, d, f)
                fhtml = f.read()

                if status == "SUCC" and len(fhtml):
                        newver = parse_dir(curname)

                        if not newver_regex:
                                """this is the default matching pattern, if recipe does not """
                                """provide a regex expression """
                                """match "{PN}-5.21.1.tar.gz">{PN}-5.21.1.tar.gz """
                                pn1 = re.search("^%s" % prefix, curname).group()
                                s = "[^\"]*%s[^\d\"]*?(\d+[\.\-_])+[^\"]*" % pn1
                                searchstr = "[hH][rR][eE][fF]=\"%s\".*[>\"]" % s
                                reg = searchstr
                        else:
                               reg = newver_regex
                        valid = 0
                        count = 0
                        for line in fhtml.split("\n"):
                                if pn == 'kconfig-frontends':
                                        m = re.findall(reg, line)
                                        if m:
                                            valid = 1
                                            for match in m:
                                                    (on, ov, oe) = newver
                                                    ver = (on, match[0], oe)
                                                    if ver and __vercmp(newver, ver) == True:
                                                            newver = ver
                                        continue
                                count += 1
                                m = re.search(reg, line)
                                if m:
                                        valid = 1
                                        if not newver_regex:
                                                ver = m.group().split("\"")[1].split("/")[-1]
                                                if ver == "download":
                                                        ver = m.group().split("\"")[1].split("/")[-2]
                                                ver = parse_dir(ver)
                                        else:
                                                """ we cheat a little here, but we assume that the
                                                regular expression in the recipe will extract exacly
                                                the version """
                                                (on, ov, oe) = newver
                                                ver = (on, m.group('pver'), oe)
                                        if ver and __vercmp(newver, ver) == True:
                                                newver = ver
                        """Expect a match for curver in directory list, or else it indicates unknown format"""
                        if not valid:
                                status = "ErrParseDir"
                        else:
                                """newver still contains a full package name string"""
                                status = re.sub('_', '.', newver[1])
                elif not len(fhtml):
                        status = "ErrHostNoDir"

                f.close()
                """if host hasn't directory information, no need to save tmp file"""
                if status != "ErrHostNoDir" and re.match("Err", status):
                        logpath = d.getVar('LOG_DIR', True)
                        subprocess.call("cp %s %s/" % (f.name, logpath), shell=True)
                os.unlink(f.name)
                return status

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

        chk_uri = d.getVar('REGEX_URI', True)
        if not chk_uri:
               chk_uri = src_uri
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

        found = 0
        for uri in src_uri.split():
                m = re.compile('(?P<type>[^:]*)').match(uri)
                if not m:
                        raise MalformedUrl(uri)
                elif m.group('type') in ('http', 'https', 'ftp', 'cvs', 'svn', 'git'):
                        found = 1
                        pproto = m.group('type')
                        break
        if not found:
                pproto = "file"
        pupver = "N/A"
        pstatus = "ErrUnknown"

        (type, host, path, user, pswd, parm) = bb.fetch.decodeurl(uri)
        if type in ['http', 'https', 'ftp']:
                if d.getVar('PRSPV', True):
                    pcurver = d.getVar('PRSPV', True)
                else:
                    pcurver = d.getVar('PV', True)
        else:
                if d.getVar('PRSPV', True):
                    pcurver = d.getVar('PRSPV', True)
                else:
                    pcurver = d.getVar("SRCREV", True)


        if type in ['http', 'https', 'ftp']:
                ud = bb.fetch2.FetchData(uri, d)
                newver = pcurver
                altpath = path
                dirver = "-"
                curname = "-"

                """
                match version number amid the path, such as "5.7" in:
                        http://download.gnome.org/sources/${PN}/5.7/${PN}-${PV}.tar.gz
                N.B. how about sth. like "../5.7/5.8/..."? Not find such example so far :-P
                """
                m = re.search(r"[^/]*(\d+\.)+\d+([\-_]r\d+)*/", path)
                if m:
                        altpath = path.split(m.group())[0]
                        dirver = m.group().strip("/")

                        """use new path and remove param. for wget only param is md5sum"""
                        alturi = bb.fetch.encodeurl([type, host, altpath, user, pswd, {}])
                        my_uri = d.getVar('REGEX_URI', True)
                        if my_uri:
                            if d.getVar('PRSPV', True):
                                    newver = d.getVar('PRSPV', True)
                            else:
                                    newver = d.getVar('PV', True)
                        else:
                            newver = check_new_dir(alturi, dirver, ud, d)
                        altpath = path
                        if not re.match("Err", newver) and dirver != newver:
                                altpath = altpath.replace(dirver, newver, True)
                # For folder in folder cases - try to enter the folder again and then try parsing
                """Now try to acquire all remote files in current directory"""
                if not re.match("Err", newver):
                        curname = altpath.split("/")[-1]

                        """get remote name by skipping pacakge name"""
                        m = re.search(r"/.*/", altpath)
                        if not m:
                                altpath = "/"
                        else:
                                altpath = m.group()

                        chk_uri = d.getVar('REGEX_URI', True)
                        if not chk_uri:
                                alturi = bb.fetch.encodeurl([type, host, altpath, user, pswd, {}])
                        else:
                                alturi = chk_uri
                        newver = check_new_version(alturi, curname, ud, d)
                        while(newver == "ErrHostNoDir"):
                                if alturi == "/download":
                                        break
                                else:
                                        alturi = "/".join(alturi.split("/")[0:-2]) + "/download"
                                        newver = check_new_version(alturi, curname, ud, d)
                if not re.match("Err", newver):
                        pupver = newver
                        if pupver != pcurver:
                                pstatus = "UPDATE"
                        else:
                                pstatus = "MATCH"

                if re.match("Err", newver):
                        pstatus = newver + ":" + altpath + ":" + dirver + ":" + curname
        elif type == 'git':
                if user:
                        gituser = user + '@'
                else:
                        gituser = ""

                if 'protocol' in parm:
                        gitproto = parm['protocol']
                else:
                        gitproto = "git"

                # Get all tags and HEAD
                if d.getVar('GIT_REGEX', True):
                        gitcmd = "git ls-remote %s://%s%s%s %s 2>&1" % (gitproto, gituser, host, path, d.getVar('GIT_REGEX', True))
                else:
                        gitcmd = "git ls-remote %s://%s%s%s *tag* 2>&1" % (gitproto, gituser, host, path)
                gitcmd2 = "git ls-remote %s://%s%s%s HEAD 2>&1" % (gitproto, gituser, host, path)

                tmp = os.popen(gitcmd).read()
                if 'unable to connect' in tmp:
                        tmp = None
                tmp2 = os.popen(gitcmd2).read()
                if 'unable to connect' in tmp2:
                        tmp2 = None
                #This is for those repos have tag like: refs/tags/1.2.2
                phash = pversion.rsplit("+")[-1]
                if tmp:
                        tmpline = tmp.split("\n")
                        verflag = 0
                        pupver = pversion
                        for line in tmpline:
                                if len(line)==0:
                                        break;
                                puptag = line.split("/")[-1]
                                upstr_regex = d.getVar('REGEX', True)
                                if upstr_regex:
                                        puptag = re.search(upstr_regex, puptag)
                                else:
                                        puptag = re.search("(?P<pver>([0-9][\.|_]?)+)", puptag)
                                if puptag == None:
                                        continue
                                puptag = puptag.group('pver')
                                puptag = re.sub("_",".",puptag)
                                plocaltag = pupver.split("+git")[0]
                                if "git" in plocaltag:
                                        plocaltag = plocaltag.split("-")[0]
                                result = bb.utils.vercmp(("0", puptag, ""), ("0", plocaltag, ""))

                                if result > 0:
                                         verflag = 1
                                         pupver = puptag
                                elif verflag == 0 :
                                         pupver = plocaltag
                #This is for those no tag repo
                elif tmp2:
                        pupver = pversion.rsplit("+")[0]
                        phash = pupver
                else:
                        pstatus = "ErrGitAccess"
                if not ('ErrGitAccess' in pstatus):

                        latest_head = tmp2.rsplit("\t")[0][:7]
                        tmp3 = re.search('(?P<git_ver>(\d+[\.-]?)+)(?P<git_prefix>(\+git[r|\-|]?)AUTOINC\+)(?P<head_md5>([\w|_]+))', pversion)
                        tmp4 = re.search('(?P<git_ver>(\d+[\.-]?)+)(?P<git_prefix>(\+git[r|\-|]?)AUTOINC\+)(?P<head_md5>([\w|_]+))', pupver)
                        if not tmp4:
                                tmp4 = re.search('(?P<git_ver>(\d+[\.-]?)+)', pupver)

                        if tmp3:
                                # Get status of the package - MATCH/UPDATE
                                result = bb.utils.vercmp(("0", tmp3.group('git_ver'), ""), ("0",tmp3.group('git_ver') , ""))
                                # Get the latest tag
                                pstatus = 'MATCH'
                                if result < 0:
                                        latest_pv = tmp3.group('git_ver')
                                else:
                                        latest_pv = pupver
                                        if not(tmp3.group('head_md5')[:7] in latest_head) or not(latest_head in tmp3.group('head_md5')[:7]):
                                                pstatus = 'UPDATE'

                                git_prefix = tmp3.group('git_prefix')
                                pupver = latest_pv + tmp3.group('git_prefix') + latest_head
                        else:
                                if not tmp3:
                                        bb.plain("#DEBUG# Package %s: current version (%s) doesn't match the usual pattern" %(pname, pversion))
        elif type == 'svn':
                ud = bb.fetch2.FetchData(uri, d)

                svnFetcher = bb.fetch2.svn.Svn(d)
                svnFetcher.urldata_init(ud, d)
                try:
                        pupver = svnFetcher.latest_revision(ud, d, ud.names[0])
                except bb.fetch2.FetchError:
                        pstatus = "ErrSvnAccess"
                
                if pupver:
                        if pupver in pversion:
                                pstatus = "MATCH"
                        else:
                                pstatus = "UPDATE"
                else:
                        pstatus = "ErrSvnAccess"
                
                if 'rev' in ud.parm:
                        pcurver = ud.parm['rev']

                if pstatus != "ErrSvnAccess":
                        tag = pversion.rsplit("+svn")[0]
                        svn_prefix = re.search('(\+svn[r|\-]?)', pversion)
                        if tag and svn_prefix:
                                pupver = tag + svn_prefix.group() + pupver

        elif type == 'cvs':
                pupver = "HEAD"
                pstatus = "UPDATE"
        elif type == 'file':
                """local file is always up-to-date"""
                pupver = pcurver
                pstatus = "MATCH"
        else:
                pstatus = "ErrUnsupportedProto"

        if re.match("Err", pstatus):
                pstatus += ":%s%s" % (host, path)

        """Read from manual distro tracking fields as alternative"""
        pmver = d.getVar("RECIPE_UPSTREAM_VERSION", True)
        if not pmver:
                pmver = "N/A"
                pmstatus = "ErrNoRecipeData"
        else:
                if pmver == pcurver:
                        pmstatus = "MATCH"
                else:
                        pmstatus = "UPDATE"

        psrcuri = psrcuri.split()[0]
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


