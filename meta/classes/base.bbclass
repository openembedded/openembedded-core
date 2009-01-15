BB_DEFAULT_TASK ?= "build"

# like os.path.join but doesn't treat absolute RHS specially
def base_path_join(a, *p):
    path = a
    for b in p:
        if path == '' or path.endswith('/'):
            path +=  b
        else:
            path += '/' + b
    return path

# for MD5/SHA handling
def base_chk_load_parser(config_path):
    import ConfigParser, os, bb
    parser = ConfigParser.ConfigParser()
    if not len(parser.read(config_path)) == 1:
        bb.note("Can not open the '%s' ini file" % config_path)
        raise Exception("Can not open the '%s'" % config_path)

    return parser

def base_chk_file(parser, pn, pv, src_uri, localpath, data):
    import os, bb
    no_checksum = False
    # Try PN-PV-SRC_URI first and then try PN-SRC_URI
    # we rely on the get method to create errors
    pn_pv_src = "%s-%s-%s" % (pn,pv,src_uri)
    pn_src    = "%s-%s" % (pn,src_uri)
    if parser.has_section(pn_pv_src):
        md5    = parser.get(pn_pv_src, "md5")
        sha256 = parser.get(pn_pv_src, "sha256")
    elif parser.has_section(pn_src):
        md5    = parser.get(pn_src, "md5")
        sha256 = parser.get(pn_src, "sha256")
    elif parser.has_section(src_uri):
        md5    = parser.get(src_uri, "md5")
        sha256 = parser.get(src_uri, "sha256")
    else:
        no_checksum = True

    # md5 and sha256 should be valid now
    if not os.path.exists(localpath):
        bb.note("The localpath does not exist '%s'" % localpath)
        raise Exception("The path does not exist '%s'" % localpath)


    # Calculate the MD5 and 256-bit SHA checksums
    md5data = bb.utils.md5_file(localpath)
    shadata = bb.utils.sha256_file(localpath)

    # sha256_file() can return None if we are running on Python 2.4 (hashlib is
    # 2.5 onwards, sha in 2.4 is 160-bit only), so check for this and call the
    # standalone shasum binary if required.
    if shadata is None:
        try:
            shapipe = os.popen('PATH=%s oe_sha256sum %s' % (bb.data.getVar('PATH', data, True), localpath))
            shadata = (shapipe.readline().split() or [ "" ])[0]
            shapipe.close()
        except OSError:
            raise Exception("Executing shasum failed, please build shasum-native")
    
    if no_checksum == True:	# we do not have conf/checksums.ini entry
        try:
            file = open("%s/checksums.ini" % bb.data.getVar("TMPDIR", data, 1), "a")
        except:
            return False

        if not file:
            raise Exception("Creating checksums.ini failed")
        
        file.write("[%s]\nmd5=%s\nsha256=%s\n\n" % (src_uri, md5data, shadata))
        file.close()
        return False

    if not md5 == md5data:
        bb.note("The MD5Sums did not match. Wanted: '%s' and Got: '%s'" % (md5,md5data))
        raise Exception("MD5 Sums do not match. Wanted: '%s' Got: '%s'" % (md5, md5data))

    if not sha256 == shadata:
        bb.note("The SHA256 Sums do not match. Wanted: '%s' Got: '%s'" % (sha256,shadata))
        raise Exception("SHA256 Sums do not match. Wanted: '%s' Got: '%s'" % (sha256, shadata))

    return True


def base_dep_prepend(d):
	import bb
	#
	# Ideally this will check a flag so we will operate properly in
	# the case where host == build == target, for now we don't work in
	# that case though.
	#

	deps = ""
    
	# bb.utils.sha256_file() will return None on Python 2.4 because hashlib
	# isn't present.  In this case we use a shasum-native to checksum, so if
	# hashlib isn't present then add shasum-native to the dependencies.
	try:
		import hashlib
	except ImportError:
		# Adding shasum-native as a dependency of shasum-native would be
		# stupid, so don't do that.
		if bb.data.getVar('PN', d, True) != "shasum-native":
			deps = "shasum-native "
            
	# INHIBIT_DEFAULT_DEPS doesn't apply to the patch command.  Whether or  not
	# we need that built is the responsibility of the patch function / class, not
	# the application.
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

def base_less_or_equal(variable, checkvalue, truevalue, falsevalue, d):
	import bb
	if float(bb.data.getVar(variable,d,1)) <= float(checkvalue):
		return truevalue
	else:
		return falsevalue

def base_version_less_or_equal(variable, checkvalue, truevalue, falsevalue, d):
    import bb
    result = bb.vercmp(bb.data.getVar(variable,d,True), checkvalue)
    if result <= 0:
        return truevalue
    else:
        return falsevalue

def base_contains(variable, checkvalues, truevalue, falsevalue, d):
	import bb
	matches = 0
	if type(checkvalues).__name__ == "str":
		checkvalues = [checkvalues]
	for value in checkvalues:
		if bb.data.getVar(variable,d,1).find(value) != -1:	
			matches = matches + 1
	if matches == len(checkvalues):
		return truevalue		
	return falsevalue

def base_both_contain(variable1, variable2, checkvalue, d):
       import bb
       if bb.data.getVar(variable1,d,1).find(checkvalue) != -1 and bb.data.getVar(variable2,d,1).find(checkvalue) != -1:
               return checkvalue
       else:
               return ""

DEPENDS_prepend="${@base_dep_prepend(d)} "

def base_prune_suffix(var, suffixes, d):
    # See if var ends with any of the suffixes listed and 
    # remove it if found
    for suffix in suffixes:
        if var.endswith(suffix):
            return var.replace(suffix, "")
    return var

def base_set_filespath(path, d):
	import os, bb
	filespath = []
	# The ":" ensures we have an 'empty' override
	overrides = (bb.data.getVar("OVERRIDES", d, 1) or "") + ":"
	for p in path:
		for o in overrides.split(":"):
			filespath.append(os.path.join(p, o))
	return ":".join(filespath)

FILESPATH = "${@base_set_filespath([ "${FILE_DIRNAME}/${PF}", "${FILE_DIRNAME}/${P}", "${FILE_DIRNAME}/${PN}", "${FILE_DIRNAME}/${BP}", "${FILE_DIRNAME}/${BPN}", "${FILE_DIRNAME}/files", "${FILE_DIRNAME}" ], d)}"

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

	# Sanity check that the libname.lai is unique
	number_of_files=`(cd $dir; find . -name "$dotlai") | wc -l`
	if [ $number_of_files -gt 1 ]; then
		oefatal "oe_libinstall: $dotlai is not unique in $dir"
	fi


	dir=$dir`(cd $dir;find . -name "$dotlai") | sed "s/^\.//;s/\/$dotlai\$//;q"`
	olddir=`pwd`
	__runcmd cd $dir

	lafile=$libname.la

	# If such file doesn't exist, try to cut version suffix
	if [ ! -f "$lafile" ]; then
		libname1=`echo "$libname" | sed 's/-[0-9.]*$//'`
		lafile1=$libname.la
		if [ -f "$lafile1" ]; then
			libname=$libname1
			lafile=$lafile1
		fi
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
			__runcmd sed -e 's/^installed=yes$/installed=no/' \
				     -e '/^dependency_libs=/s,${WORKDIR}[[:alnum:]/\._+-]*/\([[:alnum:]\._+-]*\),${STAGING_LIBDIR}/\1,g' \
				     -e "/^dependency_libs=/s,\([[:space:]']\)${libdir},\1${STAGING_LIBDIR},g" \
				     $dotlai >$destpath/$libname.la
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

def package_stagefile(file, d):
    import bb, os

    if bb.data.getVar('PSTAGING_ACTIVE', d, True) == "1":
        destfile = file.replace(bb.data.getVar("TMPDIR", d, 1), bb.data.getVar("PSTAGE_TMPDIR_STAGE", d, 1))
        bb.mkdirhier(os.path.dirname(destfile))
        #print "%s to %s" % (file, destfile)
        bb.copyfile(file, destfile)

package_stagefile_shell() {
	if [ "$PSTAGING_ACTIVE" = "1" ]; then
		srcfile=$1
		destfile=`echo $srcfile | sed s#${TMPDIR}#${PSTAGE_TMPDIR_STAGE}#`
		destdir=`dirname $destfile`
		mkdir -p $destdir
		cp -dp $srcfile $destfile
	fi
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

addtask rebuild after do_${BB_DEFAULT_TASK}
addtask rebuild
do_rebuild[dirs] = "${TOPDIR}"
do_rebuild[nostamp] = "1"
python base_do_rebuild() {
	"""rebuild a package"""
}

#addtask mrproper
#do_mrproper[dirs] = "${TOPDIR}"
#do_mrproper[nostamp] = "1"
#python base_do_mrproper() {
#	"""clear downloaded sources, build and temp directories"""
#	dir = bb.data.expand("${DL_DIR}", d)
#	if dir == '/': bb.build.FuncFailed("wrong DATADIR")
#	bb.debug(2, "removing " + dir)
#	os.system('rm -rf ' + dir)
#	bb.build.exec_func('do_clean', d)
#}

SCENEFUNCS += "base_scenefunction"
											
python base_do_setscene () {
        for f in (bb.data.getVar('SCENEFUNCS', d, 1) or '').split():
                bb.build.exec_func(f, d)
	if not os.path.exists(bb.data.getVar('STAMP', d, 1) + ".do_setscene"):
		bb.build.make_stamp("do_setscene", d)
}
do_setscene[selfstamp] = "1"
addtask setscene before do_fetch

python base_scenefunction () {
	stamp = bb.data.getVar('STAMP', d, 1) + ".needclean"
	if os.path.exists(stamp):
	        bb.build.exec_func("do_clean", d)
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
	except bb.fetch.MD5SumError:
		(type, value, traceback) = sys.exc_info()
		raise bb.build.FuncFailed("MD5  failed: %s" % value)
	except:
		(type, value, traceback) = sys.exc_info()
		raise bb.build.FuncFailed("Unknown fetch Error: %s" % value)


	# Verify the SHA and MD5 sums we have in OE and check what do
	# in
	check_sum = bb.which(bb.data.getVar('BBPATH', d, True), "conf/checksums.ini")
	if not check_sum:
		bb.note("No conf/checksums.ini found, not checking checksums")
		return

	try:
		parser = base_chk_load_parser(check_sum)
	except:
		bb.note("Creating the CheckSum parser failed")
		return

	pv = bb.data.getVar('PV', d, True)
	pn = bb.data.getVar('PN', d, True)

	# Check each URI
	for url in src_uri.split():
		localpath = bb.data.expand(bb.fetch.localpath(url, localdata), localdata)
		(type,host,path,_,_,_) = bb.decodeurl(url)
		uri = "%s://%s%s" % (type,host,path)
		try:
			if type == "http" or type == "https" or type == "ftp" or type == "ftps":
				if not base_chk_file(parser, pn, pv,uri, localpath, d):
					bb.note("%s-%s: %s has no entry in conf/checksums.ini, not checking URI" % (pn,pv,uri))
		except Exception:
			raise bb.build.FuncFailed("Checksum of '%s' failed" % uri)
}

addtask fetchall after do_fetch
do_fetchall[recrdeptask] = "do_fetch"
base_do_fetchall() {
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
base_do_checkuriall() {
	:
}

addtask buildall after do_build
do_buildall[recrdeptask] = "do_build"
base_do_buildall() {
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
	elif file.endswith('.tbz') or file.endswith('.tbz2') or file.endswith('.tar.bz2'):
		cmd = 'bzip2 -dc %s | tar x --no-same-owner -f -' % file
	elif file.endswith('.gz') or file.endswith('.Z') or file.endswith('.z'):
		cmd = 'gzip -dc %s > %s' % (file, efile)
	elif file.endswith('.bz2'):
		cmd = 'bzip2 -dc %s > %s' % (file, efile)
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

	# Change to subdir before executing command
	save_cwd = os.getcwd();
	parm = bb.decodeurl(url)[5]
	if 'subdir' in parm:
		newdir = ("%s/%s" % (os.getcwd(), parm['subdir']))
		bb.mkdirhier(newdir)
		os.chdir(newdir)

	cmd = "PATH=\"%s\" %s" % (bb.data.getVar('PATH', data, 1), cmd)
	bb.note("Unpacking %s to %s/" % (file, os.getcwd()))
	ret = os.system(cmd)

	os.chdir(save_cwd)

	return ret == 0

addtask unpack after do_fetch
do_unpack[dirs] = "${WORKDIR}"
python base_do_unpack() {
	import re, os

	localdata = bb.data.createCopy(d)
	bb.data.update_data(localdata)

	src_uri = bb.data.getVar('SRC_URI', localdata, True)
	if not src_uri:
		return

	for url in src_uri.split():
		try:
			local = bb.data.expand(bb.fetch.localpath(url, localdata), localdata)
		except bb.MalformedUrl, e:
			raise FuncFailed('Unable to generate local path for malformed uri: %s' % e)
		local = os.path.realpath(local)
		ret = oe_unpack_file(local, localdata, url)
		if not ret:
			raise bb.build.FuncFailed()
}

def base_get_scmbasepath(d):
	import bb
	path_to_bbfiles = bb.data.getVar( 'BBFILES', d, 1 ).split()
	return path_to_bbfiles[0][:path_to_bbfiles[0].rindex( "packages" )]

def base_get_metadata_monotone_branch(d):
	monotone_branch = "<unknown>"
	try:
		monotone_branch = file( "%s/_MTN/options" % base_get_scmbasepath(d) ).read().strip()
		if monotone_branch.startswith( "database" ):
			monotone_branch_words = monotone_branch.split()
			monotone_branch = monotone_branch_words[ monotone_branch_words.index( "branch" )+1][1:-1]
	except:
		pass
	return monotone_branch

def base_get_metadata_monotone_revision(d):
	monotone_revision = "<unknown>"
	try:
		monotone_revision = file( "%s/_MTN/revision" % base_get_scmbasepath(d) ).read().strip()
		if monotone_revision.startswith( "format_version" ):
			monotone_revision_words = monotone_revision.split()
			monotone_revision = monotone_revision_words[ monotone_revision_words.index( "old_revision" )+1][1:-1]
	except IOError:
		pass
	return monotone_revision

def base_get_metadata_svn_revision(d):
	revision = "<unknown>"
	try:
		revision = file( "%s/.svn/entries" % base_get_scmbasepath(d) ).readlines()[3].strip()
	except IOError:
		pass
	return revision

def base_get_metadata_git_branch(d):
	import os
	branch = os.popen('cd %s; git branch | grep "^* " | tr -d "* "' % base_get_scmbasepath(d)).read()

	if len(branch) != 0:
		return branch
	return "<unknown>"

def base_get_metadata_git_revision(d):
	import os
	rev = os.popen("cd %s; git log -n 1 --pretty=oneline --" % base_get_scmbasepath(d)).read().split(" ")[0]
	if len(rev) != 0:
		return rev
	return "<unknown>"

def base_detect_revision(d):
	scms = [base_get_metadata_git_revision, \
			base_get_metadata_svn_revision]

	for scm in scms:
		rev = scm(d)
		if rev <> "<unknown>":
			return rev

	return "<unknown>"	

def base_detect_branch(d):
	scms = [base_get_metadata_git_branch]

	for scm in scms:
		rev = scm(d)
		if rev <> "<unknown>":
			return rev.strip()

	return "<unknown>"	
	
	

METADATA_BRANCH ?= "${@base_detect_branch(d)}"
METADATA_REVISION ?= "${@base_detect_revision(d)}"

GIT_CONFIG = "${STAGING_DIR_NATIVE}/usr/etc/gitconfig"

def generate_git_config(e):
        import bb
        import os
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
do_configure[deptask] = "do_populate_staging"
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

base_do_stage () {
	:
}

do_populate_staging[dirs] = "${STAGING_DIR_TARGET}/${layout_bindir} ${STAGING_DIR_TARGET}/${layout_libdir} \
			     ${STAGING_DIR_TARGET}/${layout_includedir} \
			     ${STAGING_BINDIR_NATIVE} ${STAGING_LIBDIR_NATIVE} \
			     ${STAGING_INCDIR_NATIVE} \
			     ${STAGING_DATADIR} \
			     ${S} ${B}"

# Could be compile but populate_staging and do_install shouldn't run at the same time
addtask populate_staging after do_install

python do_populate_staging () {
    bb.build.exec_func('do_stage', d)
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
	return os.access(get_subpkgedata_fn(pkg, d) + '.packaged', os.R_OK)

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

def get_subpkgedata_fn(pkg, d):
	import bb, os
	archs = bb.data.expand("${PACKAGE_ARCHS}", d).split(" ")
	archs.reverse()
	pkgdata = bb.data.expand('${TMPDIR}/pkgdata/', d)
	targetdir = bb.data.expand('${TARGET_VENDOR}-${TARGET_OS}/runtime/', d)
	for arch in archs:
		fn = pkgdata + arch + targetdir + pkg
		if os.path.exists(fn):
			return fn
	return bb.data.expand('${PKGDATA_DIR}/runtime/%s' % pkg, d)

def has_subpkgdata(pkg, d):
	import bb, os
	return os.access(get_subpkgedata_fn(pkg, d), os.R_OK)

def read_subpkgdata(pkg, d):
	import bb
	return read_pkgdatafile(get_subpkgedata_fn(pkg, d))

def has_pkgdata(pn, d):
	import bb, os
	fn = bb.data.expand('${PKGDATA_DIR}/%s' % pn, d)
	return os.access(fn, os.R_OK)

def read_pkgdata(pn, d):
	import bb
	fn = bb.data.expand('${PKGDATA_DIR}/%s' % pn, d)
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


#
# Collapse FOO_pkg variables into FOO
#
def read_subpkgdata_dict(pkg, d):
	import bb
	ret = {}
	subd = read_pkgdatafile(get_subpkgedata_fn(pkg, d))
	for var in subd:
		newvar = var.replace("_" + pkg, "")
		ret[newvar] = subd[var]
	return ret

# Make sure MACHINE isn't exported
# (breaks binutils at least)
MACHINE[unexport] = "1"

# Make sure TARGET_ARCH isn't exported
# (breaks Makefiles using implicit rules, e.g. quilt, as GNU make has this 
# in them, undocumented)
TARGET_ARCH[unexport] = "1"

# Make sure DISTRO isn't exported
# (breaks sysvinit at least)
DISTRO[unexport] = "1"


def base_after_parse(d):
    import bb, os, exceptions

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

    pn = bb.data.getVar('PN', d, 1)

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
        depends = depends + " git-native:do_populate_staging"
        bb.data.setVarFlag('do_fetch', 'depends', depends, d)

    # OSC packages should DEPEND on osc-native
    srcuri = bb.data.getVar('SRC_URI', d, 1)
    if "osc://" in srcuri:
        depends = bb.data.getVarFlag('do_fetch', 'depends', d) or ""
        depends = depends + " osc-native:do_populate_staging"
        bb.data.setVarFlag('do_fetch', 'depends', depends, d)

    # bb.utils.sha256_file() will fail if hashlib isn't present, so we fallback
    # on shasum-native.  We need to ensure that it is staged before we fetch.
    if bb.data.getVar('PN', d, True) != "shasum-native":
        try:
            import hashlib
        except ImportError:
            depends = bb.data.getVarFlag('do_fetch', 'depends', d) or ""
            depends = depends + " shasum-native:do_populate_staging"
            bb.data.setVarFlag('do_fetch', 'depends', depends, d)

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

python () {
    base_after_parse(d)
}

def check_app_exists(app, d):
	from bb import which, data

	app = data.expand(app, d)
	path = data.getVar('PATH', d, 1)
	return len(which(path, app)) != 0

def check_gcc3(data):

	gcc3_versions = 'gcc-3.4.6 gcc-3.4.7 gcc-3.4 gcc34 gcc-3.4.4 gcc-3.3 gcc33 gcc-3.3.6 gcc-3.2 gcc32'

	for gcc3 in gcc3_versions.split():
		if check_app_exists(gcc3, data):
			return gcc3
	
	return False

# Patch handling
inherit patch

# Configuration data from site files
# Move to autotools.bbclass?
inherit siteinfo

EXPORT_FUNCTIONS do_setscene do_clean do_fetch do_unpack do_configure do_compile do_install do_package do_populate_pkgs do_stage do_rebuild do_fetchall

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
${KERNELORG_MIRROR}	http://www.kernel.org/pub
${KERNELORG_MIRROR}	ftp://ftp.us.kernel.org/pub
${KERNELORG_MIRROR}	ftp://ftp.uk.kernel.org/pub
${KERNELORG_MIRROR}	ftp://ftp.hk.kernel.org/pub
${KERNELORG_MIRROR}	ftp://ftp.au.kernel.org/pub
${KERNELORG_MIRROR}	ftp://ftp.jp.kernel.org/pub
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
ftp://lsof.itap.purdue.edu/pub/tools/unix/lsof/  ftp://ftp.cerias.purdue.edu/pub/tools/unix/sysutils/lsof/
ftp://lsof.itap.purdue.edu/pub/tools/unix/lsof/  ftp://ftp.tau.ac.il/pub/unix/admin/
ftp://lsof.itap.purdue.edu/pub/tools/unix/lsof/  ftp://ftp.cert.dfn.de/pub/tools/admin/lsof/
ftp://lsof.itap.purdue.edu/pub/tools/unix/lsof/  ftp://ftp.fu-berlin.de/pub/unix/tools/lsof/
ftp://lsof.itap.purdue.edu/pub/tools/unix/lsof/  ftp://ftp.kaizo.org/pub/lsof/
ftp://lsof.itap.purdue.edu/pub/tools/unix/lsof/  ftp://ftp.tu-darmstadt.de/pub/sysadmin/lsof/
ftp://lsof.itap.purdue.edu/pub/tools/unix/lsof/  ftp://ftp.tux.org/pub/sites/vic.cc.purdue.edu/tools/unix/lsof/
ftp://lsof.itap.purdue.edu/pub/tools/unix/lsof/  ftp://gd.tuwien.ac.at/utils/admin-tools/lsof/
ftp://lsof.itap.purdue.edu/pub/tools/unix/lsof/  ftp://sunsite.ualberta.ca/pub/Mirror/lsof/
ftp://lsof.itap.purdue.edu/pub/tools/unix/lsof/  ftp://the.wiretapped.net/pub/security/host-security/lsof/
http://www.apache.org/dist  http://archive.apache.org/dist

}

