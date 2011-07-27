DESCRIPTION = "Lightweight high-performance web server"
HOMEPAGE = "http://www.lighttpd.net/"
BUGTRACKER = "http://redmine.lighttpd.net/projects/lighttpd/issues"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=e4dac5c6ab169aa212feb5028853a579"


SECTION = "net"
DEPENDS = "zlib libpcre"
RDEPENDS_${PN} += " \
               lighttpd-module-access \
               lighttpd-module-accesslog \
               lighttpd-module-indexfile \
               lighttpd-module-dirlisting \
               lighttpd-module-staticfile \
"

PR = "r0"

SRC_URI = "http://download.lighttpd.net/lighttpd/releases-1.4.x/lighttpd-${PV}.tar.bz2 \
        file://index.html \
        file://lighttpd.conf \
        file://lighttpd \
"

SRC_URI[md5sum] = "e6e67b09986cb504db630b5a86b2dd76"
SRC_URI[sha256sum] = "afd8af213e65afb48b397d004696c6620a7ae7f3d4f162ec46a04b9b9dbfd599"

EXTRA_OECONF = " \
             --without-bzip2 \
             --without-ldap \
             --without-lua \
             --without-memcache \
             --with-pcre \
             --without-webdav-props \
             --without-webdav-locks \
             --without-openssl \
             --disable-static \
"

inherit autotools pkgconfig update-rc.d gettext

INITSCRIPT_NAME = "lighttpd"
INITSCRIPT_PARAMS = "defaults 70"

do_install_append() {
    install -d ${D}${sysconfdir}/init.d ${D}/www/logs ${D}/www/pages/dav ${D}/www/var
    install -m 0755 ${WORKDIR}/lighttpd ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/lighttpd.conf ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/index.html ${D}/www/pages/
}

FILES_${PN} += "${sysconfdir} /www"

CONFFILES_${PN} = "${sysconfdir}/lighttpd.conf"

PACKAGES_DYNAMIC = "lighttpd-module-*"

python populate_packages_prepend () {
        lighttpd_libdir = bb.data.expand('${libdir}', d)
        do_split_packages(d, lighttpd_libdir, '^mod_(.*)\.so$', 'lighttpd-module-%s', 'Lighttpd module for %s', extra_depends='')
}
