SECTION = "base"
DESCRIPTION = "This package provides the necessary \
infrastructure for basic TCP/IP based networking."
LICENSE = "GPL"
PR = "r7"

inherit update-rc.d

INITSCRIPT_NAME = "networking"
INITSCRIPT_PARAMS = "start 40 S . stop 40 0 6 1 ."
# On MNCI etc, start very late so that our own apps come up faster
INITSCRIPT_PARAMS_openmn = "start 85 1 2 3 4 5 . stop 85 0 6 1 ."
# On OpenSlug delay the stop until after network apps have exited
# Do not stop in single user - there's no way to sulogin!
INITSCRIPT_PARAMS_openslug = "start 40 S 0 6 ."

SRC_URI = "${DEBIAN_MIRROR}/main/n/netbase/netbase_${PV}.tar.gz \
           file://options \
           file://init \
           file://hosts \
           file://interfaces"

do_install () {
	install -d ${D}${sysconfdir}/init.d \
		   ${D}${sbindir} \
		   ${D}${mandir}/man8 \
		   ${D}${sysconfdir}/network/if-pre-up.d \
		   ${D}${sysconfdir}/network/if-up.d \
		   ${D}${sysconfdir}/network/if-down.d \
		   ${D}${sysconfdir}/network/if-post-down.d
	install -m 0644 ${WORKDIR}/options ${D}${sysconfdir}/network/options
	install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/networking
	install -m 0644 ${WORKDIR}/hosts ${D}${sysconfdir}/hosts
	install -m 0644 etc-rpc ${D}${sysconfdir}/rpc
	install -m 0644 etc-protocols ${D}${sysconfdir}/protocols
	install -m 0644 etc-services ${D}${sysconfdir}/services
	install -m 0755 update-inetd ${D}${sbindir}/
	install -m 0644 update-inetd.8 ${D}${mandir}/man8/
	install -m 0644 ${WORKDIR}/interfaces ${D}${sysconfdir}/network/interfaces
}

CONFFILES_${PN} = "${sysconfdir}/network/options ${sysconfdir}/hosts ${sysconfdir}/network/interfaces"
