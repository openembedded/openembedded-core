SUMMARY = "Subversion (svn) version control system client"
SECTION = "console/network"
DEPENDS = "apr-util neon sqlite3"
RDEPENDS_${PN} = "neon"
LICENSE = "Apache-2"
HOMEPAGE = "http://subversion.tigris.org"

BBCLASSEXTEND = "native"

PR = "r3"

SRC_URI = "http://subversion.tigris.org/downloads/${BPN}-${PV}.tar.bz2 \
           file://disable-revision-install.patch \
           file://libtool2.patch \
           file://fix-install-depends.patch \
           file://subversion-CVE-2013-1849.patch \
           file://subversion-CVE-2013-4505.patch \
           file://subversion-CVE-2013-1845.patch \
           file://subversion-CVE-2013-1847-CVE-2013-1846.patch \
           file://subversion-CVE-2013-4277.patch \
           file://subversion-CVE-2014-3522.patch \
           file://subversion-CVE-2014-3528.patch \
"

SRC_URI[md5sum] = "113fca1d9e4aa389d7dc2b210010fa69"
SRC_URI[sha256sum] = "b2919d603a5f3c19f42e3265c4b930e2376c43b3969b90ef9c42b2f72d5aaa45"

LIC_FILES_CHKSUM = "file://COPYING;md5=2a69fef414e2cb907b4544298569300b"

PACKAGECONFIG[sasl] = "--with-sasl,--without-sasl,cyrus-sasl"

EXTRA_OECONF = " \
                --without-berkeley-db --without-apxs --without-apache \
                --without-swig --with-apr=${STAGING_BINDIR_CROSS} \
                --with-apr-util=${STAGING_BINDIR_CROSS} \
                ac_cv_path_RUBY=none"

inherit autotools

export LDFLAGS += " -L${STAGING_LIBDIR} "

acpaths = "-I build/ -I build/ac-macros/"

do_configure_prepend () {
	rm -f ${S}/libtool
	rm -f ${S}/build/libtool.m4
	sed -i -e 's:with_sasl="/usr/local":with_sasl="${STAGING_DIR}":' ${S}/build/ac-macros/sasl.m4
}
