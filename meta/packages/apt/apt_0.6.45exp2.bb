require apt.inc

SRC_URI += "file://autofoo.patch;patch=1 \
            file://nodoc.patch;patch=1"

require apt-package.inc

FILES_${PN} += "${bindir}/apt-key"
apt-manpages += "doc/apt-key.8"
