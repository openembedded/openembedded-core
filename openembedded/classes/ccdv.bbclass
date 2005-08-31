python () {
	if bb.data.getVar('PN', d, 1) in ['ccdv-native']:
		if not bb.data.getVar('INHIBIT_DEFAULT_DEPS', d, 1):
			bb.data.setVar("DEPENDS", '%s %s' % ("ccdv-native", bb.data.getVar("DEPENDS", d, 1) or ""), d)
			bb.data.setVar("CC", '%s %s' % ("ccdv", bb.data.getVar("CC", d, 1) or ""), d)
			bb.data.setVar("BUILD_CC", '%s %s' % ("ccdv", bb.data.getVar("BUILD_CC", d, 1) or ""), d)
			bb.data.setVar("CCLD", '%s %s' % ("ccdv", bb.data.getVar("CCLD", d, 1) or ""), d)
}

def quiet_libtool(bb,d):
	deps = (bb.data.getVar('DEPENDS', d, 1) or "").split()
	if 'libtool-cross' in deps:
		return "'LIBTOOL=${STAGING_BINDIR}/${HOST_SYS}-libtool --silent'"
	elif 'libtool-native' in deps:
		return "'LIBTOOL=${B}/${HOST_SYS}-libtool --silent'"
	else:
		return ""

CCDV = "ccdv"
EXTRA_OEMAKE_append = " ${@quiet_libtool(bb,d)}"
MAKE += "-s"
