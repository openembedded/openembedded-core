SUMMARY = "ISC Internet Domain Name Server"
HOMEPAGE = "http://www.isc.org/sw/bind/"
SECTION = "console/network"

LICENSE = "ISC & BSD"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=a3df5f651469919a0e6cb42f84fb6ff1"

DEPENDS = "openssl libcap"

SRC_URI = "ftp://ftp.isc.org/isc/bind9/${PV}/${BPN}-${PV}.tar.gz \
           file://conf.patch \
           file://cross-build-fix.patch \
           file://make-etc-initd-bind-stop-work.patch \
           file://mips1-not-support-opcode.diff \
           file://dont-test-on-host.patch \
           file://init.d-add-support-for-read-only-rootfs.patch \
	   "

SRC_URI[md5sum] = "e676c65cad5234617ee22f48e328c24e"
SRC_URI[sha256sum] = "d4b64c1dde442145a316679acff2df4008aa117ae52dfa3a6bc69efecc7840d1"

# --enable-exportlib is necessary for building dhcp
ENABLE_IPV6 = "--enable-ipv6=${@base_contains('DISTRO_FEATURES', 'ipv6', 'yes', 'no', d)}"
EXTRA_OECONF = " ${ENABLE_IPV6} --with-randomdev=/dev/random --disable-threads \
                 --disable-devpoll --disable-epoll --with-gost=no \
                 --with-gssapi=no --with-ecdsa=yes \
                 --sysconfdir=${sysconfdir}/bind \
                 --with-openssl=${STAGING_LIBDIR}/.. --with-libxml2=${STAGING_LIBDIR}/.. \
                 --enable-exportlib --with-export-includedir=${includedir} --with-export-libdir=${libdir} \
               "
inherit autotools-brokensep update-rc.d

INITSCRIPT_NAME = "bind"
INITSCRIPT_PARAMS = "defaults"

PARALLEL_MAKE = ""

RDEPENDS_${PN} = "python-core"

PACKAGES_prepend = " ${PN}-utils "
FILES_${PN}-utils = "${bindir}/host ${bindir}/dig"
FILES_${PN}-dev += "${bindir}/isc-config.h"

do_install_append() {
	rm "${D}${bindir}/nslookup"
	rm "${D}${mandir}/man1/nslookup.1"
	rmdir "${D}${localstatedir}/run"
	rmdir --ignore-fail-on-non-empty "${D}${localstatedir}"
	install -d "${D}${localstatedir}/cache/bind"
	install -d "${D}${sysconfdir}/bind"
	install -d "${D}${sysconfdir}/init.d"
	install -m 644 ${S}/conf/* "${D}${sysconfdir}/bind/"
	install -m 755 "${S}/init.d" "${D}${sysconfdir}/init.d/bind"
	sed -i -e '1s,#!.*python,#! /usr/bin/env python,' ${D}${sbindir}/dnssec-coverage ${D}${sbindir}/dnssec-checkds
}

CONFFILES_${PN} = " \
	${sysconfdir}/bind/named.conf \
	${sysconfdir}/bind/named.conf.local \
	${sysconfdir}/bind/named.conf.options \
	${sysconfdir}/bind/db.0 \
	${sysconfdir}/bind/db.127 \
	${sysconfdir}/bind/db.empty \
	${sysconfdir}/bind/db.local \
	${sysconfdir}/bind/db.root \
	"

