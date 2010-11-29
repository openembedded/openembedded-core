SUMMARY = "Enable an installed system init script"
DESCRIPTION = "Enable an installed system init script by reading the comment header of the script"
# There is no known home page for insserv
SECTION = "base"
PRIORITY = "optional"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

PR = "r0"

SRC_URI = "ftp://ftp.suse.com/pub/projects/init/${PN}-${PV}.tar.bz2 \
           file://makefile.patch \
           file://disable_runtests.patch \
           file://insserv.conf"

SRC_URI[md5sum] = "4a97d900855148842b1aa8f33b988b47"
SRC_URI[sha256sum] = "89a0a093b1cf3d802ad40568e64b496b493f51ff9825905c8bd12738b374ca47"

do_install () {
	oe_runmake 'DESTDIR=${D}' install
	install -m0644 ${WORKDIR}/insserv.conf ${D}${sysconfdir}/insserv.conf
}

BBCLASSEXTEND = "native"
