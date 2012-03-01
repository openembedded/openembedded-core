SUMMARY = "Basic TCP/IP networking support"
DESCRIPTION = "This package provides the necessary infrastructure for basic TCP/IP based networking"
HOMEPAGE = "http://packages.debian.org/netbase"
SECTION = "base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://debian/copyright;md5=3dd6192d306f582dee7687da3d8748ab"
PR = "r1"

inherit update-rc.d

INITSCRIPT_NAME = "networking"
INITSCRIPT_PARAMS = "start 40 S . stop 40 0 6 1 ."

SRC_URI = "${DEBIAN_MIRROR}/main/n/netbase/netbase_${PV}.tar.gz \
           file://init \
           file://hosts \
           file://interfaces \
           file://nfsroot"

SRC_URI[md5sum] = "fcd87a62f177592724a8066e3774ae97"
SRC_URI[sha256sum] = "a7e6525c9a09b00eff3395d09739b3c70a18bff8d13f7daa21fdf81989387726"

do_install () {
	install -d ${D}${sysconfdir}/init.d \
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
	install -m 0644 ${WORKDIR}/interfaces ${D}${sysconfdir}/network/interfaces
	install -m 0755 ${WORKDIR}/nfsroot ${D}${sysconfdir}/network/if-pre-up.d

	# Disable network manager on machines that commonly do NFS booting
	case "${MACHINE}" in
		"qemuarm" | "qemux86" | "qemux86-64" | "qemumips" | "qemuppc" )
			touch ${D}${sysconfdir}/network/nm-disabled-eth0
			;;
		*)
			;;
	esac
}

CONFFILES_${PN} = "${sysconfdir}/hosts ${sysconfdir}/network/interfaces"
