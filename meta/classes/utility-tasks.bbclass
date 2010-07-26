addtask listtasks
do_listtasks[nostamp] = "1"
python do_listtasks() {
	import sys
	# emit variables and shell functions
	#bb.data.emit_env(sys.__stdout__, d)
	# emit the metadata which isnt valid shell
	for e in d.keys():
		if bb.data.getVarFlag(e, 'task', d):
			sys.__stdout__.write("%s\n" % e)
}

addtask clean
do_clean[dirs] = "${TOPDIR}"
do_clean[nostamp] = "1"
python do_clean() {
	"""clear the build and temp directories"""
	dir = bb.data.expand("${WORKDIR}", d)
	if dir == '//': raise bb.build.FuncFailed("wrong DATADIR")
	bb.note("removing " + dir)
	os.system('rm -rf ' + dir)

	dir = "%s.*" % bb.data.expand(bb.data.getVar('STAMP', d), d)
	bb.note("removing " + dir)
	os.system('rm -f '+ dir)
}

addtask rebuild after do_${BB_DEFAULT_TASK}
do_rebuild[dirs] = "${TOPDIR}"
do_rebuild[nostamp] = "1"
python do_rebuild() {
	"""rebuild a package"""
}

#addtask mrproper
#do_mrproper[dirs] = "${TOPDIR}"
#do_mrproper[nostamp] = "1"
#python do_mrproper() {
#	"""clear downloaded sources, build and temp directories"""
#	dir = bb.data.expand("${DL_DIR}", d)
#	if dir == '/': bb.build.FuncFailed("wrong DATADIR")
#	bb.debug(2, "removing " + dir)
#	os.system('rm -rf ' + dir)
#	bb.build.exec_func('do_clean', d)
#}

addtask checkpkg
do_checkpkg[nostamp] = "1"
python do_checkpkg() {
	import sys
	import re
	import tempfile

	"""
	sanity check to ensure same name and type. Match as many patterns as possible
	such as:
		gnome-common-2.20.0.tar.gz (most common format)
		gtk+-2.90.1.tar.gz
		xf86-intput-synaptics-12.6.9.tar.gz
		dri2proto-2.3.tar.gz
		blktool_4.orig.tar.gz
		libid3tag-0.15.1b.tar.gz
		unzip552.tar.gz
		icu4c-3_6-src.tgz
		genext2fs_1.3.orig.tar.gz
		gst-fluendo-mp3
	"""
	prefix1 = "[a-zA-Z][a-zA-Z0-9]*([\-_][a-zA-Z]\w+)*[\-_]"	# match most patterns which uses "-" as separator to version digits
	prefix2 = "[a-zA-Z]+"			# a loose pattern such as for unzip552.tar.gz
	prefix = "(%s|%s)" % (prefix1, prefix2)
	suffix = "(tar\.gz|tgz|tar\.bz2|zip)"
	suffixtuple = ("tar.gz", "tgz", "zip", "tar.bz2")

	sinterstr = "(?P<name>%s?)(?P<ver>.*)" % prefix
	sdirstr = "(?P<name>%s)(?P<ver>.*)\.(?P<type>%s$)" % (prefix, suffix)

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

	"""
	Check whether 'new' is newer than 'old' version. We use existing vercmp() for the
	purpose. PE is cleared in comparison as it's not for build, and PV is cleared too
	for simplicity as it's somehow difficult to get from various upstream format
	"""
	def __vercmp(old, new):
		(on, ov, ot) = old
		(en, ev, et) = new
		if on != en or (et and et not in suffixtuple):
			return 0

		ov = re.search("\d+[^a-zA-Z]+", ov).group()
		ev = re.search("\d+[^a-zA-Z]+", ev).group()
		return bb.utils.vercmp(("0", ov, ""), ("0", ev, ""))

	"""
	wrapper for fetch upstream directory info
		'url'	- upstream link customized by regular expression
		'd'	- database
		'tmpf'	- tmpfile for fetcher output
	We don't want to exit whole build due to one recipe error. So handle all exceptions 
	gracefully w/o leaking to outer. 
	"""
	def internal_fetch_wget(url, d, tmpf):
		status = "ErrFetchUnknown"
		try: 
			"""
			Clear internal url cache as it's a temporary check. Not doing so will have 
			bitbake check url multiple times when looping through a single url
			"""
			fn = bb.data.getVar('FILE', d, 1)
			bb.fetch.urldata_cache[fn] = {}
			bb.fetch.init([url], d)
		except bb.fetch.NoMethodError:
			status = "ErrFetchNoMethod"
		except:
			status = "ErrInitUrlUnknown"
		else:
			"""
			To avoid impacting bitbake build engine, this trick is required for reusing bitbake
			interfaces. bb.fetch.go() is not appliable as it checks downloaded content in ${DL_DIR}
			while we don't want to pollute that place. So bb.fetch.checkstatus() is borrowed here
			which is designed for check purpose but we override check command for our own purpose
			"""
			ld = bb.data.createCopy(d)
			bb.data.setVar('CHECKCOMMAND_wget', "/usr/bin/env wget -t 1 --passive-ftp -O %s '${URI}'" \
					% tmpf.name, d)
			bb.data.update_data(ld)

			try:
				bb.fetch.checkstatus(ld)
			except bb.fetch.MissingParameterError:
				status = "ErrMissParam"
			except bb.fetch.FetchError:
				status = "ErrFetch"
			except bb.fetch.MD5SumError:
				status = "ErrMD5Sum"
			except:
				status = "ErrFetchUnknown"
			else:
				status = "SUCC" 
		return status

	"""
	Check on middle version directory such as "2.4/" in "http://xxx/2.4/pkg-2.4.1.tar.gz", 
		'url'	- upstream link customized by regular expression
		'd'	- database
		'curver' - current version
	Return new version if success, or else error in "Errxxxx" style
	"""
	def check_new_dir(url, curver, d):
		pn = bb.data.getVar('PN', d, 1)
		f = tempfile.NamedTemporaryFile(delete=False, prefix="%s-1-" % pn)
		status = internal_fetch_wget(url, d, f)
		fhtml = f.read()

		if status == "SUCC" and len(fhtml):
			newver = parse_inter(curver)

			"""
			match "*4.1/">*4.1/ where '*' matches chars
			N.B. add package name, only match for digits
			"""
			m = re.search("^%s" % prefix, curver)
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
					if ver and __vercmp(newver, ver) < 0:
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
			logpath = bb.data.getVar('LOG_DIR', d, 1)
			os.system("cp %s %s/" % (f.name, logpath))
		os.unlink(f.name)
		return status

	"""
	Check on the last directory to search '2.4.1' in "http://xxx/2.4/pkg-2.4.1.tar.gz", 
		'url'	- upstream link customized by regular expression
		'd'	- database
		'curname' - current package name
	Return new version if success, or else error in "Errxxxx" style
	"""
	def check_new_version(url, curname, d):
		"""possible to have no version in pkg name, such as spectrum-fw"""
		if not re.search("\d+", curname):
			return pcurver
		pn = bb.data.getVar('PN', d, 1)
		f = tempfile.NamedTemporaryFile(delete=False, prefix="%s-2-" % pn)
		status = internal_fetch_wget(url, d, f)
		fhtml = f.read()

		if status == "SUCC" and len(fhtml):
			newver = parse_dir(curname)

			"""match "{PN}-5.21.1.tar.gz">{PN}-5.21.1.tar.gz """
			pn1 = re.search("^%s" % prefix, curname).group()
			s = "[^\"]*%s[^\d\"]*?(\d+[\.\-_])+[^\"]*" % pn1
			searchstr = "[hH][rR][eE][fF]=\"%s\">" % s
			reg = re.compile(searchstr)
	
			valid = 0
			for line in fhtml.split("\n"):
				m = reg.search(line)
				if m:
					valid = 1
					ver = m.group().split("\"")[1].split("/")[-1]
					ver = parse_dir(ver)
					if ver and __vercmp(newver, ver) < 0:
						newver = ver
	
			"""Expect a match for curver in directory list, or else it indicates unknown format"""
			if not valid:
				status = "ErrParseDir"
			else:
				"""newver still contains a full package name string"""
				status = re.search("(\d+[.\-_])*\d+", newver[1]).group()
		elif not len(fhtml):
			status = "ErrHostNoDir"

		f.close()
		"""if host hasn't directory information, no need to save tmp file"""
		if status != "ErrHostNoDir" and re.match("Err", status):
			logpath = bb.data.getVar('LOG_DIR', d, 1)
			os.system("cp %s %s/" % (f.name, logpath))
		os.unlink(f.name)
		return status

	"""first check whether a uri is provided"""
	src_uri = bb.data.getVar('SRC_URI', d, 1)
	if not src_uri:
		return

	"""initialize log files."""
	logpath = bb.data.getVar('LOG_DIR', d, 1)
	bb.utils.mkdirhier(logpath)
	logfile = os.path.join(logpath, "poky_pkg_info.log.%s" % bb.data.getVar('DATETIME', d, 1))
	if not os.path.exists(logfile):
		slogfile = os.path.join(logpath, "poky_pkg_info.log")
		if os.path.exists(slogfile):
			os.remove(slogfile)
		os.system("touch %s" % logfile)
		os.symlink(logfile, slogfile)

	"""generate package information from .bb file"""
	pname = bb.data.getVar('PN', d, 1)
	pdesc = bb.data.getVar('DESCRIPTION', d, 1)
	pgrp = bb.data.getVar('SECTION', d, 1)

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

	(type, host, path, user, pswd, parm) = bb.decodeurl(uri)
	if type in ['http', 'https', 'ftp']:
		pcurver = bb.data.getVar('PV', d, 1)
	else:
		pcurver = bb.data.getVar("SRCREV", d, 1)

	if type in ['http', 'https', 'ftp']:
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
			alturi = bb.encodeurl([type, host, altpath, user, pswd, {}])
	
			newver = check_new_dir(alturi, dirver, d)
			altpath = path
			if not re.match("Err", newver) and dirver != newver:
				altpath = altpath.replace(dirver, newver, 1)
				
		"""Now try to acquire all remote files in current directory"""
		if not re.match("Err", newver):
			curname = altpath.split("/")[-1]
	
			"""get remote name by skipping pacakge name"""
			m = re.search(r"/.*/", altpath)
			if not m:
				altpath = "/"
			else:
				altpath = m.group()
	
			alturi = bb.encodeurl([type, host, altpath, user, pswd, {}])
			newver = check_new_version(alturi, curname, d)
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
			gitproto = "rsync"

		gitcmd = "git ls-remote %s://%s%s%s HEAD 2>&1" % (gitproto, gituser, host, path)
		print gitcmd
		ver = os.popen(gitcmd).read()
		if ver and re.search("HEAD", ver):
			pupver = ver.split("\t")[0]
			if pcurver == pupver:
				pstatus = "MATCH"
			else:
				pstatus = "UPDATE"
		else:
			pstatus = "ErrGitAccess"
	elif type == 'svn':
		options = []
		if user:
			options.append("--username %s" % user)
		if pswd:
			options.append("--password %s" % pswd)
		svnproto = 'svn'
		if 'proto' in parm:
			svnproto = parm['proto']
		if 'rev' in parm:
			pcurver = parm['rev']

		svncmd = "svn info %s %s://%s%s/%s/ 2>&1" % (" ".join(options), svnproto, host, path, parm["module"])
		print svncmd
		svninfo = os.popen(svncmd).read()
		for line in svninfo.split("\n"):
			if re.search("^Last Changed Rev:", line):
				pupver = line.split(" ")[-1]
				if pcurver == pupver:
					pstatus = "MATCH"
				else:
					pstatus = "UPDATE"

		if re.match("Err", pstatus):
			pstatus = "ErrSvnAccess"
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
	pmver = bb.data.getVar("RECIPE_LATEST_VERSION", d, 1)
	if not pmver:
		pmver = "N/A"
		pmstatus = "ErrNoRecipeData"
	else:
		if pmver == pcurver:
			pmstatus = "MATCH"
		else:
			pmstatus = "UPDATE"
	
	lf = bb.utils.lockfile(logfile + ".lock")
	f = open(logfile, "a")
	f.write("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n" % \
		  (pname, pgrp, pproto, pcurver, pmver, pupver, pmstatus, pstatus, pdesc))
	f.close()
	bb.utils.unlockfile(lf)
}

addtask checkpkgall after do_checkpkg
do_checkpkgall[recrdeptask] = "do_checkpkg"
do_checkpkgall[nostamp] = "1"
do_checkpkgall() {
	:
}

addtask checkuri
do_checkuri[nostamp] = "1"
python do_checkuri() {
	import sys

	localdata = bb.data.createCopy(d)
	bb.data.update_data(localdata)

	src_uri = bb.data.getVar('SRC_URI', localdata, 1)

	try:
		bb.fetch.init(src_uri.split(),d)
	except bb.fetch.NoMethodError:
		(type, value, traceback) = sys.exc_info()
		raise bb.build.FuncFailed("No method: %s" % value)

	try:
		bb.fetch.checkstatus(localdata)
	except bb.fetch.MissingParameterError:
		(type, value, traceback) = sys.exc_info()
		raise bb.build.FuncFailed("Missing parameters: %s" % value)
	except bb.fetch.FetchError:
		(type, value, traceback) = sys.exc_info()
		raise bb.build.FuncFailed("Fetch failed: %s" % value)
	except bb.fetch.MD5SumError:
		(type, value, traceback) = sys.exc_info()
		raise bb.build.FuncFailed("MD5  failed: %s" % value)
	except:
		(type, value, traceback) = sys.exc_info()
		raise bb.build.FuncFailed("Unknown fetch Error: %s" % value)
}

addtask checkuriall after do_checkuri
do_checkuriall[recrdeptask] = "do_checkuri"
do_checkuriall[nostamp] = "1"
do_checkuriall() {
	:
}

addtask fetchall after do_fetch
do_fetchall[recrdeptask] = "do_fetch"
do_fetchall() {
	:
}

addtask buildall after do_build
do_buildall[recrdeptask] = "do_build"
do_buildall() {
	:
}

addtask distro_check
do_distro_check[nostamp] = "1"
python do_distro_check() {
    """checks if the package is present in other public Linux distros"""
    import oe.distro_check as dc
    localdata = bb.data.createCopy(d)
    bb.data.update_data(localdata)

    tmpdir = bb.data.getVar('TMPDIR', localdata, 1)
    distro_check_dir = os.path.join(tmpdir, "distro_check")
    datetime = bb.data.getVar('DATETIME', localdata, 1)

    # if distro packages list data is old then rebuild it 
    dc.update_distro_data(distro_check_dir, datetime)

    # do the comparison
    result = dc.compare_in_distro_packages_list(distro_check_dir, d)

    # save the results
    dc.save_distro_check_result(result, datetime, d)
}

