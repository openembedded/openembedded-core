require apt.inc
PR = "r2"

SRC_URI += "file://nodoc.patch;patch=1"

require apt-package.inc

FILES_${PN} += "${bindir}/apt-key"
apt-manpages += "doc/apt-key.8"
