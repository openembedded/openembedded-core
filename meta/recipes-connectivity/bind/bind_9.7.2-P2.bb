DESCRIPTION = "ISC Internet Domain Name Server"
HOMEPAGE = "http://www.isc.org/sw/bind/"
SECTION = "console/network"

LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=fe11ac92dcbd84134d91b6e2c2eccab5"

DEPENDS = "openssl"
PR = "r0"

SRC_URI = "ftp://ftp.isc.org/isc/bind9/${PV}/${PN}-${PV}.tar.gz \
	   file://conf.patch \
           file://cross-build-fix.patch \
	   "

# --enable-exportlib is necessary for building dhcp
EXTRA_OECONF = " --enable-ipv6=no --with-randomdev=/dev/random --disable-threads \
                 --disable-devpoll --disable-epoll \
                 --sysconfdir=${sysconfdir}/bind \
                 --with-openssl=${STAGING_LIBDIR}/.. --with-libxml2=${STAGING_LIBDIR}/.. \
                 --enable-exportlib --with-export-includedir=${includedir} --with-export-libdir=${libdir} \
               "
inherit autotools update-rc.d

INITSCRIPT_NAME = "bind"
INITSCRIPT_PARAMS = "defaults"

PARALLEL_MAKE = ""

PACKAGES_prepend = "${PN}-utils "
FILES_${PN}-utils = "${bindir}/host ${bindir}/dig ${bindir}/nslookup"
FILES_${PN}-dev += "${bindir}/isc-config.h"

do_install_append() {
	rm "${D}/usr/bin/nslookup"
	install -d "${D}/etc/bind"
	install -d "${D}/etc/init.d"
	install -m 644 ${S}/conf/* "${D}/etc/bind"
	install -m 755 "${S}/init.d" "${D}/etc/init.d/bind"
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

SRC_URI[md5sum] = "e74de6ec9c2cd27576794c873bd85f4a"
SRC_URI[sha256sum] = "e6d5938184066fc793c28ff975e09e9721116aede2a2d6d93b1be5e8654a5c8a"
