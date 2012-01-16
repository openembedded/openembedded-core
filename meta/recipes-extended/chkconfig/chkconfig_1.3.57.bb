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

SRC_URI = "http://fedorahosted.org/releases/c/h/chkconfig/${BPN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "35a2d0630f1cb5c251e749eefde70afd"
SRC_URI[sha256sum] = "6a95472873984a0a8e99dca23ce9efe0ffe9db06d9990ce325575f8506babc1c"

inherit autotools gettext

do_install_append() {
    mkdir -p ${D}/etc/chkconfig.d
    rm -f ${D}/usr/sbin/update-alternatives
}
