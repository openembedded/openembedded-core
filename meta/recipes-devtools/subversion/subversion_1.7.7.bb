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
"
SRC_URI[md5sum] = "5a42b7d0f1366a8d60f9ad7d5890295d"
SRC_URI[sha256sum] = "fc85a9341d1dc275b279e470776014b02328a028e48a7ac7868ac07d4a40a321"

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
