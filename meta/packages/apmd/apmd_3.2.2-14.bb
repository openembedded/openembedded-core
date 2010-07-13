DESCRIPTION = "Set of tools for managing notebook power consumption."
SECTION = "base"
PRIORITY = "required"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                   file://apm.h;firstline=6;endline=18;md5=c9a1f79036ab14aa157e15ed75ffd769"
DEPENDS = "libtool-cross"
PR = "r0"

SRC_URI = "${DEBIAN_MIRROR}/main/a/apmd/apmd_3.2.2.orig.tar.gz \
           ${DEBIAN_MIRROR}/main/a/apmd/apmd_${PV}.diff.gz \
           file://unlinux.patch \
           file://init \
           file://default \
           file://apmd_proxy \
           file://apmd_proxy.conf"

S = "${WORKDIR}/apmd-3.2.2.orig"

inherit update-rc.d

INITSCRIPT_NAME = "apmd"
INITSCRIPT_PARAMS = "defaults"

do_compile() {
	# apmd doesn't use whole autotools. Just libtool for installation
	oe_runmake "LIBTOOL=${STAGING_BINDIR_CROSS}/${TARGET_PREFIX}libtool" apm apmd
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
