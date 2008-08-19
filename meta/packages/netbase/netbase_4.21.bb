DESCRIPTION = "This package provides the necessary \
infrastructure for basic TCP/IP based networking."
SECTION = "base"
LICENSE = "GPL"
PR = "r22"

inherit update-rc.d

INITSCRIPT_NAME = "networking"
INITSCRIPT_PARAMS = "start 40 S . stop 40 0 6 1 ."
# On MNCI etc, start very late so that our own apps come up faster
INITSCRIPT_PARAMS_openmn = "start 85 1 2 3 4 5 . stop 85 0 6 1 ."
# On SlugOS (NSLU2) delay the stop until after network apps have exited
# Do not stop in single user - there's no way to sulogin!
INITSCRIPT_PARAMS_slugos = "start 42 S 0 6 ."

SRC_URI = "${DEBIAN_MIRROR}/main/n/netbase/netbase_${PV}.tar.gz \
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
	install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/networking
	install -m 0644 ${WORKDIR}/hosts ${D}${sysconfdir}/hosts
	install -m 0644 etc-rpc ${D}${sysconfdir}/rpc
	install -m 0644 etc-protocols ${D}${sysconfdir}/protocols
	install -m 0644 etc-services ${D}${sysconfdir}/services
	install -m 0755 update-inetd ${D}${sbindir}/
	install -m 0644 update-inetd.8 ${D}${mandir}/man8/
	install -m 0644 ${WORKDIR}/interfaces ${D}${sysconfdir}/network/interfaces

	# Disable network manager on machines that commonly do NFS booting
	case "${MACHINE}" in
		"omap-3430sdp" | "omap-3430ldp" | "omap-2430sdp")
			touch ${D}${sysconfdir}/network/nm-disabled-eth0
			;;
		*)
			;;
	esac
}

CONFFILES_${PN} = "${sysconfdir}/hosts ${sysconfdir}/network/interfaces"

PACKAGE_ARCH_omap-3430sdp = "${MACHINE_ARCH}"
PACKAGE_ARCH_omap-3430ldp = "${MACHINE_ARCH}"
PACKAGE_ARCH_omap-2430sdp = "${MACHINE_ARCH}"
