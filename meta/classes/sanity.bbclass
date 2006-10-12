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

	if (LooseVersion(__version__) < LooseVersion(minversion)):
		raise_sanity_error('Bitbake version %s is required and version %s was found' % (minversion, __version__))

	# Check TARGET_ARCH is set
	if data.getVar('TARGET_ARCH', e.data, True) == 'INVALID':
		raise_sanity_error('Please set TARGET_ARCH directly, or choose a MACHINE or DISTRO that does so.')
	
	# Check TARGET_OS is set
	if data.getVar('TARGET_OS', e.data, True) == 'INVALID':
		raise_sanity_error('Please set TARGET_OS directly, or choose a MACHINE or DISTRO that does so.')

	# Check user doesn't have ASSUME_PROVIDED = instead of += in local.conf
	if "diffstat-native" not in data.getVar('ASSUME_PROVIDED', e.data, True).split():
		raise_sanity_error('Please use ASSUME_PROVIDED +=, not ASSUME_PROVIDED = in your local.conf')
	
	# Check that the MACHINE is valid
	if not check_conf_exists("conf/machine/${MACHINE}.conf", e.data):
		raise_sanity_error('Please set a valid MACHINE in your local.conf')
	
	# Check that the DISTRO is valid
	# need to take into account DISTRO renaming DISTRO
	if not ( check_conf_exists("conf/distro/${DISTRO}.conf", e.data) or check_conf_exists("conf/distro/include/${DISTRO}.inc", e.data) ):
		raise_sanity_error("DISTRO '%s' not found. Please set a valid DISTRO in your local.conf" % data.getVar("DISTRO", e.data, True ))

	missing = ""

	if not check_app_exists("${MAKE}", e.data):
		missing = missing + "GNU make,"

	if not check_app_exists('${BUILD_PREFIX}gcc', e.data):
		missing = missing + "C Compiler,"

	if not check_app_exists('${BUILD_PREFIX}g++', e.data):
		missing = missing + "C++ Compiler,"

	if not check_app_exists('gawk', e.data):
		missing = missing + "GNU awk (gawk),"

	if not check_app_exists('patch', e.data):
		missing = missing + "GNU patch,"

	if not check_app_exists('diffstat', e.data):
		missing = missing + "diffstat,"

	if not check_app_exists('texi2html', e.data):
		missing = missing + "texi2html,"

	if not check_app_exists('cvs', e.data):
		missing = missing + "cvs,"

	if not check_app_exists('svn', e.data):
		missing = missing + "svn,"

	# qemu-native needs gcc 3.x

 	gcc_version = commands.getoutput("${BUILD_PREFIX}gcc --version | head -n 1 | cut -f 3 -d ' '")

	if not check_app_exists('gcc-3.4', e.data) and not check_app_exists('gcc-3.3', e.data) and gcc_version[0] != '3':
		missing = missing + "gcc-3.x (needed for qemu-native),"

        # FIXME: We also need to check for libsdl-dev and zlib-dev 
        # for qemu-native...

	if not missing == "":
		missing = missing.rstrip(',')
		raise_sanity_error("Missing Dependencies: %s" % missing)

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
