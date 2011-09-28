SUMMARY = "Utilities for Advanced Power Management"
DESCRIPTION = "The Advanced Power Management (APM) support provides \
access to battery status information and a set of tools for managing \
notebook power consumption."
HOMEPAGE = "http://apenwarr.ca/apmd/"
SECTION = "base"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://apm.h;firstline=6;endline=18;md5=c9a1f79036ab14aa157e15ed75ffd769"
DEPENDS = "libtool-cross"
PR = "r1"

SRC_URI = "${DEBIAN_MIRROR}/main/a/apmd/apmd_3.2.2.orig.tar.gz;name=tarball \
           ${DEBIAN_MIRROR}/main/a/apmd/apmd_${PV}.diff.gz;name=patch \
           file://libtool.patch \
           file://unlinux.patch \
           file://init \
           file://default \
           file://apmd_proxy \
           file://apmd_proxy.conf"

SRC_URI[tarball.md5sum] = "b1e6309e8331e0f4e6efd311c2d97fa8"
SRC_URI[tarball.sha256sum] = "7f7d9f60b7766b852881d40b8ff91d8e39fccb0d1d913102a5c75a2dbb52332d"

SRC_URI[patch.md5sum] = "57e1b689264ea80f78353519eece0c92"
SRC_URI[patch.sha256sum] = "7905ff96be93d725544d0040e425c42f9c05580db3c272f11cff75b9aa89d430"

S = "${WORKDIR}/apmd-3.2.2.orig"

inherit update-rc.d

INITSCRIPT_NAME = "apmd"
INITSCRIPT_PARAMS = "defaults"

do_compile() {
	# apmd doesn't use whole autotools. Just libtool for installation
	oe_runmake "LIBTOOL=${STAGING_BINDIR_CROSS}/${HOST_SYS}-libtool" apm apmd
}

do_install() {
	install -d ${D}${sysconfdir}
	install -d ${D}${sysconfdir}/apm
	install -d ${D}${sysconfdir}/apm/event.d
	install -d ${D}${sysconfdir}/apm/other.d
	install -d ${D}${sysconfdir}/apm/suspend.d
	install -d ${D}${sysconfdir}/apm/resume.d
	install -d ${D}${sysconfdir}/apm/scripts.d
	install -d ${D}${sysconfdir}/default
	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${sbindir}
	install -d ${D}${bindir}
	install -d ${D}${libdir}
	install -d ${D}${datadir}/apmd
	install -d ${D}${includedir}

	install -m 4755 ${S}/.libs/apm ${D}${bindir}/apm
	install -m 0755 ${S}/.libs/apmd ${D}${sbindir}/apmd
	install -m 0755 ${WORKDIR}/apmd_proxy ${D}${sysconfdir}/apm/
	install -m 0644 ${WORKDIR}/apmd_proxy.conf ${D}${datadir}/apmd/
	install -m 0644 ${WORKDIR}/default ${D}${sysconfdir}/default/apmd
	oe_libinstall -so libapm ${D}${libdir}
	install -m 0644 apm.h ${D}${includedir}

	cat ${WORKDIR}/init | sed -e 's,/usr/sbin,${sbindir},g; s,/etc,${sysconfdir},g;' > ${D}${sysconfdir}/init.d/apmd
	chmod 755 ${D}${sysconfdir}/init.d/apmd
}

PACKAGES =+ "libapm libapm-dev apm"

FILES_libapm = "${libdir}/libapm.so.*"
FILES_libapm-dev = "${libdir}/libapm.* ${includedir}"
FILES_apm = "${bindir}/apm*"
