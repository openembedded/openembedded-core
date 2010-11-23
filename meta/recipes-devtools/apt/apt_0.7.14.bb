DEPENDS = "curl db"
RDEPENDS = "dpkg"
LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=0636e73ff0215e8d672dc4c32c317bb3"
require apt.inc

PR = "r3"

SRC_URI += "file://nodoc.patch \
	    file://includes-fix.patch"

require apt-package.inc

FILES_${PN} += "${bindir}/apt-key"
apt-manpages += "doc/apt-key.8"

