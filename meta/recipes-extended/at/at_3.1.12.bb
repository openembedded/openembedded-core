DESCRIPTION = "Delayed job execution and batch processing"
SECTION = "base"
LICENSE="GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=4325afd396febcb659c36b49533135d4"
DEPENDS = "flex libpam initscripts"
RCONFLICTS_${PN} = "atd"
RREPLACES_${PN} = "atd"
PR = "r2"

SRC_URI = "${DEBIAN_MIRROR}/main/a/at/at_${PV}.orig.tar.gz \
    file://configure.patch \
    file://nonrootinstall.patch \
    file://use-ldflags.patch \
    file://fix_parallel_build_error.patch \
    file://posixtm.c \
    file://posixtm.h \
    file://file_replacement_with_gplv2.patch \
    file://S99at"

EXTRA_OECONF += "ac_cv_path_SENDMAIL=/bin/true \
                 --with-daemon_username=root \
                 --with-daemon_groupname=root \
                 --with-jobdir=/var/spool/cron/atjobs \
                 --with-atspool=/var/spool/cron/atspool"

inherit autotools

do_compile_prepend () {
	cp -f ${WORKDIR}/posixtm.[ch] ${S}
}

do_install () {
	oe_runmake "IROOT=${D}" install

	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${sysconfdir}/rcS.d
	install -m 0755    ${WORKDIR}/S99at		${D}${sysconfdir}/init.d/at
	ln -sf		../init.d/at		${D}${sysconfdir}/rcS.d/S99at
}
