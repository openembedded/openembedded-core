SUMMARY = "A system tool for maintaining the /etc/rc*.d hierarchy"

DESCRIPTION = "Chkconfig is a basic system utility.  It updates and queries runlevel \
information for system services.  Chkconfig manipulates the numerous \
symbolic links in /etc/rc.d, to relieve system administrators of some \
of the drudgery of manually editing the symbolic links."

HOMEPAGE = "http://fedorahosted.org/releases/c/h/chkconfig"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5574c6965ae5f583e55880e397fbb018"

DEPENDS = "libnewt popt"

PR = "r0"

SRC_URI = "http://fedorahosted.org/releases/c/h/chkconfig/${P}.tar.bz2"

SRC_URI[md5sum] = "0a7e5b271084abf368919b51fd917750"
SRC_URI[sha256sum] = "bfa83a8f69f5127ab772eee33a48f3c1839efac1a622ab484d2f688264d13929"

inherit autotools gettext

do_install_append() {
    mkdir -p ${D}/etc/chkconfig.d
}
