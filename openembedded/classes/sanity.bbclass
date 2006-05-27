#
# Sanity check the users setup for common misconfigurations
#

def raise_sanity_error(msg):
	import bb
	bb.fatal(""" Openembedded's config sanity checker detected a potential misconfiguration.
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
	import os

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
	
	# Check the MACHINE is valid
	if not check_conf_exists("conf/machine/${MACHINE}.conf", e.data):
		raise_sanity_error('Please set a valid MACHINE in your local.conf')
	
	# Check the distro is valid
	if not check_conf_exists("conf/distro/${DISTRO}.conf", e.data):
		raise_sanity_error('Please set a valid DISTRO in your local.conf')

	if not check_app_exists("${MAKE}", e.data):
		raise_sanity_error('GNU make missing. Please install GNU make')

	if not check_app_exists('${BUILD_PREFIX}gcc', e.data):
		raise_sanity_error('C Host-Compiler is missing, please install one' )

	if not check_app_exists('${BUILD_PREFIX}g++', e.data):
		raise_sanity_error('C++ Host-Compiler is missing, please install one' )

	if not check_app_exists('patch', e.data):
		raise_sanity_error('Please install the patch utility, preferable GNU patch.')

	if not check_app_exists('diffstat', e.data):
		raise_sanity_error('Please install the diffstat utility')

	if not check_app_exists('texi2html', e.data):
		raise_sanity_error('Please install the texi2html binary')

	if not check_app_exists('cvs', e.data):
		raise_sanity_error('Please install the cvs utility')

	if not check_app_exists('svn', e.data):
		raise_sanity_error('Please install the svn utility')

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
