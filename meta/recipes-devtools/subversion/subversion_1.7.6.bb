DESCRIPTION = "The Subversion (svn) client"
SECTION = "console/network"
DEPENDS = "apr-util neon sqlite3"
RDEPENDS_${PN} = "neon"
LICENSE = "Apache-2"
HOMEPAGE = "http://subversion.tigris.org"

BBCLASSEXTEND = "native"

inherit gettext

SRC_URI = "${APACHE_MIRROR}/${BPN}/${BPN}-${PV}.tar.bz2 \
           file://libtool2.patch \
           file://fix-install-depends.patch \
           file://subversion-1.7.6_mod_dontdothat_svnserve_only.patch \
"
SRC_URI[md5sum] = "4baa434db7709bb059b05d02a2547663"
SRC_URI[sha256sum] = "d1a4f13c5c992a1527a2aeac262c6f54e073963ce1d13abfabb2a90980fa7d05"

LIC_FILES_CHKSUM = "file://LICENSE;md5=4a14fd2da3134e40a087eb4326a4ecd4"

EXTRA_OECONF = " \
                --without-berkeley-db --without-apxs \
                --without-swig --with-apr=${STAGING_BINDIR_CROSS} \
                --with-apr-util=${STAGING_BINDIR_CROSS} \
                ac_cv_path_RUBY=none"

inherit autotools

export LDFLAGS += " -L${STAGING_LIBDIR} "

acpaths = "-I build/ -I build/ac-macros/"

do_configure_prepend () {
	rm -f ${S}/libtool
	rm -f ${S}/build/libtool.m4 ${S}/build/ltmain.sh ${S}/build/ltoptions.m4 ${S}/build/ltsugar.m4 ${S}/build/ltversion.m4 ${S}/build/lt~obsolete.m4
	rm -f ${S}/aclocal.m4
	sed -i -e 's:with_sasl="/usr/local":with_sasl="${STAGING_DIR}":' ${S}/build/ac-macros/sasl.m4
}
