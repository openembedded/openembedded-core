SUMMARY = "A system tool for maintaining the /etc/rc*.d hierarchy"

DESCRIPTION = "Chkconfig is a basic system utility.  It updates and queries runlevel \
information for system services.  Chkconfig manipulates the numerous \
symbolic links in /etc/rc.d, to relieve system administrators of some \
of the drudgery of manually editing the symbolic links."

HOMEPAGE = "http://fedorahosted.org/releases/c/h/chkconfig"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5574c6965ae5f583e55880e397fbb018"

DEPENDS = "libnewt gettext popt"

PR = "r0"

SRC_URI = "http://fedorahosted.org/releases/c/h/chkconfig/${P}.tar.bz2"
SRC_URI[md5sum] = "9b5e91d25f4f3b21f4c0bdf55a721431"
SRC_URI[sha256sum] = "775100a9d6f70fbd8f33ed24b560b6842161fc496c21b1fc6d2aed646f9d855f"

inherit autotools

do_install_append() {
#    mkdir -p ${D}/etc/rc.d/init.d
#   mkdir -p  ${D}/etc/rc.d/rc{0,1,2,3,4,5,6}.d
    mkdir -p ${D}/etc/chkconfig.d
}
