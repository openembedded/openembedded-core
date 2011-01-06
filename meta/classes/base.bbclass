BB_DEFAULT_TASK ?= "build"

inherit patch
inherit staging

inherit mirrors
inherit utils
inherit utility-tasks
inherit metadata_scm

python sys_path_eh () {
    if isinstance(e, bb.event.ConfigParsed):
        import sys
        import os
        import time

        bbpath = e.data.getVar("BBPATH", True).split(":")
        sys.path[0:0] = [os.path.join(dir, "lib") for dir in bbpath]

        def inject(name, value):
            """Make a python object accessible from everywhere for the metadata"""
            if hasattr(bb.utils, "_context"):
                bb.utils._context[name] = value
            else:
                __builtins__[name] = value

        import oe.path
        import oe.utils
        inject("bb", bb)
        inject("sys", sys)
        inject("time", time)
        inject("oe", oe)
}

addhandler sys_path_eh

die() {
	oefatal "$*"
}

oenote() {
	echo "NOTE:" "$*"
}

oewarn() {
	echo "WARNING:" "$*"
}

oefatal() {
	echo "FATAL:" "$*"
	exit 1
}

oedebug() {
	test $# -ge 2 || {
		echo "Usage: oedebug level \"message\""
		exit 1
	}

	test ${OEDEBUG:-0} -ge $1 && {
		shift
		echo "DEBUG:" $*
	}
}

oe_runmake() {
	if [ x"$MAKE" = x ]; then MAKE=make; fi
	oenote ${MAKE} ${EXTRA_OEMAKE} "$@"
	${MAKE} ${EXTRA_OEMAKE} "$@" || die "oe_runmake failed"
}


def base_dep_prepend(d):
	#
	# Ideally this will check a flag so we will operate properly in
	# the case where host == build == target, for now we don't work in
	# that case though.
	#

	deps = ""
	# INHIBIT_DEFAULT_DEPS doesn't apply to the patch command.  Whether or  not
	# we need that built is the responsibility of the patch function / class, not
	# the application.
	if not bb.data.getVar('INHIBIT_DEFAULT_DEPS', d):
		if (bb.data.getVar('HOST_SYS', d, 1) !=
	     	    bb.data.getVar('BUILD_SYS', d, 1)):
			deps += " virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}compilerlibs virtual/libc "
	return deps

DEPENDS_prepend="${@base_dep_prepend(d)} "
DEPENDS_virtclass-native_prepend="${@base_dep_prepend(d)} "
DEPENDS_virtclass-nativesdk_prepend="${@base_dep_prepend(d)} "

FILESPATH = "${@base_set_filespath([ "${FILE_DIRNAME}/${PF}", "${FILE_DIRNAME}/${P}", "${FILE_DIRNAME}/${PN}", "${FILE_DIRNAME}/${BP}", "${FILE_DIRNAME}/${BPN}", "${FILE_DIRNAME}/files", "${FILE_DIRNAME}" ], d)}"
# THISDIR only works properly with imediate expansion as it has to run
# in the context of the location its used (:=)
THISDIR = "${@os.path.dirname(bb.data.getVar('FILE', d, True))}"

SCENEFUNCS += "base_scenefunction"
	
python base_scenefunction () {
	stamp = bb.data.getVar('STAMP', d, 1) + ".needclean"
	if os.path.exists(stamp):
		bb.build.exec_func("do_clean", d)
}

python base_do_setscene () {
	for f in (bb.data.getVar('SCENEFUNCS', d, 1) or '').split():
		bb.build.exec_func(f, d)
	if not os.path.exists(bb.build.stampfile("do_setscene", d)):
		bb.build.make_stamp("do_setscene", d)
}
do_setscene[selfstamp] = "1"
addtask setscene before do_fetch

addtask fetch
do_fetch[dirs] = "${DL_DIR}"
python base_do_fetch() {
	import sys

	localdata = bb.data.createCopy(d)
	bb.data.update_data(localdata)

	src_uri = bb.data.getVar('SRC_URI', localdata, 1)
	if not src_uri:
		return 1

	try:
		bb.fetch.init(src_uri.split(),d)
	except bb.fetch.NoMethodError:
		(type, value, traceback) = sys.exc_info()
		raise bb.build.FuncFailed("No method: %s" % value)
	except bb.MalformedUrl:
		(type, value, traceback) = sys.exc_info()
		raise bb.build.FuncFailed("Malformed URL: %s" % value)

	try:
		bb.fetch.go(localdata)
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

def subprocess_setup():
	import signal
	# Python installs a SIGPIPE handler by default. This is usually not what
	# non-Python subprocesses expect.
	# SIGPIPE errors are known issues with gzip/bash
	signal.signal(signal.SIGPIPE, signal.SIG_DFL)

def oe_unpack_file(file, data, url = None):
	import subprocess
	if not url:
		url = "file://%s" % file
	dots = file.split(".")
	if dots[-1] in ['gz', 'bz2', 'Z']:
		efile = os.path.join(bb.data.getVar('WORKDIR', data, 1),os.path.basename('.'.join(dots[0:-1])))
	else:
		efile = file
	cmd = None
	if file.endswith('.tar'):
		cmd = 'tar x --no-same-owner -f %s' % file
	elif file.endswith('.tgz') or file.endswith('.tar.gz') or file.endswith('.tar.Z'):
		cmd = 'tar xz --no-same-owner -f %s' % file
	elif file.endswith('.tbz') or file.endswith('.tbz2') or file.endswith('.tar.bz2'):
		cmd = 'bzip2 -dc %s | tar x --no-same-owner -f -' % file
	elif file.endswith('.gz') or file.endswith('.Z') or file.endswith('.z'):
		cmd = 'gzip -dc %s > %s' % (file, efile)
	elif file.endswith('.bz2'):
		cmd = 'bzip2 -dc %s > %s' % (file, efile)
	elif file.endswith('.tar.xz'):
		cmd = 'xz -dc %s | tar x --no-same-owner -f -' % file
	elif file.endswith('.xz'):
		cmd = 'xz -dc %s > %s' % (file, efile)
	elif file.endswith('.zip') or file.endswith('.jar'):
		cmd = 'unzip -q -o'
		(type, host, path, user, pswd, parm) = bb.decodeurl(url)
		if 'dos' in parm:
			cmd = '%s -a' % cmd
		cmd = "%s '%s'" % (cmd, file)
	elif os.path.isdir(file):
		filesdir = os.path.realpath(bb.data.getVar("FILESDIR", data, 1))
		destdir = "."
		if file[0:len(filesdir)] == filesdir:
			destdir = file[len(filesdir):file.rfind('/')]
			destdir = destdir.strip('/')
			if len(destdir) < 1:
				destdir = "."
			elif not os.access("%s/%s" % (os.getcwd(), destdir), os.F_OK):
				os.makedirs("%s/%s" % (os.getcwd(), destdir))
		cmd = 'cp -pPR %s %s/%s/' % (file, os.getcwd(), destdir)
	else:
		(type, host, path, user, pswd, parm) = bb.decodeurl(url)
		if not 'patch' in parm:
			# The "destdir" handling was specifically done for FILESPATH
			# items.  So, only do so for file:// entries.
			if type == "file" and path.find("/") != -1:
				destdir = path.rsplit("/", 1)[0]
			else:
				destdir = "."
			bb.mkdirhier("%s/%s" % (os.getcwd(), destdir))
			cmd = 'cp %s %s/%s/' % (file, os.getcwd(), destdir)

	if not cmd:
		return True

	dest = os.path.join(os.getcwd(), os.path.basename(file))
	if os.path.exists(dest):
		if os.path.samefile(file, dest):
			return True

	# Change to subdir before executing command
	save_cwd = os.getcwd();
	parm = bb.decodeurl(url)[5]
	if 'subdir' in parm:
		newdir = ("%s/%s" % (os.getcwd(), parm['subdir']))
		bb.mkdirhier(newdir)
		os.chdir(newdir)

	cmd = "PATH=\"%s\" %s" % (bb.data.getVar('PATH', data, 1), cmd)
	bb.note("Unpacking %s to %s/" % (file, os.getcwd()))
	ret = subprocess.call(cmd, preexec_fn=subprocess_setup, shell=True)

	os.chdir(save_cwd)

	return ret == 0

addtask unpack after do_fetch
do_unpack[dirs] = "${WORKDIR}"
python base_do_unpack() {
	import re

	localdata = bb.data.createCopy(d)
	bb.data.update_data(localdata)

	urldata = bb.fetch.init([], localdata, True)

	src_uri = bb.data.getVar('SRC_URI', localdata, True)
	if not src_uri:
		return
	for url in src_uri.split():
		try:
			local = bb.data.expand(bb.fetch.localpath(url, localdata), localdata)
		except bb.MalformedUrl, e:
			raise FuncFailed('Unable to generate local path for malformed uri: %s' % e)
		if local is None:
			continue
		local = os.path.realpath(local)
		lf = bb.utils.lockfile(urldata[url].lockfile)
		ret = oe_unpack_file(local, localdata, url)
		bb.utils.unlockfile(lf)
		if not ret:
			raise bb.build.FuncFailed("oe_unpack_file failed with return value %s" % ret)
}

GIT_CONFIG = "${STAGING_DIR_NATIVE}/usr/etc/gitconfig"

def generate_git_config(e):
        from bb import data

        if data.getVar('GIT_CORE_CONFIG', e.data, True):
                gitconfig_path = bb.data.getVar('GIT_CONFIG', e.data, True)
                proxy_command = "    gitproxy = %s\n" % data.getVar('GIT_PROXY_COMMAND', e.data, True)

                bb.mkdirhier(bb.data.expand("${STAGING_DIR_NATIVE}/usr/etc/", e.data))
                if (os.path.exists(gitconfig_path)):
                        os.remove(gitconfig_path)

                f = open(gitconfig_path, 'w')
                f.write("[core]\n")
                ignore_hosts = data.getVar('GIT_PROXY_IGNORE', e.data, True).split()
                for ignore_host in ignore_hosts:
                        f.write("    gitproxy = none for %s\n" % ignore_host)
                f.write(proxy_command)
                f.close

addhandler base_eventhandler
python base_eventhandler() {
	from bb import note, error, data
	from bb.event import getName

	messages = {}
	messages["Completed"] = "completed"
	messages["Succeeded"] = "completed"
	messages["Started"] = "started"
	messages["Failed"] = "failed"

	name = getName(e)
	msg = ""
	if name.startswith("Pkg"):
		msg += "package %s: " % data.getVar("P", e.data, 1)
		msg += messages.get(name[3:]) or name[3:]
	elif name.startswith("Task"):
		msg += "package %s: task %s: " % (data.getVar("PF", e.data, 1), e.task)
		msg += messages.get(name[4:]) or name[4:]
	elif name.startswith("Build"):
		msg += "build %s: " % e.name
		msg += messages.get(name[5:]) or name[5:]
	elif name == "UnsatisfiedDep":
		msg += "package %s: dependency %s %s" % (e.pkg, e.dep, name[:-3].lower())

	# Only need to output when using 1.8 or lower, the UI code handles it
	# otherwise
	if (int(bb.__version__.split(".")[0]) <= 1 and int(bb.__version__.split(".")[1]) <= 8):
		if msg:
			note(msg)

	if name.startswith("BuildStarted"):
		bb.data.setVar( 'BB_VERSION', bb.__version__, e.data )
		statusvars = ['BB_VERSION', 'METADATA_BRANCH', 'METADATA_REVISION', 'TARGET_ARCH', 'TARGET_OS', 'MACHINE', 'DISTRO', 'DISTRO_VERSION','TARGET_FPU']
		statuslines = ["%-17s = \"%s\"" % (i, bb.data.getVar(i, e.data, 1) or '') for i in statusvars]
		statusmsg = "\nOE Build Configuration:\n%s\n" % '\n'.join(statuslines)
		print statusmsg

		needed_vars = [ "TARGET_ARCH", "TARGET_OS" ]
		pesteruser = []
		for v in needed_vars:
			val = bb.data.getVar(v, e.data, 1)
			if not val or val == 'INVALID':
				pesteruser.append(v)
		if pesteruser:
			bb.fatal('The following variable(s) were not set: %s\nPlease set them directly, or choose a MACHINE or DISTRO that sets them.' % ', '.join(pesteruser))

	#
	# Handle removing stamps for 'rebuild' task
	#
	if name.startswith("StampUpdate"):
		for (fn, task) in e.targets:
			#print "%s %s" % (task, fn)
			if task == "do_rebuild":
				dir = "%s.*" % e.stampPrefix[fn]
				bb.note("Removing stamps: " + dir)
				os.system('rm -f '+ dir)
				os.system('touch ' + e.stampPrefix[fn] + '.needclean')

        if name == "ConfigParsed":
                generate_git_config(e)

	if not data in e.__dict__:
		return

	log = data.getVar("EVENTLOG", e.data, 1)
	if log:
		logfile = file(log, "a")
		logfile.write("%s\n" % msg)
		logfile.close()
}

addtask configure after do_unpack do_patch
do_configure[dirs] = "${S} ${B}"
do_configure[deptask] = "do_populate_sysroot"
base_do_configure() {
	:
}

addtask compile after do_configure
do_compile[dirs] = "${S} ${B}"
base_do_compile() {
	if [ -e Makefile -o -e makefile ]; then
		oe_runmake || die "make failed"
	else
		oenote "nothing to compile"
	fi
}

addtask install after do_compile
do_install[dirs] = "${D} ${S} ${B}"
# Remove and re-create ${D} so that is it guaranteed to be empty
do_install[cleandirs] = "${D}"

base_do_install() {
	:
}

base_do_package() {
	:
}

addtask build after do_populate_sysroot
do_build = ""
do_build[func] = "1"
do_build[noexec] = "1"
do_build () {
	:
}

python () {
    import exceptions, string

    # If PRINC is set, try and increase the PR value by the amount specified
    princ = bb.data.getVar('PRINC', d, True)
    if princ:
        pr = bb.data.getVar('PR', d, True)
        start = -1
        end = -1
        for i in range(len(pr)):
            if pr[i] in string.digits:
                if start == -1:
                    start = i
                else:
                    end = i
        if start == -1 or end == -1:
            bb.error("Unable to analyse format of PR variable: %s" % pr)
        prval = pr[start:end+1]
        prval = int(prval) + int(princ)
        pr = pr[0:start] + str(prval) + pr[end:len(pr)-1]
        bb.data.setVar('PR', pr, d)

    pn = bb.data.getVar('PN', d, 1)
    license = bb.data.getVar('LICENSE', d, True)
    if license == "INVALID":
        bb.fatal('This recipe does not have the LICENSE field set (%s)' % pn)

    commercial_license = bb.data.getVar('COMMERCIAL_LICENSE', d, 1)
    import re
    if commercial_license and re.search(pn, commercial_license):
        bb.debug(1, "Skipping %s because it's commercially licensed" % pn)
        raise bb.parse.SkipPackage("because it requires commercial license to ship product")

    # If we're building a target package we need to use fakeroot (pseudo)
    # in order to capture permissions, owners, groups and special files
    if not bb.data.inherits_class('native', d) and not bb.data.inherits_class('cross', d):
        deps = (bb.data.getVarFlag('do_install', 'depends', d) or "").split()
        deps.append('virtual/fakeroot-native:do_populate_sysroot')
        bb.data.setVarFlag('do_install', 'depends', " ".join(deps),d)
        bb.data.setVarFlag('do_install', 'fakeroot', 1, d)
        deps = (bb.data.getVarFlag('do_package', 'depends', d) or "").split()
        deps.append('virtual/fakeroot-native:do_populate_sysroot')
        bb.data.setVarFlag('do_package', 'depends', " ".join(deps),d)
        bb.data.setVarFlag('do_package', 'fakeroot', 1, d)
        bb.data.setVarFlag('do_package_setscene', 'fakeroot', 1, d)
    source_mirror_fetch = bb.data.getVar('SOURCE_MIRROR_FETCH', d, 0)
    if not source_mirror_fetch:
        need_host = bb.data.getVar('COMPATIBLE_HOST', d, 1)
        if need_host:
            import re
            this_host = bb.data.getVar('HOST_SYS', d, 1)
            if not re.match(need_host, this_host):
                raise bb.parse.SkipPackage("incompatible with host %s" % this_host)

        need_machine = bb.data.getVar('COMPATIBLE_MACHINE', d, 1)
        if need_machine:
            import re
            this_machine = bb.data.getVar('MACHINE', d, 1)
            if this_machine and not re.match(need_machine, this_machine):
                raise bb.parse.SkipPackage("incompatible with machine %s" % this_machine)


        dont_want_license = bb.data.getVar('INCOMPATIBLE_LICENSE', d, 1)
        if dont_want_license and not pn.endswith("-native") and not pn.endswith("-cross") and not pn.endswith("-cross-initial") and not pn.endswith("-cross-intermediate"):
            hosttools_whitelist = (bb.data.getVar('HOSTTOOLS_WHITELIST_%s' % dont_want_license, d, 1) or "").split()
            lgplv2_whitelist = (bb.data.getVar('LGPLv2_WHITELIST_%s' % dont_want_license, d, 1) or "").split()
            dont_want_whitelist = (bb.data.getVar('WHITELIST_%s' % dont_want_license, d, 1) or "").split()
            if pn not in hosttools_whitelist and pn not in lgplv2_whitelist and pn not in dont_want_whitelist:

                import re
                this_license = bb.data.getVar('LICENSE', d, 1)
                if this_license and re.search(dont_want_license, this_license):
                    bb.note("SKIPPING %s because it's %s" % (pn, this_license))
                    raise bb.parse.SkipPackage("incompatible with license %s" % this_license)

    # OBSOLETE in bitbake 1.7.4
    srcdate = bb.data.getVar('SRCDATE_%s' % pn, d, 1)
    if srcdate != None:
        bb.data.setVar('SRCDATE', srcdate, d)

    use_nls = bb.data.getVar('USE_NLS_%s' % pn, d, 1)
    if use_nls != None:
        bb.data.setVar('USE_NLS', use_nls, d)

    # Git packages should DEPEND on git-native
    srcuri = bb.data.getVar('SRC_URI', d, 1)
    if "git://" in srcuri:
        depends = bb.data.getVarFlag('do_fetch', 'depends', d) or ""
        depends = depends + " git-native:do_populate_sysroot"
        bb.data.setVarFlag('do_fetch', 'depends', depends, d)

    # Mercurial packages should DEPEND on mercurial-native
    elif "hg://" in srcuri:
        depends = bb.data.getVarFlag('do_fetch', 'depends', d) or ""
        depends = depends + " mercurial-native:do_populate_sysroot"
        bb.data.setVarFlag('do_fetch', 'depends', depends, d)

    # OSC packages should DEPEND on osc-native
    elif "osc://" in srcuri:
        depends = bb.data.getVarFlag('do_fetch', 'depends', d) or ""
        depends = depends + " osc-native:do_populate_sysroot"
        bb.data.setVarFlag('do_fetch', 'depends', depends, d)

    # bb.utils.sha256_file() will fail if hashlib isn't present, so we fallback
    # on shasum-native.  We need to ensure that it is staged before we fetch.
    if bb.data.getVar('PN', d, True) != "shasum-native":
        try:
            import hashlib
        except ImportError:
            depends = bb.data.getVarFlag('do_fetch', 'depends', d) or ""
            depends = depends + " shasum-native:do_populate_sysroot"
            bb.data.setVarFlag('do_fetch', 'depends', depends, d)

    # *.xz should depends on xz-native for unpacking
    # Not endswith because of "*.patch.xz;patch=1". Need bb.decodeurl in future
    if '.xz' in srcuri:
        depends = bb.data.getVarFlag('do_unpack', 'depends', d) or ""
        depends = depends + " xz-native:do_populate_sysroot"
        bb.data.setVarFlag('do_unpack', 'depends', depends, d)

    # 'multimachine' handling
    mach_arch = bb.data.getVar('MACHINE_ARCH', d, 1)
    pkg_arch = bb.data.getVar('PACKAGE_ARCH', d, 1)

    if (pkg_arch == mach_arch):
        # Already machine specific - nothing further to do
        return

    #
    # We always try to scan SRC_URI for urls with machine overrides
    # unless the package sets SRC_URI_OVERRIDES_PACKAGE_ARCH=0
    #
    override = bb.data.getVar('SRC_URI_OVERRIDES_PACKAGE_ARCH', d, 1)
    if override != '0':
        paths = []
        for p in [ "${PF}", "${P}", "${PN}", "files", "" ]:
            path = bb.data.expand(os.path.join("${FILE_DIRNAME}", p, "${MACHINE}"), d)
            if os.path.isdir(path):
                paths.append(path)
        if len(paths) != 0:
            for s in srcuri.split():
                if not s.startswith("file://"):
                    continue
                local = bb.data.expand(bb.fetch.localpath(s, d), d)
                for mp in paths:
                    if local.startswith(mp):
                        #bb.note("overriding PACKAGE_ARCH from %s to %s" % (pkg_arch, mach_arch))
                        bb.data.setVar('PACKAGE_ARCH', "${MACHINE_ARCH}", d)
                        bb.data.setVar('MULTIMACH_ARCH', mach_arch, d)
                        return

    multiarch = pkg_arch

    packages = bb.data.getVar('PACKAGES', d, 1).split()
    for pkg in packages:
        pkgarch = bb.data.getVar("PACKAGE_ARCH_%s" % pkg, d, 1)

        # We could look for != PACKAGE_ARCH here but how to choose
        # if multiple differences are present?
        # Look through PACKAGE_ARCHS for the priority order?
        if pkgarch and pkgarch == mach_arch:
            multiarch = mach_arch
            break

    bb.data.setVar('MULTIMACH_ARCH', multiarch, d)
}

def check_gcc3(data):

	gcc3_versions = 'gcc-3.4.6 gcc-3.4.7 gcc-3.4 gcc34 gcc-3.4.4 gcc-3.3 gcc33 gcc-3.3.6 gcc-3.2 gcc32'

	for gcc3 in gcc3_versions.split():
		if check_app_exists(gcc3, data):
			return gcc3
	
	return False

addtask cleanall after do_clean
python do_cleanall() {
        sstate_clean_cachefiles(d)

	localdata = bb.data.createCopy(d)
	bb.data.update_data(localdata)

	dl_dir = bb.data.getVar('DL_DIR', localdata, True)
	dl_dir = os.path.realpath(dl_dir)

	src_uri = bb.data.getVar('SRC_URI', localdata, True)
	if not src_uri:
		return
	for url in src_uri.split():
		try:
			local = bb.data.expand(bb.fetch.localpath(url, localdata), localdata)
		except bb.MalformedUrl, e:
			raise FuncFailed('Unable to generate local path for malformed uri: %s' % e)
		if local is None:
			continue
		local = os.path.realpath(local)
                if local.startswith(dl_dir):
			bb.note("Removing %s*" % local)
			oe.path.remove(local + "*")
}
do_cleanall[nostamp] = "1"


EXPORT_FUNCTIONS do_setscene do_fetch do_unpack do_configure do_compile do_install do_package
