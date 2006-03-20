#
# Sanity check the users setup for common misconfigurations
#

BB_MIN_VERSION = "1.3.3"

def raise_sanity_error(msg):
	import bb
	bb.fatal("Openembedded's config sanity checker detected a potential misconfiguration.\nEither fix cause of this error or at your own risk disable the checker (see sanity.conf).\n%s" % msg)

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

addhandler check_sanity_eventhandler
python check_sanity_eventhandler() {
	from bb import note, error, data, __version__
	from bb.event import Handled, NotHandled, getName
	from distutils.version import LooseVersion
	import os

	sanity_checked = bb.data.getVar('SANITY_CHECKED', e.data)
	if sanity_checked == "1":
		return

	# Check the bitbake version meets minimum requirements
	minversion = bb.data.getVar('BB_MIN_VERSION', e.data , True)
	if not minversion:
		# Hack: BB_MIN_VERSION hasn't been parsed yet so return 
		# and wait for the next call
		return

	if (LooseVersion(bb.__version__) < LooseVersion(minversion)):
		raise_sanity_error('Bitbake version %s is required and version %s was found' % (minversion, bb.__version__))

	# Check TARGET_ARCH is set
	if bb.data.getVar('TARGET_ARCH', e.data, True) == 'INVALID':
		raise_sanity_error('Please set TARGET_ARCH directly, or choose a MACHINE or DISTRO that does so.')
	
	# Check TARGET_OS is set
	if bb.data.getVar('TARGET_OS', e.data, True) == 'INVALID':
		raise_sanity_error('Please set TARGET_OS directly, or choose a MACHINE or DISTRO that does so.')

	# Check user doesn't have ASSUME_PROVIDED = instead of += in local.conf
	if "diffstat-native" not in bb.data.getVar('ASSUME_PROVIDED', e.data, True).split():
		raise_sanity_error('Please use ASSUME_PROVIDED +=, not ASSUME_PROVIDED = in your local.conf')
	
	# Check the MACHINE is valid
	if not check_conf_exists("conf/machine/${MACHINE}.conf", e.data):
		raise_sanity_error('Please set a valid MACHINE in your local.conf')
	
	# Check the distro is valid
	if not check_conf_exists("conf/distro/${DISTRO}.conf", e.data):
		raise_sanity_error('Please set a valid DISTRO in your local.conf')
	
	bb.data.setVar('SANITY_CHECKED', "1", e.data)
	return
}

