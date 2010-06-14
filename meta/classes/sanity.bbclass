#
# Sanity check the users setup for common misconfigurations
#

def raise_sanity_error(msg):
	bb.fatal(""" Poky's config sanity checker detected a potential misconfiguration.
	Either fix the cause of this error or at your own risk disable the checker (see sanity.conf).
	Following is the list of potential problems / advisories:
	
	%s""" % msg)

def check_conf_exists(fn, data):
	bbpath = []
	fn = bb.data.expand(fn, data)
	vbbpath = bb.data.getVar("BBPATH", data)
	if vbbpath:
		bbpath += vbbpath.split(":")
	for p in bbpath:
		currname = os.path.join(bb.data.expand(p, data), fn)
		if os.access(currname, os.R_OK):
			return True
	return False

def check_sanity(e):
	from bb import note, error, data, __version__

	try:
		from distutils.version import LooseVersion
	except ImportError:
		def LooseVersion(v): print "WARNING: sanity.bbclass can't compare versions without python-distutils"; return 1
	import commands

	# Check the bitbake version meets minimum requirements
	minversion = data.getVar('BB_MIN_VERSION', e.data , True)
	if not minversion:
		# Hack: BB_MIN_VERSION hasn't been parsed yet so return 
		# and wait for the next call
		print "Foo %s" % minversion
		return

	if 0 == os.getuid():
		raise_sanity_error("Do not use Bitbake as root.")

	messages = ""

	# Check the Python version, we now use Python 2.6 features in
	# various classes
	import sys
	if sys.hexversion < 0x020600F0:
		messages = messages + 'Poky requires at least Python 2.6 to run. Please update your Python interpreter.\n'

	if (LooseVersion(__version__) < LooseVersion(minversion)):
		messages = messages + 'Bitbake version %s is required and version %s was found\n' % (minversion, __version__)

	# Check TARGET_ARCH is set
	if data.getVar('TARGET_ARCH', e.data, True) == 'INVALID':
		messages = messages + 'Please set TARGET_ARCH directly, or choose a MACHINE or DISTRO that does so.\n'
	
	# Check TARGET_OS is set
	if data.getVar('TARGET_OS', e.data, True) == 'INVALID':
		messages = messages + 'Please set TARGET_OS directly, or choose a MACHINE or DISTRO that does so.\n'

        # Check we are using a valid lacal.conf
        current_conf  = data.getVar('CONF_VERSION', e.data, True)
        conf_version =  data.getVar('POKY_CONF_VERSION', e.data, True)

        if current_conf != conf_version:
                messages = messages + "Poky has noticed your version of local.conf was generated from an older version of local.conf.sample and there have been updates made to this file. Please compare the two files and merge any changes before continuing.\nMatching the version numbers will remove this message.\n\"meld conf/local.conf conf/local.conf.sample\" is a good way to visualise the changes.\n"

        # Check bblayers.conf is valid
        current_lconf = data.getVar('LCONF_VERSION', e.data, True)
        lconf_version = data.getVar('LAYER_CONF_VERSION', e.data, True)
        if current_lconf != lconf_version:
                messages = messages + "Poky has noticed your version of bblayers.conf was generated from an older version of bblayers.conf.sample and there have been updates made to this file. Please compare the two files and merge any changes before continuing.\nMatching the version numbers will remove this message.\n\"meld conf/bblayers.conf conf/bblayers.conf.sample\" is a good way to visualise the changes.\n"

        # If we have a site.conf, check it's valid
        if check_conf_exists("conf/site.conf", e.data):
                current_sconf = data.getVar('SCONF_VERSION', e.data, True)
                sconf_version = data.getVar('SITE_CONF_VERSION', e.data, True)
                if current_sconf != sconf_version:
                        messages = messages + "Poky has noticed your version of site.conf was generated from an older version of site.conf.sample and there have been updates made to this file. Please compare the two files and merge any changes before continuing.\nMatching the version numbers will remove this message.\n\"meld conf/site.conf conf/site.conf.sample\" is a good way to visualise the changes.\n"

	assume_provided = data.getVar('ASSUME_PROVIDED', e.data , True).split()
	# Check user doesn't have ASSUME_PROVIDED = instead of += in local.conf
	if "diffstat-native" not in assume_provided:
		messages = messages + 'Please use ASSUME_PROVIDED +=, not ASSUME_PROVIDED = in your local.conf\n'
	
	# Check that the MACHINE is valid, if it is set
	if data.getVar('MACHINE', e.data, True):
		if not check_conf_exists("conf/machine/${MACHINE}.conf", e.data):
			messages = messages + 'Please set a valid MACHINE in your local.conf\n'
	
	# Check that the DISTRO is valid
	# need to take into account DISTRO renaming DISTRO
	if not ( check_conf_exists("conf/distro/${DISTRO}.conf", e.data) or check_conf_exists("conf/distro/include/${DISTRO}.inc", e.data) ):
		messages = messages + "DISTRO '%s' not found. Please set a valid DISTRO in your local.conf\n" % data.getVar("DISTRO", e.data, True )

	missing = ""

	if not check_app_exists("${MAKE}", e.data):
		missing = missing + "GNU make,"

	if not check_app_exists('${BUILD_PREFIX}gcc', e.data):
		missing = missing + "C Compiler (%sgcc)," % data.getVar("BUILD_PREFIX", e.data, True)

	if not check_app_exists('${BUILD_PREFIX}g++', e.data):
		missing = missing + "C++ Compiler (%sg++)," % data.getVar("BUILD_PREFIX", e.data, True)

	required_utilities = "patch help2man diffstat texi2html makeinfo cvs svn bzip2 tar gzip gawk hg chrpath"

	# qemu-native needs gcc 3.x
	if "qemu-native" not in assume_provided and "gcc3-native" in assume_provided:
		gcc_version = commands.getoutput("${BUILD_PREFIX}gcc --version | head -n 1 | cut -f 3 -d ' '")

		if not check_gcc3(e.data) and gcc_version[0] != '3':
			messages = messages + "gcc3-native was in ASSUME_PROVIDED but the gcc-3.x binary can't be found in PATH"
			missing = missing + "gcc-3.x (needed for qemu-native),"

	if "qemu-native" in assume_provided:
		if not check_app_exists("qemu-arm", e.data):
			messages = messages + "qemu-native was in ASSUME_PROVIDED but the QEMU binaries (qemu-arm) can't be found in PATH"

	for util in required_utilities.split():
		if not check_app_exists( util, e.data ):
			missing = missing + "%s," % util

	if missing != "":
		missing = missing.rstrip(',')
		messages = messages + "Please install following missing utilities: %s\n" % missing

	if os.path.basename(os.readlink('/bin/sh')) == 'dash':
		messages = messages + "Using dash as /bin/sh causes various subtle build problems, please use bash instead (e.g. 'dpkg-reconfigure dash' on an Ubuntu system.\n"

	omask = os.umask(022)
	if omask & 0755:
		messages = messages + "Please use a umask which allows a+rx and u+rwx\n"
	os.umask(omask)

	oes_bb_conf = data.getVar( 'OES_BITBAKE_CONF', e.data, True )
	if not oes_bb_conf:
		messages = messages + 'You do not include OpenEmbeddeds version of conf/bitbake.conf. This means your environment is misconfigured, in particular check BBPATH.\n'

	if data.getVar('SDKMACHINE', e.data, True) == 'i686':
		messages = messages + '"Please set SDKMACHINE to i586 as its currently set to i686 and this is known to have issues (see local.conf).\n'

	#
	# Check that TMPDIR hasn't changed location since the last time we were run
	#
	tmpdir = data.getVar('TMPDIR', e.data, True)
	checkfile = os.path.join(tmpdir, "saved_tmpdir")
	if os.path.exists(checkfile):
		f = file(checkfile, "r")
		saved_tmpdir = f.read().strip()
		if (saved_tmpdir != tmpdir):
			messages = messages + "Error, TMPDIR has changed location. You need to either move it back to %s or rebuild\n" % saved_tmpdir
	else:
		f = file(checkfile, "w")
		f.write(tmpdir)
	f.close()

	#
	# Check the 'ABI' of TMPDIR
	#
	current_abi = data.getVar('OELAYOUT_ABI', e.data, True)
	abifile = data.getVar('SANITY_ABIFILE', e.data, True)
	if os.path.exists(abifile):
		f = file(abifile, "r")
		abi = f.read().strip()
		if not abi.isdigit():
			f = file(abifile, "w")
			f.write(current_abi)
		elif abi == "2" and current_abi == "3":
			bb.note("Converting staging from layout version 2 to layout version 3")
			os.system(bb.data.expand("mv ${TMPDIR}/staging ${TMPDIR}/sysroots", e.data))
			os.system(bb.data.expand("ln -s sysroots ${TMPDIR}/staging", e.data))
			os.system(bb.data.expand("cd ${TMPDIR}/stamps; for i in */*do_populate_staging; do new=`echo $i | sed -e 's/do_populate_staging/do_populate_sysroot/'`; mv $i $new; done", e.data))
			f = file(abifile, "w")
			f.write(current_abi)
		elif abi == "3" and current_abi == "4":
                        bb.note("Converting staging layout from version 3 to layout version 4")
                        if os.path.exists(bb.data.expand("${STAGING_DIR_NATIVE}${bindir_native}/${MULTIMACH_HOST_SYS}", e.data)):
                            os.system(bb.data.expand("mv ${STAGING_DIR_NATIVE}${bindir_native}/${MULTIMACH_HOST_SYS} ${STAGING_BINDIR_CROSS}", e.data))
                            os.system(bb.data.expand("ln -s ${STAGING_BINDIR_CROSS} ${STAGING_DIR_NATIVE}${bindir_native}/${MULTIMACH_HOST_SYS}", e.data))

                        f = file(abifile, "w")
                        f.write(current_abi)
                elif abi == "5" and current_abi != "5":
                        messages = messages + "Staging layout has changed. The cross directory has been deprecated and cross packages are now built under the native sysroot.\nThis requires a rebuild.\n"
		elif (abi != current_abi):
			# Code to convert from one ABI to another could go here if possible.
			messages = messages + "Error, TMPDIR has changed ABI (%s to %s) and you need to either rebuild, revert or adjust it at your own risk.\n" % (abi, current_abi)
	else:
		f = file(abifile, "w")
		f.write(current_abi)
	f.close()

	oeroot = data.getVar('OEROOT', e.data)
	if oeroot.find ('+') != -1:
		messages = messages + "Error, you have an invalid character (+) in your OEROOT directory path. Please more Poky to a directory which doesn't include a +."
	elif oeroot.find (' ') != -1:
		messages = messages + "Error, you have a space in your OEROOT directory path. Please move Poky to a directory which doesn't include a space."

	if messages != "":
		raise_sanity_error(messages)

addhandler check_sanity_eventhandler
python check_sanity_eventhandler() {
    from bb.event import Handled, NotHandled
    if bb.event.getName(e) == "ConfigParsed":
        check_sanity(e)

    return NotHandled
}
