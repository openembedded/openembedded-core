DEPENDS = "curl db"
RDEPENDS = "dpkg"

require apt.inc

PR = "r3"

SRC_URI += "file://nodoc.patch \
	    file://includes-fix.patch"

require apt-package.inc

FILES_${PN} += "${bindir}/apt-key"
apt-manpages += "doc/apt-key.8"

