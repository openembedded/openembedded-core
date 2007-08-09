#
# Sanity check the users setup for common misconfigurations
#

def raise_sanity_error(msg):
	import bb
	bb.fatal(""" Poky's config sanity checker detected a potential misconfiguration.
	Either fix the cause of this error or at your own risk disable the checker (see sanity.conf).
	Following is the list of potential problems / advisories:
	
	%s""" % msg)

def check_conf_exists(fn, data):
	import bb, os

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

def check_app_exists(app, d):
	from bb import which, data

	app = data.expand(app, d)
	path = data.getVar('PATH', d)
	return len(which(path, app)) != 0


def check_sanity(e):
	from bb import note, error, data, __version__
	from bb.event import Handled, NotHandled, getName
	try:
		from distutils.version import LooseVersion
	except ImportError:
		def LooseVersion(v): print "WARNING: sanity.bbclass can't compare versions without python-distutils"; return 1
	import os, commands

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

	if (LooseVersion(__version__) < LooseVersion(minversion)):
		messages = messages + 'Bitbake version %s is required and version %s was found\n' % (minversion, __version__)

	# Check TARGET_ARCH is set
	if data.getVar('TARGET_ARCH', e.data, True) == 'INVALID':
		messages = messages + 'Please set TARGET_ARCH directly, or choose a MACHINE or DISTRO that does so.\n'
	
	# Check TARGET_OS is set
	if data.getVar('TARGET_OS', e.data, True) == 'INVALID':
		messages = messages + 'Please set TARGET_OS directly, or choose a MACHINE or DISTRO that does so.\n'

	assume_provided = data.getVar('ASSUME_PROVIDED', e.data , True).split()
	# Check user doesn't have ASSUME_PROVIDED = instead of += in local.conf
	if "diffstat-native" not in assume_provided:
		messages = messages + 'Please use ASSUME_PROVIDED +=, not ASSUME_PROVIDED = in your local.conf\n'
	
	# Check that the MACHINE is valid
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
		missing = missing + "C Compiler,"

	if not check_app_exists('${BUILD_PREFIX}g++', e.data):
		missing = missing + "C++ Compiler,"

	required_utilities = "patch diffstat help2man texi2html cvs svn bzip2 tar gzip gawk makeinfo"

	for util in required_utilities.split():
		if not check_app_exists( util, e.data ):
			missing = missing + "%s," % util

	# qemu-native needs gcc 3.x
	if "qemu-native" not in assume_provided:
		gcc_version = commands.getoutput("${BUILD_PREFIX}gcc --version | head -n 1 | cut -f 3 -d ' '")

		if not check_app_exists('gcc-3.4', e.data) and not check_app_exists('gcc-3.3', e.data) and gcc_version[0] != '3':
			missing = missing + "gcc-3.x (needed for qemu-native),"

	if missing != "":
		missing = missing.rstrip(',')
		messages = messages + "Please install following missing utilities: %s\n" % missing

	omask = os.umask(022)
	if omask & 0755:
		messages = messages + "Please use a umask which allows a+rx and u+rwx\n"
	os.umask(omask)

	if messages != "":
		raise_sanity_error(messages)

	oes_bb_conf = data.getVar( 'OES_BITBAKE_CONF', e.data, True )
	if not oes_bb_conf:
		raise_sanity_error('You do not include OpenEmbeddeds version of conf/bitbake.conf')

addhandler check_sanity_eventhandler
python check_sanity_eventhandler() {
    from bb import note, error, data, __version__
    from bb.event import getName

    if getName(e) == "BuildStarted":
        check_sanity(e)

    return NotHandled
}
