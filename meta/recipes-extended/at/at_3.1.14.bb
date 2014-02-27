SUMMARY = "Delayed job execution and batch processing"
DESCRIPTION = "At allows for commands to be run at a particular time.  Batch will execute commands when \
the system load levels drop to a particular level."
SECTION = "base"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=4325afd396febcb659c36b49533135d4"
DEPENDS = "flex flex-native \
           ${@base_contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"

VIRTUAL-RUNTIME_initscripts ?= "initscripts"                                                                                                                 
RDEPENDS_${PN} = "${@base_contains('DISTRO_FEATURES', 'pam', '${PAM_DEPS}', '', d)} \
                  ${VIRTUAL-RUNTIME_initscripts} \
"

PAM_DEPS = "libpam libpam-runtime pam-plugin-env pam-plugin-limits"

RCONFLICTS_${PN} = "atd"
RREPLACES_${PN} = "atd"

SRC_URI = "${DEBIAN_MIRROR}/main/a/at/at_${PV}.orig.tar.gz \
    file://fix_parallel_build_error.patch \
    file://posixtm.c \
    file://posixtm.h \
    file://file_replacement_with_gplv2.patch \
    file://S99at \
    file://atd.service \
    ${@base_contains('DISTRO_FEATURES', 'pam', '${PAM_SRC_URI}', '', d)}"

PAM_SRC_URI = "file://pam.conf.patch \
               file://configure-add-enable-pam.patch"

SRC_URI[md5sum] = "d41cfd79033b6e49a8838add59a42ac6"
SRC_URI[sha256sum] = "cd092bf05d29c25b286f55a960ce8b8c3c5beb571d86ed8eb1dfb3b61291b3ae"

EXTRA_OECONF += "ac_cv_path_SENDMAIL=/bin/true \
                 --with-daemon_username=root \
                 --with-daemon_groupname=root \
                 --with-jobdir=/var/spool/at/jobs \
                 --with-atspool=/var/spool/at/spool \
                 ac_cv_header_security_pam_appl_h=${@base_contains('DISTRO_FEATURES', 'pam', 'yes', 'no', d)} "

inherit autotools-brokensep systemd

SYSTEMD_SERVICE_${PN} = "atd.service"

PARALLEL_MAKE = ""

do_compile_prepend () {
	cp -f ${WORKDIR}/posixtm.[ch] ${S}
}

do_install () {
	oe_runmake -e "IROOT=${D}" install

	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${sysconfdir}/rcS.d
	install -m 0755    ${WORKDIR}/S99at		${D}${sysconfdir}/init.d/atd
	ln -sf ../init.d/atd ${D}${sysconfdir}/rcS.d/S99at

	# install systemd unit files
	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${WORKDIR}/atd.service ${D}${systemd_unitdir}/system
	sed -i -e 's,@SBINDIR@,${sbindir},g' ${D}${systemd_unitdir}/system/atd.service

	if [ "${@base_contains('DISTRO_FEATURES', 'pam', 'pam', '', d)}" = "pam" ]; then
		install -D -m 0644 ${WORKDIR}/${BP}/pam.conf ${D}${sysconfdir}/pam.d/atd
	fi
}
