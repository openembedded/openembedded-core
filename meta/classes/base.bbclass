BB_DEFAULT_TASK = "build"

def base_dep_prepend(d):
	import bb;
	#
	# Ideally this will check a flag so we will operate properly in
	# the case where host == build == target, for now we don't work in
	# that case though.
	#
	deps = ""

	# INHIBIT_DEFAULT_DEPS doesn't apply to the patch command.  Whether or  not
	# we need that built is the responsibility of the patch function / class, not
	# the application.
	patchdeps = bb.data.getVar("PATCHTOOL", d, 1)
        if patchdeps:
		patchdeps = "%s-native" % patchdeps
		if not patchdeps in bb.data.getVar("PROVIDES", d, 1):
			deps = patchdeps

	if not bb.data.getVar('INHIBIT_DEFAULT_DEPS', d):
		if (bb.data.getVar('HOST_SYS', d, 1) !=
	     	    bb.data.getVar('BUILD_SYS', d, 1)):
			deps += " virtual/${TARGET_PREFIX}gcc virtual/libc "
	return deps

def base_read_file(filename):
	import bb
	try:
		f = file( filename, "r" )
	except IOError, reason:
		return "" # WARNING: can't raise an error now because of the new RDEPENDS handling. This is a bit ugly. :M:
	else:
		return f.read().strip()
	return None

def base_conditional(variable, checkvalue, truevalue, falsevalue, d):
	import bb
	if bb.data.getVar(variable,d,1) == checkvalue:
		return truevalue
	else:
		return falsevalue

def base_contains(variable, checkvalue, truevalue, falsevalue, d):
       import bb
       if bb.data.getVar(variable,d,1).find(checkvalue) != -1:
               return truevalue
       else:
               return falsevalue

def base_both_contain(variable1, variable2, checkvalue, d):
       import bb
       if bb.data.getVar(variable1,d,1).find(checkvalue) != -1 and bb.data.getVar(variable2,d,1).find(checkvalue) != -1:
               return checkvalue
       else:
               return ""

DEPENDS_prepend="${@base_dep_prepend(d)} "

def base_set_filespath(path, d):
	import os, bb
	filespath = []
	for p in path:
		overrides = bb.data.getVar("OVERRIDES", d, 1) or ""
		overrides = overrides + ":"
		for o in overrides.split(":"):
			filespath.append(os.path.join(p, o))
	return ":".join(filespath)

FILESPATH = "${@base_set_filespath([ "${FILE_DIRNAME}/${PF}", "${FILE_DIRNAME}/${P}", "${FILE_DIRNAME}/${PN}", "${FILE_DIRNAME}/files", "${FILE_DIRNAME}" ], d)}"

def oe_filter(f, str, d):
	from re import match
	return " ".join(filter(lambda x: match(f, x, 0), str.split()))

def oe_filter_out(f, str, d):
	from re import match
	return " ".join(filter(lambda x: not match(f, x, 0), str.split()))

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

oe_soinstall() {
	# Purpose: Install shared library file and
	#          create the necessary links
	# Example:
	#
	# oe_
	#
	#oenote installing shared library $1 to $2
	#
	libname=`basename $1`
	install -m 755 $1 $2/$libname
	sonamelink=`${HOST_PREFIX}readelf -d $1 |grep 'Library soname:' |sed -e 's/.*\[\(.*\)\].*/\1/'`
	solink=`echo $libname | sed -e 's/\.so\..*/.so/'`
	ln -sf $libname $2/$sonamelink
	ln -sf $libname $2/$solink
}

oe_libinstall() {
	# Purpose: Install a library, in all its forms
	# Example
	#
	# oe_libinstall libltdl ${STAGING_LIBDIR}/
	# oe_libinstall -C src/libblah libblah ${D}/${libdir}/
	dir=""
	libtool=""
	silent=""
	require_static=""
	require_shared=""
	staging_install=""
	while [ "$#" -gt 0 ]; do
		case "$1" in
		-C)
			shift
			dir="$1"
			;;
		-s)
			silent=1
			;;
		-a)
			require_static=1
			;;
		-so)
			require_shared=1
			;;
		-*)
			oefatal "oe_libinstall: unknown option: $1"
			;;
		*)
			break;
			;;
		esac
		shift
	done

	libname="$1"
	shift
	destpath="$1"
	if [ -z "$destpath" ]; then
		oefatal "oe_libinstall: no destination path specified"
	fi
	if echo "$destpath/" | egrep '^${STAGING_LIBDIR}/' >/dev/null
	then
		staging_install=1
	fi

	__runcmd () {
		if [ -z "$silent" ]; then
			echo >&2 "oe_libinstall: $*"
		fi
		$*
	}

	if [ -z "$dir" ]; then
		dir=`pwd`
	fi
	dotlai=$libname.lai
	dir=$dir`(cd $dir;find . -name "$dotlai") | sed "s/^\.//;s/\/$dotlai\$//;q"`
	olddir=`pwd`
	__runcmd cd $dir

	lafile=$libname.la

	# If such file doesn't exist, try to cut version suffix
	if [ ! -f "$lafile" ]; then
		libname=`echo "$libname" | sed 's/-[0-9.]*$//'`
		lafile=$libname.la
	fi

	if [ -f "$lafile" ]; then
		# libtool archive
		eval `cat $lafile|grep "^library_names="`
		libtool=1
	else
		library_names="$libname.so* $libname.dll.a"
	fi

	__runcmd install -d $destpath/
	dota=$libname.a
	if [ -f "$dota" -o -n "$require_static" ]; then
		__runcmd install -m 0644 $dota $destpath/
	fi
	if [ -f "$dotlai" -a -n "$libtool" ]; then
		if test -n "$staging_install"
		then
			# stop libtool using the final directory name for libraries
			# in staging:
			__runcmd rm -f $destpath/$libname.la
			__runcmd sed -e 's/^installed=yes$/installed=no/' -e '/^dependency_libs=/s,${WORKDIR}[[:alnum:]/\._+-]*/\([[:alnum:]\._+-]*\),${STAGING_LIBDIR}/\1,g' $dotlai >$destpath/$libname.la
		else
			__runcmd install -m 0644 $dotlai $destpath/$libname.la
		fi
	fi

	for name in $library_names; do
		files=`eval echo $name`
		for f in $files; do
			if [ ! -e "$f" ]; then
				if [ -n "$libtool" ]; then
					oefatal "oe_libinstall: $dir/$f not found."
				fi
			elif [ -L "$f" ]; then
				__runcmd cp -P "$f" $destpath/
			elif [ ! -L "$f" ]; then
				libfile="$f"
				__runcmd install -m 0755 $libfile $destpath/
			fi
		done
	done

	if [ -z "$libfile" ]; then
		if  [ -n "$require_shared" ]; then
			oefatal "oe_libinstall: unable to locate shared library"
		fi
	elif [ -z "$libtool" ]; then
		# special case hack for non-libtool .so.#.#.# links
		baselibfile=`basename "$libfile"`
		if (echo $baselibfile | grep -qE '^lib.*\.so\.[0-9.]*$'); then
			sonamelink=`${HOST_PREFIX}readelf -d $libfile |grep 'Library soname:' |sed -e 's/.*\[\(.*\)\].*/\1/'`
			solink=`echo $baselibfile | sed -e 's/\.so\..*/.so/'`
			if [ -n "$sonamelink" -a x"$baselibfile" != x"$sonamelink" ]; then
				__runcmd ln -sf $baselibfile $destpath/$sonamelink
			fi
			__runcmd ln -sf $baselibfile $destpath/$solink
		fi
	fi

	__runcmd cd "$olddir"
}

oe_machinstall() {
	# Purpose: Install machine dependent files, if available
	#          If not available, check if there is a default
	#          If no default, just touch the destination
	# Example:
	#                $1  $2   $3         $4
	# oe_machinstall -m 0644 fstab ${D}/etc/fstab
	#
	# TODO: Check argument number?
	#
	filename=`basename $3`
	dirname=`dirname $3`

	for o in `echo ${OVERRIDES} | tr ':' ' '`; do
		if [ -e $dirname/$o/$filename ]; then
			oenote $dirname/$o/$filename present, installing to $4
			install $1 $2 $dirname/$o/$filename $4
			return
		fi
	done
#	oenote overrides specific file NOT present, trying default=$3...
	if [ -e $3 ]; then
		oenote $3 present, installing to $4
		install $1 $2 $3 $4
	else
		oenote $3 NOT present, touching empty $4
		touch $4
	fi
}

addtask showdata
do_showdata[nostamp] = "1"
python do_showdata() {
	import sys
	# emit variables and shell functions
	bb.data.emit_env(sys.__stdout__, d, True)
	# emit the metadata which isnt valid shell
	for e in d.keys():
	    if bb.data.getVarFlag(e, 'python', d):
	        sys.__stdout__.write("\npython %s () {\n%s}\n" % (e, bb.data.getVar(e, d, 1)))
}

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
do_clean[bbdepcmd] = ""
python base_do_clean() {
	"""clear the build and temp directories"""
	dir = bb.data.expand("${WORKDIR}", d)
	if dir == '//': raise bb.build.FuncFailed("wrong DATADIR")
	bb.note("removing " + dir)
	os.system('rm -rf ' + dir)

	dir = "%s.*" % bb.data.expand(bb.data.getVar('STAMP', d), d)
	bb.note("removing " + dir)
	os.system('rm -f '+ dir)
}

addtask rebuild
do_rebuild[dirs] = "${TOPDIR}"
do_rebuild[nostamp] = "1"
do_rebuild[bbdepcmd] = ""
python base_do_rebuild() {
	"""rebuild a package"""
	bb.build.exec_task('do_clean', d)
	bb.build.exec_task('do_' + bb.data.getVar('BB_DEFAULT_TASK', d, 1), d)
}

addtask mrproper
do_mrproper[dirs] = "${TOPDIR}"
do_mrproper[nostamp] = "1"
do_mrproper[bbdepcmd] = ""
python base_do_mrproper() {
	"""clear downloaded sources, build and temp directories"""
	dir = bb.data.expand("${DL_DIR}", d)
	if dir == '/': bb.build.FuncFailed("wrong DATADIR")
	bb.debug(2, "removing " + dir)
	os.system('rm -rf ' + dir)
	bb.build.exec_task('do_clean', d)
}

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

	try:
		bb.fetch.go(localdata)
	except bb.fetch.MissingParameterError:
		(type, value, traceback) = sys.exc_info()
		raise bb.build.FuncFailed("Missing parameters: %s" % value)
	except bb.fetch.FetchError:
		(type, value, traceback) = sys.exc_info()
		raise bb.build.FuncFailed("Fetch failed: %s" % value)
}

addtask fetchall after do_fetch
do_fetchall[recrdeptask] = "do_fetch"
base_do_fetchall() {
	:
}

def oe_unpack_file(file, data, url = None):
	import bb, os
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
	elif file.endswith('.tbz') or file.endswith('.tar.bz2'):
		cmd = 'bzip2 -dc %s | tar x --no-same-owner -f -' % file
	elif file.endswith('.gz') or file.endswith('.Z') or file.endswith('.z'):
		cmd = 'gzip -dc %s > %s' % (file, efile)
	elif file.endswith('.bz2'):
		cmd = 'bzip2 -dc %s > %s' % (file, efile)
	elif file.endswith('.zip'):
		cmd = 'unzip -q'
		(type, host, path, user, pswd, parm) = bb.decodeurl(url)
		if 'dos' in parm:
			cmd = '%s -a' % cmd
		cmd = '%s %s' % (cmd, file)
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
			if type == "file":
				destdir = bb.decodeurl(url)[1] or "."
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

	cmd = "PATH=\"%s\" %s" % (bb.data.getVar('PATH', data, 1), cmd)
	bb.note("Unpacking %s to %s/" % (file, os.getcwd()))
	ret = os.system(cmd)
	return ret == 0

addtask unpack after do_fetch
do_unpack[dirs] = "${WORKDIR}"
python base_do_unpack() {
	import re, os

	localdata = bb.data.createCopy(d)
	bb.data.update_data(localdata)

	src_uri = bb.data.getVar('SRC_URI', localdata)
	if not src_uri:
		return
	src_uri = bb.data.expand(src_uri, localdata)
	for url in src_uri.split():
		try:
			local = bb.data.expand(bb.fetch.localpath(url, localdata), localdata)
		except bb.MalformedUrl, e:
			raise FuncFailed('Unable to generate local path for malformed uri: %s' % e)
		# dont need any parameters for extraction, strip them off
		local = re.sub(';.*$', '', local)
		local = os.path.realpath(local)
		ret = oe_unpack_file(local, localdata, url)
		if not ret:
			raise bb.build.FuncFailed()
}


addhandler base_eventhandler
python base_eventhandler() {
	from bb import note, error, data
	from bb.event import Handled, NotHandled, getName
	import os

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
	if msg:
		note(msg)

	if name.startswith("BuildStarted"):
		bb.data.setVar( 'BB_VERSION', bb.__version__, e.data )
		path_to_bbfiles = bb.data.getVar( 'BBFILES', e.data, 1 )
		path_to_packages = path_to_bbfiles[:path_to_bbfiles.rindex( "packages" )]
		monotone_revision = "<unknown>"
		try:
			monotone_revision = file( "%s/_MTN/revision" % path_to_packages ).read().strip()
			if monotone_revision.startswith( "format_version" ):
				monotone_revision_words = monotone_revision.split()
				monotone_revision = monotone_revision_words[ monotone_revision_words.index( "old_revision" )+1][1:-1]
		except IOError:
			pass
		bb.data.setVar( 'OE_REVISION', monotone_revision, e.data )
		statusvars = ['BB_VERSION', 'OE_REVISION', 'TARGET_ARCH', 'TARGET_OS', 'MACHINE', 'DISTRO', 'DISTRO_VERSION','TARGET_FPU']
		statuslines = ["%-14s = \"%s\"" % (i, bb.data.getVar(i, e.data, 1) or '') for i in statusvars]
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

	if not data in e.__dict__:
		return NotHandled

	log = data.getVar("EVENTLOG", e.data, 1)
	if log:
		logfile = file(log, "a")
		logfile.write("%s\n" % msg)
		logfile.close()

	return NotHandled
}

addtask configure after do_unpack do_patch
do_configure[dirs] = "${S} ${B}"
do_configure[bbdepcmd] = "do_populate_staging"
do_configure[deptask] = "do_populate_staging"
base_do_configure() {
	:
}

addtask compile after do_configure
do_compile[dirs] = "${S} ${B}"
do_compile[bbdepcmd] = "do_populate_staging"
base_do_compile() {
	if [ -e Makefile -o -e makefile ]; then
		oe_runmake || die "make failed"
	else
		oenote "nothing to compile"
	fi
}

base_do_stage () {
	:
}

do_populate_staging[dirs] = "${STAGING_DIR}/${TARGET_SYS}/bin ${STAGING_DIR}/${TARGET_SYS}/lib \
			     ${STAGING_DIR}/${TARGET_SYS}/include \
			     ${STAGING_DIR}/${BUILD_SYS}/bin ${STAGING_DIR}/${BUILD_SYS}/lib \
			     ${STAGING_DIR}/${BUILD_SYS}/include \
			     ${STAGING_DATADIR} \
			     ${S} ${B}"

addtask populate_staging after do_package_write

python do_populate_staging () {
	bb.build.exec_func('do_stage', d)
}

addtask install after do_compile
do_install[dirs] = "${D} ${S} ${B}"

base_do_install() {
	:
}

base_do_package() {
	:
}

addtask build after do_populate_staging
do_build = ""
do_build[func] = "1"

# Functions that update metadata based on files outputted
# during the build process.

def explode_deps(s):
	r = []
	l = s.split()
	flag = False
	for i in l:
		if i[0] == '(':
			flag = True
			j = []
		if flag:
			j.append(i)
			if i.endswith(')'):
				flag = False
				r[-1] += ' ' + ' '.join(j)
		else:
			r.append(i)
	return r

def packaged(pkg, d):
	import os, bb
	return os.access(bb.data.expand('${STAGING_DIR}/pkgdata/runtime/%s.packaged' % pkg, d), os.R_OK)

def read_pkgdatafile(fn):
	pkgdata = {}

	def decode(str):
		import codecs
		c = codecs.getdecoder("string_escape")
		return c(str)[0]

	import os
	if os.access(fn, os.R_OK):
		import re
		f = file(fn, 'r')
		lines = f.readlines()
		f.close()
		r = re.compile("([^:]+):\s*(.*)")
		for l in lines:
			m = r.match(l)
			if m:
				pkgdata[m.group(1)] = decode(m.group(2))

	return pkgdata

def has_subpkgdata(pkg, d):
	import bb, os
	fn = bb.data.expand('${STAGING_DIR}/pkgdata/runtime/%s' % pkg, d)
	return os.access(fn, os.R_OK)

def read_subpkgdata(pkg, d):
	import bb, os
	fn = bb.data.expand('${STAGING_DIR}/pkgdata/runtime/%s' % pkg, d)
	return read_pkgdatafile(fn)


def has_pkgdata(pn, d):
	import bb, os
	fn = bb.data.expand('${STAGING_DIR}/pkgdata/%s' % pn, d)
	return os.access(fn, os.R_OK)

def read_pkgdata(pn, d):
	import bb, os
	fn = bb.data.expand('${STAGING_DIR}/pkgdata/%s' % pn, d)
	return read_pkgdatafile(fn)

python read_subpackage_metadata () {
	import bb
	data = read_pkgdata(bb.data.getVar('PN', d, 1), d)

	for key in data.keys():
		bb.data.setVar(key, data[key], d)

	for pkg in bb.data.getVar('PACKAGES', d, 1).split():
		sdata = read_subpkgdata(pkg, d)
		for key in sdata.keys():
			bb.data.setVar(key, sdata[key], d)
}

def base_after_parse_two(d):
    import bb
    import exceptions
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

    pn = bb.data.getVar('PN', d, 1)

    # OBSOLETE in bitbake 1.7.4
    srcdate = bb.data.getVar('SRCDATE_%s' % pn, d, 1)
    if srcdate != None:
        bb.data.setVar('SRCDATE', srcdate, d)

    use_nls = bb.data.getVar('USE_NLS_%s' % pn, d, 1)
    if use_nls != None:
        bb.data.setVar('USE_NLS', use_nls, d)

def base_after_parse(d):
    import bb, os
    mach_arch = bb.data.getVar('MACHINE_ARCH', d, 1)
    old_arch = bb.data.getVar('PACKAGE_ARCH', d, 1)
    if (old_arch == mach_arch):
        # Nothing to do
        return
    if (bb.data.getVar('SRC_URI_OVERRIDES_PACKAGE_ARCH', d, 1) == '0'):
        return
    paths = []
    for p in [ "${FILE_DIRNAME}/${PF}", "${FILE_DIRNAME}/${P}", "${FILE_DIRNAME}/${PN}", "${FILE_DIRNAME}/files", "${FILE_DIRNAME}" ]:
        paths.append(bb.data.expand(os.path.join(p, mach_arch), d))
    for s in bb.data.getVar('SRC_URI', d, 1).split():
        local = bb.data.expand(bb.fetch.localpath(s, d), d)
        for mp in paths:
            if local.startswith(mp):
                #bb.note("overriding PACKAGE_ARCH from %s to %s" % (old_arch, mach_arch))
                bb.data.setVar('PACKAGE_ARCH', mach_arch, d)
                return


python () {
    base_after_parse_two(d)
    base_after_parse(d)
}


# Patch handling
inherit patch

# Configuration data from site files
# Move to autotools.bbclass?
inherit siteinfo

EXPORT_FUNCTIONS do_clean do_mrproper do_fetch do_unpack do_configure do_compile do_install do_package do_populate_pkgs do_stage do_rebuild do_fetchall

MIRRORS[func] = "0"
MIRRORS () {
${DEBIAN_MIRROR}/main	http://snapshot.debian.net/archive/pool
${DEBIAN_MIRROR}	ftp://ftp.de.debian.org/debian/pool
${DEBIAN_MIRROR}	ftp://ftp.au.debian.org/debian/pool
${DEBIAN_MIRROR}	ftp://ftp.cl.debian.org/debian/pool
${DEBIAN_MIRROR}	ftp://ftp.hr.debian.org/debian/pool
${DEBIAN_MIRROR}	ftp://ftp.fi.debian.org/debian/pool
${DEBIAN_MIRROR}	ftp://ftp.hk.debian.org/debian/pool
${DEBIAN_MIRROR}	ftp://ftp.hu.debian.org/debian/pool
${DEBIAN_MIRROR}	ftp://ftp.ie.debian.org/debian/pool
${DEBIAN_MIRROR}	ftp://ftp.it.debian.org/debian/pool
${DEBIAN_MIRROR}	ftp://ftp.jp.debian.org/debian/pool
${DEBIAN_MIRROR}	ftp://ftp.no.debian.org/debian/pool
${DEBIAN_MIRROR}	ftp://ftp.pl.debian.org/debian/pool
${DEBIAN_MIRROR}	ftp://ftp.ro.debian.org/debian/pool
${DEBIAN_MIRROR}	ftp://ftp.si.debian.org/debian/pool
${DEBIAN_MIRROR}	ftp://ftp.es.debian.org/debian/pool
${DEBIAN_MIRROR}	ftp://ftp.se.debian.org/debian/pool
${DEBIAN_MIRROR}	ftp://ftp.tr.debian.org/debian/pool
${GNU_MIRROR}	ftp://mirrors.kernel.org/gnu
${GNU_MIRROR}	ftp://ftp.matrix.com.br/pub/gnu
${GNU_MIRROR}	ftp://ftp.cs.ubc.ca/mirror2/gnu
${GNU_MIRROR}	ftp://sunsite.ust.hk/pub/gnu
${GNU_MIRROR}	ftp://ftp.ayamura.org/pub/gnu
ftp://ftp.kernel.org/pub	http://www.kernel.org/pub
ftp://ftp.kernel.org/pub	ftp://ftp.us.kernel.org/pub
ftp://ftp.kernel.org/pub	ftp://ftp.uk.kernel.org/pub
ftp://ftp.kernel.org/pub	ftp://ftp.hk.kernel.org/pub
ftp://ftp.kernel.org/pub	ftp://ftp.au.kernel.org/pub
ftp://ftp.kernel.org/pub	ftp://ftp.jp.kernel.org/pub
ftp://ftp.gnupg.org/gcrypt/     ftp://ftp.franken.de/pub/crypt/mirror/ftp.gnupg.org/gcrypt/
ftp://ftp.gnupg.org/gcrypt/     ftp://ftp.surfnet.nl/pub/security/gnupg/
ftp://ftp.gnupg.org/gcrypt/     http://gulus.USherbrooke.ca/pub/appl/GnuPG/
ftp://dante.ctan.org/tex-archive ftp://ftp.fu-berlin.de/tex/CTAN
ftp://dante.ctan.org/tex-archive http://sunsite.sut.ac.jp/pub/archives/ctan/
ftp://dante.ctan.org/tex-archive http://ctan.unsw.edu.au/
ftp://ftp.gnutls.org/pub/gnutls ftp://ftp.gnutls.org/pub/gnutls/
ftp://ftp.gnutls.org/pub/gnutls ftp://ftp.gnupg.org/gcrypt/gnutls/
ftp://ftp.gnutls.org/pub/gnutls http://www.mirrors.wiretapped.net/security/network-security/gnutls/
ftp://ftp.gnutls.org/pub/gnutls ftp://ftp.mirrors.wiretapped.net/pub/security/network-security/gnutls/
ftp://ftp.gnutls.org/pub/gnutls http://josefsson.org/gnutls/releases/
http://ftp.info-zip.org/pub/infozip/src/ http://mirror.switch.ch/ftp/mirror/infozip/src/
http://ftp.info-zip.org/pub/infozip/src/ ftp://sunsite.icm.edu.pl/pub/unix/archiving/info-zip/src/
}

